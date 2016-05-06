package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("dividendList")
public class DividendList extends EntityQuery<Dividend> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select dividend from Dividend dividend";

	private static final String[] RESTRICTIONS = {};

	private Dividend dividend = new Dividend();

	public DividendList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Dividend getDividend() {
		return dividend;
	}
}
