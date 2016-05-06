package org.gob.gim.firestation.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import javax.persistence.Query;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;

import ec.gob.gim.firestation.model.FireMan;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.commercial.model.Business;


@Name("fireManHome")
public class FireManHome extends EntityHome<FireMan> {
	
	@Logger 
	Log logger;

	
	private String criteria;
	
	private List<Resident> residents;
	private String fireManIdentificationNumber;
	private String fireManPhoneNumber;

	public void setFireManId(Long id) {
		setId(id);
	}

	public Long getFireManId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	private boolean isFirstTime = true;
	public void wire() {
		if(!isFirstTime) return;
		if(getInstance() != null && getInstance().getFireMan() != null) setFireManIdentificationNumber(getInstance().getFireMan().getIdentificationNumber());
	//	getInstance();
		isFirstTime = false;
	}

	public boolean isWired() {
		return true;
	}

	public FireMan getDefinedInstance() {
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
	
	public void searchFireMan() {
		logger.info("looking for............holaaaaaa {0}", getFireManIdentificationNumber());
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", getFireManIdentificationNumber());
		try{
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION "+resident.getName());
			
			//resident.add(this.getInstance());			
			this.getInstance().setFireMan(resident);
			
			if (resident.getId() == null){
				addFacesMessageFromResourceBundle("resident.notFound");
			}else{
				setFireManIdentificationNumber(this.getInstance().getFireMan().getIdentificationNumber());
				
			}
			
		}
		catch(Exception e){
			this.getInstance().setFireMan(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void clearSearchPanel(){
		this.setCriteria(null);
		residents = null;
	}
	
	public void fireManSelectedListener(ActionEvent event){
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setFireMan(resident);
		if(resident != null){
			setFireManIdentificationNumber(resident.getIdentificationNumber());
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
	
	public String getFireManIdentificationNumber() {
		return fireManIdentificationNumber;
	}

	public void setFireManIdentificationNumber(String fireManIdentificationNumber) {
		this.fireManIdentificationNumber = fireManIdentificationNumber;
	}

	public String getFireManPhoneNumber() {
		return fireManPhoneNumber;
	}

	public void setFireManPhoneNumber(String fireManPhoneNumber) {
		this.fireManPhoneNumber = fireManPhoneNumber;
	}
	
}
