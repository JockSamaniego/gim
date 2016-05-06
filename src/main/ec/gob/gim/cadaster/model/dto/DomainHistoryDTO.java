package ec.gob.gim.cadaster.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DomainHistoryDTO {
	private Date date;
	private String username;
	private BigDecimal buildingAppraisal;
	private BigDecimal commercialAppraisal;
	private BigDecimal lotAppraisal;
	private String observations;
	private BigDecimal totalAreaConstruction;
	private BigDecimal valueBySquareMeter;
	private String identificationnumber;
	private String name;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public BigDecimal getBuildingAppraisal() {
		return buildingAppraisal;
	}
	public void setBuildingAppraisal(BigDecimal buildingAppraisal) {
		this.buildingAppraisal = buildingAppraisal;
	}
	public BigDecimal getCommercialAppraisal() {
		return commercialAppraisal;
	}
	public void setCommercialAppraisal(BigDecimal commercialAppraisal) {
		this.commercialAppraisal = commercialAppraisal;
	}
	public BigDecimal getLotAppraisal() {
		return lotAppraisal;
	}
	public void setLotAppraisal(BigDecimal lotAppraisal) {
		this.lotAppraisal = lotAppraisal;
	}
	public String getObservations() {
		return observations;
	}
	public void setObservations(String observations) {
		this.observations = observations;
	}
	public BigDecimal getTotalAreaConstruction() {
		return totalAreaConstruction;
	}
	public void setTotalAreaConstruction(BigDecimal totalAreaConstruction) {
		this.totalAreaConstruction = totalAreaConstruction;
	}
	public BigDecimal getValueBySquareMeter() {
		return valueBySquareMeter;
	}
	public void setValueBySquareMeter(BigDecimal valueBySquareMeter) {
		this.valueBySquareMeter = valueBySquareMeter;
	}
	public String getIdentificationnumber() {
		return identificationnumber;
	}
	public void setIdentificationnumber(String identificationnumber) {
		this.identificationnumber = identificationnumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}