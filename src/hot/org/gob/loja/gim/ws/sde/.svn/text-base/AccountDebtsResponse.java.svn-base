package org.gob.loja.gim.ws.sde;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QueryAccountDebtsResponse")   
public class AccountDebtsResponse {

	private String accountId;
	private Debt[] debt;
	
	public AccountDebtsResponse() {
	}

	public AccountDebtsResponse(String accountId, Debt[] debt) {
		this.accountId = accountId;
		this.debt = debt;
	}
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Debt[] getDebt() {
		return debt;
	}

	public void setDebt(Debt[] debt) {
		this.debt = debt;
	}

}
