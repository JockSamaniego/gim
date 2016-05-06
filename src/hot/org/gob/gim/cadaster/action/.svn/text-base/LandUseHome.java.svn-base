package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("landUseHome")
public class LandUseHome extends EntityHome<LandUse> {

	public void setLandUseId(Long id) {
		setId(id);
	}

	public Long getLandUseId() {
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

	public LandUse getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
