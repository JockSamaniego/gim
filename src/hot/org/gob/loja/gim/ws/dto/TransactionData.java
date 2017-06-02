package org.gob.loja.gim.ws.dto;

public class TransactionData {
	
	private Boolean transactionCompleted;
	private String transactionMessage;
	
	public TransactionData() {
		super();
	}
	public Boolean getTransactionCompleted() {
		return transactionCompleted;
	}
	public void setTransactionCompleted(Boolean transactionCompleted) {
		this.transactionCompleted = transactionCompleted;
	}
	public String getTransactionMessage() {
		return transactionMessage;
	}
	public void setTransactionMessage(String transactionMessage) {
		this.transactionMessage = transactionMessage;
	}
	
	

}
