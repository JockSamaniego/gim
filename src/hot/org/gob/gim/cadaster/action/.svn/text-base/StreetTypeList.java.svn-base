package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("streetTypeList")
public class StreetTypeList extends EntityQuery<StreetType> {

	private static final String EJBQL = "select streetType from StreetType streetType";

	private static final String[] RESTRICTIONS = {
			"lower(streetType.code) like lower(concat(#{streetTypeList.streetType.code},'%'))",
			"lower(streetType.name) like lower(concat(#{streetTypeList.streetType.name},'%'))",};

	private StreetType streetType = new StreetType();

	public StreetTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public StreetType getStreetType() {
		return streetType;
	}
}
