package org.gob.gim.income.action;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.ReceiptType;

@Name("receiptHome")
public class ReceiptHome extends EntityHome<Receipt> {

	@In(create = true)
	ReceiptTypeHome ReceiptTypeHome;


	public void setReceiptId(Long id) {
		setId(id);
	}

	public Long getReceiptId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		ReceiptType ReceiptType = ReceiptTypeHome.getDefinedInstance();
		if (ReceiptType != null) {
			getInstance().setReceiptType(ReceiptType);
		}
	}

	public boolean isWired() {
		return true;
	}

	public Receipt getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}



}
