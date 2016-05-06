package org.gob.gim.waterservice.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.waterservice.model.ConsumptionState;

@Name("consumptionStateList")
public class ConsumptionStateList extends EntityQuery<ConsumptionState> {

	private static final String EJBQL = "select consumptionState from ConsumptionState consumptionState";

	private static final String[] RESTRICTIONS = {"lower(consumptionState.name) like lower(concat(#{consumptionStateList.consumptionState.name},'%'))",};

	private ConsumptionState consumptionState = new ConsumptionState();

	public ConsumptionStateList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ConsumptionState getConsumptionState() {
		return consumptionState;
	}
		
}
