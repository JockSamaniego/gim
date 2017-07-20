package ec.gob.gim.finances.model.DTO;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class ConsumptionPreviousDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Integer _year;
	
	@NativeQueryResultColumn(index = 1)
	private String _month;
	
	@NativeQueryResultColumn(index = 2)
	private String measurer;
	
	@NativeQueryResultColumn(index = 3)
	private String consumption_state;
	
	@NativeQueryResultColumn(index = 4)
	private Long current_reading;
	
	@NativeQueryResultColumn(index = 5)
	private Long previous_reading;

	@NativeQueryResultColumn(index = 6)
	private BigDecimal m3;
	
	@NativeQueryResultColumn(index = 7)
	private String paid_state;
	
	@NativeQueryResultColumn(index = 8)
	private BigDecimal paid_total;
	
	public Integer get_year() {
		return _year;
	}
	public void set_year(Integer _year) {
		this._year = _year;
	}
	public String get_month() {
		return _month;
	}
	public void set_month(String _month) {
		this._month = _month;
	}
	public String getMeasurer() {
		return measurer;
	}
	public void setMeasurer(String measurer) {
		this.measurer = measurer;
	}
	public String getConsumption_state() {
		return consumption_state;
	}
	public void setConsumption_state(String consumption_state) {
		this.consumption_state = consumption_state;
	}
	public Long getCurrent_reading() {
		return current_reading;
	}
	public void setCurrent_reading(Long current_reading) {
		this.current_reading = current_reading;
	}
	public Long getPrevious_reading() {
		return previous_reading;
	}
	public void setPrevious_reading(Long previous_reading) {
		this.previous_reading = previous_reading;
	}
	public BigDecimal getM3() {
		return m3;
	}
	public void setM3(BigDecimal m3) {
		this.m3 = m3;
	}
	public String getPaid_state() {
		return paid_state;
	}
	public void setPaid_state(String paid_state) {
		this.paid_state = paid_state;
	}
	public BigDecimal getPaid_total() {
		return paid_total;
	}
	public void setPaid_total(BigDecimal paid_total) {
		this.paid_total = paid_total;
	}

}