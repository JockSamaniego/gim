package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("receiptList")
public class ReceiptList extends EntityQuery<Receipt> {

	private static final String EJBQL = "select Receipt from Receipt Receipt";

	private static final String[] RESTRICTIONS = {};

	private Receipt Receipt = new Receipt();

	public ReceiptList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Receipt getReceipt() {
		return Receipt;
	}
}
