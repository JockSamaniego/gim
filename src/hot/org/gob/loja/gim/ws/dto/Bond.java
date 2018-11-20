package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bond {
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
	private List<BondDetail> bondsDetail;
	private String metadata;
	
	public Bond() {
	}
	
	public Bond(Long id, Long number, String account, String serviceCode, BigDecimal total, Date serviceDate, Date expirationDate, 
			BigDecimal interests, BigDecimal surcharges, BigDecimal taxes, BigDecimal discounts) {
		this.id = id;
		this.number =  number;
		this.account = account;
		this.serviceCode = serviceCode;
		this.serviceDate = serviceDate;
		this.expirationDate = expirationDate;
		this.total = total;
		this.interests = interests;
		this.surcharges = surcharges;
		this.taxes = taxes;
		this.discounts = discounts;
		this.bondsDetail = new ArrayList<BondDetail>();
	}
	
	public Bond(Long id, Long number, String account, String serviceCode, BigDecimal total, Date serviceDate, Date expirationDate, 
			BigDecimal interests, BigDecimal surcharges, BigDecimal taxes, BigDecimal discounts, String metadata) {
		this.id = id;
		this.number =  number;
		this.account = account;
		this.serviceCode = serviceCode;
		this.serviceDate = serviceDate;
		this.expirationDate = expirationDate;
		this.total = total;
		this.interests = interests;
		this.surcharges = surcharges;
		this.taxes = taxes;
		this.discounts = discounts;
		this.bondsDetail = new ArrayList<BondDetail>();
		this.metadata = metadata;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getInterests() {
		return interests;
	}
	public void setInterests(BigDecimal interests) {
		this.interests = interests;
	}

	public BigDecimal getSurcharges() {
		return surcharges;
	}

	public void setSurcharges(BigDecimal surcharges) {
		this.surcharges = surcharges;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getDiscounts() {
		return discounts;
	}

	public void setDiscounts(BigDecimal discounts) {
		this.discounts = discounts;
	}

	public Date getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the bondsDetail
	 */
	public List<BondDetail> getBondsDetail() {
		return bondsDetail;
	}

	/**
	 * @param bondsDetail the bondsDetail to set
	 */
	public void setBondsDetail(List<BondDetail> bondsDetail) {
		this.bondsDetail = bondsDetail;
	}

	/**
	 * REMISION
	 * @return
	 */
	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
}
