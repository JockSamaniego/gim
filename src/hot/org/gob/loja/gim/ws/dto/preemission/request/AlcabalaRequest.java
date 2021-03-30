package org.gob.loja.gim.ws.dto.preemission.request;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.gob.gim.common.validators.NotEmpty;

public class AlcabalaRequest extends CommonRequest {

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

	@NotNull(message = "alcabalaTransactionValue no puede ser nulo")
	private BigDecimal alcabalaTransactionValue;

	@NotNull(message = "earlyTransferDiscount no puede ser nulo")
	private BigDecimal earlyTransferDiscount;

	@NotNull(message = "mortgageDiscount no puede ser nulo")
	private Boolean mortgageDiscount;

	@NotNull(message = "isHalfDiscount no puede ser nulo")
	private Boolean isHalfDiscount;

	private BigDecimal halfDiscountAmount;

	@NotNull(message = "value no puede ser nulo")
	private BigDecimal value;

	@NotNull(message = "prepayment no puede ser nulo")
	private BigDecimal prepayment;

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

	public BigDecimal getAlcabalaTransactionValue() {
		return alcabalaTransactionValue;
	}

	public void setAlcabalaTransactionValue(BigDecimal alcabalaTransactionValue) {
		this.alcabalaTransactionValue = alcabalaTransactionValue;
	}

	public BigDecimal getEarlyTransferDiscount() {
		return earlyTransferDiscount;
	}

	public void setEarlyTransferDiscount(BigDecimal earlyTransferDiscount) {
		this.earlyTransferDiscount = earlyTransferDiscount;
	}

	public Boolean getMortgageDiscount() {
		return mortgageDiscount;
	}

	public void setMortgageDiscount(Boolean mortgageDiscount) {
		this.mortgageDiscount = mortgageDiscount;
	}

	public Boolean getIsHalfDiscount() {
		return isHalfDiscount;
	}

	public void setIsHalfDiscount(Boolean isHalfDiscount) {
		this.isHalfDiscount = isHalfDiscount;
	}

	public BigDecimal getHalfDiscountAmount() {
		return halfDiscountAmount;
	}

	public void setHalfDiscountAmount(BigDecimal halfDiscountAmount) {
		this.halfDiscountAmount = halfDiscountAmount;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getPrepayment() {
		return prepayment;
	}

	public void setPrepayment(BigDecimal prepayment) {
		this.prepayment = prepayment;
	}

	@Override
	public String toString() {
		return "AlcabalaRequest [cadastralCode=" + cadastralCode
				+ ", previousCadastralCode=" + previousCadastralCode
				+ ", propertyAddress=" + propertyAddress + ", seller=" + seller
				+ ", buyer=" + buyer + ", alcabalaTransactionValue=" + alcabalaTransactionValue
				+ ", earlyTransferDiscount=" + earlyTransferDiscount
				+ ", mortgageDiscount=" + mortgageDiscount
				+ ", isHalfDiscount=" + isHalfDiscount
				+ ", halfDiscountAmount=" + halfDiscountAmount + ", value="
				+ value + ", prepayment=" + prepayment
				+ ", getEmiterIdentification()=" + getEmiterIdentification()
				+ ", getResidentIdentification()="
				+ getResidentIdentification() + ", getAccountCode()="
				+ getAccountCode() + ", getExplanation()=" + getExplanation()
				+ ", getReference()=" + getReference() + ", getAddress()="
				+ getAddress() + "]";
	}

}
