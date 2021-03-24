package org.gob.loja.gim.ws.dto.preemission;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.NotEmpty;

;

public abstract class CommonRequest {

	@NotNull(message = "emiterIdentification no puede ser nulo")
	@NotEmpty(message = "emiterIdentification no puede ser vacío")
	private String emiterIdentification;

	@NotNull(message = "residentIdentification no puede ser nulo")
	@NotEmpty(message = "residentIdentification no puede ser vacío")
	private String residentIdentification;

	@NotNull(message = "accountCode no puede ser nulo")
	@NotEmpty(message = "accountCode no puede ser vacío")
	private String accountCode;

	@NotNull(message = "explanation no puede ser nulo")
	@NotEmpty(message = "explanation no puede ser vacío")
	private String explanation;

	@NotNull(message = "reference no puede ser nulo")
	@NotEmpty(message = "reference no puede ser vacío")
	private String reference;

	@NotNull(message = "address no puede ser nulo")
	@NotEmpty(message = "address no puede ser vacío")
	private String address;

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

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "CommonRequest [emiterIdentification=" + emiterIdentification
				+ ", residentIdentification=" + residentIdentification
				+ ", accountCode=" + accountCode + ", explanation="
				+ explanation + ", reference=" + reference + ", address="
				+ address + "]";
	}

}
