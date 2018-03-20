package org.gob.loja.gim.ws.dto;

import java.util.List;

public class FutureStatement {
	
	private Taxpayer taxpayer;
	private List<FutureBond> bonds;

	public FutureStatement() {
	}

	public FutureStatement(Taxpayer taxpayer, List<FutureBond> bonds) {
		this.taxpayer = taxpayer;
		this.bonds = bonds;
	}

	public Taxpayer getTaxpayer() {
		return taxpayer;
	}

	public void setTaxpayer(Taxpayer taxpayer) {
		this.taxpayer = taxpayer;
	}

	public List<FutureBond> getBonds() {
		return bonds;
	}

	public void setBonds(List<FutureBond> bonds) {
		this.bonds = bonds;
	}

}
