package org.gob.loja.gim.ws.dto.preemission.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.InList;
import org.gob.gim.common.validators.NotEmpty;

public class AccountWithoutAdjunctRequest extends CommonRequest {

	@NotNull(message = "value no puede ser nulo")
	private BigDecimal value;

	@NotNull(message = "accountCode no puede ser nulo")
	@NotEmpty(message = "accountCode no puede ser vacío")
	// se permite unicamente SERVICIOS ADMINISTRATIVOS y TASA POR SERVICIO DE ESCOMBRERA
	@InList(values = { "00073", "00744" }, message = "accountCode no autorizado")
	private String accountCode;

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

	@Override
	public String toString() {
		return "AccountWithoutAdjunctRequest [value=" + value
				+ ", accountCode=" + accountCode
				+ ", getEmiterIdentification()=" + getEmiterIdentification()
				+ ", getResidentIdentification()="
				+ getResidentIdentification() + ", getExplanation()="
				+ getExplanation() + ", getReference()=" + getReference()
				+ ", getAddress()=" + getAddress() + "]";
	}

}
