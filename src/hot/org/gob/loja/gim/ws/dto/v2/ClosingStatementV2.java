package org.gob.loja.gim.ws.dto.v2;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.gob.loja.gim.ws.dto.Transfer;

public class ClosingStatementV2 {
	private Date paymentDate;
	private BigDecimal total;
	private List<Transfer> transfers;

	private String code;
	private String message;

	public ClosingStatementV2() {
	}

	public ClosingStatementV2(List<Transfer> transfers, BigDecimal total, Date paymentDate) {
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

}
