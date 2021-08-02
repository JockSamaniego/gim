/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.response;

import org.gob.loja.gim.ws.dto.CommonResponseWS;
import org.gob.loja.gim.ws.dto.queries.EntryDTO;

/**
 * @author René
 *
 */
public class EntryResponse extends CommonResponseWS {
	private EntryDTO entry;

	/**
	 * @return the entry
	 */
	public EntryDTO getEntry() {
		return entry;
	}

	/**
	 * @param entry
	 *            the entry to set
	 */
	public void setEntry(EntryDTO entry) {
		this.entry = entry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EntryResponse [entry=" + entry + "]";
	}

}
