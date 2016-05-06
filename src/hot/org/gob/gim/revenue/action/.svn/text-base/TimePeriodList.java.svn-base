package org.gob.gim.revenue.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.revenue.model.TimePeriod;

@Name("timePeriodList")
public class TimePeriodList extends EntityQuery<TimePeriod> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select timePeriod from TimePeriod timePeriod";

	private static final String[] RESTRICTIONS = {
			"lower(timePeriod.name) like lower(concat(#{timePeriodList.timePeriod.name},'%'))",};

	private TimePeriod timePeriod = new TimePeriod();

	public TimePeriodList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public TimePeriod getTimePeriod() {
		return timePeriod;
	}
}
