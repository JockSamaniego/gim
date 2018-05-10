package org.gob.gim.income.action;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.exception.InvoiceNumberOutOfRangeException;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.facade.IncomeServiceBean;
import org.gob.gim.income.view.MunicipalBondItem;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;
import org.gob.loja.gim.ws.dto.FutureBond;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.core.ResourceBundle;
import org.jboss.seam.document.DocumentData;
import org.jboss.seam.document.DocumentStore;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import ec.gob.gim.common.model.Alert;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.CreditNote;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.EntryTotalCollected;
import ec.gob.gim.income.model.Payment;
import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.income.model.PaymentFraction;
import ec.gob.gim.income.model.PaymentMethod;
import ec.gob.gim.income.model.PaymentRestriction;
import ec.gob.gim.income.model.PaymentType;
import ec.gob.gim.income.model.Receipt;
import ec.gob.gim.income.model.TillPermission;
import ec.gob.gim.revenue.model.FinancialInstitution;
import ec.gob.gim.revenue.model.FinancialInstitutionType;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.revenue.model.impugnment.Impugnment;
import ec.gob.gim.security.model.MunicipalbondAux;
import ec.gob.gim.security.model.User;

@Name("paymentHome")
@Scope(ScopeType.CONVERSATION)
public class PaymentHome extends EntityHome<Payment> implements Serializable {

	private static final long serialVersionUID = 1L;

	@In
	FacesMessages facesMessages;

	@Logger
	Log logger;

	private List<MunicipalBondItem> municipalBondItems = new ArrayList<MunicipalBondItem>();
	
	private List<MunicipalBond> municipalBondSubscriptionsItems = new ArrayList<MunicipalBond>();

	private List<MunicipalBond> municipalBonds;

	private List<MunicipalBond> selectedBonds;

	private Map<String, Long> inPaymentAgreementBonds;

	private List<PaymentAgreement> paymentAgreements;

	@DataModel
	private List<Deposit> deposits;

	@DataModelSelection
	private Deposit deposit;

	private Boolean hasConflict;

	private Boolean deactivatePaymentAgreement = Boolean.FALSE;
	
	private Boolean deactivateSubscription = Boolean.FALSE;

	private Boolean allBondsSelected;

	private Boolean paymentBlocked;

	// TODO move to resident chooser home
	private String criteria;

	private String identificationNumber;

	private List<Resident> residents;

	private Resident resident;
	// HERE

	private MunicipalBond conflictingBond;

	private BigDecimal deltaDown;

	private BigDecimal deltaUp;

	private Date today;

	private BigDecimal depositTotal = BigDecimal.ZERO;

	private PaymentAgreement paymentAgreement;

	private Boolean isFullPayment = Boolean.TRUE;

	public BigDecimal change;

	private List<FinancialInstitution> banks;

	private List<FinancialInstitution> stateBanks;

	private List<FinancialInstitution> creditCardEmitors;

	private List<CreditNote> creditNotes;

	private List<Alert> pendingAlerts = new ArrayList<Alert>();

	private Long compensationStatusId;

	private Boolean hasCompensationBonds;

	private String paymentFileName;

	private String paymentFilePath;

	private String paymentInstanceName;

	private String paymentRestrictionValue;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	// @author macartuche
	// para deshabilitar boton de registro de pago hasta ingresar los valores y
	// que sea mayor o igual al monto de cobro
	private Boolean canRegisterPayment = true;

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	@In(create = true)
	ReceiptPrintingManager receiptPrintingManager;

	// 2016-07-19T12:53pm
	// @author macartuche
	// @tag recaudacionCoactivas
	private String agreementType;

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	public boolean isWired() {
		return true;
	}

	/**
	 * @author mack Agregado para obtener que deudas conforman el convenio
	 * @param pa
	 */

	private List<MunicipalBond> bondsAgreement;

	@SuppressWarnings("unchecked")
	public void selectPaymentAgreement(BigInteger agreement_id) {

		// System.out.println("=============>"+agreement_id);
		// buscar el convenio en especifico
		this.paymentAgreement = (PaymentAgreement) getEntityManager().find(PaymentAgreement.class,
				new Long(agreement_id.toString()));

		String sentence = "select mb from MunicipalBond mb " + "left join FETCH mb.deposits deposit "
				+ "left join FETCH deposit.payment payment " + "left join FETCH mb.entry entry "
				+ "where mb.paymentAgreement.id=:paId " + "order by mb.creationDate";

		Query q = this.getEntityManager().createQuery(sentence);
		bondsAgreement = q.setParameter("paId", new Long(agreement_id.toString())).getResultList();
	}

	public List<MunicipalBond> getBondsAgreement() {
		return bondsAgreement;
	}

	public void setBondsAgreement(List<MunicipalBond> bondsAgreement) {
		this.bondsAgreement = bondsAgreement;
	}

