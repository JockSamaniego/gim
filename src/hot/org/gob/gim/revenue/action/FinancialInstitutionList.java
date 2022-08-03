package org.gob.gim.revenue.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.revenue.model.FinancialInstitution;

@Name("financialInstitutionList")
public class FinancialInstitutionList extends EntityQuery<FinancialInstitution> {

	private static final long serialVersionUID = 4724358314849069961L;

	private static final String EJBQL = "select financialInstitution from FinancialInstitution financialInstitution ";

	private static final String[] RESTRICTIONS = {
			"lower(financialInstitution.name) like lower(concat('%',#{financialInstitutionList.name},'%'))",};

	private FinancialInstitution financialInstitution = new FinancialInstitution();
	
	private String name;

	public FinancialInstitutionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder(name);
		setMaxResults(25);
	}

	public void setFinancialInstitution(FinancialInstitution financialInstitution) {
		this.financialInstitution = financialInstitution;
	}

	public FinancialInstitution getFinancialInstitution() {
		return financialInstitution;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
