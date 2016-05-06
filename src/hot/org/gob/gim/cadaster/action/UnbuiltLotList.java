package org.gob.gim.cadaster.action;

import java.util.Arrays;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cadaster.model.PropertyUse;
import ec.gob.gim.cadaster.model.UnbuiltLot;
import ec.gob.gim.common.model.FiscalPeriod;

@Name("unbuiltLotList")
@Scope(ScopeType.CONVERSATION)
public class UnbuiltLotList extends EntityQuery<UnbuiltLot> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7207776049590704484L;

	//private static final String EJBQL = "select unbuiltLot from UnbuiltLot unbuiltLot";
	private static final String EJBQL = "select unbuiltLot from UnbuiltLot unbuiltLot "
			+ "LEFT JOIN unbuiltLot.fiscalPeriod fiscalPeriod "
			+ "LEFT JOIN unbuiltLot.property property "
			+ "LEFT JOIN property.currentDomain currentDomain "
			+ "LEFT JOIN currentDomain.resident resident ";

	private static final String[] RESTRICTIONS = {"fiscalPeriod = #{unbuiltLotList.fiscalPeriod}",
												  "lower(resident.identificationNumber) like lower(concat('%',#{unbuiltLotList.resident},'%')) or lower(resident.name) like lower(concat('%',:el2,'%'))",
												  "lower(property.cadastralCode) like lower(concat('%',#{unbuiltLotList.cadastralCode},'%')) or lower(property.previousCadastralCode) like lower(concat('%',:el3,'%'))",
												  "unbuiltLot.propertyUse = #{unbuiltLotList.propertyUse}",};

	private UnbuiltLot unbuiltLot= new UnbuiltLot();
	

	@Out(scope=ScopeType.SESSION, required=false)
	@In(scope=ScopeType.SESSION, required=false)
	private FiscalPeriod fiscalPeriod;
	
	private PropertyUse propertyUse;
	
	private String cadastralCode;
	private String resident;

	public UnbuiltLotList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}
	
	public void cleanFiscalPeriod(){
		setFiscalPeriod(null);
	}

	public String getResident() {		
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

	public UnbuiltLot getUnbuiltLot() {
		return unbuiltLot;
	}

	public void setUnbuiltLot(UnbuiltLot unbuiltLot) {
		this.unbuiltLot = unbuiltLot;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public PropertyUse getPropertyUse() {
		return propertyUse;
	}

	public void setPropertyUse(PropertyUse propertyUse) {
		this.propertyUse = propertyUse;
	}
}
