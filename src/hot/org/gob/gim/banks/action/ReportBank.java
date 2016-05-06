package org.gob.gim.banks.action;

import java.math.BigDecimal;
import java.util.Date;

public class ReportBank {

	private Date date;
	private String liquidationTime;
	private Long id;
	private Long number; 
	private String identificationNumber;
	private BigDecimal value;
	private Long entry_id; 
	private String groupingcode;
	private String watermeternumber;
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getLiquidationTime() {
		return liquidationTime;
	}
	public void setLiquidationTime(String liquidationTime) {
		this.liquidationTime = liquidationTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public Long getEntry_id() {
		return entry_id;
	}
	public void setEntry_id(Long entry_id) {
		this.entry_id = entry_id;
	}
	public String getGroupingcode() {
		return groupingcode;
	}
	public void setGroupingcode(String groupingcode) {
		this.groupingcode = groupingcode;
	}
	public String getWatermeternumber() {
		return watermeternumber;
	}
	public void setWatermeternumber(String watermeternumber) {
		this.watermeternumber = watermeternumber;
	}
}


