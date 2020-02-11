package ec.gob.gim.coercive.model;

import java.util.Date;
import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class NotificationPayedDetailDTO {

	@NativeQueryResultColumn(index = 0)
	private int obligationNumber;
	
	@NativeQueryResultColumn(index = 1)
	private String identificationNumber;
	
	@NativeQueryResultColumn(index = 2)
	private String residentName;
	
	@NativeQueryResultColumn(index = 3)
	private Date emisionDate;
	
	@NativeQueryResultColumn(index = 4)
	private Date expirationDate;
	
	@NativeQueryResultColumn(index = 5)
	private Date notificationDate;
	
	@NativeQueryResultColumn(index = 6)
	private Date liquidationDate;
	
	@NativeQueryResultColumn(index = 7)
	private BigDecimal value;

	@NativeQueryResultColumn(index = 8)
	private String notificationNumber;
	
	
	public int getObligationNumber() {
		return obligationNumber;
	}

	public void setObligationNumber(int obligationNumber) {
		this.obligationNumber = obligationNumber;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public Date getEmisionDate() {
		return emisionDate;
	}

	public void setEmisionDate(Date emisionDate) {
		this.emisionDate = emisionDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}

	public Date getLiquidationDate() {
		return liquidationDate;
	}

	public void setLiquidationDate(Date liquidationDate) {
		this.liquidationDate = liquidationDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public String getNotificationNumber() {
		return notificationNumber;
	}

	public void setNotificationNumber(String notificationNumber) {
		this.notificationNumber = notificationNumber;
	}
	
}
