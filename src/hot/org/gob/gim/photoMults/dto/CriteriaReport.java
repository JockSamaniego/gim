package org.gob.gim.photoMults.dto;

import java.util.Date;

public class CriteriaReport {
	private ReportPhotoMultsType reportType;
	private Date startDate;
	private Date endDate;

	public CriteriaReport() {
		super();
		this.reportType = ReportPhotoMultsType.PHOTO_MULTS_EMIT;
		this.startDate = null;
		this.endDate = null;
	}

	public ReportPhotoMultsType getReportType() {
		return reportType;
	}

	public void setReportType(ReportPhotoMultsType reportType) {
		this.reportType = reportType;
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

	@Override
	public String toString() {
		return "CriteriaReport [reportType=" + reportType + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
	}

}
