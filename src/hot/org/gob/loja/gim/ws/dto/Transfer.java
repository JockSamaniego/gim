package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;

public class Transfer {
	private BigDecimal amount;
	private Long municipalBondId;
	private String transactionNumber;
	
	public Transfer(){
	}
	
	public Transfer(BigDecimal amount, Long municipalBondId, String transactionNumber) {
		this.amount = amount;
		this.municipalBondId = municipalBondId;
		this.transactionNumber = transactionNumber;
	}
	
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Long getMunicipalBondId() {
		return municipalBondId;
	}
	public void setMunicipalBondId(Long municipalBondId) {
		this.municipalBondId = municipalBondId;
	}
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
}
