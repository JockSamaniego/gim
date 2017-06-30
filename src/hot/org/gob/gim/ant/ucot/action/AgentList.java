package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("agentList")
public class AgentList extends EntityQuery<Agent> {

	private static final String EJBQL = "select agent from Agent agent";

	private static final String[] RESTRICTIONS = {"lower(agent.agentCode) like lower(concat(#{agentList.agent.agentCode},'%'))",};

	private Agent agent = new Agent();

	public AgentList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Agent getAgent() {
		return agent;
	}
}
