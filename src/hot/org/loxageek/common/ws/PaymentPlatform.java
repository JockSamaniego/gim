package org.loxageek.common.ws;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.gob.loja.gim.ws.dto.ClosingStatement;
import org.gob.loja.gim.ws.dto.Payout;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.Statement;
import org.gob.loja.gim.ws.dto.TransactionData;
import org.gob.loja.gim.ws.exception.HasNoObligations;
import org.gob.loja.gim.ws.exception.InvalidPayout;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.NotActiveWorkday;
import org.gob.loja.gim.ws.exception.NotOpenTill;
import org.gob.loja.gim.ws.exception.PayoutNotAllowed;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;
import org.gob.loja.gim.ws.service.PaymentService;

import ec.gob.gim.income.model.EMoneyPayment;

/**
 * Define los servicios que permiten la consulta de valores adeudados y el
 * registro de pagos realizados a través de instituciones financieras
 * 
 * @author gersonZaragocin gerson at loxageek dot com
 * 
 */
@WebService
public class PaymentPlatform {
	@EJB
	private PaymentService service;

	@Resource
	WebServiceContext wsContext;

	/**
	 * Permite la consulta de las obligaciones que tiene el contribuyente con el
	 * numero de identificacion identificationNumber desde una entidad
	 * financiera a la que se le ha entregado un username y un password que se
	 * incluyen en el ServiceRequest
	 * 
	 * @param request
	 *            Detalle del peticionario del servicio
	 * @return Estado de cuenta del contribuyente solicitado en el request
	 * @throws PayoutNotAllowed
	 * @throws TaxpayerNotFound
	 * @throws NotActiveWorkday
	 */
	@WebMethod
	public Statement findStatement(ServiceRequest request)
			throws PayoutNotAllowed, TaxpayerNotFound, InvalidUser, NotActiveWorkday, HasNoObligations {
		System.out.println(
				"FINDING SATATEMENT FOR " + request.getIdentificationNumber() + " with USER: " + request.getUsername());
		Statement statement = new Statement();
		try {
			statement = service.findStatement(request);
		} catch (PayoutNotAllowed e) {
			InvalidateSession();
			throw e;
		} catch (TaxpayerNotFound e) {
			InvalidateSession();
			throw e;
		} catch (InvalidUser e) {
			InvalidateSession();
			throw e;
		} catch (NotActiveWorkday e) {
			InvalidateSession();
			throw e;
		} catch (HasNoObligations e) {
			InvalidateSession();
			throw e;
		}
		InvalidateSession();
		return statement;
	}

	@WebMethod
	public Boolean validateOpenTill(ServiceRequest request) {
		System.out.println(
				"FINDING SATATEMENT FOR " + request.getIdentificationNumber() + " with USER: " + request.getUsername());
		Boolean isOpen = null;
		try {
			isOpen = service.isTillOpen(request);
		} catch (NotActiveWorkday e) {
			isOpen = Boolean.FALSE;
			// InvalidateSession();
		} catch (NotOpenTill e) {
			isOpen = Boolean.FALSE;
		} catch (InvalidUser e) {
			isOpen = Boolean.FALSE;
		}
		InvalidateSession();
		System.out.println(">>> isOpen " + isOpen);
		return isOpen;
	}

