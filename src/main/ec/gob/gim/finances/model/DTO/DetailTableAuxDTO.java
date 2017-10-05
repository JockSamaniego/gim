package ec.gob.gim.finances.model.DTO;

import java.math.BigDecimal;

public class DetailTableAuxDTO {
	
	private Long detail_id = null;
	
	private Integer index;
	
	private Long bond_number;
	
	private String month_name;
	
	private Integer year;
	
	private Long previous_reading;
	
	private Long current_reading;
	
	private BigDecimal m3;
	
	private BigDecimal value;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Long getBond_number() {
		return bond_number;
	}

	public void setBond_number(Long bond_number) {
		this.bond_number = bond_number;
	}

	public String getMonth_name() {
		return month_name;
	}

	public void setMonth_name(String month_name) {
		this.month_name = month_name;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Long getPrevious_reading() {
		return previous_reading;
	}

	public void setPrevious_reading(Long previous_reading) {
		this.previous_reading = previous_reading;
	}

	public Long getCurrent_reading() {
		return current_reading;
	}

	public void setCurrent_reading(Long current_reading) {
		this.current_reading = current_reading;
	}

	public BigDecimal getM3() {
		return m3;
	}

	public void setM3(BigDecimal m3) {
		this.m3 = m3;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public Long getDetail_id() {
		return detail_id;
	}

	public void setDetail_id(Long detail_id) {
		this.detail_id = detail_id;
	}

	@Override
	public String toString() {
		return "DetailTableAuxDTO [index=" + index + ", bond_number="
				+ bond_number + ", month_name=" + month_name + ", year=" + year
				+ ", previous_reading=" + previous_reading
				+ ", current_reading=" + current_reading + ", m3=" + m3
				+ ", value=" + value + "]";
	}
	
}