package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;

@Name("agentList")
public class AgentList extends EntityQuery<Agent> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select agent from Agent agent";

	private static final String[] RESTRICTIONS = {
		"lower(agent.resident.name) like lower(concat('%',#{agentList.criteriaSearch},'%'))",
		"lower(agent.resident.identificationNumber) like lower(concat('%',#{agentList.criteriaSearch},'%'))",
		"lower(agent.agentCode) like lower(concat('%',#{agentList.criteriaSearch},'%'))",};

	private Agent agent = new Agent();

	public AgentList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setOrderColumn("agent.resident.identificationNumber");
		setOrderDirection("asc");
		setMaxResults(25);
	}
	
	private String criteriaSearch;
	
	@Override
	public String getRestrictionLogicOperator() {
		return "or";
	}


	public Agent getAgent() {
		return agent;
	}


	public String getCriteriaSearch() {
		return criteriaSearch;
	}


	public void setCriteriaSearch(String criteriaSearch) {
		this.criteriaSearch = criteriaSearch;
	}
	
}
