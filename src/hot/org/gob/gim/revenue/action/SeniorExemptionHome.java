package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.service.PropertyService;
import org.gob.gim.revenue.view.EntryValueItem;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.Exemption;
import ec.gob.gim.revenue.model.ExemptionForProperty;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.adjunct.PropertyAppraisal;

@Name("seniorExemptionHome")
@Scope(ScopeType.CONVERSATION)
public class SeniorExemptionHome extends EntityHome<Exemption> {

	private static final long serialVersionUID = 4014289457149474315L;

	public static String ADJUNCT_PREFIX = "/revenue/adjunct/";
	public static String ADJUNCT_SUFIX = ".xhtml";
	public static String EMPTY_ADJUNCT_URI = ADJUNCT_PREFIX + "Empty" + ADJUNCT_SUFIX;

	public static String REVENUE_SERVICE_NAME = "/gim/RevenueService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	public static String ROLE_NAME_EMISOR = "ROLE_NAME_EMISOR";
	public static String MUNICIPALBOND_SERVICE_NAME = "/gim/MunicipalBondService/local";
	public static String ENTRY_ID_URBAN_PROPERTY = "ENTRY_ID_URBAN_PROPERTY";
	public static String ENTRY_ID_RUSTIC_PROPERTY = "ENTRY_ID_RUSTIC_PROPERTY";

	private List<MunicipalBond> municipalBonds;
	private List<Property> properties;

	private SystemParameterService systemParameterService;
	
	private PropertyService propertyService;

	BigDecimal cien = new BigDecimal(100);
	
	// private ItemCatalog typeTreatmentExcemptionSpecial;

	@Logger
	Log logger;

	@In
	FacesMessages facesMessages;

	@In(create = true)
	AdjunctHome adjunctHome;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public void init() {
		municipalBonds = new ArrayList<MunicipalBond>();
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}

