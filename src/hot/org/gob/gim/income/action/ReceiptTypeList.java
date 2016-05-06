package org.gob.gim.income.action;

import ec.gob.gim.income.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("receiptTypeList")
public class ReceiptTypeList extends EntityQuery<ReceiptType> {

	private static final String EJBQL = "select r from ReceiptType r";

	private static final String[] RESTRICTIONS = {"lower(r.name) like lower(concat(#{receiptTypeList.receiptType.name},'%'))",};

	private ReceiptType receiptType = new ReceiptType();

	public ReceiptType getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(ReceiptType receiptType) {
		this.receiptType = receiptType;
	}

	public ReceiptTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

}
