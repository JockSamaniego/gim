package org.gob.gim.waterservice.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.waterservice.model.BudgetEntry;

@Name("budgetEntryList")
public class BudgetEntryList extends EntityQuery<BudgetEntry> {

	private static final String EJBQL = "select budgetEntry from BudgetEntry budgetEntry " +
										"left join fetch budgetEntry.entry entry " +
										"left join fetch budgetEntry.budgetEntryType budgetEntryType ";

	private static final String[] RESTRICTIONS = {
			"budgetEntryType.name = #{budgetEntryList.budgetEntryType}",
			"lower(budgetEntry.name) like lower(concat('%',#{budgetEntryList.criteria},'%'))" };

	private String criteria;
	
	private String budgetEntryType;

	public BudgetEntryList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("budgetEntry.name");
		setMaxResults(25);
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getBudgetEntryType() {
		return budgetEntryType;
	}

	public void setBudgetEntryType(String budgetEntryType) {
		this.budgetEntryType = budgetEntryType;
	}

}
