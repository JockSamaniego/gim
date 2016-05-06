package org.gob.loja.gim.ws.sde.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.gob.loja.gim.ws.dto.Bond;
import org.gob.loja.gim.ws.dto.Payout;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.Statement;
import org.gob.loja.gim.ws.exception.HasNoObligations;
import org.gob.loja.gim.ws.exception.InvalidPayout;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.NotActiveWorkday;
import org.gob.loja.gim.ws.exception.NotOpenTill;
import org.gob.loja.gim.ws.exception.PayoutNotAllowed;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;
import org.gob.loja.gim.ws.sde.AccountDebtsResponse;
import org.gob.loja.gim.ws.sde.Debt;
import org.gob.loja.gim.ws.sde.DebtPaymentNotificationResponse;
import org.gob.loja.gim.ws.sde.DebtPaymentReversal;
import org.gob.loja.gim.ws.sde.DebtResponse;
import org.gob.loja.gim.ws.service.PaymentService;

import ec.gob.gim.income.model.EMoneyPayment;
import ec.gob.gim.security.model.User;

@Stateless(name = "EMoney")
public class EMoneyBean implements EMoney {

	@EJB
	public SystemParameterService systemParameterService;

	@EJB
	public PaymentService paymentService;

	// private final String PASSWORD_QUERY = "manuco2017A";
	// private final String USERNAME_QUERY = "bancodeloja";
	private final String PASSWORD_QUERY = "usuarioemoneygim";
	private final String USERNAME_QUERY = "usuario_emoney";

	private final String ERROR_NOT_OPEN_TILL = "La caja no se encuenta aperturada";
	private final String ERROR_NOT_ACTIVE_DAY = "No se ha abierto la jornada de recaudación";

	private Map<String, String> accountMap;
	
	public EMoneyBean() {
		accountMap = new java.util.HashMap<String, String>();
		accountMap.put("00056", "PREDIO URBANO");
		accountMap.put("00057", "PREDIO RUSTICO");
		accountMap.put("00076", "SERVICIO DE AGUA POTABLE");
		accountMap.put("00387", "MULTAS COMISARIA DE ORNATO");
		accountMap.put("00027", "ARR. PUESTOS EN LOS MERCADOS");
	}

	/**
	 * @author macartuche
	 * @date 2016-01-21 10:20
	 * @param identification
	 * @return Statement
	 * @throws PayoutNotAllowed
	 * @throws TaxpayerNotFound
	 * @throws InvalidUser
	 * @throws NotActiveWorkday
	 * @throws HasNoObligations
	 *             Realiza la busqueda de deudas de un contribuyente por su num
	 *             de ced/ruc
	 */
	private Statement findStatement(String identification)
			throws PayoutNotAllowed, TaxpayerNotFound, InvalidUser, HasNoObligations, NotOpenTill, NotActiveWorkday {
		ServiceRequest sr = new ServiceRequest();
		sr.setIdentificationNumber(identification);
		sr.setPassword(PASSWORD_QUERY);
		sr.setUsername(USERNAME_QUERY);

		// validar si la caja esta abierta
		boolean isOpen = true;

		isOpen = paymentService.isTillOpen(sr);
		if (isOpen) {
			return paymentService.findStatement(sr);
		}

		return null;

	}

	/**
	 * 
	 * @param idAgent
	 *            String =>key
	 * @return
	 */
	private String getAccount(String idAgent) {
		String account = "";
		if (accountMap.containsKey(idAgent)) {
			account = accountMap.get(idAgent);
		}
		return account;
	}

