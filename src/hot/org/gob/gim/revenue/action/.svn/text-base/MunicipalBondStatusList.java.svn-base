package org.gob.gim.revenue.action;

import ec.gob.gim.revenue.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("municipalBondStatusList")
public class MunicipalBondStatusList extends EntityQuery<MunicipalBondStatus> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select municipalBondStatus from MunicipalBondStatus municipalBondStatus";

	private static final String[] RESTRICTIONS = {
			"lower(municipalBondStatus.name) like lower(concat(#{municipalBondStatusList.municipalBondStatus.name},'%'))",};

	private MunicipalBondStatus municipalBondStatus = new MunicipalBondStatus();

	public MunicipalBondStatusList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}
}
