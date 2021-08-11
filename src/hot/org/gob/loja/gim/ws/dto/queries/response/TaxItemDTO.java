/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.response;

import java.math.BigDecimal;

import ec.gob.gim.income.model.TaxItem;

/**
 * @author René
 *
 */
public class TaxItemDTO {

	private Long id;

	private BigDecimal appliedRate;

	private BigDecimal taxRate;

	private BigDecimal value;

	private String taxName;

	public TaxItemDTO(TaxItem item) {
		this.appliedRate = item.getAppliedRate();
		this.id = item.getId();
		this.taxName = item.getTax().getName();
		this.taxRate = item.getAppliedTaxRate().getRate();
		this.value = item.getValue();
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
	 * @return the appliedRate
	 */
	public BigDecimal getAppliedRate() {
		return appliedRate;
	}

	/**
	 * @param appliedRate
	 *            the appliedRate to set
	 */
	public void setAppliedRate(BigDecimal appliedRate) {
		this.appliedRate = appliedRate;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the taxName
	 */
	public String getTaxName() {
		return taxName;
	}

	/**
	 * @param taxName
	 *            the taxName to set
	 */
	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	/**
	 * @return the taxRate
	 */
	public BigDecimal getTaxRate() {
		return taxRate;
	}

	/**
	 * @param taxRate
	 *            the taxRate to set
	 */
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TaxItemDTO [id=" + id + ", appliedRate=" + appliedRate
				+ ", taxRate=" + taxRate + ", value=" + value + ", taxName="
				+ taxName + "]";
	}

}
