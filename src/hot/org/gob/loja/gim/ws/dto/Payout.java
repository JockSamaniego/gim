package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Payout {
	private BigDecimal amount;
	private List<Long> bondIds;
	private Date paymentDate;
	//atributo para el id de la transaccion enviado por la entidad bancaria
	private String transactionId;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public List<Long> getBondIds() {
		return bondIds;
	}
	public void setBondIds(List<Long> bondIds) {
		this.bondIds = bondIds;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
}
