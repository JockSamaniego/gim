package ec.gob.gim.income.model;

import java.math.BigDecimal;

import ec.gob.gim.income.model.dto.ParameterFutureEmissionDTO;

public class EntryTotalCollected {
	
	private String  groupBy;
	
	private String  entry;
	
	private String  account;
	
	private String parametersFutureEmission;
	
	private Long  id;
	
	private Long  municipalBondsNumber;

	private BigDecimal value;
	
	private BigDecimal previousYears;
	
	private BigDecimal nextYears;
	
	private BigDecimal futureYears;
	
	private BigDecimal discount;
	
	private BigDecimal totalEmitted;
	
	private BigDecimal surcharge;
	
	private BigDecimal interest;
	
	private BigDecimal taxes;
	
	private BigDecimal total;
	
	private Boolean isDiscount;
	
	private ParameterFutureEmissionDTO parametersFutureEmissionDTO;
		
	public EntryTotalCollected(){
		this.isDiscount = Boolean.FALSE;
		total = BigDecimal.ZERO;
	}
	  	  
  	public EntryTotalCollected(Long id,Long municipalBondsNumber, String entry, String groupBy, BigDecimal value,BigDecimal discount, BigDecimal surcharge, 
  			BigDecimal interest, BigDecimal taxes,BigDecimal total){
		this.groupBy = groupBy;
		this.account = groupBy;
		this.entry = entry;		
		this.id = id;
		this.municipalBondsNumber=municipalBondsNumber;
		this.value = value;		
		this.discount = discount;				
		this.surcharge = surcharge;		
		this.interest = interest;		
		this.taxes = taxes;		
		this.total = total != null ? total : BigDecimal.ZERO;		
		this.isDiscount = Boolean.FALSE;
		this.parametersFutureEmission = null;
	}
  	
	public EntryTotalCollected(String groupBy, BigDecimal value){	
		this.value = value;
		this.total = value;	
		this.groupBy = groupBy;
		this.isDiscount = Boolean.FALSE;
		this.parametersFutureEmission = null;
	}
  	
	public EntryTotalCollected(BigDecimal value){	
		this.value = value;
		this.total = value;	
		this.discount = BigDecimal.ZERO;
		this.surcharge = BigDecimal.ZERO;
		this.taxes = BigDecimal.ZERO;
		this.interest = BigDecimal.ZERO;
		this.isDiscount = Boolean.FALSE;
		this.parametersFutureEmission = null;
	}
  	
	
	public EntryTotalCollected(Long id, Long count, String entry, String groupBy,BigDecimal value,BigDecimal interest,BigDecimal paidTaxes){		
		this.id = id;
		this.value = value;
		this.total = value;
		this.isDiscount = Boolean.FALSE;		
		this.entry = entry;
		this.groupBy = groupBy;
		this.account = groupBy;
		this.interest = interest;
		this.taxes = paidTaxes;
		this.parametersFutureEmission = null;
	}
	
  	public EntryTotalCollected(Long id, String entry, String groupBy,BigDecimal value,BigDecimal interest,BigDecimal paidTaxes){		
		this.id = id;
		this.value = value;
		this.total = value;
		this.isDiscount = Boolean.FALSE;		
		this.entry = entry;
		this.groupBy = groupBy;
		this.account = groupBy;
		this.interest = interest;
		this.taxes = paidTaxes;
		this.parametersFutureEmission = null;
	}
  	  	
  	public EntryTotalCollected(Long id, BigDecimal value){		
		this.id = id;
		this.value = value;		
		this.total = value;
		this.discount = BigDecimal.ZERO;
		this.surcharge = BigDecimal.ZERO;
		this.taxes = BigDecimal.ZERO;
		this.interest = BigDecimal.ZERO;
		this.isDiscount = Boolean.FALSE;
		this.parametersFutureEmission = null;
	}
  	
  	
  	public EntryTotalCollected(Long id, String entry, String groupBy,BigDecimal value){		
		this.id = id;
		this.value = value;
		this.total = value;
		this.isDiscount = Boolean.FALSE;
		this.entry = entry;
		this.discount = BigDecimal.ZERO;
		this.surcharge = BigDecimal.ZERO;
		this.interest = BigDecimal.ZERO;
		this.taxes = BigDecimal.ZERO;
		this.groupBy = groupBy;
		this.account = groupBy;
		this.parametersFutureEmission = null;
	}
  	
