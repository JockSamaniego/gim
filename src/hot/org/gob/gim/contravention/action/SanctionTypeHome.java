package org.gob.gim.contravention.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.contravention.model.SanctionType;

@Name("sanctionTypeHome")
public class SanctionTypeHome extends EntityHome<SanctionType> {

	public void setSanctionTypeId(Long id) {
		setId(id);
	}

	public Long getSanctionTypeId() {
		return (Long) getId();
	}

	@Override
	protected SanctionType createInstance() {
		SanctionType sanctionType = new SanctionType();
		return sanctionType;
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

	public SanctionType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}