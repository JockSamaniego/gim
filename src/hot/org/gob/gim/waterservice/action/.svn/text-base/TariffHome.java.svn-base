package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tariffHome")
public class TariffHome extends EntityHome<Tariff> {

	public void setTariffId(Long id) {
		setId(id);
	}

	public Long getTariffId() {
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

	public Tariff getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
