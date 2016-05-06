package org.gob.gim.parking.action;

import java.util.Arrays;

import ec.gob.gim.parking.model.ParkingLot;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;


@Name("parkingLotList")
public class ParkingLotList extends EntityQuery<ParkingLot> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7829139376689892812L;

	private static final String EJBQL = "select parkingLot from ParkingLot parkingLot ";

	private static final String[] RESTRICTIONS = {
		"((lower(parkingLot.name) like lower(concat(#{parkingLotList.criteria},'%'))))",
		};

	private String criteria;

	private ParkingLot parkingLot = new ParkingLot();
	

	public ParkingLotList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "and";
	}

	/**
	 * @return the criteria
	 */
	public String getCriteria() {
		if (this.criteria == null){
			setCriteria("");
		}
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public void setParkingLot(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
	}

	public ParkingLot getParkingLot() {
		return parkingLot;
	}

}