	/**
	 * Permite realizar el registro de un deposito en las cuentas municipales
	 * que tiene el municipio en la entidad financiera. Este metodo altera el
	 * estado de las obligaciones del contribuyente y las coloca como PAGADAS o
	 * EN PROCESO DE PAGO.
	 * 
	 * Se debe considerar en su implementacion las reglas que rigen para
	 * registrar pagos remotos y locales.
	 * 
	 * @param request
	 *            Detalle del peticionario del servicio
	 * @param password
	 *            Contraseña de la entidad financiera
	 * @return Valor boleano que confirma si el deposito se realizo
	 *         satisfactoriamente
	 * @throws InvalidPayout
	 * @throws PayoutNotAllowed
	 * @throws TaxpayerNotFound
	 * @throws NotActiveWorkday
	 * @throws NotOpenTill
	 */
	@WebMethod
	public Boolean registerDeposit(ServiceRequest request, Payout payout) throws PayoutNotAllowed, InvalidPayout,
			TaxpayerNotFound, InvalidUser, NotActiveWorkday, NotOpenTill, HasNoObligations {
		System.out.println("FINDING PENDING BONDS FOR " + request.getIdentificationNumber());
		boolean registro = false;
		try {
			registro = service.registerDeposit(request, payout);
		} catch (PayoutNotAllowed e) {
			InvalidateSession();
			throw e;
		} catch (InvalidPayout e) {
			InvalidateSession();
			throw e;
		} catch (TaxpayerNotFound e) {
			InvalidateSession();
			throw e;
		} catch (InvalidUser e) {
			InvalidateSession();
			throw e;
		} catch (NotActiveWorkday e) {
			InvalidateSession();
			throw e;
		} catch (NotOpenTill e) {
			InvalidateSession();
			throw e;
		} catch (HasNoObligations e) {
			InvalidateSession();
			throw e;
		}
		InvalidateSession();
		return registro;
	}

	@WebMethod
	public ClosingStatement findDeposits(ServiceRequest request, Date paymentDate) throws InvalidUser {
		System.out.println("FINDING CLOSING STATEMENT FOR " + request.getIdentificationNumber());
		ClosingStatement closingStatement = null;
		try {
			closingStatement = service.findDeposits(request, paymentDate);
		} catch (InvalidUser e) {
			InvalidateSession();
			throw e;
		}
		return closingStatement;
	}

	private void InvalidateSession() {
		final MessageContext mc = this.wsContext.getMessageContext();
		HttpServletRequest sr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);

		if (sr != null && sr instanceof HttpServletRequest) {
			final HttpServletRequest hsr = (HttpServletRequest) sr;
			HttpSession session = hsr.getSession(false);
			if (session != null) {
				System.out.println("Invalidate Session Active PaymentPlatform");
				session.invalidate();
				session.setMaxInactiveInterval(1);
			}
		}

