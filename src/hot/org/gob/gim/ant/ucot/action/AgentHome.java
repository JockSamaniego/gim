package org.gob.gim.ant.ucot.action;

import org.gob.gim.common.action.ResidentHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.ant.ucot.model.Agent;
import ec.gob.gim.common.model.Resident;

@Name("agentHome")
public class AgentHome extends EntityHome<Agent> {
	
	@In(create = true)
	ResidentHome residentHome;

	public void setAgentId(Long id) {
		setId(id);
	}

	public Long getAgentId() {
		return (Long) getId();
	}

	@Override
	protected Agent createInstance() {
		Agent agent = new Agent();
		return agent;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Resident resident = residentHome.getDefinedInstance();
		if (resident != null) {
			getInstance().setResident(resident);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Agent getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
