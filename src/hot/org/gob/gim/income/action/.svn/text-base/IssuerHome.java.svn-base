package org.gob.gim.income.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.Issuer;

@Name("issuerHome")
public class IssuerHome extends EntityHome<Issuer> {


	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public Issuer getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
