package org.gob.gim.revenue.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.EntryDefinition;

@Name("entryDefinitionHome")
public class EntryDefinitionHome extends EntityHome<EntryDefinition> {

	private static final long serialVersionUID = 1L;

	@In(create = true)
	EntryHome entryHome;
	
	@Logger
	Log logger;

	public void setEntryDefinitionId(Long id) {
		setId(id);
	}

	public Long getEntryDefinitionId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Entry entry = entryHome.getDefinedInstance();
		if (entry != null) {
			getInstance().setEntry(entry);
		}
	}

	public boolean isWired() {
		return true;
	}

	public EntryDefinition getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
}
