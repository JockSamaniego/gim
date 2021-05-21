/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.response;

import org.gob.loja.gim.ws.dto.CommonResponseWS;

/**
 * @author René
 *
 */
public class BondResponse extends CommonResponseWS {
	private BondDTO bond;

	public BondDTO getBond() {
		return bond;
	}

	public void setBond(BondDTO bond) {
		this.bond = bond;
	}

	@Override
	public String toString() {
		return "BondResponse [bond=" + bond + "]";
	}

}
