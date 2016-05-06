package org.gob.gim.commercial.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.commercial.model.TouristLicenseItem;

@Name("touristLicenseItemHome")
public class TouristLicenseItemHome extends EntityHome<TouristLicenseItem> {

	

	public void setTouristLicenseItemId(Long id) {
		setId(id);
	}

	public Long getTouristLicenseItemId() {
		return (Long) getId();
	}

	@Override
	protected TouristLicenseItem createInstance() {
		TouristLicenseItem touristLicenseItem = new TouristLicenseItem();
		return touristLicenseItem;
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

	public TouristLicenseItem getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}