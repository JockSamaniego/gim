package org.gob.loja.gim.ws.dto.preemission;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.NotEmpty;

public class UtilityRequest extends CommonRequest {

	@NotNull(message = "cadastralCode no puede ser nulo")
	@NotEmpty(message = "cadastralCode no puede ser vacío")
	private String cadastralCode;

	@NotNull(message = "previousCadastralCode no puede ser nulo")
	@NotEmpty(message = "previousCadastralCode no puede ser vacío")
	private String previousCadastralCode;

	@NotNull(message = "propertyAddress no puede ser nulo")
	@NotEmpty(message = "propertyAddress no puede ser vacío")
	private String propertyAddress;

	@NotNull(message = "seller no puede ser nulo")
	@NotEmpty(message = "seller no puede ser vacío")
	private String seller;

	@NotNull(message = "buyer no puede ser nulo")
	@NotEmpty(message = "buyer no puede ser vacío")
	private String buyer;

	@NotNull(message = "mortgageDiscount no puede ser nulo")
	private Boolean mortgageDiscount;

	@NotNull(message = "apply50Discount no puede ser nulo")
	private Boolean apply50Discount;

	// @NotNull(message = "valueDiscount no puede ser nulo")
	private BigDecimal valueDiscount;

	@NotNull(message = "purchaseDate no puede ser nulo")
	private String purchaseDate;

	@NotNull(message = "purchaseValue no puede ser nulo")
	private BigDecimal purchaseValue;

	@NotNull(message = "saleValue no puede ser nulo")
	private BigDecimal saleValue;

	@NotNull(message = "cemValue no puede ser nulo")
	private BigDecimal cemValue;

	@NotNull(message = "newBuild no puede ser nulo")
	private BigDecimal newBuild;

	@NotNull(message = "notaryNumber no puede ser nulo")
	private String notaryNumber;

	@NotNull(message = "prepayment no puede ser nulo")
	private BigDecimal prepayment;

	@NotNull(message = "value no puede ser nulo")
	private BigDecimal value;

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public String getPreviousCadastralCode() {
		return previousCadastralCode;
	}

	public void setPreviousCadastralCode(String previousCadastralCode) {
		this.previousCadastralCode = previousCadastralCode;
	}

	public String getPropertyAddress() {
		return propertyAddress;
	}

	public void setPropertyAddress(String propertyAddress) {
		this.propertyAddress = propertyAddress;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public Boolean getMortgageDiscount() {
		return mortgageDiscount;
	}

	public void setMortgageDiscount(Boolean mortgageDiscount) {
		this.mortgageDiscount = mortgageDiscount;
	}

	public Boolean getApply50Discount() {
		return apply50Discount;
	}

	public void setApply50Discount(Boolean apply50Discount) {
		this.apply50Discount = apply50Discount;
	}

	public BigDecimal getValueDiscount() {
		return valueDiscount;
	}

	public void setValueDiscount(BigDecimal valueDiscount) {
		this.valueDiscount = valueDiscount;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public BigDecimal getPurchaseValue() {
		return purchaseValue;
	}

	public void setPurchaseValue(BigDecimal purchaseValue) {
		this.purchaseValue = purchaseValue;
	}

	public BigDecimal getSaleValue() {
		return saleValue;
	}

	public void setSaleValue(BigDecimal saleValue) {
		this.saleValue = saleValue;
	}

	public BigDecimal getCemValue() {
		return cemValue;
	}

	public void setCemValue(BigDecimal cemValue) {
		this.cemValue = cemValue;
	}

	public BigDecimal getNewBuild() {
		return newBuild;
	}

	public void setNewBuild(BigDecimal newBuild) {
		this.newBuild = newBuild;
	}

	public String getNotaryNumber() {
		return notaryNumber;
	}

	public void setNotaryNumber(String notaryNumber) {
		this.notaryNumber = notaryNumber;
	}

	public BigDecimal getPrepayment() {
		return prepayment;
	}

	public void setPrepayment(BigDecimal prepayment) {
		this.prepayment = prepayment;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "UtilityRequest [cadastralCode=" + cadastralCode
				+ ", previousCadastralCode=" + previousCadastralCode
				+ ", propertyAddress=" + propertyAddress + ", seller=" + seller
				+ ", buyer=" + buyer + ", mortgageDiscount=" + mortgageDiscount
				+ ", apply50Discount=" + apply50Discount + ", valueDiscount="
				+ valueDiscount + ", purchaseDate=" + purchaseDate
				+ ", purchaseValue=" + purchaseValue + ", saleValue="
				+ saleValue + ", cemValue=" + cemValue + ", newBuild="
				+ newBuild + ", notaryNumber=" + notaryNumber + ", prepayment="
				+ prepayment + ", value=" + value + "]";
	}

}
