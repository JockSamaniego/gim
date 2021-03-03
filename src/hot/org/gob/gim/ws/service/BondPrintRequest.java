package org.gob.gim.ws.service;

import java.util.List;

public class BondPrintRequest {
	
	private List <BondPrintUpdate> bonds;

	public List<BondPrintUpdate> getBonds() {
		return bonds;
	}

	public void setBonds(List<BondPrintUpdate> bonds) {
		this.bonds = bonds;
	}
		
}
