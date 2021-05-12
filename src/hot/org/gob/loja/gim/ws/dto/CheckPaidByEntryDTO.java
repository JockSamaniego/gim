/**
 * 
 */
package org.gob.loja.gim.ws.dto;

import java.util.List;
import java.util.ArrayList;

/**
 * @author hack
 *
 */
public class CheckPaidByEntryDTO {

	private Long entry;
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
}
