package org.gob.gim.income.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity	
public class BondAuxDTO {
	@NativeQueryResultColumn(index = 0)
	private Date liquidationDate;

	@NativeQueryResultColumn(index = 1)
	private Date liquidationTime;

	@NativeQueryResultColumn(index = 2)
	private BigDecimal payvalue;
	
	@NativeQueryResultColumn(index = 3)
	private String status;
	
	@NativeQueryResultColumn(index = 4)
	private Long depositid;
	
	@NativeQueryResultColumn(index = 5)
	private Long municipalbondid;
	
	@NativeQueryResultColumn(index = 6)
	private String type;

	public Date getLiquidationDate() {
		return liquidationDate;
	}

	public void setLiquidationDate(Date liquidationDate) {
		this.liquidationDate = liquidationDate;
	}

	public Date getLiquidationTime() {
		return liquidationTime;
	}

	public void setLiquidationTime(Date liquidationTime) {
		this.liquidationTime = liquidationTime;
	}

	public BigDecimal getPayvalue() {
		return payvalue;
	}

	public void setPayvalue(BigDecimal payvalue) {
		this.payvalue = payvalue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getDepositid() {
		return depositid;
	}

	public void setDepositid(Long depositid) {
		this.depositid = depositid;
	}

	public Long getMunicipalbondid() {
		return municipalbondid;
	}

	public void setMunicipalbondid(Long municipalbondid) {
		this.municipalbondid = municipalbondid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
