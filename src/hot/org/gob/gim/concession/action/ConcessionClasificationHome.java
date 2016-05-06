package org.gob.gim.concession.action;

import ec.gob.gim.consession.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("concessionClasificationHome")
public class ConcessionClasificationHome extends
		EntityHome<ConcessionClasification> {

	public void setConcessionClasificationId(Long id) {
		setId(id);
	}

	public Long getConcessionClasificationId() {
		return (Long) getId();
	}

	@Override
	protected ConcessionClasification createInstance() {
		ConcessionClasification concessionClasification = new ConcessionClasification();
		return concessionClasification;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public ConcessionClasification getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
