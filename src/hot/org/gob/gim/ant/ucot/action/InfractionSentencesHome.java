package org.gob.gim.ant.ucot.action;

import ec.gob.gim.ant.ucot.model.*;
import ec.gob.gim.common.model.ItemCatalog;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("infractionSentencesHome")
public class InfractionSentencesHome extends EntityHome<InfractionSentences> {

	/*@In(create = true)
	ItemCatalogHome itemCatalogHome;*/

	public void setInfractionSentencesId(Long id) {
		setId(id);
	}

	public Long getInfractionSentencesId() {
		return (Long) getId();
	}

	@Override
	protected InfractionSentences createInstance() {
		InfractionSentences infractionSentences = new InfractionSentences();
		return infractionSentences;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		/*ItemCatalog type = itemCatalogHome.getDefinedInstance();
		if (type != null) {
			getInstance().setType(type);
		}*/
	}

	public boolean isWired() {
		return true;
	}

	public InfractionSentences getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
