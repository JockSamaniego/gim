package org.gob.gim.ant.ucot.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.ant.ucot.model.Agent;
import ec.gob.gim.ant.ucot.model.Bulletin;
import ec.gob.gim.common.model.ItemCatalog;

@Name("bulletinHome")
public class BulletinHome extends EntityHome<Bulletin> {

	@In(create = true)
	AgentHome agentHome;
	/*@In(create = true)
	ItemCatalogHome itemCatalogHome;*/

	public void setBulletinId(Long id) {
		setId(id);
	}

	public Long getBulletinId() {
		return (Long) getId();	
	}

	@Override
	protected Bulletin createInstance() {
		Bulletin bulletin = new Bulletin();
		return bulletin;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Agent agent = agentHome.getDefinedInstance();
		if (agent != null) {
			getInstance().setAgent(agent);
		}
		/*ItemCatalog type = itemCatalogHome.getDefinedInstance();
		if (type != null) {
			getInstance().setType(type);
		}*/
	}

	public boolean isWired() {
		return true;
	}

	public Bulletin getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
