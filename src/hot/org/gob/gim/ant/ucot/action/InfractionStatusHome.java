package org.gob.gim.ant.ucot.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.ant.ucot.model.InfractionStatus;
import ec.gob.gim.ant.ucot.model.Infractions;

@Name("infractionStatusHome")
public class InfractionStatusHome extends EntityHome<InfractionStatus> {

	@In(create = true)
	InfractionsHome infractionsHome;
	/*@In(create = true)
	ItemCatalogHome itemCatalogHome;*/

	public void setInfractionStatusId(Long id) {
		setId(id);
	}

	public Long getInfractionStatusId() {
		return (Long) getId();
	}

	@Override
	protected InfractionStatus createInstance() {
		InfractionStatus infractionStatus = new InfractionStatus();
		return infractionStatus;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Infractions infraction = infractionsHome.getDefinedInstance();
		if (infraction != null) {
			getInstance().setInfraction(infraction);
		}
		/*ItemCatalog status = itemCatalogHome.getDefinedInstance();
		if (status != null) {
			getInstance().setStatus(status);
		}*/
	}

	public boolean isWired() {
		return true;
	}

	public InfractionStatus getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
