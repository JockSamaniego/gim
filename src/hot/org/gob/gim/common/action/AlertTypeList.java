package org.gob.gim.common.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.common.model.AlertType;

@Name("alertTypeList")
public class AlertTypeList extends EntityQuery<AlertType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6582330831774437177L;

	private static final String EJBQL = "select alertType from AlertType alertType order by id desc";


	private AlertType alertType = new AlertType();

	private String criteriaSearch;

	public AlertTypeList() {
		setEjbql(EJBQL);
		setMaxResults(25);
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	/**
	 * @return the alert
	 */
	public AlertType getAlertType() {
		return alertType;
	}

	/**
	 * @param alert the alert to set
	 */
	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
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
