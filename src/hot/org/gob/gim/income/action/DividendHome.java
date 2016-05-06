package org.gob.gim.income.action;

import org.gob.gim.revenue.action.MunicipalBondHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.Dividend;
import ec.gob.gim.revenue.model.MunicipalBond;

@Name("dividendHome")
public class DividendHome extends EntityHome<Dividend> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In(create = true)
	MunicipalBondHome municipalBondHome;

	public void setDividendId(Long id) {
		setId(id);
	}

	public Long getDividendId() {
		return (Long) getId();
	}
	
	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		MunicipalBond municipalBond = municipalBondHome.getDefinedInstance();
		if (municipalBond != null) {
		//	getInstance().setMunicipalBond(municipalBond);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Dividend getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