	/**
	 * @author macartuche
	 * @date 2016-01-21 10:28
	 * @param debtId
	 * @param utfi
	 * @param id
	 * @param queryDate
	 * @param idAgent
	 * @return DebtResponse Consulta deudas municipales por identificacion de
	 *         contribuyente
	 */
	public DebtResponse queryDebt(String debtId, String utfi, String id, Date queryDate, String idAgent) {

		System.out.println("METODO queryDebt");
		System.out.println("debtId: " + debtId);
		System.out.println("utfi: " + utfi);
		System.out.println("queryDate: " + queryDate);
		System.out.println("id: " + id);
		System.out.println("idAgent: " + idAgent);

		DebtResponse response = new DebtResponse();

		String ACCOUNT = getAccount(idAgent);
		if (ACCOUNT.isEmpty()) {// no hay cta
			Debt d = new Debt();
			d.setDebtType(debtType.FIXED.getType());
			d.setDebtId("3");
			d.setDebtDescription("No se reconoce el rubro de consulta");
			d.setFixedDebtAmount("0.00");
			d.setVariableDebtAmountMin("0.00");
			d.setVariableDebtAmountMax("0.00");
			d.setExpiration(new Date());
			response.setDebt(d);
			return response;
		}

		Debt d = new Debt();
		d.setDebtType(debtType.FIXED.getType());
		d.setDebtId(debtId);
		d.setDebtDescription(ACCOUNT);
		d.setFixedDebtAmount("0.00");
		d.setVariableDebtAmountMin("0.00");
		d.setVariableDebtAmountMax("0.00");
		d.setExpiration(new Date());
		try {
			Statement cs = findStatement(debtId);
			if (cs != null) {
				if (cs.getBonds().size() > 0) {
					BigDecimal vl = BigDecimal.ZERO;

					for (Bond b : cs.getBonds()) {

						if (b.getAccount().equals(ACCOUNT)) {
							vl = vl.add(b.getTotal());
						}
					}
					if (vl.equals(BigDecimal.ZERO)) {
						d.setDebtDescription("No existen obligaciones de: " + ACCOUNT);
						d.setDebtId("2");
					}

					d.setFixedDebtAmount(String.valueOf(vl.doubleValue()));
					d.setVariableDebtAmountMin(String.valueOf(vl.doubleValue()));
					d.setVariableDebtAmountMax(String.valueOf(vl.doubleValue()));
					d.setExpiration(new Date());
				}else{
					d.setDebtDescription("No existen obligaciones de: " + ACCOUNT);
					d.setDebtId("2");
				}
			} else {
				d.setDebtDescription("No se puede realizar consultas municipales");
				d.setDebtId("2");
			}
		} catch (PayoutNotAllowed e) {
			d.setDebtDescription("Pago no permitido");
			d.setDebtId("3");
			e.printStackTrace();
		} catch (TaxpayerNotFound e) {
			d.setDebtDescription("Contribuyente no encontrado");
			d.setDebtId("3");
			e.printStackTrace();
		} catch (InvalidUser e) {
			d.setDebtId("3");
			d.setDebtDescription("Usuario invalido");
			e.printStackTrace();
		} catch (HasNoObligations e) {
			d.setDebtDescription("No existen obligaciones de: " + ACCOUNT);
			d.setDebtId("3");
			e.printStackTrace();
		} catch (NotOpenTill e) {
			d.setDebtDescription(ERROR_NOT_OPEN_TILL);
			d.setDebtId("3");
			e.printStackTrace();
		} catch (NotActiveWorkday e) {
			d.setDebtDescription(ERROR_NOT_ACTIVE_DAY);
			d.setDebtId("3");
			e.printStackTrace();
		}

		response.setDebt(d);
		return response;

	}

