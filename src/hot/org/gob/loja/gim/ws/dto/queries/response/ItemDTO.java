/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.response;

import java.math.BigDecimal;

import ec.gob.gim.revenue.model.Item;


/**
 * @author René
 *
 */
public class ItemDTO {

	
	private Long id;

	// Cantidad
	private BigDecimal amount;

	private BigDecimal value;
	
	private BigDecimal total;
	
	private Integer orderNumber;
	
	private Boolean isTaxable;
	
	private String observations;
	
	private String entryName;
	
	private String entryCode;
	
	private Long entryId;
	
	public ItemDTO(Item item) {
		this.amount = item.getAmount();
		this.entryId = item.getEntry().getId();
		this.entryName= item.getEntry().getName();
		this.id = item.getId();
		this.isTaxable = item.getIsTaxable();
		this.observations = item.getObservations();
		this.orderNumber = item.getOrderNumber();
		this.total = item.getTotal();
		this.value = item.getValue();
		this.entryCode = item.getEntry().getCode();
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	/**
	 * @return the orderNumber
	 */
	public Integer getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the isTaxable
	 */
	public Boolean getIsTaxable() {
		return isTaxable;
	}

	/**
	 * @param isTaxable the isTaxable to set
	 */
	public void setIsTaxable(Boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

	/**
	 * @return the observations
	 */
	public String getObservations() {
		return observations;
	}

	/**
	 * @param observations the observations to set
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return the entryName
	 */
	public String getEntryName() {
		return entryName;
	}

	/**
	 * @param entryName the entryName to set
	 */
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	/**
	 * @return the entryId
	 */
	public Long getEntryId() {
		return entryId;
	}

	/**
	 * @param entryId the entryId to set
	 */
	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	/**
	 * @return the entryCode
	 */
	public String getEntryCode() {
		return entryCode;
	}

	/**
	 * @param entryCode the entryCode to set
	 */
	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}
	
	
}
