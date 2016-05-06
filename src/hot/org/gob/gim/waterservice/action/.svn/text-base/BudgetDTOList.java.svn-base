package org.gob.gim.waterservice.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.waterservice.model.BudgetDTO;

@Name("budgetDTOList")
public class BudgetDTOList extends EntityQuery<BudgetDTO> {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select new ec.gob.gim.waterservice.model.BudgetDTO(budget.id, budget.resident.id,budget.resident.name, budget.code, budget.year," +
										"budget.date, budget.inspectionDate, budget.isServiceOrderGenerate, budget.cadastralCode, budget.cadastralCode," +
										"budget.total) from Budget budget ";
										
	private static final String[] RESTRICTIONS = {
			"lower(budget.resident.identificationNumber) like lower(concat(#{budgetDTOList.criteriaIdentificationNumber},'%'))",
			"lower(budget.resident.name) like lower(concat(#{budgetDTOList.criteriaName},'%'))",
			"budget.code = #{budgetDTOList.criteriaCode}"};

	private String criteriaIdentificationNumber;
	private String criteriaName;
	private Integer criteriaCode;

	public BudgetDTOList() {
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
