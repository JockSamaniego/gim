package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;

public class FutureBond {

	private String account;
	private String serviceCode;
	private Long quantity;

	private BigDecimal total;
	private BigDecimal interests;
	private BigDecimal surcharges;
	private BigDecimal taxes;
	private BigDecimal discounts;
	// private List<BondDetail> bondsDetail;

	public FutureBond() {
	}

	public FutureBond(String account, String serviceCode, Long quantity, BigDecimal total, BigDecimal interests,
			BigDecimal surcharges, BigDecimal taxes, BigDecimal discounts) {
		this.account = account;
		this.serviceCode = serviceCode;
		this.quantity = quantity;
		this.total = total;
		this.interests = interests;
		this.surcharges = surcharges;
		this.taxes = taxes;
		this.discounts = discounts;
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

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
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

}
