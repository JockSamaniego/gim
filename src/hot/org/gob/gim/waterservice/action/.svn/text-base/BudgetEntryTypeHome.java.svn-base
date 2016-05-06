package org.gob.gim.waterservice.action;

import ec.gob.gim.waterservice.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("budgetEntryTypeHome")
public class BudgetEntryTypeHome extends EntityHome<BudgetEntryType> {

	public void setBudgetEntryTypeId(Long id) {
		setId(id);
	}

	public Long getBudgetEntryTypeId() {
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

	public BudgetEntryType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
