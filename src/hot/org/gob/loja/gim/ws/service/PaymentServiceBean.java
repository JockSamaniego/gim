package org.gob.loja.gim.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.gob.gim.common.DateUtils;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.exception.NotActiveWorkdayException;
import org.gob.gim.exception.ReverseAmongPaymentsIsNotAllowedException;
import org.gob.gim.exception.ReverseNotAllowedException;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.facade.IncomeServiceBean;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.loja.gim.ws.dto.Bond;
import org.gob.loja.gim.ws.dto.BondDetail;
import org.gob.loja.gim.ws.dto.ClosingStatement;
import org.gob.loja.gim.ws.dto.Payout;
import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.dto.Statement;
import org.gob.loja.gim.ws.dto.Taxpayer;
import org.gob.loja.gim.ws.dto.TransactionData;
import org.gob.loja.gim.ws.dto.Transfer;
import org.gob.loja.gim.ws.exception.HasNoObligations;
import org.gob.loja.gim.ws.exception.InvalidPayout;
import org.gob.loja.gim.ws.exception.InvalidUser;
import org.gob.loja.gim.ws.exception.NotActiveWorkday;
import org.gob.loja.gim.ws.exception.NotOpenTill;
import org.gob.loja.gim.ws.exception.PayoutNotAllowed;
import org.gob.loja.gim.ws.exception.TaxpayerNotFound;
//import org.gob.loja.gim.ws.exception.HasObligationsExpired;


import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Alert;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.EMoneyPayment;
import ec.gob.gim.income.model.Till;
import ec.gob.gim.income.model.TillPermission;
import ec.gob.gim.income.model.Workday;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.security.model.User;

@Stateless(name = "PaymentService")
@Interceptors(SecurityInterceptor.class)
public class PaymentServiceBean implements PaymentService {
	@PersistenceContext
	private EntityManager em;

	@EJB
	private SystemParameterService systemParameterService;

	@EJB
	private IncomeService incomeService;

	public final String ACCOUNT_CODE_FOR_INTEREST = "ACCOUNT_CODE_FOR_INTEREST";

	public final String ACCOUNT_CODE_FOR_SURCHARGE = "ACCOUNT_CODE_FOR_SURCHARGE";

	public final String ACCOUNT_CODE_FOR_TAX = "ACCOUNT_CODE_FOR_TAX";

	public final String ACCOUNT_CODE_FOR_DISCOUNT = "ACCOUNT_CODE_FOR_DISCOUNT";

	/*@Override
	public Statement findStatement(ServiceRequest request)
			throws PayoutNotAllowed, TaxpayerNotFound, NotActiveWorkday,
			HasNoObligations {
		String identificationNumber = request.getIdentificationNumber();
		// Contribuyente que va a realizar el pago
		Taxpayer taxpayer = findTaxpayer(identificationNumber);
		Date workDayDate;
		if (request.getUsername().compareTo("dabetancourtc") == 0) {
			workDayDate = new GregorianCalendar().getTime();
		} else {
			// Saca fecha de la jornada de recaudacion vigente
			workDayDate = findPaymentDate();
		}

		Long inPaymentAgreementBondsNumber = findInPaymentAgreementBondsNumber(taxpayer
				.getId());

		//si existen acuerdos de pago no se puede pagar
		if (inPaymentAgreementBondsNumber > 0) {
			throw new PayoutNotAllowed();
		} else {
			//Consulta todas las obligaciones pendientes de pago.
			
			List<Bond> bonds = findPendingBonds(taxpayer.getId());
			List<Long> pendingBondIds = new ArrayList<Long>();

			List<Bond> bondsExpired = new ArrayList<Bond>();
			List<Long> expiredBondIds = new ArrayList<Long>();
			for (Bond bond : bonds) {
				pendingBondIds.add(bond.getId());
				if(isExpiredBond(bond)){
					//es expirada
					expiredBondIds.add(bond.getId());
					bondsExpired.add(bond);
				}
			}
			
			if (pendingBondIds.size() > 0) {
				try {
					//TODO se podria sacar en una sola consulta junto con pendingBondIds
					//bonds = findPendingBonds(taxpayer.getId());
					// TODO filtrar solo las vencidas para envial el else
					//si hay vencidas retorna true
					Boolean control = comparateBondsDates(bonds);
					if (!control) {
						incomeService.calculatePayment(workDayDate,
								pendingBondIds, true, true);
					} else {
						incomeService.calculatePayment(workDayDate,
								expiredBondIds, true, true);
						bonds = bondsExpired;
					}
					loadBondsDetail(bonds);
				} catch (EntryDefinitionNotFoundException e) {
					e.printStackTrace();
					throw new PayoutNotAllowed();
				}
			}
			// } else {
			// throw new HasNoObligations();
			// }
			Statement statement = new Statement(taxpayer, bonds, workDayDate);
			return statement;
		}
	}*/
	
