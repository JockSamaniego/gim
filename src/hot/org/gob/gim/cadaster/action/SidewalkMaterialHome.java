package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("sidewalkMaterialHome")
public class SidewalkMaterialHome extends EntityHome<SidewalkMaterial> {

	public void setSidewalkMaterialId(Long id) {
		setId(id);
	}

	public Long getSidewalkMaterialId() {
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

	public SidewalkMaterial getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
