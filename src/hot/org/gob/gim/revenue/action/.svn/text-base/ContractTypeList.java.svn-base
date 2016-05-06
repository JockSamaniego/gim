package org.gob.gim.revenue.action;

import ec.gob.gim.revenue.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;
import java.util.Arrays;

@Name("contractTypeList")
public class ContractTypeList extends EntityQuery<ContractType> {

	private static final String EJBQL = "select contractType from ContractType contractType";

	private static final String[] RESTRICTIONS = {
			"lower(contractType.description) like lower(concat(#{contractTypeList.contractType.description},'%'))",
			"lower(contractType.name) like lower(concat(#{contractTypeList.contractType.name},'%'))",
			"lower(contractType.entry.name) like lower(concat(#{contractTypeList.entry.name},'%'))",};

	private ContractType contractType = new ContractType();
	private Entry entry = new Entry();

	public ContractTypeList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	public ContractType getContractType() {
		return contractType;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public Entry getEntry() {
		return entry;
	}
}
