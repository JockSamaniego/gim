package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import ec.gob.gim.common.model.FiscalPeriod;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("buildingMaterialValueList")
public class BuildingMaterialValueList extends EntityQuery<BuildingMaterialValue> {

	private static final String EJBQL = "select buildingMaterialValue from BuildingMaterialValue buildingMaterialValue";

	private static final String[] RESTRICTIONS = {"buildingMaterialValue.fiscalPeriod = #{buildingMaterialValueList.fiscalPeriod}",
		"buildingMaterialValue.structureMaterial = #{buildingMaterialValueList.structureMaterial}",};

	private BuildingMaterialValue buildingMaterialValue = new BuildingMaterialValue();

	@Out(scope=ScopeType.SESSION, required=false)
	@In(scope=ScopeType.SESSION, required=false)
	private FiscalPeriod fiscalPeriod;
	
	private StructureMaterial structureMaterial;
	
	public BuildingMaterialValueList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}
	
	public void cleanFiscalPeriod(){
		setFiscalPeriod(null);
	}

	public void setBuildingMaterialValue(BuildingMaterialValue buildingMaterialValue) {
		this.buildingMaterialValue = buildingMaterialValue;
	}

	public BuildingMaterialValue getBuildingMaterialValue() {
		return buildingMaterialValue;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {		
		this.fiscalPeriod = fiscalPeriod;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setStructureMaterial(StructureMaterial structureMaterial) {		
		this.structureMaterial = structureMaterial;
	}

	public StructureMaterial getStructureMaterial() {
		return structureMaterial;
	}

}
