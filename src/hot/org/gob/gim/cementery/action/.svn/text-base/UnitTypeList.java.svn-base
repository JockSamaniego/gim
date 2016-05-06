package org.gob.gim.cementery.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cementery.model.UnitType;

@Name("unitTypeList")
public class UnitTypeList extends EntityQuery<UnitType> {

	private static final long serialVersionUID = 9032915273036977524L;

	private static final String EJBQL = "select unitType from UnitType unitType";

	private static final String[] RESTRICTIONS = {
		"(lower(unitType.name) like lower(concat(#{unitType.criteria},'%')))"		
	};

	private String criteria;
	private UnitType unitType = new UnitType();

	public UnitTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(10);
	}

	public UnitType getUnitType() {
		return unitType;
	}

	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

}
