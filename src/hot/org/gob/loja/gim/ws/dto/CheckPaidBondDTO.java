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
	private Date emisiondate;
	private Long entry;

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
	 * @return the emisiondate
	 */
	public Date getEmisiondate() {
		return emisiondate;
	}

	/**
	 * @param emisiondate the emisiondate to set
	 */
	public void setEmisiondate(Date emisiondate) {
		this.emisiondate = emisiondate;
	}

	/**
	 * @return the entry
	 */
	public Long getEntry() {
		return entry;
	}

	/**
	 * @param entry the entry to set
	 */
	public void setEntry(Long entry) {
		this.entry = entry;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CheckPaidBondDTO [id=" + id + ", emisiondate=" + emisiondate
				+ ", entry=" + entry + "]";
	}

}
