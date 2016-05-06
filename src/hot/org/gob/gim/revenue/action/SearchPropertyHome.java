package org.gob.gim.revenue.action;

import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.DateUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Resident;

@Name("searchProperty")
@Scope(ScopeType.CONVERSATION)
public class SearchPropertyHome extends EntityController {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Logger
	Log logger;
	
	private List<Resident> residents;
	private List<Property> properties;
	private Long residentId;	
	private String criteria;
	private String cadastralCode;
	private String identificationNumber;
	private Resident resident;
	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA aaaaa" + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	public void searchResident() {
		logger.info("RESIDENT CHOOSER CRITERIA... "+this.identificationNumber);
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			// resident.add(this.getInstance());
			setResident(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}
	
	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());
	}
	
	@SuppressWarnings("unchecked")
	public void searchProperties(){
//		if(resident == null) return;
//		if(cadastralCode == null) cadastralCode = "";		
//		Query query = getEntityManager().createNamedQuery("Property.findByResidentAndCadastralCode");
//		query.setParameter("residentId", resident.getId());
//		query.setParameter("cadastralCode", cadastralCode);		
//		properties = query.getResultList();		
		if(resident == null){
			if ((cadastralCode == null) || (cadastralCode.trim() == "")) return;
			Query query = getEntityManager().createNamedQuery("Property.findByCadastralCode");
			query.setParameter("criteria", cadastralCode);
			properties = query.getResultList();
		} else{
			if(cadastralCode == null) cadastralCode = "";
			Query query = getEntityManager().createNamedQuery("Property.findByResidentAndCadastralCode");
			query.setParameter("residentId", resident.getId());
			query.setParameter("cadastralCode", cadastralCode);
			properties = query.getResultList();
		}
	}
	
	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public Long getResidentId() {
		return residentId;
	}

	public void setResidentId(Long residentId) {
		this.residentId = residentId;
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

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}
	
}
