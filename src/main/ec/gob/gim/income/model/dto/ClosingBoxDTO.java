package ec.gob.gim.income.model.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class ClosingBoxDTO {

	@NativeQueryResultColumn(index = 0)
	private BigDecimal totalaccounts;

	@NativeQueryResultColumn(index = 1)
	private BigDecimal interest;

	@NativeQueryResultColumn(index = 2)
	private BigDecimal surcharge;

	@NativeQueryResultColumn(index = 3)
	private BigDecimal discount;

	@NativeQueryResultColumn(index = 4)
	private BigDecimal taxes;

	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal paymentagreements;

	
	public BigDecimal getTotalaccounts() {
		return totalaccounts;
	}

	public void setTotalaccounts(BigDecimal totalaccounts) {
		this.totalaccounts = totalaccounts;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(BigDecimal surcharge) {
		this.surcharge = surcharge;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getPaymentagreements() {
		return paymentagreements;
	}

	public void setPaymentagreements(BigDecimal paymentagreements) {
		this.paymentagreements = paymentagreements;
	}

	@Override
	public String toString() {
		return "ClosingBoxDTO [totalaccounts=" + totalaccounts + ", interest="
				+ interest + ", surcharge=" + surcharge + ", discount="
				+ discount + ", taxes=" + taxes + ", paymentagreements="
				+ paymentagreements + "]";
	}
	
}