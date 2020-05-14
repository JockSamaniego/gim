/**
 * 
 */
package org.gob.loja.gim.ws.dto.ant;

import java.math.BigDecimal;

/**
 * @author Rene
 *
 */
public class BondDTO {

	private Long id;

	private String creationDate;

	private String creationTime;

	private String liquidationDate;

	private String liquidationTime;

	private String reversedDate;

	private String reversedTime;

	private String emisionDate;

	private String emisionTime;

	private String expirationDate;

	private String serviceDate;

	private Long number;

	private String description;

	private Boolean isExpirationDateDefined;

	private String reference;

	private String emisionPeriod;

	private Integer printingsNumber;

	private String groupingCode;

	private String address;

	private String bondAddress;

	private Boolean exempt;

	private Boolean internalTramit;

	private Boolean isNoPasiveSubject;

	private Boolean applyInterest;

	private String reversedResolution;

	private BigDecimal base;

	private BigDecimal previousPayment;

	private BigDecimal value;

	private BigDecimal interest;

	private BigDecimal discount;

	private BigDecimal surcharge;

	private BigDecimal balance;

	private BigDecimal taxesTotal;

	private BigDecimal paidTotal;

	private BigDecimal taxableTotal;

	private BigDecimal nonTaxableTotal;

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
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the creationTime
	 */
	public String getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the liquidationDate
	 */
	public String getLiquidationDate() {
		return liquidationDate;
	}

	/**
	 * @param liquidationDate the liquidationDate to set
	 */
	public void setLiquidationDate(String liquidationDate) {
		this.liquidationDate = liquidationDate;
	}

	/**
	 * @return the liquidationTime
	 */
	public String getLiquidationTime() {
		return liquidationTime;
	}

	/**
	 * @param liquidationTime the liquidationTime to set
	 */
	public void setLiquidationTime(String liquidationTime) {
		this.liquidationTime = liquidationTime;
	}

	/**
	 * @return the reversedDate
	 */
	public String getReversedDate() {
		return reversedDate;
	}

	/**
	 * @param reversedDate the reversedDate to set
	 */
	public void setReversedDate(String reversedDate) {
		this.reversedDate = reversedDate;
	}

	/**
	 * @return the reversedTime
	 */
	public String getReversedTime() {
		return reversedTime;
	}

	/**
	 * @param reversedTime the reversedTime to set
	 */
	public void setReversedTime(String reversedTime) {
		this.reversedTime = reversedTime;
	}

	/**
	 * @return the emisionDate
	 */
	public String getEmisionDate() {
		return emisionDate;
	}

	/**
	 * @param emisionDate the emisionDate to set
	 */
	public void setEmisionDate(String emisionDate) {
		this.emisionDate = emisionDate;
	}

	/**
	 * @return the emisionTime
	 */
	public String getEmisionTime() {
		return emisionTime;
	}

	/**
	 * @param emisionTime the emisionTime to set
	 */
	public void setEmisionTime(String emisionTime) {
		this.emisionTime = emisionTime;
	}

	/**
	 * @return the expirationDate
	 */
	public String getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the serviceDate
	 */
	public String getServiceDate() {
		return serviceDate;
	}

	/**
	 * @param serviceDate the serviceDate to set
	 */
	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	/**
	 * @return the number
	 */
	public Long getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isExpirationDateDefined
	 */
	public Boolean getIsExpirationDateDefined() {
		return isExpirationDateDefined;
	}

