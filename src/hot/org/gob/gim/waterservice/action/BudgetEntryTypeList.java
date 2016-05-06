package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("budgetEntryTypeList")
public class BudgetEntryTypeList extends EntityQuery<BudgetEntryType> {

	private String criteria;
	
	private static final String EJBQL = "select budgetEntryType from BudgetEntryType budgetEntryType";

	private static final String[] RESTRICTIONS = {"lower(budgetEntryType.name) like lower(concat(#{budgetEntryTypeList.criteria},'%'))",};

	public BudgetEntryTypeList() {
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
}
