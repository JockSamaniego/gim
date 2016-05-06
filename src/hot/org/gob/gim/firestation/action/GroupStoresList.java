package org.gob.gim.firestation.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.firestation.model.GroupStores;

@Name("groupStoresList")
public class GroupStoresList extends EntityQuery<GroupStores> {

	private static final String EJBQL = "select groupStores from GropuStores groupStores";

	
	
	
	private static final String[] RESTRICTIONS = {"lower(groupStores.name) like lower(concat(#{groupStoresList.criteria},'%'))",};

	private String criteria;

	public GroupStoresList() {
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

	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}
}
