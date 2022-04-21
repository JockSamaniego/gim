/**
 * 
 */
package org.gob.loja.gim.ws.dto.digitalReceipts;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author René
 *
 */
public class DepositDTO {
	
	@Id
	@GeneratedValue(generator="DepositGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private Integer number;
	
	private java.util.Date date;
	
	private java.util.Date time;
	
	private java.util.Date reversedDate;
	
	private java.util.Date reversedTime;
	
	private BigDecimal capital;
	
	private BigDecimal interest;
	
	private BigDecimal surcharge;
	
	private BigDecimal discount;
	
	private BigDecimal paidTaxes;
	
	private BigDecimal balance;
	
	private BigDecimal value;
	
	private Boolean isPrinted;
	
	private String concept;
	
	private String status;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the number
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * @return the date
	 */
	public java.util.Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(java.util.Date date) {
		this.date = date;
	}

	/**
	 * @return the time
	 */
	public java.util.Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(java.util.Date time) {
		this.time = time;
	}

	/**
	 * @return the reversedDate
	 */
	public java.util.Date getReversedDate() {
		return reversedDate;
	}

	/**
	 * @param reversedDate the reversedDate to set
	 */
	public void setReversedDate(java.util.Date reversedDate) {
		this.reversedDate = reversedDate;
	}

	/**
	 * @return the reversedTime
	 */
	public java.util.Date getReversedTime() {
		return reversedTime;
	}

	/**
	 * @param reversedTime the reversedTime to set
	 */
	public void setReversedTime(java.util.Date reversedTime) {
		this.reversedTime = reversedTime;
	}

	/**
	 * @return the capital
	 */
	public BigDecimal getCapital() {
		return capital;
	}

	/**
	 * @param capital the capital to set
	 */
	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	/**
	 * @return the interest
	 */
	public BigDecimal getInterest() {
		return interest;
	}

	/**
	 * @param interest the interest to set
	 */
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	/**
	 * @return the surcharge
	 */
	public BigDecimal getSurcharge() {
		return surcharge;
	}

	/**
	 * @param surcharge the surcharge to set
	 */
	public void setSurcharge(BigDecimal surcharge) {
		this.surcharge = surcharge;
	}

	/**
	 * @return the discount
	 */
	public BigDecimal getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	/**
	 * @return the paidTaxes
	 */
	public BigDecimal getPaidTaxes() {
		return paidTaxes;
	}

	/**
	 * @param paidTaxes the paidTaxes to set
	 */
	public void setPaidTaxes(BigDecimal paidTaxes) {
		this.paidTaxes = paidTaxes;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the isPrinted
	 */
	public Boolean getIsPrinted() {
		return isPrinted;
	}

	/**
	 * @param isPrinted the isPrinted to set
	 */
	public void setIsPrinted(Boolean isPrinted) {
		this.isPrinted = isPrinted;
	}

	/**
	 * @return the concept
	 */
	public String getConcept() {
		return concept;
	}

	/**
	 * @param concept the concept to set
	 */
	public void setConcept(String concept) {
		this.concept = concept;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DepositDTO [id=" + id + ", number=" + number + ", date=" + date
				+ ", time=" + time + ", reversedDate=" + reversedDate
				+ ", reversedTime=" + reversedTime + ", capital=" + capital
				+ ", interest=" + interest + ", surcharge=" + surcharge
				+ ", discount=" + discount + ", paidTaxes=" + paidTaxes
				+ ", balance=" + balance + ", value=" + value + ", isPrinted="
				+ isPrinted + ", concept=" + concept + ", status=" + status
				+ "]";
	}
	
}