	public DebtPaymentNotificationResponse debtPaymentNotification(String debtId, String amount, String transactionId,
			String utfi, Date queryDate, String id, Date transDate, String idAgent) {
		System.out.println("==============> METODO debtPaymentNotification");
		System.out.println("==============> debtId: " + debtId);
		System.out.println("==============> amount: " + amount);
		System.out.println("==============> transactionId: " + transactionId);
		System.out.println("==============> utfi: " + utfi);
		System.out.println("==============> queryDate: " + queryDate);
		System.out.println("==============> id: " + id);
		System.out.println("==============> transDate: " + transDate);
		System.out.println("==============> idAgent: " + idAgent);

		DebtPaymentNotificationResponse response = new DebtPaymentNotificationResponse();
		// valores por defecto
		response.setDebtId(debtId);
		response.setOperationResult("2");
		response.setExternalTransactionId(transactionId);
		response.setErrorCode("");
		response.setMessage("");

		boolean isPaid = Boolean.FALSE;
		Payout payout = new Payout();
		payout.setAmount(BigDecimal.ZERO);
		payout.setBondIds(new ArrayList<Long>());
		payout.setPaymentDate(queryDate);

		String ACCOUNT = getAccount(idAgent);

		Statement cs;
		try {
			cs = findStatement(debtId);

			List<Bond> bonds = cs.getBonds();
			BigDecimal total = BigDecimal.ZERO;

			if (!bonds.isEmpty()) {
				List<Long> idBonds = new ArrayList<Long>();
				for (Bond bond : bonds) {
					if (bond.getAccount().equals(ACCOUNT)) {
						total = total.add(bond.getTotal());
						idBonds.add(bond.getId());
					}
				}

				if (total.compareTo(new BigDecimal(amount)) == 0) {
					payout.setAmount(total);
					payout.setBondIds(idBonds);
					payout.setPaymentDate(new Date());

					ServiceRequest cajero = new ServiceRequest();
					cajero.setIdentificationNumber(debtId);
					cajero.setPassword(PASSWORD_QUERY);
					cajero.setUsername(USERNAME_QUERY);

					Boolean itsPaid = paymentService.registerDeposit(cajero, payout);

					// si se realizo el pago
					if (itsPaid) {
						isPaid = Boolean.TRUE;
						response.setDebtId(debtId);
						response.setOperationResult("1");
						response.setExternalTransactionId(transactionId);
						response.setErrorCode("1");
						response.setMessage("Transaccion realizada con exito");
					} else {
						isPaid = Boolean.FALSE;
						response.setDebtId(debtId);
						response.setOperationResult("2");
						response.setExternalTransactionId(transactionId);
						response.setErrorCode("504");
						response.setMessage("La transacción no se ha podido realizar");
					}
				} else {
					// ERROR lanzar alguna excepcion o devolver algo
					isPaid = Boolean.FALSE;
					response.setDebtId(debtId);
					response.setOperationResult("2");
					response.setExternalTransactionId(transactionId);
					response.setErrorCode("505");
					response.setMessage("Las cantidades de deuda no coinciden");
				}
			}

			String idEmoneyPayment = saveEmoneyPayment(payout, debtId, isPaid, idAgent);
			response.setExternalTransactionId(idEmoneyPayment);
		} catch (PayoutNotAllowed e) {
			isPaid = Boolean.FALSE;
			response.setErrorCode("507");
			response.setMessage("Pago no permitido");
			e.printStackTrace();
		} catch (TaxpayerNotFound e) {
			response.setErrorCode("508");
			response.setMessage("Contribuyente no encontrado");
			isPaid = Boolean.FALSE;
			e.printStackTrace();
		} catch (InvalidUser e) {
			response.setErrorCode("509");
			response.setMessage("Usuario inválido");
			isPaid = Boolean.FALSE;
			e.printStackTrace();
		} catch (HasNoObligations e) {
			response.setErrorCode("509");
			response.setMessage("No tiene deudas pendientes de: " + ACCOUNT);
			isPaid = Boolean.FALSE;
			e.printStackTrace();
		} catch (NotOpenTill e) {
			response.setErrorCode("510");
			response.setMessage(ERROR_NOT_OPEN_TILL);
			isPaid = Boolean.FALSE;
			e.printStackTrace();
		} catch (NotActiveWorkday e) {
			response.setErrorCode("511");
			response.setMessage(ERROR_NOT_ACTIVE_DAY);
			isPaid = Boolean.FALSE;
			e.printStackTrace();
		} catch (InvalidPayout e) {
			response.setErrorCode("512");
			response.setMessage("Pago no permitido por convenios");
			isPaid = Boolean.FALSE;
			e.printStackTrace();
		}

		return response;
	}

	// Para guardar los datos de los pagos de dinero electronico...
	// Jock Samaniego
	@PersistenceContext
	private EntityManager em;

	public String saveEmoneyPayment(Payout payout, String debtId, boolean isPaid, String idAgent) {
		List<Long> mbs = payout.getBondIds();
		String str1 = mbs.toString();
		String str0 = str1.substring(1, str1.length() - 1);
		String str2 = str0.replaceAll("[ |:]", "");
		EMoneyPayment EMP = new EMoneyPayment();
		EMP.setDebtId(debtId);
		EMP.setAmount(payout.getAmount());
		EMP.setIsPaid(isPaid);
		EMP.setMunicipalBonds_id(str2);
		EMP.setDate(payout.getPaymentDate());
		EMP.setTime(payout.getPaymentDate());
		EMP.setAccount(idAgent);
		em.persist(EMP);
		Long idEmoney = EMP.getId();
		String id = idEmoney.toString();
		return id;
	}

