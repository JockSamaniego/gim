package org.gob.gim.waterservice.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.waterservice.model.Budget;

@Name("budgetList")
public class BudgetList extends EntityQuery<Budget> {

	private static final String EJBQL = "select budget from Budget budget left join fetch budget.resident resident";

	private static final String[] RESTRICTIONS = {
			"lower(resident.identificationNumber) like lower(concat(#{budgetList.criteriaIdentificationNumber},'%'))",
			"lower(resident.name) like lower(concat(#{budgetList.criteriaName},'%'))",
			"budget.code = #{budgetList.criteriaCode}"};

	String criteriaIdentificationNumber;
	String criteriaName;
	Integer criteriaCode;

	public BudgetList() {

		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		
		setOrderColumn("budget.id");
        setOrderDirection("desc");
	}

	public String getCriteriaIdentificationNumber() {
		return criteriaIdentificationNumber;
	}

	public void setCriteriaIdentificationNumber(
			String criteriaIdentificationNumber) {
		this.criteriaIdentificationNumber = criteriaIdentificationNumber;
	}

	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public Integer getCriteriaCode() {
		return criteriaCode;
	}

	public void setCriteriaCode(Integer criteriaCode) {
		this.criteriaCode = criteriaCode;
	}

}
