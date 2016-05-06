package org.gob.gim.cementery.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.cementery.model.Cremation;
import ec.gob.gim.cementery.model.Crematorium;

@Name("cremationList")
public class CremationList extends EntityQuery<Cremation> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select cremation from Cremation cremation " +
										"left join fetch Cremation.bloodRelation bloodRelation "+
										"left join fetch Cremation.crematorium crematorium ";

	private static final String[] RESTRICTIONS = {
		"lower(crematorium.name) = lower(#{cremationList.crematorium})",
		"lower(cremation.name) like lower(concat(#{cremationList.criteriaName},'%'))",
		"(lower(bloodRelation.name) like lower(concat(#{cremationList.criteriaNameResident},'%')) or " +
		"lower(bloodRelation.identificationNumber) like lower(concat(:el3,'%')))",
		};

	private String crematorium;
	private String criteriaName;
	private String criteriaNameResident;

	public CremationList() {
		
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public String getCrematorium() {
		return crematorium;
	}

	public void setCrematorium(String crematorium) {
		this.crematorium = crematorium;
	}

	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public String getCriteriaNameResident() {
		return criteriaNameResident;
	}

	public void setCriteriaNameResident(String criteriaNameResident) {
		this.criteriaNameResident = criteriaNameResident;
	}

}
