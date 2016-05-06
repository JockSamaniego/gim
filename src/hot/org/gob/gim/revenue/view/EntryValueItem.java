package org.gob.gim.revenue.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class EntryValueItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private BigDecimal mainValue;
	private Date serviceDate;
	private BigDecimal amount;
	private BigDecimal previousPayment = BigDecimal.ZERO;
	private Date expirationDate;
	private Boolean internalTramit;
	private String description;
	private String reference;
	
	private Boolean resetValue;
	
	private Boolean isExpirationDateEditable;
	
	public Boolean getIsExpirationDateEditable() {
		return isExpirationDateEditable;
	}

	public void setIsExpirationDateEditable(Boolean isExpirationDateEditable) {
		this.isExpirationDateEditable = isExpirationDateEditable;
	}

	private boolean isExempt;
	
	public boolean isExempt() {
		return isExempt;
	}

	public void setExempt(boolean isExempt) {
		this.isExempt = isExempt;
	}

	public boolean isNoPasiveSubject() {
		return isNoPasiveSubject;
	}

	public void setNoPasiveSubject(boolean isNoPasiveSubject) {
		this.isNoPasiveSubject = isNoPasiveSubject;
	}

	private boolean isNoPasiveSubject;
	
	public EntryValueItem() {		
		serviceDate = Calendar.getInstance().getTime();
		amount = BigDecimal.ONE;
		internalTramit = Boolean.FALSE;
		resetValue = Boolean.TRUE;
	}
	
	public EntryValueItem(Boolean fullDate) {
		if (!fullDate){
			Calendar date = Calendar.getInstance();
			date.set(Calendar.DATE, 1);
			date.set(Calendar.HOUR_OF_DAY, 0);
			date.set(Calendar.MINUTE, 0);
			date.set(Calendar.SECOND, 0);
			this.serviceDate=date.getTime();
		}else{
			serviceDate = Calendar.getInstance().getTime();
		}
		setResetValue(Boolean.TRUE);
	}

	public BigDecimal getMainValue() {
		return mainValue;
	}

	public void setMainValue(BigDecimal mainValue) {
		this.mainValue = mainValue;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getYear(){
		Calendar date = Calendar.getInstance();
		date.setTime(getServiceDate());
		return date.get(Calendar.YEAR);
	}
	
	public void setYear(Integer year){
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, year);
		date.set(Calendar.MONTH, Calendar.JANUARY);
		date.set(Calendar.DATE, 1);
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		this.serviceDate=date.getTime();
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public Boolean getInternalTramit() {
		return internalTramit;
	}

	public void setInternalTramit(Boolean internalTramit) {
		this.internalTramit = internalTramit;
	}

	public BigDecimal getPreviousPayment() {
		return previousPayment;
	}

	public void setPreviousPayment(BigDecimal previousPayment) {
		this.previousPayment = previousPayment;
	}

	public Boolean getResetValue() {
		return resetValue;
	}

	public void setResetValue(Boolean resetValue) {
		this.resetValue = resetValue;
	}

}
