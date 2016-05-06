package org.gob.gim.contravention.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.contravention.model.Provenance;

@Name("provenanceHome")
public class ProvenanceHome extends EntityHome<Provenance> {

	public void setProvenanceId(Long id) {
		setId(id);
	}

	public Long getProvenanceId() {
		return (Long) getId();
	}

	@Override
	protected Provenance createInstance() {
		Provenance provenance = new Provenance();
		return provenance;
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

	public Provenance getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}