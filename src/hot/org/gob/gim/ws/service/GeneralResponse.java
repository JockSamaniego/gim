package org.gob.gim.ws.service;

import java.util.List;

public class GeneralResponse {

	private List<BondPrintReport> bonds;

	public List<BondPrintReport> getBonds() {
		return bonds;
	}

	public void setBonds(List<BondPrintReport> bonds) {
		this.bonds = bonds;
	}

}
