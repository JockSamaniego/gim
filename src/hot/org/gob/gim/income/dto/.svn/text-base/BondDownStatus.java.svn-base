package org.gob.gim.income.dto;

import java.math.BigDecimal;
import java.util.Date;

public class BondDownStatus implements Comparable<BondDownStatus>{
	private String resolutionNumber;
	private Date resolutionDate;
	private BigDecimal resolutionTotal;
	private String account;
	private String accountName;
	private BigDecimal accountTotal;
	
	public BondDownStatus(){
		
	}
	
	public BondDownStatus( String resolutionNumber,
			BigDecimal resolutionTotal){
		this.resolutionNumber = resolutionNumber;
		this.resolutionTotal = resolutionTotal;
		this.resolutionDate = new Date();
		this.account = "";
		this.accountName = "";
		this.accountTotal = BigDecimal.ZERO;
	}

	public BondDownStatus( String resolutionNumber,
			Date resolutionDate, BigDecimal resolutionTotal){
		this.resolutionNumber = resolutionNumber;
		this.resolutionDate = resolutionDate;
		this.resolutionTotal = resolutionTotal;
		this.account = "";
		this.accountName = "";
		this.accountTotal = BigDecimal.ZERO;
	}
	
	public BondDownStatus( String account,
			String accountName, BigDecimal accountTotal){
		this.resolutionNumber = "";
		this.resolutionDate = null;
		this.resolutionTotal = BigDecimal.ZERO;
		this.account = account;
		this.accountName = accountName;
		this.accountTotal = accountTotal;
	}

	public String getResolutionNumber() {
		return resolutionNumber;
	}

	public void setResolutionNumber(String resolutionNumber) {
		this.resolutionNumber = resolutionNumber;
	}

	public Date getResolutionDate() {
		return resolutionDate;
	}

	public void setResolutionDate(Date resolutionDate) {
		this.resolutionDate = resolutionDate;
	}

	public BigDecimal getResolutionTotal() {
		return resolutionTotal;
	}

	public void setResolutionTotal(BigDecimal resolutionTotal) {
		this.resolutionTotal = resolutionTotal;
	}

	@Override
	public int compareTo(BondDownStatus o) {
		if(o != null){ 
			return this.resolutionNumber.compareToIgnoreCase(o.resolutionNumber);
		}
		return 0;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public BigDecimal getAccountTotal() {
		return accountTotal;
	}

	public void setAccountTotal(BigDecimal accountTotal) {
		this.accountTotal = accountTotal;
	}

}
