package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Exemption;
import ec.gob.gim.revenue.model.ExemptionForProperty;

@Name("exemptionHome")
public class ExemptionHome extends EntityHome<Exemption> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Resident resident;
	private String criteria;
	private String identificationNumber;
	private String partnerIdentificationNumber;	

	private ExemptionForProperty exemptionForProperty;
	
	private List<Property> properties;		
	private Property property;
	private String criteriaProperty;

	private Resident partner;
	
	private List<Resident> residents;
	
	@Logger
	Log logger;
	
	@In
	FacesMessages facesMessages;
	
	public void reCalculateValues(){
		System.out.println("<<<RR>>reCalculateValues");
		existExemption();
		this.getInstance().setPropertiesAppraisal(calculateTotalPropertiesAppraisal());
		this.getInstance().calculatePatrimony();	
	}
	
	public void searchResident() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			this.getInstance().setResident(resident);
			this.getInstance().getPropertiesInExemption().clear();
			reCalculateValues();
			
			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {		
			this.getInstance().setResident(null);
			reCalculateValues();
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}
	
	public BigDecimal calculateTotalPropertiesAppraisal(){
		BigDecimal res = BigDecimal.ZERO;
		if(this.getInstance().getResident() == null) return res;
		res = res.add(totalPropertiesAppraisal(this.getInstance().getResident().getId()));		
		if(this.getInstance().getPartner() == null) return res;
		res = res.add(totalPropertiesAppraisal(this.getInstance().getPartner().getId()));		
		return res;
	}
	
	public BigDecimal totalPropertiesAppraisal(Long residentId){		
		Query query = getEntityManager().createNamedQuery("Domain.totalAppraisalCurrentDomainByResident");
		query.setParameter("residentId", residentId);		
		BigDecimal res = (BigDecimal)query.getSingleResult();
		if(res == null) res = BigDecimal.ZERO;
		return res;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Exemption existExemption() {
		if(this.getInstance().getResident() == null || this.getInstance().getFiscalPeriod() == null) return null;
		Query query = getEntityManager().createNamedQuery("Exemption.findByFiscalPeriodAndResident");
		query.setParameter("residentId", this.getInstance().getResident().getId());
		query.setParameter("fiscalPeriodId", this.getInstance().getFiscalPeriod().getId());
		List<Exemption> list = query.getResultList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Exemption exemption = (Exemption) iterator.next();
			if (exemption.getActive() == true)
				return exemption;
		}
//		if(list.size() > 0){
//			return list.get(0);						
//		}
		return null;
	}
	
	public String save(){
		Exemption exist = existExemption();
		if(exist != null && 
				( this.isManaged() && !exist.getId().equals(this.getInstance().getId())) || (exist != null && !this.isManaged()) ){
			String message = Interpolator.instance().interpolate("#{messages['exemption.existLandExemption']}", new Object[0]);
			facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
			return "failed";
		}
		
		if (this.instance.getId() == null) {
			Calendar cal = new GregorianCalendar();
			this.instance.setCreationDate(cal.getTime());
			this.instance.setActive(true);
			this.instance.setDiscountYearNumber(new Long(1));
		}
		
		if(this.isManaged()) return super.update();
		
		return super.persist();
	}
	
	public void searchPartner() {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.partnerIdentificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			this.getInstance().setPartner(resident);
			reCalculateValues();
			
			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {			
			this.getInstance().setPartner(null);
			reCalculateValues();
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	
	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}
	
	public void clearSearchResidentPanel() {
		this.setCriteria(null);
		residents = null;
	}
	
	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();		
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.getInstance().setResident(resident);	
		this.getInstance().getPropertiesInExemption().clear();
		this.setIdentificationNumber(resident.getIdentificationNumber());
		reCalculateValues();				
		clearSearchResidentPanel();
	}

	public void partnerSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");		
		this.getInstance().setPartner(resident);
		this.setPartnerIdentificationNumber(resident.getIdentificationNumber());
		reCalculateValues();		
		clearSearchResidentPanel();
	}
	
	public void setExemptionId(Long id) {
		setId(id);
	}

	public Long getExemptionId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public boolean isFirsTime = true;
	
	public void wire() {
		if (!isFirsTime) return;
		getInstance();
		if(this.getInstance().getResident() != null) identificationNumber = this.getInstance().getResident().getIdentificationNumber();
		if(this.getInstance().getPartner() != null) partnerIdentificationNumber = this.getInstance().getPartner().getIdentificationNumber();
		isFirsTime = false;
	}

	public boolean isWired() {
		return true;
	}

	public Exemption getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Resident getPartner() {
		return partner;
	}

	public void setPartner(Resident partner) {
		this.partner = partner;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	
	public String getPartnerIdentificationNumber() {
		return partnerIdentificationNumber;
	}

	public void setPartnerIdentificationNumber(String partnerIdentificationNumber) {
		this.partnerIdentificationNumber = partnerIdentificationNumber;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getCriteriaProperty() {
		return criteriaProperty;
	}

	public void setCriteriaProperty(String criteriaProperty) {
		this.criteriaProperty = criteriaProperty;
	}

	public boolean isFirsTime() {
		return isFirsTime;
	}

	public void setFirsTime(boolean isFirsTime) {
		this.isFirsTime = isFirsTime;
	}
	
	public ExemptionForProperty getExemptionForProperty() {
		return exemptionForProperty;
	}

	public void setExemptionForProperty(ExemptionForProperty exemptionForProperty) {
		this.exemptionForProperty = exemptionForProperty;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	@SuppressWarnings("unchecked")
	public void searchPropertyByCriteria() {
		String EJBQL = "Property.findByCadastralCode";
		// Query query = getEntityManager().createQuery(EJBQL);
		Query query = getEntityManager().createNamedQuery(EJBQL);
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}
	
	public void clearSearchPropertyPanel() {
		this.setCriteriaProperty(null);
		property = null;
		properties = null;
	}
	
	public void propertySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Property property = (Property) component.getAttributes().get("property");
		this.exemptionForProperty.setProperty(property);
		this.exemptionForProperty.setNameHistoryResident(property.getCurrentDomain().getResident().getName());
	}
	
	public boolean hasPropertyInExemption(ExemptionForProperty exemptionForProperty){
		for (ExemptionForProperty exForProperty : this.getInstance().getPropertiesInExemption()){
			if (exForProperty.getProperty().equals(exemptionForProperty.getProperty())){
				return true;
			}
		}
		return false;
	}
	
	public void addExemptionForProperty(){
		if (!hasPropertyInExemption(this.exemptionForProperty))
			this.instance.getPropertiesInExemption().add(this.exemptionForProperty);
	}
	
	public void removeExemptionForProperty(){
		
	}
	
	public void createExemptionForProperty(){
		this.exemptionForProperty = new ExemptionForProperty();
		clearSearchPropertyPanel();
	}
}