	@Override
	public Statement findStatement(ServiceRequest request)
			throws PayoutNotAllowed, TaxpayerNotFound, NotActiveWorkday,
			HasNoObligations {
		System.out.println("rfarmijosm "+request.getIdentificationNumber()+"\t"+request.getUsername());
		String identificationNumber = request.getIdentificationNumber();
		
		//rfarmijosm 2017-02-06 se copia el codigo de jock de otro branch, impedir pagos con alerta por ws
		//Boolean AlertPending = ;
		if (controlAlertResident(identificationNumber)) {
			throw new PayoutNotAllowed();
		} else {

			Taxpayer taxpayer = findTaxpayer(identificationNumber);
			Date workDayDate;
			if (request.getUsername().compareTo("dabetancourtc") == 0) {
				workDayDate = new GregorianCalendar().getTime();
			} else
				workDayDate = findPaymentDate();
			Long inPaymentAgreementBondsNumber = findInPaymentAgreementBondsNumber(taxpayer.getId());

			if (inPaymentAgreementBondsNumber > 0) {
				throw new PayoutNotAllowed();
			} else {
				List<Long> pendingBondIds = hasPendingBonds(taxpayer.getId());
				List<Bond> bonds = new ArrayList<Bond>();
				if (pendingBondIds.size() > 0) {
					try {
						incomeService.calculatePayment(workDayDate, pendingBondIds, true, true);
						bonds = findPendingBonds(taxpayer.getId());
						// esta imprimiendo el log de lo q se retorna quitar
						// luego de las pruebas
						Boolean control = comparateBondsDates(bonds);
						loadBondsDetail(bonds);
					} catch (EntryDefinitionNotFoundException e) {
						e.printStackTrace();
						throw new PayoutNotAllowed();
					}
				}
				// } else {
				// throw new HasNoObligations();
				// }
				Statement statement = new Statement(taxpayer, bonds, workDayDate);
				return statement;
			}
		}
	}

