package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

import ec.gob.gim.cadaster.model.Property;
@NativeQueryResultEntity
public class WorkDealFull {

	@NativeQueryResultColumn(index = 0)
	private Long id;
	@NativeQueryResultColumn(index = 1)	
	private String address;
	@NativeQueryResultColumn(index = 2)	
	private BigDecimal contributionFront;
	@NativeQueryResultColumn(index = 3)	
	private BigDecimal differentiatedValue;
	@NativeQueryResultColumn(index = 4)	
	private BigDecimal frontLength;
	@NativeQueryResultColumn(index = 5)	
	private BigDecimal sharedValue;
	@NativeQueryResultColumn(index = 6)	
	private BigDecimal total;
	@NativeQueryResultColumn(index = 7)	
	private Long propertyId;
	@NativeQueryResultColumn(index = 8)	
	private Long workdealId;
	@NativeQueryResultColumn(index = 9)
	private BigDecimal sewerageValue;
	@NativeQueryResultColumn(index = 10)
	private BigDecimal waterValue;
	@NativeQueryResultColumn(index = 11)
	private BigDecimal commercialAppraisal;
	@NativeQueryResultColumn(index = 12)
	private String identificationNumber;
	@NativeQueryResultColumn(index = 13)
	private String name;
	@NativeQueryResultColumn(index = 14)
	private String previousCadastralCode;
	@NativeQueryResultColumn(index = 15)
	private BigDecimal lotAliquot;
	@NativeQueryResultColumn(index = 16)
	private String cadastralCode;
	
	private Property property;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public BigDecimal getContributionFront() {
		return contributionFront;
	}

	public void setContributionFront(BigDecimal contributionFront) {
		this.contributionFront = contributionFront;
	}

	public void setFrontLength(BigDecimal frontLength) {
		this.frontLength = frontLength;
	}

	public BigDecimal getDifferentiatedValue() {
		return differentiatedValue;
	}
	public void setDifferentiatedValue(BigDecimal differentiatedValue) {
		this.differentiatedValue = differentiatedValue;
	}
	public BigDecimal getFrontLength() {
		return frontLength;
	}
	 
	public BigDecimal getSharedValue() {
		return sharedValue;
	}
	public void setSharedValue(BigDecimal sharedValue) {
		this.sharedValue = sharedValue;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Long getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(Long propertyId) {
		this.propertyId = propertyId;
	}
	public Long getWorkdealId() {
		return workdealId;
	}
	public void setWorkdealId(Long workdealId) {
		this.workdealId = workdealId;
	}
	public BigDecimal getSewerageValue() {
		return sewerageValue;
	}
	public void setSewerageValue(BigDecimal sewerageValue) {
		this.sewerageValue = sewerageValue;
	}
	public BigDecimal getWaterValue() {
		return waterValue;
	}
	public void setWaterValue(BigDecimal waterValue) {
		this.waterValue = waterValue;
	}
	public BigDecimal getCommercialAppraisal() {
		return commercialAppraisal;
	}
	public void setCommercialAppraisal(BigDecimal commercialAppraisal) {
		this.commercialAppraisal = commercialAppraisal;
	}
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPreviousCadastralCode() {
		return previousCadastralCode;
	}
	
	public BigDecimal getLotAliquot() {
		return lotAliquot;
	}
	
	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}
	
	public void setPreviousCadastralCode(String previousCadastralCode) {
		this.previousCadastralCode = previousCadastralCode;
	}
	
	public void setLotAliquot(BigDecimal lotAliquot) {
		this.lotAliquot = lotAliquot;
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}
	
}