package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.math.BigInteger;
import java.util.Arrays;

@Name("bulletinList")
public class BulletinList extends EntityQuery<Bulletin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select bulletin from Bulletin bulletin";

	private static final String[] RESTRICTIONS = {
		"lower(bulletin.agent.resident.name) like lower(concat('%',#{bulletinList.criteriaSearch},'%'))",
		"lower(bulletin.agent.resident.identificationNumber) like lower(concat('%',#{bulletinList.criteriaSearch},'%'))",
		"lower(bulletin.agent.agentCode) like lower(concat('%',#{bulletinList.criteriaSearch},'%'))",
		"bulletin.startNumber >= #{bulletinList.serial}",};
	
	private Bulletin bulletin = new Bulletin();
	
	private String criteriaSearch;
	private BigInteger serial;

	public BulletinList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("bulletin.startNumber");
		setOrderDirection("asc");
		setMaxResults(500);
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

	public BigInteger getSerial() {
		return serial;
	}

	public void setSerial(BigInteger serial) {
		this.serial = serial;
	}

}
