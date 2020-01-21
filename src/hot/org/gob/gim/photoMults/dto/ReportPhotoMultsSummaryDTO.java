/**
 * 
 */
package org.gob.gim.photoMults.dto;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author Rene
 *
 */
@NativeQueryResultEntity
public class ReportPhotoMultsSummaryDTO {

	@NativeQueryResultColumn(index = 0)
	private Long year;

	@NativeQueryResultColumn(index = 1)
	private Long month;

	@NativeQueryResultColumn(index = 2)
	private String monthName;

	@NativeQueryResultColumn(index = 3)
	private Long emissionNumber;

	@NativeQueryResultColumn(index = 4)
	private Long preEmissionNumber;

	@NativeQueryResultColumn(index = 5)
	private Long lowsNumber;

	@NativeQueryResultColumn(index = 6)
	private Long paidsNumber;

	@NativeQueryResultColumn(index = 7)
	private Long extemporaneousNumber;

	public Long getYear() {
		return year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	public Long getMonth() {
		return month;
	}

	public void setMonth(Long month) {
		this.month = month;
	}

	public String getMonthName() {
		return monthName;
	}

	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}

	public Long getEmissionNumber() {
		return emissionNumber;
	}

	public void setEmissionNumber(Long emissionNumber) {
		this.emissionNumber = emissionNumber;
	}

	public Long getPreEmissionNumber() {
		return preEmissionNumber;
	}

	public void setPreEmissionNumber(Long preEmissionNumber) {
		this.preEmissionNumber = preEmissionNumber;
	}

	public Long getLowsNumber() {
		return lowsNumber;
	}

	public void setLowsNumber(Long lowsNumber) {
		this.lowsNumber = lowsNumber;
	}

	public Long getPaidsNumber() {
		return paidsNumber;
	}

	public void setPaidsNumber(Long paidsNumber) {
		this.paidsNumber = paidsNumber;
	}

	public Long getExtemporaneousNumber() {
		return extemporaneousNumber;
	}

	public void setExtemporaneousNumber(Long extemporaneousNumber) {
		this.extemporaneousNumber = extemporaneousNumber;
	}

	@Override
	public String toString() {
		return "ReportPhotoMultsSummaryDTO [year=" + year + ", month=" + month
				+ ", monthName=" + monthName + ", emissionNumber="
				+ emissionNumber + ", preEmissionNumber=" + preEmissionNumber
				+ ", lowsNumber=" + lowsNumber + ", paidsNumber=" + paidsNumber
				+ ", extemporaneousNumber=" + extemporaneousNumber + "]";
	}

}
