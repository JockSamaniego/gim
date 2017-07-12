package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;
import ec.gob.gim.common.model.ItemCatalog;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("infractionsHome")
public class InfractionsHome extends EntityHome<Infractions> {

	@In(create = true)
	BulletinHome bulletinHome;
	/*@In(create = true)
	ItemCatalogHome itemCatalogHome;*/

	public void setInfractionsId(Long id) {
		setId(id);
	}

	public Long getInfractionsId() {
		return (Long) getId();
	}

	@Override
	protected Infractions createInstance() {
		Infractions infractions = new Infractions();
		return infractions;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Bulletin bulletin = bulletinHome.getDefinedInstance();
		if (bulletin != null) {
			getInstance().setBulletin(bulletin);
		}
		/*ItemCatalog status = itemCatalogHome.getDefinedInstance();
		if (status != null) {
			getInstance().setStatus(status);
		}
		ItemCatalog type = itemCatalogHome.getDefinedInstance();
		if (type != null) {
			getInstance().setType(type);
		}*/
	}

	public boolean isWired() {
		return true;
	}

	public Infractions getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
