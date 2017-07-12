package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("replacementPaymentDepositHome")
public class ReplacementPaymentDepositHome extends EntityHome<ReplacementPaymentDeposit> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@In(create = true)
	DepositHome depositHome;
	

	public void setReplacementPaymentDepositId(Long id) {
		setId(id);
	}

	public Long getReplacementPaymentDepositId() {
		return (Long) getId();
	}

	@Override
	protected ReplacementPaymentDeposit createInstance() {
		ReplacementPaymentDeposit replacementPaymentDeposit = new ReplacementPaymentDeposit();
		return replacementPaymentDeposit;
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		Deposit deposit = depositHome.getDefinedInstance();
		if (deposit != null) {
			getInstance().setDeposit(deposit);
		}
	}

	public boolean isWired() {
		return true;
	}

	public ReplacementPaymentDeposit getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

}
