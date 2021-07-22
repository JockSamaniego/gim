package org.gob.gim.common.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.CatalogConstants;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.cadaster.model.AffectationFactor;
import ec.gob.gim.cadaster.model.CompassPoint;
import ec.gob.gim.cadaster.model.DispatchReasonType;
import ec.gob.gim.cadaster.model.ExternalFinishing;
import ec.gob.gim.cadaster.model.FenceMaterial;
import ec.gob.gim.cadaster.model.GarbageCollection;
import ec.gob.gim.cadaster.model.LandUse;
import ec.gob.gim.cadaster.model.LineType;
import ec.gob.gim.cadaster.model.LotPosition;
import ec.gob.gim.cadaster.model.LotTopography;
import ec.gob.gim.cadaster.model.MortgageType;
import ec.gob.gim.cadaster.model.PreservationState;
import ec.gob.gim.cadaster.model.PropertyUse;
import ec.gob.gim.cadaster.model.PurchaseType;
import ec.gob.gim.cadaster.model.RoofMaterial;
import ec.gob.gim.cadaster.model.Sewerage;
import ec.gob.gim.cadaster.model.SewerageType;
import ec.gob.gim.cadaster.model.Sidewalk;
import ec.gob.gim.cadaster.model.SidewalkMaterial;
import ec.gob.gim.cadaster.model.StreetMaterial;
import ec.gob.gim.cadaster.model.StreetType;
import ec.gob.gim.cadaster.model.StructureMaterial;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.cadaster.model.WallMaterial;
import ec.gob.gim.cadaster.model.WaterType;
import ec.gob.gim.cadaster.model.WorkDealType;
import ec.gob.gim.cementery.model.BodyType;
import ec.gob.gim.cementery.model.Cementery;
import ec.gob.gim.cementery.model.Crematorium;
import ec.gob.gim.cementery.model.UnitStatus;
import ec.gob.gim.cementery.model.UnitType;
import ec.gob.gim.coercive.model.NotificationTaskType;
import ec.gob.gim.commercial.model.BusinessCatalog;
import ec.gob.gim.commercial.model.BusinessType;
import ec.gob.gim.commercial.model.FeatureCategory;
import ec.gob.gim.commercial.model.FireRates;
import ec.gob.gim.common.model.AlertPriority;
import ec.gob.gim.common.model.AlertType;
import ec.gob.gim.common.model.CheckingRecordType;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Gender;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.LegalEntityType;
import ec.gob.gim.common.model.MaritalStatus;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.CreditNoteType;
import ec.gob.gim.income.model.LegalStatus;
import ec.gob.gim.income.model.MoneyType;
import ec.gob.gim.income.model.PaymentType;
import ec.gob.gim.income.model.ReceiptType;
import ec.gob.gim.income.model.StatusElectronicReceipt;
import ec.gob.gim.income.model.TaxpayerRecord;
import ec.gob.gim.market.model.Market;
import ec.gob.gim.market.model.ProductType;
import ec.gob.gim.market.model.StandStatus;
import ec.gob.gim.rental.model.SpaceStatus;
import ec.gob.gim.rental.model.SpaceType;
import ec.gob.gim.revenue.model.ConcessionType;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.ExemptionType;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.ResidentType;
import ec.gob.gim.revenue.model.SolvencyReportType;
import ec.gob.gim.revenue.model.TimePeriod;
import ec.gob.gim.revenue.model.TimeToCalculate;
import ec.gob.gim.revenue.model.adjunct.detail.VehicleMaker;
import ec.gob.gim.revenue.model.adjunct.detail.VehicleType;
import ec.gob.gim.security.model.PermissionType;
import ec.gob.gim.security.model.Role;
import ec.gob.gim.waterservice.model.BudgetEntryType;
import ec.gob.gim.waterservice.model.ConsumptionState;
import ec.gob.gim.waterservice.model.MonthType;
import ec.gob.gim.waterservice.model.WaterMeterStatus;


@Name("factories")
@Scope(ScopeType.CONVERSATION)
public class FactoryController  extends EntityController{

	private static final long serialVersionUID = 1L;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	private SystemParameterService systemParameterService;
	
	private ItemCatalogService itemCatalogService;

