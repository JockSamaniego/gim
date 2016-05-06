package org.gob.gim.waterservice.action;

import java.util.Arrays;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.waterservice.model.WaterSupply;

@Name("waterSupplyList")
@Scope(ScopeType.CONVERSATION)
public class WaterSupplyList extends EntityQuery<WaterSupply> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6923580661828073640L;

	private static final String EJBQL = "select distinct waterSupply from WaterSupply waterSupply "
			+ "left join fetch waterSupply.serviceOwner serviceOwner "
			//+ "left join fetch serviceOwner.currentAddress address "
			//+ "left join fetch waterSupply.contract contract "
			//+ "left join fetch contract.subscriber subscriber "
			//+ "left join fetch contract.contractType contractType "
			//+ "left join fetch contractType.entry "
			+ "left join fetch waterSupply.street street "
			+ "left join fetch street.place place "
			+ "left join fetch waterSupply.route route "
			+ "left join fetch waterSupply.waterSupplyCategory "
			//+ "left join fetch waterSupply.property property "
			+ "left outer join waterSupply.waterMeters waterMeter";

	private static final String[] RESTRICTIONS = {
			"lower(serviceOwner.identificationNumber) like lower(concat(#{waterSupplyList.criteriaIdentificationNumber},'%'))",
			"lower(serviceOwner.name) like lower(concat(#{waterSupplyList.criteriaName},'%'))",
			"trim(str(waterSupply.serviceNumber)) like concat(#{waterSupplyList.criteriaService},'%')",
			//"lower(property.cadastralCode) like lower(concat(#{waterSupplyList.criteriaCadastralCode},'%'))",
			"lower(waterMeter.serial) like lower(concat(#{waterSupplyList.criteriaWaterMeter},'%'))"};

	private WaterSupply waterSupply = new WaterSupply();

	private String criteriaIdentificationNumber;
	private String criteriaName;
	private String criteriaService;
	private String criteriaCadastralCode;
	private String criteriaWaterMeter;
	private Boolean isActive = new Boolean(true);

	public WaterSupplyList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public WaterSupply getWaterSupply() {
		return waterSupply;
	}

	public String getCriteriaIdentificationNumber() {
		return criteriaIdentificationNumber;
	}

	public void setCriteriaIdentificationNumber(
			String criteriaIdentificationNumber) {
		this.criteriaIdentificationNumber = criteriaIdentificationNumber;
	}

	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public String getCriteriaService() {
		return criteriaService;
	}

	public void setCriteriaService(String criteriaService) {
		this.criteriaService = criteriaService;
	}

	public String getCriteriaCadastralCode() {
		return criteriaCadastralCode;
	}

	public void setCriteriaCadastralCode(String criteriaCadastralCode) {
		this.criteriaCadastralCode = criteriaCadastralCode;
	}

	public String getCriteriaWaterMeter() {
		return criteriaWaterMeter;
	}

	public void setCriteriaWaterMeter(String criteriaWaterMeter) {
		this.criteriaWaterMeter = criteriaWaterMeter;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}
