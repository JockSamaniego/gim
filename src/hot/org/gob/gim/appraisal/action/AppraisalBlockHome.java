package org.gob.gim.appraisal.action;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.gob.gim.appraisal.facade.AppraisalService;
import org.gob.gim.cadaster.action.PropertyHome;
import org.gob.gim.cadaster.action.PropertyList;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.exception.InvoiceNumberOutOfRangeException;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.appraisal.model.AppraisalPeriod;
import ec.gob.gim.appraisal.model.AppraisalRossHeidecke;
import ec.gob.gim.appraisal.model.AppraisalTotalExternal;
import ec.gob.gim.appraisal.model.AppraisalTotalRoof;
import ec.gob.gim.appraisal.model.AppraisalTotalStructure;
import ec.gob.gim.appraisal.model.AppraisalTotalWall;
import ec.gob.gim.cadaster.model.Block;
import ec.gob.gim.cadaster.model.Building;
import ec.gob.gim.cadaster.model.PreservationState;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.cadaster.model.Sewerage;
import ec.gob.gim.cadaster.model.TerritorialDivision;

@Name("appraisalBlockHome")
public class AppraisalBlockHome extends EntityHome<Block> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create = true, value = "defaultProvince")
	TerritorialDivision province;

	@In(create = true, value = "defaultCanton")
	TerritorialDivision canton;

	@In
	FacesMessages facesMessages;

	public static String APPRAISAL_SERVICE_NAME = "/gim/AppraisalService/local";
//	private static String SYSTEM_PARAMETER_SERVICE_NAME = "/gimcatastro2014/SystemParameterService/local";

	private AppraisalPeriod appraisalPeriod;
	private TerritorialDivision parish;
	private TerritorialDivision zone;
	private TerritorialDivision sector;
	private Block block;

	private Property property = new Property();
	private Integer buildingNumber;
	private Integer floorNumber;
	private Integer housingUnitNumber;
	private String cadastralCode;
	private BigDecimal valueBySquareMeter = BigDecimal.ZERO;
	private List<TerritorialDivision> sectors;
	private List<Block> blocks;
	private List<Property> properties = new ArrayList<Property>();
	
	private boolean temporalValues = true;
	private boolean reviewValues = false;
	private boolean isFirstTime = true;
	private boolean lotValues;
	private boolean criteriaOpen = false;

