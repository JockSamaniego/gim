package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("purchaseTypeHome")
public class PurchaseTypeHome extends EntityHome<PurchaseType> {

	public void setPurchaseTypeId(Long id) {
		setId(id);
	}

	public Long getPurchaseTypeId() {
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

	public PurchaseType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
