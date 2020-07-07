package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;

import ec.gob.gim.cadaster.model.PropertyUse;


public class InformationToCalculateCEMDto {
	

	private Long property_id;
	

	private String cadastralCode;
	

	private String previousCadastralCode;
		

	private String residentIdentification;
	

	private String residentName;
	
	
	private PropertyUse propertyUse;
	
	
	private BigDecimal lotValueM2;
	
	
	private BigDecimal currentPropertyValueM2;
	
		
	private BigDecimal lotArea;
	

	private BigDecimal buildingAppraisal;
	

	private BigDecimal buildingAliquot;
	

	private BigDecimal buildingArea;
	

	private BigDecimal front;
	

	private BigDecimal frontsLength;
	

	private Integer frontsNumber;
	
	
	private BigDecimal lotAppraisal;
	

	private BigDecimal commercialAppraisal;
	

	private Integer cus;
	
	
	private Integer cos;
	
	
	public InformationToCalculateCEMDto (){
		
	}
	
	public InformationToCalculateCEMDto (Long property_id, String cadastralCode, String previousCadastralCode, String residentIdentification, 
			String residentName, PropertyUse propertyUse, BigDecimal lotValueM2, BigDecimal currentPropertyValueM2,
			BigDecimal lotArea, BigDecimal buildingAppraisal, BigDecimal buildingAliquot, BigDecimal buildingArea, BigDecimal front, BigDecimal frontsLength,
			Integer frontsNumber, BigDecimal lotAppraisal, BigDecimal commercialAppraisal, Integer cus, Integer cos){
		this.property_id = property_id;
		this.cadastralCode = cadastralCode;
		this.previousCadastralCode = previousCadastralCode;
		this.residentIdentification = residentIdentification;
		this.residentName = residentName;
		this.propertyUse = propertyUse;
		this.lotValueM2 = lotValueM2;
		this.currentPropertyValueM2 = currentPropertyValueM2;
		this.lotArea = lotArea;
		this.buildingAppraisal = buildingAppraisal;
		this.buildingAliquot = buildingAliquot;
		this.buildingArea = buildingArea;
		this.front = front;
		this.frontsLength = frontsLength;
		this.frontsNumber = frontsNumber;
		this.lotAppraisal = lotAppraisal;
		this.commercialAppraisal = commercialAppraisal;
		this.cus = cus;
		this.cos = cos;
	}

	public Long getProperty_id() {
		return property_id;
	}

	public void setProperty_id(Long property_id) {
		this.property_id = property_id;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public String getPreviousCadastralCode() {
		return previousCadastralCode;
	}

	public void setPreviousCadastralCode(String previousCadastralCode) {
		this.previousCadastralCode = previousCadastralCode;
	}

	public String getResidentIdentification() {
		return residentIdentification;
	}

	public void setResidentIdentification(String residentIdentification) {
		this.residentIdentification = residentIdentification;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public BigDecimal getLotValueM2() {
		return lotValueM2;
	}

	public void setLotValueM2(BigDecimal lotValueM2) {
		this.lotValueM2 = lotValueM2;
	}

	public BigDecimal getLotArea() {
		return lotArea;
	}

	public void setLotArea(BigDecimal lotArea) {
		this.lotArea = lotArea;
	}

	public BigDecimal getBuildingAppraisal() {
		return buildingAppraisal;
	}

	public void setBuildingAppraisal(BigDecimal buildingAppraisal) {
		this.buildingAppraisal = buildingAppraisal;
	}

	public BigDecimal getBuildingAliquot() {
		return buildingAliquot;
	}

	public void setBuildingAliquot(BigDecimal buildingAliquot) {
		this.buildingAliquot = buildingAliquot;
	}

	public BigDecimal getBuildingArea() {
		return buildingArea;
	}

	public void setBuildingArea(BigDecimal buildingArea) {
		this.buildingArea = buildingArea;
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

	public BigDecimal getLotAppraisal() {
		return lotAppraisal;
	}

	public void setLotAppraisal(BigDecimal lotAppraisal) {
		this.lotAppraisal = lotAppraisal;
	}

	public BigDecimal getCommercialAppraisal() {
		return commercialAppraisal;
	}

	public void setCommercialAppraisal(BigDecimal commercialAppraisal) {
		this.commercialAppraisal = commercialAppraisal;
	}

	public Integer getCus() {
		return cus;
	}

	public void setCus(Integer cus) {
		this.cus = cus;
	}

	public Integer getCos() {
		return cos;
	}

	public void setCos(Integer cos) {
		this.cos = cos;
	}

	public BigDecimal getCurrentPropertyValueM2() {
		return currentPropertyValueM2;
	}

	public void setCurrentPropertyValueM2(BigDecimal currentPropertyValueM2) {
		this.currentPropertyValueM2 = currentPropertyValueM2;
	}

	public PropertyUse getPropertyUse() {
		return propertyUse;
	}

	public void setPropertyUse(PropertyUse propertyUse) {
		this.propertyUse = propertyUse;
	}
	
	

}