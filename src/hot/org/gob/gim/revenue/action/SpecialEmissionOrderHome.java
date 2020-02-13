package org.gob.gim.revenue.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.cadaster.action.PropertyList;
import org.gob.gim.cadaster.facade.CadasterService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.Gim;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.facade.RevenueService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.cadaster.model.Block;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.TerritorialDivision;
import ec.gob.gim.cadaster.model.UnbuiltLot;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondView;

@Name("specialEmissionOrderHome")
@Scope(ScopeType.CONVERSATION)
public class SpecialEmissionOrderHome extends EntityHome<EmissionOrder> {

	@In(create = true, value = "defaultProvince")
	TerritorialDivision province;

	@In(create = true, value = "defaultCanton")
	TerritorialDivision canton;

	@In(create = true)
	PropertyList propertyList;

	@In
	UserSession userSession;
	
	@In
	Gim gim;
	
	@In(create = true)
	MunicipalBondHome municipalBondHome;
	
	@In
	FacesMessages facesMessages;
	
	public static String CADASTER_SERVICE_NAME = "/gim/CadasterService/local";
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private CadasterService cadasterService;
	private RevenueService revenueService;
	
	private Resident resident;
	private Entry entry;	
	private String systemParameterPropertyType = "";	
	public boolean isFirstTime = true;
	private SystemParameterService systemParameterService;
	private FiscalPeriod fiscalPeriod;
	private TerritorialDivision parish;
	private TerritorialDivision zone;
	private TerritorialDivision sector;
	private Block block;	
	private MunicipalBond municipalBond;
	private boolean isUrbanProperty;
	private Property property = new Property();
	private Integer buildingNumber;
	private Integer floorNumber;
	private Integer housingUnitNumber;
	private String rusticPropertyCode;
	private String cadastralCode;
	private MunicipalBondStatus rejectedBondStatus;
	
	private List<MunicipalBondView> municipalBonds;	
	private List<MunicipalBond> municipalBondsView;
	private List<String> rusticPropertiesNumber = new ArrayList<String>();
	private List<TerritorialDivision> sectors;
	private List<Block> blocks;

	public void setEmissionOrderId(Long id) {
		setId(id);
	}

