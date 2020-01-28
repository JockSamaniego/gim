package org.gob.gim.photoMults.dto;

import java.util.Date;

public class CriteriaReport {
	private ReportPhotoMultsType reportType;
	private Date startDate;
	private Date endDate;
	private String infractionNumber;
	private String identificationNumber;
	private String residentName;
	private String plate;

	public CriteriaReport() {
		super();
		this.reportType = ReportPhotoMultsType.PHOTO_MULTS_EMIT;
		this.startDate = new Date();
		this.endDate = new Date();
		this.infractionNumber = null;
		this.identificationNumber = null;
		this.residentName = null;
		this.plate = null;
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
	
	public String getInfractionNumber() {
		return infractionNumber;
	}

	public void setInfractionNumber(String infractionNumber) {
		this.infractionNumber = infractionNumber;
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

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	@Override
	public String toString() {
		return "CriteriaReport [reportType=" + reportType + ", startDate="
				+ startDate + ", endDate=" + endDate + ", infractionNumber="
						+ infractionNumber + ", placa="
								+ plate + ", identification="
										+ identificationNumber + ", nombres="
												+ residentName +  "]";
	}

}
