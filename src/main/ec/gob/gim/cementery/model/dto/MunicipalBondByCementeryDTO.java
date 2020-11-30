package ec.gob.gim.cementery.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class MunicipalBondByCementeryDTO {
	
	@NativeQueryResultColumn(index = 0)
	private int number;
	
	@NativeQueryResultColumn(index = 1)
	private String subscriberIdentification;
	
	@NativeQueryResultColumn(index = 2)
	private String subscriberName;
	
	@NativeQueryResultColumn(index = 3)
	private String description;
	
	@NativeQueryResultColumn(index = 4)
	private String entry;
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal value;
	
	@NativeQueryResultColumn(index = 6)
	private String status;
	
	@NativeQueryResultColumn(index = 7)
	private Date liquidationDate;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getSubscriberIdentification() {
		return subscriberIdentification;
	}

	public void setSubscriberIdentification(String subscriberIdentification) {
		this.subscriberIdentification = subscriberIdentification;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getLiquidationDate() {
		return liquidationDate;
	}

	public void setLiquidationDate(Date liquidationDate) {
		this.liquidationDate = liquidationDate;
	}
	
}
