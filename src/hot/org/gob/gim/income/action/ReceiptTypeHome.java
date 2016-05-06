package org.gob.gim.income.action;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.ReceiptType;

@Name("receiptTypeHome")
public class ReceiptTypeHome extends EntityHome<ReceiptType> {

	public void setReceiptTypeId(Long id) {
		setId(id);
	}

	public Long getReceiptTypeId() {
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

	public ReceiptType getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
