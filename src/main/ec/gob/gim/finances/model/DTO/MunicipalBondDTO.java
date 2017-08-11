package ec.gob.gim.finances.model.DTO;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class MunicipalBondDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Integer number;
	
	@NativeQueryResultColumn(index = 1)
	private String status;
	
	@NativeQueryResultColumn(index = 2)
	private String emission_date;
	
	@NativeQueryResultColumn(index = 3)
	private String expiration_date;
	
	@NativeQueryResultColumn(index = 4)
	private String liquidation_date;
	
	@NativeQueryResultColumn(index = 5)
	private String description;
	
	@NativeQueryResultColumn(index = 6)
	private BigDecimal amount;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmission_date() {
		return emission_date;
	}

	public void setEmission_date(String emission_date) {
		this.emission_date = emission_date;
	}

	public String getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}

	public String getLiquidation_date() {
		return liquidation_date;
	}

	public void setLiquidation_date(String liquidation_date) {
		this.liquidation_date = liquidation_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}