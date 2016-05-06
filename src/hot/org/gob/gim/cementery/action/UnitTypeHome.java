package org.gob.gim.cementery.action;

import ec.gob.gim.cementery.model.UnitType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("unitTypeHome")
public class UnitTypeHome extends EntityHome<UnitType> {

	private static final long serialVersionUID = 5427308502234182843L;

	public void setUnitTypeId(Long id) {
		setId(id);
	}

	public Long getUnitTypeId() {
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

	public UnitType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
