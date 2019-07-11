package ec.gob.gim.revenue.model.bankDebit.criteria;

public class BankDebitSearchCriteria {

	private Integer servicenumber;
	private String receiptIdentification;
	private String receiptName;

	public BankDebitSearchCriteria() {
		this.servicenumber = null;
		this.receiptIdentification = null;
		this.receiptName = null;
	}

	public Integer getServicenumber() {
		return servicenumber;
	}

	public void setServicenumber(Integer servicenumber) {
		this.servicenumber = servicenumber;
	}
	
	public String getReceiptIdentification() {
		return receiptIdentification;
	}

	public void setReceiptIdentification(String receiptIdentification) {
		this.receiptIdentification = receiptIdentification;
	}

	public String getReceiptName() {
		return receiptName;
	}

	public void setReceiptName(String receiptName) {
		this.receiptName = receiptName;
	}

	@Override
	public String toString() {
		return "BankDebitSearchCriteria [servicenumber=" + servicenumber
				+ ", receiptIdentification=" + receiptIdentification
				+ ", receiptName=" + receiptName + "]";
	}

}