//	private Map<String, AppraisalTotalStructure> mapTotalStructure = new HashMap<String, AppraisalTotalStructure>();
//	private Map<String, AppraisalTotalWall> mapTotalWall = new HashMap<String, AppraisalTotalWall>();
//	private Map<String, AppraisalTotalRoof> mapTotalRoof = new HashMap<String, AppraisalTotalRoof>();
//	private Map<String, AppraisalTotalExternal> mapTotalExternal = new HashMap<String, AppraisalTotalExternal>();
//	private Map<Long, AppraisalRossHeidecke> mapRossHeidecke = new HashMap<Long, AppraisalRossHeidecke>();
	private int anioAppraisal = new GregorianCalendar().get(Calendar.YEAR);

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		if(isFirstTime){
			populateCadastralCode();
			isFirstTime = false;
			properties = new ArrayList<Property>();
		}
		
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Block getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void populateCadastralCode() {
		StringBuffer cadastralCodeBuffer = new StringBuffer();
		cadastralCodeBuffer.append(province.getCode());
		cadastralCodeBuffer.append(canton.getCode());

		cadastralCodeBuffer.append(parish != null ? parish.getCode() : "00");
		cadastralCodeBuffer.append(zone != null ? zone.getCode() : "00");
		cadastralCodeBuffer.append(sector != null ? sector.getCode() : "00");
		cadastralCodeBuffer
				.append(block != null && block.getId() != null ? block
						.getCode() : "000");
		cadastralCodeBuffer
				.append(property != null && property.getId() != null ? property
						.getFormattedNumber() : "00");
		cadastralCodeBuffer
				.append(getBuildingNumber() != null ? getNumberFormat().format(
						getBuildingNumber()) : "00");
		cadastralCodeBuffer.append(floorNumber != null ? getNumberFormat()
				.format(floorNumber) : "00");
		cadastralCodeBuffer
				.append(housingUnitNumber != null ? getNumberFormat().format(
						housingUnitNumber) : "00");
		setCadastralCode(cadastralCodeBuffer.toString());
	}

	public String populateCadastralCodeSearch() {
		StringBuffer cadastralCodeBuffer = new StringBuffer();
		cadastralCodeBuffer.append(province.getCode());
		cadastralCodeBuffer.append(canton.getCode());

		cadastralCodeBuffer.append(parish != null ? parish.getCode() : "");
		cadastralCodeBuffer.append(zone != null ? zone.getCode() : "");
		cadastralCodeBuffer.append(sector != null ? sector.getCode() : "");
		cadastralCodeBuffer
				.append(block != null && block.getId() != null ? block
						.getCode() : "");
		cadastralCodeBuffer
				.append(property != null && property.getId() != null ? property
						.getFormattedNumber() : "");
		cadastralCodeBuffer
				.append(getBuildingNumber() != null ? getNumberFormat().format(
						getBuildingNumber()) : "");
		cadastralCodeBuffer.append(floorNumber != null ? getNumberFormat()
				.format(floorNumber) : "");
		cadastralCodeBuffer
				.append(housingUnitNumber != null ? getNumberFormat().format(
						housingUnitNumber) : "");
		return cadastralCodeBuffer.toString();
	}

	private java.text.NumberFormat getNumberFormat() {
		java.text.NumberFormat numberFormat = new java.text.DecimalFormat("00");
		numberFormat.setMaximumIntegerDigits(2);
		return numberFormat;
	}

	public void resetAll() {
		zone = null;
		valueBySquareMeter = BigDecimal.ZERO;
		setProperties(null);
		resetSector();
	}

	public void resetProperty(Block block) {
		property = null;
		this.setInstance(block);
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
		setProperties(null);
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
		// this.populateCadastralCode();
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

		// this.populateCadastralCode();
	}

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
		List<String> codes = getPersistenceContext()
				.createNamedQuery(
						"Property.findUrbanCadastralCodeByPartCadastralCode")
				.setParameter("criteria",
						cadastralCode.substring(0, cadastralCode.length() - 6))
				.getResultList();
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i).substring(codes.get(i).length() - 6,
					codes.get(i).length() - 4);
			if (!code.equals("00")) {
				buildings.add(new Integer(code));
			}
		}
		return buildings;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> populateFloors() {
		List<Integer> floors = new ArrayList<Integer>();
		List<String> codes = getPersistenceContext()
				.createNamedQuery(
						"Property.findUrbanCadastralCodeByPartCadastralCode")
				.setParameter("criteria",
						cadastralCode.substring(0, cadastralCode.length() - 4))
				.getResultList();
		for (int i = 0; i < codes.size(); i++) {
			String code = codes.get(i).substring(codes.get(i).length() - 4,
					codes.get(i).length() - 2);
			if (!code.equals("00")) {
				floors.add(new Integer(code));
			}
		}
		return floors; // this.populateCadastralCode();
	}

	@SuppressWarnings("unchecked")
	public List<Integer> populateHousingNumber() {
		List<Integer> numbers = new ArrayList<Integer>();
		List<String> codes = getPersistenceContext()
				.createNamedQuery(
						"Property.findUrbanCadastralCodeByPartCadastralCode")
				.setParameter("criteria",
						cadastralCode.substring(0, cadastralCode.length() - 2))
				.getResultList();
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
		resetProperty(this.getInstance());
	}

	public List<TerritorialDivision> findParishes(Long defaultCantonId) {
		return findTerritorialDivisions(defaultCantonId);
	}

	@SuppressWarnings("unchecked")
	private List<TerritorialDivision> findTerritorialDivisions(Long parentId) {
		Query query = getPersistenceContext().createNamedQuery(
				"TerritorialDivision.findByParent");
		query.setParameter("parentId", parentId);
		return query.getResultList();
	}

	public void setAppraisalPeriod(AppraisalPeriod appraisalPeriod) {
		this.appraisalPeriod = appraisalPeriod;
	}

	public AppraisalPeriod getAppraisalPeriod() {
		return appraisalPeriod;
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

	public BigDecimal getValueBySquareMeter() {
		return valueBySquareMeter;
	}

	public void setValueBySquareMeter(BigDecimal valueBySquareMeter) {
		this.valueBySquareMeter = valueBySquareMeter;
	}

	public void setBlocks(List<Block> blocks) {
		this.blocks = blocks;
	}

	public List<Block> getBlocks() {
		return blocks;
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

	public boolean isLotValues() {
		return lotValues;
	}

	public void setLotValues(boolean lotValues) {
		this.lotValues = lotValues;
	}

	public boolean isCriteriaOpen() {
		return criteriaOpen;
	}

	public void setCriteriaOpen(boolean criteriaOpen) {
		this.criteriaOpen = criteriaOpen;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	// @SuppressWarnings("unchecked")
	// public List<Property> findPropertyByCadastralCode(String cadastralCode){
	// Map<String, String> parameters = new HashMap<String, String>();
	// parameters.put("criteria", cadastralCode);
	// List<?> list =
	// crudService.findWithNamedQuery("Property.findByCadastralCode",parameters);
	// if(list != null){
	// return (ArrayList<Property>)list;
	// }
	// return new ArrayList<Property>();
	// }

	@SuppressWarnings("unchecked")
	public List<Property> findPropertyByCadastralCode(String cadastralCode) {
		Query query = this.getEntityManager().createNamedQuery(
				"Property.findUrbanByCadastralCodeAndNotDeleted");
		query.setParameter("criteria", cadastralCode);
		return (List<Property>) query.getResultList();
	}

	public void changeValuebySquareMeter() {
		// if(this.getInstance() != null){
		String cadastralCodePartial = populateCadastralCodeSearch();
		properties = findPropertyByCadastralCode(cadastralCodePartial);
		for (Property pro : properties) {
			System.out.println(";;;;;;;;;;;;;;;;;;;; changeValuebySquareMeter Clave Catastral: "+pro.getCadastralCode());
			if (isTemporalValues())
				pro.getCurrentDomain().setValueBySquareMeterTmp(valueBySquareMeter);
			else{
				if (pro.getCurrentDomain().getValueBySquareMeter() != null){
					if (pro.getCurrentDomain().getValueBySquareMeter().compareTo(valueBySquareMeter) < 0)
						pro.getCurrentDomain().setValueBySquareMeter(valueBySquareMeter);
				}else {
					pro.getCurrentDomain().setValueBySquareMeter(valueBySquareMeter);					
				}
			}
		}
	}

//	public BigDecimal getAppraisalRelationFactor(Property proper){
//		BigDecimal equivalencia = new BigDecimal(0);
//
//		if (proper.getFront().compareTo(proper.getSide()) == -1) //menor que
//			equivalencia = proper.getFront().divide(proper.getSide(), 4, RoundingMode.HALF_UP);
//		else if (proper.getFront().compareTo(proper.getSide()) == 0) // igual
//			return new BigDecimal(1);
//		else if (proper.getFront().compareTo(proper.getSide()) == 1){ // mayor que
//			equivalencia = proper.getSide().divide(proper.getFront(), 3, RoundingMode.HALF_UP);
//		}
//		equivalencia = equivalencia.add(new BigDecimal(0.001)); //para comparar solamente los menores y no los iguales
//		if (equivalencia.compareTo(new BigDecimal(0.251)) == 1)
//			return new BigDecimal(1);
//		else if (equivalencia.compareTo(new BigDecimal(0.201)) == 1)
//			return new BigDecimal(0.9925);
//		else if (equivalencia.compareTo(new BigDecimal(0.171)) == 1)
//			return new BigDecimal(0.9850);
//		else if (equivalencia.compareTo(new BigDecimal(0.141)) == 1)
//			return new BigDecimal(0.9775);
//		else if (equivalencia.compareTo(new BigDecimal(0.131)) == 1)
//			return new BigDecimal(0.9700);
//		else if (equivalencia.compareTo(new BigDecimal(0.111)) == 1)
//			return new BigDecimal(0.9625);
//		else if (equivalencia.compareTo(new BigDecimal(0.101)) == 1)
//			return new BigDecimal(0.9550);
//		else if (equivalencia.compareTo(new BigDecimal(0.091)) == 1)
//			return new BigDecimal(0.9475);
//		else return new BigDecimal(0.9400);
//	}
//	
//	public BigDecimal getAppraisalAreaFactor(Property proper){
//		if (proper.getArea().compareTo(new BigDecimal(51)) == -1)
//			return new BigDecimal(1);
//		else if (proper.getArea().compareTo(new BigDecimal(251)) == -1)
//			return new BigDecimal(0.99);
//		else if (proper.getArea().compareTo(new BigDecimal(501)) == -1)
//			return new BigDecimal(0.98);
//		else if (proper.getArea().compareTo(new BigDecimal(1001)) == -1)
//			return new BigDecimal(0.97);
//		else if (proper.getArea().compareTo(new BigDecimal(2501)) == -1)
//			return new BigDecimal(0.96);
//		else if (proper.getArea().compareTo(new BigDecimal(5001)) == -1)
//			return new BigDecimal(0.95);
//		else 
//			return new BigDecimal(0.94);
//	}
//	
//	public void getMaps(){
//		mapTotalStructure.clear();
//		for (AppraisalTotalStructure aTotalStructure : appraisalPeriod.getAppraisalTotalStructure()){
//			mapTotalStructure.put(aTotalStructure.getStructureMaterial().name(), aTotalStructure);
//		}
//		
//		mapTotalWall.clear();
//		for (AppraisalTotalWall aTotalWall : appraisalPeriod.getAppraisalTotalWall()){
//			mapTotalWall.put(aTotalWall.getWallMaterial().name(), aTotalWall);
//		}
//		
//		mapTotalRoof.clear();
//		for (AppraisalTotalRoof aTotalRoof : appraisalPeriod.getAppraisalTotalRoof()){
//			mapTotalRoof.put(aTotalRoof.getRoofMaterial().name(), aTotalRoof);
//		}
//		
//		mapTotalExternal.clear();
//		for (AppraisalTotalExternal aTotalExternal : appraisalPeriod.getAppraisalTotalExternal()){
//			mapTotalExternal.put(aTotalExternal.getExternalFinishing().name(), aTotalExternal);
//		}
//		
//		mapRossHeidecke.clear();
//		for (AppraisalRossHeidecke aRossHeidecke : findAppraisalRossHeideckeYears()){
//			mapRossHeidecke.put((long) aRossHeidecke.getYear(), aRossHeidecke);
//		}
//		
//	}
//	
//	public void calculateUrbanAppraisalOnlyProperty(Property property){
//		System.out.println("==========>>> calculateUrbanAppraisalOnlyProperty CLAVE CATASTRAL: "+property);
//		System.out.println("==========>>> calculateUrbanAppraisalOnlyProperty CLAVE CATASTRAL: "+property.getCadastralCode());
//		if (this.properties != null) 
//			this.properties.clear();
//		else
//			this.properties = new ArrayList<Property>();
//		this.properties.add(property);
//		AppraisalService appraisalService = ServiceLocator.getInstance().findResource(APPRAISAL_SERVICE_NAME);
//		this.properties = appraisalService.calculateUrbanAppraisal(appraisalPeriod, anioAppraisal, this.properties, false);
//	}
//	
	public void calculateUrbanAppraisal(){
		String cadastralCodePartial = populateCadastralCodeSearch();
		properties = findPropertyByCadastralCode(cadastralCodePartial);
		AppraisalService appraisalService = ServiceLocator.getInstance().findResource(APPRAISAL_SERVICE_NAME);
		properties = appraisalService.calculateUrbanAppraisal(appraisalPeriod, anioAppraisal, properties, temporalValues);
	}

	
//	public void calculateUrbanAppraisal(){
//		String cadastralCodePartial = "";
//		if (onlyActualProperty){
//			cadastralCodePartial = propertyForAppraisal.getCadastralCode();
//			System.out.println("==========>>> CLAVE CATASTRAL: "+cadastralCodePartial);
//		}
//		else
//			cadastralCodePartial = populateCadastralCodeSearch();
//		BigDecimal affectationFactorLot = new BigDecimal(0);
//		BigDecimal lotAppraisal = new BigDecimal(0);
//
//		BigDecimal affectationFactorBuilding = new BigDecimal(0);
//		BigDecimal buildingAppraisal = new BigDecimal(0);
//		BigDecimal totalBuildingAppraisal = new BigDecimal(0);
//		BigDecimal cienBigD = new BigDecimal(100);
//		BigDecimal ceroBigD = BigDecimal.ZERO;
//
//		getMaps();
//		if (onlyActualProperty){
//			properties.clear();
//			properties.add(propertyForAppraisal);
//		}
//		else
//			properties = findPropertyByCadastralCode(cadastralCodePartial);
//		for (Property property : properties) {
//			//Inicia Calculo de Avaluo de Terreno
//			System.out.println("======= CadastralCode: "+property.getCadastralCode());
//			lotAppraisal = BigDecimal.ZERO;
//			affectationFactorLot = BigDecimal.ZERO;
//			property.setAppraisalRelationFactor(getAppraisalRelationFactor(property));
////			System.out.println("======= RelationFactor: "+property.getAppraisalRelationFactor());
//
//			property.setAppraisalAreaFactor(getAppraisalAreaFactor(property));
////			System.out.println("======= AreaFactor: "+property.getAppraisalAreaFactor());
//			
//			affectationFactorLot = property.getAppraisalRelationFactor().multiply(property.getAppraisalAreaFactor()); 
////			System.out.println("======= affectationFactorLot: "+affectationFactorLot);
////			System.out.println("======= LotPosition: "+property.getLotPosition().getFactor());
//			affectationFactorLot = affectationFactorLot.multiply(property.getLotPosition().getFactor()); 
////			System.out.println("======= affectationFactorLot: "+affectationFactorLot);
////			System.out.println("======= Lottopography: "+property.getLotTopography().getFactor());
//			affectationFactorLot = affectationFactorLot.multiply(property.getLotTopography().getFactor()); 
////			System.out.println("======= affectationFactorLot: "+affectationFactorLot);
////			System.out.println("======= StreetMaterial: "+property.getStreetMaterial().getFactor());
//			affectationFactorLot = affectationFactorLot.multiply(property.getStreetMaterial().getFactor()); 
////			System.out.println("======= affectationFactorLot: "+affectationFactorLot);
//
//			//INFRAESTRUCTURA
//			if (property.getHasWaterService()) 
//				affectationFactorLot = affectationFactorLot.multiply(appraisalPeriod.getFactorHasWater());
//			else 
//				affectationFactorLot = affectationFactorLot.multiply(appraisalPeriod.getFactorHasWater());
//			
//			if (property.getSewerage().compareTo(Sewerage.NOT_AVAILABLE) == 0) 
//				affectationFactorLot = affectationFactorLot.multiply(appraisalPeriod.getFactorHasntSewerage());
//			else 
//				affectationFactorLot = affectationFactorLot.multiply(appraisalPeriod.getFactorHasSewerage());
//			
//			if (property.getHasElectricity()) 
//				affectationFactorLot = affectationFactorLot.multiply(appraisalPeriod.getFactorHasEnergy());
//			else 
//				affectationFactorLot = affectationFactorLot.multiply(appraisalPeriod.getFactorHasntEnergy());
//			
//			
////			System.out.println("======= affectationFactorLot: "+affectationFactorLot);
//			affectationFactorLot = affectationFactorLot.round(new MathContext(4));
//			property.setAffectationFactorLot(affectationFactorLot);
//			
//			if (!isTemporalValues()){
//				lotAppraisal = property.getCurrentDomain().getValueBySquareMeter().multiply(property.getAffectationFactorLot());
//				lotAppraisal = lotAppraisal.multiply(property.getArea());
//				lotAppraisal = lotAppraisal.setScale(2, RoundingMode.HALF_UP);
//			} else {
//				lotAppraisal = property.getCurrentDomain().getValueBySquareMeterTmp().multiply(property.getAffectationFactorLot());
//				lotAppraisal = lotAppraisal.multiply(property.getArea());
//				lotAppraisal = lotAppraisal.setScale(2, RoundingMode.HALF_UP);				
//			}
//
//			System.out.println("======= totalLotAppraisal: "+lotAppraisal);
//			if ((property.getLotAliquot().floatValue() > 0) && (property.getLotAliquot().floatValue() < 100)){
//				lotAppraisal = lotAppraisal.multiply(property.getLotAliquot()).divide(cienBigD);
//				lotAppraisal = lotAppraisal.setScale(2, RoundingMode.HALF_UP);				
//				System.out.println("======= Lot Aliquot: "+property.getLotAliquot());
//				System.out.println("======= totalLotAppraisal Aliquot: "+lotAppraisal);
//			}
//
//			if (isTemporalValues())
//				property.getCurrentDomain().setLotAppraisalTmp(lotAppraisal);
//			else
//				property.getCurrentDomain().setLotAppraisal(lotAppraisal);
//			
//			System.out.println("======= lotAppraisal : "+lotAppraisal);
//
//			//Inicia Calculo de Avaluo de Construccion
//			totalBuildingAppraisal = BigDecimal.ZERO;
//			for (Building building : property.getBuildings()) {
////				System.out.println("======= Construction: "+building.getId());
//				buildingAppraisal = BigDecimal.ZERO;
//				affectationFactorBuilding = BigDecimal.ZERO;
//				affectationFactorBuilding = affectationFactorBuilding.add(mapTotalStructure.get(building.getStructureMaterial().name()).getTotal());
//				affectationFactorBuilding = affectationFactorBuilding.add(mapTotalWall.get(building.getWallMaterial().name()).getTotal());
//				affectationFactorBuilding = affectationFactorBuilding.add(mapTotalRoof.get(building.getRoofMaterial().name()).getTotal());
//				affectationFactorBuilding = affectationFactorBuilding.add(mapTotalExternal.get(building.getExternalFinishing().name()).getTotal());
////				System.out.println("======= Structure: "+mapTotalStructure.get(building.getStructureMaterial().name()));
////				System.out.println("======= Structure: "+mapTotalStructure.get(building.getStructureMaterial().name()).getTotal());
////				System.out.println("======= Wall: "+mapTotalWall.get(building.getWallMaterial().name()));
////				System.out.println("======= Wall: "+mapTotalWall.get(building.getWallMaterial().name()).getTotal());
////				System.out.println("======= Roof: "+mapTotalRoof.get(building.getRoofMaterial().name()));
////				System.out.println("======= Roof: "+mapTotalRoof.get(building.getRoofMaterial().name()).getTotal());
////				System.out.println("======= External: "+mapTotalExternal.get(building.getExternalFinishing().name()));
////				System.out.println("======= External: "+mapTotalExternal.get(building.getExternalFinishing().name()).getTotal());
////
////				System.out.println("======= affectationFactorBuilding: "+affectationFactorBuilding);
//
//				//Equipment
//				if (building.getHasEquipment()) {
//					affectationFactorBuilding = affectationFactorBuilding.multiply(appraisalPeriod.getFactorHasEquipment());
////					System.out.println("======= appraisalPeriod.getFactorHasEquipment(): "+appraisalPeriod.getFactorHasEquipment());
//				}
//				else {
//					affectationFactorBuilding = affectationFactorBuilding.multiply(appraisalPeriod.getFactorHasntEquipment());
////					System.out.println("======= appraisalPeriod.getFactorHasntEquipment(): "+appraisalPeriod.getFactorHasntEquipment());
//				}
//					
////				System.out.println("======= affectationFactorBuilding: "+affectationFactorBuilding);
//
//				//preservationState
//				Long anioConst = anioAppraisal - building.getBuildingYear().longValue();// building.getAnioConst().longValue();
//				if (anioConst > 99) anioConst = Long.valueOf(99);
//				BigDecimal factorRoss = new BigDecimal(1);
////				System.out.println("======= Años: "+anioConst);
////				System.out.println("======= PreservationState: "+building.getPreservationState().name());
//				if (building.getPreservationState().compareTo(PreservationState.GOOD) == 0) {
//					factorRoss = mapRossHeidecke.get(anioConst).getGoodState();
//				}
//				else if (building.getPreservationState().compareTo(PreservationState.BAD) == 0) {
//					factorRoss = mapRossHeidecke.get(anioConst).getBadState();
//				}
//				else if (building.getPreservationState().compareTo(PreservationState.REGULAR) == 0) {
//					factorRoss = mapRossHeidecke.get(anioConst).getRegularState();
//				}
//
////				System.out.println("======= factorRoss : "+factorRoss );
//
//				factorRoss = factorRoss.divide(new BigDecimal(100));
//
////				System.out.println("======= factorRoss : "+factorRoss );
//
//				BigDecimal substract = new BigDecimal(0);
//				//equivale al 80% --> (VA-Vr) --> Vr = 20% de VA 
//				substract = affectationFactorBuilding.multiply(new BigDecimal(0.8));
//				substract = substract.multiply(factorRoss);
//
////				System.out.println("======= substract: "+substract);
//
//				//valor por m2 de edificacion para esa porcion de la construccion
//				affectationFactorBuilding = affectationFactorBuilding.subtract(substract); 
//				
//				buildingAppraisal = building.getArea().multiply(affectationFactorBuilding);
//				buildingAppraisal = buildingAppraisal.multiply(BigDecimal.valueOf(building.getFloorsNumber()));
//				
////				System.out.println("======= Area: "+building.getArea());
////				System.out.println("======= Pisos: "+building.getFloorsNumber());
////				System.out.println("======= affectationFactorBuilding: "+affectationFactorBuilding);
//
//				totalBuildingAppraisal = totalBuildingAppraisal.add(buildingAppraisal);
//				System.out.println("======= totalBuildingAppraisal : "+totalBuildingAppraisal );
//			}
//			
//			if ((property.getBuildingAliquot().floatValue() > 0) && (property.getBuildingAliquot().floatValue() < 100)){
//				totalBuildingAppraisal = totalBuildingAppraisal.multiply(property.getBuildingAliquot()).divide(cienBigD);
//				System.out.println("======= Building Aliquot: "+property.getBuildingAliquot());
//				System.out.println("======= totalBuildingAppraisal Aliquot: "+totalBuildingAppraisal );
//			}
//
//			totalBuildingAppraisal = totalBuildingAppraisal.setScale(2, RoundingMode.HALF_UP);
//
//			if (isTemporalValues()){
//				property.getCurrentDomain().setBuildingAppraisalTmp(totalBuildingAppraisal);
//				property.getCurrentDomain().setCommercialAppraisalTmp(totalBuildingAppraisal.add(lotAppraisal));
//			}
//			else{
//				property.getCurrentDomain().setBuildingAppraisal(totalBuildingAppraisal);
//				property.getCurrentDomain().setCommercialAppraisal(totalBuildingAppraisal.add(lotAppraisal));
//			}
//		}
//		
//	}
//
	public void reviewAppraisalValues(){
		String cadastralCodePartial = populateCadastralCodeSearch();
		properties = findPropertyByCadastralCode(cadastralCodePartial);
	}
		
	@Override
	public String persist() {
		System.out.println("PERSIST INICIO");
		AppraisalService appraisalService = ServiceLocator.getInstance().findResource(APPRAISAL_SERVICE_NAME);
		try{
			if (lotValues)
				appraisalService.saveValueBySquareMeter(properties, this.temporalValues);
			else
				appraisalService.saveAppraisals(properties, this.temporalValues);
			System.out.println("PERSISTED");
			return "persisted";
		} catch(InvoiceNumberOutOfRangeException e){
			addFacesMessageFromResourceBundle(e.getClass().getSimpleName(), e.getInvoiceNumber());
			return null;
		} catch(Exception e){
			addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
			e.printStackTrace();
			return null;
		} 
	}
	
	public void updateChanges() {
		persist();
//		for (Property pro : properties) {
//			 propertyHome.setInstance(pro);
//			 propertyHome.update();
//		}
		resetAll();
	}

	public void resetProperties() {
		setProperties(null);
	}

//	public Map<String, AppraisalTotalStructure> getMapTotalStructure() {
//		return mapTotalStructure;
//	}
//
//	public void setMapTotalStructure(
//			Map<String, AppraisalTotalStructure> mapTotalStructure) {
//		this.mapTotalStructure = mapTotalStructure;
//	}
//
//	public Map<String, AppraisalTotalWall> getMapTotalWall() {
//		return mapTotalWall;
//	}
//
//	public void setMapTotalWall(Map<String, AppraisalTotalWall> mapTotalWall) {
//		this.mapTotalWall = mapTotalWall;
//	}
//
//	public Map<String, AppraisalTotalRoof> getMapTotalRoof() {
//		return mapTotalRoof;
//	}
//
//	public void setMapTotalRoof(Map<String, AppraisalTotalRoof> mapTotalRoof) {
//		this.mapTotalRoof = mapTotalRoof;
//	}
//
//	public Map<String, AppraisalTotalExternal> getMapTotalExternal() {
//		return mapTotalExternal;
//	}
//
//	public void setMapTotalExternal(
//			Map<String, AppraisalTotalExternal> mapTotalExternal) {
//		this.mapTotalExternal = mapTotalExternal;
//	}
//
//	public Map<Long, AppraisalRossHeidecke> getMapRossHeidecke() {
//		return mapRossHeidecke;
//	}
//
//	public void setMapRossHeidecke(Map<Long, AppraisalRossHeidecke> mapRossHeidecke) {
//		this.mapRossHeidecke = mapRossHeidecke;
//	}
//
	@SuppressWarnings("unchecked")
	@Factory(value = "rossHeideckeYears")
	public List<AppraisalRossHeidecke> findAppraisalRossHeideckeYears() {
		Query query = getEntityManager().createNamedQuery(
				"AppraisalRossHeidecke.findAll");
		List<AppraisalRossHeidecke> appraisalRossHeidecke = query.getResultList();
		return appraisalRossHeidecke;
	}

	public int getAnioAppraisal() {
		return anioAppraisal;
	}

	public void setAnioAppraisal(int anioAppraisal) {
		this.anioAppraisal = anioAppraisal;
	}

	public boolean isTemporalValues() {
		return temporalValues;
	}

	public void setTemporalValues(boolean temporalValues) {
		this.temporalValues = temporalValues;
	}

	public boolean isReviewValues() {
		return reviewValues;
	}

	public void setReviewValues(boolean reviewValues) {
		this.reviewValues = reviewValues;
	}
	
	/*Manuel Uchuari
	 * Sincronizacion Sinat to Gim*/
	
	private String cadastralKey;

	public String getCadastralKey() {
		return cadastralKey;
	}

	public void setCadastralKey(String cadastralKey) {
		this.cadastralKey = cadastralKey;
	}
	
	public void SearchcadastralKey(){
		
		int numberResults=0;
		String resultSp=null; 
		
		String sql ="select * from view_predios_sinat vista " +				
				"where vista.codigo_catastral = :paramCadastralKey";		
		Query query = getEntityManager().createNativeQuery(sql);	
		query.setParameter("paramCadastralKey", cadastralKey);	
		numberResults=query.getResultList().size();
		if(numberResults != 0){
			System.out.println("data Sinat ok");			
			Query querySp = getEntityManager().createNativeQuery("SELECT * from sp_sinat_synchronization_gim(?)")
			//Query querySp = getEntityManager().createNativeQuery("SELECT * from sp_sinatsynchronizationgimtestlocal(?)")
			.setParameter(1, cadastralKey);	
			resultSp = (String) querySp.getSingleResult();
			System.out.println("numberSpResultValor: "+resultSp);
			
			if(resultSp.equals("INSERT")){
				facesMessages.addFromResourceBundle("appraisal.cadasterRegisteredToGim", cadastralKey);
			}
			else if(resultSp.equals("UPDATE")){
				facesMessages.addFromResourceBundle("appraisal.cadasterUpdatedToGim", cadastralKey);
			}
			else if(resultSp.equals("TRANSFER")){
				facesMessages.addFromResourceBundle("appraisal.cadasterTransferedToGim", cadastralKey);
			}
			else if(resultSp.equals("NOTRANSACTION")){
				facesMessages.addFromResourceBundle("appraisal.cadasterNoTransactionToGim", cadastralKey);
			}
		}
		else{
			System.out.println("no data en Sinat");
			facesMessages.addFromResourceBundle("appraisal.noFoundCadasterCode", cadastralKey);
			//addFacesMessageFromResourceBundle("appraisal.noFoundCadasterCode", cadastralKey);
		}		
	}
	

}
