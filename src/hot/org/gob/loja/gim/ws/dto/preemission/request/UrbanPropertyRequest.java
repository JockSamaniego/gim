package org.gob.loja.gim.ws.dto.preemission.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.InList;
import org.gob.gim.common.validators.NotEmpty;

public class UrbanPropertyRequest extends CommonPreemissionRequest {

	@NotNull(message = "accountCode no puede ser nulo")
	@NotEmpty(message = "accountCode no puede ser vacío")
	@InList(values = { "00056" }, message = "accountCode no autorizado")
	private String accountCode;

	@NotNull(message = "cadastralCode no puede ser nulo")
	@NotEmpty(message = "cadastralCode no puede ser vacío")
	private String cadastralCode;

	@NotNull(message = "year no puede ser nulo")
	private Integer year;

	@NotNull(message = "value no puede ser nulo")
	private BigDecimal value;

	@NotNull(message = "lotAppraisal no puede ser nulo")
	private BigDecimal lotAppraisal;

	@NotNull(message = "buildingAppraisal no puede ser nulo")
	private BigDecimal buildingAppraisal;

	@NotNull(message = "changeAppraisals no puede ser nulo")
	private Boolean changeAppraisals;

	@NotNull(message = "lotArea no puede ser nulo")
	private BigDecimal lotArea;

	@NotNull(message = "constructionArea no puede ser nulo")
	private BigDecimal constructionArea;

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public BigDecimal getLotAppraisal() {
		return lotAppraisal;
	}

	public void setLotAppraisal(BigDecimal lotAppraisal) {
		this.lotAppraisal = lotAppraisal;
	}

	public BigDecimal getBuildingAppraisal() {
		return buildingAppraisal;
	}

	public void setBuildingAppraisal(BigDecimal buildingAppraisal) {
		this.buildingAppraisal = buildingAppraisal;
	}

	public Boolean getChangeAppraisals() {
		return changeAppraisals;
	}

	public void setChangeAppraisals(Boolean changeAppraisals) {
		this.changeAppraisals = changeAppraisals;
	}


	public BigDecimal getLotArea() {
		return lotArea;
	}

	public void setLotArea(BigDecimal lotArea) {
		this.lotArea = lotArea;
	}

	public BigDecimal getConstructionArea() {
		return constructionArea;
	}

	public void setConstructionArea(BigDecimal constructionArea) {
		this.constructionArea = constructionArea;
	}

	@Override
	public String toString() {
		return "UrbanPropertyRequest [accountCode=" + accountCode
				+ ", cadastralCode=" + cadastralCode + ", year=" + year
				+ ", value=" + value + ", lotAppraisal=" + lotAppraisal
				+ ", buildingAppraisal=" + buildingAppraisal
				+ ", changeAppraisals=" + changeAppraisals
				+ ", lotArea=" + lotArea + ", constructionArea="
				+ constructionArea + "]";
	}

}
