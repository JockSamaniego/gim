package org.gob.gim.cadaster.action;

import org.gob.gim.common.action.FiscalPeriodHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.cadaster.model.Appraisal;

@Name("appraisalHome")
public class AppraisalHome extends EntityHome<Appraisal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	DomainHome domainHome;
	@In(create = true)
	FiscalPeriodHome fiscalPeriodHome;

	public void setAppraisalId(Long id) {
		setId(id);
	}

	public Long getAppraisalId() {
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

	public Appraisal getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
