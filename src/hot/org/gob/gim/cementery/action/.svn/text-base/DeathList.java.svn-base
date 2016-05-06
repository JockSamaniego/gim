package org.gob.gim.cementery.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cementery.model.Cementery;
import ec.gob.gim.cementery.model.Death;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;

@Name("deathList")
public class DeathList extends EntityQuery<Death> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select death from Death death ";

	private static final String[] RESTRICTIONS = { "((lower(death.deathName) like lower(concat('%',#{deathList.deceasedCriteria},'%')))) "
	// "lower(resident.identificationNumber) like lower(concat(#{cmementeryList.criteriaIdentificationNumber},'%'))",
	// "lower(manager.name) like lower(concat(#{cementeryList.criteriaManagerName},'%'))",
	// "lower(cementery.name) like lower(concat('%',#{cementeryList.criteriaName},'%'))"
	};

	private String criteriaName;
	private String deceasedCriteria;
	
	public DeathList() {
		setOrder("death.deathName");
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

	public void setDeceasedCriteria(String deceasedCriteria) {
		this.deceasedCriteria = deceasedCriteria;
	}

	public String getDeceasedCriteria() {
		return deceasedCriteria;
	}

}
