/**
 * 
 */
package org.gob.loja.gim.ws.dto.queries.response;

import java.util.ArrayList;
import java.util.List;

import org.gob.loja.gim.ws.dto.BondWS;
import org.gob.loja.gim.ws.dto.CommonResponseWS;
import org.gob.loja.gim.ws.dto.Taxpayer;

/**
 * @author René
 *
 */
public class DebtsResponse extends CommonResponseWS {
	private Taxpayer taxpayer;
	private List<BondWS> bonds = new ArrayList<BondWS>();

	/**
	 * @return the taxpayer
	 */
	public Taxpayer getTaxpayer() {
		return taxpayer;
	}

	/**
	 * @param taxpayer
	 *            the taxpayer to set
	 */
	public void setTaxpayer(Taxpayer taxpayer) {
		this.taxpayer = taxpayer;
	}

	/**
	 * @return the bonds
	 */
	public List<BondWS> getBonds() {
		return bonds;
	}

	/**
	 * @param bonds
	 *            the bonds to set
	 */
	public void setBonds(List<BondWS> bonds) {
		this.bonds = bonds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DebtsResponse [taxpayer=" + taxpayer + ", bonds=" + bonds + "]";
	}

}
