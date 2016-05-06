package org.gob.gim.cementery.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cementery.model.Cementery;

@Name("cementeryList")
public class CementeryList extends EntityQuery<Cementery> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select cementery from Cementery cementery " +
										"left join fetch Cementery.manager manager";

	private static final String[] RESTRICTIONS = {
//			"lower(resident.identificationNumber) like lower(concat(#{cementeryList.criteriaIdentificationNumber},'%'))",
		"lower(manager.name) like lower(concat(#{cementeryList.criteriaManagerName},'%'))",
		"lower(cementery.name) like lower(concat('%',#{cementeryList.criteriaName},'%'))" 
		};

	private String criteriaName;
	private String criteriaManagerName;

	public CementeryList() {
		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public void setCriteriaManagerName(String criteriaManagerName) {
		this.criteriaManagerName = criteriaManagerName;
	}

	public String getCriteriaManagerName() {
		return criteriaManagerName;
	}


}
