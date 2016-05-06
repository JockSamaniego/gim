package org.gob.gim.concession.action;

import java.util.ArrayList;
import java.util.List;

import org.gob.gim.common.action.ResidentHome;
import org.gob.gim.revenue.action.ContractHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.consession.model.ConcessionGroup;
import ec.gob.gim.consession.model.ConcessionPlace;
import ec.gob.gim.consession.model.ConcessionPlaceType;
import ec.gob.gim.revenue.model.ConcessionType;
import ec.gob.gim.revenue.model.Contract;

@Name("concessionPlaceHome")
public class ConcessionPlaceHome extends EntityHome<ConcessionPlace> {

	@In(create = true)
	ConcessionGroupHome concessionGroupHome;
	@In(create = true)
	ConcessionPlaceTypeHome concessionPlaceTypeHome;
	@In(create = true)
	ContractHome contractHome;
	@In(create = true)
	ResidentHome residentHome;

	public void setConcessionPlaceId(Long id) {
		setId(id);
	}

	public Long getConcessionPlaceId() {
		return (Long) getId();
	}

	@Override
	protected ConcessionPlace createInstance() {
		ConcessionPlace concessionPlace = new ConcessionPlace();
		return concessionPlace;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		ConcessionGroup concessionGroup = concessionGroupHome
				.getDefinedInstance();
		if (concessionGroup != null) {
			getInstance().setConcessionGroup(concessionGroup);
		}
		ConcessionPlaceType concessionPlaceType = concessionPlaceTypeHome.getDefinedInstance();
		if (concessionPlaceType != null) {
			getInstance().setConsessionPlaceType(concessionPlaceType);
		}
		Contract currentContract = contractHome.getDefinedInstance();
		if (currentContract != null) {
			getInstance().setCurrentContract(currentContract);
		}
		Resident resident = residentHome.getDefinedInstance();
		if (resident != null) {
			getInstance().setResident(resident);
		}
	}

	public boolean isWired() {
		return true;
	}

	public ConcessionPlace getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<Contract> getContracts() {
		return getInstance() == null ? null : new ArrayList<Contract>(
				getInstance().getContracts());
	}

}