	public Long getEmissionOrderId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}	 
	 	 
	 private void loadMunicipalBonds(){
		 Query query = getEntityManager().createNamedQuery("EmissionOrder.findMunicipalBondViewByEmissionOrderId");
		 query.setParameter("id", getEmissionOrderId());
		 municipalBonds = query.getResultList();
	 }
	 
	public void wire() {
		if(isFirstTime){
			populateCadastralCode();
			isFirstTime = false;
			if(getEmissionOrderId() != null){
				loadMunicipalBonds();
			}else{
				getInstance();
			}	
		}			
	}

	public boolean isWired() {
		return true;
	}

	public EmissionOrder getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void beforeViewMunicipalBond(MunicipalBond m) {
		this.municipalBond = m;
	}

	public void populateCadastralCode() {
		StringBuffer cadastralCodeBuffer = new StringBuffer();
		cadastralCodeBuffer.append(province.getCode());
		cadastralCodeBuffer.append(canton.getCode());
		cadastralCodeBuffer.append(parish != null ? parish.getCode() : "0000");
		cadastralCodeBuffer.append(zone != null ? zone.getCode() : "00");
		cadastralCodeBuffer.append(sector != null ? sector.getCode() : "00");
		cadastralCodeBuffer.append(block != null && block.getId() != null ? block.getCode() : "00");
		cadastralCodeBuffer.append(property != null && property.getId() != null ? property.getFormattedNumber() : "00");
		cadastralCodeBuffer.append(getBuildingNumber() != null ? getNumberFormat().format(getBuildingNumber()) : "00");
		cadastralCodeBuffer.append(floorNumber != null ? getNumberFormat().format(floorNumber) : "00");
		cadastralCodeBuffer.append(housingUnitNumber != null ? getNumberFormat().format(housingUnitNumber) : "00");
		setCadastralCode(cadastralCodeBuffer.toString());
	}

	public void populateRusticCadastralCode() {		
		StringBuffer cadastralCodeBuffer = new StringBuffer();
		cadastralCodeBuffer.append(province.getCode());
		cadastralCodeBuffer.append(canton.getCode());
		cadastralCodeBuffer.append(parish != null ? parish.getCode() : "0000");
		//cadastralCodeBuffer.append("000000000");
		cadastralCodeBuffer.append(rusticPropertyCode != null ? getRusticPropertyNumberFormat().format(Long.parseLong(getRusticPropertyCode())) : "000000");
		setCadastralCode(cadastralCodeBuffer.toString());		
	}

	private java.text.NumberFormat getRusticPropertyNumberFormat() {
		java.text.NumberFormat numberFormat = new java.text.DecimalFormat("00000000000");
		numberFormat.setMaximumIntegerDigits(11);
		return numberFormat;
	}

	private java.text.NumberFormat getNumberFormat() {
		java.text.NumberFormat numberFormat = new java.text.DecimalFormat("00");
		numberFormat.setMaximumIntegerDigits(2);
		return numberFormat;
	}

	public void resetAll() {
		zone = null;
		resetSector();
	}

	public void resetPropertiesNumber() {		
		if (parish == null) {
			populateRusticCadastralCode();
			rusticPropertiesNumber = new ArrayList<String>();
			return;
		}
		populateRusticCadastralCode();
		setRusticPropertyCode(null);
		populateRusticPropertiesNumber();
		
	}

	public void populateRusticPropertiesNumber() {
		List<String> codes = findCadastralCodeByType(cadastralCode.substring(0, 9), "PROPERTY_TYPE_ID_RUSTIC");
		
		rusticPropertiesNumber = new ArrayList<String>();
		for(String c: codes){
			rusticPropertiesNumber.add(Long.parseLong(c.substring(9)) + "");
		}
		
	}
	
	public List<String> findCadastralCodeByType(String cadastralCode, String systemParameterName){
		
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Long idType = systemParameterService.findParameter(systemParameterName);		
		
		List<?> list = getPersistenceContext().createNamedQuery("Property.findCadastralCodeByType").setParameter("cadastralCode", cadastralCode)
		.setParameter("idType", idType).getResultList();
		if(list != null){
			return (ArrayList<String>)list;
		}
		return new ArrayList<String>();
	}

	public String emitEmissionOrder(EmissionOrder emissionOrder) {
		try{
			if (systemParameterService == null)
				systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
						
			if(revenueService == null) revenueService = ServiceLocator.getInstance().findResource(RevenueService.LOCAL_NAME);
			revenueService.emit(emissionOrder.getId(), userSession.getPerson(), userSession.getFiscalPeriod().getStartDate());
			return "updated";			
		} catch(Exception e){
			addFacesMessageFromResourceBundle("emissionOrder.errorWhileEmitting");
			return null;
		}
	}	
	
	
	public String emitEmissionOrder(List<EmissionOrder> orders) {
		for(EmissionOrder eo :orders){
			if(eo.getIsSelected()){
				//System.out.println("------------------------- ok "+eo.getId());
			}
		}
		return null;
		/*try{
			if (systemParameterService == null)
				systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
						
			if(revenueService == null) revenueService = ServiceLocator.getInstance().findResource(RevenueService.LOCAL_NAME);
			revenueService.emit(emissionOrder.getId(), userSession.getUser().getId());			
			return "updated";			
		} catch(Exception e){
			addFacesMessageFromResourceBundle("emissionOrder.errorWhileEmitting");
			return null;
		}*/
	}
	
	public String rejectEmissionOrder(EmissionOrder emissionOrder) {		
		try{
			if (systemParameterService == null)
				systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
				
			if(revenueService == null) revenueService = ServiceLocator.getInstance().findResource(RevenueService.LOCAL_NAME);
			rejectedBondStatus = systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_REJECTED");			
			updateStatus(findMunicipalBondIds(emissionOrder.getId()), rejectedBondStatus);
			emissionOrder.setIsDispatched(true);
			this.setInstance(emissionOrder);			
			return super.update();			
		} catch(Exception e){
			addFacesMessageFromResourceBundle("emissionOrder.errorWhileEmitting");
			return null;
		}
	}
	
	private List<Long> findMunicipalBondIds(Long emissionOrderId){
		Query query = getEntityManager().createNamedQuery("EmissionOrder.MunicipalBondsIdsByEmissionOrderId");
		query.setParameter("id", emissionOrderId);		
		return query.getResultList();
	}
	
	private void updateStatus(List<Long> selected, MunicipalBondStatus status) {
		revenueService.update(selected, status.getId(), userSession.getUser().getId(), null, null);
	}	
	
	public String sendMunicipalBond(MunicipalBond m){
		municipalBondHome.setInstance(m);
		municipalBondHome.setFromUrbanProperty(true);
		return "ready";
	}

	public void resetPropety() {
		property = null;
		resetBuilding();
	}

	public void resetBuilding() {
		buildingNumber = null;
		resetFloor();
	}

	public void resetFloor() {
		floorNumber = null;
		resetHousingNumber();
	}

	public void resetHousingNumber() {
		housingUnitNumber = null;
		populateCadastralCode();
	}

	public void resetCadastralCode() {
		setCadastralCode(null);
	}

	public void resetSector() {
		sector = null;
		resetBlock();
	}

	public List<TerritorialDivision> findZones() {
		if (parish != null) {
			return findTerritorialDivisions(parish.getId());
		}
		return new ArrayList<TerritorialDivision>();
	}

	public List<TerritorialDivision> populateSectors() {
		if (this.zone != null && this.zone.getId() != null) {
			sectors = findTerritorialDivisions(zone.getId());
		} else {
			sectors = new ArrayList<TerritorialDivision>();
		}

		if (block == null)
			setBlock(new Block());
		return sectors;		
	}

	@SuppressWarnings("unchecked")
	public List<Block> populateBlocks() {
		if (sector != null && sector.getId() != null) {
			Query query = getEntityManager().createNamedQuery(
					"Block.findBySector");
			query.setParameter("sectorId", sector.getId());
			setBlocks((List<Block>) query.getResultList());
		} else {
			setBlocks(new ArrayList<Block>());
		}

		return getBlocks();
	}

	@SuppressWarnings("unchecked")
	public List<Property> populateProperties() {
		List<Property> properties = new ArrayList<Property>();		
		if (block != null && block.getId() != null) {
			List<Integer> numbers = new ArrayList<Integer>();
			for (Property p : block.getProperties()) {
				numbers.add(p.getNumber());
			}
			Collections.sort(numbers);
			for(Integer i : numbers){
				for (Property p : block.getProperties()) {
					if (p.getNumber() == i)
						properties.add(p);
				}
			}			
			return properties;
		} else {
			return new ArrayList<Property>();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Integer> populateBuildings() {
		List<Integer> buildings = new ArrayList<Integer>();
		List<String> codes = getPersistenceContext().createNamedQuery("Property.findCadastralCodeByPartCadastralCode")
				.setParameter("criteria",cadastralCode.substring(0, cadastralCode.length() - 6)).getResultList();
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i).substring(codes.get(i).length() - 6,codes.get(i).length() - 4);
			if (!code.equals("00")) {
				buildings.add(new Integer(code));
			}
		}
		return buildings;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> populateFloors() {
		List<Integer> floors = new ArrayList<Integer>();
		List<String> codes = getPersistenceContext().createNamedQuery("Property.findCadastralCodeByPartCadastralCode")
				.setParameter("criteria",cadastralCode.substring(0, cadastralCode.length() - 4)).getResultList();
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i).substring(codes.get(i).length() - 4,codes.get(i).length() - 2);
			if (!code.equals("00")) {
				floors.add(new Integer(code));
			}
		}
		return floors;
	}

	public List<Integer> populateHousingNumber() {
		List<Integer> numbers = new ArrayList<Integer>();
		List<String> codes = getPersistenceContext().createNamedQuery("Property.findCadastralCodeByPartCadastralCode")
				.setParameter("criteria",cadastralCode.substring(0, cadastralCode.length() - 2)).getResultList();
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i).substring(codes.get(i).length() - 2);
			if (!code.equals("00")) {
				numbers.add(new Integer(code));
			}
		}
		return numbers;
	}

	public void resetBlock() {
		setBlock(new Block());
		resetPropety();
	}

	public List<TerritorialDivision> findParishes(Long defaultCantonId) {
		return findTerritorialDivisions(defaultCantonId);
	}

	@SuppressWarnings("unchecked")
	private List<TerritorialDivision> findTerritorialDivisions(Long parentId) {
		Query query = getPersistenceContext().createNamedQuery("TerritorialDivision.findByParentAndSpecial");
		query.setParameter("parentId", parentId);
		query.setParameter("type", "SPECIAL");
		return query.getResultList();
	}
	
	public String formCadastralCode() {
		StringBuffer cadastralCodeBuffer = new StringBuffer();
		cadastralCodeBuffer.append(province.getCode());
		cadastralCodeBuffer.append(canton.getCode());
		
		if (parish != null) {
			cadastralCodeBuffer.append(parish.getCode());
		} else {
			return cadastralCodeBuffer.toString();
		}
		
		if(!isUrbanProperty && rusticPropertyCode == null){
			return cadastralCodeBuffer.toString();
		}
		
		if(!isUrbanProperty && rusticPropertyCode != null){
			//cadastralCodeBuffer.append("00000");
			//System.out.println("--*-*-*-*-*-*-*:"+getRusticPropertyNumberFormat().format(Long.parseLong(rusticPropertyCode)));
			return cadastralCodeBuffer.append(getRusticPropertyNumberFormat().format(Long.parseLong(rusticPropertyCode))).toString();
		}

		if (zone != null) {
			cadastralCodeBuffer.append(zone.getCode());
		} else {
			return cadastralCodeBuffer.toString();
		}

		if (sector != null) {
			cadastralCodeBuffer.append(sector.getCode());
		} else {
			return cadastralCodeBuffer.toString();
		}

		if (block != null) {
			cadastralCodeBuffer.append(block.getCode());
		} else {
			return cadastralCodeBuffer.toString();
		}

		if (property != null) {
			cadastralCodeBuffer.append(property.getFormattedNumber());
		} else {
			return cadastralCodeBuffer.toString();
		}

		if (buildingNumber != null) {
			cadastralCodeBuffer.append(getNumberFormat().format(
					getBuildingNumber()));
		} else {
			return cadastralCodeBuffer.toString();
		}

		if (floorNumber != null) {
			cadastralCodeBuffer.append(getNumberFormat().format(floorNumber));
		} else {
			return cadastralCodeBuffer.toString();
		}

		if (housingUnitNumber != null) {
			cadastralCodeBuffer.append(getNumberFormat().format(
					housingUnitNumber));
		}

		return cadastralCodeBuffer.toString();

	}
	
	public void loadMunicipalBonds(MunicipalBond m){		
		municipalBondsView = new ArrayList<MunicipalBond>();
		if(m == null) return;		
		municipalBondsView.add(m);		
		resident = m.getResident();
	}
		
	public String saveEmissionOrder() {
		return persist();
	}
	
	private void loadEntry(){
		if(entry != null) return;
		systemParameterPropertyType = "";
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		if(isUrbanProperty){
			entry = systemParameterService.materialize(Entry.class, "ENTRY_ID_URBAN_PROPERTY");
			systemParameterPropertyType = "PROPERTY_TYPE_ID_URBAN";
		}else{
			entry = systemParameterService.materialize(Entry.class, "ENTRY_ID_RUSTIC_PROPERTY");
			systemParameterPropertyType = "PROPERTY_TYPE_ID_RUSTIC";
		}
	}
	
	private Long searchResidentId(String identificationNumber) {
		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();			
			if(resident != null) return resident.getId();
			if (resident == null) {
				addFacesMessageFromResourceBundle("resident.notFound");
			}
		} catch (Exception e) {
			addFacesMessageFromResourceBundle("resident.notFound");
		}
		return null;
	}

	
	private Long findDefaultInstitutionId(){		
		String identification = gim.getInstitution().getNumber();
		return searchResidentId(identification);
	}
		
	private void createEmissionOrder(String cadastralCode, List<Long> noEmitFor) throws Exception {		
		//@tag predioColoma
		//solo para cargar el entry como rustico
		isUrbanProperty = false;
		loadEntry();  
		
		//@tag predioColoma
		isUrbanProperty = true;
		this.getInstance().setServiceDate(fiscalPeriod.getStartDate());
		this.getInstance().setDescription(entry.getName() + ": " + cadastralCode);
		this.getInstance().setEmisor(userSession.getPerson());
		
		//agregado macartuche
		//2015-12-18
		//para realizar preemision de sinat, caso contrario 
		//sea urbano o quintas obtiene propiedades de la forma normal del gim
		//1 = quintas
		//2 = sinat
		
		//2016-12-07T17:12:00
		//
		List<Property> properties =null;
		if(emissionRusticType==2){
			properties = findPropertiesBySectorName(noEmitFor); 
		}else{
			//@tag predioColoma
			systemParameterPropertyType = "PROPERTY_TYPE_ID_URBAN";
			//fin @tag
			properties = cadasterService.findPropertyByCadastralCodeAndType(cadastralCode,systemParameterPropertyType, noEmitFor);
		}
		

		//special
		//@tag precioColoma
		isUrbanProperty = false;
		boolean isSpecial =true;
		List<MunicipalBond> mb = cadasterService.onlyCalculatePreEmissionOrderPropertyTax(this.getInstance(), entry, properties, fiscalPeriod, userSession.getPerson(), isUrbanProperty, isSpecial);				 
		if(mb != null) this.getInstance().getMunicipalBonds().addAll(mb);
		isUrbanProperty = true;
	}
	

	
	@SuppressWarnings("unchecked")
	private List<UnbuiltLot> findByFiscalPeriod(Long fiscalPeriodId){
		Query query = getEntityManager().createNamedQuery("UnbuiltLot.findByFiscalPeriod");		
		query.setParameter("fiscalPeriodId", fiscalPeriodId);
		return query.getResultList();		
	}

	
	public void calculateEmissionOrderUnbuilLots() throws Exception {
		if (cadasterService == null) cadasterService = ServiceLocator.getInstance().findResource(CADASTER_SERVICE_NAME);
		
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		
		this.getInstance().setMunicipalBonds(new ArrayList<MunicipalBond>());
			
		try {
			BigDecimal minimumAppraisal = BigDecimal.ZERO;
			minimumAppraisal = fiscalPeriod.getBasicSalary().multiply(new BigDecimal(25));
			//System.out.println("****************************");
			/******************************ATENCION REVISAR CON TODOS ANTES DE SUBIR **************************/
			List<UnbuiltLot> unbuildLots = findByFiscalPeriodAndMinAppraisal(fiscalPeriod.getId(),minimumAppraisal);
			//removeUnbuilLotMinimumAppraisalAndIsEmited(unbuildLots, fiscalPeriod);
			/******************************ATENCION REVISAR CON TODOS ANTES DE SUBIR **************************/
			//System.out.println("Total solares modificando llamada a remover: "+unbuildLots.size());
			createEmissionOrder(unbuildLots);
			 
//			createEmissionOrder(findByFiscalPeriod(fiscalPeriod.getId()));
		} catch (Exception e) {
			addFacesMessageFromResourceBundle("emissionOrder.error");			
		}
	}
	
 
	/**
	 * Cambiar funcion para obtener los solares no edificados que cumplan 
	 * con la condiciones de tner un avaluo mayor o igual a 25SBU
	 * @author mack
	 * @date 2015/12/08 16:37:37
	 * @param fiscalPeriodId
	 * @return
	 */
	private List<UnbuiltLot> findByFiscalPeriodAndMinAppraisal(Long fiscalPeriodId, BigDecimal minimumAppraisal){
		Query query = getEntityManager().createNamedQuery("UnbuiltLot.findByFiscalPeriodAndMinAppraisal");		
		query.setParameter("fiscalPeriodId", fiscalPeriodId);
		query.setParameter("minimumAppraisal", minimumAppraisal);
		query.setParameter("emited", Boolean.FALSE);
		return query.getResultList();		
	}
	
	/**POSIBLEMENTE MEJORA CON CONSULTA ***/
	private void removeUnbuilLotMinimumAppraisalAndIsEmited(List<UnbuiltLot> unbuildLots, FiscalPeriod fiscalPeriod){
		BigDecimal minimumAppraisal = BigDecimal.ZERO;
		minimumAppraisal = fiscalPeriod.getBasicSalary().multiply(new BigDecimal(25));
		List<UnbuiltLot> listRemove = new ArrayList<UnbuiltLot>();
		for (UnbuiltLot unbuiltLot : unbuildLots) {
			if ((unbuiltLot.getProperty().getCurrentDomain().getLotAppraisal().compareTo(minimumAppraisal) < 1) || (unbuiltLot.isEmited())){
				listRemove.add(unbuiltLot);
			}
		}
		for (UnbuiltLot unbuiltLotRemove : listRemove) {
			unbuildLots.remove(unbuiltLotRemove);
		}
		//System.out.println("<<<<<R>>>>>removeUnbuilLotMinimumAppraisal EXIT");
	}
	
	private void loadUnbuiltLotEntry(){
		if(entry != null) return;
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		
		entry = systemParameterService.materialize(Entry.class, "ENTRY_ID_RENT_UNBUILTLOT");
		
	}
	
	private void createEmissionOrder(List<UnbuiltLot> unbuiltLots) throws Exception {
		loadUnbuiltLotEntry();
		this.getInstance().setServiceDate(fiscalPeriod.getStartDate());
		this.getInstance().setDescription(entry.getName());
		this.getInstance().setEmisor(userSession.getPerson());				
		List<MunicipalBond> mb = cadasterService.onlyCalculatePreEmissionOrderUnbuiltLotTax(this.getInstance(), entry, unbuiltLots, fiscalPeriod, userSession.getPerson());		
		if(mb != null) this.getInstance().getMunicipalBonds().addAll(mb);
	}
	
	public void calculateTotalEmissionOrder() throws Exception {
		if (cadasterService == null) cadasterService = ServiceLocator.getInstance().findResource(CADASTER_SERVICE_NAME);		
		if (systemParameterService == null) systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		this.getInstance().setMunicipalBonds(new ArrayList<MunicipalBond>());
		List<Long> list = new ArrayList<Long>();
		Long defaultInstitutionId = findDefaultInstitutionId();  
		if(defaultInstitutionId != null)list.add(defaultInstitutionId);
		try {
			createEmissionOrder(formCadastralCode(), list);			 
		} catch (Exception e) {	 
			String message = Interpolator.instance().interpolate("#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
		}
	}	
	
	public void calculateExemptions() {		
		if(fiscalPeriod == null){
			String message = Interpolator.instance().interpolate("#{messages['property.errorGenerateTax']}", new Object[0]);
			facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
			return;
		}
//		if(creationDate == null){
//			String message = Interpolator.instance().interpolate("#{messages['property.errorGenerateTax']}", new Object[0]);
//			facesMessages.addToControl("",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);
//			return;
//		}
//		
		if(systemParameterService == null)systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		if(cadasterService == null) cadasterService = ServiceLocator.getInstance().findResource(CadasterService.LOCAL_NAME);
		cadasterService.calculateExemptions(fiscalPeriod.getId(), fromDate, untilDate);
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setParish(TerritorialDivision parish) {
		this.parish = parish;
	}

	public TerritorialDivision getParish() {
		return parish;
	}

	public void setZone(TerritorialDivision zone) {
		this.zone = zone;
	}

	public TerritorialDivision getZone() {
		return zone;
	}

	public void setSector(TerritorialDivision sector) {
		this.sector = sector;
	}

	public TerritorialDivision getSector() {
		return sector;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public List<Block> getBlocks() {
		return blocks;
	}
		
	public void setFloorNumber(Integer floorNumber) {
		this.floorNumber = floorNumber;
	}

	public Integer getFloorNumber() {
		return floorNumber;
	}

	public void setHousingUnitNumber(Integer housingUnitNumber) {
		this.housingUnitNumber = housingUnitNumber;
	}

	public Integer getHousingUnitNumber() {
		return housingUnitNumber;
	}

	public void setBuildingNumber(Integer buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public Integer getBuildingNumber() {
		return buildingNumber;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}
	
	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setUrbanProperty(boolean isUrbanProperty) {
		this.isUrbanProperty = isUrbanProperty;
	}

	public boolean isUrbanProperty() {
		return isUrbanProperty;
	}

	public void setRusticPropertiesNumber(List<String> rusticPropertiesNumber) {
		this.rusticPropertiesNumber = rusticPropertiesNumber;
	}

	public List<String> getRusticPropertiesNumber() {
		return rusticPropertiesNumber;
	}

	public void setRusticPropertyCode(String rusticPropertyCode) {
		this.rusticPropertyCode = rusticPropertyCode;
	}

	public String getRusticPropertyCode() {		
		return rusticPropertyCode;
	}

	public List<MunicipalBondView> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBondView> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}
	
	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}
	
	public List<MunicipalBond> getMunicipalBondsView() {
		return municipalBondsView;
	}

	public void setMunicipalBondsView(List<MunicipalBond> municipalBondsView) {
		this.municipalBondsView = municipalBondsView;
	}

	//macartuche
	//2015-11-30
	private Date fromDate;
	private Date untilDate;
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(Date untilDate) {
		this.untilDate = untilDate;
	}
	
	//macartuche
	//2015-12-18
	//agregar campo para tipo de emision sinat o quintas
	//solo para predio rustico
	private Integer emissionRusticType=1; //por defecto
	private Long propertiesSize=0L;
	private List<String> parishList;
	private List<String> sectorNameList;
	private List<String> selectedSectors;
	private String selectedParish;
	private Boolean sinat;
	
	//@tag predioEspecial
	//@author macartuche
	//@date 2016-10-27 15:20
	@SuppressWarnings("unchecked")
	public List<TerritorialDivision> findParishesSpecial(Long defaultCantonId) {
		return findTerritorialDivisions(defaultCantonId);
	}
	
	@SuppressWarnings("unchecked")
	private List<TerritorialDivision> findTerritorialDivisionSpecial(Long parentId) {
		
		//falta hacer eso
		Query query = getPersistenceContext().createNamedQuery("TerritorialDivision.findByParent");
		query.setParameter("parentId", parentId);
		return query.getResultList();
	}
	
	//@end macartuche
	

	public List<String> findParishesSinat(){
		//LocationPropertySinat
		Query q = getEntityManager().createQuery("Select DISTINCT(lps.parishName) "
				+ "from LocationPropertySinat lps order by lps.parishName");
		return (List<String>)q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public void findSectorsByParish(){
		if(selectedParish==null) sectorNameList = new ArrayList<String>();
		Query q = getEntityManager().createQuery("Select DISTINCT(lps.sectorName) "
				+ "from LocationPropertySinat lps "
				+ "where lps.parishName=:parishName order by lps.sectorName");
		q.setParameter("parishName", selectedParish);		 
		sectorNameList = (List<String>)q.getResultList(); 	
		
		//fijar tambien el codigo de parroquia en el input
		q=getEntityManager().createQuery("Select DISTINCT(lps.parishCode) from LocationPropertySinat lps "
				+ "where lps.parishName=:parishName");
		q.setParameter("parishName", selectedParish);
		String code = (String)q.getSingleResult();
		setCadastralCode(code);	
	}
	
	
	public void setSinat(){
		if(emissionRusticType==1) sinat=false; else sinat=true;	
	}
	
	public void countProperties(){
		if(selectedSectors==null && selectedSectors.isEmpty()) return;
		Query q = getEntityManager().createQuery("Select count(lps.property) from LocationPropertySinat lps "
				+ "where lps.sectorName in :list and lps.parishName=:parishName");
		q.setParameter("list", selectedSectors);
		q.setParameter("parishName", selectedParish);
		
		propertiesSize = (Long)q.getSingleResult();
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Property> findPropertiesBySectorName(List<Long> noEmitFor){
		if(selectedSectors==null && selectedSectors.isEmpty()) return new ArrayList<Property>();
		Query q = getEntityManager().createNamedQuery("LocationPropertySinat.findSinat");
		q.setParameter("list", selectedSectors);
		q.setParameter("noEmitFor", noEmitFor);
		q.setParameter("parishName", selectedParish);
		
		return q.getResultList();
	}

	public Integer getEmissionRusticType() {
		return emissionRusticType;
	}

	public void setEmissionRusticType(Integer emissionRusticType) {
		this.emissionRusticType = emissionRusticType;
	}

	public Long getPropertiesSize() {
		return propertiesSize;
	}

	public void setPropertiesSize(Long propertiesSize) {
		this.propertiesSize = propertiesSize;
	}

	public List<String> getParishList() {
		return parishList;
	}

	public void setParishList(List<String> parishList) {
		this.parishList = parishList;
	}

	public List<String> getSectorNameList() {
		return sectorNameList;
	}

	public void setSectorNameList(List<String> sectorNameList) {
		this.sectorNameList = sectorNameList;
	}

	public List<String> getSelectedSectors() {
		return selectedSectors;
	}

	public void setSelectedSectors(List<String> selectedSectors) {
		this.selectedSectors = selectedSectors;
	}

	public String getSelectedParish() {
		return selectedParish;
	}

	public void setSelectedParish(String selectedParish) {
		this.selectedParish = selectedParish;
	}

	public Boolean getSinat() {
		return sinat;
	}

	public void setSinat(Boolean sinat) {
		this.sinat = sinat;
	} 
			
	/**
	 * --------------------- para fotomultas	
	 */
	private List<EmissionOrder> emissionOrders;
	private String identificationNumber;
	private String residentName;
	
	public void loadPending(){
		
		
		
		String EJBQL = "select e from EmissionOrder e "
		+"left join fetch e.municipalBonds m "
		+"left join fetch m.resident res "
		+"left join fetch m.receipt "
		+"left join fetch m.entry entry "
		+ "where ";
		//+ "(lower(m.resident.name) like lower(concat(#{emissionOrderList.resident},'%'))) "
		if (identificationNumber != null && !identificationNumber.equals("")) {
			EJBQL = EJBQL + "(lower(m.resident.identificationNumber) like lower(concat(:identificationNumber,'%'))) and ";	
		}
		EJBQL = EJBQL + " e.isDispatched= false and entry.id in (643,644) ";
		
		Query q=this.getEntityManager().createQuery(EJBQL);
		
		if(identificationNumber!=null && !identificationNumber.equals("")){
			q.setParameter("identificationNumber", identificationNumber);
		}
		//System.out.println("------------------- "+identificationNumber+"    -  "+residentName+"    "+q.getResultList().size());
		emissionOrders = q.getResultList();
	}

	public List<EmissionOrder> getEmissionOrders() {
		return emissionOrders;
	}

	public void setEmissionOrders(List<EmissionOrder> emissionOrders) {
		this.emissionOrders = emissionOrders;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}
	
	public void changeSelectedEmissionOrder(EmissionOrder eo, boolean selected){
		eo.setIsSelected(!selected);
	}
	
//Autor: Jock Samaniego
//Para emitir o rechazar multiples ordenes de fotomultas.
	public String multipleEmission(){
		int count=0;
		if (emissionOrders != null) {
			for (int i = 0; i < emissionOrders.size(); i++) {
				if (emissionOrders.get(i).getIsSelected() == true) {
					emitEmissionOrder(emissionOrders.get(i));
					//System.out.println("--------------------------orden de emision " + (count + 1) + " terminada");
					count++;
				}
			}
			
		}
		//System.out.println("---------------------------------"+count);
		loadPending();
		return "updated";
	}
	
	public String multipleReject(){
		int count = 0;
		if (emissionOrders != null) {
			for (int i = 0; i < emissionOrders.size(); i++) {
				if (emissionOrders.get(i).getIsSelected() == true) {
					rejectEmissionOrder(emissionOrders.get(i));
					//System.out							.println("---------------------------------orden de emision " + (count + 1) + " rechazada");
					count++;
				}
			}
		}
		//System.out.println("---------------------------------"+count);
		loadPending();
		return "updated";
	}
		
}
