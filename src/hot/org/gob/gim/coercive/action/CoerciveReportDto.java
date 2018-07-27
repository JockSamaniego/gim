package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class CoerciveReportDto {

	@NativeQueryResultColumn(index = 0)
	private Long paymentAgreementID;
	
	@NativeQueryResultColumn(index = 1)
	private Date expiration;
	
	@NativeQueryResultColumn(index = 2)
	private String identification;
	
	@NativeQueryResultColumn(index = 3)
	private String names; 
	
	@NativeQueryResultColumn(index = 4)
	private BigDecimal payed;
	
	@NativeQueryResultColumn(index = 5)
	private String description;
	
	 
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public String getIdentification() {
		return identification;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	 
	public BigDecimal getPayed() {
		return payed;
	}
	
	public void setPayed(BigDecimal payed) {
		this.payed = payed;
	}
	 
	public Long getPaymentAgreementID() {
		return paymentAgreementID;
	}
	
	public void setPaymentAgreementID(Long paymentAgreementID) {
		this.paymentAgreementID = paymentAgreementID;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
