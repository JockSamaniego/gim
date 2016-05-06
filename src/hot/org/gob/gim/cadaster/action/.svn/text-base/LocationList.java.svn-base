package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("locationList")
public class LocationList extends EntityQuery<Location> {

	private static final String EJBQL = "select location from Location location";

	private static final String[] RESTRICTIONS = {"lower(location.houseNumber) like lower(concat(#{locationList.location.houseNumber},'%'))",};

	private Location location = new Location();

	public LocationList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Location getLocation() {
		return location;
	}
}
