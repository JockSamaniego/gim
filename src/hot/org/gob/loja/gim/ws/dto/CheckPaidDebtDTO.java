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
public class CheckPaidDebtDTO {

	private Long entry;
	private List<CheckPaidBondDTO> pendings = new ArrayList<CheckPaidBondDTO>();

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
	 * @return the pendings
	 */
	public List<CheckPaidBondDTO> getPendings() {
		return pendings;
	}

	/**
	 * @param pendings
	 *            the pendings to set
	 */
	public void setPendings(List<CheckPaidBondDTO> pendings) {
		this.pendings = pendings;
	}

}
