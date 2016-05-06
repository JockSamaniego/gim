package org.gob.gim.firestation.action;

import java.util.Arrays;
import java.util.Date;

import ec.gob.gim.firestation.model.Inspections;
import ec.gob.gim.firestation.model.InspectionsState;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.commercial.model.Business;

@Name("inspectionsList")
public class InspectionsList extends EntityQuery<Inspections> {

	private static final long serialVersionUID = -3242997811348537850L;
	private static final String EJBQL = "select inspections from Inspections inspections left join fetch inspections.technicalInformations r ";
	
	
	private static final String[] RESTRICTIONS = {"inspections.inspectionsState = #{inspectionsList.inspectionsState}",
					"lower(inspections.inspector.name) like lower(concat(#{inspectionsList.criteria},'%'))",
					"lower(r.numberTechnicalInformation) like lower(concat(#{criteriaNumberTechnicalInformation},'%'))",
					"inspections.inspectionsDate = #{inspectionsList.inspectionsDate}",
					"lower(inspections.local.business.owner.name) like lower(concat(#{inspectionsList.criteriaOwner},'%'))",
					"lower(inspections.local.business.owner.identificationNumber) like lower(concat(#{inspectionsList.criteriaOwner},'%'))",
					"lower(inspections.local.business.name) like lower(concat(#{inspectionsList.criteriaOwner},'%'))"};

	private Inspections inspections = new Inspections();
	private Business business = new Business();

	private String criteria;
	private InspectionsState inspectionsState;
	private Date inspectionsDate;
	private String criteriaOwner;
	private String criteriaNumberTechnicalInformation;

	public InspectionsList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
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

	public Inspections getInspections() {
		return inspections;
	}

	public Business getBusiness() {
		return business;
	}

	public InspectionsState getInspectionsState() {
		return inspectionsState;
	}

	public void setInspectionsState(InspectionsState inspectionsState) {
		this.inspectionsState = inspectionsState;
	}

	public Date getInspectionsDate() {
		return inspectionsDate;
	}
	public void setInspectionsDate(Date inspectionsDate) {
		this.inspectionsDate = inspectionsDate;
	}

	public String getCriteriaOwner() {
		return criteriaOwner;
	}

	public void setCriteriaOwner(String criteriaOwner) {
		this.criteriaOwner = criteriaOwner;
	}

	public String getCriteriaNumberTechnicalInformation() {
		return criteriaNumberTechnicalInformation;
	}

	public void setCriteriaNumberTechnicalInformation(String criteriaNumberTechnicalInformation) {
		this.criteriaNumberTechnicalInformation = criteriaNumberTechnicalInformation;
	}

}
