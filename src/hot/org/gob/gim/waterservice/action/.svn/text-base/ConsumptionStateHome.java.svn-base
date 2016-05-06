package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("consumptionStateHome")
public class ConsumptionStateHome extends EntityHome<ConsumptionState> {

	public void setConsumptionStateId(Long id) {
		setId(id);
	}

	public Long getConsumptionStateId() {
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

	public ConsumptionState getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