	@Override
	public Boolean registerDeposit(ServiceRequest request, Payout payout)
			throws InvalidPayout, PayoutNotAllowed, TaxpayerNotFound,
			InvalidUser, NotActiveWorkday, NotOpenTill, HasNoObligations {
		// Statement statement = findStatement(request);
		
		System.out.println("start PPPPPPPPPPPPPPP");
		System.out.println(request.getIdentificationNumber()+"\t"+payout.getAmount()+"\t"+payout.getPaymentDate()+"\t"+payout.getBondIds());
		System.out.println("end o PPPPPPPPPPPPPPP");

		Person cashier = findCashier(request.getUsername());

		Till till = findTill(cashier.getId());
		Long tillId = till.getId();

		Date workDayDate = findPaymentDate();
		try {
			TillPermission tillPermission = findTillPermission(cashier.getId(),
					workDayDate);
			if (tillPermission == null
					|| tillPermission.getOpeningTime() == null)
				throw new NotOpenTill();
		} catch (NotOpenTill e) {
			e.printStackTrace();
			throw new NotOpenTill();
		}

		// System.out.println("\n\n\n<<R>> TIME WebService: "+payout.getPaymentDate()+"\n\n\n");
		//
		Date payoutDate = DateUtils.truncate(payout.getPaymentDate());

		if (workDayDate.compareTo(payoutDate) == 0) {
			if (BigDecimal.ZERO.compareTo(payout.getAmount()) < 0) {
				// Taxpayer taxpayer = statement.getTaxpayer();
				Taxpayer taxpayer = findTaxpayer(request
						.getIdentificationNumber());
				BigDecimal totalToPay = findAmountToPay(taxpayer.getId(),
						payout.getBondIds());
				//System.out.println("TOTAL TO PAY FOR SELECTED BONDS ----> "
						//+ totalToPay);
				if (totalToPay != null
						&& totalToPay.compareTo(payout.getAmount()) == 0) {
					try {
						incomeService.save(payout.getPaymentDate(),
								payout.getBondIds(), cashier, tillId);
					} catch (Exception e) {
						e.printStackTrace();
						throw new InvalidPayout();
					}
					em.flush();
					Long paidFromExternalBondStatusId = systemParameterService
							.findParameter(IncomeServiceBean.PAID_FROM_EXTERNAL_CHANNEL_BOND_STATUS);
					persistChangeStatus(payout.getBondIds(),
							paidFromExternalBondStatusId);
					return true;
				}
			}
		} else {
			throw new NotActiveWorkday();
		}
		throw new InvalidPayout();
	}

	public ClosingStatement findDeposits(ServiceRequest request,
			Date paymentDate) throws InvalidUser {
		Person cashier = findCashier(request.getUsername());
		List<Transfer> transfers = findTransfers(paymentDate, cashier.getId());
		BigDecimal total = findTotal(paymentDate, cashier.getId());

		ClosingStatement closingStatement = new ClosingStatement();
		closingStatement.setPaymentDate(paymentDate);
		closingStatement.setTotal(total);
		closingStatement.setTransfers(transfers);

		return closingStatement;
	}

	@SuppressWarnings("unchecked")
	private List<Transfer> findTransfers(Date paymentDate, Long cashierId) {
		Query query = em
				.createNamedQuery("Transfer.findByPaymentDateAndCashier");
		query.setParameter("paymentDate", paymentDate);
		query.setParameter("cashierId", cashierId);
		List<Transfer> transfers = query.getResultList();
		return transfers;
	}

	@SuppressWarnings("unchecked")
	private BigDecimal findTotal(Date paymentDate, Long cashierId) {
		Query query = em
				.createNamedQuery("Transfer.findTotalByPaymentDateAndCashier");
		query.setParameter("paymentDate", paymentDate);
		query.setParameter("cashierId", cashierId);
		List<BigDecimal> totals = query.getResultList();
		System.out.println("ESCALAR CLASS ----> " + totals.get(0));
		BigDecimal total = BigDecimal.ZERO;
		if (totals.get(0) != null)
			total = totals.get(0);
		return total;
	}

	private Person findCashier(String username) throws InvalidUser {
		Query query = em.createNamedQuery("User.findByUsername");
		query.setParameter("name", username);
		try {
			User user = (User) query.getSingleResult();
			query = em.createNamedQuery("User.findResidentByUserId");
			query.setParameter("userId", user.getId());
			Person person = null;
			person = (Person) query.getSingleResult();
			// TODO Es necesario verificar si tiene ROL de cajero?
			return person;
		} catch (Exception e) {
		}

		throw new InvalidUser();

	}

	private Till findTill(Long personId) throws NotOpenTill {
		Query query = em.createNamedQuery("Till.findByPersonId");
		query.setParameter("personId", personId);
		Till till = null;
		till = (Till) query.getSingleResult();
		if (till != null) {
			if (till.isActive() == true) {
				return till;
			}
		}
		throw new NotOpenTill();
	}

