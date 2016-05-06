package org.gob.gim.cadaster.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cadaster.model.Neighborhood;
import ec.gob.gim.cadaster.model.Place;

@Name("neighborhoodList")
public class NeighborhoodList extends EntityQuery<Neighborhood> {

	private static final String EJBQL = "select neighborhood from Neighborhood neighborhood";

	private static final String[] RESTRICTIONS = {		
		"lower(neighborhood.name) like lower(concat(#{neighborhoodList.neighborhood.name},'%'))",
		"lower(neighborhood.place.name) like lower(concat(#{neighborhoodList.place.name},'%'))",};

	private Neighborhood neighborhood = new Neighborhood();
	private Place place = new Place();

	public NeighborhoodList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("neighborhood.name");
		setOrderDirection("asc");
		setMaxResults(25);
	}

	public Neighborhood getNeighborhood() {
		return neighborhood;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}
}
