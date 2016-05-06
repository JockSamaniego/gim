package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("streetList")
public class StreetList extends EntityQuery<Street> {

	private static final String EJBQL = "select street from Street street";

	private static final String[] RESTRICTIONS = {			
			"lower(street.name) like lower(concat(#{streetList.street.name},'%'))",
			"lower(street.place.name) like lower(concat(#{streetList.place.name},'%'))",};

	private Street street = new Street();
	
	private Place place = new Place();

	public StreetList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("street.name");
		setOrderDirection("asc");
		setMaxResults(25);
	}

	public Street getStreet() {
		return street;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}
}
