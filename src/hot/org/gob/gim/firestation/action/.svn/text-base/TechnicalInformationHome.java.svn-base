package org.gob.gim.firestation.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.commercial.model.Local;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.firestation.model.InspectionObservation;
import ec.gob.gim.firestation.model.TechnicalInformation;



@Name("technicalInformationHome")
public class TechnicalInformationHome extends EntityHome<TechnicalInformation> {
	
	@Logger 
	Log logger;
	//@In(create = true)
	
	private String criteria;
	
	private List<Resident> residents;
	private String ownerIdentificationNumber;
	private String fireManPhoneNumber;
	private Resident resident;
	private Date inspectionsDate;
	private List<Integer> emissionYearList;	
	private Date maximumServiceDate;
	private Date minimumServiceDate;	

	
	public void setTechnicalInformationId(Long id) {
		setId(id);
	}

	public Long getTechnicalInformationId() {
		return (Long) getId();
	}

	
	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	private boolean isFirstTime = true;
	public void wire() {
		//if(!isFirstTime) return;
	//	if(getInstance() != null && getInstance().getFireMan() != null) setFireManIdentificationNumber(getInstance().getFireMan().getIdentificationNumber());
		getInstance();
	//	isFirstTime = false;
	}

	public boolean isWired() {
		return true;
	}

	public TechnicalInformation getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}
	
	public void searchResident() {
		logger.info("looking for............holaaaaaa {0}", getOwnerIdentificationNumber());
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", getOwnerIdentificationNumber());
		try{
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION "+resident.getName());
			logger.info("RESIDENT CHOOSER ACTION "+resident.getId());
			
			//resident.add(this.getInstance());			
			this.setResident(resident);
			
			if (resident.getId() == null){
				addFacesMessageFromResourceBundle("resident.notFound");
			}else{
				//setOwnerIdentificationNumber(this.getInstance().getOwner().getIdentificationNumber());
			}
			
		}
		catch(Exception e){
			this.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria(){
		logger.info("SEARCH RESIDENT BY CRITERIA "+this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()){
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	public String getOwnerIdentificationNumber() {
		return ownerIdentificationNumber;
	}

	public void setOwnerIdentificationNumber(String ownerIdentificationNumber) {
		this.ownerIdentificationNumber = ownerIdentificationNumber;
	}

	public String getFireManPhoneNumber() {
		return fireManPhoneNumber;
	}

	public void setFireManPhoneNumber(String fireManPhoneNumber) {
		this.fireManPhoneNumber = fireManPhoneNumber;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}
	
	@SuppressWarnings("unchecked")
	public List<Local> findLocalesByResidentId(){
		System.out.println("lleaga alear localesssssssss acctionsssssssss");
		if(resident != null){
			Query query = getEntityManager().createNamedQuery("Local.findByOwnerId");
			query.setParameter("ownerId", resident.getId());
			return query.getResultList();
		}
		return new ArrayList<Local>();
	}
	
	public void clearSearchPanel(){
		this.setCriteria(null);
		residents = null;
	}
	
	public void ownerSelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setResident(resident);
		if(resident != null){
			setOwnerIdentificationNumber(resident.getIdentificationNumber());
		}
	}

	public Date getInspectionsDate() {
		return inspectionsDate;
	}
	public void setInspectionsDate(Date inspectionsDate) {
		this.inspectionsDate = inspectionsDate;
	}
	
	public void addObservation(){
		InspectionObservation io= new InspectionObservation();
		io.setTechnicalInformation(this.getInstance());
		this.getInstance().add(io);
	}
	
	public void removeObservation(InspectionObservation io){
		this.getInstance().remove(io);
	}
}