	public void initialize() {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		compensationStatusId = systemParameterService.findParameter(IncomeService.COMPENSATION_BOND_STATUS);
		paymentRestrictionValue = systemParameterService.findParameter("PAYMENT_RESTRICTION_VALUE");

		FacesContext fcontext = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) fcontext.getExternalContext().getContext();
		String realPath = scontext.getRealPath("/");
		paymentFilePath = realPath + "PDFDocuments" + File.separator;
		int i = realPath.indexOf("server") + 7;
		int j = realPath.indexOf(File.separator.toString(), i);
		paymentInstanceName = realPath.substring(i, j);
		paymentFileName = "payment." + paymentInstanceName + this.getConversationId() + "."
				+ userSession.getPerson().getId() + ".pdf";
		chargeControlImpugnmentStates();
		bondIsWire = Boolean.TRUE;
	}

	public void search() {
		logger.info("RESIDENT identificationNumber " + this.identificationNumber);
		if (this.identificationNumber == null || this.identificationNumber.length() == 0) {
			addFacesMessageFromResourceBundle("resident.enterValidIdentifier");
			return;
		}

		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.setResident(resident);
			findPendingBonds();
			findPendingAlerts(resident.getId());
			if (paymentRestrictionValue.equals(PaymentRestriction.ALL_EXPIRED.toString())) {
				selectAllExpiredItems();
			}

		} catch (Exception e) {
			// e.printStackTrace();
			this.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}
	}

	List<MunicipalBond> paymentsBonds;

	public List<MunicipalBond> getPaymentsBonds() {
		return paymentsBonds;
	}

	public void setPaymentsBonds(List<MunicipalBond> paymentsBonds) {
		this.paymentsBonds = paymentsBonds;
	}

	public void searchVoucher() {

		logger.info("RESIDENT identificationNumber " + this.identificationNumber);
		if (this.identificationNumber == null || this.identificationNumber.length() == 0) {
			addFacesMessageFromResourceBundle("resident.enterValidIdentifier");
			return;
		}

		Query query = getEntityManager().createNamedQuery("Resident.findByIdentificationNumber");
		query.setParameter("identificationNumber", this.identificationNumber);
		try {
			Resident resident = (Resident) query.getSingleResult();
			this.setResident(resident);

			// System.out.println("BD: " + resident.getName());
			this.criteria = this.identificationNumber;
			// searchByCriteria();
			findPaymentsBonds();
			// System.out.println("=======>");

		} catch (Exception e) {
			this.setResident(null);
			addFacesMessageFromResourceBundle("resident.notFound");
		}

	}

	private void resetPaymentTotals() {
		getInstance().setValue(BigDecimal.ZERO);
		depositTotal = BigDecimal.ZERO;
		pendingAlerts.clear();
		paymentBlocked = false;
	}

	public boolean isPair(int size) {
		if (size % 2 == 0)
			return true;
		return false;
	}

	public boolean isCashier() {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		systemParameterService.initialize();
		String cashierRoleName = systemParameterService.findParameter("ROLE_NAME_CASHIER");
		User user = userSession.getUser();
		if (user.hasRole(cashierRoleName)) {
			return true;
		}
		return false;
	}

	public void tillIsActive() {
		if (isCashier()) {
			TillPermission tp = findTillPermission();
			if (tp != null) {
				userSession.setTillPermission(tp);
			} else {
				userSession.setTillPermission(null);
			}

		}
	}

	@SuppressWarnings("unchecked")
	public TillPermission findTillPermission() {
		Date currentDate = new Date();
		Query query = getEntityManager().createNamedQuery("TillPermission.findByPersonIdAndWorkdayDate");
		query.setParameter("personId", userSession.getPerson().getId());
		query.setParameter("date", currentDate);
		List<TillPermission> tillPermissions = query.getResultList();
		if (tillPermissions != null && tillPermissions.size() > 0) {
			if (tillPermissions.get(0).isEnabled()) {
				return tillPermissions.get(0);
			}
		}
		return null;
	}

	public void residentSelectedListener(ActionEvent event) throws Exception {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());

		findPendingBonds();
		findPendingAlerts(resident.getId());
		if (paymentRestrictionValue.equals(PaymentRestriction.ALL_EXPIRED.toString())) {
			selectAllExpiredItems();
		}
	}

	public void residentSelectedListener2(ActionEvent event) throws Exception {
		UIComponent component = event.getComponent();
		Resident resident = (Resident) component.getAttributes().get("resident");
		this.setResident(resident);
		this.setIdentificationNumber(resident.getIdentificationNumber());

		findPaymentsBonds();
		// findPendingBonds();
		// findPendingAlerts(resident.getId());
		// if (paymentRestrictionValue.equals(PaymentRestriction.ALL_EXPIRED
		// .toString())) {
		// selectAllExpiredItems();
		// }
	}

	private void findPendingBonds() throws Exception {
		logger.info("FIND PENDING BONDS " + resident.getName());
		loadLists();
		try {
			findFutureEmision(resident.getId());
			this.inPaymentAgreementBonds = findInPaymentAgreementBonds(resident.getId());
			this.municipalBondItems = findPendingMunicipalBondItems(resident.getId());
			for (MunicipalBondItem mbi : municipalBondItems) {
				mbi.calculateTotals(null, null, null);
			}
			//@author Jock
//			this.municipalBondSubscriptionsItems = findPendingMunicipalBondSubscriptionsItems(resident.getId());
//			for (MunicipalBondItem mbi : municipalBondSubscriptionsItems) {
//				mbi.calculateTotals(null, null, null);
//			}
			calculateSubscriptionTotals();
			
		} catch (EntryDefinitionNotFoundException e) {
			String message = Interpolator.instance()
					.interpolate("#{messages['entryDefinition.entryDefinitionNotFoundException']}", new Object[0]);
			facesMessages.addToControl("", org.jboss.seam.international.StatusMessage.Severity.ERROR, message);
			throw e;
		}
		resetPaymentTotals();
	}

	List<MunicipalBondItem> items;

	public List<MunicipalBondItem> getItems() {
		return items;
	}

	public void setItems(List<MunicipalBondItem> items) {
		this.items = items;
	}

	public Person person;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	private SystemParameterService systemParameterService;

	private List<Person> cashiers;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	public List<Person> getCashiers() {
		return cashiers;
	}

	public void setCashiers(List<Person> cashiers) {
		this.cashiers = cashiers;
	}

	@SuppressWarnings("unchecked")
	public List<Person> findCashiers() {
		if (cashiers == null) {
			if (systemParameterService == null) {
				systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
			}
			String role_name = systemParameterService.findParameter("ROLE_NAME_CASHIER");
			Query query = getPersistenceContext().createNamedQuery("Person.findByRoleName").setParameter("roleName",
					role_name);
			cashiers = query.getResultList();
		}
		return cashiers != null ? cashiers : new ArrayList<Person>();
	}

	@SuppressWarnings("unchecked")
	private void findPaymentsBonds() {

		Calendar time = Calendar.getInstance();
		time.setTime(new Date());
		time.set(Calendar.HOUR_OF_DAY, 0);
		time.set(Calendar.MINUTE, 0);
		time.set(Calendar.SECOND, 0);

		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			// System.out.println("TIME " + time.getTime());

			Query q1 = getEntityManager().createQuery("Select m from MunicipalBond m " + "JOIN m.deposits d "
					+ "JOIN d.payment p "
					+ "where m.resident.id=:resident_id and m.liquidationDate >= :currentDate and p.cashier =:cashier ");
			q1.setParameter("resident_id", resident.getId());
			q1.setParameter("currentDate", formatter.parse(formatter.format(time.getTime())));
			q1.setParameter("cashier", person);

			// System.out.println("Persona=====================================>:
			// "+person.getId());
			paymentsBonds = q1.getResultList();
			// System.out.println("SIZE: " + paymentsBonds.size());
		} catch (ParseException e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
		MunicipalBondItem root = new MunicipalBondItem(null);

		for (MunicipalBond municipalBond : paymentsBonds) {

			String entryId = municipalBond.getEntry().getId().toString();
			MunicipalBondItem item = root.findNode(entryId, municipalBond);

			String groupingCode = municipalBond.getGroupingCode();
			MunicipalBondItem groupingItem = item.findNode(groupingCode, municipalBond);

			MunicipalBondItem mbi = new MunicipalBondItem(municipalBond);
			groupingItem.add(mbi);
		}
		items = root.getMunicipalBondItems();
	}

	private List<FutureBond> futureBonds;
	private BigDecimal totalFutereBond = BigDecimal.ZERO;

	public void findFutureEmision(Long residentId) {
		try {
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
			this.futureBonds = incomeService.findFutureBonds(residentId);
			/*
			 * for (MunicipalBond mb : futureBonds) { totalFutereBond =
			 * totalFutereBond.add(mb.getValue()); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Long> findInPaymentAgreementBonds(Long residentId) {
		try {
			SystemParameterService systemParameterService = ServiceLocator.getInstance()
					.findResource(SystemParameterService.LOCAL_NAME);
			Long pendingMunicipalBondStatusId = systemParameterService
					.findParameter(IncomeServiceBean.IN_PAYMENT_AGREEMENT_BOND_STATUS);
			Query query = getEntityManager().createNativeQuery("SELECT e.id FROM Entry e, MunicipalBond mb "
					+ " WHERE mb.resident_id=:residentId AND " + "       mb.municipalBondType=:municipalBondType AND "
					+ "       mb.municipalBondStatus_id=:municipalBondStatusId AND "
					+ "       mb.entry_id = e.id ORDER BY mb.expirationDate");
			query.setParameter("residentId", residentId);
			query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER.name());
			query.setParameter("municipalBondStatusId", pendingMunicipalBondStatusId);
			List<BigInteger> mbs = query.getResultList();

			// System.out.println("MUNICIPAL BONDS EN CONVENIO " + mbs.size());
			Map<String, Long> inPaymentAgreementMap = new HashMap<String, Long>();
			for (BigInteger entryId : mbs) {
				inPaymentAgreementMap.put(entryId.toString(), entryId.longValue());
			}
			return inPaymentAgreementMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<PaymentAgreement> findPaymentAgreements(Long residentId) {

		Query query = getEntityManager().createNamedQuery("PaymentAgreement.findByResidentIdAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("isActive", Boolean.TRUE);

		logger.info("PARAMETERS -----> residentId " + residentId);
		List<PaymentAgreement> pas = query.getResultList();

		return pas;
	}

	public void calculateTotals() {
		try {
			if (paymentAgreement != null) {
				clearDeposits();
				hasConflict = Boolean.FALSE;
				municipalBonds = findAgreementMunicipalBonds();
				System.out.println("Total de bonds " + municipalBonds.size());
				IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
				incomeService.calculatePayment(municipalBonds, new Date(), true, true);
				logger.info("CALCULATE 2");
				resetPaymentTotals();
				logger.info("CALCULATE 3");

				// obtener el tipo de acuerdo de pago
				// 2016-07-19T12:56
				// @tag recaudacionCoactivas
				if (paymentAgreement.getAgreementType() != null
						&& !paymentAgreement.getAgreementType().name().isEmpty()) {
					agreementType = paymentAgreement.getAgreementType().name();
				} else {
					agreementType = "";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void calculateSubscriptionTotals() {
		try {
			//if (paymentAgreement != null) {
				clearDeposits();
				//hasConflict = Boolean.FALSE;
				municipalBondSubscriptionsItems = findSubscriptionMunicipalBonds();
				System.out.println("Total de bonds subscription ----------------> " + municipalBondSubscriptionsItems.size());
				IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
				incomeService.calculatePayment(municipalBondSubscriptionsItems, new Date(), true, true);
				logger.info("CALCULATE 2");
				resetPaymentTotals();
				logger.info("CALCULATE 3");

				// obtener el tipo de acuerdo de pago
				// 2016-07-19T12:56
				// @tag recaudacionCoactivas
				/*if (paymentAgreement.getAgreementType() != null && !paymentAgreement.getAgreementType().name().isEmpty()) {
					agreementType = paymentAgreement.getAgreementType().name();
				} else {
					agreementType = "";
				}*/
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBond> findAgreementMunicipalBonds() {
		if (paymentAgreement != null) {
			SystemParameterService systemParameterService = ServiceLocator.getInstance()
					.findResource(SystemParameterService.LOCAL_NAME);
			Long inAgreementMunicipalBondStatusId = systemParameterService
					.findParameter(IncomeServiceBean.IN_PAYMENT_AGREEMENT_BOND_STATUS);
			Query query = getEntityManager().createNamedQuery("MunicipalBond.findByPaymentAgreementIdAndStatusId");
			query.setParameter("municipalBondStatusId", inAgreementMunicipalBondStatusId);
			query.setParameter("paymentAgreementId", paymentAgreement.getId());
			List<MunicipalBond> results = query.getResultList();
			for (MunicipalBond municipalBond : results) {
				for (Deposit deposit : municipalBond.getDeposits()) {
					System.out.println("ID ====>" + deposit.getDate());
				}
			}
			return results;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	//rfam 2018-05-09 para generar los convenios de pago
	private List<MunicipalBond> findSubscriptionMunicipalBonds() {
		//if (paymentAgreement != null) {
			SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
			Long inSubscriptionMunicipalBondStatusId = systemParameterService
					.findParameter(IncomeServiceBean.SUBSCRIPTION_BOND_STATUS);
			Query query = getEntityManager().createNamedQuery("MunicipalBond.findBySubscriptionStatusId");
			query.setParameter("municipalBondStatusId", inSubscriptionMunicipalBondStatusId);
			query.setParameter("residentId", resident.getId());
			//query.setParameter("paymentAgreementId", paymentAgreement.getId());
			List<MunicipalBond> results = query.getResultList();
			for (MunicipalBond municipalBond : results) {
				for (Deposit deposit : municipalBond.getDeposits()) {
					System.out.println("ID ====>" + deposit.getDate());
				}
			}
			return results;
		//}
		//return null;
	}

	private List<MunicipalBondItem> findPendingMunicipalBondItems(Long residentId) throws Exception {

		MunicipalBondItem root = new MunicipalBondItem(null);

		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		List<MunicipalBond> mbs = incomeService.findPendingBonds(residentId);
		incomeService.calculatePayment(mbs, new Date(), true, true);
		impugnmentsTotal = new ArrayList<Impugnment>();
		for (MunicipalBond municipalBond : mbs) {
			// System.out.println("BASE IMPONIBLE EN PaymentHome -----> TAXABLE " +
			// municipalBond.getTaxableTotal()
			// + " TAXES TOTAL " + municipalBond.getTaxesTotal());
			String entryId = municipalBond.getEntry().getId().toString();
			MunicipalBondItem item = root.findNode(entryId, municipalBond);

			String groupingCode = municipalBond.getGroupingCode();
			MunicipalBondItem groupingItem = item.findNode(groupingCode, municipalBond);

			MunicipalBondItem mbi = new MunicipalBondItem(municipalBond);
			groupingItem.add(mbi);
			findPendingsImpugnments(municipalBond.getId());
		}
		return root.getMunicipalBondItems();
	}

	// Jock Samaniego
	// Para bloquear emisión
	
	//Jock samaniego.. obligaciones con abonos..........
	
	private List<MunicipalBond> bondSuscriptionts;
	private List<MunicipalBondItem> findPendingMunicipalBondSubscriptionsItems(Long residentId) throws Exception {

		MunicipalBondItem root = new MunicipalBondItem(null);

		bondSuscriptionts = new ArrayList<MunicipalBond>();
		
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		List<MunicipalBond> mbs = incomeService.findPendingBondsSubscriptions(residentId);
		incomeService.calculatePayment(mbs, new Date(), true, true);
		impugnmentsTotal = new ArrayList<Impugnment>();
		for (MunicipalBond municipalBond : mbs) {
			//System.out.println("BASE IMPONIBLE EN PaymentHome -----> TAXABLE " + municipalBond.getTaxableTotal()
					//+ " TAXES TOTAL " + municipalBond.getTaxesTotal());
			String entryId = municipalBond.getEntry().getId().toString();
			MunicipalBondItem item = root.findNode(entryId, municipalBond);

			String groupingCode = municipalBond.getGroupingCode();
			MunicipalBondItem groupingItem = item.findNode(groupingCode, municipalBond);

			MunicipalBondItem mbi = new MunicipalBondItem(municipalBond);
			groupingItem.add(mbi);
			findPendingsImpugnments(municipalBond.getId());
		}
		return root.getMunicipalBondItems();
	}
	
	//Jock Samaniego
	//Para bloquear emisión
	private Boolean isBlocketToCollect = Boolean.FALSE;
	private String blocketMessage;
	private String colorMessage;

	public Boolean getIsBlocketToCollect() {
		return isBlocketToCollect;
	}

	public String getBlocketMessage() {
		return blocketMessage;
	}

	public String getColorMessage() {
		return colorMessage;
	}

	@SuppressWarnings("unchecked")
	private void findPendingAlerts(Long residentId) {
		blocketMessage = "";
		pendingAlerts.clear();
		isBlocketToCollect = Boolean.FALSE;
		colorMessage = "blue";
		Query query = getEntityManager().createNamedQuery("Alert.findPendingAlertsByResidentId");
		query.setParameter("residentId", resident.getId());
		pendingAlerts = query.getResultList();
		if (pendingAlerts.size() > 0) {
			blocketMessage = pendingAlerts.get(0).getOpenDetail();
		}
		for (Alert alert : pendingAlerts) {
			// if (alert.getPriority() == AlertPriority.HIGH) {
			// paymentBlocked = true;
			// }
			if (alert.getAlertType().getIsToCollect()) {
				isBlocketToCollect = Boolean.TRUE;
				blocketMessage = alert.getOpenDetail();
				colorMessage = "red";
			}
		}
	}

	public String getConversationId() {
		return getConversation().getId().toString();
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBondItem> findPendingMunicipalBondItems(Long residentId, String criteria, Date expirationDate,
			BigDecimal amount, FiscalPeriod fiscalPeriod) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long pendingMunicipalBondStatusId = systemParameterService.findParameter(IncomeServiceBean.PENDING_BOND_STATUS);
		Query query = getEntityManager()
				.createNamedQuery("MunicipalBond.findByResidentIdAndTypeAndStatusAndDaysOutOfDateAndAmount");
		query.setParameter("residentId", residentId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusId", pendingMunicipalBondStatusId);
		query.setParameter("municipalEntryName", criteria);
		query.setParameter("municipalBondPaymentTotal", amount);
		query.setParameter("municipalBondFiscalPeriod", fiscalPeriod);
		query.setParameter("municipalBondExpirationDate", expirationDate);

		List<MunicipalBond> mbs = query.getResultList();

		MunicipalBondItem root = new MunicipalBondItem(null);

		for (MunicipalBond municipalBond : mbs) {
			MunicipalBondItem mbi = new MunicipalBondItem(municipalBond);

			String entryId = municipalBond.getEntry().getId().toString();
			MunicipalBondItem item = root.findNode(entryId, municipalBond);

			String groupingCode = municipalBond.getGroupingCode();
			MunicipalBondItem groupingItem = item.findNode(groupingCode, municipalBond);

			groupingItem.add(mbi);
		}

		return root.getMunicipalBondItems();
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBondItem> findPendingMunicipalBondItems(Resident resident, Long entryId) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		Long pendingMunicipalBondStatusId = systemParameterService.findParameter(IncomeServiceBean.PENDING_BOND_STATUS);
		Query query = getEntityManager().createNamedQuery("MunicipalBond.findByResidentIdAndEntryId");
		query.setParameter("residentId", resident.getId());
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusId", pendingMunicipalBondStatusId);
		query.setParameter("entryId", entryId);

		List<MunicipalBond> mbs = query.getResultList();

		MunicipalBondItem root = new MunicipalBondItem(null);

		for (MunicipalBond municipalBond : mbs) {
			MunicipalBondItem mbi = new MunicipalBondItem(municipalBond);

			String _entryId = municipalBond.getEntry().getId().toString();
			MunicipalBondItem item = root.findNode(_entryId, municipalBond);

			String groupingCode = municipalBond.getGroupingCode();
			MunicipalBondItem groupingItem = item.findNode(groupingCode, municipalBond);

			groupingItem.add(mbi);
		}

		return root.getMunicipalBondItems();
	}

	public void build(Resident resident, String criteria, Date expirationDate, BigDecimal amount,
			FiscalPeriod fiscalPeriod) {
		this.setResident(resident);
		this.municipalBondItems = findPendingMunicipalBondItems(resident.getId(), criteria, expirationDate, amount,
				fiscalPeriod);
		for (MunicipalBondItem mbi : municipalBondItems) {
			try {
				mbi.calculateTotals(null, null, null);
			} catch (Exception e) {
				StatusMessages.instance().addFromResourceBundleOrDefault(Severity.ERROR, e.getClass().getSimpleName(),
						e.getMessage(), mbi.getMunicipalBond().getEntry().getName(),
						mbi.getMunicipalBond().getServiceDate());
				logger.error(ResourceBundle.instance().getString(e.getClass().getSimpleName()),
						mbi.getMunicipalBond().getEntry().getName(), mbi.getMunicipalBond().getServiceDate());
			}
		}
	}

	public void build(Resident resident, Long entryId) {
		this.setResident(resident);
		this.municipalBondItems = findPendingMunicipalBondItems(resident, entryId);
		for (MunicipalBondItem mbi : municipalBondItems) {
			try {
				mbi.calculateTotals(null, null, null);
			} catch (Exception e) {
				StatusMessages.instance().addFromResourceBundleOrDefault(Severity.ERROR, e.getClass().getSimpleName(),
						e.getMessage(), mbi.getMunicipalBond().getEntry().getName(),
						mbi.getMunicipalBond().getServiceDate());
				logger.error(ResourceBundle.instance().getString(e.getClass().getSimpleName()),
						mbi.getMunicipalBond().getEntry().getName(), mbi.getMunicipalBond().getServiceDate());
			}
		}
	}

	public void calculateCreditNoteValue(PaymentFraction fraction) {
		// System.out.println("CHECKING CREDIT NOTE VALUE");
		if (fraction.getPaymentType() == PaymentType.CREDIT_NOTE && fraction.getCreditNote() != null) {
			BigDecimal received = fraction.getReceivedAmount();
			BigDecimal availableBalance = fraction.getCreditNote().getAvailableAmount();
			addFacesMessageFromResourceBundle("creditNote.availableBalance", availableBalance);
			if (received != null) {
				if (received.compareTo(availableBalance) > 0) {
					addFacesMessageFromResourceBundle("creditNote.availableAmountIsNotEnough", availableBalance);
				}
			}
		}
	}

	public void calculatePaidTotal() {
		// System.out.println("INICIA CALCULO DE calculatePaidTotal");
		BigDecimal paidTotal = BigDecimal.ZERO;

		if (this.isFullPayment) {
			// System.out.println("" + municipalBondItems.size());
			if (municipalBondItems != null) {
				for (MunicipalBondItem mbi : municipalBondItems) {
					paidTotal = paidTotal.add(mbi.calculatePaymentTotal());
				}
			}
		} else {
			if (hasConflict != null && !hasConflict) {
				paidTotal = depositTotal;
			}
		}
		this.getInstance().setValue(paidTotal);
	}
	
	//Jock samaniego
	
//	public void calculatePaidTotalSubscriptions(){
//		isFullPayment = Boolean.TRUE;
//		BigDecimal paidTotal = BigDecimal.ZERO;
//
//		if (this.isFullPayment) {
//			//System.out.println("" + municipalBondItems.size());
//			if (municipalBondSubscriptionsItems != null) {
//				for (MunicipalBondItem mbi : municipalBondSubscriptionsItems) {
//					paidTotal = paidTotal.add(mbi.calculatePaymentTotal());
//				}
//			}
//		} else {
//			if (hasConflict != null && !hasConflict) {
//				paidTotal = depositTotal;
//			}
//		}
//		this.getInstance().setValue(paidTotal);
//	}

	/**
	 * mac
	 */
	public boolean printBtn = true;

	public boolean isPrintBtn() {
		return printBtn;
	}

	public void setPrintBtn(boolean printBtn) {
		this.printBtn = printBtn;
	}

	public void calculatePaidTotal2() {
		System.out.println("INICIA CALCULO DE calculatePaidTotal2");
		BigDecimal paidTotal = BigDecimal.ZERO;

		if (items != null) {
			for (MunicipalBond mbi : getSelected2()) {
				paidTotal = paidTotal.add(mbi.getPaidTotal());
			}
		}

		// if (this.isFullPayment) {
		// System.out.println("" + items.size());
		// if (municipalBondItems != null) {
		// for (MunicipalBondItem mbi : items) {
		// paidTotal = paidTotal.add(mbi.calculatePaymentTotal());
		// }
		// }
		// } else {
		// if (hasConflict != null && !hasConflict) {
		// paidTotal = depositTotal;
		// }
		// }

		System.out.println("PAIDTOTAL " + paidTotal);
		if (paidTotal.compareTo(BigDecimal.ZERO) == 1) {
			printBtn = false;
		}

		this.getInstance().setValue(paidTotal);
	}

	@SuppressWarnings("unchecked")
	public void searchByCriteria() {
		logger.info("SEARCH BY CRITERIA " + this.criteria);
		if (this.criteria != null && !this.criteria.isEmpty()) {
			Query query = getEntityManager().createNamedQuery("Resident.findByCriteria");
			query.setParameter("criteria", this.criteria);
			residents = query.getResultList();
		}
	}

	public void clearSearchPanel() {
		this.setCriteria(null);
		residents = null;
	}

	public List<Resident> getResidents() {
		return residents;
	}

	public String getCriteria() {
		return criteria;
	}

	public void selectAllItems() {
		for (MunicipalBondItem mbi : municipalBondItems) {
			mbi.setIsSelected(allBondsSelected);
		}
		if ((!allBondsSelected) && (paymentRestrictionValue.equals(PaymentRestriction.ALL_EXPIRED.toString()))
				&& (!resident.isEnabledIndividualPayment())) {
			selectAllExpiredItems();
		}
		calculatePaidTotal();
	}

	/**
	 * 
	 */
	public void selectAllItems2() {
		for (MunicipalBondItem mbi : items) {
			mbi.setIsSelected(allBondsSelected);
		}
		if ((!allBondsSelected) && (paymentRestrictionValue.equals(PaymentRestriction.ALL_EXPIRED.toString()))
				&& (!resident.isEnabledIndividualPayment())) {
			selectAllExpiredItems();
		}
		calculatePaidTotal2();
	}

	public void selectAllExpiredItems() {
		for (MunicipalBondItem mbi : municipalBondItems) {
			if (mbi.getMunicipalBond().getId() == null) {
				for (MunicipalBondItem mbi1 : mbi.getMunicipalBondItems()) {
					if (mbi1.getMunicipalBond().getId() == null) {
						for (MunicipalBondItem mbi2 : mbi1.getMunicipalBondItems()) {
							if (mbi2.getMunicipalBond().getExpirationDate()
									.before(GregorianCalendar.getInstance().getTime()))
								mbi2.setIsSelected(true);
						}
					}
				}
			}
		}
		calculatePaidTotal();
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public List<PaymentType> getPaymentTypes() {
		if (getHasCompensationCashierRole()) {
			return Arrays.asList(PaymentType.values());
		} else {
			return Arrays.asList(PaymentType.getRestrictedPaymentTypes());
		}
	}

	private Boolean getHasCompensationCashierRole() {
		return userSession.hasRole(UserSession.ROLE_NAME_COMPENSATION_CASHIER);
	}

	/*
	 * private Boolean getIsPublicInstitution(){ Boolean isPublicInstitution =
	 * Boolean.FALSE; if(this.resident!=null && this.resident.getClass() ==
	 * LegalEntity.class){ LegalEntity institution = (LegalEntity) resident;
	 * if(institution.getLegalEntityType() == LegalEntityType.PUBLIC){
	 * isPublicInstitution = Boolean.TRUE; } } //System.out.println(
	 * "IS PUBLIC INSTITUTION "+isPublicInstitution); return isPublicInstitution; }
	 */

	public Boolean getIsAccountStateButtonRendered() {
		Boolean isRendered = Boolean.FALSE;
		if (resident != null) {
			// isRendered = getIsPublicInstitution() &&
			// getHasCompensationCashierRole();
			isRendered = resident.getIsEnabledForDeferredPayments() && getHasCompensationCashierRole();
		}
		return isRendered;
	}

	public List<MunicipalBondItem> getMunicipalBondItems() {
		return municipalBondItems;
	}

	public void setMunicipalBondItems(List<MunicipalBondItem> municipalBondItems) {
		this.municipalBondItems = municipalBondItems;
	}
	


	public List<MunicipalBond> getMunicipalBondSubscriptionsItems() {
		return municipalBondSubscriptionsItems;
	}

	public void setMunicipalBondSubscriptionsItems(
			List<MunicipalBond> municipalBondSubscriptionsItems) {
		this.municipalBondSubscriptionsItems = municipalBondSubscriptionsItems;
	}

	public Boolean getAllBondsSelected() {
		return allBondsSelected;
	}

	public void setAllBondsSelected(Boolean allBondsSelected) {
		this.allBondsSelected = allBondsSelected;
	}

	private Boolean enableSubscription=Boolean.FALSE;
	public void changeSelectedTab(ValueChangeEvent vce) {
		//@author mack
				//@date 2018-05-09
				//cambio tab de abonos
				if(vce.getNewValue().equals("municipalBondsSubscriptions")) {
					enableSubscription = Boolean.TRUE;
					isFullPayment = Boolean.TRUE;
				}else if(vce.getNewValue().equals("municipalBondsTab")){
					isFullPayment = Boolean.TRUE;
				}else if(vce.getNewValue().equals("paymentAgreementsTab")){
					isFullPayment = Boolean.FALSE;
				}
		
		//isFullPayment = !isFullPayment;
		if (!isFullPayment) {
			this.paymentAgreements = findPaymentAgreements(resident.getId());
		}
		resetPaymentTotals();
		conflictingBond = null;
		hasConflict = false;
			
	}

	public Boolean getIsFullPayment() {
		return isFullPayment;
	}

	public Boolean getIsPaymentButtonDisabled() {
		BigDecimal paymentValue = this.getInstance().getValue();
		if (paymentValue == null || BigDecimal.ZERO.equals(paymentValue)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void clearFractions() {
		getInstance().getPaymentFractions().clear();
		getInstance().add(new PaymentFraction());
		change = BigDecimal.ZERO;
	}

	public void clearValues(PaymentFraction fraction) {
		// System.out.println("VALUES CLEARED");
		fraction.setReceivedAmount(BigDecimal.ZERO);
		fraction.setFinantialInstitution(null);
		fraction.setDocumentNumber(null);
		fraction.setAccountNumber(null);
		calculateChange();
	}

	private Boolean canPass = false;

	public void increase() {
		depositTotal = depositTotal.add(deltaUp);

		// mac
		canPass = true;
		generateDeposits();
	}

	public void decrease() {
		depositTotal = depositTotal.subtract(deltaDown);
		removeConflictingDeposit();
		generateDeposits();
	}

	private void removeConflictingDeposit() {
		if (conflictingBond != null) {
			Deposit toRemove = null;
			for (Deposit d : conflictingBond.getDeposits()) {
				if (d.getId() == null) {
					toRemove = d;
				}
			}

			if (toRemove != null) {
				conflictingBond.remove(toRemove);
			}
			conflictingBond = null;
		}
	}

	@Override
	protected Payment createInstance() {
		this.instance = super.createInstance();
		Person cashier = userSession.getPerson();
		today = new Date();
		this.instance.setCashier(cashier);
		this.instance.setDate(today);
		this.instance.setTime(today);
		return this.instance;
	}

	private void clearDeposits() {
		if (municipalBonds != null) {
			for (MunicipalBond mb : municipalBonds) {
				if (mb.getDeposits() != null && mb.getDeposits().size() > 0) {
					Deposit lastDeposit = (Deposit) Arrays.asList(mb.getDeposits().toArray()).get(mb.getDeposits().size() - 1);
					if (lastDeposit != null && lastDeposit.getId() == null) {
						mb.getDeposits().remove(lastDeposit);
						deposits.remove(lastDeposit);
						lastDeposit.getPayment().remove(lastDeposit);
					}
				}
			}
		}
	}

	public void confirmCompensationAccountState() {
		selectedBonds = getSelected();
		// updateBonds();
	}

	public String saveForCompensationPayment() {
		// System.out.println("SAVING FOR COMPENSATION PAYMENT");

		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		try {
			Long tillId = userSession.getTillPermission().getTill().getId();
			incomeService.saveForCompensationPayment(selectedBonds, tillId);
			return "printCompensationAccountState";
		} catch (InvoiceNumberOutOfRangeException e) {
			addFacesMessageFromResourceBundle(e.getClass().getSimpleName(), e.getInvoiceNumber());
			return null;
		} catch (Exception e) {
			addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
			addFacesMessageFromResourceBundle("Problemas con el servicio de facturacion electronica");
			e.printStackTrace();
			return null;
		}
	}

	public String persist() {

		// System.out.println("PERSIST INICIO");

		if (!getIsPaymentOk()) {
			if (!paymentBlocked)
				facesMessages.addToControl("savePaymentBtn", Severity.ERROR,
						"#{messages['payment.paymentDetailsMissed']}");
			else
				facesMessages.addToControl("savePaymentBtn", Severity.ERROR,
						"#{messages['payment.paymentBlockedAlertPriority']}");
			return null;
		} else {

			if (isFullPayment) {
				deposits = fillDeposits();
			}

			// @author macartuche
			// unicamente para pagos normales
			/// para convenios se va por otro metodo
			// REVISAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAR EN PRUEBAS SI CONVENIO VIENE POR
			// AQUI!!!!!!!
			String paymentMethod = (this.isPaymentSubscription) ? PaymentMethod.SUBSCRIPTION.name()
					: PaymentMethod.NORMAL.name();
			// fin pago abonos
			IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
			Long paymentAgreementId = deactivatePaymentAgreement ? paymentAgreement.getId() : null;

			try {
				org.jboss.seam.transaction.Transaction.instance().setTransactionTimeout(1800);
				Long tillId = userSession.getTillPermission().getTill().getId();
				// @tag recaudacionCoactivas
				// agregar el tipo de pago por defecto I (solo afecta convenios de pago)
				incomeService.save(deposits, paymentAgreementId, tillId, paymentMethod);
				// fin recaudacionCoactivas
				incomeService.deactivateCreditNotes(getInstance().getPaymentFractions());
				receiptPrintingManager.print(deposits);
				renderingDepositPDF(userSession.getUser().getId());

				// @author macartuche
				// @date 2016-07-01 11:16
				// @tag InteresCeroInstPub
				// No realizar el calculo de interes para instituciones publicas
				// invocar al incomeservice
				// incomeService.compensationPayment(deposits);

			} catch (InvoiceNumberOutOfRangeException e) {
				addFacesMessageFromResourceBundle(e.getClass().getSimpleName(), e.getInvoiceNumber());
			} catch (Exception e) {
				// System.out.println("GZ -----> Exception saving Deposits");
				addFacesMessageFromResourceBundle(e.getClass().getSimpleName());
				e.printStackTrace();
				// System.out.println("GZ -----> Returns error");
				return "unknownError";
			}
			return null;
		}
	}

	public void rePrint() {
		deposits = fillDeposits2();
		receiptPrintingManager.print(deposits);
		renderingDepositPDF(userSession.getUser().getId());
	}

	public void renderingDepositPDF(Long userId) {
		// System.out.println("<<<<----->>>>>renderingDepositPDF:" + paymentFileName);
		PdfExporter pdfExporter = new PdfExporter();
		byte[] pdfBytes = pdfExporter.pdfExport("/income/report/Receipt.xhtml");
		try {
			FileOutputStream fos = new FileOutputStream(paymentFilePath + paymentFileName);
			fos.write(pdfBytes);
			fos.flush();
			fos.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public String redirect() {
		return "persisted";
	}

	public String print() {
		String result = receiptPrintingManager.print(deposit);
		// System.out.println("RESULTADO ----> " + result + " " +
		// receiptPrintingManager);
		return result;
	}

	public String printAll() {
		String result = receiptPrintingManager.print(deposits);
		// System.out.println("RESULTADO ----> " + result + " " +
		// receiptPrintingManager);
		return result;
	}

	public String printCompensationAccount() {
		return "sentToPrint";
	}

	public String confirmCompensationPrinting() {
		return "printed";
	}

	public String confirmPrinting() {
		List<Long> printedDepositIds = new ArrayList<Long>();
		if (deposits == null)
			return "printed";
		for (Deposit deposit : deposits) {
			printedDepositIds.add(deposit.getId());
		}
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		incomeService.setAsPrinted(printedDepositIds);
		return "printed";
	}

	private List<Deposit> fillDeposits() {

		// CAMBIAR DE ACUERDO AL ABONO
		// REALIZAR TIPO CONVENIO
		// @author macartuche
		List<MunicipalBond> selectedBonds = getSelected();
		List<Long> idsBonds = new ArrayList<Long>();


		List<MunicipalBond> temp = new ArrayList<MunicipalBond>();
		if(this.enableSubscription) {
			for (MunicipalBondItem mbi : municipalBondSubscriptionsItems) {
				temp.add(mbi.getMunicipalBond());
				idsBonds.add(mbi.getMunicipalBond().getId());
			}
		}else {
			for (MunicipalBond municipalBond : selectedBonds) {
				idsBonds.add(municipalBond.getId());
			}
			temp = selectedBonds;
		}
		
		
		Query q = this.getEntityManager().createQuery("Select m from  MunicipalBond m where m.id in (:list)");
		q.setParameter("list", idsBonds);
		
		List<MunicipalBond> selectedNew = (List<MunicipalBond>)q.getResultList(); 
		List<MunicipalBond> selectedNew2 = getDiscount(selectedNew, temp);
		 
		
		if (this.isPaymentSubscription) {
			List<Deposit> deps = subscriptionDeposit(selectedNew2);
			return deps;
		} else {

			List<Deposit> deps = new LinkedList<Deposit>();
			for (MunicipalBond mb : selectedNew) {
				Deposit deposit = createDeposit(1);
				deposit.setBalance(BigDecimal.ZERO);
				deposit.setCapital(mb.getValue());
				deposit.setInterest(mb.getInterest());
				deposit.setValue(mb.getPaidTotal());
				mb.add(deposit);
				this.getInstance().add(deposit);
				deps.add(deposit);
			}
			return deps;
		}
	}
	
	private List<MunicipalBond> getDiscount(List<MunicipalBond> bondsBD, List<MunicipalBond> bondsCalculate){
		
		List<MunicipalBond> retornoList = new ArrayList<MunicipalBond>();
		for (MunicipalBond mbDB : bondsBD) {
			for (MunicipalBond mbCalc : bondsCalculate) {
				System.out.println("mbDB "+mbDB.getId());
				System.out.println("mbCalc "+mbCalc.getId());
				if(mbDB.getId().equals(mbCalc.getId())) {
					mbDB.setDiscount(mbCalc.getDiscount());
					retornoList.add(mbDB);
					break;
				}
			}
		}
		return retornoList;
	}

	/**
	 * Para pago de abonos
	 * 
	 * @param paidBonds
	 * @return
	 */
	private List<Deposit> subscriptionDeposit(List<MunicipalBond> paidBonds) {

		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		Integer index = 0;
		MunicipalBond municipalBond = null;
		BigDecimal remaining = getReceivedAmount().setScale(2, RoundingMode.HALF_UP);
		List<Deposit> depositsLocal = new LinkedList<Deposit>();

		while (remaining.compareTo(BigDecimal.ZERO) > 0) {

			if (index < paidBonds.size()) {
				municipalBond = paidBonds.get(index);
				index++;
			} else {
				depositTotal = depositTotal.subtract(remaining);
				break;
			}

			Deposit deposit = null;
			Boolean createDeposit = Boolean.TRUE;
			Boolean hasTaxes = false;
			Boolean hasSurcharge = false;

			if (municipalBond.getDeposits() != null && municipalBond.getDeposits().size() > 0) {
				deposit = (Deposit) Arrays.asList(municipalBond.getDeposits().toArray()).get(municipalBond.getDeposits().size() - 1);
				
				if (deposit.getId() == null) {
					createDeposit = Boolean.FALSE;
				}
			}

			if (createDeposit) {
				deposit = createDeposit(municipalBond.getDeposits().size() + 1);
			}

			BigDecimal value = BigDecimal.ZERO;
			Map<String, Object> plainResult = new HashMap<String, Object>();

			// 4 rubros
			// interes
			List<MunicipalbondAux> ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true,
					"VALID", "I", PaymentMethod.SUBSCRIPTION.name());
			if (ratesList.isEmpty()) { // si no hay elementos no se ha pagado o no se termina de pagar
				plainResult = calculateRate3(incomeService, municipalBond, "I", municipalBond.getInterest(), remaining,
						deposit, PaymentMethod.SUBSCRIPTION.name());
				remaining = (BigDecimal) plainResult.get("remaining");
				value = (BigDecimal) plainResult.get("value");
				hasConflict = (Boolean) plainResult.get("hasConflict");
				deltaUp = (BigDecimal) plainResult.get("deltaUp");
				deltaDown = (BigDecimal) plainResult.get("deltaDown");
			} else {
				value = BigDecimal.ZERO;
			}

			deposit.setInterest(value); // fijar el interes depositado
			deposit.setHasConflict(hasConflict);

			// recargos
			value = BigDecimal.ZERO;
			if (!hasConflict) {
				ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true, "VALID", "S",
						PaymentMethod.SUBSCRIPTION.name());
				if (ratesList.isEmpty()) { // si no hay elementos no se ha pagado o no se termina de pagar
					value = BigDecimal.ZERO;
					plainResult = calculateRate3(incomeService, municipalBond, "S", municipalBond.getSurcharge(),
							remaining, deposit, PaymentMethod.SUBSCRIPTION.name());
					remaining = (BigDecimal) plainResult.get("remaining");
					value = (BigDecimal) plainResult.get("value");
					hasSurcharge = true;
					hasConflict = (Boolean) plainResult.get("hasConflict");
					deltaUp = (BigDecimal) plainResult.get("deltaUp");
					deltaDown = (BigDecimal) plainResult.get("deltaDown");
				}
				deposit.setHasConflict(hasConflict);
			}
			deposit.setSurcharge(value); // fijar los impuestos depositados

			// impuestos
			value = BigDecimal.ZERO;
			if (!hasConflict) {
				ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true, "VALID", "T",
						PaymentMethod.SUBSCRIPTION.name());
				if (ratesList.isEmpty()) { // si no hay elementos no se ha pagado o no se termina de pagar
					value = BigDecimal.ZERO;
					plainResult = calculateRate3(incomeService, municipalBond, "T", municipalBond.getTaxesTotal(),
							remaining, deposit, PaymentMethod.SUBSCRIPTION.name());
					remaining = (BigDecimal) plainResult.get("remaining");
					value = (BigDecimal) plainResult.get("value");
					hasTaxes = true;
					hasConflict = (Boolean) plainResult.get("hasConflict");
					deltaUp = (BigDecimal) plainResult.get("deltaUp");
					deltaDown = (BigDecimal) plainResult.get("deltaDown");
				}
				deposit.setHasConflict(hasConflict);
			}
			deposit.setPaidTaxes(value); // fijar los impuestos depositados

			// capital
			value = BigDecimal.ZERO;
			if (!hasConflict) {
				ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true, "VALID", "C",
						PaymentMethod.SUBSCRIPTION.name());
				if (ratesList.isEmpty()) { // si no hay elementos no se ha pagado o no se termina de pagar
					
					//BigDecimal discount = mbService.calculateDiscount(municipalBond); 
					
					deposit.setDiscount(municipalBond.getDiscount());
					plainResult = calculateRate3(incomeService, municipalBond, "C", municipalBond.getBalance(),
							remaining, deposit, PaymentMethod.SUBSCRIPTION.name());
					remaining = (BigDecimal) plainResult.get("remaining");
					value = (BigDecimal) plainResult.get("value");
					hasConflict = (Boolean) plainResult.get("hasConflict");
					deltaUp = (BigDecimal) plainResult.get("deltaUp");
					deltaDown = (BigDecimal) plainResult.get("deltaDown");
				}
				deposit.setHasConflict(hasConflict);

			}

			// validar si se pone o no el descuento
			BigDecimal validate = value.add(deposit.getInterest()).add(deposit.getSurcharge())
					.add(deposit.getPaidTaxes());
			BigDecimal balanceMinusDiscount = municipalBond.getBalance().add(deposit.getInterest())
					.add(deposit.getSurcharge()).add(deposit.getPaidTaxes()).subtract(municipalBond.getDiscount());
			if (validate.compareTo(balanceMinusDiscount) == 0) {
				deposit.setDiscount(municipalBond.getDiscount());
				deposit.setCapital(value.add(municipalBond.getDiscount())); // fijar el capital depositado
			} else {
				deposit.setDiscount(BigDecimal.ZERO);
				deposit.setCapital(value);
			}

			// calcular el balance del municipalBond
			BigDecimal balance = municipalBond.getBalance().subtract(deposit.getCapital());
			deposit.setBalance(balance);
			deposit.setValue(deposit.getCapital().add(deposit.getInterest()).add(deposit.getPaidTaxes())
					.add(deposit.getSurcharge()).subtract(deposit.getDiscount()));
			
			municipalBond.add(deposit);
			this.getInstance().add(deposit);

			depositsLocal.add(deposit);

		}

		return depositsLocal;
	}

	/**
	 * mac
	 * 
	 * @return
	 */
	private List<Deposit> fillDeposits2() {
		List<MunicipalBond> paidBonds = getSelected2();
		List<Deposit> deps = new LinkedList<Deposit>();
		BigDecimal total = BigDecimal.ZERO;
		for (MunicipalBond mb : paidBonds) {
			Set<Deposit> dep = mb.getDeposits();
			for (Deposit deposit : dep) {
				this.getInstance().add(deposit);
				deps.add(deposit);
				total = total.add(deposit.getValue());
			}
		}

		for (Deposit deposit : deps) {
			System.out.println("ID ===>" + deposit.getId());
		}
		return deps;
	}

	public void generateDeposits() {
		// agregado macartuche
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);

		// System.out.println("GENERATE DEPOSITS -----> STARTS");
		if (depositTotal.compareTo(BigDecimal.ZERO) < 0) {
			depositTotal = BigDecimal.ZERO;
			this.getInstance().setValue(BigDecimal.ZERO);
			return;
		}

		if (paymentAgreement != null) {
			/**
			 * @author macartuche agregado para juicios coactivos tratamiento en metodo
			 *         separado
			 */
			if (paymentAgreement.getAgreementType() != null
					&& paymentAgreement.getAgreementType().toString().equals("COERCIVEJUDGEMENT")) {
				this.coerciveJudgement();
				// solo en juicio coactivo llamar a cobro por fracciones de
				// Impuesto/Recargos/Interes/Capital
				return;
			}

			clearDeposits();
			hasConflict = Boolean.FALSE;
			deactivatePaymentAgreement = Boolean.FALSE;
			deposits = new LinkedList<Deposit>();
			// System.out.println("GENERATE DEPOSITS -----> municipalBonds.size() " +
			// municipalBonds.size());
			BigDecimal remaining = depositTotal;
			// System.out.println("GENERATE DEPOSITS -----> depositTotal " + depositTotal);
			Integer index = 0;

			MunicipalBond municipalBond = null;
			while (remaining.compareTo(BigDecimal.ZERO) > 0) {
				// System.out.println("GENERATE DEPOSITS -----> remaining " + remaining);

				if (index < municipalBonds.size()) {
					municipalBond = municipalBonds.get(index);
					index++;
				} else {
					depositTotal = depositTotal.subtract(remaining);
					deactivatePaymentAgreement = Boolean.TRUE;
					break;
				}

				Deposit deposit = null;
				Boolean createDeposit = Boolean.TRUE;

				if (municipalBond.getDeposits() != null && municipalBond.getDeposits().size() > 0) {
					deposit = (Deposit) Arrays.asList(municipalBond.getDeposits().toArray()).get(municipalBond.getDeposits().size() - 1);
					if (deposit.getId() == null) {
						createDeposit = Boolean.FALSE;
					}
				}

				if (createDeposit) {
					deposit = createDeposit(municipalBond.getDeposits().size() + 1);
				}

				// @author macartuche
				// @date 2016-07-04T16:30
				// @tag recaudacionCoactivas
				Boolean interestIsPayed = false;
				BigDecimal sum = BigDecimal.ZERO;

				List<MunicipalbondAux> list = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true,
						"VALID", "I", PaymentMethod.AGREEMENT.name());

				if (list.isEmpty()) {
					sum = incomeService.sumAccumulatedInterest(municipalBond.getId(), false, "VALID", "I",
							PaymentMethod.AGREEMENT.name());
					if (sum != null && sum.compareTo(BigDecimal.ZERO) >= 0) {
						BigDecimal temp = remaining.add(sum);
						if (temp.compareTo(municipalBond.getInterest()) >= 0)
							interestIsPayed = true;
					}
				}

				BigDecimal interestToPay = BigDecimal.ZERO;
				if (interestIsPayed) {
					// el interes a pagar sera lo faltante de la sumatoria
					interestToPay = municipalBond.getInterest().subtract(sum);// ============>
				} else {
					interestToPay = municipalBond.getInterest();
				}

				// BigDecimal interestToPay = municipalBond.getInterest();
				if (remaining.compareTo(interestToPay) >= 0) {
					deposit.setInterest(interestToPay);
					remaining = remaining.subtract(interestToPay);
					this.getInstance().add(deposit);
					municipalBond.add(deposit);
				} else {
					// rfarmijos 2016-05-23
					// preguntar proceso de pago para fraccionar interes
					if (paymentAgreement.getLowerPercentage()) {
						// deposit.setInterest(interestToPay);
						// remaining = remaining.subtract(interestToPay);
						// this.getInstance().add(deposit);
						// municipalBond.add(deposit);

						// @author macartuche
						// @date 2016-06-20T17:00:00
						// @tag recaudacionCoactivas
						deposit.setInterest(remaining);
						deposit.setCapital(BigDecimal.ZERO);
						this.getInstance().add(deposit);
						municipalBond.add(deposit);

					} else {
						hasConflict = Boolean.TRUE;
						deposit.setHasConflict(Boolean.TRUE);
						conflictingBond = municipalBond;
						deltaUp = interestToPay.subtract(remaining);
						deltaDown = remaining;
						break;
					}

				}

				BigDecimal capitalToPay = municipalBond.getBalance().subtract(municipalBond.getDiscount());
				if (remaining.compareTo(capitalToPay) >= 0) {
					BigDecimal taxesToPay = municipalBond.getTaxesTotal();
					BigDecimal surcharge = municipalBond.getSurcharge();
					BigDecimal discount = municipalBond.getDiscount();

					BigDecimal totalWithTaxes = municipalBond.getBalance().add(taxesToPay);
					totalWithTaxes = totalWithTaxes.add(surcharge);
					totalWithTaxes = totalWithTaxes.subtract(discount);
					if (remaining.compareTo(totalWithTaxes) >= 0) {
						deposit.setCapital(capitalToPay.add(discount));
						deposit.setBalance(BigDecimal.ZERO);
						deposit.setPaidTaxes(taxesToPay);
						deposit.setDiscount(discount);
						deposit.setSurcharge(surcharge);

						remaining = remaining.subtract(capitalToPay);
						remaining = remaining.subtract(taxesToPay);
						remaining = remaining.subtract(surcharge);
						/*
						 * System.out.println("OBLIGACION CANCELADA A ZERO --> " +
						 * municipalBond.getEntry().getDescription() + " " + municipalBond.getId());
						 */
						if (index == municipalBonds.size()) {
							deactivatePaymentAgreement = Boolean.TRUE;
						}
					} else {
						hasConflict = Boolean.TRUE;
						deposit.setHasConflict(Boolean.TRUE);
						conflictingBond = municipalBond;
						deltaUp = totalWithTaxes.subtract(remaining);
						deltaDown = remaining.add(deposit.getInterest());
						break;
					}

				} else {
					// @author macartuche
					// @date 2016-06-20T17:00:00
					// @tag recaudacionCoactivas
					// BigDecimal discount = municipalBond.getDiscount();
					// deposit.setDiscount(discount);
					if (deposit.getInterest().compareTo(interestToPay) < 0) {
						deposit.setCapital(BigDecimal.ZERO);
						remaining = BigDecimal.ZERO;
					} else {
						deposit.setCapital(remaining);
						remaining = BigDecimal.ZERO;
					}

				}
				if (!deactivatePaymentAgreement) {

					// BigDecimal interest = municipalBond.getInterest();
					// BigDecimal taxesToPay = municipalBond.getTaxesTotal();
					// BigDecimal surcharge = municipalBond.getSurcharge();
					// BigDecimal discount = municipalBond.getDiscount();
					//
					// BigDecimal totalWithTaxes =
					// municipalBond.getBalance().add(interest).add(taxesToPay);
					// totalWithTaxes = totalWithTaxes.add(surcharge);
					// totalWithTaxes = totalWithTaxes.subtract(discount);
					//
					// deposit.setBalance(totalWithTaxes.subtract(deposit.getCapital()));
					if (deposit.getInterest().compareTo(municipalBond.getInterest()) < 0) {
						deposit.setBalance(municipalBond.getBalance());
					} else {
						deposit.setBalance(municipalBond.getBalance().subtract(deposit.getCapital()));
					}

					// modificar tambien para el interes acumulado
					// @author macartuche
					// @date 2016-06-06T09:00:00
					// @tag recaudacionCoactivas
					if (interestIsPayed) {
						deposit.setBalance(municipalBond.getBalance().subtract(deposit.getCapital()));
					}
				}
				deposit.setValue(deposit.getCapital().add(deposit.getInterest()).add(deposit.getPaidTaxes())
						.add(deposit.getSurcharge()).subtract(deposit.getDiscount()));
				deposits.add(deposit);
			}

			if (!hasConflict) {
				this.getInstance().setValue(depositTotal);
			} else {
				this.getInstance().setValue(BigDecimal.ZERO);
			}
		}
		// System.out.println("GENERATE DEPOSITS -----> ENDS");
	}

	private void coerciveJudgement() {
		// agregado macartuche
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		clearDeposits();
		hasConflict = Boolean.FALSE;
		deactivatePaymentAgreement = Boolean.FALSE;
		deposits = new LinkedList<Deposit>();
		// System.out.println("GENERATE DEPOSITS -----> municipalBonds.size() " +
		// municipalBonds.size());
		BigDecimal remaining = depositTotal;
		// System.out.println("GENERATE DEPOSITS -----> depositTotal " + depositTotal);
		Integer index = 0;

		MunicipalBond municipalBond = null;

		while (remaining.compareTo(BigDecimal.ZERO) > 0) {
			// System.out.println("GENERATE DEPOSITS -----> remaining " + remaining);

			if (index < municipalBonds.size()) {
				municipalBond = municipalBonds.get(index);
				index++;
			} else {
				depositTotal = depositTotal.subtract(remaining);
				deactivatePaymentAgreement = Boolean.TRUE;
				break;
			}

			Deposit deposit = null;
			Boolean createDeposit = Boolean.TRUE;
			Boolean hasTaxes = false;
			Boolean hasSurcharge = false;

			if (municipalBond.getDeposits() != null && municipalBond.getDeposits().size() > 0) {
				deposit = (Deposit) Arrays.asList(municipalBond.getDeposits().toArray()).get(municipalBond.getDeposits().size() - 1);
				if (deposit.getId() == null) {
					createDeposit = Boolean.FALSE;
				}
			}

			if (createDeposit) {
				deposit = createDeposit(municipalBond.getDeposits().size() + 1);
			}

			BigDecimal value = BigDecimal.ZERO;
			Map<String, Object> plainResult = new HashMap<String, Object>();

			// 4 rubros
			// interes
			List<MunicipalbondAux> ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true,
					"VALID", "I", PaymentMethod.AGREEMENT.name());
			if (ratesList.isEmpty()) { // si no hay elementos no se ha pagado o no se termina de pagar
				plainResult = calculateRate2(incomeService, municipalBond, "I", municipalBond.getInterest(), remaining,
						deposit, PaymentMethod.AGREEMENT.name());
				remaining = (BigDecimal) plainResult.get("remaining");
				value = (BigDecimal) plainResult.get("value");
				hasConflict = (Boolean) plainResult.get("hasConflict");
				deltaUp = (BigDecimal) plainResult.get("deltaUp");
				deltaDown = (BigDecimal) plainResult.get("deltaDown");
			} else {
				value = BigDecimal.ZERO;
			}

			deposit.setInterest(value); // fijar el interes depositado
			deposit.setHasConflict(hasConflict);

			// recargos
			value = BigDecimal.ZERO;
			if (!hasConflict) {
				ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true, "VALID", "S",
						PaymentMethod.AGREEMENT.name());
				if (ratesList.isEmpty()) { // si no hay elementos no se ha pagado o no se termina de pagar
					value = BigDecimal.ZERO;
					plainResult = calculateRate2(incomeService, municipalBond, "S", municipalBond.getSurcharge(),
							remaining, deposit, PaymentMethod.AGREEMENT.name());
					remaining = (BigDecimal) plainResult.get("remaining");
					value = (BigDecimal) plainResult.get("value");
					hasSurcharge = true;
					hasConflict = (Boolean) plainResult.get("hasConflict");
					deltaUp = (BigDecimal) plainResult.get("deltaUp");
					deltaDown = (BigDecimal) plainResult.get("deltaDown");
				}
				deposit.setHasConflict(hasConflict);
			}
			deposit.setSurcharge(value); // fijar los impuestos depositados

			// impuestos
			value = BigDecimal.ZERO;
			if (!hasConflict) {
				ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true, "VALID", "T",
						PaymentMethod.AGREEMENT.name());
				if (ratesList.isEmpty()) { // si no hay elementos no se ha pagado o no se termina de pagar
					value = BigDecimal.ZERO;
					plainResult = calculateRate2(incomeService, municipalBond, "T", municipalBond.getTaxesTotal(),
							remaining, deposit, PaymentMethod.AGREEMENT.name());
					remaining = (BigDecimal) plainResult.get("remaining");
					value = (BigDecimal) plainResult.get("value");
					hasTaxes = true;
					hasConflict = (Boolean) plainResult.get("hasConflict");
					deltaUp = (BigDecimal) plainResult.get("deltaUp");
					deltaDown = (BigDecimal) plainResult.get("deltaDown");
				}
				deposit.setHasConflict(hasConflict);
			}
			deposit.setPaidTaxes(value); // fijar los impuestos depositados

			// capital
			value = BigDecimal.ZERO;
			if (!hasConflict) {
				ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true, "VALID", "C",
						PaymentMethod.AGREEMENT.name());
				if (ratesList.isEmpty()) { // si no hay elementos no se ha pagado o no se termina de pagar
					deposit.setDiscount(municipalBond.getDiscount());
					plainResult = calculateRate2(incomeService, municipalBond, "C", municipalBond.getBalance(),
							remaining, deposit, PaymentMethod.AGREEMENT.name());
					remaining = (BigDecimal) plainResult.get("remaining");
					value = (BigDecimal) plainResult.get("value");
					hasConflict = (Boolean) plainResult.get("hasConflict");
					deltaUp = (BigDecimal) plainResult.get("deltaUp");
					deltaDown = (BigDecimal) plainResult.get("deltaDown");
				}
				deposit.setHasConflict(hasConflict);

			}

			// validar si se pone o no el descuento
			BigDecimal validate = value.add(deposit.getInterest()).add(deposit.getSurcharge())
					.add(deposit.getPaidTaxes());
			BigDecimal balanceMinusDiscount = municipalBond.getBalance().add(deposit.getInterest())
					.add(deposit.getSurcharge()).add(deposit.getPaidTaxes()).subtract(municipalBond.getDiscount());
			if (validate.compareTo(balanceMinusDiscount) == 0) {
				deposit.setDiscount(municipalBond.getDiscount());
				deposit.setCapital(value.add(municipalBond.getDiscount())); // fijar el capital depositado
			} else {
				deposit.setDiscount(BigDecimal.ZERO);
				deposit.setCapital(value);
			}

			// calcular el balance del municipalBond
			BigDecimal balance = municipalBond.getBalance().subtract(deposit.getCapital());

			/*
			 * if(hasSurcharge || hasTaxes){ BigDecimal sumTaxes =
			 * incomeService.sumAccumulatedInterest(municipalBond.getId(), false, "VALID",
			 * "T"); BigDecimal sumSurcharge =
			 * incomeService.sumAccumulatedInterest(municipalBond.getId(), false, "VALID",
			 * "S"); sumTaxes = (sumTaxes==null)? BigDecimal.ZERO : sumTaxes; sumSurcharge =
			 * (sumSurcharge==null)? BigDecimal.ZERO : sumSurcharge; sumTaxes =
			 * sumTaxes.add(deposit.getPaidTaxes()); sumSurcharge =
			 * sumSurcharge.add(deposit.getSurcharge()); BigDecimal sumTotal =
			 * sumTaxes.add(sumSurcharge); balance = balance.subtract(sumTotal); }
			 */
			deposit.setBalance(balance);
			municipalBond.add(deposit);
			this.getInstance().add(deposit);

			if (balance.compareTo(BigDecimal.ZERO) == 0) {
				deactivatePaymentAgreement = Boolean.TRUE;
			}

			System.out.println("**********************************REMAINING: " + remaining);
			System.out.println("Capital: " + deposit.getCapital());
			System.out.println("interes: " + deposit.getInterest());
			System.out.println("impuestos: " + deposit.getPaidTaxes());
			System.out.println("recargos: " + deposit.getSurcharge());
			System.out.println("Descuento: " + deposit.getDiscount());
			System.out.println("Balance: " + deposit.getBalance());

			deposit.setValue(deposit.getCapital().add(deposit.getInterest()).add(deposit.getPaidTaxes())
					.add(deposit.getSurcharge()).subtract(deposit.getDiscount()));
			deposits.add(deposit);

			if (hasConflict) {
				// deposit.setValue(null);
				// deposit.setCapital(null);
				// deposit.setBalance(null);
				break;
			}
			canPass = false;
		}

		if (!hasConflict) {
			this.getInstance().setValue(depositTotal);
		} else {
			this.getInstance().setValue(BigDecimal.ZERO);
		}

	}
	
	
	//Jock samaniego
	public void generateDepositsBySubscriptions() {
		// agregado macartuche
				IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);

				// System.out.println("GENERATE DEPOSITS -----> STARTS");
				if (depositTotal.compareTo(BigDecimal.ZERO) < 0) {
					depositTotal = BigDecimal.ZERO;
					this.getInstance().setValue(BigDecimal.ZERO);
					return;
				}

				//if (paymentAgreement != null) {
					/**
					 * @author macartuche agregado para juicios coactivos tratamiento en metodo
					 *         separado
					 */