  	public EntryTotalCollected(Long id, String entry, String groupBy,BigDecimal value, String parametersFutureEmission){		
		this.id = id;
		this.value = value;
		this.total = value;
		this.isDiscount = Boolean.FALSE;
		this.entry = entry;
		this.discount = BigDecimal.ZERO;
		this.surcharge = BigDecimal.ZERO;
		this.interest = BigDecimal.ZERO;
		this.taxes = BigDecimal.ZERO;
		this.groupBy = groupBy;
		this.account = groupBy;
		this.parametersFutureEmission = parametersFutureEmission;
	}

	
	public EntryTotalCollected(String groupBy, String entry, String account, Long id,BigDecimal value,BigDecimal previousYears, BigDecimal nextYears,
			BigDecimal discount, BigDecimal totalEmitted, BigDecimal surcharge, BigDecimal interest, BigDecimal taxes,BigDecimal total){
		this.groupBy = groupBy;		
		this.entry = entry;		
		this.account = account;		
		this.id = id;
		this.value = value;	
		this.isDiscount = Boolean.FALSE;
		this.previousYears = previousYears;		
		this.nextYears = nextYears;		
		this.discount = discount;		
		this.totalEmitted = totalEmitted;		
		this.surcharge = surcharge;		
		this.interest = interest;		
		this.taxes = taxes;		
		this.total = total;		
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
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

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getGroupBy() {
		return groupBy;
	}
	
	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public void setTotalEmitted(BigDecimal totalEmitted) {
		this.totalEmitted = totalEmitted;
	}

	public BigDecimal getTotalEmitted() {
		return totalEmitted;
	}
	
	public BigDecimal getPreviousYears() {
		return previousYears;
	}

	public void setPreviousYears(BigDecimal previousYears) {
		this.previousYears = previousYears;
	}

	public BigDecimal getNextYears() {
		return nextYears;
	}

	public void setNextYears(BigDecimal nextYears) {
		this.nextYears = nextYears;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

	public Boolean getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(Boolean isDiscount) {
		this.isDiscount = isDiscount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMunicipalBondsNumber() {
		return municipalBondsNumber;
	}

	public void setMunicipalBondsNumber(Long municipalBondsNumber) {
		this.municipalBondsNumber = municipalBondsNumber;
	}

	public BigDecimal getFutureYears() {
		return futureYears;
	}

	public void setFutureYears(BigDecimal futureYears) {
		this.futureYears = futureYears;
	}

	

	public String getParametersFutureEmission() {
		return parametersFutureEmission;
	}

	public void setParametersFutureEmission(String parametersFutureEmission) {
		this.parametersFutureEmission = parametersFutureEmission;
	}

	public ParameterFutureEmissionDTO getParametersFutureEmissionDTO() {
		return parametersFutureEmissionDTO;
	}

	public void setParametersFutureEmissionDTO(
			ParameterFutureEmissionDTO parametersFutureEmissionDTO) {
		this.parametersFutureEmissionDTO = parametersFutureEmissionDTO;
	}

	@Override
	public String toString() {
		return "EntryTotalCollected [groupBy=" + groupBy + ", entry=" + entry
				+ ", account=" + account + ", parametersFutureEmission="
				+ parametersFutureEmission + ", id=" + id
				+ ", municipalBondsNumber=" + municipalBondsNumber + ", value="
				+ value + ", previousYears=" + previousYears + ", nextYears="
				+ nextYears + ", futureYears=" + futureYears + ", discount="
				+ discount + ", totalEmitted=" + totalEmitted + ", surcharge="
				+ surcharge + ", interest=" + interest + ", taxes=" + taxes
				+ ", total=" + total + ", isDiscount=" + isDiscount
				+ ", parametersFutureEmissionDTO="
				+ parametersFutureEmissionDTO + "]";
	}
	
}