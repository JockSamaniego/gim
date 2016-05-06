package org.gob.gim.appraisal.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.appraisal.model.AppraisalTotalStructure;

@Name("appraisalTotalStructureList")
public class AppraisalTotalStructureList extends EntityQuery<AppraisalTotalStructure> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select aTStructure from AppraisalTotalStructure aTStructure ";

	private static final String[] RESTRICTIONS = {
//			"lower(resident.identificationNumber) like lower(concat(#{cementeryList.criteriaIdentificationNumber},'%'))",
		"lower(aPeriod.code) like lower(concat(#{appraisalPeriodList.criteriaCode},'%'))",
		"lower(aPeriod.name) like lower(concat(#{appraisalPeriodList.criteriaName},'%'))",
		};

	private String criteriaName;
	private String criteriaCode;
	private boolean open;

	public AppraisalTotalStructureList() {
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

	public String getCriteriaCode() {
		return criteriaCode;
	}

	public void setCriteriaCode(String criteriaCode) {
		this.criteriaCode = criteriaCode;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

}
