package ec.gob.gim.cementery.model.dto;


import java.util.Date;
import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class UnitContractExpiredDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Long unitId;
	
	@NativeQueryResultColumn(index = 1)
	private Date subscriptionDate;
	
	@NativeQueryResultColumn(index = 2)
	private Date expirationDate;
	
	@NativeQueryResultColumn(index = 3)
	private String cementery;
	
	@NativeQueryResultColumn(index = 4)
	private String type;
	
	@NativeQueryResultColumn(index = 5)
	private String deseased;
	
	@NativeQueryResultColumn(index = 6)
	private String subscriberName;
	
	@NativeQueryResultColumn(index = 7)
	private String subscriberIdentification;
	
	@NativeQueryResultColumn(index = 8)
	private String subscriberEmail;
	
	@NativeQueryResultColumn(index = 9)
	private Long contractId;
	
	@NativeQueryResultColumn(index = 10)
	private Date lastNotification;

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public Date getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getCementery() {
		return cementery;
	}

	public void setCementery(String cementery) {
		this.cementery = cementery;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeseased() {
		return deseased;
	}

	public void setDeseased(String deseased) {
		this.deseased = deseased;
	}

	public String getSubscriberName() {
		return subscriberName;
	}

	public void setSubscriberName(String subscriberName) {
		this.subscriberName = subscriberName;
	}

	public String getSubscriberIdentification() {
		return subscriberIdentification;
	}

	public void setSubscriberIdentification(String subscriberIdentification) {
		this.subscriberIdentification = subscriberIdentification;
	}

	public String getSubscriberEmail() {
		return subscriberEmail;
	}

	public void setSubscriberEmail(String subscriberEmail) {
		this.subscriberEmail = subscriberEmail;
	}

	public Long getContractId() {
		return contractId;
	}

	public void setContractId(Long contractId) {
		this.contractId = contractId;
	}

	public Date getLastNotification() {
		return lastNotification;
	}

	public void setLastNotification(Date lastNotification) {
		this.lastNotification = lastNotification;
	}
	
}
