package org.gob.gim.firestation.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.firestation.model.GroupStores;

@Name("groupStoresHome")
public class GroupStoresHome extends EntityHome<GroupStores> {

	public void setGroupStoresId(Long id) {
		setId(id);
	}

	public Long getGroupStoresId() {
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

	public GroupStores getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
