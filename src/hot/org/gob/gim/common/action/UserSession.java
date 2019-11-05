package org.gob.gim.common.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.async.Asynchronous;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.TillPermission;
import ec.gob.gim.parking.model.Journal;
import ec.gob.gim.security.model.PermissionComparator;
import ec.gob.gim.security.model.Permission;
import ec.gob.gim.security.model.Role;
import ec.gob.gim.security.model.SessionRecord;
import ec.gob.gim.security.model.SessionRecordType;
import ec.gob.gim.security.model.User;

@Name("userSession")
@Scope(ScopeType.SESSION)
public class UserSession extends EntityController{

	private static final long serialVersionUID = 1L;
	public static final String COMPONENT_NAME = "userSession";
	private static final String GIM_PREFIX = "/gim";

	public static final String ROLE_NAME_COMPENSATION_CASHIER = "ROLE_NAME_COMPENSATION_CASHIER";
	public static final String ROLE_NAME_INCOME_BOSS = "ROLE_NAME_INCOME_BOSS";
	public static final String ROLE_NAME_REVENUE_BOSS = "ROLE_NAME_REVENUE_BOSS";
	public static final String ROLE_NAME_REVENUE_CERTIFICATE = "ROLE_NAME_REVENUE_CERTIFICATE";
	public static final String ROLE_NAME_DETAIL_CHECKER = "ROLE_NAME_DETAIL_CHECKER";
	public static final String ROLE_NAME_EDITION_READING = "ROLE_NAME_EDITION_READING";
	
	
	private static final long ROOT_PARENT_ID = 0L;

	private Map<Long, Map<Long, Permission>> permissions = new HashMap<Long, Map<Long, Permission>>();
	private Map<String, Permission> linkPermissions = new HashMap<String, Permission>();
	private SessionRecordType logoutType = SessionRecordType.TIMEOUT_LOGOUT;

	private FiscalPeriod fiscalPeriod;
	private List<FiscalPeriod> fiscalPeriods;
	private Map<String, String> unauthorizedInformation = new HashMap<String, String>();
	
	@Logger
	Log logger;

	@In(create = true)
	private Renderer renderer;
	
	@In(scope=ScopeType.APPLICATION)
	Gim gim;

    public Gim getGim() {
        return gim;
    }

    public void setGim(Gim gim) {
        this.gim = gim;
    }

        
        
	/**
	 * Horario de trabajo registrado para el caso de operadores de parqueadero
	 */
	private Journal journal;

	private User user;

	private TillPermission tillPermission;

	private Person person;
	
	private Integer numberMunicipalBondsFormalizing;

	public UserSession() {
		permissions.put(ROOT_PARENT_ID, new HashMap<Long, Permission>());
	}
	
