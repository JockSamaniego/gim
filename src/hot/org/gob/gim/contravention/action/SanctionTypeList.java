package org.gob.gim.contravention.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.contravention.model.SanctionType;

@Name("sanctionTypeList")
public class SanctionTypeList extends EntityQuery<SanctionType> {

	private static final String EJBQL = "select sanctionType from SanctionType sanctionType";

	private static final String[] RESTRICTIONS = {"lower(sanctionType.name) like lower(concat(#{sanctionTypeList.sanctionType.name},'%'))",};

	private SanctionType sanctionType = new SanctionType();

	public SanctionTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SanctionType getSanctionType() {
		return sanctionType;
	}
}