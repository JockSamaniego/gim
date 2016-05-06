package org.gob.loja.gim.ws.sde;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DebtPaymentNotificationResponse", propOrder = { "debtId", "errorCode", "externalTransactionId", "message", "operationResult" })
public class DebtPaymentNotificationResponse {	

	private String debtId;
	private String operationResult;
	private String externalTransactionId;
	private String errorCode;
	private String message;

	public DebtPaymentNotificationResponse() {
	}

	public DebtPaymentNotificationResponse(String debtId, String operationResult, String externalTransactionId,
			String errorCode, String message) {
		this.debtId = debtId;
		this.operationResult = operationResult;
		this.externalTransactionId = externalTransactionId;
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
	}

	public String getExternalTransactionId() {
		return externalTransactionId;
	}

	public void setExternalTransactionId(String externalTransactionId) {
		this.externalTransactionId = externalTransactionId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebtId() {
		return debtId;
	}

	public void setDebtId(String debtId) {
		this.debtId = debtId;
	}

}
