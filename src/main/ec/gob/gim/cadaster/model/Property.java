package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Attachment;
import ec.gob.gim.common.model.CheckingRecord;
import ec.gob.gim.waterservice.model.WaterSupply;

/**
 * Predio
 * 
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:41
 */
@Audited
@Entity
@TableGenerator(name = "PropertyGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Property", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "Property.findByCadastralCode", query = "select property from Property property "
		+ "left join fetch property.currentDomain currentDomain "				
		+ "left join fetch currentDomain.notarysProvince "		
		+ "left join fetch currentDomain.notarysCity "
		+ "left join fetch currentDomain.purchaseType purchaseType "
		+ "left join fetch property.streetMaterial sm "		
		+ "left join fetch property.location l "
		+ "left join fetch l.neighborhood nb "
		+ "left join fetch nb.place place "
		+ "left JOIN fetch l.mainBlockLimit bl " 
		+ "left join fetch bl.sidewalkMaterial swm "
		+ "left join fetch bl.street street "
		+ "left join fetch bl.streetMaterial streetMaterial "
		+ "left join fetch bl.streetType streetType "		
		+ "left join fetch property.lotPosition lp "
		+ "left join fetch property.fenceMaterial fm "
		+ "left join fetch property.block block "
		+ "left join fetch block.sector sector "
		+ "left join fetch sector.territorialDivisionType "
		+ "left join fetch property.propertyType pt "
		+ "left join fetch pt.entry entry "
		+ "left join fetch currentDomain.resident resident "		
		+ "left join fetch resident.currentAddress address "		
		+ "where property.cadastralCode like concat(:criteria,'%') or property.previousCadastralCode like concat(:criteria,'%') "
		+ "or lower(resident.name) like lower(concat(:criteria,'%')) "
		+ "or lower(resident.identificationNumber) like lower(concat(:criteria,'%')) "
		+ "and property.deleted = false "
		+ "order by property.cadastralCode"),
		
		@NamedQuery(name = "Property.findResidentByProperty", query = "select property from Property property "
				+ "left join fetch property.currentDomain currentDomain "				
				+ "left join fetch currentDomain.resident resident "				
				+ "where  property.cadastralCode = :code"),
				
	@NamedQuery(name = "Property.findUrbanByCadastralCode", query = "select property from Property property "
			+ "left join fetch property.currentDomain currentDomain "				
			+ "left join fetch currentDomain.notarysProvince "		
			+ "left join fetch currentDomain.notarysCity "
			+ "left join fetch currentDomain.purchaseType purchaseType "
			+ "left join fetch property.streetMaterial sm "		
			+ "left join fetch property.location l "
			+ "left join fetch l.neighborhood nb "
			+ "left join fetch nb.place place "
			+ "left JOIN fetch l.mainBlockLimit bl " 
			+ "left join fetch bl.sidewalkMaterial swm "
			+ "left join fetch bl.street street "
			+ "left join fetch bl.streetMaterial streetMaterial "
			+ "left join fetch bl.streetType streetType "		
			+ "left join fetch property.lotPosition lp "
			+ "left join fetch property.fenceMaterial fm "
			+ "left join fetch property.block block "
			+ "left join fetch block.sector sector "
			+ "left join fetch sector.territorialDivisionType "
			+ "left join fetch property.propertyType pt "
			+ "left join fetch pt.entry entry "
			+ "left join fetch currentDomain.resident resident "		
			+ "left join fetch resident.currentAddress address "		
			+ "where (property.cadastralCode like concat(:criteria,'%') or property.previousCadastralCode like concat(:criteria,'%') "
			+ "or lower(resident.name) like lower(concat(:criteria,'%')) "
			+ "or lower(resident.identificationNumber) like lower(concat(:criteria,'%'))) and pt.id = 1 order by property.cadastralCode"),

	@NamedQuery(name = "Property.findUrbanByCadastralCodeAndNotDeleted", query = "select property from Property property "
			+ "left join fetch property.currentDomain currentDomain "
			+ "left join fetch currentDomain.notarysProvince "
			+ "left join fetch currentDomain.notarysCity "
			+ "left join fetch currentDomain.purchaseType purchaseType "
			+ "left join fetch property.streetMaterial sm "
			+ "left join fetch property.location l "
			+ "left join fetch l.neighborhood nb "
			+ "left join fetch nb.place place "
			+ "left JOIN fetch l.mainBlockLimit bl " 
			+ "left join fetch bl.sidewalkMaterial swm "
			+ "left join fetch bl.street street "
			+ "left join fetch bl.streetMaterial streetMaterial "
			+ "left join fetch bl.streetType streetType "
			+ "left join fetch property.lotPosition lp "
			+ "left join fetch property.fenceMaterial fm "
			+ "left join fetch property.block block "
			+ "left join fetch block.sector sector "
			+ "left join fetch sector.territorialDivisionType "
			+ "left join fetch property.propertyType pt "
			+ "left join fetch pt.entry entry "
			+ "left join fetch currentDomain.resident resident "
			+ "left join fetch resident.currentAddress address "
			+ "where property.cadastralCode like concat(:criteria,'%') "
			+ "and pt.id = 1 and property.deleted=false order by property.cadastralCode"),

	@NamedQuery(name = "Property.findByResidentAndCadastralCode", query = "select property from Property property "
			+ "left join fetch property.currentDomain currentDomain "				
			+ "left join fetch currentDomain.notarysProvince "		
			+ "left join fetch currentDomain.notarysCity "
			+ "left join fetch currentDomain.purchaseType purchaseType "
			+ "left join fetch property.streetMaterial sm "		
			+ "left join fetch property.location l "
			+ "left join fetch l.neighborhood nb "
			+ "left join fetch nb.place place "
			+ "left JOIN fetch l.mainBlockLimit bl " 
			+ "left join fetch bl.sidewalkMaterial swm "
			+ "left join fetch bl.street street "
			+ "left join fetch bl.streetMaterial streetMaterial "
			+ "left join fetch bl.streetType streetType "		
			+ "left join fetch property.lotPosition lp "
			+ "left join fetch property.fenceMaterial fm "
			+ "left join fetch property.block block "
			+ "left join fetch block.sector sector "
			+ "left join fetch sector.territorialDivisionType "
			+ "left join fetch property.propertyType pt "
			+ "left join fetch pt.entry entry "
			+ "left join fetch currentDomain.resident resident "		
			+ "left join fetch resident.currentAddress address "		
			+ "where (property.cadastralCode like concat(:cadastralCode,'%') or property.previousCadastralCode like concat(:cadastralCode,'%')) "
			+ "and resident.id = :residentId"),
	@NamedQuery(name = "Property.findByCadastralCodeAndType", query = "select property from Property property "
		+ "left join fetch property.currentDomain currentDomain "				
		+ "left join fetch currentDomain.notarysProvince "		
		+ "left join fetch currentDomain.notarysCity "
		+ "left join fetch currentDomain.purchaseType purchaseType "
		+ "left join fetch property.streetMaterial sm "		
		+ "left join fetch property.location l "
		+ "left join fetch l.neighborhood nb "
		+ "left join fetch nb.place place "
		+ "left JOIN fetch l.mainBlockLimit bl " 
		+ "left join fetch bl.sidewalkMaterial swm "
		+ "left join fetch bl.street street "
		+ "left join fetch bl.streetMaterial streetMaterial "
		+ "left join fetch bl.streetType streetType "		
		+ "left join fetch property.lotPosition lp "
		+ "left join fetch property.fenceMaterial fm "
		+ "left join fetch property.block block "
		+ "left join fetch block.sector sector "
		+ "left join fetch sector.territorialDivisionType "
		+ "left join fetch property.propertyType pt "
		+ "left join fetch pt.entry entry "
		+ "left join fetch currentDomain.resident resident "		
		+ "left join fetch resident.currentAddress address "
		+ "where property.cadastralCode like concat(:cadastralCode,'%') "
		+ "and property.deleted = false "
		+ "and pt.id = :idType ORDER BY property.cadastralCode"),
		
		
	@NamedQuery(name = "Property.findByCadastralCodeAndTypeExceptFromSomeOwnerIds", query = "select property from Property property "
			+ "left join fetch property.currentDomain currentDomain "				
//			+ "left join fetch currentDomain.notarysProvince "		
//			+ "left join fetch currentDomain.notarysCity "
//			+ "left join fetch currentDomain.purchaseType purchaseType "
//			+ "left join fetch property.streetMaterial sm "		
//			+ "left join fetch property.location l "
//			+ "left join fetch l.neighborhood nb "
//			+ "left join fetch nb.place place "
//			+ "left JOIN fetch l.mainBlockLimit bl " 
//			+ "left join fetch bl.sidewalkMaterial swm "
//			+ "left join fetch bl.street street "
//			+ "left join fetch bl.streetMaterial streetMaterial "
//			+ "left join fetch bl.streetType streetType "		
//			+ "left join fetch property.lotPosition lp "
//			+ "left join fetch property.fenceMaterial fm "
//			+ "left join fetch property.block block "
//			+ "left join fetch block.sector sector "
//			+ "left join fetch sector.territorialDivisionType "
			+ "left join fetch property.propertyType pt "
			+ "left join fetch pt.entry entry "
			+ "left join fetch currentDomain.resident resident "		
//			+ "left join fetch resident.currentAddress address "
			+ "where property.cadastralCode like concat(:cadastralCode,'%') "
			+ "and resident.id not in (:ownersIds) "
			+ "and property.deleted = false "
			+ "and pt.id = :idType ORDER BY property.cadastralCode"),		
	
		
	@NamedQuery(name = "Property.findCadastralCodeByType", query = "select property.cadastralCode from Property property "					
			+ "where property.cadastralCode like concat(:cadastralCode,'%') and property.propertyType.id = :idType ORDER BY property.cadastralCode"),
			
	@NamedQuery(name = "Property.findCadastralCodeByPartCadastralCode", query = "select property.cadastralCode from Property property "
			+ "where property.cadastralCode like concat(:criteria,'%') order by property.cadastralCode"),
			
	@NamedQuery(name = "Property.findUrbanCadastralCodeByPartCadastralCode", query = "select property.cadastralCode from Property property "
			+ "where property.cadastralCode like concat(:criteria,'%') and property.propertyType.id = 1 order by property.cadastralCode"),
			
	@NamedQuery(name = "Property.findMaxCadastralCodeByParish", query = "select max(property.cadastralCode) from Property property "
			+ "where property.propertyType.id = :propertyTypeId and property.cadastralCode like concat(:cadastralCode,'%')"),
			
	@NamedQuery(name = "Property.findCheckingRecordsByChekingRecordTypeAndDates", query = "select c from Property p join p.checkingRecords c "
			+ "where c.checkingRecordType = :checkingRecordType AND c.date BETWEEN :startDate AND :endDate"),
			
	@NamedQuery(name = "Property.findByResidentId", 
			    query = "SELECT p FROM Property p " +
			    		"LEFT JOIN p.location l " +
			    		"LEFT JOIN l.mainBlockLimit bl " +
			    		"LEFT JOIN bl.street " +
			    		"WHERE p.currentDomain.resident.id = :residentId"),
	@NamedQuery(name = "Property.findByResidentIdForEmission", 
			    query = "SELECT property FROM Property property " +
	
						 "left join fetch property.currentDomain currentDomain "
						//+ "left join fetch currentDomain.notarysProvince "		
						//+ "left join fetch currentDomain.notarysCity "
						//+ "left join fetch currentDomain.purchaseType purchaseType "
						+ "left join fetch property.streetMaterial sm "		
						+ "left join fetch property.location l "
						+ "left join fetch l.neighborhood nb "
						+ "left join fetch nb.place place "
						+ "left JOIN fetch l.mainBlockLimit bl " 
						+ "left join fetch bl.sidewalkMaterial swm "
						+ "left join fetch bl.street street "
						+ "left join fetch bl.streetMaterial streetMaterial "
						+ "left join fetch bl.streetType streetType "		
						+ "left join fetch property.lotPosition lp "
						+ "left join fetch property.fenceMaterial fm "
						+ "left join fetch property.block block "
						+ "left join fetch block.sector sector "
						+ "left join fetch sector.territorialDivisionType "
						+ "left join fetch property.propertyType pt "
						+ "left join fetch pt.entry entry "
						+ "left join fetch currentDomain.resident resident "		
						//+ "left join fetch resident.currentAddress address "
	
			    		//"LEFT JOIN p.location l " +
			    		//"LEFT JOIN l.mainBlockLimit bl " +
			    		//"LEFT JOIN bl.street " +
			    		+"WHERE resident.id = :residentId and property.deleted=false"),
	@NamedQuery(name = "Property.findByResidentIdNotDeleted", 
			    query = "SELECT p FROM Property p " +
			    		"LEFT JOIN p.location l " +
			    		"LEFT JOIN l.mainBlockLimit bl " +
			    		"LEFT JOIN bl.street " +
			    		"WHERE p.currentDomain.resident.id = :residentId and p.deleted=false"),
	@NamedQuery(name = "Property.countPreviousCadastralCode", 
			    query = "SELECT count(p.id) FROM Property p " +
			    		"WHERE p.previousCadastralCode = :previousCadastralCode and p.deleted = false"),
	@NamedQuery(name = "Property.countPreviousCadastralCodeAndId", 
			    query = "SELECT count(p.id) FROM Property p " +
			    		"WHERE p.previousCadastralCode = :previousCadastralCode and p.id <> :propertyId and p.deleted = false"),
	@NamedQuery(name = "RealEstate.findByCadastralCode", 
		    query = "SELECT NEW org.gob.loja.gim.ws.dto.RealEstate(" +
		    		"p.id, p.cadastralCode, p.currentDomain.resident.identificationNumber, " +
		    		"location.houseNumber, mainStreet.name, nb.name, block.id, block.code, block.cadastralCode) " +
		    		"FROM Property p " +
		    		"LEFT JOIN p.block block " +
		    		"LEFT JOIN p.location location " +
		    		"LEFT JOIN location.mainBlockLimit mainBlockLimit " +
		    		"LEFT JOIN mainBlockLimit.street mainStreet " +
		    		"LEFT JOIN location.neighborhood nb " +
		    		"WHERE p.cadastralCode like :cadastralCode or p.previousCadastralCode like :cadastralCode"),
    @NamedQuery(name = "RealEstate.findByIdentificationNumber", query = "SELECT NEW org.gob.loja.gim.ws.dto.RealEstate("
				+ "p.id, p.cadastralCode, p.currentDomain.resident.identificationNumber, "
				+ "location.houseNumber, mainStreet.name, nb.name, block.id, block.code, block.cadastralCode) "
				+ "FROM Property p "
				+ "LEFT JOIN p.block block "
				+ "LEFT JOIN p.location location "
				+ "LEFT JOIN location.mainBlockLimit mainBlockLimit "
				+ "LEFT JOIN mainBlockLimit.street mainStreet "
				+ "LEFT JOIN location.neighborhood nb "
				+ "WHERE p.currentDomain.resident.identificationNumber = :identificationNumber order by p.cadastralCode"),
	@NamedQuery(name = "Property.findByResidentsIds", 
			    query = "SELECT p FROM Property p " +
			    		"WHERE p.currentDomain.resident.id in (:ids) "+
			    		"AND p.deleted = FALSE ")
})
public class Property {

