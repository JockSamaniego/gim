package ec.gob.gim.finances.model.DTO;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class WriteOffDetailDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Long bond_id;
	
	@NativeQueryResultColumn(index = 1)
	private Long bond_number;
	
	@NativeQueryResultColumn(index = 2)
	private Integer month;
	
	@NativeQueryResultColumn(index = 3)
	private String monthtype;
	
	@NativeQueryResultColumn(index = 4)
	private Integer year;
	
	@NativeQueryResultColumn(index = 5)
	private Long previousreading;
	
	@NativeQueryResultColumn(index = 6)
	private Long currentreading;
	
	@NativeQueryResultColumn(index = 7)
	private BigDecimal amount_m3;
	
	@NativeQueryResultColumn(index = 8)
	private BigDecimal value;
	
	@NativeQueryResultColumn(index = 9)
	private Long adjunct_id;
	
	public WriteOffDetailDTO() {
		this.adjunct_id = null;
		this.amount_m3 = null;
		this.bond_id = null;
		this.bond_number = null;
		this.currentreading = null;
		this.month = null;
		this.monthtype = null;
		this.previousreading = null;
		this.value = null;
		this.year = null;
	}
	public Long getBond_id() {
		return bond_id;
	}
	public void setBond_id(Long bond_id) {
		this.bond_id = bond_id;
	}
	public Long getBond_number() {
		return bond_number;
	}
	public void setBond_number(Long bond_number) {
		this.bond_number = bond_number;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public String getMonthtype() {
		return monthtype;
	}
	public void setMonthtype(String monthtype) {
		this.monthtype = monthtype;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Long getPreviousreading() {
		return previousreading;
	}
	public void setPreviousreading(Long previousreading) {
		this.previousreading = previousreading;
	}
	public Long getCurrentreading() {
		return currentreading;
	}
	public void setCurrentreading(Long currentreading) {
		this.currentreading = currentreading;
	}
	public BigDecimal getAmount_m3() {
		return amount_m3 == null? null: amount_m3.compareTo(new BigDecimal(-1))== 0?null:amount_m3;
	}
	public void setAmount_m3(BigDecimal amount_m3) {
		this.amount_m3 = amount_m3;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public Long getAdjunct_id() {
		return adjunct_id;
	}
	public void setAdjunct_id(Long adjunct_id) {
		this.adjunct_id = adjunct_id;
	}
	@Override
	public String toString() {
		return "WriteOffDetailDTO [bond_id=" + bond_id + ", bond_number="
				+ bond_number + ", month=" + month + ", monthtype=" + monthtype
				+ ", year=" + year + ", previousreading=" + previousreading
				+ ", currentreading=" + currentreading + ", amount_m3="
				+ amount_m3 + ", value=" + value + ", adjunct_id=" + adjunct_id
				+ "]";
	}
}