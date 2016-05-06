package org.gob.gim.parking.action;

import java.util.Arrays;
import java.util.Date;

import ec.gob.gim.parking.model.ParkingRent;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;


@Name("parkingRentList")
public class ParkingRentList extends EntityQuery<ParkingRent> {



	/**
	 * 
	 */
	private static final long serialVersionUID = -690659007088506860L;

	private static final String EJBQL = "select parkingRent from ParkingRent parkingRent ";

	private static final String[] RESTRICTIONS = {
		"((lower(parkingRent.contract.subscriber.name) like lower(concat(#{parkingRentList.criteria},'%')))) ",
		"parkingRent.parkingLot = #{parkingRentList.parkingRent.parkingLot}",
		"parkingRent.subscriptionDate <= #{parkingRentList.start}",
		"parkingRent.subscriptionDate >= #{parkingRentList.end}",
		};
	
	private String criteria;
	
	private Date start;

	private Date end;
	

	private ParkingRent parkingRent = new ParkingRent();
	

	public ParkingRentList() {
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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public ParkingRent getParkingRent() {
		return parkingRent;
	}

	public void setParkingRent(ParkingRent parkingRent) {
		this.parkingRent = parkingRent;
	}

}
