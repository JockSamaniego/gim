package ec.gob.gim.income.model.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class ClosignBoxDetailsTypesDTO {

	@NativeQueryResultColumn(index = 0)
	private String paymenttype;
	@NativeQueryResultColumn(index = 1)
	private BigDecimal totaltype;
	@NativeQueryResultColumn(index = 2)
	private int numberpayments;
	
	public String getPaymenttype() {
		return paymenttype;
	}
	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}
	public BigDecimal getTotaltype() {
		return totaltype;
	}
	public void setTotaltype(BigDecimal totaltype) {
		this.totaltype = totaltype;
	}
	public int getNumberpayments() {
		return numberpayments;
	}
	public void setNumberpayments(int numberpayments) {
		this.numberpayments = numberpayments;
	}
	
	@Override
	public String toString() {
		return "ClosignBoxDetailsTypesDTO [paymenttype=" + paymenttype
				+ ", totaltype=" + totaltype + ", numberpayments="
				+ numberpayments + "]";
	}
	
}