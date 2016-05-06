package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ClosingStatement {
	private Date paymentDate;
	private BigDecimal total;
	private List<Transfer> transfers;
	
	public ClosingStatement() {
	}
	
	public ClosingStatement(List<Transfer> transfers, BigDecimal total, Date paymentDate) { 
		this.transfers = transfers;
		this.paymentDate = paymentDate;
	}
	
	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<Transfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(List<Transfer> transfers) {
		this.transfers = transfers;
	}
		
}
