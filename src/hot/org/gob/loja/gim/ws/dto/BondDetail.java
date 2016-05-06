package org.gob.loja.gim.ws.dto;

import java.math.BigDecimal;

public class BondDetail {
	private Long bondId;
	private String subLineAccount;
	private String name;
	private BigDecimal partialValue;
	
	public BondDetail() {
	}
	
	public BondDetail(Long bondId, String subLineAccount, String name, BigDecimal partialValue) {
		this.bondId = bondId;
		this.subLineAccount =  subLineAccount;
		this.name = name;
		this.partialValue = partialValue;
	}

	/**
	 * @return the bondId
	 */
	public Long getBondId() {
		return bondId;
	}

	/**
	 * @param bondId the bondId to set
	 */
	public void setBondId(Long bondId) {
		this.bondId = bondId;
	}

	/**
	 * @return the subLineAccount
	 */
	public String getSubLineAccount() {
		return subLineAccount;
	}

	/**
	 * @param subLineAccount the subLineAccount to set
	 */
	public void setSubLineAccount(String subLineAccount) {
		this.subLineAccount = subLineAccount;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the partialValue
	 */
	public BigDecimal getPartialValue() {
		return partialValue;
	}

	/**
	 * @param partialValue the partialValue to set
	 */
	public void setPartialValue(BigDecimal partialValue) {
		this.partialValue = partialValue;
	}
	
}