	@Id
	@GeneratedValue(generator = "PropertyGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private Integer number;
	private BigDecimal area;
	private BigDecimal buildingAliquot;
	private Integer electricMetersNumber;
	private BigDecimal front;
	private BigDecimal frontsLength;
	private Integer frontsNumber;
	private Boolean hasElectricity;
	private Boolean hasFrontFence;
	private Boolean hasPerimeterFence;
	private Boolean hasTelephoneNetwork;
	private Boolean hasWaterService;
	private Boolean deleted;
	
	private BigDecimal lotAliquot;
	private String observations;
	
	@Transient
	private int totalYearsFromLastChangeOwner; 
	
	@Transient
	private boolean isUrban;
	

	private Boolean needConfirmChangeOwner;	

	@Transient
	private BigDecimal appraisalRelationFactor;
	
	@Transient
	private BigDecimal appraisalAreaFactor;
	
	@Transient
	private BigDecimal affectationFactorLot;
	
	@Transient
	private BigDecimal affectationFactorBuilding;

	@Transient
	private BigDecimal valueBySquareMeterForDomain;

	@Transient
	private Appraisal lastAppraisal;

	@Transient
	private boolean duplicate;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Attachment photo;

	@Column(length = 30)
	private String previousCadastralCode;

	@Column(length = 30, unique=true)
	private String cadastralCode;

	private BigDecimal side;
	private BigDecimal sidewalkWidth;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Attachment sketch;

	private BigDecimal streetWidth;
	private Integer telephoneLinesNumber;
	@Deprecated
	private BigDecimal valueByAreaUnit;

	private Integer waterMetersNumber;
	@Column(length = 15)
	private String phoneNumber;
	@Column(length = 80)
	private String addressReference;

	private Integer buildingNumber;
	private Integer floorNumber;
	private Integer housingUnitNumber;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private GarbageCollection garbageCollection;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private Sewerage sewerage;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private Sidewalk sidewalk;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private LotTopography lotTopography;
	
	
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private PropertyLocationType propertyLocationType;

	/**
	 * Relationships
	 */
	@ManyToOne
	@JoinColumn(name = "propertyType_id")
	private PropertyType propertyType;

	@ManyToOne
	@JoinColumn(name = "lotPosition_id")
	private LotPosition lotPosition;

	@ManyToOne
	@JoinColumn(name = "fenceMaterial_id")
	private FenceMaterial fenceMaterial;

	@ManyToOne
	@JoinColumn(name = "streetMaterial_id")
	private StreetMaterial streetMaterial;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "location_id")
	private Location location;