	/**
	 * @param isExpirationDateDefined the isExpirationDateDefined to set
	 */
	public void setIsExpirationDateDefined(Boolean isExpirationDateDefined) {
		this.isExpirationDateDefined = isExpirationDateDefined;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the emisionPeriod
	 */
	public String getEmisionPeriod() {
		return emisionPeriod;
	}

	/**
	 * @param emisionPeriod the emisionPeriod to set
	 */
	public void setEmisionPeriod(String emisionPeriod) {
		this.emisionPeriod = emisionPeriod;
	}

	/**
	 * @return the printingsNumber
	 */
	public Integer getPrintingsNumber() {
		return printingsNumber;
	}

	/**
	 * @param printingsNumber the printingsNumber to set
	 */
	public void setPrintingsNumber(Integer printingsNumber) {
		this.printingsNumber = printingsNumber;
	}

	/**
	 * @return the groupingCode
	 */
	public String getGroupingCode() {
		return groupingCode;
	}

	/**
	 * @param groupingCode the groupingCode to set
	 */
	public void setGroupingCode(String groupingCode) {
		this.groupingCode = groupingCode;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the bondAddress
	 */
	public String getBondAddress() {
		return bondAddress;
	}

	/**
	 * @param bondAddress the bondAddress to set
	 */
	public void setBondAddress(String bondAddress) {
		this.bondAddress = bondAddress;
	}

	/**
	 * @return the exempt
	 */
	public Boolean getExempt() {
		return exempt;
	}

	/**
	 * @param exempt the exempt to set
	 */
	public void setExempt(Boolean exempt) {
		this.exempt = exempt;
	}

	/**
	 * @return the internalTramit
	 */
	public Boolean getInternalTramit() {
		return internalTramit;
	}

	/**
	 * @param internalTramit the internalTramit to set
	 */
	public void setInternalTramit(Boolean internalTramit) {
		this.internalTramit = internalTramit;
	}

	/**
	 * @return the isNoPasiveSubject
	 */
	public Boolean getIsNoPasiveSubject() {
		return isNoPasiveSubject;
	}

	/**
	 * @param isNoPasiveSubject the isNoPasiveSubject to set
	 */
	public void setIsNoPasiveSubject(Boolean isNoPasiveSubject) {
		this.isNoPasiveSubject = isNoPasiveSubject;
	}

	/**
	 * @return the applyInterest
	 */
	public Boolean getApplyInterest() {
		return applyInterest;
	}

	/**
	 * @param applyInterest the applyInterest to set
	 */
	public void setApplyInterest(Boolean applyInterest) {
		this.applyInterest = applyInterest;
	}

	/**
	 * @return the reversedResolution
	 */
	public String getReversedResolution() {
		return reversedResolution;
	}

	/**
	 * @param reversedResolution the reversedResolution to set
	 */
	public void setReversedResolution(String reversedResolution) {
		this.reversedResolution = reversedResolution;
	}

	/**
	 * @return the base
	 */
	public BigDecimal getBase() {
		return base;
	}

	/**
	 * @param base the base to set
	 */
	public void setBase(BigDecimal base) {
		this.base = base;
	}

	/**
	 * @return the previousPayment
	 */
	public BigDecimal getPreviousPayment() {
		return previousPayment;
	}

	/**
	 * @param previousPayment the previousPayment to set
	 */
	public void setPreviousPayment(BigDecimal previousPayment) {
		this.previousPayment = previousPayment;
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
	 * @return the taxesTotal
	 */
	public BigDecimal getTaxesTotal() {
		return taxesTotal;
	}

	/**
	 * @param taxesTotal the taxesTotal to set
	 */
	public void setTaxesTotal(BigDecimal taxesTotal) {
		this.taxesTotal = taxesTotal;
	}

	/**
	 * @return the paidTotal
	 */
	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	/**
	 * @param paidTotal the paidTotal to set
	 */
	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	/**
	 * @return the taxableTotal
	 */
	public BigDecimal getTaxableTotal() {
		return taxableTotal;
	}

	/**
	 * @param taxableTotal the taxableTotal to set
	 */
	public void setTaxableTotal(BigDecimal taxableTotal) {
		this.taxableTotal = taxableTotal;
	}

	/**
	 * @return the nonTaxableTotal
	 */
	public BigDecimal getNonTaxableTotal() {
		return nonTaxableTotal;
	}

	/**
	 * @param nonTaxableTotal the nonTaxableTotal to set
	 */
	public void setNonTaxableTotal(BigDecimal nonTaxableTotal) {
		this.nonTaxableTotal = nonTaxableTotal;
	}

}
