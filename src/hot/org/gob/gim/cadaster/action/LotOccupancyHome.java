package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("lotOccupancyHome")
public class LotOccupancyHome extends EntityHome<LotOccupancy> {

	public void setLotOccupancyId(Long id) {
		setId(id);
	}

	public Long getLotOccupancyId() {
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

	public LotOccupancy getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
