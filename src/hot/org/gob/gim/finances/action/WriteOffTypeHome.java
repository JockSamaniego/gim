package org.gob.gim.finances.action;

import ec.gob.gim.finances.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("writeOffTypeHome")
public class WriteOffTypeHome extends EntityHome<WriteOffType> {

	public void setWriteOffTypeId(Long id) {
		setId(id);
	}

	public Long getWriteOffTypeId() {
		return (Long) getId();
	}

	@Override
	protected WriteOffType createInstance() {
		WriteOffType writeOffType = new WriteOffType();
		return writeOffType;
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

	public WriteOffType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
