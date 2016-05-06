package org.gob.gim.cementery.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cementery.model.Cremation;
import ec.gob.gim.cementery.model.Crematorium;
import ec.gob.gim.common.model.Resident;

@Name("cremationHome")
public class CremationHome extends EntityHome<Cremation> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	Log logger;
	private List<Resident> residents;

	private String criteria;
	private String identificationNumber;

	@In
	FacesMessages facesMessages;
	
	public boolean isFirsTime = true;
	
	public Crematorium crematorium;
	private Date startDate;
	private Date endDate;
		
	private List<Cremation> cremations = new ArrayList<Cremation>();
	private Double totalValue;
	private Double totalRateValue;
	
	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		if (!isFirsTime) return;
		getInstance();

		if(this.getInstance().getBloodRelation() != null){
			this.identificationNumber = this.getInstance().getBloodRelation().getIdentificationNumber();
		} else {
			identificationNumber = null;
		}
	}

	public boolean isWired() {
		return true;
	}

	public String save(){
		if(this.instance.getBloodRelation() == null){
			String message = Interpolator.instance().interpolate("#{messages['cremation.notExistBloodRelation']}", new Object[0]);
			facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
			return "failed";
		}
		updateRateValue();
		if(this.isManaged()) return super.update();
		
		return super.persist();
	}
	
	public Cremation getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setCremationId(Long id) {
		setId(id);
	}

	public Long getCremationId() {
		return (Long) getId();
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
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

	@SuppressWarnings("unchecked")
	public void searchBloodRelationCremationByCriteria() {
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			setResidents(query.getResultList());
		}
	}

	public void searchBloodRelationCremation() throws NullPointerException {
		try {
			Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
			query.setParameter("identificationNumber", this.identificationNumber);
			Resident resident = (Resident) query.getSingleResult();

			this.getInstance().setBloodRelation(resident);

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.getInstance().setBloodRelation(null);
			this.identificationNumber = null;
			this.setResidents(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setBloodRelation(resident);
	}

	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		setResidents(null);
	}

	public boolean isFirsTime() {
		return isFirsTime;
	}

	public void setFirsTime(boolean isFirsTime) {
		this.isFirsTime = isFirsTime;
	}

	public void updateRateValue(){
		Double rateVal = 0.00;
		rateVal = (this.instance.getValue() * this.instance.getCrematorium().getRate()) / 100;
		this.instance.setRateValue(rateVal);
	}

	public Crematorium getCrematorium() {
		return crematorium;
	}

	public void setCrematorium(Crematorium crematorium) {
		this.crematorium = crematorium;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Cremation> getCremations() {
		return cremations;
	}

	public void setCremations(List<Cremation> cremations) {
		this.cremations = cremations;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public Double getTotalRateValue() {
		return totalRateValue;
	}

	public void setTotalRateValue(Double totalRateValue) {
		this.totalRateValue = totalRateValue;
	}

	public void generateCremationsReport() {
		cremations = generateCremations();
	}
	
	@SuppressWarnings("unchecked")
	public List<Cremation> generateCremations(){
		Query query = getEntityManager().createNamedQuery("Cremation.findByCrematoriumAndDates");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("crematorium", this.crematorium);
		List<Cremation> cremations = query.getResultList();
		this.totalRateValue = 0.00;
		this.totalValue = 0.00;
		for (Cremation cremation : cremations){
			totalRateValue += cremation.getRateValue();
			totalValue += cremation.getValue();
		}
		
		return cremations;
	}
	
}