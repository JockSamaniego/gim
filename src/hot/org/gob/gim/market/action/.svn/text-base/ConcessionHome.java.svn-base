package org.gob.gim.market.action;

import org.gob.gim.common.action.ResidentHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.market.model.Concession;
import ec.gob.gim.market.model.Market;

@Name("concessionHome")
public class ConcessionHome extends EntityHome<Concession> {

	@In(create = true)
	MarketHome marketHome;
	@In(create = true)
	ResidentHome residentHome;

	public void setConcessionId(Long id) {
		setId(id);
	}

	public Long getConcessionId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Market market = marketHome.getDefinedInstance();
		if (market != null) {
			getInstance().setMarket(market);
		}
		Resident resident = residentHome.getDefinedInstance();
		if (resident != null) {
			getInstance().setResident(resident);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Concession getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