	private TillPermission findTillPermission(Long personId, Date workDayDate)
			throws NotOpenTill {
		Query query = em
				.createNamedQuery("TillPermission.findByCashierAndWorkdayDate");
		query.setParameter("date", workDayDate);
		query.setParameter("cashierId", personId);
		TillPermission tillPermission = null;
		tillPermission = (TillPermission) query.getSingleResult();
		if (tillPermission != null) {
			if ((tillPermission.getClosingTime() == null)
					&& (tillPermission.isEnabled())) {
				return tillPermission;
			}
		}
		System.out.println("#################NOT OPEN TILL############");
		throw new NotOpenTill();
	}

	private Date findPaymentDate() throws NotActiveWorkday {
		try {
			Workday workday = incomeService.findActiveWorkday();
			return workday.getDate();
		} catch (NotActiveWorkdayException e) {
			throw new NotActiveWorkday();
		}
	}

	private void persistChangeStatus(List<Long> municipalBondIds,
			Long municipalBondStatusId) {
		Query query = em.createNamedQuery("MunicipalBond.changeStatus");
		query.setParameter("municipalBondIds", municipalBondIds);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);
		query.executeUpdate();
	}

	private BigDecimal findAmountToPay(Long taxpayerId, List<Long> selectedBonds) {
		Long pendingBondStatusId = systemParameterService
				.findParameter(IncomeServiceBean.PENDING_BOND_STATUS);
		Query query = em.createNamedQuery("Bond.findTotalToPay");
		query.setParameter("residentId", taxpayerId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("pendingBondStatusId", pendingBondStatusId);
		query.setParameter("selectedBonds", selectedBonds);
		BigDecimal totalToPay = (BigDecimal) query.getSingleResult();
		return totalToPay;
	}

	private Long findInPaymentAgreementBondsNumber(Long taxpayerId) {
		Long inPaymentAgreementBondStatusId = systemParameterService
				.findParameter(IncomeServiceBean.IN_PAYMENT_AGREEMENT_BOND_STATUS);
		System.out.println("BOND STATUS ---->inPaymentAgreementBondStatusId "
				+ inPaymentAgreementBondStatusId);
		Query query = em.createNamedQuery("Bond.countByStatusAndResidentId");
		query.setParameter("residentId", taxpayerId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("pendingBondStatusId",
				inPaymentAgreementBondStatusId);
		Long size = (Long) query.getSingleResult();
		return size;
	}

	@SuppressWarnings("unchecked")
	private List<Long> hasPendingBonds(Long taxpayerId) {
		Long pendingBondStatusId = systemParameterService
				.findParameter(IncomeServiceBean.PENDING_BOND_STATUS);
		Query query = em.createNamedQuery("Bond.findIdsByStatusAndResidentId");
		query.setParameter("residentId", taxpayerId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("pendingBondStatusId", pendingBondStatusId);
		List<Long> ids = query.getResultList();
		System.out.println("PENDING BONDS TOTAL ---->" + ids.size());
		return ids;
	}

	@SuppressWarnings("unchecked")
	private List<Bond> findPendingBonds(Long taxpayerId) {
		Long pendingBondStatusId = systemParameterService
				.findParameter(IncomeServiceBean.PENDING_BOND_STATUS);
		Query query = em.createNamedQuery("Bond.findByStatusAndResidentId");
		query.setParameter("residentId", taxpayerId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("pendingBondStatusId", pendingBondStatusId);
		List<Bond> bonds = query.getResultList();
		// System.out.println("RECORRIENDO RESULTADOS");
		for (Bond bond : bonds) {
			System.out.println("L===========>" + bond.getServiceDate());
		}
		// System.out.println("BONDS TOTAL ---->" + bonds.size());
		return bonds;

	}
	
	// by Jock Samaniego.......
	private Boolean controlAlertResident(String identificationNumber) {
		List<Alert> alerts = new ArrayList<Alert>();
		Query query = em.createNamedQuery("Alert.findPendingAlertsByResidentIdNum");
		query.setParameter("residentIdNum", identificationNumber);
		alerts = query.getResultList();
		if (alerts.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// by Jock Samaniego.......
	private Boolean comparateBondsDates(List<Bond> bonds) {
		// Boolean expiratedDate = Boolean.FALSE;
		Date now = DateUtils.truncate(new Date());
		/*
		* rfarmijos 2016-06-21 quito estas lineas y dejo en una sola ya existe
		* un clase q hace los mismo Date today = new Date(); Calendar calendar
		* = Calendar.getInstance(); calendar.setTime(today);
		* calendar.set(Calendar.MILLISECOND, 0); calendar.set(Calendar.SECOND,
		* 0); calendar.set(Calendar.MINUTE, 0); calendar.set(Calendar.HOUR, 0);
		*/
		// comparar si
		System.out.println("========>cantidad "+bonds.size());
		for (Bond bond : bonds) {			
			System.out.println(bond.getAccount()+"\t"+bond.getNumber()+"\t"+bond.getDiscounts()+
					"\t"+bond.getInterests()+"\t"+bond.getSurcharges()+"\t"+bond.getTaxes()+"\t"+bond.getTotal());
			/*if (now.compareTo(bond.getExpirationDate()) == 1) {
				// System.out.println("=======================> deuda expirada");
				// expiratedDate = Boolean.TRUE;
				return true;
			}*/
		}
		// return expiratedDate;
		return false;
	}

	private Boolean isExpiredBond(Bond bond) {
		Date now = DateUtils.truncate(new Date());
 
		if (now.compareTo(bond.getExpirationDate()) == 1) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void loadBondsDetail(List<Bond> bonds) {
		String accountCodeDiscount = systemParameterService
				.findParameter(ACCOUNT_CODE_FOR_DISCOUNT);
		String accountCodeInterest = systemParameterService
				.findParameter(ACCOUNT_CODE_FOR_INTEREST);
		String accountCodeSurcharge = systemParameterService
				.findParameter(ACCOUNT_CODE_FOR_SURCHARGE);
		String accountCodeTax = systemParameterService
				.findParameter(ACCOUNT_CODE_FOR_TAX);
		List<Long> bondIds = new ArrayList<Long>();
		for (Bond bond : bonds) {
			bondIds.add(bond.getId());
		}
		Query query = em.createNamedQuery("BondDetail.findBondDetailByIds");
		query.setParameter("bondIds", bondIds);
		List<BondDetail> bondDetails = new ArrayList<BondDetail>();
		bondDetails = query.getResultList();
		for (Bond bond : bonds) {
			List<BondDetail> bdAux = new ArrayList<BondDetail>();
			for (BondDetail bd : bondDetails) {
				if (bd.getBondId().equals(bond.getId())) {
					// if(bd.getBondId().compareTo(bond.getId()) == 0){
					// System.out.println("Detalle: "+bd.getName()+" $"+bd.getPartialValue());
					bdAux.add(bd);
				}
			}
			bond.setBondsDetail(bdAux);
			BondDetail bondDetail = new BondDetail(bond.getId(),
					accountCodeInterest, "intereses", bond.getInterests());
			bond.getBondsDetail().add(bondDetail);
			bondDetail = new BondDetail(bond.getId(), accountCodeSurcharge,
					"recargos", bond.getSurcharges());
			bond.getBondsDetail().add(bondDetail);
			bondDetail = new BondDetail(bond.getId(), accountCodeTax,
					"impuestos", bond.getTaxes());
			bond.getBondsDetail().add(bondDetail);
			bondDetail = new BondDetail(bond.getId(), accountCodeDiscount,
					"descuentos", bond.getDiscounts().multiply(
							BigDecimal.valueOf(-1)));
			bond.getBondsDetail().add(bondDetail);
		}
		// Query query = em.createNamedQuery("BondDetail.findBondDetailById");
		// String accountCodeDiscount =
		// systemParameterService.findParameter(ACCOUNT_CODE_FOR_DISCOUNT);
		// String accountCodeInterest =
		// systemParameterService.findParameter(ACCOUNT_CODE_FOR_INTEREST);
		// String accountCodeSurcharge =
		// systemParameterService.findParameter(ACCOUNT_CODE_FOR_SURCHARGE);
		// String accountCodeTax =
		// systemParameterService.findParameter(ACCOUNT_CODE_FOR_TAX);
		// for (Bond bond : bonds){
		// query.setParameter("bondId", bond.getId());
		// bond.setBondsDetail(query.getResultList());
		// // BondDetail bondDetail = new BondDetail(bondId, subLineAccount,
		// name, partialValue)
		// BondDetail bondDetail = new BondDetail(bond.getId(),
		// accountCodeInterest, "intereses", bond.getInterests());
		// bond.getBondsDetail().add(bondDetail);
		// bondDetail = new BondDetail(bond.getId(), accountCodeSurcharge,
		// "recargos", bond.getSurcharges());
		// bond.getBondsDetail().add(bondDetail);
		// bondDetail = new BondDetail(bond.getId(), accountCodeTax,
		// "impuestos", bond.getTaxes());
		// bond.getBondsDetail().add(bondDetail);
		// bondDetail = new BondDetail(bond.getId(), accountCodeDiscount,
		// "descuentos", bond.getDiscounts().multiply(BigDecimal.valueOf(-1)));
		// bond.getBondsDetail().add(bondDetail);
		// }
	}

	private Taxpayer findTaxpayer(String identificationNumber)
			throws TaxpayerNotFound {
		Query query = em
				.createNamedQuery("Taxpayer.findByIdentificationNumber");
		query.setParameter("identificationNumber", identificationNumber);
		try {
			Taxpayer taxpayer = (Taxpayer) query.getSingleResult();
			if (taxpayer == null || taxpayer.getId() == null) {
				throw new TaxpayerNotFound();
			}
			return taxpayer;
		} catch (EJBException NoResultException) {
			throw new TaxpayerNotFound();
		} catch (NoResultException NoResultException) {
			throw new TaxpayerNotFound();
		}
	}

	@Override
	public Boolean isTillOpen(ServiceRequest request) throws NotActiveWorkday,
			NotOpenTill, InvalidUser {
		System.out.println(">?");
		Boolean isOpen = true;
		Person cashier = findCashier(request.getUsername());
		// Till till = findTill(cashier.getId());
		// Long tillId = till.getId();
		Date workDayDate = findPaymentDate();
		try {
			TillPermission tillPermission = findTillPermission(cashier.getId(),
					workDayDate);
			if (tillPermission == null
					|| tillPermission.getOpeningTime() == null) {
				isOpen = Boolean.FALSE;
			}
			// throw new NotOpenTill();
		} catch (NotOpenTill e) {
			isOpen = Boolean.FALSE;
			// e.printStackTrace();
			// throw new NotOpenTill();
		}
		System.out.println("---> " + isOpen);
		return isOpen;
	}

	@Override
	public List<EMoneyPayment> findPaidsForEmoney(ServiceRequest request,
			String debtId, BigDecimal amount, Date date, String idAgent) {
		Query query = em
				.createNamedQuery("EMoneyPayment.findPaymentByDebtIdAmountAndDate");

		query.setParameter("debtId", debtId);
		query.setParameter("amount", amount);
		query.setParameter("date", date, TemporalType.DATE);
		query.setParameter("account", idAgent);

		List<EMoneyPayment> retorno = query.getResultList();

		System.out.println("numero de elementos retornados de EmoneyPayment:"
				+ retorno.size());

		return retorno;
	}

	@Override
	public User findUserByUsername(ServiceRequest request, String username) {
		Query query = em.createNamedQuery("User.findByUsername");
		query.setParameter("name", username);
		User user = (User) query.getSingleResult();
		return user;
	}

	@Override
	public List<Long> findDepositsIdsForReverse(ServiceRequest request,
			List<Long> municipalBondsIds) {
		List<Long> retorno = new ArrayList<Long>();
		// List<Deposit> aux = new ArrayList<Deposit>();
		Query query = em.createNamedQuery("Deposit.findByMunicipalBondIds");
		query.setParameter("municipalBondIds", municipalBondsIds);
		retorno = query.getResultList();
		return retorno;
	}

	/**
	* macartuche auxiliar emoney
	*/
	@Override
	public String saveEmoneyPayment(ServiceRequest request, Payout payout,
			String debtId, boolean isPaid, String idAgent) {

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
	* macartuche auxiliar emoney
	* 
	* @param paid
	*/
	@Override
	public void updateEMoneyPayment(ServiceRequest request, EMoneyPayment paid) {
		em.merge(paid);
	}

	private final String USERNAME_QUERY = "usuario_emoney";

	@Override
	public void reverse(ServiceRequest request, List<Long> depositsToReverse) {

		User user = findUserByUsername(request, USERNAME_QUERY);
		try {
			incomeService.reverse(depositsToReverse,
					"Reverso por medio de Dinero Electronico",
					user.getResident());
		} catch (ReverseNotAllowedException e) {
			e.printStackTrace();
		} catch (ReverseAmongPaymentsIsNotAllowedException e) {
			e.printStackTrace();
		}
	}

	/**
	* macartuche auxiliar emoney
	*/
	@Override
	public boolean hasRolEmoney(ServiceRequest request) {
		User user = findUserByUsername(request, USERNAME_QUERY);
		return (user.hasRole("ROLE_REVERSE_EMONEY")) ? true : false;
	}

	// by Jock Samaniego..
	@Override
	public String searchPropertyByCadastralCode(ServiceRequest request,
			String cadastralCode) {
		List<Property> properties = new ArrayList<Property>();
		Query query = em.createNamedQuery("Property.findResidentByProperty");
		query.setParameter("code", cadastralCode);
		properties = query.getResultList();
		if (properties.size() > 0) {
			return properties.get(0).getCurrentDomain().getResident()
					.getIdentificationNumber();
		} else {
			return "No existe la clave catastral";
		}
	}

	@Override
	public Statement debtConsult(ServiceRequest request)
			throws PayoutNotAllowed, TaxpayerNotFound, InvalidUser,
			NotActiveWorkday, HasNoObligations {
		String identificationNumber = request.getIdentificationNumber();
		Taxpayer taxpayer = findTaxpayer(identificationNumber);

		Date workDayDate = new GregorianCalendar().getTime();
		/*
		* if (request.getUsername().compareTo("dabetancourtc") == 0)
		* workDayDate = ; else workDayDate = findPaymentDate();
		*/
		Long inPaymentAgreementBondsNumber = findInPaymentAgreementBondsNumber(taxpayer
				.getId());

		/*
		* if (inPaymentAgreementBondsNumber > 0) { throw new
		* PayoutNotAllowed(); } else {
		*/
		List<Long> pendingBondIds = hasPendingBonds(taxpayer.getId());
		List<Bond> bonds = new ArrayList<Bond>();
		if (pendingBondIds.size() > 0) {
			try {
				incomeService.calculatePayment(workDayDate, pendingBondIds,
						true, true);
				bonds = findPendingBonds(taxpayer.getId());
				loadBondsDetail(bonds);
			} catch (EntryDefinitionNotFoundException e) {
				e.printStackTrace();
				throw new PayoutNotAllowed();
			}
		}
		// quitar los id's para q no se realicen pagos
		for (Bond bd : bonds) {
			bd.setId(null);
		}
		Statement statement = new Statement(taxpayer, bonds, workDayDate);
		return statement;
	}

	@Override
	public TransactionData reversePaymentBank(ServiceRequest request,
			List<Long> bondIds, String transactionId) {
		// TODO Auto-generated method stub
		return null;
	}
}
