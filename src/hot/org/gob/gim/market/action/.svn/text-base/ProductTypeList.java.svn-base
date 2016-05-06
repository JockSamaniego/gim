package org.gob.gim.market.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.market.model.ProductType;

@Name("productTypeList")
public class ProductTypeList extends EntityQuery<ProductType> {

	private static final String EJBQL = "select productType from ProductType productType";

	private static final String[] RESTRICTIONS = {"lower(productType.name) like lower(concat(#{productTypeList.criteria},'%'))",};

	private String criteria;

	public ProductTypeList() {
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
