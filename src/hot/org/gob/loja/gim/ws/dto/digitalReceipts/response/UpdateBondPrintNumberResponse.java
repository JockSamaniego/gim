/**
 * 
 */
package org.gob.loja.gim.ws.dto.digitalReceipts.response;

import org.gob.loja.gim.ws.dto.CommonResponseWS;

/**
 * @author René
 *
 */
public class UpdateBondPrintNumberResponse extends CommonResponseWS{
	
	private Boolean ok;

	/**
	 * @return the ok
	 */
	public Boolean getOk() {
		return ok;
	}

	/**
	 * @param ok the ok to set
	 */
	public void setOk(Boolean ok) {
		this.ok = ok;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UpdateBondPrintNumberResponse [ok=" + ok + "]";
	}
	
}
