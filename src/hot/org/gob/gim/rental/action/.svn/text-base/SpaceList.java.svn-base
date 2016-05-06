package org.gob.gim.rental.action;

import java.util.Arrays;
import java.util.Date;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.rental.model.Space;
import ec.gob.gim.rental.model.SpaceStatus;

@Name("spaceList")
public class SpaceList extends EntityQuery<Space> {

	private static final String EJBQL = "select space from Space space left join fetch space.currentContract contract";

	private static final String[] RESTRICTIONS = {		
		"lower(space.address) like lower(concat('%',#{spaceList.space.address},'%'))",
		"lower(space.spaceType.name) = lower(#{spaceList.spaceType})",
		"contract.expirationDate <= #{spaceList.expirationDate}",
		"space.spaceStatus = #{spaceList.spaceStatus}",
		"(lower(contract.subscriber.name) like lower(concat('%',#{spaceList.residentCriteria},'%')) or lower(contract.subscriber.identificationNumber) like lower(concat('%',:el5,'%')))",
		};

	private Space space = new Space();
	
	private SpaceStatus spaceStatus;
	
	private String spaceType;
	
	private Date expirationDate;
	
	private String residentCriteria;

	public SpaceList() {
		setEjbql(EJBQL);		
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("contract.expirationDate");
		setOrderDirection("asc");
		setMaxResults(25);
	}


	public void setSpace(Space space) {
		this.space = space;
	}


	public Space getSpace() {
		return space;
	}

	public void setExpirationDate(Date expirationDate) {		
		this.expirationDate = expirationDate;
	}


	public Date getExpirationDate() {
		return expirationDate;
	}


	public void setResidentCriteria(String residentCriteria) {
		this.residentCriteria = residentCriteria;
	}


	public String getResidentCriteria() {
		return residentCriteria;
	}


	public void setSpaceType(String spaceType) {
		this.spaceType = spaceType;
	}


	public String getSpaceType() {
		return spaceType;
	}


	public void setSpaceStatus(SpaceStatus spaceStatus) {
		this.spaceStatus = spaceStatus;
	}


	public SpaceStatus getSpaceStatus() {
		return spaceStatus;
	}
	
}
