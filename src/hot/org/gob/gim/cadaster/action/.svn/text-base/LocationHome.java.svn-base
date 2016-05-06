package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("locationHome")
public class LocationHome extends EntityHome<Location> {

	@In(create = true)
	BlockLimitHome blockLimitHome;
	@In(create = true)
	NeighborhoodHome neighborhoodHome;

	public void setLocationId(Long id) {
		setId(id);
	}

	public Long getLocationId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		BlockLimit mainBlockLimit = blockLimitHome.getDefinedInstance();
		if (mainBlockLimit != null) {
			getInstance().setMainBlockLimit(mainBlockLimit);
		}
		Neighborhood neighborhood = neighborhoodHome.getDefinedInstance();
		if (neighborhood != null) {
			getInstance().setNeighborhood(neighborhood);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Location getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
