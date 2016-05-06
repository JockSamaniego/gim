package org.gob.gim.exception;

public class InvoiceNumberOutOfRangeException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private Long invoiceNumber;
	
	public InvoiceNumberOutOfRangeException(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Long getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

}
