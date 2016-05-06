package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 15-Ago-2011 18:45:44
 */
@Audited
@Entity
@TableGenerator(
	 name="BuildingGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Building",
	 initialValue=1, allocationSize=1
)
public class Building {

	@Id
	@GeneratedValue(generator="BuildingGenerator",strategy=GenerationType.TABLE)
	
	private Long id;
	
	private BigDecimal area;
	private Integer number;
	private Integer floorsNumber;
	private Integer buildingYear;
	private Integer anioConst;
	
	@Transient
	private BigDecimal totalArea;
	
	private Boolean isFinished;
	private Boolean hasEquipment;
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private PreservationState preservationState;
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private WallMaterial wallMaterial;
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private StructureMaterial structureMaterial;
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private RoofMaterial roofMaterial;
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private ExternalFinishing externalFinishing;
	
	/**
	 * Relationship
	 */
	@ManyToOne
	private Property property;

	public Building(){

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the area
	 */
	public BigDecimal getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(BigDecimal area) {
		this.area = area;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @return the floorsNumber
	 */
	public Integer getFloorsNumber() {
		return floorsNumber;
	}

	/**
	 * @param floorsNumber the floorsNumber to set
	 */
	public void setFloorsNumber(Integer floorsNumber) {
		this.floorsNumber = floorsNumber;
	}

	/**
	 * @return the buildingYear
	 */
	public Integer getBuildingYear() {
		return buildingYear;
	}

	/**
	 * @param buildingYear the buildingYear to set
	 */
	public void setBuildingYear(Integer buildingYear) {
		this.buildingYear = buildingYear;
	}

	public Integer getAnioConst() {
		return anioConst;
	}

	public void setAnioConst(Integer anioConst) {
		this.anioConst = anioConst;
	}

	/**
	 * @return the isFinished
	 */
	public Boolean getIsFinished() {
		return isFinished;
	}

	/**
	 * @param isFinished the isFinished to set
	 */
	public void setIsFinished(Boolean isFinished) {
		this.isFinished = isFinished;
	}

	/**
	 * @return the hasEquipment
	 */
	public Boolean getHasEquipment() {
		return hasEquipment;
	}

	/**
	 * @param hasEquipment the hasEquipment to set
	 */
	public void setHasEquipment(Boolean hasEquipment) {
		this.hasEquipment = hasEquipment;
	}

	/**
	 * @return the preservationState
	 */
	public PreservationState getPreservationState() {
		return preservationState;
	}

	/**
	 * @param preservationState the preservationState to set
	 */
	public void setPreservationState(PreservationState preservationState) {
		this.preservationState = preservationState;
	}

	/**
	 * @return the wallMaterial
	 */
	public WallMaterial getWallMaterial() {
		return wallMaterial;
	}

	/**
	 * @param wallMaterial the wallMaterial to set
	 */
	public void setWallMaterial(WallMaterial wallMaterial) {
		this.wallMaterial = wallMaterial;
	}

	/**
	 * @return the structureMaterial
	 */
	public StructureMaterial getStructureMaterial() {
		return structureMaterial;
	}

	/**
	 * @param structureMaterial the structureMaterial to set
	 */
	public void setStructureMaterial(StructureMaterial structureMaterial) {
		this.structureMaterial = structureMaterial;
	}

	/**
	 * @return the roofMaterial
	 */
	public RoofMaterial getRoofMaterial() {
		return roofMaterial;
	}

	/**
	 * @param roofMaterial the roofMaterial to set
	 */
	public void setRoofMaterial(RoofMaterial roofMaterial) {
		this.roofMaterial = roofMaterial;
	}

	/**
	 * @return the externalFinishing
	 */
	public ExternalFinishing getExternalFinishing() {
		return externalFinishing;
	}

	/**
	 * @param externalFinishing the externalFinishing to set
	 */
	public void setExternalFinishing(ExternalFinishing externalFinishing) {
		this.externalFinishing = externalFinishing;
	}

	/**
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(Property property) {
		this.property = property;
	}

	public void setTotalArea(BigDecimal totalArea) {
		this.totalArea = totalArea;
	}

	public BigDecimal getTotalArea() {
		return totalArea;
	}
	
	
	/*private java.text.NumberFormat getNumberFormat(){
		java.text.NumberFormat numberFormat = new java.text.DecimalFormat("00");
		numberFormat.setMaximumIntegerDigits(2);
		return numberFormat;
	}
	
	public String getFormattedNumber(){
		return getNumberFormat().format(number);
	}*/
	
	
}//end Building