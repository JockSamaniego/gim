package org.gob.loja.gim.ws.dto.preemission;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.NotEmpty;

public class BuildingPermitRequest extends CommonRequest {

	@NotNull(message = "value no puede ser nulo")
	private BigDecimal value;

	@NotNull(message = "cadastralCode no puede ser nulo")
	@NotEmpty(message = "cadastralCode no puede ser vacío")
	private String cadastralCode;

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	@Override
	public String toString() {
		return "BuildingPermitRequest [value=" + value + ", cadastralCode="
				+ cadastralCode + "]";
	}

}
