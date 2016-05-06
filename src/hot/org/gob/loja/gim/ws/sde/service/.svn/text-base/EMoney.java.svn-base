package org.gob.loja.gim.ws.sde.service;

import java.util.Date;

import javax.ejb.Local;

import org.gob.loja.gim.ws.sde.AccountDebtsResponse;
import org.gob.loja.gim.ws.sde.DebtPaymentNotificationResponse;
import org.gob.loja.gim.ws.sde.DebtPaymentReversal;
import org.gob.loja.gim.ws.sde.DebtResponse; 

@Local
public interface EMoney {
	public String LOCAL_NAME = "/gim/EMoney/local";
	
	public DebtResponse queryDebt(String debtId, String utfi, String id, Date queryDate, String idAgent);
	public DebtPaymentNotificationResponse debtPaymentNotification(String debtId, String amount, 
			String transactionId, String utfi, Date queryDate, String id, Date transDate, String idAgent);
	public AccountDebtsResponse queryAccountDebts(String accountId, String utfi, String id,
			Date queryDate, String idAgent);
	
	public DebtPaymentReversal debtPaymentReversalS(String debtId,
			String amount, String transactionId, String utfi, String id,
			Date paymentNotificationReversalDate, Date transDate, String idAgent);
	
}
