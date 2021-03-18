package org.gob.loja.gim.ws.dto.preemission;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.NotEmpty;

;

public class PreemitAdministrativeServicesRequest {

	@NotNull(message = "emiterIdentification no puede ser nulo")
	@NotEmpty(message = "emiterIdentification no puede ser vacío")
	private String emiterIdentification;

	@NotNull(message = "residentIdentification no puede ser nulo")
	@NotEmpty(message = "residentIdentification no puede ser vacío")
	private String residentIdentification;

	@NotNull(message = "accountCode no puede ser nulo")
	@NotEmpty(message = "accountCode no puede ser vacío")
	private String accountCode;

	@NotNull(message = "value no puede ser nulo")
	private BigDecimal value;

	@NotNull(message = "comment no puede ser nulo")
	@NotEmpty(message = "comment no puede ser vacío")
	private String comment;

	public String getEmiterIdentification() {
		return emiterIdentification;
	}

	public void setEmiterIdentification(String emiterIdentification) {
		this.emiterIdentification = emiterIdentification;
	}

	public String getResidentIdentification() {
		return residentIdentification;
	}

	public void setResidentIdentification(String residentIdentification) {
		this.residentIdentification = residentIdentification;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "PreemitAdministrativeServicesRequest [emiterIdentification="
				+ emiterIdentification + ", residentIdentification="
				+ residentIdentification + ", accountCode=" + accountCode
				+ ", value=" + value + ", comment=" + comment + "]";
	}

}
