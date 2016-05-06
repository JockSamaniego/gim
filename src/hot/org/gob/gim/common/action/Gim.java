package org.gob.gim.common.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.Redirect;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.security.model.User;

@Name("gim")
@Scope(ScopeType.APPLICATION)
public class Gim extends EntityController {

	private static final long serialVersionUID = 2272832861988891642L;

	private Map<User, Boolean> activeSessions;

	private TaxpayerRecord institution;
	
	private FiscalPeriod fiscalPeriod;
	private List<FiscalPeriod> fiscalPeriods;

	@Logger
	Log logger;

	@Observer("org.jboss.seam.postInitialization")
	public void initialize() {
		activeSessions = new HashMap<User, Boolean>();
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		systemParameterService.initialize();
		IncomeService service = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		institution = service.findDefaultInstitution();
		loadFiscalPeriod();
	}

	public void refreshSystemparameters() {
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		systemParameterService.updateParameters();
		Redirect r = Redirect.instance();
		r.setViewId("/home.xhtml");
		r.setConversationPropagationEnabled(false);
		r.execute();
	}

	public Boolean isSessionActive(User user) {
		Boolean isActive = activeSessions.get(user);
		return (isActive != null && isActive) ? Boolean.TRUE : Boolean.FALSE;
	}

	public void unregisterSession(User user) {
		System.out.println("SESSION UNREGISTERED FOR USER " + user.getName());
		if (isSessionActive(user)) {
			activeSessions.remove(user);
		} else {
			System.out.println("NO SE HA INVALIDADO LA SESION PARA EL USUARIO, NO SE ENCONTRO UNA SESION ACTIVA  " + user.getName());
		}
	}

	public List<User> getActiveSessions() {
		List<User> connectedUsers = new ArrayList<User>();
		connectedUsers.addAll(activeSessions.keySet());
		return connectedUsers;
	}

	public Boolean registerSession(User user) {
		System.out.println("REGISTERING SESSION EXECUTED");
		Boolean isActive = Boolean.FALSE;
		if (isActive == null || !isActive) {
			activeSessions.put(user, Boolean.TRUE);
			System.out.println("SESSION REGISTERED" + Identity.instance());
			return Boolean.TRUE;
		}
		System.out.println("SESSION ALREADY REGISTERED ");
		return Boolean.FALSE;
	}

	public void setInstitution(TaxpayerRecord institution) {
		this.institution = institution;
	}

	public TaxpayerRecord getInstitution() {
		return institution;
	}

	public HttpSession getHttpSession() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		System.out.println("FACES CONTEXT ----> "+facesContext);
		if(facesContext != null){
			ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
			Object session = context.getSession(Boolean.FALSE);
			return (HttpSession) session;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public void loadFiscalPeriod() {
		Date currentDate = new Date();
		Query query = getEntityManager().createNamedQuery("FiscalPeriod.findCurrent");
		query.setParameter("currentDate", currentDate);
		fiscalPeriod = (FiscalPeriod) query.getSingleResult();
		query = getEntityManager().createNamedQuery("FiscalPeriod.findAll");
		fiscalPeriods = query.getResultList();
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public List<FiscalPeriod> getFiscalPeriods() {
		return fiscalPeriods;
	}

	public void setFiscalPeriods(List<FiscalPeriod> fiscalPeriods) {
		this.fiscalPeriods = fiscalPeriods;
	}
}
