package org.gob.gim.cadaster.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cadaster.model.LandUse;

@Name("landUseList")
public class LandUseList extends EntityQuery<LandUse> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select landUse from LandUse landUse";

	private static final String[] RESTRICTIONS = {
		"lower(landUse.name) like lower(concat(#{landUseList.landUse.name},'%'))",
	};

	private LandUse landUse = new LandUse();

	public LandUseList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public LandUse getLandUse() {
		return landUse;
	}
}
