package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("sidewalkMaterialList")
public class SidewalkMaterialList extends EntityQuery<SidewalkMaterial> {

	private static final String EJBQL = "select sidewalkMaterial from SidewalkMaterial sidewalkMaterial";

	private static final String[] RESTRICTIONS = {
			"lower(sidewalkMaterial.code) like lower(concat(#{sidewalkMaterialList.sidewalkMaterial.code},'%'))",
			"lower(sidewalkMaterial.name) like lower(concat(#{sidewalkMaterialList.sidewalkMaterial.name},'%'))",};

	private SidewalkMaterial sidewalkMaterial = new SidewalkMaterial();

	public SidewalkMaterialList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public SidewalkMaterial getSidewalkMaterial() {
		return sidewalkMaterial;
	}
}
