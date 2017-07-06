package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;

@Name("bulletinList")
public class BulletinList extends EntityQuery<Bulletin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select bulletin from Bulletin bulletin";

	private static final String[] RESTRICTIONS = {
		"lower(bulletin.agent.resident.name) like lower(concat(#{bulletinList.criteriaSearch},'%'))",
		"lower(bulletin.agent.resident.identificationNumber) like lower(concat(#{bulletinList.criteriaSearch},'%'))"
	};
	
	private Bulletin bulletin = new Bulletin();
	
	private String criteriaSearch;

	public BulletinList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}

	public Bulletin getBulletin() {
		return bulletin;
	}

	public String getCriteriaSearch() {
		return criteriaSearch;
	}

	public void setCriteriaSearch(String criteriaSearch) {
		this.criteriaSearch = criteriaSearch;
	}
	
}
