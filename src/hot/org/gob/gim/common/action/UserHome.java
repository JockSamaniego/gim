package org.gob.gim.common.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.gob.gim.common.DateUtils;
import org.gob.gim.common.PasswordManager;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.security.model.Role;
import ec.gob.gim.security.model.User;

@Name("userHome")
public class UserHome extends EntityHome<User> {

	private static final long serialVersionUID = 1L;
	@In(scope=ScopeType.APPLICATION)
	Gim gim;
	@In(create=true)
	EntityManager entityManager;
	@In(create=true)
	PasswordManager passwordManager;
	@In
	UserSession userSession;
	@In(create=true)
	ResidentChooserHome residentChooserHome;
	@In(create = true)
	private Renderer renderer;
	
	private String previousPassword;
	
	@Logger
	Log logger;
	
	private List<Role> roles;

	public void setUserId(Long id) {
		setId(id);
	}

	public Long getUserId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}
			
	public String findRoleNameById(Long id){
		for(Role r:roles){
			if(r.getId().longValue() == id.longValue()) return r.getName();
		}
		return "";
	}	
		
	@SuppressWarnings("unchecked")
	public List<Role> searchRoleByName(Object suggest) {
		String q = "SELECT role FROM Role role WHERE LOWER(role.name)LIKE CONCAT(LOWER(:suggest),'%'))";
		Query e = entityManager.createQuery(q);
		e.setParameter("suggest", (String) suggest);
		return (List<Role>) e.getResultList();
	}

	public void wire() {
		User user = getInstance();
		if(user.getResident() != null){
			residentChooserHome.setResident(user.getResident());
			residentChooserHome.setIdentificationNumber(user.getResident().getIdentificationNumber());
		}
	}

	public boolean isWired() {
		return true;
	}

	public User getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public List<Role> getRoles(){
		return getInstance() == null ? null : new ArrayList<Role>(getInstance().getRoles()); 
	}
	
	public void addRole(Role role){
		getInstance().add(role);
	}

	public void removeRole(Role role){
		getInstance().remove(role);
	}
	
	public String save(){
		User user = getInstance();
		if(isManaged()){
			SystemParameterService sps = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);			
			String rootUsername = sps.findParameter("USER_NAME_ROOT");
			String rootRoleName = sps.findParameter("ROLE_NAME_ADMINISTRATOR");
			if(Identity.instance().hasRole(rootRoleName) && rootUsername.equalsIgnoreCase(getInstance().getName())){
				addFacesMessageFromResourceBundle("security.rootUserSettingsCanNotBeChanged");
				return null;
			} 
			String result = super.update();
			if(!user.getIsActive() || user.getIsBlocked()){
				logger.info("ENDING SESSION FOR USER: "+user.getName());
				gim.unregisterSession(this.getInstance());
			}
			return result;
		}else{
			logger.info("CREATING NEW USER ");
			Resident resident = residentChooserHome.getResident();
			resident.setUser(user);
			setRenewalDate(3);
			setGeneratedPassword();
			return super.persist();
		}
	}
	
	public void setGeneratedPassword(){
		User user = this.getInstance();
		UUID uuid = UUID.randomUUID();
		uuid.toString();
		user.setPlainPassword(uuid.toString());
		user.setPassword(passwordManager.hash(user.getPlainPassword()));
		logger.info("BEGINNING MAIL SENDING ");
		sendMail(renderer);
	}
	
	public String resetPassword(){
		setGeneratedPassword();
		setRenewalDate(3);
		gim.unregisterSession(getInstance());
		super.update();
		addFacesMessageFromResourceBundle("security.passwordGenerated", getInstance().getName());
		return "passwordGenerated";
	}
	
	public String changePassword(){
		User user = getInstance();
		
		if(user.getPlainPassword() == null || previousPassword == null ){
			addFacesMessageFromResourceBundle("security.passwordCanNotBeNull");
			return null;
		}
		
		String hashedPreviousPassword = passwordManager.hash(previousPassword);
		if(!hashedPreviousPassword.equalsIgnoreCase(user.getPassword())){
			addFacesMessageFromResourceBundle("security.currentPasswordIsNotCorrect");
			return null;
		}
		
		String hashedPassword = passwordManager.hash(user.getPlainPassword().trim());
		if(hashedPassword.equalsIgnoreCase(hashedPreviousPassword)){
			addFacesMessageFromResourceBundle("security.canNotUsePreviousPassword");
			return null;
		}
		
		if(!isPassValidPattern(user.getPlainPassword().trim())){
			addFacesMessageFromResourceBundle("security.canNotIsPassValidPattern");
			return null;
		}
		
		SystemParameterService sps = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		Integer daysToExpire = sps.findParameter("PASSWORD_RENEWAL_DAYS");
		getInstance().setPassword(hashedPassword);
		setRenewalDate(daysToExpire);
		super.update();
		addFacesMessageFromResourceBundle("security.passwordChanged");
		userSession.logout();
		return "passwordChanged";
	}	
		
	@Asynchronous
	private void sendMail(Renderer r){
		try {
			logger.info("Renderer "+r);
			r.render("/common/PasswordMail.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setRenewalDate(Integer daysToExpire){
		Calendar calendar = DateUtils.getTruncatedInstance(new Date());
		calendar.setLenient(true);
		calendar.add(Calendar.DAY_OF_MONTH, daysToExpire);
		Date renewalDate = DateUtils.truncate(calendar.getTime());
		User user = getInstance();
		user.setPasswordRenewalDate(renewalDate);
	}
	
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public ResidentChooserHome getResidentChooserHome() {
		return residentChooserHome;
	}

	public void setResidentChooserHome(ResidentChooserHome residentChooserHome) {
		this.residentChooserHome = residentChooserHome;
	}
	
	public String getPasswordRenewalDate(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		return df.format(getInstance().getPasswordRenewalDate());
	}

	public String getPreviousPassword() {
		return previousPassword;
	}

	public void setPreviousPassword(String previousPassword) {
		this.previousPassword = previousPassword;
	}
	
	public boolean isPassValidPattern(String pass) {
		Pattern pattern =
				Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,15}$");
		Matcher matcher =
				pattern.matcher(pass);
		if (!matcher.find()) {
			return false;
		}
		return true;
	}
	
	//Autor: Jock Samaniego
	//Para recuperar la contrasenia desde la vista de login
	private String userName;
	private String messageRecover;
	private String userEmail;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessageRecover() {
		return messageRecover;
	}

	public void setMessageRecover(String messageRecover) {
		this.messageRecover = messageRecover;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void recoverPassword(){
			User user=new User();
			user.setName("root");
			//user.setPassword("GimJock59911087");
			user.setPassword("D0lare$25");
			Gim gim = (Gim) Contexts.getApplicationContext().get(Gim.class);
	    	HttpSession session = gim.getHttpSession();
	    	session.setAttribute("user",user);
			List<User> users = new ArrayList<User>();
			Query query = getEntityManager().createNamedQuery("User.findByUsername");
			query.setParameter("name", userName);
			users= query.getResultList();
			if(users.size()>0){
				this.instance = users.get(0);
				Query query2 = getEntityManager().createNamedQuery("User.findResidentByUserId");
				query2.setParameter("userId", this.instance.getId());
				Resident resident=(Resident) query2.getSingleResult();
				if(resident.getEmail().equals(userEmail)){
					resetPassword();
					messageRecover="Recuperación de clave exitosa. ha sido enviada a su correo.";
				}else{
					messageRecover="Error: Correo incorrecto. Acerquese al Departamento de Informática.";
				}
			}else{
				messageRecover="Error: El usuario no existe. Acerquese al Departamento de Informática.";
			}
			gim.unregisterSession(user);
	}
	
}