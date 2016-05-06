package org.gob.gim.appraisal.action;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.appraisal.model.AppraisalPeriod;
import ec.gob.gim.appraisal.model.AppraisalRossHeidecke;
import ec.gob.gim.common.model.FiscalPeriod;

@Name("appraisalPeriodList")
public class AppraisalPeriodList extends EntityQuery<AppraisalPeriod> {

	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select aPeriod from AppraisalPeriod aPeriod ";
//		"left join fetch aPeriod.appraisalTotalStructure " +
//		"left join fetch aPeriod.appraisalTotalWall " +
//		"left join fetch aPeriod.appraisalTotalRoof " +
//		"left join fetch aPeriod.appraisalTotalExternal ";

	private static final String[] RESTRICTIONS = {
//			"lower(resident.identificationNumber) like lower(concat(#{cementeryList.criteriaIdentificationNumber},'%'))",
		"lower(aPeriod.code) like lower(concat(#{appraisalPeriodList.criteriaCode},'%'))",
		"lower(aPeriod.name) like lower(concat(#{appraisalPeriodList.criteriaName},'%'))"
//		"aPeriod.open = #{appraisalPeriodList.criteriaOpen})",
		};

	private String criteriaName;
	private String criteriaCode;
	private String criteriaOpen;

	public AppraisalPeriodList() {
		
		setEjbql(EJBQL);
		setOrderColumn("aPeriod.id");
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(5);
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

	public String getCriteriaOpen() {
		return criteriaOpen;
	}

	public void setCriteriaOpen(String criteriaOpen) {
		this.criteriaOpen = criteriaOpen;
	}

	@SuppressWarnings("unchecked")
	@Factory(value = "appraisalPeriods")
	public List<AppraisalPeriod> findAppraisalPeriods() {
		Query query = getEntityManager().createNamedQuery(
				"AppraisalPeriod.findAll");
		List<AppraisalPeriod> appraisalPeriods = query.getResultList();
		return appraisalPeriods;
	}

	@SuppressWarnings("unchecked")
	@Factory(value = "closeAppraisalPeriods")
	public List<AppraisalPeriod> findCloseAppraisalPeriods() {
		Query query = getEntityManager().createNamedQuery(
				"AppraisalPeriod.findAll");
		query.setParameter("criteriaOpen", criteriaOpen);
		List<AppraisalPeriod> appraisalPeriods = query.getResultList();
		return appraisalPeriods;
	}
	
}