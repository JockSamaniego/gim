/**
 * 
 */
package org.gob.gim.coercive.dto.criteria;

import ec.gob.gim.common.model.ItemCatalog;

/**
 * @author René
 *
 */
public class DataInfractionSearchCriteria {
	
	private String identification;
	private String name;
	private String licensePlate;
	private String ticket;
	private ItemCatalog status;
	
	
	
	/**
	 * 
	 */
	public DataInfractionSearchCriteria() {
		super();
		identification = null;
		name = null;
		licensePlate = null;
		ticket = null;
		this.status = null;
	}
	/**
	 * @return the identification
	 */
	public String getIdentification() {
		return identification;
	}
	/**
	 * @param identification the identification to set
	 */
	public void setIdentification(String identification) {
		this.identification = identification;
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
	 * @return the licensePlate
	 */
	public String getLicensePlate() {
		return licensePlate;
	}
	/**
	 * @param licensePlate the licensePlate to set
	 */
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	/**
	 * @return the ticket
	 */
	public String getTicket() {
		return ticket;
	}
	/**
	 * @param ticket the ticket to set
	 */
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	/**
	 * @return the status
	 */
	public ItemCatalog getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(ItemCatalog status) {
		this.status = status;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataInfractionSearchCriteria [identification=" + identification
				+ ", name=" + name + ", licensePlate=" + licensePlate
				+ ", ticket=" + ticket + ", status=" + status + "]";
	}
	
}
