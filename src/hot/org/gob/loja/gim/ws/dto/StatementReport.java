package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatementReport {

	private Date startDate;
	private Date endDate;
	private BigDecimal total;
	private List<BondReport> bondReports;

	public StatementReport(Date startDate, Date endDate, BigDecimal total) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.total = total;
		this.bondReports = new ArrayList<BondReport>();
	}

	public StatementReport() {
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

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<BondReport> getBondReports() {
		return bondReports;
	}

	public void setBondReports(List<BondReport> bondReports) {
		this.bondReports = bondReports;
	}

}