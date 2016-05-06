package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("routePeriodList")
public class RoutePeriodList extends EntityQuery<RoutePeriod> {

	private static final String EJBQL = "select routePeriod from RoutePeriod routePeriod";

	private static final String[] RESTRICTIONS = {};

	private RoutePeriod routePeriod = new RoutePeriod();

	public RoutePeriodList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public RoutePeriod getRoutePeriod() {
		return routePeriod;
	}
}
