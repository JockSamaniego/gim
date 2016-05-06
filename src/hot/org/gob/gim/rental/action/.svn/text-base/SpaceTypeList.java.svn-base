package org.gob.gim.rental.action;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.rental.model.Space;
import ec.gob.gim.rental.model.SpaceType;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;


import java.util.Arrays;
import java.util.Date;

@Name("spaceTypeList")
public class SpaceTypeList extends EntityQuery<SpaceType> {

	private static final String EJBQL = "select spaceType from SpaceType spaceType";

	private static final String[] RESTRICTIONS = {		
		"lower(spaceType.name) like lower(concat('%',#{spaceTypeList.spaceType.name},'%'))"};

	private SpaceType spaceType = new SpaceType();
	
	public SpaceTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));		
		setMaxResults(25);
	}

	public void setSpaceType(SpaceType spaceType) {
		this.spaceType = spaceType;
	}

	public SpaceType getSpaceType() {
		return spaceType;
	}


	
	
}
