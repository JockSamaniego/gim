package org.gob.gim.common.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.common.model.Alert;

@Name("alertList")
public class AlertList extends EntityQuery<Alert> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6582330831774437177L;

	private static final String EJBQL = "select alert from Alert alert";

	private static final String[] RESTRICTIONS = {
		"lower(alert.resident.name) like lower(concat(#{alertList.criteriaSearch},'%'))",
		"lower(alert.resident.identificationNumber) like lower(concat(#{alertList.criteriaSearch},'%'))"
		};

	private Alert alert = new Alert();

	private String criteriaSearch;

	public AlertList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	/**
	 * @return the alert
	 */
	public Alert getAlert() {
		return alert;
	}

	/**
	 * @param alert the alert to set
	 */
	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	/**
	 * @return the criteriaSearch
	 */
	public String getCriteriaSearch() {
		return criteriaSearch;
	}

	/**
	 * @param criteriaSearch the criteriaSearch to set
	 */
	public void setCriteriaSearch(String criteriaSearch) {
		this.criteriaSearch = criteriaSearch;
	}

}
