package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("lotPositionList")
public class LotPositionList extends EntityQuery<LotPosition> {

	private static final String EJBQL = "select lotPosition from LotPosition lotPosition";

	private static final String[] RESTRICTIONS = {"lower(lotPosition.name) like lower(concat(#{lotPositionList.lotPosition.name},'%'))",};

	private LotPosition lotPosition = new LotPosition();

	public LotPositionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public LotPosition getLotPosition() {
		return lotPosition;
	}
}
