package org.gob.gim.common.action;

import ec.gob.gim.common.model.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("checkingRecordHome")
public class CheckingRecordHome extends EntityHome<CheckingRecord> {

	@In(create = true)
	PersonHome personHome;

	public void setCheckingRecordId(Long id) {
		setId(id);
	}

	public Long getCheckingRecordId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Person checker = personHome.getDefinedInstance();
		if (checker != null) {
			getInstance().setChecker(checker);
		}
	}

	public boolean isWired() {
		return true;
	}

	public CheckingRecord getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
