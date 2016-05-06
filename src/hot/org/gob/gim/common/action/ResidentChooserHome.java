package org.gob.gim.common.action;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;

@Name("residentChooserHome")
@Scope(ScopeType.CONVERSATION)
public class ResidentChooserHome extends EntityController{
	private static final long serialVersionUID = 1L;
	
	@Logger
	Log logger;

	private String criteria;
	private String identificationNumber;
	private List<Resident> residents;
	private Resident resident;

	@SuppressWarnings("unchecked")
	public void searchByCriteria() {
		logger.info("SEARCH BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	public void clearSearchPanel() {
		criteria = null;
		residents = null;
	}
	
	public void search() {
		logger.info("RESIDENT CHOOSER CRITERIA " + this.criteria);
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.resident = resident;
			//findPendingBonds();
		} catch (Exception e) {
			e.printStackTrace();
			resident = null;
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	public void residentSelectedListener(ActionEvent event) throws Exception {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.resident = resident;
		this.identificationNumber = resident.getIdentificationNumber();

		// findPendingBonds();
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

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}
	
	public String getHandicapedPercentage(Resident resident){		
		return resident instanceof Person ? ((Person)resident).getHandicapedPercentage() != null ? ((Person)resident).getHandicapedPercentage().toString() : "" : "";
	}

}
