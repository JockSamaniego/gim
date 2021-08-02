/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries;

import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author René
 *
 */
@NativeQueryResultEntity
public class OperatingPermitDTO {

	@NativeQueryResultColumn(index = 0)
	private Long id;

	@NativeQueryResultColumn(index = 1)
	private String parish;
	@NativeQueryResultColumn(index = 2)
	private Date emissionDate;
	@NativeQueryResultColumn(index = 3)
	private Date dateValidity;
	@NativeQueryResultColumn(index = 4)
	private String ruc;
	@NativeQueryResultColumn(index = 5)
	private String localName;
	@NativeQueryResultColumn(index = 6)
	private String paperCode;
	@NativeQueryResultColumn(index = 7)
	private String localCode;
	@NativeQueryResultColumn(index = 8)
	private String activity;
	@NativeQueryResultColumn(index = 9)
	private String address;
	@NativeQueryResultColumn(index = 10)
	private String phoneNumber;
	@NativeQueryResultColumn(index = 11)
	private String mobileNumber;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the parish
	 */
	public String getParish() {
		return parish;
	}

	/**
	 * @param parish
	 *            the parish to set
	 */
	public void setParish(String parish) {
		this.parish = parish;
	}

	/**
	 * @return the emissionDate
	 */
	public Date getEmissionDate() {
		return emissionDate;
	}

	/**
	 * @param emissionDate
	 *            the emissionDate to set
	 */
	public void setEmissionDate(Date emissionDate) {
		this.emissionDate = emissionDate;
	}

	/**
	 * @return the dateValidity
	 */
	public Date getDateValidity() {
		return dateValidity;
	}

	/**
	 * @param dateValidity
	 *            the dateValidity to set
	 */
	public void setDateValidity(Date dateValidity) {
		this.dateValidity = dateValidity;
	}

	/**
	 * @return the ruc
	 */
	public String getRuc() {
		return ruc;
	}

	/**
	 * @param ruc
	 *            the ruc to set
	 */
	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	/**
	 * @return the localName
	 */
	public String getLocalName() {
		return localName;
	}

	/**
	 * @param localName
	 *            the localName to set
	 */
	public void setLocalName(String localName) {
		this.localName = localName;
	}

	/**
	 * @return the paperCode
	 */
	public String getPaperCode() {
		return paperCode;
	}

	/**
	 * @param paperCode
	 *            the paperCode to set
	 */
	public void setPaperCode(String paperCode) {
		this.paperCode = paperCode;
	}

	/**
	 * @return the localCode
	 */
	public String getLocalCode() {
		return localCode;
	}

	/**
	 * @param localCode
	 *            the localCode to set
	 */
	public void setLocalCode(String localCode) {
		this.localCode = localCode;
	}

	/**
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *            the activity to set
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber
	 *            the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OperatingPermitDTO [id=" + id + ", parish=" + parish
				+ ", emissionDate=" + emissionDate + ", dateValidity="
				+ dateValidity + ", ruc=" + ruc + ", localName=" + localName
				+ ", paperCode=" + paperCode + ", localCode=" + localCode
				+ ", activity=" + activity + ", address=" + address
				+ ", phoneNumber=" + phoneNumber + ", mobileNumber="
				+ mobileNumber + "]";
	}

}
