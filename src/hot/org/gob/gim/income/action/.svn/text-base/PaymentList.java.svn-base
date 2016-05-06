package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("paymentList")
public class PaymentList extends EntityQuery<Payment> {

	private static final String EJBQL = "select payment from Payment payment";

	private static final String[] RESTRICTIONS = {};

	private Payment payment = new Payment();

	public PaymentList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Payment getPayment() {
		return payment;
	}
}
