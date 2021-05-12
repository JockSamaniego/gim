/**
 * 
 */
package org.gob.loja.gim.ws.dto;

import java.util.Date;

/**
 * @author hack
 *
 */
public class CheckPaidBondDTO {

	private Long id;
	private Date creation;
	private Long entry;

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
	 * @return the creation
	 */
	public Date getCreation() {
		return creation;
	}

	/**
	 * @param creation
	 *            the creation to set
	 */
	public void setCreation(Date creation) {
		this.creation = creation;
	}

	/**
	 * @return the entry
	 */
	public Long getEntry() {
		return entry;
	}

	/**
	 * @param entry
	 *            the entry to set
	 */
	public void setEntry(Long entry) {
		this.entry = entry;
	}

}
