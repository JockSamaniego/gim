package org.gob.gim.appraisal.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.appraisal.model.AppraisalItemStructure;

@Name("appraisalItemStructureList")
public class AppraisalItemStructureList extends EntityQuery<AppraisalItemStructure> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select aIStructure from AppraisalItemStructure aIStructure ";

	private static final String[] RESTRICTIONS = {
//			"lower(resident.identificationNumber) like lower(concat(#{cementeryList.criteriaIdentificationNumber},'%'))",
//		"lower(aPeriod.name) like lower(concat(#{appraisalPeriodList.criteriaName},'%'))",
		};

	private String criteriaName;

	public AppraisalItemStructureList () {
		
		setEjbql(EJBQL);
		setOrder("aIStructure.appraisalItemBase.name");
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
