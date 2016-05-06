package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("fenceMaterialHome")
public class FenceMaterialHome extends EntityHome<FenceMaterial> {

	public void setFenceMaterialId(Long id) {
		setId(id);
	}

	public Long getFenceMaterialId() {
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

	public FenceMaterial getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
