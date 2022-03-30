/**
 * 
 */
package org.gob.loja.gim.ws.dto.digitalReceipts;

import java.math.BigDecimal;
import java.util.Date;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

/**
 * @author René
 *
 */
@NativeQueryResultEntity
public class BondShortDTO {
	
	@NativeQueryResultColumn(index = 0)
	private Long id;

	@NativeQueryResultColumn(index = 1)
	private Date liquidationDate;

	@NativeQueryResultColumn(index = 2)
	private Date liquidationTime;

	@NativeQueryResultColumn(index = 3)
	private Date emisionDate;

	@NativeQueryResultColumn(index = 4)
	private Date emisionTime;

	@NativeQueryResultColumn(index = 5)
	private Date expirationDate;

	@NativeQueryResultColumn(index = 6)
	private Date serviceDate;		

	@NativeQueryResultColumn(index = 7)
	private Long number;

	@NativeQueryResultColumn(index = 8)
	private String description;

	@NativeQueryResultColumn(index = 9)
	private String reference;

	@NativeQueryResultColumn(index = 10)
	private BigDecimal paidTotal;

	@NativeQueryResultColumn(index = 11)
	private Long entryId;

	@NativeQueryResultColumn(index = 12)
	private String entryName;

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
	 * @return the liquidationDate
	 */
	public Date getLiquidationDate() {
		return liquidationDate;
	}

	/**
	 * @param liquidationDate the liquidationDate to set
	 */
	public void setLiquidationDate(Date liquidationDate) {
		this.liquidationDate = liquidationDate;
	}

	/**
	 * @return the liquidationTime
	 */
	public Date getLiquidationTime() {
		return liquidationTime;
	}

	/**
	 * @param liquidationTime the liquidationTime to set
	 */
	public void setLiquidationTime(Date liquidationTime) {
		this.liquidationTime = liquidationTime;
	}

	/**
	 * @return the emisionDate
	 */
	public Date getEmisionDate() {
		return emisionDate;
	}

	/**
	 * @param emisionDate the emisionDate to set
	 */
	public void setEmisionDate(Date emisionDate) {
		this.emisionDate = emisionDate;
	}

	/**
	 * @return the emisionTime
	 */
	public Date getEmisionTime() {
		return emisionTime;
	}

	/**
	 * @param emisionTime the emisionTime to set
	 */
	public void setEmisionTime(Date emisionTime) {
		this.emisionTime = emisionTime;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the serviceDate
	 */
	public Date getServiceDate() {
		return serviceDate;
	}

	/**
	 * @param serviceDate the serviceDate to set
	 */
	public void setServiceDate(Date serviceDate) {
		this.serviceDate = serviceDate;
	}

	/**
	 * @return the number
	 */
	public Long getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the paidTotal
	 */
	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	/**
	 * @param paidTotal the paidTotal to set
	 */
	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BondShortDTO [id=" + id + ", liquidationDate="
				+ liquidationDate + ", liquidationTime=" + liquidationTime
				+ ", emisionDate=" + emisionDate + ", emisionTime="
				+ emisionTime + ", expirationDate=" + expirationDate
				+ ", serviceDate=" + serviceDate + ", number=" + number
				+ ", description=" + description + ", reference=" + reference
				+ ", paidTotal=" + paidTotal + ", entryId=" + entryId
				+ ", entryName=" + entryName + "]";
	}
	

}
