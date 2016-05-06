package org.gob.gim.appraisal.action;

import java.io.Serializable;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.appraisal.model.AppraisalItemExternal;

@Name("appraisalItemExternalHome")
public class AppraisalItemExternalHome extends EntityHome<AppraisalItemExternal> implements
		Serializable {

	private static final long serialVersionUID = 1L;

	Log logger;
	private String criteria;

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

	public AppraisalItemExternal getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setAppraisalItemExternalId(Long id) {
		setId(id);
	}

	public Long getAppraisalItemExternalId() {
		return (Long) getId();
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

}
