package org.gob.gim.revenue.view;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.TimePeriod;

public class DeferredMunicipalBondItem {
	
	private Entry entry;
	private TimePeriod timePeriod;
	
	private BigDecimal amount;
	private BigDecimal value;	
	private Integer fee;
	private Date startDate;
	private Date endDate;
	private String description;	
	private String bondAddress;	
	private String reference;
	
	/**
	 * 
	 */
	public DeferredMunicipalBondItem() {
		// TODO Auto-generated constructor stub
		amount = new BigDecimal(0);
	}


	/**
	 * @param item
	 * @param fee
	 * @param startDate
	 * @param endDate
	 */
	public DeferredMunicipalBondItem(Entry entry, Integer fee, 
			Date startDate,	Date endDate) {
		this();
		this.entry = entry;
		this.fee = fee;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public DeferredMunicipalBondItem(Entry entry,BigDecimal amount, Integer fee, Date startDate, Date endDate) {
		this.entry = entry;
		this.amount = amount;
		this.fee = fee;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = entry.getDescription();
	}


	/**
	 * @return the entry
	 */
	public Entry getEntry() {
		return entry;
	}


	/**
	 * @param entry the entry to set
	 */
	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the fee
	 */
	public Integer getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(Integer fee) {
		this.fee = fee;
	}


	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}


	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}	
	
	/**
	 * @return the timePeriod
	 */
	public TimePeriod getTimePeriod() {
		return timePeriod;
	}


	/**
	 * @param timePeriod the timePeriod to set
	 */
	public void setTimePeriod(TimePeriod timePeriod) {
		this.timePeriod = timePeriod;
	}
	
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getReference() {
		return reference;
	}


	public void setReference(String reference) {
		this.reference = reference;
	}

	public BigDecimal getValue() {
		return value;
	}


	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public void calculateEndDate(){
		Calendar cstartDate = Calendar.getInstance();
		cstartDate.setTime(startDate);
		if (timePeriod != null)
			cstartDate.add(Calendar.DATE, timePeriod.getDaysNumber()*fee);
		endDate = cstartDate.getTime(); 
	}
	
	public Date calculateNextDate(Date startDate){
		Calendar cstartDate = Calendar.getInstance();
		cstartDate.setTime(startDate);
		if (timePeriod != null)
			cstartDate.add(Calendar.DATE, timePeriod.getDaysNumber());
		return cstartDate.getTime(); 
	}	
	
	public String getBondAddress() {
		return bondAddress;
	}


	public void setBondAddress(String bondAddress) {
		this.bondAddress = bondAddress;
	}
	
}