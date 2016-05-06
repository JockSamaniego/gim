package org.gob.gim.accounting.dto;

import java.math.BigDecimal;
import java.util.Date;

public class AccountDetail implements Comparable<AccountDetail>{
	private Long bondNumber;
	private String entryName;
	private String identificationNumber;
	private String residentName;
	private Date expirationDate;
	private BigDecimal debit;
	private BigDecimal credit;

	@Override
	public int compareTo(AccountDetail o) {
		if(o != null){
			return this.residentName.compareToIgnoreCase(o.residentName);
		}
		return 0;
	}


	public Long getBondNumber() {
		return bondNumber;
	}


	public void setBondNumber(Long bondNumber) {
		this.bondNumber = bondNumber;
	}


	public String getEntryName() {
		return entryName;
	}


	public void setEntryName(String entryName) {
		this.entryName = entryName;
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


	public BigDecimal getDebit() {
		return debit;
	}


	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}


	public BigDecimal getCredit() {
		return credit;
	}


	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	
	public Date getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}
