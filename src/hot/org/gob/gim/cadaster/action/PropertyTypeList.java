package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("propertyTypeList")
public class PropertyTypeList extends EntityQuery<PropertyType> {

	private static final String EJBQL = "select propertyType from PropertyType propertyType";

	private static final String[] RESTRICTIONS = {"lower(propertyType.name) like lower(concat(#{propertyTypeList.propertyType.name},'%'))",};

	private PropertyType propertyType = new PropertyType();

	public PropertyTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}
}
