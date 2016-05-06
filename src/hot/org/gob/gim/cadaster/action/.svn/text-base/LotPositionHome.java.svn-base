package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("lotPositionHome")
public class LotPositionHome extends EntityHome<LotPosition> {

	public void setLotPositionId(Long id) {
		setId(id);
	}

	public Long getLotPositionId() {
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

	public LotPosition getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
