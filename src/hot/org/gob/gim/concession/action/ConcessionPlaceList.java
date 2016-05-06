package org.gob.gim.concession.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.consession.model.ConcessionPlace;


@Name("concessionPlaceList")
public class ConcessionPlaceList extends EntityQuery<ConcessionPlace> {

	private static final String EJBQL = "select concessionPlace from ConcessionPlace concessionPlace";

	private static final String[] RESTRICTIONS = {"lower(concessionPlace.name) like lower(concat(#{concessionPlaceList.concessionPlace.name},'%'))",};

	private ConcessionPlace concessionPlace = new ConcessionPlace();

	public ConcessionPlaceList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ConcessionPlace getConcessionPlace() {
		return concessionPlace;
	}
}
