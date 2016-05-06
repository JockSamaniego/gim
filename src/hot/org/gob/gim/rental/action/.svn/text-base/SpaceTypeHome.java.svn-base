package org.gob.gim.rental.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.rental.model.SpaceType;

@Name("spaceTypeHome")
public class SpaceTypeHome extends EntityHome<SpaceType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public void setSpaceTypeId(Long id) {
		setId(id);
	}

	public Long getSpaceTypeId() {
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

	public SpaceType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
