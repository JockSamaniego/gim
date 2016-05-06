package org.gob.gim.revenue.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.revenue.model.FinancialInstitution;

@Name("financialInstitutionList")
public class FinancialInstitutionList extends EntityQuery<FinancialInstitution> {

	private static final String EJBQL = "select financialInstitution from FinancialInstitution financialInstitution";

	private static final String[] RESTRICTIONS = {
			"lower(financialInstitution.name) like lower(concat('%',#{financialInstitutionList.financialInstitution.name},'%'))",};

	private FinancialInstitution financialInstitution = new FinancialInstitution();
	
	

	public FinancialInstitutionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}



	public void setFinancialInstitution(FinancialInstitution financialInstitution) {
		this.financialInstitution = financialInstitution;
	}



	public FinancialInstitution getFinancialInstitution() {
		return financialInstitution;
	}
}
