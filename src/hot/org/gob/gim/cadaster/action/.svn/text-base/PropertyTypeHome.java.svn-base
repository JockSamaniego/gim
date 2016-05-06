package org.gob.gim.cadaster.action;

import org.gob.gim.revenue.action.EntryHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.cadaster.model.PropertyType;
import ec.gob.gim.revenue.model.Entry;

@Name("propertyTypeHome")
public class PropertyTypeHome extends EntityHome<PropertyType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	EntryHome entryHome;

	public void setPropertyTypeId(Long id) {
		setId(id);
	}

	public Long getPropertyTypeId() {
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

	public PropertyType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
