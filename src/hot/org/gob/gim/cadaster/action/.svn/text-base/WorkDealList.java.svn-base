package org.gob.gim.cadaster.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cadaster.model.WorkDeal;
import ec.gob.gim.cadaster.model.WorkDealType;

@Name("workDealList")
public class WorkDealList extends EntityQuery<WorkDeal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private WorkDealType workDealType;

	private static final String EJBQL = "select workDeal from WorkDeal workDeal";
	
	private static final String[] RESTRICTIONS= {
		"lower(workDeal.name) like lower(concat(#{workDealList.name},'%'))",
		"workDeal.workDealType = #{workDealList.workDealType}",};

	
	public WorkDealList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public WorkDealType getWorkDealType() {
		return workDealType;
	}


	public void setWorkDealType(WorkDealType workDealType) {
		this.workDealType = workDealType;
	}
	
}
