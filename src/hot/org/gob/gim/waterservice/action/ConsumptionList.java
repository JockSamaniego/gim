package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("consumptionList")
public class ConsumptionList extends EntityQuery<Consumption> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select consumption from Consumption consumption";

	private static final String[] RESTRICTIONS = {};

	private Consumption consumption = new Consumption();

	public ConsumptionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Consumption getConsumption() {
		return consumption;
	}
}
