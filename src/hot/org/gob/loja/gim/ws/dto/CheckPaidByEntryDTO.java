/**
 * 
 */
package org.gob.loja.gim.ws.dto;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author hack
 *
 */
public class CheckPaidByEntryDTO {

	private Long entry;
	private Date mindate;
	private Date maxdate;
	private String group;
	private List<CheckPaidBondDTO> bonds = new ArrayList<CheckPaidBondDTO>();

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

	/**
	 * @return the bonds
	 */
	public List<CheckPaidBondDTO> getBonds() {
		return bonds;
	}

	/**
	 * @param bonds the bonds to set
	 */
	public void setBonds(List<CheckPaidBondDTO> bonds) {
		this.bonds = bonds;
	}

	public Date getMindate() {
		return mindate;
	}

	public void setMindate(Date mindate) {
		this.mindate = mindate;
	}
	
	/**
	 * @return the maxdate
	 */
	public Date getMaxdate() {
		return maxdate;
	}

	/**
	 * @param maxdate the maxdate to set
	 */
	public void setMaxdate(Date maxdate) {
		this.maxdate = maxdate;
	}
	
	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CheckPaidByEntryDTO [entry=" + entry + ", mindate=" + mindate
				+ ", maxdate=" + maxdate + ", group=" + group + ", bonds="
				+ bonds + "]";
	}

}
