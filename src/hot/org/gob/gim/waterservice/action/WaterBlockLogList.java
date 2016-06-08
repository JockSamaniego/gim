package org.gob.gim.waterservice.action;

import java.util.Arrays;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.waterservice.model.WaterBlockLog;

@Name("waterBlockLogList")
@Scope(ScopeType.CONVERSATION)
public class WaterBlockLogList extends EntityQuery<WaterBlockLog>{
	
	private static final long serialVersionUID = 6923580661828073640L;

	private static final String EJBQL = "select waterBlockLog from WaterBlockLog waterBlockLog ";

	private static final String[] RESTRICTIONS = {};

	private WaterBlockLog waterBlockLog = new WaterBlockLog();

	public WaterBlockLogList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public WaterBlockLog getWaterBlockLog() {
		return waterBlockLog;
	}

	public void setWaterBlockLog(WaterBlockLog waterBlockLog) {
		this.waterBlockLog = waterBlockLog;
	}
	
	
}
