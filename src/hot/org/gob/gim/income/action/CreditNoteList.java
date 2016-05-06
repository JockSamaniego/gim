package org.gob.gim.income.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.income.model.CreditNote;

@Name("creditNoteList")
public class CreditNoteList extends EntityQuery<CreditNote> {
	
	private static final long serialVersionUID = 1L;

	private String criteria;

	private static final String EJBQL = "SELECT o FROM CreditNote o";

	private static final String[] RESTRICTIONS = {
			"lower(o.resident.identificationNumber) like lower(concat(#{creditNoteList.criteria},'%'))",
			"lower(o.resident.name) like lower(concat(#{creditNoteList.criteria},'%'))"};

	public CreditNoteList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("o.resident.name");
		setOrderDirection("asc");
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

}
