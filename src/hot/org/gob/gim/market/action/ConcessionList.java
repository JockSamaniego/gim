package org.gob.gim.market.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.market.model.Concession;

@Name("concessionList")
public class ConcessionList extends EntityQuery<Concession> {

	private static final String EJBQL = "select concession from Concession concession";

	private static final String[] RESTRICTIONS = {};

	private Concession concession = new Concession();

	public ConcessionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Concession getConcession() {
		return concession;
	}
}
