package org.gob.gim.common.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Alert;
import ec.gob.gim.common.model.Resident;

@Name("alertHome")
public class AlertHome extends EntityHome<Alert> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2343245935635548909L;

	private Resident resident;
	private List<Resident> residents = new ArrayList<Resident>();
	private String identificationNumber = "";
	private String residentCriteria = "";
	private boolean isFirstTime = true;
	private boolean enabledSave = true;
	
	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	
	public void setAlertId(Long id) {
		setId(id);
	}

	public Long getAlertId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		if (isFirstTime) {
			getInstance();
			isFirstTime = false;
			Calendar now = Calendar.getInstance();
			if(this.getInstance().getResident() != null){
				this.identificationNumber = this.instance.getResident().getIdentificationNumber();
				this.resident = this.instance.getResident();
				this.instance.setCloseUser(userSession.getUser());
				this.instance.setCloseDate(now.getTime());
				this.instance.setIsActive(false);
			} else {
				identificationNumber = null;
				resident = null;
				this.instance.setOpenUser(userSession.getUser());
				this.instance.setOpenDate(now.getTime());
			}
		}
	}

	public boolean isWired() {
		return true;
	}

	public Alert getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	/**
	 * @return the resident
	 */
	public Resident getResident() {
		return resident;
	}

	/**
	 * @param resident the resident to set
	 */
	public void setResident(Resident resident) {
		this.resident = resident;
	}

	/**
	 * @return the residents
	 */
	public List<Resident> getResidents() {
		return residents;
	}

	/**
	 * @param residents the residents to set
	 */
	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	/**
	 * @return the identificationNumber
	 */
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	/**
	 * @param identificationNumber the identificationNumber to set
	 */
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	/**
	 * @return the residentCriteria
	 */
	public String getResidentCriteria() {
		return residentCriteria;
	}

	/**
	 * @param residentCriteria the residentCriteria to set
	 */
	public void setResidentCriteria(String residentCriteria) {
		this.residentCriteria = residentCriteria;
	}
	
	/**
	 * @return the isFirstTime
	 */
	public boolean isFirstTime() {
		return isFirstTime;
	}

	/**
	 * @param isFirstTime the isFirstTime to set
	 */
	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	/**
	 * @return the enabledSave
	 */
	public boolean isEnabledSave() {
		return enabledSave;
	}

	/**
	 * @param enabledSave the enabledSave to set
	 */
	public void setEnabledSave(boolean enabledSave) {
		this.enabledSave = enabledSave;
	}

	public void searchResident() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try{
			Resident resident = (Resident) query.getSingleResult();
			this.setResident(resident);
			this.instance.setResident(resident);
		}
		catch(Exception e){
			this.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria(){
		if(this.residentCriteria != null && !this.residentCriteria.isEmpty()){
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.residentCriteria);
			residents = query.getResultList();
		}
	}
	
	public void clearSearchResidentPanel(){
		this.setResidentCriteria(null);
		residents = null;
		resident = null;
	}

	public void residentSelectedListener(ActionEvent event) throws Exception {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setResident(resident);
		this.instance.setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}

}