package ec.gob.gim.income.model.dto;

import java.math.BigDecimal;
import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class SummaryClosingBoxDTO {

	@NativeQueryResultColumn(index = 0)
	private BigDecimal totalCapital;

	@NativeQueryResultColumn(index = 1)
	private BigDecimal totalInterest;

	@NativeQueryResultColumn(index = 2)
	private BigDecimal totalSurcharge;

	@NativeQueryResultColumn(index = 3)
	private BigDecimal totalTaxes;

	@NativeQueryResultColumn(index = 4)
	private BigDecimal totalDiscount;

	@NativeQueryResultColumn(index = 5)
	private BigDecimal totalPayment;

	public BigDecimal getTotalCapital() {
		return totalCapital;
	}

	public void setTotalCapital(BigDecimal totalCapital) {
		this.totalCapital = totalCapital;
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

	public BigDecimal getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(BigDecimal totalPayment) {
		this.totalPayment = totalPayment;
	}


}
