package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.gob.gim.revenue.service.PropertyService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Exemption;
import ec.gob.gim.revenue.model.ExemptionCem;
import ec.gob.gim.revenue.model.ExemptionForProperty;
import ec.gob.gim.revenue.model.ExemptionType;
import ec.gob.gim.revenue.model.DTO.PropertyDTO;

@Name("exemptioncemHome")
@Scope(ScopeType.CONVERSATION)
public class ExemptionCEMHome extends EntityHome<ExemptionCem> {

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

	private Boolean isExemptionEspecial = Boolean.FALSE;

	private SystemParameterService systemParameterService;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private ExemptionType exemptionSpecial;

	private List<ItemCatalog> typerTreatmentExcemption = new ArrayList<ItemCatalog>();

	private ItemCatalogService itemCatalogService;

	private PropertyService propertyService;

	private List<ExemptionForProperty> propertiesInExemptionNormal = new ArrayList<ExemptionForProperty>();

	private List<ExemptionForProperty> propertiesInExemptionSpecial = new ArrayList<ExemptionForProperty>();

	// rfam 2017-12-26
	private List<ExemptionForProperty> deletedPropertiesInExemption = new ArrayList<ExemptionForProperty>();

	private ItemCatalog typeTreatmentExcemptionSpecial;

	private List<PropertyDTO> propertiesForSelection = new ArrayList<PropertyDTO>();

	// private ItemCatalog typeTreatmentExcemptionSpecial;

	@Logger
	Log logger;

	@In
	FacesMessages facesMessages;

	public List<PropertyDTO> getPropertiesForSelection() {
		return propertiesForSelection;
	}

	public void setPropertiesForSelection(
			List<PropertyDTO> propertiesForSelection) {
		this.propertiesForSelection = propertiesForSelection;
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

		this.typerTreatmentExcemption = itemCatalogService
				.findItemsForCatalogCode(CatalogConstants.CATALOG_TYPES_TREATMENT_EXCEMPTION);

		this.typeTreatmentExcemptionSpecial = itemCatalogService
				.findItemByCodeAndCodeCatalog(
						CatalogConstants.CATALOG_TYPES_TREATMENT_EXCEMPTION,
						CatalogConstants.ITEM_CATALOG_TYPE_EXCLUDE_TREATMENT_EXCEMPTION);
	}

	/*
	public void reCalculateValues() {
		//System.out.println("<<<RR>>reCalculateValues");
		existExemption();
		this.getInstance().setPropertiesAppraisal(
				calculateTotalPropertiesAppraisal());
		this.getInstance().calculatePatrimony();
	}

	public void searchResident() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
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

	public BigDecimal calculateTotalPropertiesAppraisal() {
		BigDecimal res = BigDecimal.ZERO;
		if (this.getInstance().getResident() == null)
			return res;
		res = res.add(totalPropertiesAppraisal(this.getInstance().getResident()
				.getId()));
		if (this.getInstance().getPartner() == null)
			return res;
		res = res.add(totalPropertiesAppraisal(this.getInstance().getPartner()
				.getId()));
		return res;
	}

	public BigDecimal totalPropertiesAppraisal(Long residentId) {
		// @tag exoneraciones2017 //colocar en la consulta el deleted=false
		Query query = getEntityManager().createNamedQuery(
				"Domain.totalAppraisalCurrentDomainByResident");
		query.setParameter("residentId", residentId);
		BigDecimal res = (BigDecimal) query.getSingleResult();
		if (res == null)
			res = BigDecimal.ZERO;
		return res;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Exemption existExemption() {
		if (this.getInstance().getResident() == null
				|| this.getInstance().getFiscalPeriod() == null)
			return null;
		Query query = getEntityManager().createNamedQuery(
				"Exemption.findByFiscalPeriodAndResident");
		query.setParameter("residentId", this.getInstance().getResident()
				.getId());
		query.setParameter("fiscalPeriodId", this.getInstance()
				.getFiscalPeriod().getId());
		List<Exemption> list = query.getResultList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Exemption exemption = (Exemption) iterator.next();
			if (exemption.getActive() == true)
				return exemption;
		}
		// if(list.size() > 0){
		// return list.get(0);
		// }
		return null;
	}

	public String save() {

		Exemption exist = existExemption();
		if (exist != null
				&& (this.isManaged() && !exist.getId().equals(
						this.getInstance().getId()))
				|| (exist != null && !this.isManaged())) {
			String message = Interpolator.instance().interpolate(
					"#{messages['exemption.existLandExemption']}",
					new Object[0]);
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return "failed";
		}
*/
		/*
		 * René Ortega 2016-08-08
		 */
		// TODO agregar propiedades de listas auxiliares

