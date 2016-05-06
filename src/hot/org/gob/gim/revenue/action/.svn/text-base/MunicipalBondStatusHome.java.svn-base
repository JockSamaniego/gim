package org.gob.gim.revenue.action;

import ec.gob.gim.revenue.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("municipalBondStatusHome")
public class MunicipalBondStatusHome extends EntityHome<MunicipalBondStatus> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setMunicipalBondStatusId(Long id) {
		setId(id);
	}

	public Long getMunicipalBondStatusId() {
		return (Long) getId();
	}

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

	public MunicipalBondStatus getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
