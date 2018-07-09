package org.gob.gim.accounting.dto;

import java.math.BigDecimal;

public class AccountItem implements Comparable<AccountItem>{
	private Long accountId;
	private String accountNumber;
	private String previousYearsAccountCode;
	private String budgetAccountNumber;
	private String accountName;
	private BigDecimal debit;
	private BigDecimal credit;
	private BigDecimal balance;
	private ReportFilter reportFilter;
	private ReportType reportType;
	
	public AccountItem(){
		reportFilter = ReportFilter.CURRENT;
	}
	
	public AccountItem( Long accountId, 
						String accountNumber,
						String budgetAccountumber,
						String accountName,
						BigDecimal debit,
						BigDecimal credit,
						BigDecimal balance){
		this.accountId = accountId;
		this.accountNumber = accountNumber;
		this.budgetAccountNumber = budgetAccountumber;
		this.accountName = accountName;
		this.debit = debit;
		this.credit = credit;
		this.balance = balance;
		
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getBudgetAccountNumber() {
		return budgetAccountNumber;
	}

	public void setBudgetAccountNumber(String budgetAccountNumber) {
		this.budgetAccountNumber = budgetAccountNumber;
	}
	
	public void calculateBalance(){
		setBalance(debit.subtract(credit));
	}

	@Override
	public int compareTo(AccountItem o) {
		if(o != null){ 
			return this.accountNumber.compareToIgnoreCase(o.accountNumber);
		}
		return 0;
	}

	public ReportFilter getReportFilter() {
		return reportFilter;
	}

	public void setReportFilter(ReportFilter reportFilter) {
		this.reportFilter = reportFilter;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub		
		if(reportType.equals(ReportType.COMBINED)) return toStringCombinedReport();
		if(reportType.equals(ReportType.INCOME) || reportType.equals(ReportType.QUOTAS_LIQUIDATION) || reportType.equals(ReportType.SUBSCRIPTION)) return toStringCreditReport();
		if(!reportType.equals(ReportType.INCOME) && !reportType.equals(ReportType.QUOTAS_LIQUIDATION) && !reportType.equals(ReportType.SUBSCRIPTION)) return toStringDebitReport(); 
		return super.toString();
	}
	
	private String toStringDebitReport() {
		// TODO Auto-generated method stub
		String aux= (this.accountNumber == null ? "" : this.accountNumber) + ";" + 
				(this.accountName == null ? "" : this.accountName) + ";" + 
				(this.debit == null ? "" : this.debit) + ";";
//		El caracter * reemplaza a las comas (,) para luego diferenciar en el toString de 
//		List<DuePortfolioDTO> que se colocan para separar los elementos
		aux = aux.replace(',', '*');
		return aux;		
	}
	
	private String toStringCreditReport() {
		// TODO Auto-generated method stub
		String aux= (this.accountNumber == null ? "" : this.accountNumber) + ";" + 
				(this.accountName == null ? "" : this.accountName) + ";" +
				(this.credit == null ? "" : this.credit) + ";"  ;
//		El caracter * reemplaza a las comas (,) para luego diferenciar en el toString de 
//		List<DuePortfolioDTO> que se colocan para separar los elementos
		aux = aux.replace(',', '*');
		return aux;		
	}
	
	private String toStringCombinedReport() {
		// TODO Auto-generated method stub
		String aux= (this.accountNumber == null ? "" : this.accountNumber) + ";" + 
				(this.accountName == null ? "" : this.accountName) + ";" + 
				(this.debit == null ? "" : this.debit) + ";" + 
				(this.credit == null ? "" : this.credit) + ";" + 
				(this.balance == null ? "" : this.balance) + ";" ;
//		El caracter * reemplaza a las comas (,) para luego diferenciar en el toString de 
//		List<DuePortfolioDTO> que se colocan para separar los elementos
		aux = aux.replace(',', '*');
		return aux;		
	}

	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public String getPreviousYearsAccountCode() {
		return previousYearsAccountCode;
	}

	public void setPreviousYearsAccountCode(String previousYearsAccountCode) {
		this.previousYearsAccountCode = previousYearsAccountCode;
	}
	
}
