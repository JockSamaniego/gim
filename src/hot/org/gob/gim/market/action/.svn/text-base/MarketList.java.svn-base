package org.gob.gim.market.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.market.model.Market;

@Name("marketList")
public class MarketList extends EntityQuery<Market> {

	private static final String EJBQL = "select market from Market market";

	private static final String[] RESTRICTIONS = {
			"lower(market.name) like lower(concat(#{marketList.criteria},'%'))",
			"market.isActive = #{marketList.isActive}" };

	private String criteria;
	private Boolean isActive = Boolean.TRUE;

	public MarketList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("market.name");
		setMaxResults(25);
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/*@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}*/

}