	@Observer(value = "org.jboss.seam.security.loginSuccessful")
	public void postLogin() {
		//logger.info("POST LOGIN");
		HttpSession session = gim.getHttpSession();
		user.setIpAddress(getIpAddress());
		session.setAttribute("user", user);
		//logger.info("SESSION User ATTRIBUTE SET");
		saveSessionRecord(SessionRecordType.LOGIN);
		loadFiscalPeriod();
		loadRestrictedMenu();
		deletePDFFiles();
		Date today = new Date();
		Date renewalDate = user.getPasswordRenewalDate();
		if(renewalDate != null && renewalDate.after(today)){
			loadMenu();
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			addFacesMessageFromResourceBundle("security.pleaseChangeYourPassword");
			try {
				context.getExternalContext().redirect("/gim/common/ChangePassword.seam");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void logout(){
		logout(SessionRecordType.LOGOUT);
	}
	
	public void logout(SessionRecordType logoutType){
		//if(user != null){
		if(Identity.instance().getPrincipal() != null){
			setLogoutType(logoutType);
			gim.unregisterSession(user);
			Identity.instance().logout();
			//System.out.println("org.jboss.seam.security.loggedOut ----> POST LOGOUT");
			saveSessionRecord(logoutType);
			permissions.clear();
			linkPermissions.clear();
			setUser(null);
			setNumberMunicipalBondsFormalizing(null);
		}
	}
	
	private void saveSessionRecord(SessionRecordType sessionRecordType){
		Date date = new Date();
		SessionRecord sr = new SessionRecord();
		sr.setUserName(user.getName());
		sr.setDate(date);
		sr.setTime(date);
		sr.setIpAddress(user.getIpAddress());
		sr.setSessionRecordType(sessionRecordType);
		ServiceLocator locator = ServiceLocator.getInstance();
		CrudService service = locator.findResource(CrudService.LOCAL_NAME);
		service.create(sr);
	}
	
	private String getIpAddress(){
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest)context.getRequest();
        String ipAddress = request.getHeader("X-Forwarded-For");
        logger.info("<<<IP FOR USER>>>:", user.getName()+"  :  "+ipAddress);
        if (isIPAddress(ipAddress)){
            //System.out.println("IP Return:"+ipAddress);
        	return ipAddress;
        } else{
        	ipAddress = request.getRemoteAddr();
            //System.out.println("RemoteAddr:"+ipAddress);
        	//logger.info("<<<RemoteAddr>>>:", user.getName()+"  :  "+ipAddress);
            //System.out.println("\n\n\n\n\n");
        	return ipAddress;
        }
//        
//        System.out.println("\n\n\n\n\n");
//        String ipAddress = request.getRemoteAddr();
//		return ipAddress;
	}

	private boolean isIPAddress(String ip){
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
        	return false;
        }
        Pattern pattern = null;
        Matcher matcher;
        
        String IPADDRESS_PATTERN = 
    		"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
    		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
    		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
    		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
     
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();	    	    
	}
	
	@SuppressWarnings("unchecked")
	private void loadRestrictedMenu(){
		Query query = getEntityManager().createNamedQuery("Permission.findCommonPermissions");
		List<Permission> permissions = query.getResultList();
		
		for (Permission permission : permissions) {
			linkPermissions.put(GIM_PREFIX + permission.getAction().getUrl(), permission);
		}		
	}
	
	@SuppressWarnings("unchecked")
	private void loadMenu(){
		Long userId = getUser().getId();
		Query query = getEntityManager().createNamedQuery("Role.findByUserId");
		query.setParameter("userId", userId);
		List<Role> roles = query.getResultList();

		if (roles.size() > 0) {
			List<Long> roleIds = new ArrayList<Long>();
			for (Role r : roles) {
				roleIds.add(r.getId());
			}

			query = getEntityManager().createNamedQuery("Permission.findByRoleIds");
			query.setParameter("roleIds", roleIds);
			List<Permission> availablePermissions = query.getResultList();

			for (Permission permission : availablePermissions) {
				if (permission.getAction() != null && permission.getAction().getUrl() != null) {
					linkPermissions.put(GIM_PREFIX + permission.getAction().getUrl(), permission);
					Map<String, String> params =  getParameters(permission.getAction().getUrl());
					permission.getAction().setParameters(params);
				}

				if (permission.getVisibleFromMenu()) {
					Long parentId = ROOT_PARENT_ID;
					if (permission.getAction().getParent() != null) {
						parentId = permission.getAction().getParent().getId();
					}
					
					Map<Long, Permission> permissionMap = permissions.get(parentId);
					if (permissionMap == null) {
						permissionMap = new HashMap<Long, Permission>();
						permissions.put(parentId, permissionMap);
					}
					permissionMap.put(permission.getAction().getId(),permission);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadFiscalPeriod() {
		/*Date currentDate = new Date();
		Query query = getEntityManager().createNamedQuery("FiscalPeriod.findCurrent");
		query.setParameter("currentDate", currentDate);
		fiscalPeriod = (FiscalPeriod) query.getSingleResult();
		query = getEntityManager().createNamedQuery("FiscalPeriod.findAll");
		fiscalPeriods = query.getResultList();*/
		//System.out.println("ya no accesa a la base de datos................para cada usuario 2 menos");
		fiscalPeriod = gim.getFiscalPeriod();
		fiscalPeriods = gim.getFiscalPeriods();
	}

	@Factory(value = "fiscalPeriods")
	public List<FiscalPeriod> findFiscalPeriods() {
		return fiscalPeriods;
	}

	public Boolean hasRole(String roleNameParameter) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		String roleName = systemParameterService.findParameter(roleNameParameter);
		return Identity.instance().hasRole(roleName);
	}
	
	public Boolean hasRoleByNameRol(String roleName) {
		return Identity.instance().hasRole(roleName);
	}


	
	public void notifySecurityViolation(String resourceName) {
		//System.out.print(renderer);
		sendMail(resourceName, renderer);
	}
	
	@Asynchronous
	private void sendMail(String resourceName, Renderer r){
		try {
			if(person.getEmail() != null){
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				unauthorizedInformation.put("DATE", df.format(new Date()));
				unauthorizedInformation.put("URL", resourceName);
				r.render("/common/SecurityMail.xhtml");
				addFacesMessageFromResourceBundle("#{messages['mailSent']}");
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			addFacesMessageFromResourceBundle("#{messages['mailNotSent']}");
		}		
	}
	
	public List<Permission> getPermissions(Long parentActionId) {
		List<Permission> ps = new LinkedList<Permission>();
		if (permissions.get(parentActionId) != null) {
			ps.addAll(permissions.get(parentActionId).values());
		}		
		Collections.sort(ps, new PermissionComparator());
		return ps;
	}

	public Permission findPermission(String checkedUrl) {
		checkedUrl = checkedUrl.toLowerCase();
		for (Entry<String, Permission> entry : linkPermissions.entrySet()) {
			if (entry.getKey() != null && entry.getKey().toLowerCase().contains(checkedUrl)) {
				return entry.getValue();
			}
		}
		return null;
	}
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	public Integer getNumberMunicipalBondsFormalizing() {
		return numberMunicipalBondsFormalizing;
	}

	public void setNumberMunicipalBondsFormalizing(
			Integer numberMunicipalBondsFormalizing) {
		this.numberMunicipalBondsFormalizing = numberMunicipalBondsFormalizing;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public Journal getJournal() {
		return journal;
	}

	public void setTillPermission(TillPermission tillPermission) {
		this.tillPermission = tillPermission;
	}

	public TillPermission getTillPermission() {
		return tillPermission;
	}

	public Map<String, String> getUnauthorizedInformation() {
		return unauthorizedInformation;
	}
	
	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}
	
	
	
	private Map<String, String> getParameters(String url){
		
		Map<String, String> parameters = new HashMap<String, String>();
		if(url.indexOf("?") > 0 && url.indexOf("?") < url.length()){
			String searchURL = url.substring(url.indexOf("?") + 1);
			String parsedUrl[] = searchURL.split("&");
			for (String pair : parsedUrl) {
				String temp[] = pair.split("=");
				try {
					parameters.put(temp[0], java.net.URLDecoder.decode(temp[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return parameters;
	}

	public SessionRecordType getLogoutType() {
		return logoutType;
	}

	public void setLogoutType(SessionRecordType logoutType) {
		this.logoutType = logoutType;
	}
	
	@Observer("org.jboss.seam.preDestroyContext.SESSION")
	public void preSessionDestroy() {
		//System.out.println("\n\n\n\n\n\nLISTENER preSessionDestroy "+this);
		try{
			UserSession userSession = (UserSession) this;
			if (userSession != null){
				//System.out.println("LISTENER VALUE UNBOUND ");
				
				if(SessionRecordType.TIMEOUT_LOGOUT.equals(userSession.getLogoutType())){
					//System.out.println("IS TIMEOUT LOGOUT");
					logger.info("IS TIMEOUT LOGOUT>>>>> ", userSession.getUser().getName());
					userSession.logout(SessionRecordType.TIMEOUT_LOGOUT);
					//userSession.setLogoutType(SessionRecordType.TIMEOUT_LOGOUT);
					//((UserSession)this).postLogout();
				}
			}
		} catch (NullPointerException e){
			logger.error("LISTENER VALUE UNBOUND NullPointerException ", e.toString());
			//System.out.println("LISTENER VALUE UNBOUND NullPointerException");
		}
	}
			
	public void deletePDFFiles(){
		FacesContext fcontext = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) fcontext.getExternalContext().getContext();
		String realPath = scontext.getRealPath("/");
		String filePath = realPath + "PDFDocuments";
		File path = new File(filePath);
		File[] files = path.listFiles();
		String userPdf = user.getResident().getId() + ".pdf";
		try{
			for (int i = 0; i < files.length; i++) {
	        	if (files[i].getName().indexOf(userPdf) > 0) {
	                files[i].delete();
	            }
	        }	
		}catch(Exception e){
			//e.printStackTrace();
		}		
        
	}

}