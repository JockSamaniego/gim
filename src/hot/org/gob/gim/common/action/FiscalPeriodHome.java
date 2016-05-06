package org.gob.gim.common.action;

import ec.gob.gim.common.model.*;
import ec.gob.gim.revenue.model.MunicipalBond;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("fiscalPeriodHome")
public class FiscalPeriodHome extends EntityHome<FiscalPeriod> {

	public void setFiscalPeriodId(Long id) {
		setId(id);
	}

	public Long getFiscalPeriodId() {
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

	public FiscalPeriod getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return getInstance() == null ? null : new ArrayList<MunicipalBond>(
				getInstance().getMunicipalBonds());
	}

}
