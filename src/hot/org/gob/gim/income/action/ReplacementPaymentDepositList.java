package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("replacementPaymentDepositList")
public class ReplacementPaymentDepositList extends EntityQuery<ReplacementPaymentDeposit> {

	private static final String EJBQL = "select replacementPaymentDeposit from ReplacementPaymentDeposit replacementPaymentDeposit";

	private static final String[] RESTRICTIONS = {};

	private ReplacementPaymentDeposit replacementPaymentDeposit = new ReplacementPaymentDeposit();

	public ReplacementPaymentDepositList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ReplacementPaymentDeposit getReplacementPaymentDeposit() {
		return replacementPaymentDeposit;
	}
}
