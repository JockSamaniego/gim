package org.gob.gim.ws.service;

import java.util.List;

public class GeneralResponse {

	private List<BondPrintReport> vouchers;

	public List<BondPrintReport> getVouchers() {
		return vouchers;
	}

	public void setVouchers(List<BondPrintReport> vouchers) {
		this.vouchers = vouchers;
	}
	
}
