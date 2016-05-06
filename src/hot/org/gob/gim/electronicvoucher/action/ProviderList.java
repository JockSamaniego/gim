package org.gob.gim.electronicvoucher.action;

import java.util.Arrays;
import java.util.Date;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.complementvoucher.model.ElectronicVoucher;
import ec.gob.gim.complementvoucher.model.InstitutionService; 
import ec.gob.gim.complementvoucher.model.Provider;

@Name("providerList")
public class ProviderList extends EntityQuery<Provider> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select p from Provider p";

	private static final String[] RESTRICTIONS = {
			"p.identificationNumber like  concat(#{providerList.criteria},'%')",
			"lower(p.lastname) like lower(concat(#{providerList.criteria},'%'))" };

	private String criteria; 
	

	public ProviderList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrder("p.lastname");
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
}