		// if (sr != null && sr instanceof HttpServletRequest) {
		// final HttpServletRequest hsr = (HttpServletRequest) sr;
		// hsr.getSession(true).invalidate();
		// hsr.getSession().setMaxInactiveInterval(1);
		// }
		//
	}

	/**
	 * Permite la consulta de las obligaciones que tiene el contribuyente con el
	 * numero de identificacion identificationNumber desde una entidad
	 * financiera a la que se le ha entregado un username y un password que se
	 * incluyen en el ServiceRequest
	 * 
	 * @param request
	 *            Detalle del peticionario del servicio
	 * @return Estado de cuenta del contribuyente solicitado en el request
	 * @throws PayoutNotAllowed
	 * @throws TaxpayerNotFound
	 * @throws NotActiveWorkday
	 * @throws HasNoObligations
	 * @throws InvalidUser
	 */

	 
	/**
	 * @author macartuche
	 * @date 2016-04-13 17:21 Metodo auxiliar para el servicio web de emoney
	 * @param request
	 *            ServiceRequest
	 * @param debtId
	 *            String
	 * @param amount
	 *            BigDecimal
	 * @param date
	 *            Date
	 * @param idAgent
	 *            String
	 * @return List<EMoneyPayment>
	 */
	@WebMethod
	public List<EMoneyPayment> findPaidsForEmoney(ServiceRequest request, String debtId, BigDecimal amount, Date date,
			String idAgent) {
		return service.findPaidsForEmoney(request, debtId, amount, date, idAgent);
	}

	/**
	 * @author macartuche
	 * @date 2016-04-13 17:30 Metodo auxiliar para el servicio web de emoney
	 * @param depositsToReverse
	 *            List<Long>
	 * @param user
	 *            User
	 */
	@WebMethod
	public void reverse(ServiceRequest request, List<Long> depositsToReverse) {
		service.reverse(request, depositsToReverse);
	}

	/**
	 * @author macartuche
	 * @date 2016-04-13 17:32 Metodo auxiliar para el servicio web de emoney
	 * @param request
	 *            ServiceRequest
	 * @param municipalBondsIds
	 *            List<Long>
	 * @return
	 */
	@WebMethod
	public List<Long> findDepositsIdsForReverse(ServiceRequest request, List<Long> municipalBondsIds) {
		return service.findDepositsIdsForReverse(request, municipalBondsIds);
	}

	/**
	 * @author macartuche
	 * @date 2016-04-13 17:43 
	 * Metodo auxiliar para el servicio web de emoney
	 * @param payout Payout
	 * @param debtId String
	 * @param isPaid boolean
	 * @param idAgent String
	 * @return String
	 */
	@WebMethod
	public String saveEmoneyPayment(ServiceRequest request, Payout payout, String debtId, boolean isPaid, String idAgent) {
		String data = service.saveEmoneyPayment(request, payout, debtId, isPaid, idAgent);
		return data;
	}

	
	/**
	 * @author macartuche
	 * @date 2016-04-13 17:44 
	 * Metodo auxiliar para el servicio web de emoney
	 * @param paid EMoneyPayment
	 */
	@WebMethod
	public void updateEMoneyPayment(ServiceRequest request, EMoneyPayment paid) {
		service.updateEMoneyPayment(request, paid);
	}
	
	/**
	 * @author macartuche
	 * @date 2016-04-14 09:44 
	 * Metodo auxiliar para el servicio web de emoney
	 * @param request ServiceRequest
	 */
	@WebMethod
	public boolean hasRolEmoney(ServiceRequest request) {
		return service.hasRolEmoney(request);
	}

	
	//by Jock Samaniego...
	
	@WebMethod
	public String cadastralCodeDNI(ServiceRequest request, String cadastralCode) throws PayoutNotAllowed, TaxpayerNotFound, InvalidUser, NotActiveWorkday, HasNoObligations {
		String idNumber = service.searchPropertyByCadastralCode(request, cadastralCode);
		InvalidateSession();
		return idNumber;
	}
	
	/**
	 * @author rfarmijosm
	 * @date 2016-06-12 16:44
	 * Permite consultar la deuda se incluyen acuerdos de pago
	 * con este objeto no es posible realizar pagos, sive solo de consulta. 
	 * 
	 * @param request
	 * @return
	 * @throws PayoutNotAllowed
	 * @throws TaxpayerNotFound
	 * @throws InvalidUser
	 * @throws NotActiveWorkday
	 * @throws HasNoObligations
	 */
	@WebMethod
	public Statement debtConsult(ServiceRequest request)
			throws PayoutNotAllowed, TaxpayerNotFound, InvalidUser, NotActiveWorkday, HasNoObligations {
		System.out.println(
				"FINDING SATATEMENT FOR " + request.getIdentificationNumber() + " with USER: " + request.getUsername());
		Statement statement = new Statement();
		try {
			statement = service.debtConsult(request);
		} catch (PayoutNotAllowed e) {
			InvalidateSession();
			throw e;
		} catch (TaxpayerNotFound e) {
			InvalidateSession();
			throw e;
		} catch (InvalidUser e) {
			InvalidateSession();
			throw e;
		} catch (NotActiveWorkday e) {
			InvalidateSession();
			throw e;
		} catch (HasNoObligations e) {
			InvalidateSession();
			throw e;
		}
		InvalidateSession();
		return statement;
	}
	
	/* reversos para bancos
	 * By: René Ortega
	 * 2017-04-18
	 */
	@WebMethod
	public TransactionData reversePayment (ServiceRequest request,
			Payout payout){
		System.out.println("Llega al metodo de reverso");
		
		return this.service.reversePaymentBank(request, payout);
	}
	
	
	@WebMethod
	public TransactionData  queryPayment(ServiceRequest request, String transactionId){
		return this.service.queryPayment(request, transactionId);
	}
}
