/**
 * 
 */
package org.gob.gim.photoMults.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author Rene
 *
 */
@NativeQueryResultEntity
public class ReportPhotoMultsDTO {

	@NativeQueryResultColumn(index = 0)
	private Long number;

	@NativeQueryResultColumn(index = 1)
	private Date dateTypeReport;

	@NativeQueryResultColumn(index = 2)
	private String status;

	@NativeQueryResultColumn(index = 3)
	private String entry;

	@NativeQueryResultColumn(index = 4)
	private BigDecimal value;

	@NativeQueryResultColumn(index = 5)
	private String identificationNumber;

	@NativeQueryResultColumn(index = 6)
	private String resident;

	@NativeQueryResultColumn(index = 7)
	private Date citationDate;

	@NativeQueryResultColumn(index = 8)
	private String contraventionNumber;

	@NativeQueryResultColumn(index = 9)
	private String numberPlate;

	@NativeQueryResultColumn(index = 10)
	private String physicalInfractionNumber;

	@NativeQueryResultColumn(index = 11)
	private String serviceType;

	@NativeQueryResultColumn(index = 12)
	private BigDecimal speeding;

	@NativeQueryResultColumn(index = 13)
	private String supportDocumentURL;

	@NativeQueryResultColumn(index = 14)
	private String vehicleType;

	@NativeQueryResultColumn(index = 15)
	private Date creationDate;

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getResident() {
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

	public Date getCitationDate() {
		return citationDate;
	}

	public void setCitationDate(Date citationDate) {
		this.citationDate = citationDate;
	}

	public String getContraventionNumber() {
		return contraventionNumber;
	}

	public void setContraventionNumber(String contraventionNumber) {
		this.contraventionNumber = contraventionNumber;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getPhysicalInfractionNumber() {
		return physicalInfractionNumber;
	}

	public void setPhysicalInfractionNumber(String physicalInfractionNumber) {
		this.physicalInfractionNumber = physicalInfractionNumber;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public BigDecimal getSpeeding() {
		return speeding;
	}

	public void setSpeeding(BigDecimal speeding) {
		this.speeding = speeding;
	}

	public String getSupportDocumentURL() {
		return supportDocumentURL;
	}

	public void setSupportDocumentURL(String supportDocumentURL) {
		this.supportDocumentURL = supportDocumentURL;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	public Date getDateTypeReport() {
		return dateTypeReport;
	}

	public void setDateTypeReport(Date dateTypeReport) {
		this.dateTypeReport = dateTypeReport;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "ReportPhotoMultsDTO [number=" + number + ", dateTypeReport="
				+ dateTypeReport + ", status=" + status + ", entry=" + entry
				+ ", value=" + value + ", identificationNumber="
				+ identificationNumber + ", resident=" + resident
				+ ", citationDate=" + citationDate + ", contraventionNumber="
				+ contraventionNumber + ", numberPlate=" + numberPlate
				+ ", physicalInfractionNumber=" + physicalInfractionNumber
				+ ", serviceType=" + serviceType + ", speeding=" + speeding
				+ ", supportDocumentURL=" + supportDocumentURL
				+ ", vehicleType=" + vehicleType +  ", creationDate=" + creationDate + "]";
	}

}