//					if (paymentAgreement.getAgreementType() != null
//							&& paymentAgreement.getAgreementType().toString().equals("COERCIVEJUDGEMENT")) {
//						this.coerciveJudgement();
//						// solo en juicio coactivo llamar a cobro por fracciones de
//						// Impuesto/Recargos/Interes/Capital
//						return;
//					}

					clearDeposits();
					hasConflict = Boolean.FALSE;
					deactivateSubscription= Boolean.FALSE;
					deposits = new LinkedList<Deposit>();
					// System.out.println("GENERATE DEPOSITS -----> municipalBonds.size() " +
					// municipalBonds.size());
					BigDecimal remaining = depositTotal;
					// System.out.println("GENERATE DEPOSITS -----> depositTotal " + depositTotal);
					Integer index = 0;

					MunicipalBond municipalBond = null;
					while (remaining.compareTo(BigDecimal.ZERO) > 0) {
						// System.out.println("GENERATE DEPOSITS -----> remaining " + remaining);

						if (index < municipalBondSubscriptionsItems.size()) {
							municipalBond = municipalBondSubscriptionsItems.get(index);
							index++;
						} else {
							depositTotal = depositTotal.subtract(remaining);
							deactivateSubscription = Boolean.TRUE;
							break;
						}

						Deposit deposit = null;
						Boolean createDeposit = Boolean.TRUE;

						if (municipalBond.getDeposits() != null && municipalBond.getDeposits().size() > 0) {
							deposit = municipalBond.getDeposits().get(municipalBond.getDeposits().size() - 1);
							if (deposit.getId() == null) {
								createDeposit = Boolean.FALSE;
							}
						}

						if (createDeposit) {
							deposit = createDeposit(municipalBond.getDeposits().size() + 1);
						}

						// @author macartuche
						// @date 2016-07-04T16:30
						// @tag recaudacionCoactivas
						Boolean interestIsPayed = false;
						BigDecimal sum = BigDecimal.ZERO;

						List<MunicipalbondAux> list = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true,
								"VALID", "I", PaymentMethod.AGREEMENT.name());

						if (list.isEmpty()) {
							sum = incomeService.sumAccumulatedInterest(municipalBond.getId(), false, "VALID", "I",
									PaymentMethod.AGREEMENT.name());
							if (sum != null && sum.compareTo(BigDecimal.ZERO) >= 0) {
								BigDecimal temp = remaining.add(sum);
								if (temp.compareTo(municipalBond.getInterest()) >= 0)
									interestIsPayed = true;
							}
						}

						BigDecimal interestToPay = BigDecimal.ZERO;
						if (interestIsPayed) {
							// el interes a pagar sera lo faltante de la sumatoria
							interestToPay = municipalBond.getInterest().subtract(sum);// ============>
						} else {
							interestToPay = municipalBond.getInterest();
						}

						// BigDecimal interestToPay = municipalBond.getInterest();
						if (remaining.compareTo(interestToPay) >= 0) {
							deposit.setInterest(interestToPay);
							remaining = remaining.subtract(interestToPay);
							this.getInstance().add(deposit);
							municipalBond.add(deposit);
						} else {
							// rfarmijos 2016-05-23
							// preguntar proceso de pago para fraccionar interes
//							if (paymentAgreement.getLowerPercentage()) {
								// deposit.setInterest(interestToPay);
								// remaining = remaining.subtract(interestToPay);
								// this.getInstance().add(deposit);
								// municipalBond.add(deposit);

								// @author macartuche
								// @date 2016-06-20T17:00:00
								// @tag recaudacionCoactivas
								deposit.setInterest(remaining);
								deposit.setCapital(BigDecimal.ZERO);
								this.getInstance().add(deposit);
								municipalBond.add(deposit);

//							} else {
//								hasConflict = Boolean.TRUE;
//								deposit.setHasConflict(Boolean.TRUE);
//								conflictingBond = municipalBond;
//								deltaUp = interestToPay.subtract(remaining);
//								deltaDown = remaining;
//								break;
//							}

						}

						BigDecimal capitalToPay = municipalBond.getBalance().subtract(municipalBond.getDiscount());
						if (remaining.compareTo(capitalToPay) >= 0) {
							BigDecimal taxesToPay = municipalBond.getTaxesTotal();
							BigDecimal surcharge = municipalBond.getSurcharge();
							BigDecimal discount = municipalBond.getDiscount();

							BigDecimal totalWithTaxes = municipalBond.getBalance().add(taxesToPay);
							totalWithTaxes = totalWithTaxes.add(surcharge);
							totalWithTaxes = totalWithTaxes.subtract(discount);
							if (remaining.compareTo(totalWithTaxes) >= 0) {
								deposit.setCapital(capitalToPay.add(discount));
								deposit.setBalance(BigDecimal.ZERO);
								deposit.setPaidTaxes(taxesToPay);
								deposit.setDiscount(discount);
								deposit.setSurcharge(surcharge);

								remaining = remaining.subtract(capitalToPay);
								remaining = remaining.subtract(taxesToPay);
								remaining = remaining.subtract(surcharge);
								/*
								 * System.out.println("OBLIGACION CANCELADA A ZERO --> " +
								 * municipalBond.getEntry().getDescription() + " " + municipalBond.getId());
								 */
								if (index == municipalBondSubscriptionsItems.size()) {
									deactivateSubscription = Boolean.TRUE;
								}
							} else {
								hasConflict = Boolean.TRUE;
								deposit.setHasConflict(Boolean.TRUE);
								conflictingBond = municipalBond;
								deltaUp = totalWithTaxes.subtract(remaining);
								deltaDown = remaining.add(deposit.getInterest());
								break;
							}

						} else {
							// @author macartuche
							// @date 2016-06-20T17:00:00
							// @tag recaudacionCoactivas
							// BigDecimal discount = municipalBond.getDiscount();
							// deposit.setDiscount(discount);
							if (deposit.getInterest().compareTo(interestToPay) < 0) {
								deposit.setCapital(BigDecimal.ZERO);
								remaining = BigDecimal.ZERO;
							} else {
								deposit.setCapital(remaining);
								remaining = BigDecimal.ZERO;
							}

						}
						if (!deactivateSubscription) {

							// BigDecimal interest = municipalBond.getInterest();
							// BigDecimal taxesToPay = municipalBond.getTaxesTotal();
							// BigDecimal surcharge = municipalBond.getSurcharge();
							// BigDecimal discount = municipalBond.getDiscount();
							//
							// BigDecimal totalWithTaxes =
							// municipalBond.getBalance().add(interest).add(taxesToPay);
							// totalWithTaxes = totalWithTaxes.add(surcharge);
							// totalWithTaxes = totalWithTaxes.subtract(discount);
							//
							// deposit.setBalance(totalWithTaxes.subtract(deposit.getCapital()));
							if (deposit.getInterest().compareTo(municipalBond.getInterest()) < 0) {
								deposit.setBalance(municipalBond.getBalance());
							} else {
								deposit.setBalance(municipalBond.getBalance().subtract(deposit.getCapital()));
							}

							// modificar tambien para el interes acumulado
							// @author macartuche
							// @date 2016-06-06T09:00:00
							// @tag recaudacionCoactivas
							if (interestIsPayed) {
								deposit.setBalance(municipalBond.getBalance().subtract(deposit.getCapital()));
							}
						}
						deposit.setValue(deposit.getCapital().add(deposit.getInterest()).add(deposit.getPaidTaxes())
								.add(deposit.getSurcharge()).subtract(deposit.getDiscount()));
						deposits.add(deposit);
					}

					if (!hasConflict) {
						this.getInstance().setValue(depositTotal);
					} else {
						this.getInstance().setValue(BigDecimal.ZERO);
					}
				//}
				// System.out.println("GENERATE DEPOSITS -----> ENDS");
	}

	private Map<String, Object> calculateRate2(IncomeService incomeService, MunicipalBond municipalBond,
			String itemType, BigDecimal itemValue, BigDecimal remaining, Deposit deposit, String paymentMethod) {

		Boolean itemHasDeposit = false;
		BigDecimal sum = BigDecimal.ZERO;
		Map<String, Object> result = new HashMap<String, Object>();
		BigDecimal valueToPay = BigDecimal.ZERO;
		BigDecimal value = BigDecimal.ZERO;
		Boolean hasConflictP = Boolean.FALSE;

		// en caso que el interes/recargo/impuestos sean cero en el municipalbond
		if (itemValue.compareTo(BigDecimal.ZERO) == 0 || remaining.compareTo(BigDecimal.ZERO) == 0) {
			result.put("value", BigDecimal.ZERO);
			result.put("remaining", remaining);
			result.put("hasConflict", hasConflictP);
			result.put("conflictingBond", null);
			result.put("deltaUp", BigDecimal.ZERO);
			result.put("deltaDown", BigDecimal.ZERO);
			return result;
		}

		if (itemType != "C") {
			List<MunicipalbondAux> ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true,
					"VALID", itemType, paymentMethod);
			if (ratesList.isEmpty()) {
				sum = incomeService.sumAccumulatedInterest(municipalBond.getId(), false, "VALID", itemType,
						paymentMethod);
				if (sum != null && sum.compareTo(BigDecimal.ZERO) >= 0) {
					BigDecimal temp = remaining.add(sum);
					if (temp.compareTo(sum) >= 0) {
						itemHasDeposit = true;
					}
				}
			}
		}

		valueToPay = (itemHasDeposit) ? compareCase(itemValue, sum) : itemValue;
		if (itemType.equals("C")) {
			valueToPay = valueToPay.subtract(municipalBond.getDiscount());
		}
		if (remaining.compareTo(valueToPay) >= 0 && valueToPay.compareTo(BigDecimal.ZERO) == 1) {
			value = valueToPay;
			remaining = remaining.subtract(value);
		} else if (valueToPay.compareTo(BigDecimal.ZERO) == 1) {
			// abonos de rubro (impuesto, interes, recargo) - solo los de 20%
			if (paymentAgreement.getLowerPercentage() != null && paymentAgreement.getLowerPercentage()) {
				value = remaining;
				remaining = BigDecimal.ZERO;
			} else {
				hasConflictP = Boolean.TRUE;
				deposit.setHasConflict(Boolean.TRUE);
				conflictingBond = municipalBond;
				deltaUp = valueToPay.subtract(remaining);
				deltaDown = remaining;
			}
		}

		result.put("value", value);
		result.put("remaining", remaining);
		result.put("hasConflict", hasConflictP);
		result.put("conflictingBond", municipalBond);
		result.put("deltaUp", deltaUp);
		result.put("deltaDown", deltaDown);
		return result;
	}

	private Map<String, Object> calculateRate3(IncomeService incomeService, MunicipalBond municipalBond,
			String itemType, BigDecimal itemValue, BigDecimal remaining, Deposit deposit, String paymentMethod) {

		Boolean itemHasDeposit = false;
		BigDecimal sum = BigDecimal.ZERO;
		Map<String, Object> result = new HashMap<String, Object>();
		BigDecimal valueToPay = BigDecimal.ZERO;
		BigDecimal value = BigDecimal.ZERO;
		Boolean hasConflictP = Boolean.FALSE;

		// en caso que el interes/recargo/impuestos sean cero en el municipalbond
		if (itemValue.compareTo(BigDecimal.ZERO) == 0 || remaining.compareTo(BigDecimal.ZERO) == 0) {
			result.put("value", BigDecimal.ZERO);
			result.put("remaining", remaining);
			result.put("hasConflict", hasConflictP);
			result.put("conflictingBond", null);
			result.put("deltaUp", BigDecimal.ZERO);
			result.put("deltaDown", BigDecimal.ZERO);
			return result;
		}

		//if (itemType != "C") {
			List<MunicipalbondAux> ratesList = incomeService.getBondsAuxByIdAndStatus(municipalBond.getId(), true,
					"VALID", itemType, paymentMethod);
			if (ratesList.isEmpty()) {
				sum = incomeService.sumAccumulatedInterest(municipalBond.getId(), false, "VALID", itemType,
						paymentMethod);
				if (sum != null && sum.compareTo(BigDecimal.ZERO) >= 0) {
					BigDecimal temp = remaining.add(sum);
					if (temp.compareTo(sum) >= 0) {
						itemHasDeposit = true;
					}
				}
			}
		//}

		valueToPay = (itemHasDeposit) ? compareCase(itemValue, sum) : itemValue;
		if (itemType.equals("C")) {
			valueToPay = valueToPay.subtract(municipalBond.getDiscount());
		}
		if (remaining.compareTo(valueToPay) >= 0 && valueToPay.compareTo(BigDecimal.ZERO) == 1) {
			value = valueToPay;
			remaining = remaining.subtract(value);
		} else if (valueToPay.compareTo(BigDecimal.ZERO) == 1) {
			// abonos de rubro (impuesto, interes, recargo) - solo los de 20%
			value = remaining;
			remaining = BigDecimal.ZERO;
		}

		result.put("value", value);
		result.put("remaining", remaining);
		result.put("hasConflict", hasConflictP);
		result.put("conflictingBond", municipalBond);
		result.put("deltaUp", deltaUp);
		result.put("deltaDown", deltaDown);
		return result;
	}

	private BigDecimal compareCase(BigDecimal value, BigDecimal sum) {
		BigDecimal realValue = BigDecimal.ZERO;

		if (value.compareTo(sum) > 0) {
			realValue = value.subtract(sum);
		} else if (sum.compareTo(value) >= 0) {
			realValue = value;
		}

		return realValue;
	}

	// public String ITEM_TYPE = "I";

	private Deposit createDeposit(Integer ordinal) {
		Deposit deposit = new Deposit();
		deposit.setDate(today);
		deposit.setTime(today);
		deposit.setNumber(ordinal);
		deposit.setHasConflict(Boolean.FALSE);
		deposit.setPaidTaxes(BigDecimal.ZERO);
		deposit.setDiscount(BigDecimal.ZERO);
		deposit.setSurcharge(BigDecimal.ZERO);
		return deposit;
	}

	public List<FinancialInstitution> getFinantialInstitutions(PaymentType paymentType) {
		if (paymentType == PaymentType.CHECK) {
			return banks;
		} else {
			if (paymentType == PaymentType.CREDIT_CARD) {
				return creditCardEmitors;
			} else {
				if (paymentType == PaymentType.TRANSFER) {
					return stateBanks;
				}
			}
		}
		return new ArrayList<FinancialInstitution>();
	}

	private void loadLists() {
		if (banks == null)
			banks = findFinantialInstitutions(FinancialInstitutionType.BANK);
		if (creditCardEmitors == null)
			creditCardEmitors = findFinantialInstitutions(FinancialInstitutionType.CREDIT_CARD_EMISOR);
		if (stateBanks == null)
			stateBanks = findFinantialInstitutions(FinancialInstitutionType.STATE_BANK);
		creditNotes = findCreditNotes();
	}

	@SuppressWarnings("unchecked")
	private List<FinancialInstitution> findFinantialInstitutions(FinancialInstitutionType finantialInstitutionType) {
		Query query = getPersistenceContext().createNamedQuery("FinancialInstitution.findByType");
		query.setParameter("type", finantialInstitutionType);
		return query.getResultList();
	}

	private List<MunicipalBond> getSelected() {
		selectedBonds = new ArrayList<MunicipalBond>();
		for (MunicipalBondItem mbi : municipalBondItems) {
			mbi.fillSelected(selectedBonds);
		}
		return selectedBonds;
	}

	/**
	 * mac
	 * 
	 * @return
	 */
	private List<MunicipalBond> getSelected2() {
		selectedBonds = new ArrayList<MunicipalBond>();
		for (MunicipalBondItem mbi : items) {
			mbi.fillSelected(selectedBonds);
		}
		return selectedBonds;
	}

	public List<PaymentAgreement> getPaymentAgreements() {
		return paymentAgreements;
	}

	public void setPaymentAgreements(List<PaymentAgreement> paymentAgreements) {
		this.paymentAgreements = paymentAgreements;
	}

	public PaymentAgreement getPaymentAgreement() {
		return paymentAgreement;
	}

	public void setPaymentAgreement(PaymentAgreement paymentAgreement) {
		this.paymentAgreement = paymentAgreement;
	}

	public BigDecimal getDepositTotal() {
		return depositTotal;
	}

	public void setDepositTotal(BigDecimal depositTotal) {
		this.depositTotal = depositTotal;
	}

	public Boolean getHasConflict() {
		return hasConflict;
	}

	public void setHasConflict(Boolean hasConflict) {
		this.hasConflict = hasConflict;
	}

	public BigDecimal getDeltaDown() {
		return deltaDown;
	}

	public void setDeltaDown(BigDecimal deltaDown) {
		this.deltaDown = deltaDown;
	}

	public BigDecimal getDeltaUp() {
		return deltaUp;
	}

	public void setDeltaUp(BigDecimal deltaUp) {
		this.deltaUp = deltaUp;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public BigDecimal getChange() {
		return change;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public Long getCompensationStatusId() {
		return compensationStatusId;
	}

	public Boolean isInPaymentAgreement(MunicipalBond municipalBond) {
		return inPaymentAgreementBonds.containsKey(municipalBond.getEntry().getId().toString());
	}

	/**
	 * mac
	 * 
	 * @param municipalBond
	 * @return
	 */
	public Boolean isInPaymentAgreement2(MunicipalBond municipalBond) {
		// System.out.println("====>"+municipalBond.getEntry().getId());
		return false;
	}

	public Map<String, Long> getInPaymentAgreementBonds() {
		return inPaymentAgreementBonds;
	}

	public void setInPaymentAgreementBonds(Map<String, Long> inPaymentAgreementBonds) {
		this.inPaymentAgreementBonds = inPaymentAgreementBonds;
	}

	/**
	 * @return the pendingAlerts
	 */
	public List<Alert> getPendingAlerts() {
		return pendingAlerts;
	}

	/**
	 * @param pendingAlerts
	 *            the pendingAlerts to set
	 */
	public void setPendingAlerts(List<Alert> pendingAlerts) {
		this.pendingAlerts = pendingAlerts;
	}

	public void addPaymentFraction() {
		// System.out.println("SE AGREGA NUEVA FRACCION");
		this.getInstance().add(new PaymentFraction());
	}

	public void removePaymentFraction(PaymentFraction paymentFraction) {
		this.getInstance().remove(paymentFraction);
	}

	private BigDecimal getReceivedAmount() {
		BigDecimal receivedAmount = BigDecimal.ZERO;
		for (PaymentFraction fraction : getInstance().getPaymentFractions()) {
			receivedAmount = receivedAmount.add(fraction.getReceivedAmount());
		}
		return receivedAmount;
	}

	public void calculateChange() {
		BigDecimal value = this.getInstance().getValue();
		BigDecimal receivedAmount = getReceivedAmount();
		this.canRegisterPayment = true;
		if (value != null && receivedAmount != null) {
			if (value.compareTo(receivedAmount) <= 0) {
				change = receivedAmount.subtract(value);
				this.canRegisterPayment = false;
			} else {
				change = BigDecimal.ZERO;
			}
		}
	}

	public List<CreditNote> getCreditNotes() {
		return creditNotes;
	}

	@SuppressWarnings("unchecked")
	public List<CreditNote> findCreditNotes() {
		creditNotes = getCreditNotes();
		// System.out.println("FINDING ACTIVE CREDIT NOTES");
		if (resident != null) {
			Query query = getPersistenceContext().createNamedQuery("CreditNote.findActiveByResidentId");
			query.setParameter("residentId", resident.getId());
			return query.getResultList();
		}
		return new ArrayList<CreditNote>();
	}

	public Boolean getIsPaymentOk() {
		Payment payment = getInstance();
		Boolean isPaymentOk = Boolean.TRUE;

		BigDecimal value = this.getInstance().getValue().setScale(2, RoundingMode.HALF_UP);
		BigDecimal receivedAmount = getReceivedAmount().setScale(2, RoundingMode.HALF_UP);
		// System.out.println("VALUE = " + value);
		// System.out.println("RECEIVED AMOUNT = " + receivedAmount);

		if (paymentBlocked) {
			addFacesMessageFromResourceBundle("payment.paymentBlockedAlertPriority");
			return Boolean.FALSE;

		}
		if (receivedAmount == null || value == null || value.compareTo(receivedAmount) > 0) {

			// pago por abonos ...
			// el monto recibido es menor
			if (this.isPaymentSubscription) {
				for (PaymentFraction fraction : payment.getPaymentFractions()) {
					fraction.setPaidAmount(fraction.getReceivedAmount());
					if (fraction.getPaymentType() == PaymentType.CASH) {
						if (fraction.getReceivedAmount() == BigDecimal.ZERO) {
							addFacesMessageFromResourceBundle("payment.cashDetailInvalid");
							isPaymentOk = Boolean.FALSE;
							break;
						}
						fraction.setPaidAmount(fraction.getReceivedAmount().subtract(change));
					}
				}
			} else {
				addFacesMessageFromResourceBundle("payment.receivedAmountNotEnough");
				return Boolean.FALSE;
			}
			// fin pago por abonos
		} else {
			for (PaymentFraction fraction : payment.getPaymentFractions()) {
				// System.out.println("PAYMENT TYPE = " + fraction.getPaymentType());
				fraction.setPaidAmount(fraction.getReceivedAmount());
				if (fraction.getPaymentType() == PaymentType.CASH) {
					if (fraction.getReceivedAmount() == BigDecimal.ZERO) {
						addFacesMessageFromResourceBundle("payment.cashDetailInvalid");
						isPaymentOk = Boolean.FALSE;
						break;
					}
					fraction.setPaidAmount(fraction.getReceivedAmount().subtract(change));
				}

				if (fraction.getPaymentType() == PaymentType.CHECK) {
					if (fraction.getAccountNumber() == null || fraction.getDocumentNumber() == null
							|| fraction.getFinantialInstitution() == null || fraction.getAccountNumber().isEmpty()
							|| fraction.getDocumentNumber().isEmpty()
							|| fraction.getReceivedAmount() == BigDecimal.ZERO) {
						// System.out.println("CHECK FRACTION IS NOT VALID");
						addFacesMessageFromResourceBundle("payment.checkDetailInvalid");
						isPaymentOk = Boolean.FALSE;
						break;
					}
					// paidAmountIsAdjusted = adjustPaidAmount(fraction,
					// change);
				}

				if (fraction.getPaymentType() == PaymentType.CREDIT_CARD
						|| fraction.getPaymentType() == PaymentType.TRANSFER) {
					if (fraction.getDocumentNumber() == null || fraction.getFinantialInstitution() == null
							|| fraction.getDocumentNumber().isEmpty()
							|| fraction.getReceivedAmount() == BigDecimal.ZERO) {
						// System.out.println("CREDIT_CARD FRACTION IS NOT VALID");
						addFacesMessageFromResourceBundle("payment.creditCardDetailInvalid");
						isPaymentOk = Boolean.FALSE;
						break;
					}
				}

				if (fraction.getPaymentType() == PaymentType.CREDIT_NOTE) {
					BigDecimal received = fraction.getReceivedAmount();
					if (fraction.getCreditNote() == null) {
						isPaymentOk = Boolean.FALSE;
						addFacesMessageFromResourceBundle("payment.creditNoteNotSelected");
						break;
					} else {
						if (received != null) {
							BigDecimal availableBalance = fraction.getCreditNote().getAvailableAmount();
							if (received.compareTo(availableBalance) > 0) {
								// System.out.println("CREDIT NOTE FRACTION IS NOT VALID");
								addFacesMessageFromResourceBundle("creditNote.availableAmountIsNotEnough");
								isPaymentOk = Boolean.FALSE;
								break;
							}
						}
					}
				}
			}

		}

		Boolean paidAmountIsAdjusted = adjustPaidAmountForNoCash(payment, value);

		if (!paidAmountIsAdjusted) {
			addFacesMessageFromResourceBundle("payment.changeIsOnlyAvailableForCashFractions");
		}

		return isPaymentOk && paidAmountIsAdjusted;
	}

	private Boolean adjustPaidAmountForNoCash(Payment payment, BigDecimal value) {
		BigDecimal totalNoCash = BigDecimal.ZERO;
		for (PaymentFraction fraction : payment.getPaymentFractions()) {
			if (fraction.getPaymentType() == PaymentType.CHECK
					|| fraction.getPaymentType() == PaymentType.CREDIT_CARD) {
				totalNoCash = totalNoCash.add(fraction.getReceivedAmount());
			}
		}

		if (totalNoCash.compareTo(value) > 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public List<MunicipalBond> getSelectedBonds() {
		return selectedBonds;
	}

	public ReceiptPrintingManager getReceiptPrintingManager() {
		return receiptPrintingManager;
	}

	public void updateHasCompensationBonds() {
		// @author macartuche
		// deshabilitar boton de registro de pago
		this.canRegisterPayment = true;
		// fin
		clearFractions();
		hasCompensationBonds = Boolean.FALSE;
		selectedBonds = getSelected();
		for (MunicipalBond bond : selectedBonds) {
			if (bond.getMunicipalBondStatus().getId().longValue() == compensationStatusId.longValue()) {
				hasCompensationBonds = Boolean.TRUE;
				break;
			}
		}
		// System.out.println("HAS COMPENSATION BONDS ----> "
		// + hasCompensationBonds);
	}

	private boolean isPaymentSubscription = false;

	public void updateHasCompensationBonds(String paymentType) {
		this.isPaymentSubscription = true;
		// @author macartuche
		// deshabilitar boton de registro de pago
		this.canRegisterPayment = true;
		// fin
		clearFractions();
		hasCompensationBonds = Boolean.FALSE;
		selectedBonds = getSelected();
		for (MunicipalBond bond : selectedBonds) {
			if (bond.getMunicipalBondStatus().getId().longValue() == compensationStatusId.longValue()) {
				hasCompensationBonds = Boolean.TRUE;
				break;
			}
		}
		// System.out.println("HAS COMPENSATION BONDS ----> "
		// + hasCompensationBonds);
	}

	public Boolean getHasCompensationBonds() {
		return hasCompensationBonds;
	}

	public void setHasCompensationBonds(Boolean hasCompensationBonds) {
		this.hasCompensationBonds = hasCompensationBonds;
	}

	public Boolean getPaymentBlocked() {
		return paymentBlocked;
	}

	public void setPaymentBlocked(Boolean paymentBlocked) {
		this.paymentBlocked = paymentBlocked;
	}

	public String getPaymentFileName() {
		return paymentFileName;
	}

	public void setPaymentFileName(String paymentFileName) {
		this.paymentFileName = paymentFileName;
	}

	public String getPaymentFilePath() {
		return paymentFilePath;
	}

	public void setPaymentFilePath(String paymentFilePath) {
		this.paymentFilePath = paymentFilePath;
	}

	public String getPaymentInstanceName() {
		return paymentInstanceName;
	}

	public void setPaymentInstanceName(String paymentInstanceName) {
		this.paymentInstanceName = paymentInstanceName;
	}

	public List<FutureBond> getFutureBonds() {
		return futureBonds;
	}

	public void setFutureBonds(List<FutureBond> futureBonds) {
		this.futureBonds = futureBonds;
	}

	public BigDecimal getTotalFutereBond() {
		return totalFutereBond;
	}

	public void setTotalFutereBond(BigDecimal totalFutereBond) {
		this.totalFutereBond = totalFutereBond;
	}

	public class PdfExporter {
		/**
		 * returns the byte array of the PDF file generated using the page xhtml
		 * 
		 * @param page
		 *            the page name of the xhtml for the pdf
		 * @return the byte array containing the PDF file data
		 */
		public byte[] pdfExport(String page) {

			byte[] pdfBytes = null;
			try {
				Renderer renderer = Renderer.instance();
				renderer.render(page);
				DocumentStore store = DocumentStore.instance();
				String nextId = store.newId();
				long docId = Long.parseLong(nextId) - 1;
				DocumentData data = store.getDocumentData("" + docId);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				data.writeDataToStream(baos);
				pdfBytes = baos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return pdfBytes;
		}
	}

	private Boolean agreementsBtn = Boolean.FALSE;

	public Boolean getAgreementsBtn() {
		return agreementsBtn;
	}

	public void setAgreementsBtn(Boolean agreementsBtn) {
		this.agreementsBtn = agreementsBtn;
	}

	public void agreementsMethod() {
		isFullPayment = !isFullPayment;
		if (!isFullPayment) {
			this.paymentAgreements = findPaymentAgreements(resident.getId());
		}
		resetPaymentTotals();
		conflictingBond = null;
		hasConflict = false;
		agreementsBtn = Boolean.TRUE;
		if (resident.getClass().getSimpleName().equalsIgnoreCase("Person")) {
			Person person = (Person) resident;
			personIsDead = person.getIsDead();
		}
	}

	private boolean personIsDead = Boolean.FALSE;

	public boolean isPersonIsDead() {
		return personIsDead;
	}

	public void setPersonIsDead(boolean personIsDead) {
		this.personIsDead = personIsDead;
	}

	/**
	 * @author macartuche
	 * @date 2015-10-23
	 * @return
	 */
	public boolean enableButtonPrint() {
		// System.out.println("AQUI");
		List<MunicipalBond> selected = selectedBonds;
		boolean disabled = false;
		if (!selected.isEmpty()) {
			for (MunicipalBond municipalBond : selected) {
				Receipt receipt = municipalBond.getReceipt();
				if (receipt != null) {
					if (receipt.getAuthorizationNumber().isEmpty()) {
						disabled = true;
						break;
					}
				}
			}
		}
		// System.out.println("====>" + disabled);
		return disabled;
	}

	// ----------------IA---------------------------------------------

	private Date date;
	private Date hourReport;
	private List<Data> lsr = new LinkedList<Data>();

	public Date getHourReport() {
		if (this.hourReport == null)
			this.hourReport = new Date();
		return hourReport;
	}

	public String printReport() {
		return "print";
	}

	public List<Data> getLsr() {
		return lsr;
	}

	public void setLsr(List<Data> lsr) {
		this.lsr = lsr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @author ISMAEL
	 * @return
	 */
	public String listAgreed() {
		// System.out.println("INICIO");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		String sql = "select pag.id as Agreement,\n" + " firstpaymentdate,\n" + "	resident.id as resident,\n"
				+ "	resident.identificationnumber as ci, \n" + "	resident.name, \n" + "	pag.description, \n"
				+ "	dividendsnumber, aux.count, aux.suma,\n" + "	max(divi.date)\n"
				+ "from gimprod.PaymentAgreement pag\n" + "inner join gimprod.resident on pag.resident_id=resident.id\n"
				+ "inner join dividend divi on divi.PaymentAgreement_id = pag.id\n"
				+ "inner join (select resident_id, \n" + "			count(*) as count, \n"
				+ "			sum(mb.value) as suma, PaymentAgreement_id as pay_id\n"
				+ "		from gimprod.municipalbond mb \n" + "		where mb.PaymentAgreement_id in (\n"
				+ "						select div2.PaymentAgreement_id \n"
				+ "						from gimprod.dividend div2\n"
				+ "						group by div2.PaymentAgreement_id\n"
				+ "						having max(div2.date) <= '" + df.format(date) + "'\n"
				+ "						order by div2.PaymentAgreement_id)\n" + "		and municipalbondstatus_id=4\n"
				+ "		group by mb.PaymentAgreement_id,resident_id)aux on aux.pay_id  = pag.id\n"
				+ "where  pag.isactive=true and pag.id in (select pag1.id \n" + "			from gimprod.dividend div\n"
				+ "			inner join gimprod.PaymentAgreement pag1 on div.PaymentAgreement_id=pag1.id\n"
				+ "			group by pag1.id\n" + "			having max(div.date) <= '" + df.format(date) + "'\n"
				+ "			order by max(div.date))\n" + "	--and pag.id  =1478 \n" + "\n"
				+ "group by pag.id, resident.id,resident.identificationnumber, \n" + "	resident.name, \n"
				+ "	description, \n" + "	dividendsnumber, aux.count, aux.suma\n" + "order by pag.id";

		EntityManager em = getEntityManager();
		Query q = em.createNativeQuery(sql);
		// q.setParameter("datev", df.format(date));
		aginarValores(q.getResultList());
		return "ok";
	}

	private void aginarValores(List<Object> lsr) {
		this.lsr = new LinkedList<Data>();
		for (Object v : lsr) {
			Object[] va = (Object[]) v;
			Data vd = new Data((BigInteger) va[0], (Timestamp) va[1], (BigInteger) va[2], (String) va[3],
					(String) va[4], (String) va[5], (Integer) va[6], (BigInteger) va[7], (BigDecimal) va[8],
					(Date) va[9]);
			this.lsr.add(vd);
		}
	}

	public void cleanSearch() {
		this.lsr = new LinkedList<Data>();
		// System.out.println("===))(&%%");
	}

	public class Data {
		BigInteger agreement_id;
		Timestamp firstpaymentdate;
		BigInteger resident_id;
		String identificationnumber_ci;
		String name;
		String description;
		Integer dividendsnumber;
		BigInteger count;
		BigDecimal suma;
		Date date;

		public Data(BigInteger agreement_id, Timestamp firstpaymentdate, BigInteger resident_id,
				String identificationnumber_ci, String name, String description, Integer dividendsnumber,
				BigInteger count, BigDecimal suma, Date date) {
			this.agreement_id = agreement_id;
			this.firstpaymentdate = firstpaymentdate;
			this.resident_id = resident_id;
			this.identificationnumber_ci = identificationnumber_ci;
			this.name = name;
			this.description = description;
			this.dividendsnumber = dividendsnumber;
			this.count = count;
			this.suma = suma;
			this.date = date;
		}

		public BigInteger getAgreement_id() {
			return agreement_id;
		}

		public void setAgreement_id(BigInteger agreement_id) {
			this.agreement_id = agreement_id;
		}

		public Timestamp getFirstpaymentdate() {
			return firstpaymentdate;
		}

		public void setFirstpaymentdate(Timestamp firstpaymentdate) {
			this.firstpaymentdate = firstpaymentdate;
		}

		public BigInteger getResident_id() {
			return resident_id;
		}

		public void setResident_id(BigInteger resident_id) {
			this.resident_id = resident_id;
		}

		public String getIdentificationnumber_ci() {
			return identificationnumber_ci;
		}

		public void setIdentificationnumber_ci(String identificationnumber_ci) {
			this.identificationnumber_ci = identificationnumber_ci;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public Integer getDividendsnumber() {
			return dividendsnumber;
		}

		public void setDividendsnumber(Integer dividendsnumber) {
			this.dividendsnumber = dividendsnumber;
		}

		public BigInteger getCount() {
			return count;
		}

		public void setCount(BigInteger count) {
			this.count = count;
		}

		public BigDecimal getSuma() {
			return suma;
		}

		public void setSuma(BigDecimal suma) {
			this.suma = suma;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

	}

	// Para controlar las impugnaciones.............
	// Jock Samaniego............. 20-07-2016........

	private List<Impugnment> impugnmentsTotal = new ArrayList<Impugnment>();
	private String[] states;

	@SuppressWarnings("unchecked")
	public void findPendingsImpugnments(Long id) {
		List<Impugnment> impugnments = new ArrayList<Impugnment>();
		for (String st : states) {
			Query query = getEntityManager().createNamedQuery("Impugnment.findByMunicipalBond");
			query.setParameter("municipalBond_id", id);
			query.setParameter("code", st);
			impugnments = query.getResultList();
			if (impugnments.size() > 0) {
				impugnmentsTotal.addAll(impugnments);
			}
		}
	}

	public void chargeControlImpugnmentStates() {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		String controlStates = systemParameterService.findParameter("STATES_IMPUGNMENT_CONTROL_REGISTER_PAID");

		/*
		 * Query query =
		 * getEntityManager().createNamedQuery("SystemParameter.findByName");
		 * query.setParameter("name", ""); SystemParameter controlStates =
		 * (SystemParameter) query.getSingleResult();
		 */
		states = controlStates.trim().split(",");
	}
	
	//Jock samaniego... buscar abonos por obligacion
	private String groupBy;
	public List<EntryTotalCollected> getTotalDepositsInSubscriptionByMB(Long mb_id) {

		if (groupBy == null)
			groupBy = "ac.accountCode";

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,count(d.id), e.name,"
				+ groupBy
				+ ", SUM(d.value), "
				+ " SUM(d.interest), SUM(d.paidTaxes)) from Deposit d join d.municipalBond m "
				+ "join m.entry e left join e.account ac "
				+ "where m.id =:mb_id "
				+ " GROUP BY e.id, e.name," + groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("mb_id", mb_id);
		return query.getResultList();
	}

	public List<Impugnment> getImpugnmentsTotal() {
		return impugnmentsTotal;
	}

	public void setImpugnmentsTotal(List<Impugnment> impugnmentsTotal) {
		this.impugnmentsTotal = impugnmentsTotal;
	}

	// =============================

	private Boolean bondIsWire = Boolean.FALSE;

	public Boolean getBondIsWire() {
		return bondIsWire;
	}

	public void setBondIsWire(Boolean bondIsWire) {
		this.bondIsWire = bondIsWire;
	}

	public Boolean getCanRegisterPayment() {
		return canRegisterPayment;
	}

	public void setCanRegisterPayment(Boolean canRegisterPayment) {
		this.canRegisterPayment = canRegisterPayment;
	}
}