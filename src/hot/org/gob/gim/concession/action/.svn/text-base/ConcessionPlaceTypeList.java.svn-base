package org.gob.gim.concession.action;

import ec.gob.gim.consession.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("concessionPlaceTypeList")
public class ConcessionPlaceTypeList extends EntityQuery<ConcessionPlaceType> {

	private static final String EJBQL = "select concessionPlaceType from ConcessionPlaceType concessionPlaceType";

	private static final String[] RESTRICTIONS = {
			"lower(concessionPlaceType.name) like lower(concat(#{concessionPlaceTypeList.concessionPlaceType.name},'%'))",
			"lower(concessionPlaceType.reason) like lower(concat(#{concessionPlaceTypeList.concessionPlaceType.reason},'%'))",};

	private ConcessionPlaceType concessionPlaceType = new ConcessionPlaceType();

	public ConcessionPlaceTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ConcessionPlaceType getConcessionPlaceType() {
		return concessionPlaceType;
	}
}
