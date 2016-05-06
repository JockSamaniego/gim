package org.gob.gim.electronicvoucher.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import ec.gob.gim.complementvoucher.model.InstitutionService;

@Name("institutionServiceList")
public class InstitutionServiceList extends EntityQuery<InstitutionService> {
	
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select isv from InstitutionService isv";

	private static final String[] RESTRICTIONS = {
			"lower(isv.ruc) like  lower(concat(#{institutionServiceList.criteria},'%')) ",
			"lower(isv.name) like lower(concat(#{institutionServiceList.criteria},'%'))"
			};

	private String criteria;
	
	public InstitutionServiceList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("isv.name");
		setMaxResults(25);
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

}
