/**
 * 
 */
package org.gob.loja.gim.ws.dto.digitalReceipts;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;

/**
 * @author René
 *
 */
public class TaxpayerRecordDTO {

	private Long id;

	private String name;

	private String number;

	private String address;

	private String phone;

	private String fax;

	private String slogan;

	private Boolean isSpecialTaxpayer;

	private String specialTaxpayerResolution;

	private Date specialTaxpayerDate;

	private Boolean isDefault;

	private byte[] flag;

	private byte[] logo;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the slogan
	 */
	public String getSlogan() {
		return slogan;
	}

	/**
	 * @param slogan
	 *            the slogan to set
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	/**
	 * @return the isSpecialTaxpayer
	 */
	public Boolean getIsSpecialTaxpayer() {
		return isSpecialTaxpayer;
	}

	/**
	 * @param isSpecialTaxpayer
	 *            the isSpecialTaxpayer to set
	 */
	public void setIsSpecialTaxpayer(Boolean isSpecialTaxpayer) {
		this.isSpecialTaxpayer = isSpecialTaxpayer;
	}

	/**
	 * @return the specialTaxpayerResolution
	 */
	public String getSpecialTaxpayerResolution() {
		return specialTaxpayerResolution;
	}

	/**
	 * @param specialTaxpayerResolution
	 *            the specialTaxpayerResolution to set
	 */
	public void setSpecialTaxpayerResolution(String specialTaxpayerResolution) {
		this.specialTaxpayerResolution = specialTaxpayerResolution;
	}

	/**
	 * @return the specialTaxpayerDate
	 */
	public Date getSpecialTaxpayerDate() {
		return specialTaxpayerDate;
	}

	/**
	 * @param specialTaxpayerDate
	 *            the specialTaxpayerDate to set
	 */
	public void setSpecialTaxpayerDate(Date specialTaxpayerDate) {
		this.specialTaxpayerDate = specialTaxpayerDate;
	}

	/**
	 * @return the isDefault
	 */
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the flag
	 */
	public byte[] getFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            the flag to set
	 */
	public void setFlag(byte[] flag) {
		this.flag = flag;
	}

	/**
	 * @return the logo
	 */
	public byte[] getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            the logo to set
	 */
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TaxpayerRecordDTO [id=" + id + ", name=" + name + ", number="
				+ number + ", address=" + address + ", phone=" + phone
				+ ", fax=" + fax + ", slogan=" + slogan
				+ ", isSpecialTaxpayer=" + isSpecialTaxpayer
				+ ", specialTaxpayerResolution=" + specialTaxpayerResolution
				+ ", specialTaxpayerDate=" + specialTaxpayerDate
				+ ", isDefault=" + isDefault + ", flag="
				+ Arrays.toString(flag) + ", logo=" + Arrays.toString(logo)
				+ "]";
	}

}
