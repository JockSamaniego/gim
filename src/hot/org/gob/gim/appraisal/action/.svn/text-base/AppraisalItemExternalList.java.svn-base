package org.gob.gim.appraisal.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.appraisal.model.AppraisalItemExternal;

@Name("appraisalItemExternalList")
public class AppraisalItemExternalList extends EntityQuery<AppraisalItemExternal> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select aIExternal from AppraisalItemExternal aIExternal";

	private static final String[] RESTRICTIONS = {
//			"lower(resident.identificationNumber) like lower(concat(#{cementeryList.criteriaIdentificationNumber},'%'))",
//		"lower(aPeriod.name) like lower(concat(#{appraisalPeriodList.criteriaName},'%'))",
		};

	private String criteriaName;

	public AppraisalItemExternalList () {
		
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

}
