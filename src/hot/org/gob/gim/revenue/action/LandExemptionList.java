package org.gob.gim.revenue.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.revenue.model.LandExemption;

@Name("landExemptionList")
public class LandExemptionList extends EntityQuery<LandExemption> {

	private static final String EJBQL = "select landExemption from LandExemption landExemption " +
										"left join fetch landExemption. property property " ;

	private static final String[] RESTRICTIONS = {"landExemption.fiscalPeriod.name = #{landExemptionList.fiscalPeriod}",
												  "lower(property.cadastralCode) like lower(concat('%',#{landExemptionList.cadastralCode},'%')) or lower(property.previousCadastralCode) like lower(concat('%',:el2,'%'))",
												  "lower(property.currentDomain.resident.identificationNumber) like lower(concat('%',#{landExemptionList.resident},'%')) or lower(property.currentDomain.resident.name) like lower(concat('%',:el3,'%'))",};

	private LandExemption landExemption= new LandExemption();
	
	public LandExemption getLandExemption() {
		return landExemption;
	}

	private String fiscalPeriod;
	private String resident;
	private String cadastralCode;

	public LandExemptionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public String getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(String fiscalPeriod) {		
		this.fiscalPeriod = fiscalPeriod;
	}

	public String getResident() {		
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}
}