	/*
		for (ExemptionForProperty property : this.propertiesInExemptionNormal) {
			if (!hasPropertyInExemption(property))
				this.instance.getPropertiesInExemption().add(property);
		}

		for (ExemptionForProperty property : this.propertiesInExemptionSpecial) {
			if (!hasPropertyInExemption(property))
				this.instance.getPropertiesInExemption().add(property);
		}

		// rfam 2017-12-16
		// para borrar las proiedades quitadas en el evento remove
		for (ExemptionForProperty property : this.deletedPropertiesInExemption) {
			if (hasPropertyInExemption(property))
				this.instance.getPropertiesInExemption().remove(property);
		}

		if (this.instance.getId() == null) {
			Calendar cal = new GregorianCalendar();
			this.instance.setCreationDate(cal.getTime());
			this.instance.setActive(true);
			this.instance.setDiscountYearNumber(new Long(1));
		}

		if (this.isManaged())
			return super.update();

		return super.persist();
	}

	public void searchPartner() {
		Query query = getEntityManager().createNamedQuery(
				"Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber",
				this.partnerIdentificationNumber);
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
			Query query = getEntityManager().createNamedQuery(
					"Resident.findByCriteria");
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
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
		this.getInstance().setResident(resident);
		this.getInstance().getPropertiesInExemption().clear();
		this.setIdentificationNumber(resident.getIdentificationNumber());
		reCalculateValues();
		clearSearchResidentPanel();
	}

	public void partnerSelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes()
				.get("resident");
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
		if (!isFirsTime)
			return;
		getInstance();
		if (this.getInstance().getResident() != null)
			identificationNumber = this.getInstance().getResident()
					.getIdentificationNumber();
		if (this.getInstance().getPartner() != null)
			partnerIdentificationNumber = this.getInstance().getPartner()
					.getIdentificationNumber();
		isFirsTime = false;
		init();

		this.typeTreatmentExcemptionSpecial = itemCatalogService
				.findItemByCodeAndCodeCatalog(
						CatalogConstants.CATALOG_TYPES_TREATMENT_EXCEMPTION,
						CatalogConstants.ITEM_CATALOG_TYPE_EXCLUDE_TREATMENT_EXCEMPTION);
		this.exemptionSpecial = systemParameterService.materialize(
				ExemptionType.class, "EXEMPTION_TYPE_ID_SPECIAL");
		prepareEditExcemption(this.instance);
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

	public void setPartnerIdentificationNumber(
			String partnerIdentificationNumber) {
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

	public void setExemptionForProperty(
			ExemptionForProperty exemptionForProperty) {
		this.exemptionForProperty = exemptionForProperty;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public Boolean getIsExemptionEspecial() {
		return isExemptionEspecial;
	}

	public void setIsExemptionEspecial(Boolean isExemptionEspecial) {
		this.isExemptionEspecial = isExemptionEspecial;
	}

	public List<ItemCatalog> getTyperTreatmentExcemption() {
		return typerTreatmentExcemption;
	}

	public void setTyperTreatmentExcemption(
			List<ItemCatalog> typerTreatmentExcemption) {
		this.typerTreatmentExcemption = typerTreatmentExcemption;
	}

	public List<ExemptionForProperty> getPropertiesInExemptionNormal() {
		return propertiesInExemptionNormal;
	}

	public void setPropertiesInExemptionNormal(
			List<ExemptionForProperty> propertiesInExemptionNormal) {
		this.propertiesInExemptionNormal = propertiesInExemptionNormal;
	}

	public List<ExemptionForProperty> getPropertiesInExemptionSpecial() {
		return propertiesInExemptionSpecial;
	}

	public void setPropertiesInExemptionSpecial(
			List<ExemptionForProperty> propertiesInExemptionSpecial) {
		this.propertiesInExemptionSpecial = propertiesInExemptionSpecial;
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
		Property property = (Property) component.getAttributes()
				.get("property");
		this.exemptionForProperty.setProperty(property);
		this.exemptionForProperty.setNameHistoryResident(property
				.getCurrentDomain().getResident().getName());
	}

	public boolean hasPropertyInExemption(
			ExemptionForProperty exemptionForProperty) {
		for (ExemptionForProperty exForProperty : this.getInstance()
				.getPropertiesInExemption()) {
			if (exForProperty.getProperty().equals(
					exemptionForProperty.getProperty())) {
				return true;
			}
		}
		return false;
	}

	public void addExemptionForProperty() {

		// TODO controlar que no este ya agregada

		// if (!hasPropertyInExemption(this.exemptionForProperty))
		// this.instance.getPropertiesInExemption().add(
		// this.exemptionForProperty);

		if (this.exemptionForProperty.getTreatmentType() == null) {
			this.propertiesInExemptionNormal.add(this.exemptionForProperty);
		} else if (this.exemptionForProperty.getTreatmentType().getId()
				.equals(typeTreatmentExcemptionSpecial.getId())) {
			this.propertiesInExemptionSpecial.add(this.exemptionForProperty);
			// Recalcular el valor del avaluo de las propiedades
			// this.instance
			// .setPropertiesAppraisalSpecialTreatment(this.exemptionForProperty
			// .getProperty().getCurrentDomain()
			// .getCommercialAppraisal());
		}

		BigDecimal totalTreatmentSpecial = BigDecimal.ZERO;
		for (ExemptionForProperty exemptionForProperty : propertiesInExemptionSpecial) {
			totalTreatmentSpecial = totalTreatmentSpecial
					.add(exemptionForProperty.getProperty().getCurrentDomain()
							.getCommercialAppraisal());
		}

		this.instance
				.setPropertiesAppraisalSpecialTreatment(totalTreatmentSpecial);

	}

	public void addProperties() {
		//System.out.println(this.propertiesForSelection);

		for (PropertyDTO propertyDTO : propertiesForSelection) {

			if (propertyDTO.getSelected()) {

				Property proBD = this.propertyService
						.findPropertyById(propertyDTO.getProperty_id());
				ExemptionForProperty efp = new ExemptionForProperty();
				efp.setAmountCreditYear1(propertyDTO.getAmountCreditYear1());
				efp.setAmountCreditYear2(propertyDTO.getAmountCreditYear2());
				efp.setAmountCreditYear3(propertyDTO.getAmountCreditYear3());
				efp.setProperty(proBD);
				efp.setTreatmentType(propertyDTO.getTreatmentType());
				efp.setValidUntil(propertyDTO.getValidUntil());
				efp.setNameHistoryResident(propertyDTO.getResident_name());

				if (propertyDTO.getTreatmentType() == null) {
					this.propertiesInExemptionNormal.add(efp);
				} else if (propertyDTO.getTreatmentType().getId()
						.equals(typeTreatmentExcemptionSpecial.getId())) {
					this.propertiesInExemptionSpecial.add(efp);
				}

				BigDecimal totalTreatmentSpecial = BigDecimal.ZERO;
				for (ExemptionForProperty exemptionForProperty : propertiesInExemptionSpecial) {
					totalTreatmentSpecial = totalTreatmentSpecial
							.add(exemptionForProperty.getProperty()
									.getCurrentDomain()
									.getCommercialAppraisal());
				}

				this.instance
						.setPropertiesAppraisalSpecialTreatment(totalTreatmentSpecial);
			}
		}

	}

	public void removeExemptionForProperty(ExemptionForProperty property) {
		//System.out.println("ID propiedad a remover:"				+ property.getProperty().getCadastralCode());
		if (property.getTreatmentType() == null) {
			this.propertiesInExemptionNormal.remove(property);
		} else if (property.getTreatmentType().getId()
				.equals(typeTreatmentExcemptionSpecial.getId())) {
			this.propertiesInExemptionSpecial.remove(property);
		}

		BigDecimal totalTreatmentSpecial = BigDecimal.ZERO;
		for (ExemptionForProperty exemptionForProperty : propertiesInExemptionSpecial) {
			totalTreatmentSpecial = totalTreatmentSpecial
					.add(exemptionForProperty.getProperty().getCurrentDomain()
							.getCommercialAppraisal());
		}

		// rfam 2017-12-26
		deletedPropertiesInExemption.add(property);

		this.instance
				.setPropertiesAppraisalSpecialTreatment(totalTreatmentSpecial);

	}
*/
	/*
	public void createExemptionForProperty() {
		// TODO obtener propiedades de contibuyente y conyuge si es el caso
		List<Long> idsResidentsForQuery = new ArrayList<Long>();
		List<Long> idsPropertiesForQuery = new ArrayList<Long>();
		idsPropertiesForQuery.add(new Long(-1));
		if (this.instance.getResident() != null) {
			idsResidentsForQuery.add(this.instance.getResident().getId());
		}
		if (this.instance.getPartner() != null) {
			idsResidentsForQuery.add(this.instance.getPartner().getId());
		}

		 
		
		for (ExemptionForProperty efp : propertiesInExemptionNormal) {
			idsPropertiesForQuery.add(efp.getProperty().getId());
		}
		
		for (ExemptionForProperty efp : propertiesInExemptionSpecial) {
			idsPropertiesForQuery.add(efp.getProperty().getId());
		}

		this.propertiesForSelection = this.propertyService
				.findByResidentIds(idsResidentsForQuery, idsPropertiesForQuery );

		this.exemptionForProperty = new ExemptionForProperty();
		clearSearchPropertyPanel();
	}

	public void onChangeExemptionType() {

		this.instance.getPropertiesInExemption().clear();

		this.instance.setReference(this.instance.getExemptionType()
				.getReference());

		if (this.instance.getExemptionType().getId()
				.equals(exemptionSpecial.getId())) {
			this.isExemptionEspecial = Boolean.TRUE;
		} else {
			this.isExemptionEspecial = Boolean.FALSE;
		}

	}

	public void prepareEditExcemption(Exemption excemption) {

		this.propertiesInExemptionNormal.clear();
		this.propertiesInExemptionSpecial.clear();

		if (excemption.getExemptionType() != null) {

			if (excemption.getExemptionType().getId()
					.equals(exemptionSpecial.getId())) {
				this.isExemptionEspecial = Boolean.TRUE;
			} else {
				this.isExemptionEspecial = Boolean.FALSE;
			}
		}

		for (ExemptionForProperty propertyInExcemption : excemption
				.getPropertiesInExemption()) {
			// TODO
			if (propertyInExcemption.getTreatmentType() == null) {
				this.propertiesInExemptionNormal.add(propertyInExcemption);
			} else if (propertyInExcemption.getTreatmentType().getId()
					.equals(typeTreatmentExcemptionSpecial.getId())) {
				this.propertiesInExemptionSpecial.add(propertyInExcemption);
			}
		}
	}

	public Boolean renderTableTreatmentSpecial() {
		if (this.instance.getExemptionType() == null) {
			return Boolean.FALSE;
		} else {
			if (this.instance.getExemptionType().getName()
					.equals("Por Tercera Edad")) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;

	}

	public void changeSelectedProperty(PropertyDTO property, boolean selected) {
		//System.out.println(property);
		for (PropertyDTO propertyDto : propertiesForSelection) {
			if (propertyDto.getProperty_id() == property.getProperty_id()) {
				propertyDto.setSelected(!selected);
				break;
			}
		}
	}
*/
}
