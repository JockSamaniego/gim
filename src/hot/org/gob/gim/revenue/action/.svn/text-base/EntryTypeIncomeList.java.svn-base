package org.gob.gim.revenue.action;

import ec.gob.gim.revenue.model.EntryTypeIncome;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("entryTypeIncomeList")
public class EntryTypeIncomeList extends EntityQuery<EntryTypeIncome> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select entryTypeIncome from EntryTypeIncome entryTypeIncome";

	private static final String[] RESTRICTIONS = {
			"lower(entryTypeIncome.name) like lower(concat(#{entryTypeIncomeList.entryTypeIncome.name},'%'))",};

	
	
	private EntryTypeIncome entryTypeIncome = new EntryTypeIncome();

	public EntryTypeIncomeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}


	public void setEntryTypeIncome(EntryTypeIncome entryTypeIncome) {
		this.entryTypeIncome = entryTypeIncome;
	}

	public EntryTypeIncome getEntryTypeIncome() {
		return entryTypeIncome;
	}
}
