package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("placeList")
public class PlaceList extends EntityQuery<Place> {

	private static final String EJBQL = "select place from Place place";

	private static final String[] RESTRICTIONS = {
			"lower(place.name) like lower(concat(#{placeList.place.name},'%'))",};

	
	
	private Place place = new Place();

	public PlaceList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	

	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}
}
