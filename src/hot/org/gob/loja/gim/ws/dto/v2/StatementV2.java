package org.gob.loja.gim.ws.dto.v2;

import java.util.Date;
import java.util.List;

import org.gob.loja.gim.ws.dto.Bond;
import org.gob.loja.gim.ws.dto.Taxpayer;

public class StatementV2 {
	
	private Taxpayer taxpayer;
	private List<Bond> bonds;
	private Date paymentDate;

	private String code;
	private String message;

	public StatementV2() {
	}

	public StatementV2(Taxpayer taxpayer, List<Bond> bonds, Date paymentDate) {
		this.taxpayer = taxpayer;
		this.bonds = bonds;
		this.paymentDate = paymentDate;

	}
	
	public void setData(Taxpayer taxpayer, List<Bond> bonds, Date paymentDate) {
		this.taxpayer = taxpayer;
		this.bonds = bonds;
		this.paymentDate = paymentDate;

	}

	public Taxpayer getTaxpayer() {
		return taxpayer;
	}

	public void setTaxpayer(Taxpayer taxpayer) {
		this.taxpayer = taxpayer;
	}

	public List<Bond> getBonds() {
		return bonds;
	}

	public void setBonds(List<Bond> bonds) {
		this.bonds = bonds;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

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
		return "StatementV2 [code=" + code + ", message=" + message + "]";
	}

}
