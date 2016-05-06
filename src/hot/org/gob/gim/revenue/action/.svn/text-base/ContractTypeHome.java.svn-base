package org.gob.gim.revenue.action;

import ec.gob.gim.revenue.model.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("contractTypeHome")
public class ContractTypeHome extends EntityHome<ContractType> {

	@In(create = true)
	EntryHome entryHome;

	public void setContractTypeId(Long id) {
		setId(id);
	}

	public Long getContractTypeId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Entry entry = entryHome.getDefinedInstance();
		if (entry != null) {
			getInstance().setEntry(entry);
		}
	}

	public boolean isWired() {
		return true;
	}

	public ContractType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
