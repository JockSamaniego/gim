/**
 * 
 */
package org.gob.loja.gim.ws.dto.digitalReceipts.response;

import java.util.ArrayList;
import java.util.List;

import org.gob.loja.gim.ws.dto.CommonResponseWS;
import org.gob.loja.gim.ws.dto.digitalReceipts.BondShortDTO;
import org.gob.loja.gim.ws.dto.queries.response.BondDTO;

/**
 * @author René
 *
 */
public class ExternalPaidsResponse extends CommonResponseWS {
	
	private List<BondShortDTO> bonds = new ArrayList<BondShortDTO>();

	/**
	 * @return the bonds
	 */
	public List<BondShortDTO> getBonds() {
		return bonds;
	}

	/**
	 * @param bonds the bonds to set
	 */
	public void setBonds(List<BondShortDTO> bonds) {
		this.bonds = bonds;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExternalPaidsResponse [bonds=" + bonds + "]";
	}
	
}
