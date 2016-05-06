package org.gob.loja.gim.ws.sde.ws;

import java.util.Date;

import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
//import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElement;

import org.apache.axis2.AxisFault;
import org.gob.loja.gim.ws.sde.AccountDebtsResponse;
import org.gob.loja.gim.ws.sde.DebtPaymentNotificationResponse;
import org.gob.loja.gim.ws.sde.DebtPaymentReversal;
import org.gob.loja.gim.ws.sde.DebtResponse; 
import org.gob.loja.gim.ws.sde.service.EMoney;

@WebService
public class ServicePayment {

	@EJB
	private EMoney service;

	/**
	 * @author macartuche
	 * @date 2016-01-21 11:45
	 * @param debtId
	 * @param utfi
	 * @param id
	 * @param queryDate
	 * @param idAgent
	 * @return
	 */
	@WebMethod
	public DebtResponse queryDebt(@WebParam(name = "debtId") String debtId, 
			@WebParam(name = "utfi")  String utfi, 
			@WebParam(name = "id")  String id, 
			@WebParam(name = "queryDate")  Date queryDate, 
			@WebParam(name = "idAgent")  String idAgent)
			throws AxisFault {
		try {
			return service.queryDebt(debtId, utfi, debtId, queryDate, idAgent);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@WebMethod
	public DebtPaymentNotificationResponse debtPaymentNotification(
			@WebParam(name = "debtId")  String debtId, 
			@WebParam(name = "amount")  String amount, 
			@WebParam(name = "transactionId")  String transactionId,
			@WebParam(name = "utfi")  String utfi, 
			@WebParam(name = "queryDate")  Date queryDate, 
			@WebParam(name = "id")  String id, 
			@WebParam(name = "transDate")  Date transDate, 
			@WebParam(name = "idAgent")  String idAgent) throws AxisFault {
		try {
			return service.debtPaymentNotification(debtId, amount, transactionId, utfi, queryDate, id, transDate,
					idAgent);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	/**
	 * @author macartuche
	 * @date 2016-01-21 11:45
	 * @param accountId
	 * @param utfi
	 * @param id
	 * @param queryDate
	 * @param idAgent
	 * @return
	 */
	@WebMethod
	public AccountDebtsResponse queryAccountDebts(
			@WebParam(name = "accountId")  String accountId, 
			@WebParam(name = "utfi")  String utfi, 
			@WebParam(name = "id")  String id, 
			@WebParam(name = "queryDate")  Date queryDate,
			@WebParam(name = "idAgent")  String idAgent
			) throws AxisFault {
		try {
			return service.queryAccountDebts(accountId, utfi, id, queryDate, idAgent);
		} catch (Exception ex) {
			// throw new AxisFault(ex.getMessage());
			return null;
		}
	}

	/** ------- ****/
	@WebMethod
	public DebtPaymentReversal debtPaymentReversal(
			@WebParam(name = "debtId")  String debtId, 
			@WebParam(name = "amount")  String amount, 
			@WebParam(name = "transactionId")  String transactionId, 
			@WebParam(name = "utfi")  String utfi,
			@WebParam(name = "id")  String id, 
			@WebParam(name = "paymentNotificationReversalDate")  Date paymentNotificationReversalDate, 
			@WebParam(name = "transDate")  Date transDate, 
			@WebParam(name = "idAgent")  String idAgent) throws AxisFault {

		try {
			return service.debtPaymentReversalS(debtId, amount, transactionId, utfi, id,
					paymentNotificationReversalDate, transDate, idAgent);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
