package org.gob.loja.gim.ws.dto.preemission;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.NotEmpty;

public class RuralPropertyRequest extends CommonRequest {

	@NotNull(message = "cadastralCode no puede ser nulo")
	@NotEmpty(message = "cadastralCode no puede ser vacío")
	private String cadastralCode;

	@NotNull(message = "cadastralCode no puede ser nulo")
	private Integer year;

	@NotNull(message = "value no puede ser nulo")
	private BigDecimal value;

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

	@Override
	public String toString() {
		return "RuralPropertyRequest [cadastralCode=" + cadastralCode
				+ ", year=" + year + ", value=" + value
				+ ", getEmiterIdentification()=" + getEmiterIdentification()
				+ ", getResidentIdentification()="
				+ getResidentIdentification() + ", getAccountCode()="
				+ getAccountCode() + ", getExplanation()=" + getExplanation()
				+ ", getReference()=" + getReference() + ", getAddress()="
				+ getAddress() + "]";
	}

}
