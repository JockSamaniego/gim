package org.gob.gim.waterservice.action;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.waterservice.model.ConsumptionState;
import ec.gob.gim.waterservice.model.WaterMeterStatus;

@Name("waterMeterStatusHome")
public class WaterMeterStatusHome extends EntityHome<WaterMeterStatus> {

	public void setWaterMeterStatusId(Long id) {
		setId(id);
	}

	public Long getWaterMeterStatusId() {
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

	public WaterMeterStatus getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

		
}
