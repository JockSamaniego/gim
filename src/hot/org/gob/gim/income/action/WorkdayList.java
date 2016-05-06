package org.gob.gim.income.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.income.model.Workday;

@Name("workdayList")
public class WorkdayList extends EntityQuery<Workday> {

	private static final String EJBQL = "select workday from Workday workday";

	private static final String[] RESTRICTIONS = {"workday.charge = #{workdayList.workday.charge}",												  
												  "workday.date = #{workdayList.workday.date}",};

	private Workday workday = new Workday();
	
	public WorkdayList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("workday.date");		
		setOrderDirection("DESC");
		setMaxResults(25);
		
	}

	public Workday getWorkday() {
		return workday;
	}
}
