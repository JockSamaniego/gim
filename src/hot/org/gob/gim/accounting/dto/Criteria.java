package org.gob.gim.accounting.dto;

import java.util.Date;

public class Criteria {
	private ReportType reportType = ReportType.REVENUE;
	private Long fiscalPeriodId;
	private String accountCode;
	private ReportFilter reportFilter;
	private Date startDate;
	private Date endDate;
	
	private String textSearch;
	
	public ReportType getReportType() {
		return reportType;
	}

	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}

	public Long getFiscalPeriodId() {
		return fiscalPeriodId;
	}

	public void setFiscalPeriodId(Long fiscalPeriodId) {
		this.fiscalPeriodId = fiscalPeriodId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public ReportFilter getReportFilter() {
		return reportFilter;
	}

	public void setReportFilter(ReportFilter reportFilter) {
		this.reportFilter = reportFilter;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getTextSearch() {
		return textSearch;
	}

	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}	
	
}
