/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ec.gob.gim.income.model.TaxItem;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author René
 *
 */
public class BondDTO {

	private Long id;

	private Date creationDate;

	private Date creationTime;

	private Date liquidationDate;

	private Date liquidationTime;

	private Date reversedDate;

	private Date reversedTime;

	private Date emisionDate;

	private Date emisionTime;

	private Date expirationDate;

	private Date serviceDate;

	private Long number;

	private String description;

	private String reference;

	private Date emisionPeriod;

	private String groupingCode;

	private String address;

	private String bondAddress;

	private Boolean exempt;

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

	private BigDecimal totalCancelled;

	private BigDecimal taxableTotal;

	private BigDecimal nonTaxableTotal;

	private String municipalBondStatus;

	private List<ItemDTO> items = new ArrayList<ItemDTO>();

	private List<ItemDTO> discountItems = new ArrayList<ItemDTO>();

	private List<ItemDTO> surchargeItems = new ArrayList<ItemDTO>();

	private Long entryId;

	private String entryName;

	private List<TaxItemDTO> taxItems = new ArrayList<TaxItemDTO>();

	private ResidentDTO resident;

	private String timePeriod;

	private String emitter;

	private Adjunct adjunct;

	public BondDTO(MunicipalBond municipalBond) {
		this.address = municipalBond.getAddress();
		this.adjunct = municipalBond.getAdjunct();
		this.applyInterest = municipalBond.getApplyInterest();
		this.balance = municipalBond.getBalance();
		this.base = municipalBond.getBase();
		this.bondAddress = municipalBond.getBondAddress();
		this.creationDate = municipalBond.getCreationDate();
		this.creationTime = municipalBond.getCreationTime();
		this.description = municipalBond.getDescription();
		this.discount = municipalBond.getDiscount();
		// this.discountItems = municipalBond.getDiscountItems();
		for (int i = 0; i < municipalBond.getDiscountItems().size(); i++) {
			Item item = municipalBond.getDiscountItems().get(i);
			this.discountItems.add(new ItemDTO(item));
		}
		this.emisionDate = municipalBond.getEmisionDate();
		this.emisionPeriod = municipalBond.getEmisionPeriod();
		this.emisionTime = municipalBond.getEmisionTime();
		this.emitter = municipalBond.getEmitter().getName();
		this.entryId = municipalBond.getEntry().getId();
		this.entryName = municipalBond.getEntry().getName();
		this.exempt = municipalBond.getExempt();
		this.expirationDate = municipalBond.getExpirationDate();
		this.groupingCode = municipalBond.getGroupingCode();
		this.id = municipalBond.getId();
		this.interest = municipalBond.getInterest();
		// this.items = municipalBond.getItems();
		for (int i = 0; i < municipalBond.getItems().size(); i++) {
			Item item = municipalBond.getItems().get(i);
			this.items.add(new ItemDTO(item));
		}
		this.liquidationDate = municipalBond.getLiquidationDate();
		this.liquidationTime = municipalBond.getLiquidationTime();
		this.municipalBondStatus = municipalBond.getMunicipalBondStatus()
				.getName();
		this.nonTaxableTotal = municipalBond.getNonTaxableTotal();
		this.number = municipalBond.getNumber();
		this.paidTotal = municipalBond.getPaidTotal();
		this.previousPayment = municipalBond.getPreviousPayment();
		this.reference = municipalBond.getReference();
		this.resident = new ResidentDTO(municipalBond.getResident());
		this.reversedDate = municipalBond.getReversedDate();
		this.reversedResolution = municipalBond.getReversedResolution();
		this.reversedTime = municipalBond.getReversedTime();
		this.serviceDate = municipalBond.getServiceDate();
		this.surcharge = municipalBond.getSurcharge();
		for (int i = 0; i < municipalBond.getSurchargeItems().size(); i++) {
			Item item = municipalBond.getSurchargeItems().get(i);
			this.surchargeItems.add(new ItemDTO(item));
		}
		this.taxableTotal = municipalBond.getTaxableTotal();
		this.taxesTotal = municipalBond.getTaxesTotal();
		// this.taxItems = municipalBond.getTaxItems();
		for (int i = 0; i < municipalBond.getTaxItems().size(); i++) {
			TaxItem item = municipalBond.getTaxItems().get(i);
			this.taxItems.add(new TaxItemDTO(item));
		}

		this.timePeriod = municipalBond.getTimePeriod().getName();
		this.totalCancelled = municipalBond.getTotalCancelled();
		this.value = municipalBond.getValue();

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

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

	public Date getReversedDate() {
		return reversedDate;
	}

	public void setReversedDate(Date reversedDate) {
		this.reversedDate = reversedDate;
	}

	public Date getReversedTime() {
		return reversedTime;
	}

	public void setReversedTime(Date reversedTime) {
		this.reversedTime = reversedTime;
	}

	public Date getEmisionDate() {
		return emisionDate;
	}

	public void setEmisionDate(Date emisionDate) {
		this.emisionDate = emisionDate;
	}

	public Date getEmisionTime() {
		return emisionTime;
	}

	public void setEmisionTime(Date emisionTime) {
		this.emisionTime = emisionTime;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
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

	public Date getEmisionPeriod() {
		return emisionPeriod;
	}

	public void setEmisionPeriod(Date emisionPeriod) {
		this.emisionPeriod = emisionPeriod;
	}

	public String getGroupingCode() {
		return groupingCode;
	}

	public void setGroupingCode(String groupingCode) {
		this.groupingCode = groupingCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBondAddress() {
		return bondAddress;
	}

	public void setBondAddress(String bondAddress) {
		this.bondAddress = bondAddress;
	}

	public Boolean getExempt() {
		return exempt;
	}

	public void setExempt(Boolean exempt) {
		this.exempt = exempt;
	}

	public Boolean getApplyInterest() {
		return applyInterest;
	}

	public void setApplyInterest(Boolean applyInterest) {
		this.applyInterest = applyInterest;
	}

	public String getReversedResolution() {
		return reversedResolution;
	}

	public void setReversedResolution(String reversedResolution) {
		this.reversedResolution = reversedResolution;
	}

	public BigDecimal getBase() {
		return base;
	}

	public void setBase(BigDecimal base) {
		this.base = base;
	}

	public BigDecimal getPreviousPayment() {
		return previousPayment;
	}

	public void setPreviousPayment(BigDecimal previousPayment) {
		this.previousPayment = previousPayment;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(BigDecimal surcharge) {
		this.surcharge = surcharge;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getTaxesTotal() {
		return taxesTotal;
	}

	public void setTaxesTotal(BigDecimal taxesTotal) {
		this.taxesTotal = taxesTotal;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public BigDecimal getTotalCancelled() {
		return totalCancelled;
	}

	public void setTotalCancelled(BigDecimal totalCancelled) {
		this.totalCancelled = totalCancelled;
	}

	public BigDecimal getTaxableTotal() {
		return taxableTotal;
	}

	public void setTaxableTotal(BigDecimal taxableTotal) {
		this.taxableTotal = taxableTotal;
	}

	public BigDecimal getNonTaxableTotal() {
		return nonTaxableTotal;
	}

	public void setNonTaxableTotal(BigDecimal nonTaxableTotal) {
		this.nonTaxableTotal = nonTaxableTotal;
	}

	public String getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public void setMunicipalBondStatus(String municipalBondStatus) {
		this.municipalBondStatus = municipalBondStatus;
	}

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	/**
	 * @return the resident
	 */
	public ResidentDTO getResident() {
		return resident;
	}

	/**
	 * @param resident
	 *            the resident to set
	 */
	public void setResident(ResidentDTO resident) {
		this.resident = resident;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public String getEmitter() {
		return emitter;
	}

	public void setEmitter(String emitter) {
		this.emitter = emitter;
	}

	public Adjunct getAdjunct() {
		return adjunct;
	}

	public void setAdjunct(Adjunct adjunct) {
		this.adjunct = adjunct;
	}

	/**
	 * @return the items
	 */
	public List<ItemDTO> getItems() {
		return items;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(List<ItemDTO> items) {
		this.items = items;
	}

	/**
	 * @return the discountItems
	 */
	public List<ItemDTO> getDiscountItems() {
		return discountItems;
	}

	/**
	 * @param discountItems
	 *            the discountItems to set
	 */
	public void setDiscountItems(List<ItemDTO> discountItems) {
		this.discountItems = discountItems;
	}

	/**
	 * @return the surchargeItems
	 */
	public List<ItemDTO> getSurchargeItems() {
		return surchargeItems;
	}

	/**
	 * @param surchargeItems
	 *            the surchargeItems to set
	 */
	public void setSurchargeItems(List<ItemDTO> surchargeItems) {
		this.surchargeItems = surchargeItems;
	}

	/**
	 * @return the taxItems
	 */
	public List<TaxItemDTO> getTaxItems() {
		return taxItems;
	}

	/**
	 * @param taxItems
	 *            the taxItems to set
	 */
	public void setTaxItems(List<TaxItemDTO> taxItems) {
		this.taxItems = taxItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BondDTO [id=" + id + ", creationDate=" + creationDate
				+ ", creationTime=" + creationTime + ", liquidationDate="
				+ liquidationDate + ", liquidationTime=" + liquidationTime
				+ ", reversedDate=" + reversedDate + ", reversedTime="
				+ reversedTime + ", emisionDate=" + emisionDate
				+ ", emisionTime=" + emisionTime + ", expirationDate="
				+ expirationDate + ", serviceDate=" + serviceDate + ", number="
				+ number + ", description=" + description + ", reference="
				+ reference + ", emisionPeriod=" + emisionPeriod
				+ ", groupingCode=" + groupingCode + ", address=" + address
				+ ", bondAddress=" + bondAddress + ", exempt=" + exempt
				+ ", applyInterest=" + applyInterest + ", reversedResolution="
				+ reversedResolution + ", base=" + base + ", previousPayment="
				+ previousPayment + ", value=" + value + ", interest="
				+ interest + ", discount=" + discount + ", surcharge="
				+ surcharge + ", balance=" + balance + ", taxesTotal="
				+ taxesTotal + ", paidTotal=" + paidTotal + ", totalCancelled="
				+ totalCancelled + ", taxableTotal=" + taxableTotal
				+ ", nonTaxableTotal=" + nonTaxableTotal
				+ ", municipalBondStatus=" + municipalBondStatus + ", items="
				+ items + ", discountItems=" + discountItems
				+ ", surchargeItems=" + surchargeItems + ", entryId=" + entryId
				+ ", entryName=" + entryName + ", taxItems=" + taxItems
				+ ", resident=" + resident + ", timePeriod=" + timePeriod
				+ ", emitter=" + emitter + ", adjunct=" + adjunct + "]";
	}

}
