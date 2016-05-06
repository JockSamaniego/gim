package org.gob.gim.waterservice.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.waterservice.model.WaterSupplyCategory;

@Name("waterSupplyCategoryList")
public class WaterSupplyCategoryList extends EntityQuery<WaterSupplyCategory> {

	private static final String EJBQL = "select waterSupplyCategory from WaterSupplyCategory waterSupplyCategory";

	private static final String[] RESTRICTIONS = { "lower(waterSupplyCategory.name) like lower(concat(#{waterSupplyCategoryList.criteria},'%'))", };

	// private WaterSupplyCategory waterSupplyCategory = new
	// WaterSupplyCategory();
	String criteria;

	public WaterSupplyCategoryList() {
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

}
