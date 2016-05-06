package org.gob.loja.gim.ws.sde;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder={"debtDescription", "debtId", "debtType","expiration", "fixedDebtAmount", "variableDebtAmountMax","variableDebtAmountMin"})
public class Debt {
	
	private String debtType;
	private String debtId;
	private String debtDescription;
	private String fixedDebtAmount;
	private String variableDebtAmountMin;
	private String variableDebtAmountMax;
	private Date expiration;

	public Debt() {
	}

	public Debt(String debtType, String debtId, String debtDescription, String fixedDebtAmount,
			String variableDebtAmountMin, String variableDebtAmountMax, Date expiration) {
		this.debtType = debtType;
		this.debtId = debtId;
		this.debtDescription = debtDescription;
		this.fixedDebtAmount = fixedDebtAmount;
		this.variableDebtAmountMin = variableDebtAmountMin;
		this.variableDebtAmountMax = variableDebtAmountMax;
		this.expiration = expiration;
	}
	
	public String getDebtType() {
		return debtType;
	}

	public void setDebtType(String debtType) {
		this.debtType = debtType;
	}
	
	public String getDebtId() {
		return debtId;
	}

	public void setDebtId(String debtId) {
		this.debtId = debtId;
	}

	public String getDebtDescription() {
		return debtDescription;
	}

	public void setDebtDescription(String debtDescription) {
		this.debtDescription = debtDescription;
	}

	public String getFixedDebtAmount() {
		return fixedDebtAmount;
	}

	public void setFixedDebtAmount(String fixedDebtAmount) {
		this.fixedDebtAmount = fixedDebtAmount;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getVariableDebtAmountMin() {
		return variableDebtAmountMin;
	}

	public void setVariableDebtAmountMin(String variableDebtAmountMin) {
		this.variableDebtAmountMin = variableDebtAmountMin;
	}

	public String getVariableDebtAmountMax() {
		return variableDebtAmountMax;
	}

	public void setVariableDebtAmountMax(String variableDebtAmountMax) {
		this.variableDebtAmountMax = variableDebtAmountMax;
	}

}
