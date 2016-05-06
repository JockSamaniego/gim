package org.gob.gim.market.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.market.model.Stand;
import ec.gob.gim.market.model.StandStatus;

@Name("standList")
public class StandList extends EntityQuery<Stand> {

	private static final String EJBQL = "select stand from Stand stand";

	private static final String[] RESTRICTIONS = {
			"lower(stand.name) like lower(concat('%',#{standList.criteria},'%'))",
			"lower(stand.market.name) = lower(#{standList.market})",
			"lower(stand.standType.name) = lower(#{standList.standType})",
			"stand.standStatus = #{standList.standStatus}" };

	private String criteria;

	private String market;
	private StandStatus standStatus;
	private String standType;

	public StandList() {
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

	/*
	 * public Market getMarket() { return market; }
	 * 
	 * public void setMarket(Market market) { this.market = market; }
	 */

	public StandStatus getStandStatus() {
		return standStatus;
	}

	public void setStandStatus(StandStatus standStatus) {
		this.standStatus = standStatus;
	}

	public String getStandType() {
		return standType;
	}

	public void setStandType(String standType) {
		this.standType = standType;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

}
