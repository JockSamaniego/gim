package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("streetMaterialHome")
public class StreetMaterialHome extends EntityHome<StreetMaterial> {

	public void setStreetMaterialId(Long id) {
		setId(id);
	}

	public Long getStreetMaterialId() {
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

	public StreetMaterial getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
