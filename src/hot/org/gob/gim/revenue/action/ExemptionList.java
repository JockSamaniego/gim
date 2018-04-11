package org.gob.gim.revenue.action;

import java.util.Arrays;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.revenue.model.Exemption;
import ec.gob.gim.revenue.model.ExemptionType;

@Name("exemptionList")
public class ExemptionList extends EntityQuery<Exemption> {

	private static final String EJBQL = "select exemption from Exemption exemption "
			+ "left join fetch exemption.resident "
			+ "left join fetch exemption.partner ";

	private static final String[] RESTRICTIONS = {"exemption.fiscalPeriod = #{exemptionList.fiscalPeriod}",
		"exemption.exemptionType = #{exemptionList.exemptionType}",
		"lower(exemption.resident.identificationNumber) like lower(concat('%',#{exemptionList.resident},'%')) or lower(exemption.resident.name) like lower(concat('%',:el3,'%'))",
		"lower(exemption.partner.identificationNumber) like lower(concat('%',#{exemptionList.partner},'%')) or lower(exemption.partner.name) like lower(concat('%',:el4,'%'))"};

	private Exemption exemption= new Exemption();
	
	@Out(scope=ScopeType.SESSION, required=false)
	@In(scope=ScopeType.SESSION, required=false)
	private FiscalPeriod fiscalPeriod;

	@Out(scope=ScopeType.SESSION, required=false)
	@In(scope=ScopeType.SESSION, required=false)
	private ExemptionType exemptionType;
	
	public Exemption getExemption() {
		return exemption;
	}

	private String resident;
	private String partner;

	public ExemptionList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public String getResident() {		
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public ExemptionType getExemptionType() {
		return exemptionType;
	}

	public void setExemptionType(ExemptionType exemptionType) {
		this.exemptionType = exemptionType;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

}
