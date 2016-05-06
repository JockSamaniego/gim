package org.gob.gim.market.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.market.model.StandType;

@Name("standTypeList")
public class StandTypeList extends EntityQuery<StandType> {

	private static final String EJBQL = "select standType from StandType standType";

	private static final String[] RESTRICTIONS = { "lower(standType.name) like lower(concat(#{standTypeList.criteria},'%'))", };

	private String criteria;

	public StandTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}
}
