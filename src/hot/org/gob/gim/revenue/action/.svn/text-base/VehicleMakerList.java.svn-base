package org.gob.gim.revenue.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.revenue.model.adjunct.detail.VehicleMaker;

@Name("vehicleMakerList")
public class VehicleMakerList extends EntityQuery<VehicleMaker> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select vehicleMaker from VehicleMaker vehicleMaker";

	private static final String[] RESTRICTIONS = {
			"lower(vehicleMaker.name) like lower(concat(#{vehicleMakerList.vehicleMaker.name},'%'))",};

	//private TimePeriod timePeriod = new TimePeriod();
	private VehicleMaker vehicleMaker = new VehicleMaker();

	public VehicleMakerList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public VehicleMaker getVehicleMaker() {
		return vehicleMaker;
	}

	public void setVehicleMaker(VehicleMaker vehicleMaker) {
		this.vehicleMaker = vehicleMaker;
	}

}
