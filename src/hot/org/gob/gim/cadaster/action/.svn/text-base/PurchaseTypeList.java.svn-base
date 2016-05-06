package org.gob.gim.cadaster.action;

import ec.gob.gim.cadaster.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("purchaseTypeList")
public class PurchaseTypeList extends EntityQuery<PurchaseType> {

	private static final String EJBQL = "select purchaseType from PurchaseType purchaseType";

	private static final String[] RESTRICTIONS = {"lower(purchaseType.name) like lower(concat(#{purchaseTypeList.purchaseType.name},'%'))",};

	private PurchaseType purchaseType = new PurchaseType();

	public PurchaseTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public PurchaseType getPurchaseType() {
		return purchaseType;
	}
}
