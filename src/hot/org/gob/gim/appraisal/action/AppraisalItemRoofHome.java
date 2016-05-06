package org.gob.gim.appraisal.action;

import java.io.Serializable;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.appraisal.model.AppraisalItemRoof;

@Name("appraisalItemRoofHome")
public class AppraisalItemRoofHome extends EntityHome<AppraisalItemRoof> implements
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

	public AppraisalItemRoof getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setAppraisalItemRoofId(Long id) {
		setId(id);
	}

	public Long getAppraisalItemRoofId() {
		return (Long) getId();
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

}