	@SuppressWarnings("unchecked")
	@Factory("purchaseTypes")
	public List<PurchaseType> findPurchaseTypes() {
		Query query = this.getEntityManager().createNamedQuery("PurchaseType.findAll");
		return query.getResultList();
	}
	
	@Factory("concessionTypes")
	public List<ConcessionType> loadConcessionTypes() {
		return Arrays.asList(ConcessionType.values());
	}

	@Factory("monthTypes")
	public List<MonthType> loadMonthTypes() {
		return Arrays.asList(MonthType.values());
	}
	
	@Factory("propertyUses")
	public List<PropertyUse> loadPropertyUse() {
		return Arrays.asList(PropertyUse.values());
	}	

	@SuppressWarnings("unchecked")
	@Factory(value="cementeries")
	public List<Cementery> loadCementeries(){
		Query query = this.getEntityManager().createNamedQuery("Cementery.findAll");
		return (List<Cementery>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Factory(value="typesExcemptionSpecialTreatment")
	public List<ItemCatalog> loadtypesExcemptionSpecialTreatment(){
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		
		//Query query = this.getEntityManager().createNamedQuery("Cementery.findAll");
		return (List<ItemCatalog>) itemCatalogService
				.findItemsForCatalogCode(CatalogConstants.CATALOG_TYPES_TREATMENT_EXCEMPTION);
	}
	
	@SuppressWarnings("unchecked")
	@Factory("departments")
	public List<String> findDepartments() {
		Query query = this.getEntityManager().createNamedQuery("Charge.findDepartments");
		return query.getResultList();
	}
		
	@Factory("waterMeterStatuses")
	@SuppressWarnings("unchecked")
	public List<WaterMeterStatus> findWaterMeterStatuses(){
		Query query = getPersistenceContext().createNamedQuery("WaterMeterStatus.findAll");
		return query.getResultList();
	}
	
	@Factory("consumptionStates")
	@SuppressWarnings("unchecked")
	public List<ConsumptionState> findConsumptionStates(){
		Query query = getPersistenceContext().createNamedQuery("ConsumptionState.findAll");
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Entry> findCriteria(Object suggest){
		String pref = (String)suggest;
		Query query = this.getEntityManager().createNamedQuery("Entry.findByCriteria");
		query.setParameter("criteria", pref);
		return (List<Entry>)query.getResultList();
	}
	
	@Factory("timesToCalculate")
	@SuppressWarnings("unchecked")
	public List<TimeToCalculate> findTimesToCalculate(){		
		return Arrays.asList(TimeToCalculate.values());
	}
	
	@Factory("solvencyReports")
	@SuppressWarnings("unchecked")
	public List<SolvencyReportType> findSolvencyReportTypes(){		
		return Arrays.asList(SolvencyReportType.values());
	}

	@Factory("notificationTaskTypes")
	@SuppressWarnings("unchecked")
	public List<NotificationTaskType> findNotificationTaskTypes() {
		Query query = getPersistenceContext().createNamedQuery(
				"NotificationTaskType.findAll");
		return query.getResultList();
	}
	
	@Factory("currentCashiers")
	@SuppressWarnings("unchecked")
	public List<Person> findCurrentCashiers() {		
		Query query = getPersistenceContext().createNamedQuery("TillPermission.findCashiersByWorkdayDate");
		query.setParameter("date", Calendar.getInstance().getTime());
		return query.getResultList();
	}
	
	@Factory("provinces")
	@SuppressWarnings("unchecked")
	public List<TerritorialDivision> findProvinces() {
		Query query = getPersistenceContext().createNamedQuery(
				"TerritorialDivision.findProvinces");
		return query.getResultList();
	}
	
	@Factory("dispatchReasonsType")
	@SuppressWarnings("unchecked")
	public List<DispatchReasonType> findDispatchReasons() {
		return Arrays.asList(DispatchReasonType.values());
	}
	
	@Factory("permissionsType")
	@SuppressWarnings("unchecked")
	public List<PermissionType> findPermissionTypes() {
		return Arrays.asList(PermissionType.values());
	}
	
	@Factory("checkingRecordTypes")
	@SuppressWarnings("unchecked")
	public List<CheckingRecordType> findCheckingRecordTypes() {
		List<CheckingRecordType> checkingRecordTypes = new ArrayList<CheckingRecordType>();
		checkingRecordTypes.add(CheckingRecordType.CHECKED);
		checkingRecordTypes.add(CheckingRecordType.REGISTERED);
		return checkingRecordTypes;
	}
	
	@Factory("timePeriods")
	@SuppressWarnings("unchecked")
	public List<TimePeriod> getTimePeriods(){
		Query query = getPersistenceContext().createNamedQuery("TimePeriod.findAll");
		return query.getResultList();
	}

	@Factory("bondStatus")
	public List<MunicipalBondStatus> bondStatus(){
		 List<MunicipalBondStatus> list = new ArrayList<MunicipalBondStatus>();
		 if (systemParameterService == null)
				systemParameterService = ServiceLocator.getInstance().findResource(
						SYSTEM_PARAMETER_SERVICE_NAME);
		 MunicipalBondStatus pendingStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");	
		 MunicipalBondStatus blockedStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		 MunicipalBondStatus paidStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");
						
		 list.add(pendingStatus);
		 list.add(blockedStatus);		 
		 list.add(paidStatus);
		 
		 return list;
	}
	
	@Factory("pendingAndPaidBondStatus")
	public List<MunicipalBondStatus> findPendingAndPaidBondStatus(){
		 List<MunicipalBondStatus> list = new ArrayList<MunicipalBondStatus>();
		 if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		 MunicipalBondStatus pendingStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");	 
		 MunicipalBondStatus paidStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");
		 
		 list.add(pendingStatus);		 
		 list.add(paidStatus);
		 
		 return list;
	}
	
	@Factory("validBondStatus")
	public List<MunicipalBondStatus> findValidBondStatus(){
		 List<MunicipalBondStatus> list = new ArrayList<MunicipalBondStatus>();
		 if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		 MunicipalBondStatus pendingStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");	 
		 MunicipalBondStatus paidStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");
		 MunicipalBondStatus compensationStatus = systemParameterService.materialize(MunicipalBondStatus.class,"MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
		 MunicipalBondStatus inPaymentAgreementStatus  = systemParameterService.materialize(MunicipalBondStatus.class,"MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		 MunicipalBondStatus externalChannelStatus = systemParameterService.materialize(MunicipalBondStatus.class,"MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL");
		 MunicipalBondStatus blockedStatus = systemParameterService.materialize(MunicipalBondStatus.class,"MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		 MunicipalBondStatus reversedStatus = systemParameterService.materialize(MunicipalBondStatus.class,"MUNICIPAL_BOND_STATUS_ID_REVERSED");
		 MunicipalBondStatus futureIssuanceBondStatus = systemParameterService.materialize(MunicipalBondStatus.class,"MUNICIPAL_BOND_STATUS_ID_FUTURE_ISSUANCE");
		 //MunicipalBondStatus errorsCorrectionBondStatus = systemParameterService.materialize(MunicipalBondStatus.class,"MUNICIPAL_BOND_STATUS_ID_ERRORS_CORRECTION");
						
		 list.add(pendingStatus);		 
		 list.add(paidStatus);
		 list.add(compensationStatus);
		 list.add(inPaymentAgreementStatus);
		 list.add(externalChannelStatus);
		 list.add(blockedStatus);
		 list.add(reversedStatus);
		 list.add(futureIssuanceBondStatus);
		 //list.add(errorsCorrectionBondStatus);
		 return list;
	}
	
	@Factory("emitters")
	public List<Person> findEmitters(){		 
		 if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		 String emisorRole = systemParameterService.findParameter("ROLE_NAME_EMISOR");	 
		 Query query = this.getEntityManager().createNamedQuery("Person.findByRoleName");
		 query.setParameter("roleName", emisorRole);
		 return query.getResultList();
	}
	
	@Factory("cashiers")
	public List<Person> findCashiers(){		 
		 if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		 String emisorRole = systemParameterService.findParameter("ROLE_NAME_CASHIER");	 
		 Query query = this.getEntityManager().createNamedQuery("Person.findByRoleName");
		 query.setParameter("roleName", emisorRole);
		 return query.getResultList();
	}
	
	@Factory("compassPoints")
	@SuppressWarnings("unchecked")
	public List<CompassPoint> findCompassPoints() {
		return Arrays.asList(CompassPoint.values());
	}
		
	
	@SuppressWarnings("unchecked")
	@Factory("landUses")
	public List<LandUse> getLandUseList() {
		Query query = this.getEntityManager().createNamedQuery(
				"LandUse.findAllOrderByCode");
		return (List<LandUse>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Factory("roles")
	public List<Role> getRoles() {
		Query query = this.getEntityManager().createNamedQuery(
				"Role.findAll");
		return (List<Role>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Factory("fenceMaterials")
	public List<FenceMaterial> loadFenceMaterials() {
		Query query = this.getEntityManager().createNamedQuery(
				"FenceMaterial.findAll");
		return (List<FenceMaterial>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Factory("lotPositions")
	public List<LotPosition> loadLotPositions() {
		Query query = this.getEntityManager().createNamedQuery(
				"LotPosition.findAllOrderById");
		return (List<LotPosition>) query.getResultList();
	}

	@Factory("sewerages")
	public List<Sewerage> loadSewerages() {
		return Arrays.asList(Sewerage.values());
	}

	@Factory("garbageCollections")
	public List<GarbageCollection> loadGarbageCollections() {
		return Arrays.asList(GarbageCollection.values());
	}

	@Factory("lotTopographys")
	public List<LotTopography> loadLotTopographys() {
		return Arrays.asList(LotTopography.values());
	}

	@Factory("sidewalks")
	public List<Sidewalk> loadSidewalk() {
		return Arrays.asList(Sidewalk.values());
	}

	@Factory("structureMaterials")
	public List<StructureMaterial> loadStructureMaterials() {
		return Arrays.asList(StructureMaterial.values());
	}

	@Factory("wallMaterials")
	public List<WallMaterial> loadWallMaterials() {
		return Arrays.asList(WallMaterial.values());
	}

	@Factory("roofMaterials")
	public List<RoofMaterial> loadRoofMaterials() {
		return Arrays.asList(RoofMaterial.values());
	}

	@Factory("preservationStates")
	public List<PreservationState> loadPreservationStates() {
		return Arrays.asList(PreservationState.values());
	}

	@Factory("externalFinishings")
	public List<ExternalFinishing> loadExternalFinishings() {
		return Arrays.asList(ExternalFinishing.values());
	}
	
	@Factory("mortgageTypes")
	public List<MortgageType> loadMortgages() {
		return Arrays.asList(MortgageType.values());
	}
	

	@Factory("lineTypes")
	public List<LineType> findLineTypes(){
		return Arrays.asList(LineType.values());
	}
	
	@Factory("waterTypes")
	public List<WaterType> findWaterTypes(){
		return Arrays.asList(WaterType.values());
	}
	
	@Factory("sewerageTypes")
	public List<SewerageType> findSewerageTypes(){
		return Arrays.asList(SewerageType.values());
	}
	
	@Factory("streetTypes")
	@SuppressWarnings("unchecked")
	public List<StreetType> findStreetTypes(){
		Query query = getPersistenceContext().createNamedQuery("StreetType.findAll");
		return query.getResultList();
	}
	
	@Factory("sidewalkMaterials")
	@SuppressWarnings("unchecked")
	public List<SidewalkMaterial> findSidewalkMaterial(){
		Query query = getPersistenceContext().createNamedQuery("SidewalkMaterial.findAll");
		return query.getResultList();
	}
	
	@Factory("receiptTypes")
	@SuppressWarnings("unchecked")
	public List<ReceiptType> findReceiptType(){
		Query query = getPersistenceContext().createNamedQuery("ReceiptType.findAll");
		return query.getResultList();
	}
	
	@Factory("taxpayerRecords")
	@SuppressWarnings("unchecked")
	public List<TaxpayerRecord> findTaxpayerRecord(){
		Query query = getPersistenceContext().createNamedQuery("TaxpayerRecord.findAll");
		return query.getResultList();
	}
	
	@Factory("streetMaterials")
	@SuppressWarnings("unchecked")
	public List<StreetMaterial> findStreetMaterial(){
		Query query = getPersistenceContext().createNamedQuery("StreetMaterial.findAllOrderByCode");
		return query.getResultList();
	}
	
	
	@Factory("vehicleMakers")
	@SuppressWarnings("unchecked")
	public List<VehicleMaker> findVehicleMakers(){
		Query query = getPersistenceContext().createNamedQuery("VehicleMaker.findAll");
		return query.getResultList();
	}
	
	@Factory("vehicleTypes")
	@SuppressWarnings("unchecked")
	public List<VehicleType> findVehicleTypes() {
		Query query = getPersistenceContext().createNamedQuery("VehicleType.findAll");
		return query.getResultList();
	}
	
	@Factory("spaceStatus")
	public List<SpaceStatus> loadSpaceStatus() {
		return Arrays.asList(SpaceStatus.values());
	}
	
	@Factory("spaceTypes")
	public List<SpaceType> loadSpaceTypes() {
		Query query = getPersistenceContext().createNamedQuery("SpaceType.findAll");
		return query.getResultList();		
	}
	
	@Factory("moneyTypes")
	public List<MoneyType> loadMoneyTypes() {
		return Arrays.asList(MoneyType.values());		
	}
	
	@Factory("spaceTypesNames")
	public List<String> loadSpaceTypesNames() {
		Query query = getPersistenceContext().createNamedQuery("SpaceType.findAllNames");
		return query.getResultList();		
	}
	
	@Factory("workDealTypes")
	public List<WorkDealType> loadWorkDealTypes() {		
		return Arrays.asList(WorkDealType.values());		
	}
	
	@Factory("residentTypes")
	public List<ResidentType> loadResidentTypes() {		
		return Arrays.asList(ResidentType.values());		
	}
	
	@Factory("entriesWithoutParents")
	public List<Entry> findEntriesWithoutParents() {
		Query query = this.getEntityManager().createNamedQuery(
				"Entry.findAll");		
		List<Entry> list = query.getResultList();
		List<Entry> entries = new ArrayList<Entry>();
		if(list != null && list.size() > 0){
			for(Entry e: list){
				if(e.getParents().size() == 0) entries.add(e);
			}
		}
		return entries;
	}
	
	@Factory("standStatuses")
	public List<StandStatus> loadStandStatus() {
		return Arrays.asList(StandStatus.values());
	}

	@Factory(value = "markets")
	@SuppressWarnings("unchecked")
	public List<Market> loadMarkets() {
		Query query = getPersistenceContext().createNamedQuery("Market.findAll");
		return query.getResultList();
	}

	@Factory(value = "standTypes")
	@SuppressWarnings("unchecked")
	public List<String> loadStandTypes() {
		Query query = getPersistenceContext().createNamedQuery("StandType.findAll");
		return query.getResultList();
	}
	
	@Factory(value = "fiscalPeriodNames")
	@SuppressWarnings("unchecked")
	public List<String> findFiscalPeriodNames() {
		Query query = getPersistenceContext().createNamedQuery("FiscalPeriod.findAllNames");
		return query.getResultList();
	}
	
	@Factory("budgetEntryTypes")
	public List<BudgetEntryType> loadBudgetEntryTypes() {
		Query query = getPersistenceContext().createNamedQuery("BudgetEntryType.findAll");
		return query.getResultList();		
	}
	
	@Factory("budgetEntryTypesNames")
	public List<BudgetEntryType> loadBudgetEntryTypesNames() {
		Query query = getPersistenceContext().createNamedQuery("BudgetEntryType.findAllNames");
		return query.getResultList();		
	}
	
	@SuppressWarnings("unchecked")
	@Factory("productTypes")
	public List<ProductType> findProductTypes() {
		Query query = this.getEntityManager().createNamedQuery(
				"ProductType.findAll");
		return query.getResultList();
	}
	
	@Factory("unitStatuses")
	public List<UnitStatus> loadUnitStatus() {
		return Arrays.asList(UnitStatus.values());
	}

	@SuppressWarnings("unchecked")
	@Factory("unitTypes")
	public List<UnitType> loadUnitTypes() {
		Query query = this.getEntityManager().createNamedQuery("UnitType.findAll");
		return (List<UnitType>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Factory("unitTypesName")
	public List<UnitType> loadUnitTypesName() {
		Query query = this.getEntityManager().createNamedQuery("UnitType.findAllNames");
		return (List<UnitType>) query.getResultList();
	}
	
	@Factory("legalStatuses")
	public List<LegalStatus> findLegalStatuses() {
		return LegalStatus.getRestrictedLegalStatuses();
	}

	@SuppressWarnings("unchecked")
	@Factory("creditNoteTypes")
	public List<CreditNoteType> findCreditNoteTypes() {
		Query query = getEntityManager().createNamedQuery("CreditNoteType.findAll");
		return query.getResultList();
	}
	
	@Factory("devolutionPaymentTypes")
	public List<PaymentType> findDevolutionPaymentTypes() {
		return Arrays.asList(PaymentType.getDevolutionPaymentTypes());
	}
	
	/*@Factory("defaultProvince")
	public TerritorialDivision findDefaultProvince(){
		CadasterService cadasterService = ServiceLocator.getInstance().findResource(CadasterService.LOCAL_NAME);
		return cadasterService.findDefaultProvince();
	}*/
	
	@SuppressWarnings("unchecked")
	@Factory("exemptionTypes")
	public List<ExemptionType> loadExemptionTypes() {
		Query query = getEntityManager().createNamedQuery("ExemptionType.findAll");
		return query.getResultList();
	}
	
	@Factory("businessTypes")
	@SuppressWarnings("unchecked")
	public List<BusinessType> findBusinessTypes(){
		Query query = getPersistenceContext().createNamedQuery("BusinessType.findAllActive");
		return query.getResultList();
	}
	
	@Factory("featureCategories")
	@SuppressWarnings("unchecked")
	public List<FeatureCategory> findFeatureCategories(){
		Query query = getPersistenceContext().createNamedQuery("FeatureCategory.findAllActive");
		return query.getResultList();
	}
	
	@Factory("businessCatalogs")
	@SuppressWarnings("unchecked")
	public List<BusinessCatalog> findBusinessCatalogs(){
		Query query = getPersistenceContext().createNamedQuery("BusinessCatalog.findAllFeatureActive");
		return query.getResultList();
	}
//Autor: Jock Samaniego M.
//Para buscar los tipos de negocios referentes a turismo.
	@Factory("businessTouristNames")
	@SuppressWarnings("unchecked")
	public List<BusinessCatalog> findBusinessTouristNames(){
		Query query = getPersistenceContext().createNamedQuery("BusinessCatalog.findAllTourist");
		return query.getResultList();
	}
	
	//Para buscar solo los nombres de negocios referentes a turismo.
		@Factory("businessTouristOnlyNames")
		@SuppressWarnings("unchecked")
		public List<String> findBusinessTouristOnlyNames(){
			Query query = getPersistenceContext().createNamedQuery("BusinessCatalog.findAllTouristOnlyNames");
			return query.getResultList();
		}
//------------------------------------------------------------------------------------------------------	
	
	@Factory("fireRatesNames")
	@SuppressWarnings("unchecked")
	public List<FireRates> findFireRatesNames(){
		Query query = getPersistenceContext().createNamedQuery("FireNames.findAllNames");
		return query.getResultList();
	}
	
		
	@Factory("businessNames")
	@SuppressWarnings("unchecked")
	public List<String> findBusinessNames(){
		Query query = getPersistenceContext().createNamedQuery("BusinessCatalog.findAllNames");
		return query.getResultList();
	}
	
	
	@Factory("businessNamesTourism")
	@SuppressWarnings("unchecked")
	public List<BusinessCatalog> findBusinessTourism(){
		Query query = getPersistenceContext().createNamedQuery("BusinessCatalog.findAllNamesTourism");
		return query.getResultList();
	}

	@Factory("crematoriums")
	@SuppressWarnings("unchecked")
	public List<Crematorium> loadCrematoriums(){
		Query query = getPersistenceContext().createNamedQuery("Crematorium.findAll");
		return query.getResultList();
	}

	@Factory("crematoriumNames")
	@SuppressWarnings("unchecked")
	public List<Crematorium> loadCrematoriumNames(){
		Query query = getPersistenceContext().createNamedQuery("Crematorium.findAllName");
		return query.getResultList();
	}

	@Factory("bodyTypes")
	public List<BodyType> loadBodyTypes() {
		return Arrays.asList(BodyType.values());
	}

	@Factory("genders")
	public List<Gender> loadGenders() {
		return Arrays.asList(Gender.values());
	}

	@Factory("maritalStatuses")
	public List<MaritalStatus> loadMaritalStatuses() {
		return Arrays.asList(MaritalStatus.values());

	}

	@Factory("legalEntityTypes")
	public List<LegalEntityType> loadLegalEntityTypes() {
		return Arrays.asList(LegalEntityType.values());
	}

	@Factory("alertPriorities")
	public List<AlertPriority> loadAlertPrioritys() {
		return Arrays.asList(AlertPriority.values());
	}	

	@Factory("alertTypes")
	public List<AlertType> findAlertTypes(){
		Query query = this.getEntityManager().createNamedQuery("AlertType.findAllByActive");
		return query.getResultList();
	}
	
	@Factory("statusElectronicReceipts")
	public List<StatusElectronicReceipt> loadStatusElectronicReceipts() {
		return Arrays.asList(StatusElectronicReceipt.values());
	}
	
	@Factory("usersCoercives")
	public List<Person> findUsersCoercives(){		 
		 if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		 String userCoerciveRole = systemParameterService.findParameter("ROLE_NAME_COERCIVE");	 
		 Query query = this.getEntityManager().createNamedQuery("Person.findByRoleName");
		 query.setParameter("roleName", userCoerciveRole);
		 return query.getResultList();
	}
	
	@Factory("futuresFiscalPeriods")
	public List<FiscalPeriod> findFutureFiscalPeriods(){
		Query query1 = this.getEntityManager().createNamedQuery("FiscalPeriod.findCurrent");
		query1.setParameter("currentDate", new Date());
		FiscalPeriod currentFiscalPeriod = (FiscalPeriod) query1.getSingleResult();		
		if(currentFiscalPeriod==null){
			return null;
		}
		Query query = this.getEntityManager().createQuery("select f from FiscalPeriod f where f.startDate>:endDateCurrentFiscalP order by f.startDate");
		query.setParameter("endDateCurrentFiscalP", currentFiscalPeriod.getEndDate());
		return query.getResultList();
	}

	//macartuche
	//emision2020
	@SuppressWarnings("unchecked")
	@Factory("risks")
	public List<AffectationFactor> loadRisk() {
		Query query = this.getEntityManager().createNamedQuery(
				"AffectationFactor.findByCategoryAndStatus");
		query.setParameter("code", "RISK");
		query.setParameter("status", true);
		List<AffectationFactor> list = query.getResultList();
		//System.out.println("============>"+list.size());
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Factory("threat")
	public List<AffectationFactor> loadThreat() {
		Query query = this.getEntityManager().createNamedQuery(
				"AffectationFactor.findByCategoryAndStatus");
		query.setParameter("code", "THREAT");
		query.setParameter("status", true);
		return (List<AffectationFactor>) query.getResultList();
	}
	
	//jock samaniego
	//emision2020
	
	@SuppressWarnings("unchecked")
	@Factory("sidewalk")
	public List<AffectationFactor> hasSideWalk() {
		Query query = this.getEntityManager().createNamedQuery(
				"AffectationFactor.findByCategoryAndStatus");
		query.setParameter("code", "SIDEWALK");
		query.setParameter("status", true);
		return (List<AffectationFactor>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Factory("curb")
	public List<AffectationFactor> hasCurb() {
		Query query = this.getEntityManager().createNamedQuery(
				"AffectationFactor.findByCategoryAndStatus");
		query.setParameter("code", "CURB");
		query.setParameter("status", true);
		return (List<AffectationFactor>) query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Factory("garbagecollected")
	public List<AffectationFactor> hasGarbageCollected() {
		Query query = this.getEntityManager().createNamedQuery(
				"AffectationFactor.findByCategoryAndStatus");
		query.setParameter("code", "GARBAGE_COLLECT");
		query.setParameter("status", true);
		return (List<AffectationFactor>) query.getResultList();
	}

	//macartuche 2021-07-021 10:50am
	//descuentos CEM tercera edad y discapacidad
	@SuppressWarnings("unchecked")
	@Factory("exemptionCEMTypes")
	public List<ItemCatalog> loadCEMExemptionTypes() {
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		
		//Query query = this.getEntityManager().createNamedQuery("Cementery.findAll");
		return (List<ItemCatalog>) itemCatalogService
				.findItemsForCatalogCode(CatalogConstants.CATALOG_TYPES_EXEMPTION_CEM);
	}
}
