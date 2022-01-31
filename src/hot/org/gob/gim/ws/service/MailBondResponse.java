package org.gob.gim.ws.service;

import java.util.List;

public class MailBondResponse {

	private List<BondSendMail> bonds;

	public List<BondSendMail> getBonds() {
		return bonds;
	}

	public void setBonds(List<BondSendMail> bonds) {
		this.bonds = bonds;
	}

}
