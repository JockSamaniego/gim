package org.gob.gim.income.view;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class AgreementSummaryDTO {

	@NativeQueryResultColumn(index = 0)
	private String rubro;
	
	@NativeQueryResultColumn(index = 1)
	private Date f_min;
	
	@NativeQueryResultColumn(index = 2)
	private Date f_max;
	
	@NativeQueryResultColumn(index = 3)
	private Integer cantidad;
	
	@NativeQueryResultColumn(index = 4)
	private BigDecimal sum_val;
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal sum_total;

	public String getRubro() {
		return rubro;
	}

	public void setRubro(String rubro) {
		this.rubro = rubro;
	}

	public Date getF_min() {
		return f_min;
	}

	public void setF_min(Date f_min) {
		this.f_min = f_min;
	}

	public Date getF_max() {
		return f_max;
	}

	public void setF_max(Date f_max) {
		this.f_max = f_max;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getSum_val() {
		return sum_val;
	}

	public void setSum_val(BigDecimal sum_val) {
		this.sum_val = sum_val;
	}

	public BigDecimal getSum_total() {
		return sum_total;
	}

	public void setSum_total(BigDecimal sum_total) {
		this.sum_total = sum_total;
	}

}
