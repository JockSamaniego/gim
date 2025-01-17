package org.gob.gim.revenue.action;

import java.util.Arrays;
import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.revenue.model.ExemptionCem;

@Name("exemptioncemList")
public class ExemptionCEMList extends EntityQuery<ExemptionCem> {

	private static final String EJBQL = "select exemptioncem from ExemptionCem exemptioncem "
			+ "left join fetch exemptioncem.resident ";

	private static final String[] RESTRICTIONS = {
			"exemptioncem.type = #{exemptioncemList.type}",
			"exemptioncem.creationDate >= #{exemptioncemList.from}",
			"lower(exemptioncem.resident.identificationNumber) like lower(concat('%',#{exemptioncemList.resident},'%')) or lower(exemptioncem.resident.name) like lower(concat('%',:el2,'%'))"};

	private ExemptionCem exemption= new ExemptionCem();
	 

	@Out(scope=ScopeType.SESSION, required=false)
	@In(scope=ScopeType.SESSION, required=false)
	private ItemCatalog type;
	
	private Date from; 
	
	public ExemptionCem getExemption() {
		return exemption;
	}

	private String resident;
	
	public ExemptionCEMList() {
 
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("exemptioncem.creationDate desc");
		setMaxResults(25);
		
	}

	public String getResident() {		
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

 
	public ItemCatalog getType() {
		return type;
	}

	public void setType(ItemCatalog type) {
		this.type = type;
	}

	public void setExemption(ExemptionCem exemption) {
		this.exemption = exemption;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	 
}