	@ManyToOne
	private Block block;

	@OneToOne(mappedBy = "currentProperty")
	private Domain currentDomain;
	
	

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<PropertyLandUse> propertyLandUses;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "property")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)	
	private List<CheckingRecord> checkingRecords;

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<WaterSupply> waterSupplies;

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Domain> domains;

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Building> buildings;

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@OrderBy("date desc")
	private List<Appraisal> appraisals;
	
	

	public Property() {
		this.propertyLandUses = new ArrayList<PropertyLandUse>();
		this.checkingRecords = new ArrayList<CheckingRecord>();		
		this.waterSupplies = new ArrayList<WaterSupply>();
		this.domains = new ArrayList<Domain>();
		this.buildings = new ArrayList<Building>();
		this.appraisals = new ArrayList<Appraisal>();
		this.lastAppraisal = new Appraisal();
		this.duplicate = false;

		this.number = 0;
		this.buildingNumber = 0;
		this.floorNumber = 0;
		this.housingUnitNumber = 0;
		
		this.deleted = Boolean.FALSE;

		setBlock(new Block());
		//
		Domain currentDomain = new Domain();
		add(currentDomain);
		setCurrentDomain(currentDomain);
		updateValueBySquareMeterForDomain();
		//
		needConfirmChangeOwner=Boolean.FALSE;
		
	}

	public BigDecimal getArea() {
		return area;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public BigDecimal getBuildingAliquot() {
		return buildingAliquot;
	}

	public void setBuildingAliquot(BigDecimal buildingAliquot) {
		this.buildingAliquot = buildingAliquot;
	}

	public Integer getElectricMetersNumber() {
		return electricMetersNumber;
	}

	public void setElectricMetersNumber(Integer electricMetersNumber) {
		this.electricMetersNumber = electricMetersNumber;
	}

	public BigDecimal getFront() {
		return front;
	}

	public void setFront(BigDecimal front) {
		this.front = front;
	}

	public BigDecimal getFrontsLength() {
		return frontsLength;
	}

	public void setFrontsLength(BigDecimal frontsLength) {
		this.frontsLength = frontsLength;
	}

	public Integer getFrontsNumber() {
		return frontsNumber;
	}

	public void setFrontsNumber(Integer frontsNumber) {
		this.frontsNumber = frontsNumber;
	}

	public Boolean getHasElectricity() {
		return hasElectricity;
	}

	public void setHasElectricity(Boolean hasElectricity) {
		this.hasElectricity = hasElectricity;
	}

	public Boolean getHasFrontFence() {
		return hasFrontFence;
	}

	public void setHasFrontFence(Boolean hasFrontFence) {
		this.hasFrontFence = hasFrontFence;
	}

	public Boolean getHasPerimeterFence() {
		return hasPerimeterFence;
	}

	public void setHasPerimeterFence(Boolean hasPerimeterFence) {
		this.hasPerimeterFence = hasPerimeterFence;
	}

	public Boolean getHasTelephoneNetwork() {
		return hasTelephoneNetwork;
	}

	public void setHasTelephoneNetwork(Boolean hasTelephoneNetwork) {
		this.hasTelephoneNetwork = hasTelephoneNetwork;
	}

	public Boolean getHasWaterService() {
		return hasWaterService;
	}

	public void setHasWaterService(Boolean hasWaterService) {
		this.hasWaterService = hasWaterService;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLotAliquot() {
		return lotAliquot;
	}

	public void setLotAliquot(BigDecimal lotAliquot) {
		this.lotAliquot = lotAliquot;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	private java.text.NumberFormat getNumberFormat() {
		java.text.NumberFormat numberFormat = new java.text.DecimalFormat("00");
		numberFormat.setMaximumIntegerDigits(2);
		return numberFormat;
	}

	//macartuche
	//cambio clave catastral
	public String getFormattedNumber() {
		return getNumberFormat2().format(number);
	}

	/**
	 * @macartuche
	 * Predio-Lote 3 digitos
	 * @return
	 */
	private java.text.NumberFormat getNumberFormat2() {
		java.text.NumberFormat numberFormat = new java.text.DecimalFormat("000");
		numberFormat.setMaximumIntegerDigits(3);
		return numberFormat;
	}
	
	
	public String getFormattedBuildingNumber() {
		return getNumberFormat2().format(buildingNumber);
	}

	public String getFormattedHousingUnitNumber() {
		return getNumberFormat2().format(housingUnitNumber);
	}

	public String getFormattedFloorNumber() {
		return getNumberFormat().format(floorNumber);
	}

	/**
	 * @return the photo
	 */
	public Attachment getPhoto() {
		return photo;
	}

	/**
	 * @param photo
	 *            the photo to set
	 */
	public void setPhoto(Attachment photo) {
		this.photo = photo;
	}

	/**
	 * @return the sketch
	 */
	public Attachment getSketch() {
		return sketch;
	}

	/**
	 * @param sketch
	 *            the sketch to set
	 */
	public void setSketch(Attachment sketch) {
		this.sketch = sketch;
	}

	public String getPreviousCadastralCode() {
		return previousCadastralCode;
	}

	public void setPreviousCadastralCode(String previousCadastralCode) {
		this.previousCadastralCode = previousCadastralCode;
	}

	/**
	 * @return the cadastralCode
	 */
	public String getCadastralCode() {
		return cadastralCode;
	}

	/**
	 * @param cadastralCode
	 *            the cadastralCode to set
	 */
	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public BigDecimal getSide() {
		return side;
	}

	public void setSide(BigDecimal side) {
		this.side = side;
	}

	public BigDecimal getSidewalkWidth() {
		return sidewalkWidth;
	}

	public void setSidewalkWidth(BigDecimal sidewalkWidth) {
		this.sidewalkWidth = sidewalkWidth;
	}

	public BigDecimal getStreetWidth() {
		return streetWidth;
	}

	public void setStreetWidth(BigDecimal streetWidth) {
		this.streetWidth = streetWidth;
	}

	public Integer getTelephoneLinesNumber() {
		return telephoneLinesNumber;
	}

	public void setTelephoneLinesNumber(Integer telephoneLinesNumber) {
		this.telephoneLinesNumber = telephoneLinesNumber;
	}

	@Deprecated
	public BigDecimal getValueByAreaUnit() {
		return valueByAreaUnit;
	}

	@Deprecated
	public void setValueByAreaUnit(BigDecimal valueByAreaUnit) {
		this.valueByAreaUnit = valueByAreaUnit;
	}

	public Integer getWaterMetersNumber() {
		return waterMetersNumber;
	}

	public void setWaterMetersNumber(Integer waterMetersNumber) {
		this.waterMetersNumber = waterMetersNumber;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public Sewerage getSewerage() {
		return sewerage;
	}

	public void setSewerage(Sewerage sewerage) {
		this.sewerage = sewerage;
	}

	public Sidewalk getSidewalk() {
		return sidewalk;
	}

	public void setSidewalk(Sidewalk sidewalk) {
		this.sidewalk = sidewalk;
	}

	public LotTopography getLotTopography() {
		return lotTopography;
	}

	public void setLotTopography(LotTopography lotTopography) {
		this.lotTopography = lotTopography;
	}

	public FenceMaterial getFenceMaterial() {
		return fenceMaterial;
	}

	public void setFenceMaterial(FenceMaterial fenceMaterial) {
		this.fenceMaterial = fenceMaterial;
	}

	public StreetMaterial getStreetMaterial() {
		return streetMaterial;
	}

	public void setStreetMaterial(StreetMaterial streetMaterial) {
		this.streetMaterial = streetMaterial;
	}

	public LotPosition getLotPosition() {
		return lotPosition;
	}

	public void setLotPosition(LotPosition lotPosition) {
		this.lotPosition = lotPosition;
	}

	public List<CheckingRecord> getCheckingRecords() {
		return checkingRecords;
	}

	public void setCheckingRecords(List<CheckingRecord> checkingRecords) {
		this.checkingRecords = checkingRecords;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<WaterSupply> getWaterSupplies() {
		return waterSupplies;
	}

	public void setWaterSupplies(List<WaterSupply> waterSupplies) {
		this.waterSupplies = waterSupplies;
	}

	public Block getBlock() {		
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the garbageCollection
	 */
	public GarbageCollection getGarbageCollection() {
		return garbageCollection;
	}

	/**
	 * @param garbageCollection
	 *            the garbageCollection to set
	 */
	public void setGarbageCollection(GarbageCollection garbageCollection) {
		this.garbageCollection = garbageCollection;
	}

	/**
	 * @return the currentDomain
	 */
	public Domain getCurrentDomain() {
		return currentDomain;
	}

	/**
	 * @param currentDomain
	 *            the currentDomain to set
	 */
	public void setCurrentDomain(Domain currentDomain) {
		if (currentDomain != this.currentDomain) {
			this.currentDomain = currentDomain;
			if (currentDomain != null)
				this.currentDomain.setCurrentProperty(this);
		}
	}

	/**
	 * @return the domains
	 */
	public List<Domain> getDomains() {
		return domains;
	}

	/**
	 * @param domains
	 *            the domains to set
	 */
	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

	/**
	 * @return the appraisals
	 */
	public List<Appraisal> getAppraisals() {
		return appraisals;
	}

	/**
	 * @param appraisals
	 *            the appraisals to set
	 */
	public void setAppraisals(List<Appraisal> appraisals) {
		this.appraisals = appraisals;
	}

	/**
	 * @return the buildings
	 */
	public List<Building> getBuildings() {
		return buildings;
	}

	/**
	 * @param buildings
	 *            the buildings to set
	 */
	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}

	/**
	 * @return the propertyLandUses
	 */
	public List<PropertyLandUse> getPropertyLandUses() {
		return propertyLandUses;
	}

	/**
	 * @param propertyLandUses
	 *            the propertyLandUses to set
	 */
	public void setPropertyLandUses(List<PropertyLandUse> propertyLandUses) {
		this.propertyLandUses = propertyLandUses;
	}

	/**
	 * @return the buildingNumber
	 */
	public Integer getBuildingNumber() {
		return buildingNumber;
	}

	/**
	 * @param buildingNumber
	 *            the buildingNumber to set
	 */
	public void setBuildingNumber(Integer buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	/**
	 * @return the floorNumber
	 */
	public Integer getFloorNumber() {
		return floorNumber;
	}

	/**
	 * @param floorNumber
	 *            the floorNumber to set
	 */
	public void setFloorNumber(Integer floorNumber) {
		this.floorNumber = floorNumber;
	}

	/**
	 * @return the housingUnitNumber
	 */
	public Integer getHousingUnitNumber() {
		return housingUnitNumber;
	}

	/**
	 * @param housingUnitNumber
	 *            the housingUnitNumber to set
	 */
	public void setHousingUnitNumber(Integer housingUnitNumber) {
		this.housingUnitNumber = housingUnitNumber;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	public void add(PropertyLandUse propertyLandUse) {
		if (!this.propertyLandUses.contains(propertyLandUse)) {
			this.propertyLandUses.add(propertyLandUse);
			propertyLandUse.setProperty(this);
		}
	}

	public void remove(PropertyLandUse propertyLandUse) {
		boolean removed = this.propertyLandUses.remove(propertyLandUse);
		if (removed)
			propertyLandUse.setProperty((Property) null);
	}

	public void add(CheckingRecord checkingRecord) {
		if (!this.checkingRecords.contains(checkingRecord)) {
            checkingRecord.setProperty(this);
			this.checkingRecords.add(checkingRecord);			
		}
	}

	public void remove(CheckingRecord checkingRecord) {
		this.checkingRecords.remove(checkingRecord);
	}

	public void add(WaterSupply waterSupply) {
		if (!this.waterSupplies.contains(waterSupply)) {
			this.waterSupplies.add(waterSupply);
			waterSupply.setProperty(this);
		}
	}

	public void remove(WaterSupply waterSupply) {
		boolean removed = this.waterSupplies.remove(waterSupply);
		if (removed)
			waterSupply.setProperty((Property) null);
	}

	public void add(Domain domain) {
		if (!this.domains.contains(domain)) {
			this.domains.add(domain);
			domain.setProperty(this);
		}
	}

	public void remove(Domain domain) {
		boolean removed = this.domains.remove(domain);
		if (removed)
			domain.setProperty((Property) null);
	}

	public void add(Building building) {
		if (!this.buildings.contains(building)) {
			this.buildings.add(building);
			building.setProperty(this);
		}
	}

	public void remove(Building building) {
		boolean removed = this.buildings.remove(building);
		if (removed)
			building.setProperty((Property) null);
	}

	public void add(Appraisal appraisal) {
		if (!this.appraisals.contains(appraisal)) {
			if ((this.getCurrentDomain().getLotAppraisal().compareTo(lastAppraisal.getLot()) != 0) || 
					(this.getCurrentDomain().getBuildingAppraisal().compareTo(lastAppraisal.getBuilding()) != 0)){
				this.appraisals.add(appraisal);
				appraisal.setProperty(this);
			}
		}
	}

	public void setUrban(boolean isUrban) {
		this.isUrban = isUrban;
	}

	public boolean isUrban() {
		return isUrban;
	}

	public void setTotalYearsFromLastChangeOwner(
			int totalYearsFromLastChangeOwner) {
		this.totalYearsFromLastChangeOwner = totalYearsFromLastChangeOwner;
	}

	public int getTotalYearsFromLastChangeOwner() {
		return totalYearsFromLastChangeOwner;
	}
	
	public String toString(){
		String previousCode;
		String code;
		
		previousCode = previousCadastralCode != null ? previousCadastralCode : "";
		code = cadastralCode != null ? cadastralCode : "";
		return code+" - "+previousCode+" - "+getAddress();	
		
	}
	
	public String getAddress(){
		String street = "";
		String number = "";
		String refAdd = "";
		if(location != null && location.getMainBlockLimit() != null && location.getMainBlockLimit().getStreet() != null){
			Street streetObject = location.getMainBlockLimit().getStreet();
			street = streetObject.getName() != null ? streetObject.getName() : "";
			number = location.getHouseNumber() != null ? location.getHouseNumber() : "";
			refAdd = addressReference != null ? addressReference : "";
		}
		return street+" "+number+" "+refAdd;
	}
		
	public Boolean getNeedConfirmChangeOwner() {
		return needConfirmChangeOwner;
	}

	public void setNeedConfirmChangeOwner(Boolean needConfirmChangeOwner) {
		this.needConfirmChangeOwner = needConfirmChangeOwner;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	public Appraisal getLastAppraisal() {
		return lastAppraisal;
	}

	public void setLastAppraisal(Appraisal lastAppraisal) {
		this.lastAppraisal = lastAppraisal;
	}

	public BigDecimal getAppraisalRelationFactor() {
		return appraisalRelationFactor;
	}

	public void setAppraisalRelationFactor(BigDecimal appraisalRelationFactor) {
		this.appraisalRelationFactor = appraisalRelationFactor;
	}

	public BigDecimal getAppraisalAreaFactor() {
		return appraisalAreaFactor;
	}

	public void setAppraisalAreaFactor(BigDecimal appraisalAreaFactor) {
		this.appraisalAreaFactor = appraisalAreaFactor;
	}

	public BigDecimal getAffectationFactorLot() {
		return affectationFactorLot;
	}

	public void setAffectationFactorLot(BigDecimal affectationFactorLot) {
		this.affectationFactorLot = affectationFactorLot;
	}

	public BigDecimal getAffectationFactorBuilding() {
		return affectationFactorBuilding;
	}

	public void setAffectationFactorBuilding(BigDecimal affectationFactorBuilding) {
		this.affectationFactorBuilding = affectationFactorBuilding;
	}

	public boolean isDuplicate() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}

	public String getAddressReference() {
		return addressReference;
	}

	public void setAddressReference(String addressReference) {
		this.addressReference = addressReference;
	}
	
	

	public BigDecimal getValueBySquareMeterForDomain() {
		if (this.currentDomain != null){
			if ((this.currentDomain.getLotAppraisal() != null) && (this.area != null)){
				if (this.getLotAliquot().compareTo(new BigDecimal(100)) == 0){
					return this.currentDomain.getLotAppraisal().divide(this.area, 2, RoundingMode.HALF_UP);
				}
				else{
					return this.currentDomain.getLotAppraisal().multiply(new BigDecimal(100)).divide(this.area, 2, RoundingMode.HALF_UP).divide(this.getLotAliquot(), 2, RoundingMode.HALF_UP);
				}
			}
			else return BigDecimal.ZERO;
		}

		return BigDecimal.ZERO;
	}

	public void setValueBySquareMeterForDomain(
			BigDecimal valueBySquareMeterForDomain) {
		this.valueBySquareMeterForDomain = valueBySquareMeterForDomain;
	}

	public void updateValueBySquareMeterForDomain() {
		if (this.currentDomain != null){
			if ((this.currentDomain.getLotAppraisal() != null) && (this.area != null)){
				this.valueBySquareMeterForDomain = this.currentDomain.getLotAppraisal().divide(this.area);
				this.valueBySquareMeterForDomain = this.valueBySquareMeterForDomain.setScale(2, RoundingMode.HALF_UP);
			}
		}
	}

	public PropertyLocationType getPropertyLocationType() {
		return propertyLocationType;
	}

	public void setPropertyLocationType(PropertyLocationType propertyLocationType) {
		this.propertyLocationType = propertyLocationType;
	}
 
	// para consultas por la clave antigua
	@Column(name = "cadastralcode_old")
	private String cadastralCode_old;

	public String getCadastralCode_old() {
		return cadastralCode_old;
	}

	public void setCadastralCode_old(String cadastralCode_old) {
		this.cadastralCode_old = cadastralCode_old;
	}

}