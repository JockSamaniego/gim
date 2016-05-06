package org.gob.gim.audit.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.audit.model.AuditRequest;

@Name("auditRequest")
public class AuditRequestHome extends EntityHome<AuditRequest> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public void setAuditRequestId(Long id) {
		setId(id);
	}

	public Long getAuditRequestId() {
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

	public AuditRequest getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
