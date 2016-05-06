package org.gob.gim.waterservice.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.waterservice.model.ConnectionSite;

@Name("connectionSiteHome")
public class ConnectionSiteHome extends EntityHome<ConnectionSite> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void setConnectionSiteId(Long id) {
		setId(id);
	}

	public Long getConnectionSiteId() {
		return (Long) getId();
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

	public ConnectionSite getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
