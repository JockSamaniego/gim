package org.gob.loja.gim.ws.dto.v2;

import java.math.BigDecimal;

public class DepositStatementV2 {

	private String code;
	private String message;

	private String reference;
	private String residentName;
	private String residentIdentificaciton;
	private BigDecimal total;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "DepositStatementV2 [code=" + code + ", message=" + message + "]";
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public String getResidentIdentificaciton() {
		return residentIdentificaciton;
	}

	public void setResidentIdentificaciton(String residentIdentificaciton) {
		this.residentIdentificaciton = residentIdentificaciton;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
