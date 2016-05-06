package org.gob.gim.revenue.action;

import ec.gob.gim.revenue.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("contractList")
public class ContractList extends EntityQuery<Contract> {

	private static final String EJBQL = "select contract from Contract contract";

	private static final String[] RESTRICTIONS = {};

	private Contract contract = new Contract();

	public ContractList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public Contract getContract() {
		return contract;
	}
}