	/**
	 * @author macartuche
	 * @date 2016-01-21 10:12
	 * @param accountId
	 *            String cedula de contribuyente
	 * @param utfi
	 *            String Identificador de invocacion para propositos de trazado
	 * @param id
	 *            String Identificador auxiliar para la institucion
	 * @param queryDate
	 *            Date fecha de consulta
	 * @param idAgent
	 *            String Identificador de agente
	 * @return AccountDebtsResponse Consultar todas las deudas municipales y
	 *         devolover una sola
	 */
	public AccountDebtsResponse queryAccountDebts(String accountId, String utfi, String id, Date queryDate,
			String idAgent) {

		System.out.println("METODO queryAccountDebts");
		System.out.println("accountId: " + accountId);
		System.out.println("utfi: " + utfi);
		System.out.println("id: " + id);
		System.out.println("idAgent: " + idAgent);

		AccountDebtsResponse response = new AccountDebtsResponse();
		response.setAccountId(accountId);

		Debt[] debts = new Debt[1];

		String ACCOUNT = getAccount(idAgent);

		if (ACCOUNT.isEmpty()) {// no hay cta
			Debt d = new Debt();
			d.setDebtType(debtType.FIXED.getType());
			d.setDebtId("3");
			d.setDebtDescription("No se reconoce el rubro de consulta");
			d.setFixedDebtAmount("0.00");
			d.setVariableDebtAmountMin("0.00");
			d.setVariableDebtAmountMax("0.00");
			d.setExpiration(new Date());

			debts[0] = d;
			return response;
		}

		Debt d = new Debt();
		d.setDebtType(debtType.FIXED.getType());
		d.setDebtId(accountId);
		d.setDebtDescription(ACCOUNT);
		d.setFixedDebtAmount("0.00");
		d.setVariableDebtAmountMin("0.00");
		d.setVariableDebtAmountMax("0.00");
		d.setExpiration(new Date());

		try {
			Statement st = findStatement(accountId);

			if (st.getBonds().size() > 0) {
				BigDecimal vl = BigDecimal.ZERO;

				for (Bond b : st.getBonds()) {
					if (b.getAccount().equals(ACCOUNT)) {
						vl = vl.add(b.getTotal());
					}
				}

				if (vl.equals(BigDecimal.ZERO)) {
					d.setDebtDescription("No existen obligaciones de: " + ACCOUNT);
					d.setDebtId("2");
				}

				d.setFixedDebtAmount(String.valueOf(vl.doubleValue()));
				d.setVariableDebtAmountMin(String.valueOf(vl.doubleValue()));
				d.setVariableDebtAmountMax(String.valueOf(vl.doubleValue()));
				d.setExpiration(new Date());
			} else {
				d.setDebtDescription("No existen obligaciones de: " + ACCOUNT);
				d.setDebtId("2");
			}
		} catch (PayoutNotAllowed e) {
			d.setDebtDescription("Pago no permitido");
			d.setDebtId("3");
			e.printStackTrace();
		} catch (TaxpayerNotFound e) {
			d.setDebtDescription("Contribuyente no encontrado");
			d.setDebtId("3");
			e.printStackTrace();
		} catch (InvalidUser e) {
			d.setDebtDescription("Usuario invalido");
			d.setDebtId("3");
			e.printStackTrace();
		} catch (HasNoObligations e) {
			d.setDebtDescription("No existen obligaciones de: " + ACCOUNT);
			d.setDebtId("3");
			e.printStackTrace();
		} catch (NotOpenTill e) {
			d.setDebtDescription(ERROR_NOT_OPEN_TILL);
			d.setDebtId("3");
			e.printStackTrace();
		} catch (NotActiveWorkday e) {
			d.setDebtDescription(ERROR_NOT_ACTIVE_DAY);
			d.setDebtId("3");
			e.printStackTrace();
		}

		debts[0] = d;
		response.setDebt(debts);
		return response;
	}

