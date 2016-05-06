package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("streetMaterialList")
public class StreetMaterialList extends EntityQuery<StreetMaterial> {

	private static final String EJBQL = "select streetMaterial from StreetMaterial streetMaterial";

	private static final String[] RESTRICTIONS = {
			"lower(streetMaterial.code) like lower(concat(#{streetMaterialList.streetMaterial.code},'%'))",
			"lower(streetMaterial.name) like lower(concat(#{streetMaterialList.streetMaterial.name},'%'))",};

	private StreetMaterial streetMaterial = new StreetMaterial();

	public StreetMaterialList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public StreetMaterial getStreetMaterial() {
		return streetMaterial;
	}
}
