package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BondWS {
	private Long id;
	private Long number;
	private String account;
	private String serviceCode;
	private Date serviceDate;
	private Date expirationDate;
	private BigDecimal total;
	private BigDecimal interests;
	private BigDecimal surcharges;
	private BigDecimal taxes;
	private BigDecimal discounts;
	private String status;
	private List<BondDetail> bondsDetail;

	public BondWS(Long id, Long number, String account, String serviceCode,
			BigDecimal total, Date serviceDate, Date expirationDate,
			BigDecimal interests, BigDecimal surcharges, BigDecimal taxes,
			BigDecimal discounts, String status) {
		this.id = id;
		this.number = number;
		this.account = account;
		this.serviceCode = serviceCode;
		this.serviceDate = serviceDate;
		this.expirationDate = expirationDate;
		this.total = total;
		this.interests = interests;
		this.surcharges = surcharges;
		this.taxes = taxes;
		this.discounts = discounts;
		this.status = status;
		this.bondsDetail = new ArrayList<BondDetail>();
	}

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
	 * @return the number
	 */
	public Long getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the serviceCode
	 */
	public String getServiceCode() {
		return serviceCode;
	}

	/**
	 * @param serviceCode
	 *            the serviceCode to set
	 */
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	/**
	 * @return the serviceDate
	 */
	public Date getServiceDate() {
		return serviceDate;
	}

	/**
	 * @param serviceDate
	 *            the serviceDate to set
	 */
	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * @return the interests
	 */
	public BigDecimal getInterests() {
		return interests;
	}

	/**
	 * @param interests
	 *            the interests to set
	 */
	public void setInterests(BigDecimal interests) {
		this.interests = interests;
	}

	/**
	 * @return the surcharges
	 */
	public BigDecimal getSurcharges() {
		return surcharges;
	}

	/**
	 * @param surcharges
	 *            the surcharges to set
	 */
	public void setSurcharges(BigDecimal surcharges) {
		this.surcharges = surcharges;
	}

	/**
	 * @return the taxes
	 */
	public BigDecimal getTaxes() {
		return taxes;
	}

	/**
	 * @param taxes
	 *            the taxes to set
	 */
	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	/**
	 * @return the discounts
	 */
	public BigDecimal getDiscounts() {
		return discounts;
	}

	/**
	 * @param discounts
	 *            the discounts to set
	 */
	public void setDiscounts(BigDecimal discounts) {
		this.discounts = discounts;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the bondsDetail
	 */
	public List<BondDetail> getBondsDetail() {
		return bondsDetail;
	}

	/**
	 * @param bondsDetail
	 *            the bondsDetail to set
	 */
	public void setBondsDetail(List<BondDetail> bondsDetail) {
		this.bondsDetail = bondsDetail;
	}

}
