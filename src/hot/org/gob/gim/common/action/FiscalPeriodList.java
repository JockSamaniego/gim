package org.gob.gim.common.action;

import ec.gob.gim.common.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("fiscalPeriodList")
public class FiscalPeriodList extends EntityQuery<FiscalPeriod> {

	private static final String EJBQL = "select fiscalPeriod from FiscalPeriod fiscalPeriod";

	private static final String[] RESTRICTIONS = {"lower(fiscalPeriod.name) like lower(concat(#{fiscalPeriodList.fiscalPeriod.name},'%'))",};

	private FiscalPeriod fiscalPeriod = new FiscalPeriod();

	public FiscalPeriodList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}
}
