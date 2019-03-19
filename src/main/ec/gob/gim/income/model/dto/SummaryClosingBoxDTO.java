package ec.gob.gim.income.model.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class SummaryClosingBoxDTO {

	@NativeQueryResultColumn(index = 0)
	private String paymentType;
	
	@NativeQueryResultColumn(index = 1)
	private int numberPayments;
	
	@NativeQueryResultColumn(index = 2)
	private BigDecimal totalValue;
	
	@NativeQueryResultColumn(index = 3)
	private BigDecimal totalInterest;
	
	@NativeQueryResultColumn(index = 4)
	private BigDecimal totalSurcharge;
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal totalTaxes;
	
	@NativeQueryResultColumn(index = 6)
	private BigDecimal totalDiscount;
	
	@NativeQueryResultColumn(index = 7)
	private BigDecimal totalType;

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public int getNumberPayments() {
		return numberPayments;
	}

	public void setNumberPayments(int numberPayments) {
		this.numberPayments = numberPayments;
	}

	public BigDecimal getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(BigDecimal totalValue) {
		this.totalValue = totalValue;
	}

	public BigDecimal getTotalInterest() {
		return totalInterest;
	}

	public void setTotalInterest(BigDecimal totalInterest) {
		this.totalInterest = totalInterest;
	}

	public BigDecimal getTotalSurcharge() {
		return totalSurcharge;
	}

	public void setTotalSurcharge(BigDecimal totalSurcharge) {
		this.totalSurcharge = totalSurcharge;
	}

	public BigDecimal getTotalTaxes() {
		return totalTaxes;
	}

	public void setTotalTaxes(BigDecimal totalTaxes) {
		this.totalTaxes = totalTaxes;
	}

	public BigDecimal getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(BigDecimal totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public BigDecimal getTotalType() {
		return totalType;
	}

	public void setTotalType(BigDecimal totalType) {
		this.totalType = totalType;
	}
	
	
	
}
