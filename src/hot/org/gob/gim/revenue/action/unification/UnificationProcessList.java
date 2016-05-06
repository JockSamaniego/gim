package org.gob.gim.revenue.action.unification;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.waterservice.model.Budget;

@Name("unificationProcessList")
public class UnificationProcessList extends EntityQuery<Budget> {

	private static final String EJBQL = "select up from UnificationProcess up left join up.performChange resident";

	private static final String[] RESTRICTIONS = {
			"lower(resident.identificationNumber) like lower(concat(#{unificationProcessList.criteria},'%'))",
			"lower(resident.name) like lower(concat(#{unificationProcessList.criteria},'%'))"};

	String criteria;	

	public UnificationProcessList() {

		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		
		//setOrderColumn("budget.id");
        //setOrderDirection("desc");
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
	
}
