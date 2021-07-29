package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.ResidentService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.revenue.service.ExemptiomCemService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.gob.gim.revenue.service.PropertyService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.revenue.model.ExemptionCem;

@Name("exemptioncemHome")
@Scope(ScopeType.CONVERSATION)
public class ExemptionCEMHome extends EntityController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ExemptionCem exemption;

	private String cadastraCode;
	private Property property;
	private List<Property> properties;
	private String criteriaProperty;

	private Resident resident;
	private String criteria;
	private String identificationNumber;

	private List<Resident> residents;

	private BigDecimal percentValue;

	private ItemCatalog type;

	private String reference;

	private String explanation;

	private SystemParameterService systemParameterService;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private ItemCatalogService itemCatalogService;

	private PropertyService propertyService;

	private ExemptiomCemService exemptiomCemService;
	
	private Boolean isEdit = false;
	
	private ResidentService residentService;

	private Long id; //anular y edit 
	
	@Logger
	Log logger;

	@In
	FacesMessages facesMessages;
	
	public void setExemptionId(Long id) {
		System.out.println(id);
		this.id = id;
	}

	public Long getExemptionId() {
		return this.id;
	}

	public void init() {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}

		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}

		if (propertyService == null) {
			propertyService = ServiceLocator.getInstance().findResource(
					PropertyService.LOCAL_NAME);
		}

		if (exemptiomCemService == null) {
			exemptiomCemService = ServiceLocator.getInstance().findResource(
					ExemptiomCemService.LOCAL_NAME);
		}
		
		if (residentService == null) {
			residentService = ServiceLocator.getInstance().findResource(
					ResidentService.LOCAL_NAME);
		}
		
		//macartuche
		if(getId()!=null) {
			this.exemption = getEntityManager().find(ExemptionCem.class, this.id);
		}else {
			this.exemption = new ExemptionCem();
		}

	}

	public void searchResident() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			logger.info("RESIDENT CHOOSER ACTION " + resident.getName());

			this.resident = resident;
			// this.getInstance().getPropertiesInExemption().clear();
			// reCalculateValues();

			if (resident.getId() == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}

		} catch (Exception e) {
			this.resident = null;
			// reCalculateValues();
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	@SuppressWarnings("unchecked")
	public void searchResidentByCriteria() {
		logger.info("SEARCH RESIDENT BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	public void clearSearchResidentPanel() {
		this.criteria = null;
		residents = null;
	}

	public void residentSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.resident = resident;
		// this.getInstance().getPropertiesInExemption().clear();
		this.identificationNumber = resident.getIdentificationNumber();
		// reCalculateValues();
		clearSearchResidentPanel();
	}

	public void searchPropertyByCriteria() {
		String EJBQL = "Property.findByCadastralCode";
		// Query query = getEntityManager().createQuery(EJBQL);
		Query query = getEntityManager().createNamedQuery(EJBQL);
		query.setParameter("criteria", this.criteriaProperty);
		properties = query.getResultList();
	}

	public void clearSearchPropertyPanel() {
		this.criteriaProperty = null;
		properties = null;
	}

	public void propertySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Property property = (Property) component.getAttributes()
				.get("property");
		this.property = property;
	}

	public String save() {

		if (exemptiomCemService == null) {
			exemptiomCemService = ServiceLocator.getInstance().findResource(
					ExemptiomCemService.LOCAL_NAME);
		}
		/*
		 * System.out.println(this.property); System.out.println(this.resident);
		 * System.out.println("Porcentaje:"+this.percentValue);
		 * System.out.println("Tipo:"+this.type);
		 * System.out.println("Referencia:"+this.reference);
		 * System.out.println("Explicacion:"+this.explanation);
		 */

		ExemptionCem exemption = new ExemptionCem();
		exemption.setDiscountPercentage(this.percentValue);
		exemption.setExplanation(this.explanation);
		exemption.setProperty(this.property);
		exemption.setReference(this.reference);
		exemption.setResident(this.resident);
		exemption.setType(this.type);

		ExemptionCem result = exemptiomCemService.save(exemption);
		if (result != null) {
			return "persisted";
		}
		return null;

	}
	
	public String prepareEditExcemption(){
		
		FacesContext fc = FacesContext.getCurrentInstance();
		
		Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	
		if (exemptiomCemService == null) {
			exemptiomCemService = ServiceLocator.getInstance().findResource(
					ExemptiomCemService.LOCAL_NAME);
		}
		this.isEdit = Boolean.TRUE;
		ExemptionCem exemption = this.exemptiomCemService.findById(Long.valueOf(params.get("exemptionId")));
		this.property = exemption.getProperty();
		this.percentValue = exemption.getDiscountPercentage();
		this.explanation = exemption.getExplanation();
		this.reference = exemption.getReference();
		if (residentService == null) {
			residentService = ServiceLocator.getInstance().findResource(
					ResidentService.LOCAL_NAME);
		}
		this.resident = this.residentService.find(exemption.getResident().getId());
		this.type = exemption.getType();
		this.criteria = this.resident.getIdentificationNumber();
		this.identificationNumber = this.resident.getIdentificationNumber();
		return "edit";
	}

	
	public void nullifiedExemption() {
		try {			
			ExemptionCem exemptioncem = getEntityManager().find(ExemptionCem.class, this.id);
			if (exemptioncem != null) {
				exemptioncem.setActive(Boolean.FALSE);
				getEntityManager().merge(exemptioncem);
				getEntityManager().flush(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getCadastraCode() {
		return cadastraCode;
	}

	public void setCadastraCode(String cadastraCode) {
		this.cadastraCode = cadastraCode;
	}

	public ExemptionCem getExemption() {
		return exemption;
	}

	public void setExemption(ExemptionCem exemption) {
		this.exemption = exemption;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
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

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	/**
	 * @return the properties
	 */
	public List<Property> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	/**
	 * @return the criteriaProperty
	 */
	public String getCriteriaProperty() {
		return criteriaProperty;
	}

	/**
	 * @param criteriaProperty
	 *            the criteriaProperty to set
	 */
	public void setCriteriaProperty(String criteriaProperty) {
		this.criteriaProperty = criteriaProperty;
	}

	/**
	 * @return the percentValue
	 */
	public BigDecimal getPercentValue() {
		return percentValue;
	}

	/**
	 * @param percentValue
	 *            the percentValue to set
	 */
	public void setPercentValue(BigDecimal percentValue) {
		this.percentValue = percentValue;
	}

	/**
	 * @return the type
	 */
	public ItemCatalog getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ItemCatalog type) {
		this.type = type;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @param explanation
	 *            the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * @return the isEdit
	 */
	public Boolean getIsEdit() {
		return isEdit;
	}

	/**
	 * @param isEdit the isEdit to set
	 */
	public void setIsEdit(Boolean isEdit) {
		this.isEdit = isEdit;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
 		
}
