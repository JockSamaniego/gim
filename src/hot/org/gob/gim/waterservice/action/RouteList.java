package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("routeList")
public class RouteList extends EntityQuery<Route> {

	private String criteria;
	private static final String EJBQL = "select route from Route route";

	private static final String[] RESTRICTIONS = {"lower(route.name) like lower(concat(#{routeList.criteria},'%'))",};


	public RouteList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrderColumn("route.number");
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getCriteria() {
		return criteria;
	}
	
}
