package org.gob.gim.cementery.action;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cementery.model.Death;
import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.cementery.model.UnitStatus;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;

@Name("unitDeathHome")
public class UnitDeathHome extends EntityHome<Unit> implements Serializable {

	private static final long serialVersionUID = 1L;

	Log logger;

	private String criteria;
	private String identificationNumber;

	private boolean selectStatus;
	private UnitStatus unitStatus;
	
	private boolean reserve;
	private boolean exhumation;
	private boolean forExhumation;

	private String responsableIdentification;
	private String responsableCriteria;

	private Person responsable;
	private Resident responsableResident;

	private List<Resident> residents;
	
	private Unit unitLocal;
	private Date dateExhumation;
	private Date dateToExhumation;

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public boolean isFirsTime = true;
	
	public void wire() {
		if (!isFirsTime) return;
		getInstance();
		isFirsTime = false;
		dateExhumation = new GregorianCalendar().getTime();
		dateToExhumation = new GregorianCalendar().getTime();
	}

	public boolean isWired() {
		return true;
	}

	public Unit getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setUnitDeathId(Long id) {
		setId(id);
	}

	public Long getUnitDeathId() {
		return (Long) getId();
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public boolean isSelectStatus() {
		return selectStatus;
	}

	public void setSelectStatus(boolean selectStatus) {
		this.selectStatus = selectStatus;
	}

	public UnitStatus getUnitStatus() {
		return unitStatus;
	}

	public void setUnitStatus(UnitStatus unitStatus) {
		this.unitStatus = unitStatus;
	}

	public boolean isReserve() {
		return reserve;
	}

	public void setReserve(boolean reserve) {
		this.reserve = reserve;
	}

	public boolean isExhumation() {
		return exhumation;
	}

	public void setExhumation(boolean exhumation) {
		this.exhumation = exhumation;
	}

	@Begin(join=true)
	public void setInstanceReserve(Unit unitDeath){
		clearSearchResidentPanel();
		this.reserve = true;
		this.exhumation = false;
		this.forExhumation = false;
		this.setInstance(unitDeath);
	}

	@Begin(join=true)
	public void setInstanceExhumation(Unit unitDeath){
		clearSearchResidentPanel();
		this.reserve = false;
		this.exhumation = true;
		this.forExhumation = false;
		this.responsableResident = unitDeath.getCurrentDeath().getResponsableExhumation();
		this.responsableIdentification = unitDeath.getCurrentDeath().getResponsableExhumation().getIdentificationNumber();
		this.dateToExhumation = unitDeath.getCurrentDeath().getDateForExhumation();
		this.dateExhumation = new GregorianCalendar().getTime();
		this.setInstance(unitDeath);
	}

	@Begin(join=true)
	public void setInstanceForExhumation(Unit unitDeath){
		clearSearchResidentPanel();
		this.reserve = false;
		this.exhumation = true;
		this.forExhumation = true;
		this.dateToExhumation = new GregorianCalendar().getTime();
		this.setInstance(unitDeath);
	}

	public String getResponsableIdentification() {
		return responsableIdentification;
	}

	public void setResponsableIdentification(String responsableIdentification) {
		this.responsableIdentification = responsableIdentification;
	}

	public String getResponsableCriteria() {
		return responsableCriteria;
	}

	public void setResponsableCriteria(String responsableCriteria) {
		this.responsableCriteria = responsableCriteria;
	}

	public Person getResponsable() {
		return responsable;
	}

	public void setResponsable(Person responsable) {
		this.responsable = responsable;
	}

	public Resident getResponsableResident() {
		return responsableResident;
	}

	public void setResponsableResident(Resident responsableResident) {
		this.responsableResident = responsableResident;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	@SuppressWarnings("unchecked")
	public void searchResponsableByCriteria() {
		if (this.responsableCriteria != null && !this.responsableCriteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.responsableCriteria);
			setResidents(query.getResultList());
		}
	}

	public void searchPersonResponsable() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.responsableIdentification);
		if (forExhumation || reserve){
			try {
				Resident resident = (Resident) query.getSingleResult();
				this.setResponsableResident(resident);
				this.setResponsableIdentification(resident.getIdentificationNumber());
				//System.out.println("RESIDENT CHOOSER ACTION " + resident.getName());
				if (resident.getId() == null) {
					addFacesMessageFromResourceBundle("resident.notFound");
				}
	
			} catch (Exception e) {
				this.setResponsableResident(null);
				addFacesMessageFromResourceBundle("resident.notFound");
			}
		}
	}

	public void responsableSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		if (forExhumation || reserve){
			this.setResponsableResident(resident);
			this.setResponsableIdentification(resident.getIdentificationNumber());
		}
	}

	public void clearSearchResidentPanel() {
		this.setResponsableCriteria(null);
		this.setResponsableIdentification(null);
		this.setResponsable(null);
		this.setResponsableResident(null);
		setResidents(null);
	}

	public Unit getUnitLocal() {
		return unitLocal;
	}

	public void setUnitLocal(Unit unitLocal) {
		this.unitLocal = unitLocal;
	}

	public boolean isFirsTime() {
		return isFirsTime;
	}

	public void setFirsTime(boolean isFirsTime) {
		this.isFirsTime = isFirsTime;
	}

	public Date getDateExhumation() {
		return dateExhumation;
	}

	public void setDateExhumation(Date dateExhumation) {
		this.dateExhumation = dateExhumation;
	}

	public Date getDateToExhumation() {
		return dateToExhumation;
	}

	public void setDateToExhumation(Date dateToExhumation) {
		this.dateToExhumation = dateToExhumation;
	}

	public boolean isForExhumation() {
		return forExhumation;
	}

	public void setForExhumation(boolean forExhumation) {
		this.forExhumation = forExhumation;
	}

	@End
	public void updateExhumationAndReserve(){
		if (this.responsableResident == null) return;
		if (exhumation && !forExhumation){
			this.instance.setUnitStatus(UnitStatus.FREE);
			this.instance.getCurrentDeath().setResponsableExhumation((Person)this.responsableResident);
			this.instance.getCurrentDeath().setCurrent(Boolean.FALSE);
			this.instance.getCurrentDeath().setExhumationDate(dateExhumation);
			this.instance.setCurrentDeath(null);
		}
		else{
//			Death currentDeath = new Death();
//			currentDeath.setResponsableReserve((Person)this.responsableResident);
//			this.instance.setDeath(currentDeath);
			this.instance.setUnitStatus(UnitStatus.RESERVED);
		}
		getEntityManager().merge(this.instance);
		clearSearchResidentPanel();
	}

	@End
	public void updateForExhumation(){
		if (this.responsableResident == null) return;
		if (this.forExhumation){
			this.instance.setUnitStatus(UnitStatus.TO_EXHUME);
			this.instance.getCurrentDeath().setResponsableExhumation((Person)this.responsableResident);
			this.instance.getCurrentDeath().setDateForExhumation(dateToExhumation);
		}
		getEntityManager().merge(this.instance);
		clearSearchResidentPanel();
	}

	//Autor: Jock Samaniego
	//para la exhumacion directa
		
		public String directExhumation(){
			if(this.instance.getUnitStatus().toString().equals("RENTED")){
				return "TO_EXHUME";
			}else if(this.instance.getUnitStatus().toString().equals("TO_EXHUME")){		
				return "EXHUME";
			}
			return "null";
		}	
		
		//Autor: Jock Samaniego
		//para editar datos de registro y expiracion
				
		public void editUnitDeath(Long unitDeathId, Date dateOfDeath, Date subscriptionDate, Date expirationDate){
			try{
				Query query = getEntityManager().createNamedQuery("Death.FindById");
				query.setParameter("id", unitDeathId);
				Death death = (Death) query.getSingleResult();
				death.setDateOfDeath(dateOfDeath);
				death.getCurrentContract().setSubscriptionDate(subscriptionDate);
				death.getCurrentContract().setExpirationDate(expirationDate);
				
				EntityManager em = getEntityManager();
				em.persist(death);

			}catch(Exception e){
				//System.out.println("no se puedo realizar la edición");
				e.printStackTrace();
			}
		}
}