package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("fenceMaterialList")
public class FenceMaterialList extends EntityQuery<FenceMaterial> {

	private static final String EJBQL = "select fenceMaterial from FenceMaterial fenceMaterial";

	private static final String[] RESTRICTIONS = {"lower(fenceMaterial.name) like lower(concat(#{fenceMaterialList.fenceMaterial.name},'%'))",};

	private FenceMaterial fenceMaterial = new FenceMaterial();

	public FenceMaterialList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public FenceMaterial getFenceMaterial() {
		return fenceMaterial;
	}
}
