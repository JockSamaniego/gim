package org.gob.gim.action;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;

import org.gob.gim.common.PasswordManager;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.Gim;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.Till;
import ec.gob.gim.income.model.TillPermission;
import ec.gob.gim.parking.model.Journal;
import ec.gob.gim.security.model.Role;
import ec.gob.gim.security.model.User;

@Stateless
@Name("authenticator")
public class AuthenticatorBean implements Authenticator {

	@Logger
	private Log log;

	@In
	Identity identity;
	@In
	Credentials credentials;

	@In(create = true)
	UserSession userSession;

	@In(scope = ScopeType.APPLICATION)
	Gim gim;

	@PersistenceContext
	private EntityManager entityManager;

	@In
	FacesMessages facesMessages;

	@In(create = true)
	private PasswordManager passwordManager;

	private String ip;

	public boolean authenticate() {

		return isUserValid();
	}

	@SuppressWarnings({ "unchecked" })
	private Boolean isUserValid() {
		String hashPassword = passwordManager.hash(credentials.getPassword());
		log.info("Authenticating user {0}, {1}", credentials.getUsername(), hashPassword);
		//System.out.println("Authenticating user: " + credentials.getUsername() + " " + hashPassword);
		Query query = entityManager
				.createNamedQuery("User.findByUsernameAndPassword");
		query.setParameter("name", credentials.getUsername());
		query.setParameter("password", hashPassword);
		User user = null;
		try {
			user = (User) query.getSingleResult();
		} catch (Exception e) {
			// e.printStackTrace();
			facesMessages
					.addFromResourceBundle("security.userAccountOrPasswordInvalid");
			return false;
		}

		if (!user.getIsActive()) {
			facesMessages.addFromResourceBundle("security.accountIsNotActive");
			return false;
		}
		if (user.getIsBlocked()) {
			facesMessages.addFromResourceBundle("security.accountWasBlocked");
			return false;
		}

		//log.info("USUARIO OBTENIDO ---> " + user.getName() + " "				+ user.getPassword());
		Boolean isSuccessfullyRegistered = gim.registerSession(user);
		if (!isSuccessfullyRegistered) {
			facesMessages.addFromResourceBundle(
					"security.userIsAlreadyLoggedIn", user.getName());
			return false;
		}

		query = entityManager.createNamedQuery("User.findResidentByUserId");
		query.setParameter("userId", user.getId());
		Person person = null;
		try {
			person = (Person) query.getSingleResult();
		} catch (Exception e) {
			facesMessages
					.addFromResourceBundle("security.noAccountOwnerAssociated");
			return false;
		}

		userSession.setUser(user);
		userSession.setPerson(person);
		userSession.setJournal(retrieveJournal(person));

		query = entityManager.createNamedQuery("User.findRolesByUserId");
		query.setParameter("userId", user.getId());
		List<Role> roles = query.getResultList();

		if (roles != null) {
			user.setRoles(roles);
			for (Role mr : roles) {
				identity.addRole(mr.getName());
			}
		}
		
		findIpRemoteMachine();
		if (isCashier()) {
			TillPermission tp = findTillPermission();
			if (tp != null && isTillAdressCorrect(tp.getTill())) {
				userSession.setTillPermission(tp);
			}
		}

		this.userSession.setNumberMunicipalBondsFormalizing(null);

		if (isFormalizing()) {
			// TODO implementar busqueda de emisiones pendienets de formalizar
			// menores o iguales a hoy
			this.userSession
					.setNumberMunicipalBondsFormalizing(numberMuicipalBondsFormalize());
		}

		//System.out.println("USUARIO CONECTADO: " + user.getName());
		return true;
	}

	public Integer numberMuicipalBondsFormalize() {

		SystemParameterService systemParameterService = ServiceLocator
				.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		Long municipallBondStatusFutureId = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_FUTURE_ISSUANCE");

		Query query = entityManager
				.createNamedQuery("MunicipalBond.countMunicipalsBondsPendingFormalize");
		query.setParameter("futureStatusId", municipallBondStatusFutureId);
		query.setParameter("now", new Date(), TemporalType.DATE);
		Long number = (Long) query.getSingleResult();
		if (number == 0) {
			return null;
		} else {
			return number.intValue();
		}
	}

	public boolean isCashier() {
		SystemParameterService systemParameterService = ServiceLocator
				.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		String cashierRoleName = systemParameterService
				.findParameter("ROLE_NAME_CASHIER");

		if (userSession.getUser().hasRole(cashierRoleName)) {
			return true;
		}

		return false;
	}

	public boolean isFormalizing() {
		SystemParameterService systemParameterService = ServiceLocator
				.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		String formalizingRoleName = systemParameterService
				.findParameter("ROLE_NAME_FORMALIZING");

		if (userSession.getUser().hasRole(formalizingRoleName)) {
			return true;
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public TillPermission findTillPermission() {
		Date currentDate = new Date();
		Query query = entityManager
				.createNamedQuery("TillPermission.findByPersonIdAndWorkdayDate");
		query.setParameter("personId", userSession.getPerson().getId());
		query.setParameter("date", currentDate);
		List<TillPermission> tillPermissions = query.getResultList();
		if (tillPermissions != null && tillPermissions.size() > 0) {
			if (tillPermissions.get(0).isEnabled()) {
				return tillPermissions.get(0);
			}
		}
		return null;
	}

	public String findIpRemoteMachine() {

		try {
			ExternalContext context = javax.faces.context.FacesContext
					.getCurrentInstance().getExternalContext();
			HttpServletRequest request = (HttpServletRequest) context
					.getRequest();
			ip = request.getHeader("X-Forwarded-For");
			if (isIPAddress(ip)) {
				//System.out.println("IP Return:" + ip);
				//System.out.println("::::::::::::::::::::::::::::::::::::::machine ip found:::::::::::::::::::::::::::::::::::::: " + ip);
				return ip;
			} else {
				ip = request.getRemoteAddr();
				//System.out.println("RemoteAddr:" + ip);
				//System.out.println("::::::::::::::::::::::::::::::::::::::machine ip found:::::::::::::::::::::::::::::::::::::: " + ip);
				return ip;
			}
		} catch (Exception e) {
			//System.out.println(e);
			ip = "ws REST";
			return "ws REST";
		}

		// ip = request.getRemoteAddr(); //En algunos casos solamente detecta la
		// IP del Proxy y no la del Equipo Local
		// System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::machine ip found:::::::::::::::::::::::::::::::::: "
		// + ip);
		// return ip;
	}

	private boolean isIPAddress(String ip) {
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			return false;
		}
		Pattern pattern = null;
		Matcher matcher;

		String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

		pattern = Pattern.compile(IPADDRESS_PATTERN);
		matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	public boolean isTillAdressCorrect(Till t) {
		if (t.getAddress().equals(ip))
			return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public Journal retrieveJournal(Person person) {
		Date currentDate = new Date();
		Query query = entityManager.createNamedQuery("Journal.findCurrent");
		query.setParameter("currentDate", currentDate);
		query.setParameter("operatorId", person.getId());
		for (Journal j : (List<Journal>) query.getResultList()) {
			return j;
		}
		return null;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}