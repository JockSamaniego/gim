package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("lotOccupancyList")
public class LotOccupancyList extends EntityQuery<LotOccupancy> {

	private static final String EJBQL = "select lotOccupancy from LotOccupancy lotOccupancy";

	private static final String[] RESTRICTIONS = {"lower(lotOccupancy.name) like lower(concat(#{lotOccupancyList.lotOccupancy.name},'%'))",};

	private LotOccupancy lotOccupancy = new LotOccupancy();

	public LotOccupancyList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public LotOccupancy getLotOccupancy() {
		return lotOccupancy;
	}
}
