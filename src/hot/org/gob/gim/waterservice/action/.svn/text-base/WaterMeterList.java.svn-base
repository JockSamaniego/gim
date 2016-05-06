package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("waterMeterList")
public class WaterMeterList extends EntityQuery<WaterMeter> {

	private static final String EJBQL = "select waterMeter from WaterMeter waterMeter";

	private static final String[] RESTRICTIONS = {"lower(waterMeter.serial) like lower(concat(#{waterMeterList.waterMeter.serial},'%'))",};

	private WaterMeter waterMeter = new WaterMeter();

	public WaterMeterList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public WaterMeter getWaterMeter() {
		return waterMeter;
	}
}