	/*
	 * René Ortega 2016-01-20 Implementacion de metodo para reverso de pago por
	 * medio de dinero electronico
	 */
	public DebtPaymentReversal debtPaymentReversalS(String debtId, String amount, String transactionId, String utfi,
			String id, Date paymentNotificationReversalDate, Date transDate, String idAgent) {
		DebtPaymentReversal response = new DebtPaymentReversal();
		try {

			System.out.println("METODO DebtPaymentReversal");
			System.out.println("debtId: " + debtId);
			System.out.println("amount: " + amount);
			System.out.println("transactionId: " + transactionId);
			System.out.println("utfi: " + utfi);
			System.out.println("id: " + id);
			System.out.println("paymentNotificationReversalDate: " + paymentNotificationReversalDate);
			System.out.println("transDate: " + transDate);
			System.out.println("idAgent: " + idAgent); // =>key

			ServiceRequest sr = new ServiceRequest();
			sr.setIdentificationNumber(debtId);
			sr.setPassword(PASSWORD_QUERY);
			sr.setUsername(USERNAME_QUERY);

			User user = paymentService.findUserByUsername(sr, sr.getUsername());
			if (user.hasRole("ROLE_REVERSE_EMONEY")) {
				// ROLE_REVERSE_EMONEY

				SystemParameterService systemParameterService = ServiceLocator.getInstance()
						.findResource(SystemParameterService.LOCAL_NAME);

				// 1. Buscar si realizo pago por medio de SDE
				List<EMoneyPayment> paids = paymentService.findPaidsForEmoney(sr, debtId, new BigDecimal(amount),
						new Date(), idAgent);
				// 2. comprobar si existen pagos por Emoney para la informacion
				// ingresada
				if (!paids.isEmpty()) {
					IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
					//

					EMoneyPayment paid = paids.get(0);

					String[] arrayIds = paid.getMunicipalBonds_id().split(",");

					List<Long> municipalsBondsIdsToReverse = new ArrayList<Long>();

					for (int i = 0; i < arrayIds.length; i++) {
						municipalsBondsIdsToReverse.clear();
						municipalsBondsIdsToReverse.add(new Long(arrayIds[i]));
						List<Long> depositsToReverse = new ArrayList<Long>();

						depositsToReverse = paymentService.findDepositsIdsForReverse(sr, municipalsBondsIdsToReverse);

						incomeService.reverse(depositsToReverse, "Reverso por medio de Dinero Electronico",
								user.getResident());

						response.setDebtId(debtId);
						// response.setErrorCode("No hay transaccion por
						// reversar en el sistema GIM");
						// Todo que se retornaria aqui cuando falla???
						response.setExternalTransactionId(paid.getId().toString());
						response.setMessage("Operación exitosa");
						response.setOperationResult("1");

						paid.setIsReverse(Boolean.TRUE);
						em.merge(paid);
					}

				} else {
					response.setDebtId(debtId);
					// response.setErrorCode("No hay transaccion por reversar en
					// el sistema GIM");
					response.setErrorCode("504");
					// Todo que se retornaria aqui cuando falla???
					// response.setExternalTransactionId("");
					response.setMessage("Se produjo un error. Consulte con el administrador del sistema.");
					response.setOperationResult("2");
				}

			} else {
				response.setDebtId(debtId);
				// response.setErrorCode("No tiene rol para reversar pagos
				// EMoney");
				response.setErrorCode("505");
				// Todo que se retornaria aqui cuando falla???
				// response.setExternalTransactionId("");
				response.setMessage("Se produjo un error. Consulte con el administrador del sistema.");
				response.setOperationResult("2");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			response.setDebtId(debtId);
			response.setErrorCode("506");
			// Todo que se retornaria aqui cuando falla???
			// response.setExternalTransactionId("");
			response.setMessage("Se produjo un error. Consulte con el administrador del sistema.");
			response.setOperationResult("2");
			return response;
		}
		return response;
	}

	enum debtType {

		FIXED("FIXED"), VARIABLE("VARIABLE");

		private final String type;

		public String getType() {
			return type;
		}

		debtType(String v) {
			this.type = v;
		}

	}
}