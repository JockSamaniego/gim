package ec.gob.gim.revenue.model.DTO;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class CRTV_ORDER {

	@NativeQueryResultColumn(index = 0)
	private BigDecimal sumtotal;
		
	@NativeQueryResultColumn(index = 1)
	private String ordernumber;
	
	
	@NativeQueryResultColumn(index = 2)
	private String identification;

	public BigDecimal getSumtotal() {
		return sumtotal;
	}

	public void setSumtotal(BigDecimal sumtotal) {
		this.sumtotal = sumtotal;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}	

}
