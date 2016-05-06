package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("territorialDivisionTypeList")
public class TerritorialDivisionTypeList
		extends
			EntityQuery<TerritorialDivisionType> {

	private static final String EJBQL = "select territorialDivisionType from TerritorialDivisionType territorialDivisionType";

	private static final String[] RESTRICTIONS = {"lower(territorialDivisionType.name) like lower(concat(#{territorialDivisionTypeList.territorialDivisionType.name},'%'))",};

	private TerritorialDivisionType territorialDivisionType = new TerritorialDivisionType();

	public TerritorialDivisionTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public TerritorialDivisionType getTerritorialDivisionType() {
		return territorialDivisionType;
	}
}
