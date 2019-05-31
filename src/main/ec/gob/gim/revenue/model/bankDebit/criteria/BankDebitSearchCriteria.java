package ec.gob.gim.revenue.model.bankDebit.criteria;

import ec.gob.gim.common.model.ItemCatalog;

public class BankDebitSearchCriteria {

	private String servicenumber;

	public BankDebitSearchCriteria() {
	}

	public String getServicenumber() {
		return servicenumber;
	}

	public void setServicenumber(String servicenumber) {
		this.servicenumber = servicenumber;
	}

	@Override
	public String toString() {
		return "BankDebitSearchCriteria [servicenumber=" + servicenumber + "]";
	}
	
}