		if (propertyService == null) {
			propertyService = ServiceLocator.getInstance().findResource(
					PropertyService.LOCAL_NAME);
		}
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
		isFirsTime = false;
		init();
	}

	public boolean isWired() {
		return true;
	}

	public Exemption getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public boolean isFirsTime() {
		return isFirsTime;
	}

	public void setFirsTime(boolean isFirsTime) {
		this.isFirsTime = isFirsTime;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public void getAllProperties() {
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
	}

	public void findAllProperties() {
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
		Query query = getEntityManager().createNamedQuery(
		"Property.findByResidentsIds"); query.setParameter("ids",
		idsResidentsForQuery); 
		this.properties = query.getResultList();
	}

	public BigDecimal percentageExemption(){
		BigDecimal percentage = BigDecimal.ZERO;
		percentage.setScale(2);
		BigDecimal patrimony = BigDecimal.ZERO;
		BigDecimal maxDiscountPersonal = BigDecimal.ZERO;
		BigDecimal maxExemption = instance.getFiscalPeriod().getSalariesNumber().multiply(instance.getFiscalPeriod().getBasicSalaryUnifiedForRevenue());
//		System.out.println("maxExemption: " + maxExemption);
		findAllProperties();
		for (Property property : properties) {
//			System.out.println(property.getCadastralCode() + "   " + property.getPropertyType().getId() + "   " + property.getCurrentDomain().getCommercialAppraisal());
			patrimony = patrimony.add(property.getCurrentDomain().getCommercialAppraisal());
		}
//		System.out.println("Patrimonio: " + patrimony);
		patrimony = patrimony.add(instance.getVehiclesAppraisal()).add(instance.getCommercialValues()).add(instance.getPersonalAssets());
//		System.out.println("Patrimonio: " + patrimony);
		if (patrimony.compareTo(maxExemption) == 1){
			maxDiscountPersonal = maxExemption.multiply(instance.getExemptionPercentage()).divide(cien);
//			System.out.println("maxDiscountPersonal: " + maxDiscountPersonal);
			percentage = maxDiscountPersonal.divide(patrimony, 2, RoundingMode.HALF_UP);
		} else {
			percentage = instance.getExemptionPercentage().divide(cien, 2, RoundingMode.HALF_UP);
		}
//		System.out.println("Porcentaje de descuento: " + percentage);
		return percentage;
	}
	
	public Entry findEntry(String parameterName){
		Long entryId = systemParameterService.findParameter(parameterName);
		Query query = getEntityManager().createNamedQuery(
				"Entry.findById");
		query.setParameter("entryId", entryId);
		return (Entry) query.getSingleResult();
	}
	
	public void calculateEmission(){
		BigDecimal discountPercentage = percentageExemption();
		if (!municipalBonds.isEmpty()) {
			municipalBonds.clear();
		}
		FiscalPeriod fiscalPeriod = userSession.getFiscalPeriod();
		RevenueService revenueService = ServiceLocator.getInstance()
				.findResource(REVENUE_SERVICE_NAME);

		EntryValueItem entryValueItem = new EntryValueItem();
		for (Property property : properties) {
			Entry entry = null;
			try {
				if (property.getPropertyType().getId().intValue() == 1){
					entry = findEntry(ENTRY_ID_URBAN_PROPERTY);
					entryValueItem.setDescription("IMPUESTO PREDIAL URBANO con Exención por Tercera Edad");
				} else{
					entry = findEntry(ENTRY_ID_RUSTIC_PROPERTY);
					entryValueItem.setDescription("IMPUESTO PREDIAL RÚSTICO con Exención por Tercera Edad");
				}
				entryValueItem.setMainValue(property.getCurrentDomain().getCommercialAppraisal());
				entryValueItem.setServiceDate(new Date());
				entryValueItem
						.setReference(property.getPreviousCadastralCode() + property.getCadastralCode());
				entryValueItem.setExempt(false);
				entryValueItem.setInternalTramit(false);
				entryValueItem.setNoPasiveSubject(false);
				PropertyAppraisal prop = createAdjunct(property, discountPercentage); 
				String groupingCode = property.getCadastralCode();
				Adjunct adjunct = createAdjunct(property, discountPercentage);
//				System.out.println("------------------");
				MunicipalBond mb = revenueService.createMunicipalBond(
						instance.getResident(), entry, fiscalPeriod,
						entryValueItem, true, adjunct);
				if (mb.getGroupingCode() == null)
					mb.setGroupingCode(groupingCode);
				mb.setAdjunct(prop);
				mb.setEmisionPeriod(mb.getEmisionDate());
				mb.setAddress(property.getCurrentDomain().getResident().getCurrentAddressAsString());
				mb.setBondAddress(property.getAddress());
				mb.setOriginator(userSession.getPerson());
				mb.setServiceDate(userSession.getFiscalPeriod().getStartDate());
				mb.setReference(instance.getReference());
				mb.setDescription(instance.getExplanation());
//				System.out.println("------------------ groupingCode: " + mb.getGroupingCode());
//				System.out.println("------------------ comercial: " + prop.getCommercialAppraisal());
//				System.out.println("------------------ discount: " + prop.getExemptionValue());
//				System.out.println("------------------ paymentValue: " + mb.getPaidTotal());
				municipalBonds.add(mb);				
			} catch (EntryDefinitionNotFoundException e) {
				e.printStackTrace();
				StatusMessages.instance().addFromResourceBundleOrDefault(
						Severity.ERROR, e.getClass().getSimpleName(),
						e.getMessage(), entry.getName(),
						entryValueItem.getServiceDate());
			} catch (Exception e) {
				e.printStackTrace();
				StatusMessages.instance().addFromResourceBundleOrDefault(
						Severity.ERROR, e.getClass().getSimpleName(),
						e.getMessage());
			}
		}
	}
	
	private PropertyAppraisal createAdjunct(Property pro, BigDecimal discountPercentage) {
		PropertyAppraisal adj = new PropertyAppraisal();
		adj.setPreviousCadastralCode(pro.getPreviousCadastralCode());
		adj.setCadastralCode(pro.getCadastralCode());
		adj.setBuildingAppraisal(pro.getCurrentDomain().getBuildingAppraisal());
		adj.setLotAppraisal(pro.getCurrentDomain().getLotAppraisal());
		adj.setCommercialAppraisal(pro.getCurrentDomain()
				.getCommercialAppraisal());
		adj.setLotArea(pro.getArea());
		adj.setConstructionArea(pro.getCurrentDomain().getTotalAreaConstruction());
		adj.setExemptionValue(pro.getCurrentDomain().getCommercialAppraisal().multiply(discountPercentage));
		adj.setProperty(pro);
		return adj;
	}
	
	private void copyExemption(Exemption exemption){
		Exemption old = exemption;
		Exemption obj = new Exemption();
		obj.setActive(true);
		obj.setAuxDate(exemption.getAuxDate());
		obj.setCommercialValues(exemption.getCommercialValues());
		obj.setCreationDate(new Date());
		obj.setDiscountPercentage(exemption.getDiscountPercentage());
		obj.setDiscountYearNumber(exemption.getDiscountYearNumber());
		obj.setExemptionPercentage(exemption.getExemptionPercentage());
		obj.setExemptionType(exemption.getExemptionType());
		obj.setExpirationDate(exemption.getExpirationDate());
		obj.setExplanation(exemption.getExplanation());
		obj.setFiscalPeriod(nextFiscalPeriod(exemption.getFiscalPeriod()));
		obj.setPartner(exemption.getPartner());
		obj.setPatrimony(exemption.getPatrimony());
		obj.setPersonalAssets(exemption.getPersonalAssets());
		obj.setPropertiesAppraisal(exemption.getPropertiesAppraisal());
		obj.setPropertiesAppraisalSpecialTreatment(exemption.getPropertiesAppraisalSpecialTreatment());
		obj.setReference(exemption.getReference());
		obj.setResident(exemption.getResident());
		obj.setVehiclesAppraisal(exemption.getVehiclesAppraisal());
		obj.setPropertiesInExemption(new ArrayList<ExemptionForProperty>());
		for (ExemptionForProperty exemptionForProperty : instance.getPropertiesInExemption()) {
			ExemptionForProperty ex = new ExemptionForProperty();
			ex.setAmountCreditYear1(exemptionForProperty.getAmountCreditYear1());
			ex.setAmountCreditYear2(exemptionForProperty.getAmountCreditYear2());
			ex.setAmountCreditYear3(exemptionForProperty.getAmountCreditYear3());
			ex.setDiscountPercentage(exemptionForProperty.getDiscountPercentage());
			ex.setExemption(obj);
			ex.setNameHistoryResident(exemptionForProperty.getNameHistoryResident());
			ex.setProperty(exemptionForProperty.getProperty());
			ex.setTreatmentType(exemptionForProperty.getTreatmentType());
			ex.setValidUntil(exemptionForProperty.getValidUntil());
			obj.getPropertiesInExemption().add(ex);
		}
		this.setInstance(obj);
		super.persist();
		this.setInstance(old);
	}
	
	private FiscalPeriod nextFiscalPeriod(FiscalPeriod fiscalPeriod){
		FiscalPeriod next = null;
		String sql = "select id from fiscalperiod f where startdate > '"
				+ fiscalPeriod.getStartDate() + "' order by startdate";
		Query query = getEntityManager().createNativeQuery(sql);
		List<BigInteger> list = query.getResultList();
		if (list.size() > 0){
			BigInteger id = list.get(0);
			query = getEntityManager().createNamedQuery("FiscalPeriod.findById"); 
			query.setParameter("id", id.longValue());
			next = (FiscalPeriod) query.getSingleResult();
			return next;
		} else
			return null;
	}
	
	public void emitMunicipalBonds(){
		try {
			RevenueService revenueService = ServiceLocator.getInstance()
					.findResource(REVENUE_SERVICE_NAME);
			revenueService.emit(municipalBonds, userSession.getUser());
			municipalBonds.clear();
			StatusMessages.instance().addFromResourceBundleOrDefault(
					Severity.INFO, "Emisión de títulos",
					"Títulos emitidos correctamente.");
			getInstance().setActive(false);
			super.update();
			copyExemption(this.getInstance());
		} catch (Exception e) {
			e.printStackTrace();
			StatusMessages.instance().addFromResourceBundleOrDefault(
					Severity.ERROR, e.getClass().getSimpleName(),
					e.getMessage());
		}
	}

	public void remove(MunicipalBond bond){
		boolean removed = municipalBonds.remove(bond);
		if (removed)
			bond = null;
	}
	
	public boolean hasRoles(){
		if (userSession.hasRole(ROLE_NAME_EMISOR)) return true;
		else return false;
	}
	
}
