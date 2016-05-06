package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("streetTypeHome")
public class StreetTypeHome extends EntityHome<StreetType> {

	public void setStreetTypeId(Long id) {
		setId(id);
	}

	public Long getStreetTypeId() {
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

	public StreetType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
