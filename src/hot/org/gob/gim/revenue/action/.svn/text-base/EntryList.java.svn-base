package org.gob.gim.revenue.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.revenue.model.Entry;

@Name("entryList")
public class EntryList extends EntityQuery<Entry> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String criteria;

	//private static final String EJBQL = "select entry from Entry entry left join fetch entry.parent";
	private static final String EJBQL = "select entry from Entry entry left join fetch entry.timePeriod left join entry.account ac";
	
	private static final String[] RESTRICTIONS= {
		"lower(ac.accountCode) like lower(concat(#{entryList.criteria},'%'))",
		"lower(entry.code) like lower(concat(#{entryList.criteria},'%'))",
		"lower(entry.previousCode) like lower(concat(#{entryList.criteria},'%'))",
		"lower(entry.name) like lower(concat(#{entryList.criteria},'%'))",
		"lower(entry.completeName) like lower(concat(#{entryList.criteria},'%'))"};

	
	public EntryList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setRestrictionLogicOperator("or");
		setMaxResults(25);
		setOrder("entry.id");
	}
	
	/*@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}*/

	/**
	 * @return the criteria
	 */
	public String getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria the criteria to set
	 */
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	
}
