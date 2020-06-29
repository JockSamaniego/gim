package org.loxageek.common.ws.v2;

import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.gob.loja.gim.ws.dto.Payout;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.v2.ClosingStatementV2;
import org.gob.loja.gim.ws.dto.v2.DepositStatementV2;
import org.gob.loja.gim.ws.dto.v2.ReverseStatementV2;
import org.gob.loja.gim.ws.dto.v2.StatementV2;
import org.gob.loja.gim.ws.exception.InvalidPayout;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.NotActiveWorkday;
import org.gob.loja.gim.ws.exception.NotOpenTill;
import org.gob.loja.gim.ws.exception.PayoutNotAllowed;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;
import org.gob.loja.gim.ws.service.v2.PaymentServiceV2;
//import org.mvel2.util.Make.String;

/**
 * Define los servicios que permiten la consulta de valores adeudados y el
 * registro de pagos realizados a través de instituciones financieras
 * 
 * @author rfam
 * 
 */
@WebService(		
		targetNamespace="http://192.168.1.31:8080/gim/paymentPlatformV2"
		
		)
@HandlerChain(file="handler-chain.xml")
public class PaymentPlatformV2 {
	@EJB
	private PaymentServiceV2 service;

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
	public StatementV2 findStatement(ServiceRequest request){
		StatementV2 statement = service.findStatement(request);
		InvalidateSession();
		return statement;
	}

	@WebMethod
	public Boolean validateOpenTill(ServiceRequest request) {
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
		// System.out.println(">>> isOpen " + isOpen);
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
	public DepositStatementV2 registerDeposit(ServiceRequest request, Payout payout) {
		DepositStatementV2 statement = service.registerDeposit(request, payout);		
		InvalidateSession();
		return statement;
	}

	@WebMethod
	public ClosingStatementV2 findDeposits(ServiceRequest request, Date paymentDate) throws InvalidUser {
		ClosingStatementV2 statement = service.findDeposits(request, paymentDate);
		InvalidateSession();
		return statement;
	}

	private void InvalidateSession() {
		final MessageContext mc = this.wsContext.getMessageContext();
		HttpServletRequest sr = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);

		if (sr != null && sr instanceof HttpServletRequest) {
			final HttpServletRequest hsr = (HttpServletRequest) sr;
			HttpSession session = hsr.getSession(false);
			if (session != null) {
				// System.out.println("Invalidate Session Active PaymentPlatform");
				session.invalidate();
				session.setMaxInactiveInterval(1);
			}
		}
	}
	
	@WebMethod
	public ReverseStatementV2 reversePayment (ServiceRequest request, Payout payout){
		ReverseStatementV2 statement= service.reversePaymentBank(request, payout);
		InvalidateSession();
		return statement;
	}
	
}
