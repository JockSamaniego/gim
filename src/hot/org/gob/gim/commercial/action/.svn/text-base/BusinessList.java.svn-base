package org.gob.gim.commercial.action;

import ec.gob.gim.commercial.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("businessList")
public class BusinessList extends EntityQuery<Business> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3242997811348537850L;

	private static final String EJBQL = "select business from Business business";

	private static final String[] RESTRICTIONS = {"lower(business.name) like lower(concat(#{businessList.business.name},'%'))",
		"lower(business.owner.name) like lower(concat(#{businessList.business.name},'%'))",
		"lower(business.owner.identificationNumber) like lower(concat(#{businessList.business.name},'%'))"};

	private Business business = new Business();

	public BusinessList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrderColumn("id");
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public Business getBusiness() {
		return business;
	}
}