package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("waterMeterHome")
public class WaterMeterHome extends EntityHome<WaterMeter> {

	@In(create = true)
	WaterMeterStatusHome waterMeterStatusHome;

	public void setWaterMeterId(Long id) {
		setId(id);
	}

	public Long getWaterMeterId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		WaterMeterStatus waterMeterStatus = waterMeterStatusHome.getDefinedInstance();
		if (waterMeterStatus != null) {
			getInstance().setWaterMeterStatus(waterMeterStatus);
		}
	}

	public boolean isWired() {
		return true;
	}

	public WaterMeter getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
