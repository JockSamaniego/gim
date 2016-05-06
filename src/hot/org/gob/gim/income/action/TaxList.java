package org.gob.gim.income.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.income.model.Tax;

@Name("taxList")
public class TaxList extends EntityQuery<Tax> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select tax from Tax tax";

	private static final String[] RESTRICTIONS = {
			"lower(tax.name) like lower(concat(#{taxList.tax.name},'%'))",};

	private Tax tax = new Tax();

	public TaxList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Tax getTax() {
		return tax;
	}
}
