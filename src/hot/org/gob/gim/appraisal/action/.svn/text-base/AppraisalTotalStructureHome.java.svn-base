package org.gob.gim.appraisal.action;

import java.io.Serializable;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.appraisal.model.AppraisalItemStructure;
import ec.gob.gim.appraisal.model.AppraisalTotalStructure;

@Name("appraisalTotalStructureHome")
public class AppraisalTotalStructureHome extends
		EntityHome<AppraisalTotalStructure> implements Serializable {

	private static final long serialVersionUID = 1L;

	@In(create = true)
	AppraisalItemStructureHome appraisalItemStructureHome;

	Log logger;
	private String criteria;

	private AppraisalItemStructure appraisalItemStructure;

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

	public AppraisalTotalStructure getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setAppraisalTotalStructureId(Long id) {
		setId(id);
	}

	public Long getAppraisalTotalStructureId() {
		return (Long) getId();
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public AppraisalItemStructure getAppraisalItemStructure() {
		return appraisalItemStructure;
	}

	public void setAppraisalItemStructure(
			AppraisalItemStructure appraisalItemStructure) {
		this.appraisalItemStructure = appraisalItemStructure;
	}

	public void createAppraisalItemStructure() {
		this.appraisalItemStructure = new AppraisalItemStructure();
	}

	public void editAppraisalItemStructure(
			AppraisalItemStructure appraisalItemStructure) {
		this.appraisalItemStructure = appraisalItemStructure;
	}

	public void addAppraisalItemStructure() {
		this.getInstance().add(this.appraisalItemStructure);
		logger.info(this.getInstance().getAppraisalItemsStructure().size());
	}

	public void removeAppraisalItemStructure(
			AppraisalItemStructure appraisalItemStructure) {
		this.getInstance().remove(appraisalItemStructure);
	}

}
