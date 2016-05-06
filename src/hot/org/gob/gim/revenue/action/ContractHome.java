package org.gob.gim.revenue.action;

import org.gob.gim.common.action.ResidentHome;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.ContractType;

@Name("contractHome")
public class ContractHome extends EntityHome<Contract> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In(create = true)
	ContractTypeHome contractTypeHome;
	@In(create = true)
	ResidentHome residentHome;

	public void setContractId(Long id) {
		setId(id);
	}

	public Long getContractId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		ContractType contractType = contractTypeHome.getDefinedInstance();
		if (contractType != null) {
			getInstance().setContractType(contractType);
		}
		Resident subscriber = residentHome.getDefinedInstance();
		if (subscriber != null) {
			getInstance().setSubscriber(subscriber);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Contract getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
