package org.gob.gim.revenue.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.revenue.model.SolvencyHistory;

@Name("solvencyHistoryList")
public class SolvencyHistoryList extends EntityQuery<SolvencyHistory> {

	private static final long serialVersionUID = 1L;

	private String criteria;

	private static final String EJBQL = "select sh from SolvencyHistory sh "
			+ "left join fetch sh.resident resident " 
			+ "left join fetch sh.user user ";

	private static final String[] RESTRICTIONS = {
			"lower(resident.identificationNumber) like lower(concat(#{solvencyHistoryList.criteria},'%'))",
			"lower(user.identificationNumber) like lower(concat(#{solvencyHistoryList.criteria},'%'))",
			"lower(resident.name) like lower(concat(#{solvencyHistoryList.criteria},'%'))",
			"lower(user.name) like lower(concat(#{solvencyHistoryList.criteria},'%'))",};

	public SolvencyHistoryList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setRestrictionLogicOperator("or");
		setMaxResults(25);

		setOrderColumn("sh.id");
		setOrderDirection("desc");

	}
	
	/**
	 * @return the criteria
	 */
	public String getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

}
