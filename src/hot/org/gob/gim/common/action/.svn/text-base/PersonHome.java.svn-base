package org.gob.gim.common.action;

import ec.gob.gim.common.model.*;
import ec.gob.gim.commercial.model.Business;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("personHome")
public class PersonHome extends EntityHome<Person> {

	public void setPersonId(Long id) {
		setId(id);
	}

	public Long getPersonId() {
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

	public Person getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Business> getManagedBusinesses() {
		return getInstance() == null ? null : new ArrayList<Business>(
				getInstance().getManagedBusinesses());
	}

}
