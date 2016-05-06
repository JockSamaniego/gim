package org.gob.gim.cadaster.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.cadaster.model.TerritorialDivisionType;

@Name("territorialDivisionTypeHome")
public class TerritorialDivisionTypeHome extends EntityHome<TerritorialDivisionType> {

	public void setTerritorialDivisionTypeId(Long id) {
		setId(id);
	}

	public Long getTerritorialDivisionTypeId() {
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
	
	public TerritorialDivisionType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
