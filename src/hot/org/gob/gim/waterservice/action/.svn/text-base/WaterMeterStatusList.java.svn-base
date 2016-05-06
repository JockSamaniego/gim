package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("waterMeterStatusList")
public class WaterMeterStatusList extends EntityQuery<WaterMeterStatus> {

	private static final String EJBQL = "select waterMeterStatus from WaterMeterStatus waterMeterStatus";

	private static final String[] RESTRICTIONS = {"lower(waterMeterStatus.name) like lower(concat(#{waterMeterStatusList.waterMeterStatus.name},'%'))",};

	private WaterMeterStatus waterMeterStatus = new WaterMeterStatus();

	public WaterMeterStatusList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public WaterMeterStatus getWaterMeterStatus() {
		return waterMeterStatus;
	}
}
