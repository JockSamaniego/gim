package org.gob.gim.cementery.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cementery.model.Unit;

@Name("unitList")
public class UnitList extends EntityQuery<Unit> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select unit from Unit unit";

	private static final String[] RESTRICTIONS = {
	// "lower(resident.identificationNumber) like lower(concat(#{cmementeryList.criteriaIdentificationNumber},'%'))",
	// "lower(resident.name) like lower(concat(#{cementeryList.criteriaManagerName},'%'))"
	// "lower(cementery.name) like lower(concat(#{cementeryList.criteriaName},'%'))"
	};

	public UnitList() {

		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(5);
	}

}
