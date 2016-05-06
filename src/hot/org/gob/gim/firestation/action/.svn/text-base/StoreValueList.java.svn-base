package org.gob.gim.firestation.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.firestation.model.StoreValue;

@Name("storeValueList")
public class StoreValueList extends EntityQuery<StoreValue> {

	private static final String EJBQL = "select storeValue from StoreValue storeValue";

	private static final String[] RESTRICTIONS = {"lower(storeValue.name) like lower(concat(#{storeValueList.criteria},'%'))",
												"lower(groupstores.name) like lower(concat(#{storeValueList.criteria},'%'))"};

	private StoreValue storeValue = new StoreValue();
	
	private String criteria;

	public StoreValueList() {
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
	
	public StoreValue getStoreValue() {
		return storeValue;
	}
}
