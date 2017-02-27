package org.gob.gim.income.action;

/*
 * 2013-07-16
 * Actualización Ronald Paladines Celi GAD Municipal de Loja
 * 
 * se actualiza el cálculo de intereses para diferenciar por institución: del GAD Municipal y la EMAALEP
 * 
 * 
 */
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.naming.NamingException;
import javax.persistence.Query;

import org.codehaus.jackson.map.ObjectMapper;
import org.gob.gim.common.DateUtils;
import org.gob.gim.common.GimUtils;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.MunicipalBondUtil;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.view.MunicipalBondItem;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.view.ReportView;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.Account;
import ec.gob.gim.income.model.AccountBalance;
import ec.gob.gim.income.model.Branch;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.EntryTotalCollected;
import ec.gob.gim.income.model.InterestRate;
import ec.gob.gim.income.model.Payment;
import ec.gob.gim.income.model.PaymentFraction;
import ec.gob.gim.income.model.PaymentType;
import ec.gob.gim.income.model.Tax;
import ec.gob.gim.income.model.TaxItem;
import ec.gob.gim.income.model.TaxRate;
import ec.gob.gim.income.model.Till;
import ec.gob.gim.income.model.TillPermission;
import ec.gob.gim.income.model.TillPermissionDetail;
import ec.gob.gim.income.model.Workday;
import ec.gob.gim.income.model.dto.ParameterFutureEmissionDTO;
import ec.gob.gim.income.model.dto.ReplacementAccountDTO;
import ec.gob.gim.income.model.dto.ReplacementAgreementDTO;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.Item;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondView;

@Name("workdayHome")
public class WorkdayHome extends EntityHome<Workday> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private SystemParameterService systemParameterService;
	public static String REVENUE_SERVICE_NAME = "/gim/RevenueService/local";
	private final static String ENTRIES_EMAALEP_LIST = "ENTRIES_EMAALEP_LIST";

	// private RevenueService revenueService;

	private boolean isFirstTime = true;
	private boolean renderPanel = true;
	private boolean isFromIncome = true;
	private boolean existOpenedTills = false;
	private boolean showDetail = false;
	private Boolean isCurrentBalanceReport = Boolean.FALSE;
	private boolean isClosingWorkday = false;
	private Boolean existsCurrentWorkday;
	private boolean isReadyForPrint;
	private boolean needsTaxRateDefined = false;
	private boolean needsInterestRateDefined = false;
	private Boolean existOpenWorkdays;
	private boolean globalReport = false;
	private boolean detailDifferentYears = true;
	private Long idForTree = 1L;

	private Entry entry;
	private Person person;
	private MunicipalBondStatus municipalBondStatus;
	private String entryCode;
	private Date closingWorkdayDate;
	private String criteriaEntry;
	private Date startDate;
	private Date endDate;
	private BigDecimal generalTotal;
	private BigDecimal totalCompensationCollected;
	private BigDecimal totalEmitted;
	private BigDecimal totalSummaryCollected;
	private BigDecimal discountsValueEffective;
	private BigDecimal discountsValueByCompensation;
	private BigDecimal totalCashCollected;
	private BigDecimal totalCashCollectedPreviousYears;
	private BigDecimal totalCashCollectedCurrentYear;
	private BigDecimal currentDiscountsValueCompensation;
	private BigDecimal previousDiscountsValueCompensation;
	private BigDecimal currentDiscountsValueEffective;
	private BigDecimal previousDiscountsValueEffective;
	private BigDecimal totalReversed;
	private BigDecimal totalValueByCompensation;
	private BigDecimal previousValueAgreement;
	private BigDecimal totalValueAgreement;
	private BigDecimal currentValueAgreement;
	private BigDecimal taxesValueAgreement;
	private BigDecimal interestValueAgreement;
	private BigDecimal realTotalEmitted;
	private BigDecimal totalNullifieds;
	private BigDecimal surchargesValueEffective;
	private BigDecimal interestValueEffective;
	private BigDecimal taxesValueEffective;
	private BigDecimal surchargesValueCompensation;
	private BigDecimal interestValueCompensation;
	private BigDecimal taxesValueCompensation;
	private BigDecimal currentValueCompensation;
	private BigDecimal previousValueCompensation;
	private BigDecimal totalCreditCardCollected;
	private BigDecimal totalCheckCollected;
	private BigDecimal totalCreditNoteCollected;
	private Long totalBondsCollected;
	private AccountBalance mainAccountBalance = new AccountBalance();
	private Account interestAccount;
	private Account interestAccountEmaalep;
	private boolean closingCurrentWorkday = true;
	private Long totalBondsCollectedByCompensation;

	private MunicipalBondStatus inPaymentAgreementStatus;
	private MunicipalBondStatus externalChannelStatus;
	private MunicipalBondStatus paidMunicipalBondStatus;
	private MunicipalBondStatus compensationMunicipalBondStatus;
	private MunicipalBondStatus blockedMunicipalBondStatus;
	private MunicipalBondStatus reversedMunicipalBondStatus;
	private MunicipalBondStatus pendingStatus;
	private MunicipalBondStatus cancelledBondStatus;
	private MunicipalBondStatus futureBondStatus;

	private String groupBy;
	private String keygroupBy;
	private String columnsNumber;
	private int numberOfServedPeople;
	private Long numberOfTransactions;
	private Long paidStatus;
	private Long externalPaidStatus;
	private Long compensationStatus;
	private Long agreementStatus;
	private Long totalMunicipalBondEmitted;
	private Long totalMunicipalBondNullified;
	private Branch branch;

	private List<Branch> branches;
	private List<PaymentFraction> creditNotePayments;
	private List<Account> parentAccounts;
	private List<Entry> entriesSearch;
	private Map<String, String> valuesGroupBy = new HashMap<String, String>();
	private List<AccountBalance> mainAccounts;
	private List<Entry> entries;
	private List<AccountBalance> accountsForSummary;
	private List<Deposit> deposits;
	private List<Payment> payments;
	private List<MunicipalBond> municipalBonds;
	private List<AccountBalance> finallyAccountsForSummary;

	private Charge incomeCharge;
	private Charge revenueCharge;
	private Delegate incomeDelegate;
	private Delegate revenueDelegate;

	private TreeNode<AccountBalance> rootNode = null;

	private List<PaymentFraction> paymentsFractionForCheckDetail;
	private List<PaymentFraction> paymentsFractionForCashDetail;
	private List<PaymentFraction> paymentsFractionForCardDetail;
	private List<PaymentFraction> paymentsFractionForCreditNoteDetail;
	private List<EntryTotalCollected> entryTotalCollecteds;
	private List<EntryTotalCollected> entryTotalCancelled;
	// emisiones futuras
	private List<EntryTotalCollected> entryTotalFuture;
	private List<EntryTotalCollected> entryTotalPrepaid;
	private List<EntryTotalCollected> entryTotalFormalize;
	private String explanation;
	private String explanationFormalize;

	private List<EntryTotalCollected> entryTotalReversed;
	private List<EntryTotalCollected> entryTotalDiscount;
	private List<EntryTotalCollected> entryTotalDiscountByCompensation;
	private List<EntryTotalCollected> summaryTotalCollecteds;
	private List<EntryTotalCollected> entryTotalCollectedsByCompensation;
	private List<EntryTotalCollected> entryTotalCollectedsInAgreement;
	private List<EntryTotalCollected> taxesEmitted;
	private List<EntryTotalCollected> taxesCancelled;
	private List<EntryTotalCollected> taxesReversed;
	private List<EntryTotalCollected> taxesFuture;
	private List<EntryTotalCollected> taxesPrepaid;
	private List<EntryTotalCollected> taxesFormalize;
	private List<EntryTotalCollected> entryTotalCollectedsForTree;
	private List<EntryTotalCollected> entryTotalCollectedsByCompensationForTree;
	private List<EntryTotalCollected> entryTotalCollectedsInAgreementForTree;
	private List<MunicipalBondView> municipalBondViews;
	private List<Deposit> completeDeposits = new ArrayList<Deposit>();
	private List<Deposit> partialDeposits = new ArrayList<Deposit>();
	private List<Deposit> readyDeposits = new ArrayList<Deposit>();
	private List<Long> statusIds;
	private List<ReportView> totalEmitteds;
	private List<ReportView> totalNullified;
	private List<ReportView> totalByCashier;
	private List<MunicipalBondItem> municipalBondItems;
	
	private List<ReplacementAgreementDTO> replacementAgreements;
	private List<ReplacementAccountDTO> replacementAccountDTOs;

	@In
	UserSession userSession;

	@In(create = true)
	TillPermissionHome tillPermissionHome;

	@In
	FacesMessages facesMessages;

	public Person getPerson() {
		return person;
	}

	@SuppressWarnings("unchecked")
	@Observer("org.jboss.seam.postCreate.workdayHome")
	public void checkSettings() {
		System.out.println("CHECKING SETTINGS FOR SCREEN ...");
		Date date = DateUtils.truncate(new Date());
		Query query = getPersistenceContext().createNamedQuery(
				"InterestRate.findActive");
		query.setParameter("date", date);
		List<InterestRate> interestRates = query.getResultList();
		if (interestRates.size() == 0) {
			System.out.println("INTERES RATE NOT DEFINED");
			needsInterestRateDefined = true;
		} else {
			System.out.println("INTERES RATE ARE OK");
			needsInterestRateDefined = false;
		}
	}

	private void initializeSystemParameterService() {
		systemParameterService = ServiceLocator.getInstance().findResource(
				SystemParameterService.LOCAL_NAME);
	}

	public void setWorkdayId(Long id) {
		setId(id);
	}

	public Long getWorkdayId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	/**
	 * Verifica si existe Taxrate activas
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean existsTaxRates() {
		Query query = getPersistenceContext().createNamedQuery(
				"Tax.findByEntryNotNull");
		Calendar now = Calendar.getInstance();
		List<Tax> taxes = query.getResultList();
		for (Tax t : taxes) {
			TaxRate tr = findActiveTaxRate(t);
			if (tr == null)
				return false;
			if (now.getTime().before(tr.getStartDate())
					|| now.getTime().after(tr.getEndDate())) {
				return false;
			}
		}
		return true;
	}

	private void loadCharge() {
		incomeCharge = getCharge("DELEGATE_ID_INCOME");
		if (incomeCharge != null) {
			for (Delegate d : incomeCharge.getDelegates()) {
				if (d.getIsActive())
					incomeDelegate = d;
			}
		}
		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive())
					revenueDelegate = d;
			}
		}
	}

	private Charge getCharge(String systemParameter) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,
				systemParameter);
		return charge;
	}

	/**
	 * Total recaudado, Total recaudado años anteriores y Total recaudado año
	 * actual
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal sumTotalCashCollected() {
		BigDecimal total = BigDecimal.ZERO;
		totalCashCollectedPreviousYears = BigDecimal.ZERO;
		totalCashCollectedCurrentYear = BigDecimal.ZERO;
		totalBondsCollected = 0L;
		if (entryTotalCollecteds == null) {
			totalCashCollected = total;
			return total;
		}

		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getMunicipalBondsNumber() != null)
				totalBondsCollected = totalBondsCollected
						+ e.getMunicipalBondsNumber();
			if (e.getIsDiscount() && e.getTotal() != null) {
				totalCashCollectedPreviousYears = totalCashCollectedPreviousYears
						.subtract(e.getPreviousYears() == null ? BigDecimal.ZERO
								: e.getPreviousYears());
				totalCashCollectedCurrentYear = totalCashCollectedCurrentYear
						.subtract(e.getValue() == null ? BigDecimal.ZERO : e
								.getValue());
				total = total.subtract(e.getTotal());
			}
			if (!e.getIsDiscount() && e.getTotal() != null) {
				totalCashCollectedPreviousYears = totalCashCollectedPreviousYears
						.add(e.getPreviousYears() == null ? BigDecimal.ZERO : e
								.getPreviousYears());
				totalCashCollectedCurrentYear = totalCashCollectedCurrentYear
						.add(e.getValue() == null ? BigDecimal.ZERO : e
								.getValue());
				total = total.add(e.getTotal());
			}
		}

		totalCashCollected = total;

		return total;
	}

	public BigDecimal sumTotalEmitted() {
		BigDecimal total = BigDecimal.ZERO;
		if (entryTotalCollecteds == null)
			return total;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			total = total.add(e.getTotal());
		}
		totalEmitted = total;
		return total;
	}

	/**
	 * Total de descuentos en compensación : total, total año actual y total
	 * años previos
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal totalDiscountCompensationCollected() {
		discountsValueByCompensation = BigDecimal.ZERO;
		currentDiscountsValueCompensation = BigDecimal.ZERO;
		previousDiscountsValueCompensation = BigDecimal.ZERO;
		if (entryTotalDiscountByCompensation == null) {
			return discountsValueByCompensation;
		}
		for (EntryTotalCollected e : entryTotalDiscountByCompensation) {
			if (e.getTotal() != null)
				discountsValueByCompensation = discountsValueByCompensation
						.add(e.getTotal());
			if (e.getValue() != null)
				currentDiscountsValueCompensation = currentDiscountsValueCompensation
						.add(e.getValue());
			if (e.getPreviousYears() != null)
				previousDiscountsValueCompensation = previousDiscountsValueCompensation
						.add(e.getPreviousYears());
		}

		return discountsValueByCompensation;
	}

	/**
	 * Total de descuentos en efectivo : total, total año actual y total años
	 * previos
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal totalDiscountCashCollected() {
		discountsValueEffective = BigDecimal.ZERO;
		currentDiscountsValueEffective = BigDecimal.ZERO;
		previousDiscountsValueEffective = BigDecimal.ZERO;
		if (entryTotalDiscount == null) {
			return discountsValueEffective;
		}

		for (EntryTotalCollected e : entryTotalDiscount) {
			if (e.getTotal() != null) {
				discountsValueEffective = discountsValueEffective.add(e
						.getTotal());
			}
			if (e.getValue() != null) {
				currentDiscountsValueEffective = currentDiscountsValueEffective
						.add(e.getValue());
			}
			if (e.getPreviousYears() != null) {
				previousDiscountsValueEffective = previousDiscountsValueEffective
						.add(e.getPreviousYears());
			}
		}

		return discountsValueEffective;
	}

	/**
	 * Total en municipalBonds anulados
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal totalCashCancelled() {
		BigDecimal total = BigDecimal.ZERO;
		if (entryTotalCancelled == null)
			return total;
		for (EntryTotalCollected e : entryTotalCancelled) {
			if (e.getTotal() != null)
				total = total.add(e.getTotal());
		}
		return total;
	}

	public BigDecimal totalCashFuture() {
		BigDecimal total = BigDecimal.ZERO;
		if (entryTotalFuture == null)
			return total;
		for (EntryTotalCollected e : entryTotalFuture) {
			if (e.getTotal() != null)
				total = total.add(e.getTotal());
		}
		return total;
	}

	public BigDecimal totalCashPrepaid() {
		BigDecimal total = BigDecimal.ZERO;
		if (entryTotalPrepaid == null)
			return total;
		for (EntryTotalCollected e : entryTotalPrepaid) {
			if (e.getTotal() != null)
				total = total.add(e.getTotal());
		}
		return total;
	}

	public BigDecimal totalCashFormalize() {
		BigDecimal total = BigDecimal.ZERO;
		if (entryTotalFormalize == null)
			return total;
		for (EntryTotalCollected e : entryTotalFormalize) {
			if (e.getTotal() != null)
				total = total.add(e.getTotal());
		}
		return total;
	}

	/**
	 * Total en municipalBonds dados de baja
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal totalCashReversed() {
		totalReversed = BigDecimal.ZERO;
		if (entryTotalReversed == null)
			return totalReversed;
		for (EntryTotalCollected e : entryTotalReversed) {
			if (e.getTotal() != null)
				totalReversed = totalReversed.add(e.getTotal());
		}
		return totalReversed;
	}

	/**
	 * Total valores en comṕensación
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal totalCompensationGenerate() {
		totalValueByCompensation = BigDecimal.ZERO;

		if (entryTotalCollectedsByCompensation == null)
			return totalValueByCompensation;
		for (EntryTotalCollected e : entryTotalCollectedsByCompensation) {
			if (e.getIsDiscount() && e.getTotal() != null) {
				totalValueByCompensation = totalValueByCompensation.subtract(e
						.getTotal());
			}

			if (!e.getIsDiscount() && e.getTotal() != null) {
				totalValueByCompensation = totalValueByCompensation.add(e
						.getTotal());
			}

		}
		return totalValueByCompensation;
	}

	/**
	 * Total de acuerdos de pago detallado
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal totalInAgreement() {
		totalValueAgreement = BigDecimal.ZERO;
		previousValueAgreement = BigDecimal.ZERO;
		currentValueAgreement = BigDecimal.ZERO;
		taxesValueAgreement = BigDecimal.ZERO;
		interestValueAgreement = BigDecimal.ZERO;

		if (entryTotalCollectedsInAgreement == null)
			return totalValueAgreement;
		for (EntryTotalCollected e : entryTotalCollectedsInAgreement) {
			if (e.getTotal() != null)
				totalValueAgreement = totalValueAgreement.add(e.getTotal());
			if (e.getPreviousYears() != null)
				previousValueAgreement = previousValueAgreement.add(e
						.getPreviousYears());
			if (e.getValue() != null)
				currentValueAgreement = currentValueAgreement.add(e.getValue());
			if (e.getInterest() != null)
				interestValueAgreement = interestValueAgreement.add(e
						.getInterest());
			if (e.getTaxes() != null)
				taxesValueAgreement = taxesValueAgreement.add(e.getTaxes());
		}
		return totalValueAgreement;
	}

	public BigDecimal totalCollectedByBranch() {
		BigDecimal aux = BigDecimal.ZERO;
		if (payments == null)
			return aux;
		for (Payment p : payments) {
			if (p.getStatus().equals(FinancialStatus.VALID))
				aux = aux.add(p.getValue());
		}
		return aux;
	}

	public BigDecimal realTotalCollectedByBranch() {
		if (payments == null)
			return BigDecimal.ZERO;
		if (totalCreditNoteCollected == null)
			totalCreditNoteCollected = BigDecimal.ZERO;
		return totalCollectedByBranch().subtract(totalCreditNoteCollected);
	}

	private BigDecimal sumTotalSummaryCollected() {
		BigDecimal total = BigDecimal.ZERO;
		for (EntryTotalCollected e : summaryTotalCollecteds) {
			if (e.getTotal() != null && e.getIsDiscount()) {
				total = total.subtract(e.getTotal());
			}
			if (e.getTotal() != null && !e.getIsDiscount()) {
				total = total.add(e.getTotal());
			}
		}
		totalSummaryCollected = total;
		return total;
	}

	private void reportTotalEmitted() {
		totalEmitted = BigDecimal.ZERO;
		if (totalEmitteds == null)
			return;
		for (ReportView t : totalEmitteds) {
			if (t.getTotalEmitted() != null)
				totalEmitted = totalEmitted.add(t.getTotalEmitted());
		}
	}

	private void sumRealTotalEmitted() {
		realTotalEmitted = BigDecimal.ZERO;
		if (totalEmitteds == null)
			return;
		for (ReportView t : totalEmitteds) {
			if (t.getTotal() != null)
				realTotalEmitted = realTotalEmitted.add(t.getTotal());
		}
	}

	private void sumTotalNullified() {
		totalNullifieds = BigDecimal.ZERO;
		if (totalEmitteds == null)
			return;
		for (ReportView t : totalEmitteds) {
			if (t.getTotalValueReversed() != null)
				totalNullifieds = totalNullifieds
						.add(t.getTotalValueReversed());
		}
	}

	/**
	 * Total de transacciones completas
	 * 
	 * @return Long
	 */
	public Long totalCompleteTransactions() {
		Long total = 0L;
		if (totalByCashier == null)
			return total;
		for (ReportView t : totalByCashier) {
			if (t.getTotalCorrect() == null)
				t.setTotalCorrect(0L);
			total = total + t.getTotalCorrect();
		}
		return total;
	}

	/**
	 * Total de transacciones reversadas
	 * 
	 * @return Long
	 */
	public Long totalReversedTransactions() {
		Long total = 0L;
		if (totalByCashier == null)
			return total;
		for (ReportView t : totalByCashier) {
			if (t.getTotalReversed() == null)
				t.setTotalReversed(0L);
			total = total + t.getTotalReversed();
		}
		return total;
	}

	private void sumTotalMunicipalBondEmitted() {
		totalMunicipalBondEmitted = 0L;
		if (totalEmitteds == null)
			return;
		for (ReportView t : totalEmitteds) {
			totalMunicipalBondEmitted = totalMunicipalBondEmitted
					+ t.getTotalCorrect();
		}
	}

	private void sumTotalMunicipalBondNullified() {
		totalMunicipalBondNullified = 0L;
		if (totalNullified == null)
			return;
		for (ReportView t : totalNullified) {
			totalMunicipalBondNullified = totalMunicipalBondNullified
					+ t.getTotalCorrect();
		}
	}

	@SuppressWarnings("unchecked")
	private TaxRate findActiveTaxRate(Tax t) {
		Query query = getEntityManager().createNamedQuery(
				"TaxRate.findActiveByTaxIdAndPaymentDate");
		query.setParameter("taxId", t.getId());
		query.setParameter("paymentDate", new Date());
		List<TaxRate> list = query.getResultList();
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	private BigDecimal totalCollected;

	public void findDetailedDepositsByCashier() {
		municipalBondViews = getMunicipalBondsViewBetweenDatesByCashier(person
				.getId());
		totalCollected = sumTotal(municipalBondViews);
	}

	public BigDecimal sumTotal(List<MunicipalBondView> list) {
		BigDecimal aux = BigDecimal.ZERO;
		for (MunicipalBondView m : municipalBondViews) {
			aux = aux.add(m.getValue());
		}
		return aux;
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBondView> getMunicipalBondsViewBetweenDatesByCashier(
			Long cashierId) {
		String namedQuery = "Deposit.findDetailedBetweenDatesByCashier";
		Query query = getEntityManager().createNamedQuery(namedQuery);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("cashierId", cashierId);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public int servedPeople() {
		if (summaryTotalCollecteds == null)
			return 0;
		Query query = getEntityManager().createNamedQuery(
				"Payment.findNumberBetweenDates");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		List<Long> list = query.getResultList();
		numberOfServedPeople = list.size();
		return list.size();
	}

	private Long transactionsNumber() {
		if (summaryTotalCollecteds == null)
			return 0L;
		Query query = getEntityManager().createNamedQuery(
				"Payment.depositsNumber");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		numberOfTransactions = (Long) query.getSingleResult();
		return numberOfTransactions;
	}

	public int totalServedPeople() {
		int aux = 0;
		if (payments != null) {
			aux = aux + payments.size();
		}
		return aux;
	}

	/**
	 * Generate reporte de cierre de jornada de una fecha determinada
	 * 
	 * @param start
	 * @return String
	 */
	public String printReport(Date start) {
		if (start == null) {
			Calendar c = Calendar.getInstance();
			startDate = c.getTime();
		} else {
			startDate = start;
		}
		endDate = startDate;
		findWorkday(endDate);
		generateClosingWorkdayCompleteReport();
		
		//rfarmijosm 2017-02-23
		replacementPaymentReport();
		
		return "/income/report/ClosingWorkdayReport.xhtml";
	}

	/**
	 * Filtrar payments repetidos debido a que en la consulta
	 * 'Payment.findByBranchAndDate' se están duplicando los payments
	 */
	private void filterPayments() {
		for (int i = 0; i < payments.size(); i++) {
			for (int j = i + 1; j < payments.size(); j++) {
				if (payments.get(i).getId() == payments.get(j).getId()) {
					payments.remove(j);
					j = j - 1;
				}
			}
		}

		creditNotePayments = new ArrayList<PaymentFraction>();

		for (Payment p : payments) {
			if (p.getStatus().equals(FinancialStatus.VALID)) {
				for (PaymentFraction pf : p.getPaymentFractions()) {
					if (pf.getPaymentType().equals(PaymentType.CREDIT_NOTE)
							&& !creditNotePayments.contains(pf)) {
						creditNotePayments.add(pf);
					}
				}
			}
		}

	}

	private List<PaymentFraction> orderPaymentFractionsByPaymentType(Payment p) {
		List<PaymentFraction> order = new ArrayList<PaymentFraction>();
		List<PaymentFraction> check = new ArrayList<PaymentFraction>();
		List<PaymentFraction> creditcard = new ArrayList<PaymentFraction>();
		List<PaymentFraction> creditNote = new ArrayList<PaymentFraction>();
		List<PaymentFraction> cash = new ArrayList<PaymentFraction>();

		for (PaymentFraction pf : p.getPaymentFractions()) {
			if (pf.getPaymentType().equals(PaymentType.CHECK)) {
				check.add(pf);
			}
			if (pf.getPaymentType().equals(PaymentType.CREDIT_CARD)) {
				creditcard.add(pf);
			}
			if (pf.getPaymentType().equals(PaymentType.CREDIT_NOTE)) {
				creditNote.add(pf);
			}
			if (pf.getPaymentType().equals(PaymentType.CASH)) {
				cash.add(pf);
			}
		}
		order.addAll(check);
		order.addAll(creditcard);
		order.addAll(creditNote);
		order.addAll(cash);
		return order;
	}

	private List<Long> findMunicipalBondIds() {
		List<Long> list = new ArrayList<Long>();
		for (Deposit d : deposits) {
			Long aux = d.getMunicipalBond().getId();
			if (!list.contains(aux)) {
				list.add(aux);
			}
		}
		return list;
	}

	/**
	 * Liquidar los municipalBonds que estaban en compensación
	 */
	public void liquidateMunicipalBondsInCompensation() {
		List<Long> municipalBondIds = findMunicipalBondIds();
		if (municipalBondIds.size() == 0)
			return;

		if (municipalBondIds.size() == 0)
			setReadyForPrint(false);
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.changeStatus");
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		Long municipalBondStatusId = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID");
		query.setParameter("municipalBondStatusId", municipalBondStatusId);
		query.setParameter("municipalBondIds", municipalBondIds);

		query.executeUpdate();
		setReadyForPrint(true);
	}

	public Long totalTransactions() {
		Long l = 0L;
		if (payments == null)
			return l;
		for (Payment p : payments) {
			l = l + p.getDeposits().size();
		}
		return l;
	}

	@SuppressWarnings("unchecked")
	private List<Payment> getPaymentsBetweenDates() {
		Query query = getEntityManager().createNamedQuery(
				"Payment.BetweenDates");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	private Long getNumberOfTillOpened(Long workdayId) {
		Query query = getEntityManager().createNamedQuery(
				"TillPermission.findNumberOpenTillsByWorkday");
		query.setParameter("workdayId", workdayId);
		return (Long) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	private List<Entry> getEntriesFromMunicipalBondsBetweenDates() {
		Query query = null;
		if (municipalBondStatus == null) {
			query = getEntityManager().createNamedQuery(
					"MunicipalBond.findEntriesBetweenEmissionDates");
			query.setParameter("statusId", statusIds);
		} else {
			query = getEntityManager()
					.createNamedQuery(
							"MunicipalBond.findEntriesBetweenEmissionDatesAndMunicipalBondStatus");
			query.setParameter("statusId", municipalBondStatus.getId());
		}

		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBondView> getMunicipalBondsViewBetweenDates() {
		String namedQuery = "MunicipalBond.findMunicipalBondViewBetweenDates";
		Query query = getEntityManager().createNamedQuery(namedQuery);
		query.setParameter("statusIds", statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<ReportView> getMunicipalBondsTotalEmittedBetweenDates() {
		String namedQuery = "";
		if (person == null) {
			namedQuery = "MunicipalBond.SumTotalEmitted";
		} else {
			namedQuery = "MunicipalBond.SumTotalEmittedByPerson";
		}

		Query query = getEntityManager().createNamedQuery(namedQuery);

		if (person != null)
			query.setParameter("personId", person.getId());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBond> getMunicipalBondsEmittedBetweenDates() {
		String namedQuery = "";
		if (person == null) {
			namedQuery = "MunicipalBond.findEmittedByDates";
		} else {
			namedQuery = "MunicipalBond.findEmittedByDatesAndEmitter";
		}

		Query query = getEntityManager().createNamedQuery(namedQuery);

		if (person != null)
			query.setParameter("personId", person.getId());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<MunicipalBondView> getMunicipalBondsViewBetweenDatesByEntry(
			Entry e) {
		String namedQuery = "";
		if (municipalBondStatus == null) {
			namedQuery = "MunicipalBond.findMunicipalBondViewBetweenDatesByEntry";
		} else {
			namedQuery = "MunicipalBond.findMunicipalBondViewBetweenDatesByMunicipalBondStatusAndEntry";
		}

		Query query = getEntityManager().createNamedQuery(namedQuery);
		if (municipalBondStatus != null) {
			query.setParameter("statusId", municipalBondStatus.getId());
		} else {
			query.setParameter("statusId", statusIds);
		}

		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("entryId", e.getId());
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Account> getTaxesAccountsFromMunicipalBondsBetweenDates() {
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.findATaxAccountsBetweenEmissionDatesByItem");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	private List<Account> getAccountsFromMunicipalBondsBetweenDates() {
		Query query = null;
		if (entry != null) {
			query = getEntityManager()
					.createNamedQuery(
							"MunicipalBond.findAccountBetweenEmissionDatesByEntryByItem");
			query.setParameter("entryId", entry.getId());
		} else {
			query = getEntityManager().createNamedQuery(
					"MunicipalBond.findAccountBetweenEmissionDatesByItem");
		}

		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	/**
	 * Genera todo para el reporte de recaudaciones por partidas
	 */
	private void generateCompleteReportByItems() {
		groupBy = "ac.accountCode";
		if (isClosingWorkday) {
			startDate = getInstance().getDate();
		}
		if (endDate == null)
			endDate = startDate;
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}

		loadStatuses();

		entryTotalCollecteds = getTotalsByItem(startDate, endDate);
		mergeValuesByAccountCode(entryTotalCollecteds);
		orderByGroupBy(entryTotalCollecteds);
		entryTotalCollectedsByCompensation = getTotalCompensationCollectedByItem(
				startDate, endDate);
		mergeValuesByAccountCode(entryTotalCollectedsByCompensation);
		orderByGroupBy(entryTotalCollectedsByCompensation);
		entryTotalCollectedsInAgreement = getAllTotalsPaymentAgrement();
		orderByGroupBy(entryTotalCollectedsInAgreement);
		sumAllValuesByEntry();
		totalDiscountCashCollected();
		totalDiscountCompensationCollected();
		summaryTotalCollecteds = getSummaryTotalCollected();
		sumTotalSummaryCollected();
		servedPeople();
		transactionsNumber();
	}

	/**
	 * Mezcla las cuentas para ubicar en una misma cuenta los valores de año
	 * actual y años previos, agrupados por groupBy que puede ser el código del
	 * rubro o el código de la cuenta contable
	 * 
	 * @param list
	 */
	private void mergeValuesByAccountCode(List<EntryTotalCollected> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(i).getGroupBy().equals(list.get(j).getGroupBy())) {
					list.get(i).setValue(
							list.get(i).getValue().add(list.get(j).getValue()));
					if (list.get(i).getPreviousYears() == null)
						list.get(i).setPreviousYears(BigDecimal.ZERO);
					if (list.get(j).getPreviousYears() == null)
						list.get(j).setPreviousYears(BigDecimal.ZERO);
					list.get(i).setPreviousYears(
							list.get(i).getPreviousYears()
									.add(list.get(j).getPreviousYears()));
					list.get(i).setTotal(
							list.get(i).getTotal().add(list.get(j).getTotal()));
					list.remove(j);
					if (j > 0)
						j = j - 1;
				}
			}
		}
	}

	/**
	 * Total recaudado por compensación por partidas
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalCompensationCollectedByItem(
			Date startDate, Date endDate) {
		List<EntryTotalCollected> result;

		List<Long> statuses = new ArrayList<Long>();
		statuses.add(compensationStatus);

		if (groupBy == null)
			groupBy = "ac.accountCode";

		result = getAllTotalsByItem(statuses);
		if (entryTotalDiscountByCompensation == null)
			entryTotalDiscountByCompensation = new ArrayList<EntryTotalCollected>();
		entryTotalDiscountByCompensation.addAll(getAllDiscountByItem(statuses));
		result.addAll(getAllSurchargeByItem(statuses));
		result.addAll(getAllTaxesByItem(statuses));
		result.addAll(getAllInterestByItem(statuses, interestAccount, "GAD"));
		result.addAll(getAllInterestByItem(statuses, interestAccountEmaalep,
				"EMAALEP"));

		return result;
	}

	/**
	 * Total por items años previos, año actual y total por
	 * municipalBondStatuses
	 * 
	 * @param statusIds
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getAllTotalsByItem(List<Long> statusIds) {
		List<EntryTotalCollected> currentPeriod = getTotalsByItems(startDate,
				endDate, statusIds, false);
		List<EntryTotalCollected> previousPeriod = getTotalsByItems(startDate,
				endDate, statusIds, true);
		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getValue().add(et.getPreviousYears()));
					break;
				}
			}
			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				et1.setTotal(et1.getPreviousYears());
				missing.add(et1);
			}
		}
		currentPeriod.addAll(missing);
		return currentPeriod;
	}

	/**
	 * Total de descuentos por items años previos, año actual y total por
	 * municipalBondStatuses
	 * 
	 * @param statusIds
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getAllDiscountByItem(List<Long> statusIds) {
		List<EntryTotalCollected> currentPeriod = getTotalsDiscountByItems(
				startDate, endDate, statusIds, false);
		List<EntryTotalCollected> previousPeriod = getTotalsDiscountByItems(
				startDate, endDate, statusIds, true);
		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getValue().add(et.getPreviousYears()));
					break;
				}
			}
			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				et1.setTotal(et1.getPreviousYears());
				missing.add(et1);
			}
		}
		currentPeriod.addAll(missing);
		return currentPeriod;
	}

	/**
	 * Total de descuentos por items entre fechas, municipalBondStatuses y si es
	 * año actual o años previos
	 * 
	 * @param statusIds
	 * @param startDate
	 * @param endDate
	 * @param municipalBondStatusIds
	 * @param isPreviousYears
	 * @return List<EntryTotalCollected>
	 */
	@SuppressWarnings("unchecked")
	private List<EntryTotalCollected> getTotalsDiscountByItems(Date startDate,
			Date endDate, List<Long> municipalBondStatusIds,
			boolean isPreviousYears) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(ac.id, ac.accountName,"
				+ groupBy
				+ ", SUM(i.total)) "
				+ "from MunicipalBond m join m.discountItems i "
				+ "join i.entry e left "
				+ "join e.account ac "
				+ "where "
				+ "i.total > 0 and "
				+ "m.id in "
				+ "(select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id in (:municipalBondStatusIds) ";

		if (isPreviousYears) {
			sql = sql + "AND m.emisionPeriod < :emisionPeriod ";
		} else {
			sql = sql + "AND m.emisionPeriod = :emisionPeriod ";
		}

		sql = sql + "AND pa is null)" + " GROUP BY ac.id, ac.accountName, "
				+ groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusIds", municipalBondStatusIds);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		List<EntryTotalCollected> result = query.getResultList();
		for (EntryTotalCollected e : result) {
			e.setIsDiscount(true);
		}
		return result;
	}

	/**
	 * Total de recargos por items años previos, año actual y total por
	 * municipalBondStatuses
	 * 
	 * @param statusIds
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getAllSurchargeByItem(List<Long> statusIds) {
		List<EntryTotalCollected> currentPeriod = getTotalsSurchargeByItems(
				startDate, endDate, statusIds, false);
		List<EntryTotalCollected> previousPeriod = getTotalsSurchargeByItems(
				startDate, endDate, statusIds, true);
		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getValue().add(et.getPreviousYears()));
					break;
				}
			}
			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				et1.setTotal(et1.getPreviousYears());
				missing.add(et1);
			}
		}
		currentPeriod.addAll(missing);
		return currentPeriod;
	}

	/**
	 * Total de impuestos por items años previos, año actual y total por
	 * municipalBondStatuses
	 * 
	 * @param statusIds
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getAllTaxesByItem(List<Long> statusIds) {
		List<EntryTotalCollected> currentPeriod = getTotalsTaxByItems(
				startDate, endDate, statusIds, false);
		List<EntryTotalCollected> previousPeriod = getTotalsTaxByItems(
				startDate, endDate, statusIds, true);
		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getValue().add(et.getPreviousYears()));
					break;
				}
			}
			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				et1.setTotal(et1.getPreviousYears());
				missing.add(et1);
			}
		}
		currentPeriod.addAll(missing);
		return currentPeriod;
	}

	/**
	 * Total de impuestos por items entre fechas, municipalBondStatuses y si es
	 * año actual o años previos
	 * 
	 * @param statusIds
	 * @param startDate
	 * @param endDate
	 * @param municipalBondStatusIds
	 * @param isPreviousYears
	 * @return List<EntryTotalCollected>
	 */
	@SuppressWarnings("unchecked")
	private List<EntryTotalCollected> getTotalsTaxByItems(Date startDate,
			Date endDate, List<Long> municipalBondStatusIds,
			boolean isPreviousYears) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(ac.id, ac.accountName, ac.accountCode, SUM(i.value)) "
				+ "from MunicipalBond m "
				+ "join m.taxItems i "
				+ "join i.tax t "
				+ "join t.taxAccount ac "
				+ "where "
				+ "i.value > 0 and "
				+ "m.id in "
				+ "(select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate ";

		if (isPreviousYears) {
			sql = sql + "AND m.emisionPeriod < :emisionPeriod ";
		} else {
			sql = sql + "AND m.emisionPeriod = :emisionPeriod ";
		}

		sql = sql
				+ "AND m.municipalBondStatus.id in (:municipalBondStatusIds) "
				+ "AND pa is null)"
				+ "GROUP BY ac.id, ac.accountName ORDER BY ac.id";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusIds", municipalBondStatusIds);
		return query.getResultList();
	}

	/**
	 * Total recaudado entre fechas por partidas
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalsByItem(Date startDate,
			Date endDate) {
		List<EntryTotalCollected> result;

		if (groupBy == null)
			groupBy = "ac.accountCode";

		result = getAllTotalsByItem(getPaidStatuses());
		entryTotalDiscount = new ArrayList<EntryTotalCollected>();
		entryTotalDiscount.addAll(getAllDiscountByItem(getPaidStatuses()));
		result.addAll(getAllSurchargeByItem(getPaidStatuses()));
		result.addAll(getAllTaxesByItem(getPaidStatuses()));
		result.addAll(getAllInterestByItem(getPaidStatuses(), interestAccount,
				"GAD"));
		result.addAll(getAllInterestByItem(getPaidStatuses(),
				interestAccountEmaalep, "EMAALEP"));

		return result;
	}

	/**
	 * Total intereses años previos, año actual y total por partidas y
	 * municipalBondStatuses
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getAllInterestByItem(List<Long> statusId,
			Account account, String institution) {
		List<EntryTotalCollected> currentPeriod = getTotalsInterestByItems(
				startDate, endDate, statusId, false, account, institution);
		List<EntryTotalCollected> previousPeriod = getTotalsInterestByItems(
				startDate, endDate, statusId, true, account, institution);
		if (currentPeriod.size() == 0) {
			for (EntryTotalCollected et1 : previousPeriod) {
				et1.setPreviousYears(et1.getValue());
				et1.setTotal(et1.getPreviousYears());
				et1.setValue(BigDecimal.ZERO);
			}
			return previousPeriod;
		}
		for (EntryTotalCollected et1 : previousPeriod) {
			for (EntryTotalCollected et : currentPeriod) {
				et.setPreviousYears(et1.getValue());
				et.setTotal(et.getValue().add(et.getPreviousYears()));
			}
		}
		return currentPeriod;
	}

	/**
	 * Total de intereses por items entre fechas, municipalBondStatuses y si es
	 * año actual o años previos
	 * 
	 * @param statusIds
	 * @param startDate
	 * @param endDate
	 * @param municipalBondStatusIds
	 * @param isPreviousYears
	 * @return List<EntryTotalCollected>
	 */
	@SuppressWarnings("unchecked")
	private List<EntryTotalCollected> getTotalsInterestByItems(Date startDate,
			Date endDate, List<Long> municipalBondStatusIds,
			boolean isPreviousYears, Account account, String institution) {
		String strListEmaalep = systemParameterService
				.findParameter(ENTRIES_EMAALEP_LIST);
		List<Long> listEmaalep = new ArrayList<Long>();
		listEmaalep = GimUtils.convertStringWithCommaToListLong(strListEmaalep);

		// List<Long> listEmaalep= new ArrayList<Long>();
		// for (Long i = (long) 530 ; i <= 549 ; i++){
		// listEmaalep.add(i);
		// }
		//
		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(SUM(m.interest)) "
				+ "from MunicipalBond m "
				+ "join m.entry e "
				+ "left join e.account ac "
				+ "where "
				+ "m.interest > 0 and "
				+ "m.id in "
				+ "(select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate ";

		if (institution.compareTo("GAD") == 0)
			sql = sql + "AND m.entry.id not in (:listEmaalep) ";
		else
			sql = sql + "AND m.entry.id in (:listEmaalep) ";

		if (isPreviousYears) {
			sql = sql + "AND m.emisionPeriod < :emisionPeriod ";
		} else {
			sql = sql + "AND m.emisionPeriod = :emisionPeriod ";
		}

		sql = sql
				+ "AND m.municipalBondStatus.id in (:municipalBondStatusIds) "
				+ "AND pa is null)";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusIds", municipalBondStatusIds);
		query.setParameter("listEmaalep", listEmaalep);

		List<EntryTotalCollected> list = query.getResultList();

		deleteEmptyEntryTotalCollected(list);
		//
		// for (EntryTotalCollected e : list) {
		// e.setAccount(interestAccount.getAccountName());
		// e.setGroupBy(interestAccount.getAccountCode());
		// e.setEntry(interestAccount.getAccountName());
		// e.setId(interestAccount.getId());
		// }

		for (EntryTotalCollected e : list) {
			e.setAccount(account.getAccountName());
			e.setGroupBy(account.getAccountCode());
			e.setEntry(account.getAccountName());
			e.setId(account.getId());
		}

		return list;
	}

	/**
	 * Total de recargos por items entre fechas, municipalBondStatuses y si es
	 * año actual o años previos
	 * 
	 * @param statusIds
	 * @param startDate
	 * @param endDate
	 * @param municipalBondStatusIds
	 * @param isPreviousYears
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalsSurchargeByItems(Date startDate,
			Date endDate, List<Long> municipalBondStatusIds,
			boolean isPreviousYears) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(ac.id, ac.accountName,"
				+ groupBy
				+ ", SUM(i.total)) "
				+ "from MunicipalBond m "
				+ "join m.surchargeItems i "
				+ "join i.entry e "
				+ "left join e.account ac "
				+ "where "
				+ "i.total > 0 and "
				+ "m.id in "
				+ "(select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate ";

		if (isPreviousYears) {
			sql = sql + "AND m.emisionPeriod < :emisionPeriod ";
		} else {
			sql = sql + "AND m.emisionPeriod = :emisionPeriod ";
		}

		sql = sql
				+ "AND m.municipalBondStatus.id in (:municipalBondStatusIds) "
				+ "AND pa is null)" + " GROUP BY ac.id, ac.accountName, "
				+ groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusIds", municipalBondStatusIds);
		List<EntryTotalCollected> result = query.getResultList();
		return result;
	}

	/**
	 * Total de items entre fechas, municipalBondStatuses y si es año actual o
	 * años previos
	 * 
	 * @param statusIds
	 * @param startDate
	 * @param endDate
	 * @param municipalBondStatusIds
	 * @param isPreviousYears
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalsByItems(Date startDate,
			Date endDate, List<Long> municipalBondStatusIds,
			boolean isPreviousYears) {
		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(ac.id, ac.accountName,"
				+ groupBy
				+ ", SUM(i.total)) "
				+ "from MunicipalBond m "
				+ "join m.items i "
				+ "join i.entry e left "
				+ "join e.account ac "
				+ "where "
				+ "i.total > 0 and "
				+ "m.id in (select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id in (:municipalBondStatusIds) ";

		if (isPreviousYears) {
			sql = sql + "AND m.emisionPeriod < :emisionPeriod ";
		} else {
			sql = sql + "AND m.emisionPeriod = :emisionPeriod ";
		}
		sql = sql + "AND pa is null)" + " GROUP BY ac.id, ac.accountName, "
				+ groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusIds", municipalBondStatusIds);
		return query.getResultList();
	}

	/**
	 * Generate reporte de recaudaciones por entry del municipalBond
	 */
	private void generateCompleteReport() {
		if (interestAccount == null)
			loadInterestAccount();
		if (isClosingWorkday) {
			startDate = getInstance().getDate();
		}
		if (endDate == null)
			endDate = startDate;
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}

		loadStatuses();

		entryTotalCollecteds = getAllTotalEffectiveCollectedByEntry();
		orderByGroupBy(entryTotalCollecteds);
		entryTotalCollectedsInAgreement = getAllTotalsPaymentAgrement();
		orderByGroupBy(entryTotalCollectedsInAgreement);
		entryTotalCollectedsByCompensation = getAllTotalsCompensationCollectedByEntry();
		orderByGroupBy(entryTotalCollectedsByCompensation);
		sumAllValuesByEntry();
		summaryTotalCollecteds = getSummaryTotalCollected();
		sumTotalSummaryCollected();
		servedPeople();
		transactionsNumber();
	}

	private void sumAllValuesByEntry() {
		totalValuesCashCollectedByEntry();
		totalValuesCompensationCollectedByEntry();
	}

	/**
	 * Total recaudado en compensación por entry del municipalBond
	 */
	private void totalValuesCompensationCollectedByEntry() {
		discountsValueByCompensation = BigDecimal.ZERO;
		surchargesValueCompensation = BigDecimal.ZERO;
		interestValueCompensation = BigDecimal.ZERO;
		taxesValueCompensation = BigDecimal.ZERO;
		currentValueCompensation = BigDecimal.ZERO;
		previousValueCompensation = BigDecimal.ZERO;
		totalBondsCollectedByCompensation = 0L;

		for (EntryTotalCollected e : entryTotalCollectedsByCompensation) {
			if (e.getMunicipalBondsNumber() != null)
				totalBondsCollectedByCompensation = totalBondsCollectedByCompensation
						+ e.getMunicipalBondsNumber();
			if (e.getDiscount() != null) {
				discountsValueByCompensation = discountsValueByCompensation
						.add(e.getDiscount());
			}

			if (e.getSurcharge() != null) {
				surchargesValueCompensation = surchargesValueCompensation.add(e
						.getSurcharge());
			}

			if (e.getInterest() != null) {
				interestValueCompensation = interestValueCompensation.add(e
						.getInterest());
			}

			if (e.getPreviousYears() != null) {
				previousValueCompensation = previousValueCompensation.add(e
						.getPreviousYears());
			}

			if (e.getValue() != null) {
				currentValueCompensation = currentValueCompensation.add(e
						.getValue());
			}

			if (e.getTaxes() != null) {
				taxesValueCompensation = taxesValueCompensation.add(e
						.getTaxes());
			}
		}

	}

	/**
	 * Total de efectivo recaudado por entry del municipalBond
	 */
	private void totalValuesCashCollectedByEntry() {
		discountsValueEffective = BigDecimal.ZERO;
		surchargesValueEffective = BigDecimal.ZERO;
		interestValueEffective = BigDecimal.ZERO;
		taxesValueEffective = BigDecimal.ZERO;

		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getDiscount() != null) {
				discountsValueEffective = discountsValueEffective.add(e
						.getDiscount());
			}

			if (e.getSurcharge() != null) {
				surchargesValueEffective = surchargesValueEffective.add(e
						.getSurcharge());
			}

			if (e.getInterest() != null) {
				interestValueEffective = interestValueEffective.add(e
						.getInterest());
			}

			if (e.getTaxes() != null) {
				taxesValueEffective = taxesValueEffective.add(e.getTaxes());
			}
		}

	}

	private BigDecimal findTotalCreditNoteBetweenDates(Date start, Date end) {
		String sql = "select sum(pf.paidAmount) from Payment payment "
				+ "join payment.paymentFractions pf "
				+ "where payment.date BETWEEN :startDate AND :endDate "
				+ "and pf.creditNote is not null "
				+ "and payment.status = 'VALID'";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		BigDecimal res = (BigDecimal) query.getSingleResult();
		res = res == null ? BigDecimal.ZERO : res;
		return res;
	}

	/**
	 * Total recaudado en compensación por entry del municipalbond
	 * 
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getAllTotalsCompensationCollectedByEntry() {
		List<EntryTotalCollected> currentPeriod = getTotalCompensationCollectedByEntryCurrentPeriod(
				startDate, endDate);
		List<EntryTotalCollected> previousPeriod = getTotalCompensationCollectedByEntryPreviousYears(
				startDate, endDate);
		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setInterest(et.getInterest().add(et1.getInterest()));
					et.setTaxes(et.getTaxes().add(et1.getTaxes()));
					et.setSurcharge(et.getSurcharge().add(et1.getSurcharge()));
					et.setDiscount(et.getDiscount().add(et1.getDiscount()));
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getTotal().add(et1.getTotal()));
					break;
				}
			}
			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				missing.add(et1);
			}
		}
		currentPeriod.addAll(missing);
		return currentPeriod;
	}

	private List<EntryTotalCollected> getTotalCompensationCollectedByEntryPreviousYears(
			Date startDate, Date endDate) {
		if (groupBy == null)
			groupBy = "e.code";

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,count(m.id), e.name,"
				+ groupBy
				+ ", SUM(m.value), SUM(m.discount), SUM(m.surcharge),"
				+ " SUM(m.interest), SUM(m.taxesTotal), SUM (m.paidTotal)) "
				+ "from MunicipalBond m join m.entry e left join e.account ac where m.id in "
				+ "(select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id = :municipalBondStatusId "
				+ "AND m.emisionPeriod < :emisionPeriod "
				+ "AND pa is null) "
				+ "GROUP BY e.id, e.name, " + groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusId", compensationStatus);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getTotalCompensationCollectedByEntryCurrentPeriod(
			Date startDate, Date endDate) {
		if (groupBy == null)
			groupBy = "e.code";

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,count(m.id), e.name,"
				+ groupBy
				+ ", SUM(m.value), SUM(m.discount), SUM(m.surcharge),"
				+ " SUM(m.interest), SUM(m.taxesTotal), SUM (m.paidTotal)) "
				+ "from MunicipalBond m join m.entry e left join e.account ac where m.id in "
				+ "(select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id = :municipalBondStatusId "
				+ "AND m.emisionPeriod = :emisionPeriod "
				+ "AND pa is null) "
				+ "GROUP BY e.id, e.name, " + groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusId", compensationStatus);
		return query.getResultList();
	}

	/**
	 * Total recaudado por acuerdos de pago entre fechas, años previos, año
	 * actual, totales
	 * 
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getAllTotalsPaymentAgrement() {
		List<EntryTotalCollected> currentPeriod = getTotalInPaymentAgreementByEntryCurrentPeriod(
				startDate, endDate);
		List<EntryTotalCollected> previousPeriod = getTotalInPaymentAgreementByEntryPreviosuYears(
				startDate, endDate);
		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setInterest(et.getInterest().add(et1.getInterest()));
					et.setTaxes(et.getTaxes().add(et1.getTaxes()));
					et.setSurcharge(et.getSurcharge().add(et1.getSurcharge()));
					et.setDiscount(et.getDiscount().add(et1.getDiscount()));
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getTotal().add(et1.getTotal()));
					break;
				}
			}
			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				missing.add(et1);
			}
		}
		currentPeriod.addAll(missing);
		return currentPeriod;
	}

	/**
	 * Total recaudado años previos por entry del municipalBond
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalInPaymentAgreementByEntryPreviosuYears(
			Date startDate, Date endDate) {
		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,count(d.id), e.name,e.code, SUM(d.capital), SUM(d.discount), SUM(d.surcharge), "
				+ " SUM(d.interest), SUM(d.paidTaxes), SUM (d.value)) from MunicipalBond m "
				+ "join m.deposits d "
				+ "join m.paymentAgreement pa "
				+ "join m.entry e "
				+ "left join e.account ac "
				+ "where d.date Between :startDate and :endDate AND "
				+ "m.emisionPeriod < :emisionPeriod AND "
				+ "m.municipalBondStatus.id in (:municipalBondStatusIds) AND "
				+ "pa is not null "
				+ "GROUP BY e.id, e.name,e.code ORDER BY e.code";
		List<Long> statusIds = new ArrayList<Long>();
		statusIds.add(paidStatus);
		statusIds.add(agreementStatus);
		Query query = query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusIds", statusIds);
		return query.getResultList();
	}

	/**
	 * Total recaudado año actual por entry del municipalBond
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalInPaymentAgreementByEntryCurrentPeriod(
			Date startDate, Date endDate) {
		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,count(d.id), e.name,e.code, SUM(d.capital), SUM(d.discount), SUM(d.surcharge), "
				+ " SUM(d.interest), SUM(d.paidTaxes), SUM (d.value)) from MunicipalBond m "
				+ "join m.deposits d "
				+ "join m.paymentAgreement pa "
				+ "join m.entry e "
				+ "left join e.account ac "
				+ "where d.date Between :startDate and :endDate AND "
				+ "m.emisionPeriod = :emisionPeriod AND "
				+ "m.municipalBondStatus.id in (:municipalBondStatusIds) AND "
				+ "pa is not null "
				+ "GROUP BY e.id, e.name,e.code ORDER BY e.code";
		List<Long> statusIds = new ArrayList<Long>();
		statusIds.add(paidStatus);
		statusIds.add(agreementStatus);
		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusIds", statusIds);
		return query.getResultList();
	}

	/**
	 * Total recaudado en efecivo (PaidStatuses)
	 * 
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getAllTotalEffectiveCollectedByEntry() {
		List<EntryTotalCollected> currentPeriod = getTotalEffectiveCollectedByEntryCurrentPeriod(
				startDate, endDate);
		List<EntryTotalCollected> previousPeriod = getTotalEffectiveCollectedByEntryPreviousYears(
				startDate, endDate);
		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setDiscount(et.getDiscount().add(et1.getDiscount()));
					et.setSurcharge(et.getSurcharge().add(et1.getSurcharge()));
					et.setInterest(et.getInterest().add(et1.getInterest()));
					et.setTaxes(et.getTaxes().add(et1.getTaxes()));
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getTotal().add(et1.getTotal()));
					break;
				}
			}

			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				missing.add(et1);
			}
		}
		currentPeriod.addAll(missing);
		return currentPeriod;
	}

	/**
	 * Total de efecivo recaudado(paidStatuses) años previos
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalEffectiveCollectedByEntryPreviousYears(
			Date startDate, Date endDate) {
		if (groupBy == null)
			groupBy = "e.code";

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,count(m.id), e.name,"
				+ groupBy
				+ ", SUM(m.value), SUM(m.discount), SUM(m.surcharge),"
				+ " SUM(m.interest), SUM(m.taxesTotal), SUM (m.paidTotal)) "
				+ "from MunicipalBond m join m.entry e left join e.account ac where m.id in "
				+ "(select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate "
				+ "AND m.emisionPeriod < :emisionPeriod "
				+ "AND m.municipalBondStatus.id in (:municipalBondStatusIds) "
				+ "AND pa is null) "
				+ "GROUP BY e.id, e.name, "
				+ groupBy
				+ " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusIds", getPaidStatuses());
		return query.getResultList();
	}

	public void loadStatuses() {
		if (systemParameterService == null)
			initializeSystemParameterService();
		paidStatus = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID");
		externalPaidStatus = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL");
		agreementStatus = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		compensationStatus = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
	}

	private List<Long> getPaidStatuses() {
		List<Long> list = new ArrayList<Long>();
		list.add(paidStatus);
		list.add(externalPaidStatus);
		return list;
	}

	/**
	 * Total de efecivo (paidStatuses) recaudado año actual
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalEffectiveCollectedByEntryCurrentPeriod(
			Date startDate, Date endDate) {
		if (groupBy == null)
			groupBy = "e.code";

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, count(m.id),e.name,"
				+ groupBy
				+ ", SUM(m.value), SUM(m.discount), SUM(m.surcharge),"
				+ " SUM(m.interest), SUM(m.taxesTotal), SUM (m.paidTotal)) "
				+ "from MunicipalBond m join m.entry e left join e.account ac where m.id in "
				+ "(select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate "
				+ "AND m.emisionPeriod = :emisionPeriod "
				+ "AND m.municipalBondStatus.id in (:municipalBondStatusIds) "
				+ "AND pa is null) "
				+ "GROUP BY e.id, e.name, "
				+ groupBy
				+ " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("municipalBondStatusIds", getPaidStatuses());
		return query.getResultList();
	}

	private void generateClosingWorkdayCompleteReport() {
		if (isClosingWorkday)
			startDate = getInstance().getDate();

		if (endDate == null)
			endDate = startDate;
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}

		payments = getPaymentsBetweenDates();
		entryTotalCollecteds = getTotalCollectedByEntry(false);
		entryTotalCollectedsByCompensation = getTotalCompensationCollectedByEntry();
		entryTotalCollectedsInAgreement = getTotalInAgreementCollectedByEntry();
		if (discountsValueEffective == null)
			discountsValueEffective = BigDecimal.ZERO;
		if (discountsValueByCompensation == null)
			discountsValueByCompensation = BigDecimal.ZERO;
		summaryTotalCollecteds = getSummaryTotalCollected();
		sumTotalSummaryCollected();
		deposits = getDepositsFromPayments(payments);
		servedPeople();
		transactionsNumber();
		//rfarmijosm 2017-02-20
		replacementPaymentReport();
	}	
	
	/**
	 * rfarmijosm 2017-02-20
	 * obtiene el reporte de bajas de mb con abonos
	 */
	public void replacementPaymentReport() {
		String sql = "select rpa.id, rpa.date f_baja, rpa.detail, rpa.total total_abanado, "
				+ "re.identificationnumber, re.name, mb.number, mb.value, mb.emisiondate, ent.code, ent.name entry"
				+ "name "
				+ "from gimprod.ReplacementPaymentAgreement rpa "
				+ "inner join gimprod.municipalbond mb on rpa.municipalbond_id = mb.id "
				+ "inner join gimprod.resident re on mb.resident_id = re.id "
				+ "inner join gimprod.entry ent on mb.entry_id = ent.id "
				+ "where rpa.date between :startDate and :endDate "
				+ "order by rpa.date, mb.number";
		Query query = getEntityManager().createNativeQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		
		replacementAgreements = NativeQueryResultsMapper.map(query.getResultList(), ReplacementAgreementDTO.class);
		/*for(ReplacementAgreementDTO ra: replacementAgreements){
			System.out.println(ra.getEnt_name()+" "+ra.getMb_number());
		}*/
		//return query.getResultList();
		
		String sqlPartidas="select ac.accountcode, ac.accountname, sum(it.total) total "
				+ "from gimprod.ReplacementPaymentAgreement rpa "
				+ "inner join gimprod.municipalbond mb on rpa.municipalbond_id = mb.id "
				+ "left outer join gimprod.item it on mb.id = it.municipalbond_id "
				+ "inner join gimprod.entry ent on it.entry_id = ent.id "
				+ "inner join gimprod.account ac on ent.account_id = ac.id "
				+ "where rpa.date between :startDate and :endDate "
				+ "group by ac.accountcode, ac.accountname "
				+ "order by ac.accountcode, ac.accountname";
		
		Query queryPartidas = getEntityManager().createNativeQuery(sqlPartidas);
		queryPartidas.setParameter("startDate", startDate);
		queryPartidas.setParameter("endDate", endDate);
		
		replacementAccountDTOs = NativeQueryResultsMapper.map(queryPartidas.getResultList(), ReplacementAccountDTO.class);
	}

	/**
	 * Accounts de los items de los municipalbonds para el resumen de cuentas
	 */
	private void findAccountsForEmissionSummary() {
		parentAccounts = new ArrayList<Account>();
		List<Account> accountsFromMunicipalBond = new ArrayList<Account>();

		accountsFromMunicipalBond = getAccountsFromMunicipalBondsBetweenDates();
		accountsFromMunicipalBond
				.addAll(getTaxesAccountsFromMunicipalBondsBetweenDates());

		for (Account ac : accountsFromMunicipalBond) {
			if (!parentAccounts.contains(ac)) {
				Account ac1 = parentForSummary(ac);
				if (ac1 != null && !parentAccounts.contains(ac1)) {
					parentAccounts.add(ac1);
				}
			}
		}

		orderByAccountCode(parentAccounts);
	}

	private void generateEmissionSummaryTree() {
		mainAccountBalance.setChildren(new ArrayList<AccountBalance>());
		findAccountsForEmissionSummary();
		for (Account ac : parentAccounts) {
			AccountBalance ab = buildAccountBalance(ac);
			mainAccountBalance.add(ab);
		}

		if (rootNode == null) {
			try {
				getTreeNode();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
	}

	public void generateSummaryTree() {
		if (!globalReport && isFromIncome) {
			entryTotalCollectedsForTree = getTotalsByItem(startDate, endDate);
			mergeValuesByAccountCode(entryTotalCollectedsForTree);
			entryTotalCollectedsByCompensationForTree = getTotalCompensationCollectedByItem(
					startDate, endDate);
			mergeValuesByAccountCode(entryTotalCollectedsByCompensationForTree);
			entryTotalCollectedsInAgreementForTree = entryTotalCollectedsInAgreement;
		}

		if (globalReport && isFromIncome) {
			entryTotalCollectedsForTree = entryTotalCollecteds;
			entryTotalCollectedsByCompensationForTree = entryTotalCollectedsByCompensation;
			entryTotalCollectedsInAgreementForTree = entryTotalCollectedsInAgreement;
		}

		mainAccountBalance.setChildren(new ArrayList<AccountBalance>());

		findAccounts();

		for (Account ac : parentAccounts) {
			AccountBalance ab = buildAccountBalance(ac);
			mainAccountBalance.add(ab);
		}

		if (rootNode == null) {
			try {
				getTreeNode();
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}

	}

	private void findWorkday(Date date) {
		Query query = getEntityManager().createNamedQuery("Workday.findByDate");
		query.setParameter("date", date);
		List<Workday> list = query.getResultList();
		if (list.size() > 0) {
			this.setInstance(list.get(0));
		} else {
			this.setInstance(null);
		}

	}

	/**
	 * Genera reportes de cierre de jornada o de recaudaciones o de partidas
	 */
	public void generateGlobalReport() {
		rootNode = null;
		if (startDate != null && endDate != null && startDate.equals(endDate)) {
			findWorkday(startDate);
		} else {
			this.setInstance(null);
		}

		if (isClosingWorkday) {
			groupBy = "e.code";
			generateCompleteReport();
		} else {
			loadInterestAccount();
			if (globalReport) {
				generateCompleteReportByItems();
			} else {
				groupBy = "e.code";
				generateCompleteReport();
				// HABILITAR para revisar errores en cuadres de cajas y reportes
				// de recaudaciones o partidas
				// findMistakesInValuesByEntry(startDate, endDate,
				// pendingStatus.getId(),76L);
				// findMistakesInValues(startDate, endDate, paidStatus,true);
				// findMistakesInValues(startDate, endDate, paidStatus,false);
				// findMistakesInValues(startDate, endDate, compensationStatus);
			}
			generateSummaryTree();
		}
		replacementPaymentReport();
	}

	/**
	 * Ordenar por el groupBy que puede ser el código del entry o el código de
	 * la cuenta contable
	 * 
	 * @param list
	 */
	private void orderByGroupBy(List<EntryTotalCollected> list) {
		List<String> groupBy = new ArrayList<String>();
		List<EntryTotalCollected> res = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et : list) {
			if (et != null)
				groupBy.add(et.getGroupBy());
		}

		Collections.sort(groupBy);

		for (String s : groupBy) {
			for (EntryTotalCollected ac : list) {
				if (ac != null && ac.getGroupBy().equals(s)
						&& !res.contains(ac)) {
					res.add(ac);
				}
			}
		}

		for (EntryTotalCollected ac : list) {
			if (!res.contains(ac)) {
				res.add(ac);
			}
		}

		list.clear();
		list.addAll(res);
	}

	/**
	 * Recupear MunicipalBonds por el municipalBondStatus y si está o no en
	 * acuerdo de pago
	 * 
	 * @param statusId
	 * @param isPaymentAgreement
	 * @return List<MunicipalBond>
	 */
	private List<MunicipalBond> findMunicipalBonds(Long statusId,
			boolean isPaymentAgreement) {
		String sql = "";

		if (!isPaymentAgreement) {
			sql = "select distinct m from MunicipalBond m "
					+ "join m.deposits d " + "left join m.paymentAgreement pa "
					+ "where d.date Between :startDate and :endDate "
					+ "AND m.municipalBondStatus.id = :municipalBondStatusId "
					+ "AND pa is null ";
		} else {
			sql = "select distinct m from MunicipalBond m "
					+ "left join m.paymentAgreement pa "
					+ "where m.liquidationDate Between :startDate and :endDate "
					+ "AND m.municipalBondStatus.id = :municipalBondStatusId "
					+ "AND pa is not null ";
		}
		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", statusId);
		return query.getResultList();
	}

	private List<MunicipalBond> findMunicipalBonds(Long statusId, Long entryId) {
		String sql = "select distinct m from MunicipalBond m "
				+ "left join m.paymentAgreement pa " + "left join m.entry e "
				+ "where m.emisionDate Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id = :municipalBondStatusId "
				+ "AND e.id = :entryId " + "AND pa is null ";
		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", statusId);
		query.setParameter("entryId", entryId);
		return query.getResultList();
	}

	/**
	 * Busca errores en municipalbonds sea en la suma de sus items o en la suma
	 * de sus valores: value + interest + recargos -descuentos = paidtotal entre
	 * fechas y por rubro
	 * 
	 * @param startDate
	 * @param endDate
	 * @param statusId
	 * @param entryId
	 */
	private void findMistakesInValuesByEntry(Date startDate, Date endDate,
			Long statusId, Long entryId) {
		List<Long> ids = new ArrayList<Long>();
		List<Long> paidTotalIds = new ArrayList<Long>();
		List<MunicipalBond> municipalBonds = findMunicipalBonds(statusId,
				entryId);
		for (MunicipalBond mb : municipalBonds) {
			if (!isPaidTotalCorrect(mb)) {
				paidTotalIds.add(mb.getId());
			}

			if (!isTotalDepositsCorrect(mb)) {
				ids.add(mb.getId());
			}
		}
		isMunicipalBondItemsTotalCorrect(municipalBonds, statusId, false);
		if (paidTotalIds.size() > 0)
			System.out
					.println("::::::::::::::::::::.statusId::::::::::::::"
							+ statusId
							+ ":::::::::::::::::::::::::::::::::::::mistake in sum of paidtotal :::::::::::::::::. "
							+ paidTotalIds);
		if (ids.size() > 0)
			System.out
					.println("::::::::::::::::::::.statusId::::::::::::::"
							+ statusId
							+ ":::::::::::::::::::::::::::::::::::::mistake in total deposits :::::::::::::::::. "
							+ ids);
	}

	/**
	 * Busca errores en municipalbonds sea en la suma de sus items entre fechas
	 * y si está en acuerdo de pago o no
	 * 
	 * @param startDate
	 * @param endDate
	 * @param statusId
	 * @param ispaymentAgreement
	 */
	private void findMistakesInValues(Date startDate, Date endDate,
			Long statusId, Boolean ispaymentAgreement) {
		List<Long> ids = new ArrayList<Long>();
		List<Long> ids1 = new ArrayList<Long>();
		List<Long> paidTotalIds = new ArrayList<Long>();
		List<MunicipalBond> municipalBonds = findMunicipalBonds(statusId,
				ispaymentAgreement);
		for (MunicipalBond mb : municipalBonds) {
			ids1.add(mb.getId());
			if (!isPaidTotalCorrect(mb)) {
				paidTotalIds.add(mb.getId());
			}

			if (!isTotalDepositsCorrect(mb)) {
				ids.add(mb.getId());
			}
		}
		isMunicipalBondItemsTotalCorrect(municipalBonds, statusId,
				ispaymentAgreement);
		if (paidTotalIds.size() > 0)
			System.out
					.println("::::::::::::::::::::.paid total statusId::::::::::::::"
							+ statusId
							+ ":::::::::::::::::::::::::::::::::::::mistake in sum of paidtotal :::::::::::::::::. "
							+ paidTotalIds);
		if (ids.size() > 0)
			System.out
					.println("::::::::::::::::::::.deposits statusId::::::::::::::"
							+ statusId
							+ ":::::::::::::::::::::::::::::::::::::mistake in total deposits :::::::::::::::::. "
							+ ids);
	}

	/**
	 * Es para revisión de errores en municipalBonds por items,
	 * 
	 * @param municipalBonds
	 * @param statusId
	 * @param isPaymentAgreement
	 */
	private void isMunicipalBondItemsTotalCorrect(
			List<MunicipalBond> municipalBonds, Long statusId,
			Boolean isPaymentAgreement) {
		List<Long> ids = new ArrayList<Long>();
		BigDecimal res = BigDecimal.ZERO;
		if (!isPaymentAgreement) {
			List<EntryTotalCollected> entryTotalCollectedsItems = getTotalsByItemsByMb(statusId);
			List<EntryTotalCollected> entryTotalCollectedsInteres = getTotalsInterestByItemsByMb(statusId);
			List<EntryTotalCollected> entryTotalCollectedsTax = getTotalsTaxByItemsByMb(statusId);
			List<EntryTotalCollected> entryTotalCollectedsDisc = getTotalsDiscountByItemsByMb(statusId);
			List<EntryTotalCollected> entryTotalCollectedsSurchar = getTotalsSurchargeByItemsByMb(statusId);

			for (MunicipalBond mb : municipalBonds) {
				res = BigDecimal.ZERO;
				for (EntryTotalCollected et : entryTotalCollectedsItems) {
					if (et.getId().equals(mb.getId())) {
						res = res.add(et.getValue());
						break;
					}
				}

				for (EntryTotalCollected et : entryTotalCollectedsInteres) {
					if (et.getId().equals(mb.getId())) {
						res = res.add(et.getValue());
						break;
					}
				}

				for (EntryTotalCollected et : entryTotalCollectedsTax) {
					if (et.getId().equals(mb.getId())) {
						res = res.add(et.getValue());
						break;
					}
				}

				if (!statusId.equals(pendingStatus.getId())) {
					for (EntryTotalCollected et : entryTotalCollectedsSurchar) {
						if (et.getId().equals(mb.getId())) {
							res = res.add(et.getValue());
							break;
						}
					}

					for (EntryTotalCollected et : entryTotalCollectedsDisc) {
						if (et.getId().equals(mb.getId())) {
							res = res.subtract(et.getValue());
							break;
						}
					}
				}
				if (res.compareTo(mb.getPaidTotal()) != 0
						&& !statusId.equals(pendingStatus.getId())) {
					System.out
							.println(":::::::::::::::::::::::::.id::::::::::::::::::::::::::::::: "
									+ mb.getId());
					System.out
							.println(":::::::::::::::::::::::::.suma es::::::::::::::::::::::::::::::: "
									+ res);
					System.out
							.println(":::::::::::::::::::::::::.paidtotal es::::::::::::::::::::::::::::::: "
									+ mb.getPaidTotal());
					System.out
							.println(":::::::::::::::::::::::::.value es::::::::::::::::::::::::::::::: "
									+ mb.getValue());
					System.out
							.println(":::::::::::::::::::::::::.emisiondate es::::::::::::::::::::::::::::::: "
									+ mb.getEmisionDate());
					ids.add(mb.getId());
				}

				if (res.compareTo(mb.getValue()) != 0
						&& statusId.equals(pendingStatus.getId())) {
					System.out
							.println(":::::::::::::::::::::::::.id::::::::::::::::::::::::::::::: "
									+ mb.getId());
					System.out
							.println(":::::::::::::::::::::::::.suma es::::::::::::::::::::::::::::::: "
									+ res);
					System.out
							.println(":::::::::::::::::::::::::.paidtotal es::::::::::::::::::::::::::::::: "
									+ mb.getPaidTotal());
					System.out
							.println(":::::::::::::::::::::::::.difference es::::::::::::::::::::::::::::::: "
									+ res.subtract(mb.getPaidTotal()));
					ids.add(mb.getId());
				}
			}

		} else {
			for (MunicipalBond mb : municipalBonds) {
				res = BigDecimal.ZERO;
				for (Item et : mb.getItems()) {
					res = res.add(et.getTotal());
				}

				for (Item et : mb.getSurchargeItems()) {
					res = res.add(et.getTotal());
				}

				for (Item et : mb.getDiscountItems()) {
					res = res.subtract(et.getTotal());
				}

				if (mb.getInterest() != null)
					res = res.add(mb.getInterest());

				for (TaxItem et : mb.getTaxItems()) {
					res = res.add(et.getValue());
				}
				if (res.compareTo(mb.getPaidTotal()) != 0
						&& !statusId.equals(pendingStatus.getId())) {
					System.out
							.println(":::::::::::::::::::::::::.id::::::::::::::::::::::::::::::: "
									+ mb.getId());
					System.out
							.println(":::::::::::::::::::::::::.suma es::::::::::::::::::::::::::::::: "
									+ res);
					System.out
							.println(":::::::::::::::::::::::::.paidtotal es::::::::::::::::::::::::::::::: "
									+ mb.getPaidTotal());
					System.out
							.println(":::::::::::::::::::::::::.value es::::::::::::::::::::::::::::::: "
									+ mb.getValue());
					System.out
							.println(":::::::::::::::::::::::::.emisiondate es::::::::::::::::::::::::::::::: "
									+ mb.getEmisionDate());
					ids.add(mb.getId());
				}

				if (res.compareTo(mb.getValue()) != 0
						&& statusId.equals(pendingStatus.getId())) {
					System.out
							.println(":::::::::::::::::::::::::.id::::::::::::::::::::::::::::::: "
									+ mb.getId());
					System.out
							.println(":::::::::::::::::::::::::.suma es::::::::::::::::::::::::::::::: "
									+ res);
					System.out
							.println(":::::::::::::::::::::::::.paidtotal es::::::::::::::::::::::::::::::: "
									+ mb.getPaidTotal());
					System.out
							.println(":::::::::::::::::::::::::.difference es::::::::::::::::::::::::::::::: "
									+ res.subtract(mb.getPaidTotal()));
					ids.add(mb.getId());
				}

			}

		}

		if (ids.size() > 0) {
			System.out.println("::::::::::::::::::...son::::::::::::::::::: "
					+ ids.size());
			System.out
					.println("::::::::::::::::::::.statusId::::::::::::::"
							+ statusId
							+ " :::::::::::::::::::::::::::::::::::::mistake in items :::::::::::::::::. "
							+ ids);
		}

	}

	/**
	 * Total de intereses por MunicipalBond y status, se utiliza para revisar
	 * errores de los municipalBond
	 * 
	 * @param municipalBondStatusId
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalsInterestByItemsByMb(
			Long municipalBondStatusId) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(m.id, SUM(m.interest)) "
				+ "from MunicipalBond m "
				+ "join m.entry e "
				+ "left join e.account ac "
				+ "where m.id in "
				+ "(select distinct (m.id) from MunicipalBond m ";
		if (!municipalBondStatusId.equals(pendingStatus.getId()))
			sql = sql + "join m.deposits d ";

		sql = sql + "left join m.paymentAgreement pa " + "where ";

		if (!municipalBondStatusId.equals(pendingStatus.getId())) {
			sql = sql + "d.date Between :startDate and :endDate AND ";
		} else {
			sql = sql + "m.emisionDate Between :startDate and :endDate AND ";
		}

		sql = sql + " m.municipalBondStatus.id = :municipalBondStatusId "
				+ "AND pa is null) group by m.id";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);

		List<EntryTotalCollected> list = query.getResultList();

		return list;
	}

	/**
	 * Total de impuestos por MunicipalBond y status, se utiliza para revisar
	 * errores de los municipalBond
	 * 
	 * @param municipalBondStatusId
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalsTaxByItemsByMb(
			Long municipalBondStatusId) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(m.id, SUM(i.value)) "
				+ "from MunicipalBond m "
				+ "join m.taxItems i "
				+ "join i.tax t "
				+ "join t.taxAccount ac "
				+ "where m.id in "
				+ "(select distinct (m.id) from MunicipalBond m ";

		if (!municipalBondStatusId.equals(pendingStatus.getId()))
			sql = sql + "join m.deposits d ";
		sql = sql + "left join m.paymentAgreement pa " + "where ";
		if (!municipalBondStatusId.equals(pendingStatus.getId())) {
			sql = sql + "d.date Between :startDate and :endDate AND ";
		} else {
			sql = sql + "m.emisionDate Between :startDate and :endDate AND ";
		}

		sql = sql + " m.municipalBondStatus.id = :municipalBondStatusId "
				+ "AND pa is null) group by m.id";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);
		return query.getResultList();
	}

	/**
	 * Total de descuentos por MunicipalBond y status, se utiliza para revisar
	 * errores de los municipalBond
	 * 
	 * @param municipalBondStatusId
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalsDiscountByItemsByMb(
			Long municipalBondStatusId) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(m.id, SUM(i.total)) "
				+ "from MunicipalBond m join m.discountItems i "
				+ "join i.entry e left "
				+ "join e.account ac "
				+ "where m.id in "
				+ "(select distinct (m.id) from MunicipalBond m ";

		if (!municipalBondStatusId.equals(pendingStatus.getId()))
			sql = sql + "join m.deposits d ";
		sql = sql + "left join m.paymentAgreement pa " + "where ";
		if (!municipalBondStatusId.equals(pendingStatus.getId())) {
			sql = sql + "d.date Between :startDate and :endDate AND ";
		} else {
			sql = sql + "m.emisionDate Between :startDate and :endDate AND ";
		}

		sql = sql + " m.municipalBondStatus.id = :municipalBondStatusId "
				+ "AND pa is null) group by m.id";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);
		List<EntryTotalCollected> result = query.getResultList();
		for (EntryTotalCollected e : result) {
			e.setIsDiscount(true);
		}
		return result;
	}

	/**
	 * Total de recargos por MunicipalBond y status, se utiliza para revisar
	 * errores de los municipalBond
	 * 
	 * @param municipalBondStatusId
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalsSurchargeByItemsByMb(
			Long municipalBondStatusId) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(m.id, SUM(i.total)) "
				+ "from MunicipalBond m "
				+ "join m.surchargeItems i "
				+ "join i.entry e "
				+ "left join e.account ac "
				+ "where m.id in "
				+ "(select distinct (m.id) from MunicipalBond m ";
		if (!municipalBondStatusId.equals(pendingStatus.getId()))
			sql = sql + "join m.deposits d ";
		sql = sql + "left join m.paymentAgreement pa " + "where ";
		if (!municipalBondStatusId.equals(pendingStatus.getId())) {
			sql = sql + "d.date Between :startDate and :endDate AND ";
		} else {
			sql = sql + "m.emisionDate Between :startDate and :endDate AND ";
		}

		sql = sql + " m.municipalBondStatus.id = :municipalBondStatusId "
				+ "AND pa is null) group by m.id";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);
		return query.getResultList();
	}

	/**
	 * Total de items por MunicipalBond y status, se utiliza para revisar
	 * errores de los municipalBond
	 * 
	 * @param municipalBondStatusId
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getTotalsByItemsByMb(
			Long municipalBondStatusId) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(m.id, SUM(i.total)) "
				+ "from MunicipalBond m "
				+ "join m.items i "
				+ "join i.entry e left "
				+ "join e.account ac "
				+ "where m.id in (select distinct (m.id) from MunicipalBond m ";

		if (!municipalBondStatusId.equals(pendingStatus.getId()))
			sql = sql + "join m.deposits d ";
		sql = sql + "left join m.paymentAgreement pa " + "where ";
		if (!municipalBondStatusId.equals(pendingStatus.getId())) {
			sql = sql + "d.date Between :startDate and :endDate AND ";
		} else {
			sql = sql + "m.emisionDate Between :startDate and :endDate AND ";
		}

		sql = sql + " m.municipalBondStatus.id = :municipalBondStatusId "
				+ "AND pa is null) group by m.id";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);
		return query.getResultList();
	}

	/**
	 * Es para revisión de errores, el total de depositos igual al paidTotal
	 * 
	 * @param mb
	 * @return boolean
	 */
	private boolean isTotalDepositsCorrect(MunicipalBond mb) {
		BigDecimal res = BigDecimal.ZERO;
		for (Deposit d : mb.getDeposits()) {
			res = res.add(d.getValue());
		}
		if (res.compareTo(mb.getPaidTotal()) == 0)
			return true;
		return false;
	}

	/**
	 * Es para revisión de errores, el total de value + surcharge + interest -
	 * discounts
	 * 
	 * @param mb
	 * @return boolean
	 */
	private boolean isPaidTotalCorrect(MunicipalBond mb) {
		BigDecimal res = mb.getValue().add(mb.getSurcharge())
				.add(mb.getInterest()).add(mb.getTaxesTotal())
				.subtract(mb.getDiscount());
		if (res.compareTo(mb.getPaidTotal()) == 0)
			return true;
		return false;
	}

	/**
	 * Para reporte de saldos vigentes
	 * @throws Exception 
	 */
	public void generateCurrentBalanceReport() throws Exception {
		isCurrentBalanceReport = Boolean.TRUE;
		generateEmissionGlobalReport();
	}

	@Logger
	Log log;

	/**
	 * Genera el reporte de emisión por partidas
	 * @throws Exception 
	 */
	public void generateEmissionGlobalReport() throws Exception {

		rootNode = null;
		Calendar now = Calendar.getInstance();

		if (startDate != null && endDate != null && startDate.equals(endDate)) {
			findWorkday(startDate);
		} else {
			this.setInstance(null);
		}

		if (isClosingWorkday) {
			startDate = now.getTime();
		}

		if (endDate == null) {
			endDate = startDate;
		}
		// log.info("-------------------------1.------------------");
		statusIds = new ArrayList<Long>();
		if (municipalBondStatus != null) {
			// Cuando seleccionan un estado (RevenueReport)
			statusIds.add(municipalBondStatus.getId());
		} else {
			if (isCurrentBalanceReport) {
				// Cuando es reporte de saldos vigentes (RevenueReport)
				statusIds.add(pendingStatus.getId());
				statusIds.add(compensationMunicipalBondStatus.getId());
				statusIds.add(inPaymentAgreementStatus.getId());
				statusIds.add(blockedMunicipalBondStatus.getId());
			} else {
				// Cuando seleccionan todos (RevenueReport)
				statusIds.add(paidMunicipalBondStatus.getId());
				statusIds.add(pendingStatus.getId());
				statusIds.add(compensationMunicipalBondStatus.getId());
				statusIds.add(inPaymentAgreementStatus.getId());
				statusIds.add(externalChannelStatus.getId());
				statusIds.add(blockedMunicipalBondStatus.getId());
			}
		}
		// log.info("-------------------------2.------------------");
		entryTotalCollecteds = getTotalEmissionByEntry();
		// log.info("-------------------------2.1------------------");
		if (entry == null) {
			// log.info("-------------------------2.2------------------");
			taxesEmitted = getAllEmissionTaxesTotals();
			// taxesEmitted = new ArrayList<EntryTotalCollected>();
		} else {
			// log.info("-------------------------2.3------------------");
			taxesEmitted = new ArrayList<EntryTotalCollected>();
		}
		// log.info("-------------------------3.------------------");
		entryTotalCollecteds.addAll(taxesEmitted);
		// log.info("-------------------------4.------------------");
		orderByGroupBy(entryTotalCollecteds);

		// emisiones futuras
		// ----------------**********************-----------------------------------------------
		// log.info("-------------------------5.------------------");
		entryTotalFuture = getTotalEmittedFutureByEntryAndStatus(futureBondStatus
				.getId());
		// log.info("-------------------------6.------------------");
		if (entry == null || entry.getId().equals(futureBondStatus.getId())) {
			taxesFuture = getAllFutureTaxesTotalsByStatus(futureBondStatus
					.getId());
		} else {
			taxesFuture = new ArrayList<EntryTotalCollected>();
		}
		// log.info("-------------------------7.------------------");
		entryTotalFuture.addAll(taxesFuture);
		// log.info("-------------------------8.------------------");
		orderByGroupBy(entryTotalFuture);
		// log.info("-------------------------9.------------------");
		/* //////////// */

		// Pago anticipado
		entryTotalPrepaid = getTotalEmittedPrepaidByEntryAndStatus(explanation);
		System.out.println("Entru total prepaid antes de taxes prepaid:"
				+ entryTotalPrepaid.size());
		// log.info("-------------------------6.------------------");
		taxesPrepaid = getAllPrepaidTaxesTotalsByStatus(explanation);

		// log.info("-------------------------7.------------------");
		entryTotalPrepaid.addAll(taxesPrepaid);
		// log.info("-------------------------8.------------------");
		orderByGroupBy(entryTotalPrepaid);
		System.out.println("Entru total prepaid:" + entryTotalPrepaid.size());

		// //********fomalizacion***********////
		entryTotalFormalize = getTotalEmittedFormalizeByEntryAndStatus(explanationFormalize);
		System.out
				.println("Entru total formalize antes add de taxes formalize:"
						+ entryTotalFormalize.size());
		// log.info("-------------------------6.------------------");

		taxesFormalize = getAllFormalizeTaxesTotalsByStatus(explanationFormalize);

		// log.info("-------------------------7.------------------");
		entryTotalFormalize.addAll(taxesFormalize);
		// log.info("-------------------------8.------------------");
		orderByGroupBy(entryTotalFormalize);

		// log.info("-------------------------5.------------------");
		entryTotalCancelled = getTotalEmittedByEntryAndStatus(cancelledBondStatus
				.getId());
		// log.info("-------------------------6.------------------");
		if (entry == null) {
			taxesCancelled = getAllReversedTaxesTotalsByStatus(cancelledBondStatus
					.getId());
		} else {
			taxesCancelled = new ArrayList<EntryTotalCollected>();
		}
		// log.info("-------------------------7.------------------");
		entryTotalCancelled.addAll(taxesCancelled);
		// log.info("-------------------------8.------------------");
		orderByGroupBy(entryTotalCancelled);
		// log.info("-------------------------9.------------------");

		totalReversed = BigDecimal.ZERO;
		entryTotalReversed = getTotalEmittedByEntryAndStatus(reversedMunicipalBondStatus
				.getId());
		// log.info("-------------------------10.------------------");
		if (entry == null) {
			taxesReversed = getAllReversedTaxesTotalsByStatus(reversedMunicipalBondStatus
					.getId());
		} else {
			taxesReversed = new ArrayList<EntryTotalCollected>();
		}
		// log.info("-------------------------11.------------------");
		entryTotalReversed.addAll(taxesReversed);
		// log.info("-------------------------12.------------------");
		orderByGroupBy(entryTotalReversed);
		// log.info("-------------------------13.------------------");

		generateEmissionSummaryTree();
		// log.info("-------------------------14.------------------");

	}

	/**
	 * Total de Impuestos por municipalBondStatus
	 * 
	 * @param statusId
	 * @return
	 */
	private List<EntryTotalCollected> getAllReversedTaxesTotalsByStatus(
			Long statusId) {
		statusIds = new ArrayList<Long>();
		statusIds.add(statusId);
		Query query = query = getEntityManager().createNamedQuery(
				"MunicipalBond.SumTotalReversedBetweenDatesByTaxItem");
		query.setParameter("municipalBondStatusIds", statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getAllFutureTaxesTotalsByStatus(
			Long statusId) {
		try {
			statusIds = new ArrayList<Long>();
			statusIds.add(statusId);

			Query query = query = getEntityManager().createNamedQuery(
					"MunicipalBond.SumTotalFutureBetweenDatesByTaxItem");
			query.setParameter("municipalBondStatusId", statusId);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);

			List<EntryTotalCollected> retorno = query.getResultList();
			if (!retorno.isEmpty()) {
				for (EntryTotalCollected entryTotalCollected : retorno) {
					ParameterFutureEmissionDTO parameters = new ObjectMapper()
							.readValue(entryTotalCollected
									.getParametersFutureEmission(),
									ParameterFutureEmissionDTO.class);
					entryTotalCollected
							.setParametersFutureEmissionDTO(parameters);
				}
			}
			System.out.println("Retono Future:" + retorno);
			return retorno;

		} catch (Exception e) {
			System.out.println(e);
			return new ArrayList<EntryTotalCollected>();
		}
	}

	private List<EntryTotalCollected> getAllPrepaidTaxesTotalsByStatus(
			String explanation) {

		try {
			Query query = query = getEntityManager().createNamedQuery(
					"MunicipalBond.SumTotalPrepaidBetweenDatesByTaxItem");
			query.setParameter("explanation", explanation);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			List<EntryTotalCollected> retorno = query.getResultList();
			if (!retorno.isEmpty()) {
				for (EntryTotalCollected entryTotalCollected : retorno) {
					ParameterFutureEmissionDTO parameters = new ObjectMapper()
							.readValue(entryTotalCollected
									.getParametersFutureEmission(),
									ParameterFutureEmissionDTO.class);
					entryTotalCollected
							.setParametersFutureEmissionDTO(parameters);
				}
			}
			System.out.println("Retono Future:" + retorno);
			return retorno;

		} catch (Exception e) {
			System.out.println(e);
			return new ArrayList<EntryTotalCollected>();
		}
	}

	private List<EntryTotalCollected> getAllFormalizeTaxesTotalsByStatus(
			String explanation) {
		try {
			Query query = query = getEntityManager().createNamedQuery(
					"MunicipalBond.SumTotalPrepaidBetweenDatesByTaxItem");
			query.setParameter("explanation", explanation);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			List<EntryTotalCollected> retorno = query.getResultList();
			if (!retorno.isEmpty()) {
				for (EntryTotalCollected entryTotalCollected : retorno) {
					ParameterFutureEmissionDTO parameters = new ObjectMapper()
							.readValue(entryTotalCollected
									.getParametersFutureEmission(),
									ParameterFutureEmissionDTO.class);
					entryTotalCollected
							.setParametersFutureEmissionDTO(parameters);
				}
			}
			System.out.println("Retono Future:" + retorno);
			return retorno;
		} catch (Exception e) {
			System.out.println(e);
			return new ArrayList<EntryTotalCollected>();
		}
	}

	public void loadReversedStatus() {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		cancelledBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_VOID");
	}

	private List<EntryTotalCollected> getAllEmissionTotalsCurrentPeriod() {
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.SumTotalEmittedBetweenDatesCurrentPeriodByItem");
		query.setParameter("statusIds", statusIds);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());

		System.out
				.println("Periodo fiscal que se envia a consultar en obtener emisiones:"
						+ userSession.getFiscalPeriod().getStartDate());
		System.out.println("StatusId a enviar a consultar:" + statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getAllEmissionTotalsPreviousPeriods() {
		Query query = getEntityManager()
				.createNamedQuery(
						"MunicipalBond.SumTotalEmittedBetweenDatesPreviousPeriodsByItem");
		query.setParameter("statusIds", statusIds);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getAllEmissionTotalsFuturePeriods() {
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.SumTotalEmittedBetweenDatesFuturePeriodsByItem");
		System.out.println("Status Ids que se envian a la consulta:"
				+ statusIds);
		query.setParameter("statusId", futureBondStatus.getId());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	/**
	 * Todo lo emitido años previos y año actual
	 * 
	 * @return List<EntryTotalCollected>
	 */
	private List<EntryTotalCollected> getAllEmissionTotals() {
		System.out.println("Entra al gat All EmisionTOtals");
		List<EntryTotalCollected> currentPeriod = getAllEmissionTotalsCurrentPeriod();
		List<EntryTotalCollected> previousPeriod = getAllEmissionTotalsPreviousPeriods();
		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();

		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getValue().add(et.getPreviousYears()));
					break;
				}
			}

			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				et1.setTotal(et1.getPreviousYears());
				missing.add(et1);
			}

		}

		currentPeriod.addAll(missing);
		return currentPeriod;
	}

	/**
	 * Total impuestos emitidos años previos y año actual
	 * 
	 * @return
	 */
	private List<EntryTotalCollected> getAllEmissionTaxesTotals() {
		List<EntryTotalCollected> currentPeriod = getAllEmissionTaxesTotalsCurrentPeriod();
		List<EntryTotalCollected> previousPeriod = getAllEmissionTaxesTotalsPreviousPeriods();

		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getValue().add(et.getPreviousYears()));
					break;
				}
			}

			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				et1.setTotal(et1.getPreviousYears());
				missing.add(et1);
			}

		}

		currentPeriod.addAll(missing);

		return currentPeriod;
	}

	private List<EntryTotalCollected> getAllEmissionTaxesTotalsPreviousPeriods() {
		Query query = getEntityManager()
				.createNamedQuery(
						"MunicipalBond.SumTotalTaxesEmittedByPreviousPeriodsBetweenDatesByItem");
		query.setParameter("statusIds", statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());

		List<EntryTotalCollected> aux = query.getResultList();
		List<EntryTotalCollected> result = new ArrayList<EntryTotalCollected>();

		for (EntryTotalCollected etc : aux) {
			if (etc.getValue().doubleValue() != 0) {
				result.add(etc);
			}
		}

		return result;
	}

	private List<EntryTotalCollected> getAllEmissionTaxesTotalsFuturePeriods() {
		Query query = getEntityManager()
				.createNamedQuery(
						"MunicipalBond.SumTotalTaxesEmittedByFuturePeriodsBetweenDatesByItem");
		query.setParameter("statusId", futureBondStatus.getId());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		List<EntryTotalCollected> aux = query.getResultList();
		List<EntryTotalCollected> result = new ArrayList<EntryTotalCollected>();

		for (EntryTotalCollected etc : aux) {
			if (etc.getValue().doubleValue() != 0) {
				result.add(etc);
			}
		}

		return result;
	}

	private List<EntryTotalCollected> getAllEmissionTaxesTotalsCurrentPeriod() {
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.SumTotalTaxesEmittedByPeriodBetweenDatesByItem");
		query.setParameter("statusIds", statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		// return query.getResultList();

		List<EntryTotalCollected> aux = query.getResultList();
		List<EntryTotalCollected> result = new ArrayList<EntryTotalCollected>();

		for (EntryTotalCollected etc : aux) {
			if (etc.getValue().doubleValue() != Double.parseDouble("0")) {
				result.add(etc);
			}
		}

		return result;
	}

	private List<EntryTotalCollected> getAllReversedTotals(Long statusId) {
		statusIds = new ArrayList<Long>();
		statusIds.add(statusId);
		Query query = query = getEntityManager().createNamedQuery(
				"MunicipalBond.SumTotalReversedBetweenDatesByItem");
		query.setParameter("municipalBondStatusIds", statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getAllFutureTotals(Long statusId)
			throws Exception {

		statusIds = new ArrayList<Long>();
		statusIds.add(statusId);
		Query query = query = getEntityManager().createNamedQuery(
				"MunicipalBond.SumTotalFutureBetweenDatesByItem");
		query.setParameter("municipalBondStatusId", statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		List<EntryTotalCollected> retorno = query.getResultList();
		if (!retorno.isEmpty()) {
			for (EntryTotalCollected entryTotalCollected : retorno) {
				if (entryTotalCollected.getParametersFutureEmission() == null) {
					 throw new Exception("No existe configuracion de parametros futuros para la cuenta: "
								+ entryTotalCollected.getAccount()+" - "+entryTotalCollected.getEntry());
				} else {
					ParameterFutureEmissionDTO parameters = new ObjectMapper()
							.readValue(entryTotalCollected
									.getParametersFutureEmission(),
									ParameterFutureEmissionDTO.class);
					entryTotalCollected
							.setParametersFutureEmissionDTO(parameters);
				}

			}
		}
		System.out.println("Retono Future:" + retorno);
		return retorno;

	}

	private List<EntryTotalCollected> getAllFutureTotals(Long statusId,
			Long entryId) {
		try {
			statusIds = new ArrayList<Long>();
			statusIds.add(statusId);
			Query query = query = getEntityManager().createNamedQuery(
					"MunicipalBond.SumTotalFutureBetweenDatesByItemAndEntry");
			query.setParameter("municipalBondStatusId", statusIds);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			query.setParameter("entry_id", entryId);

			List<EntryTotalCollected> retorno = query.getResultList();
			if (!retorno.isEmpty()) {
				for (EntryTotalCollected entryTotalCollected : retorno) {
					ParameterFutureEmissionDTO parameters = new ObjectMapper()
							.readValue(entryTotalCollected
									.getParametersFutureEmission(),
									ParameterFutureEmissionDTO.class);
					entryTotalCollected
							.setParametersFutureEmissionDTO(parameters);
				}
			}
			System.out.println("Retono Future:" + retorno);
			return retorno;
		} catch (Exception e) {
			System.out.println(e);
			return new ArrayList<EntryTotalCollected>();
		}

	}

	private List<EntryTotalCollected> getAllPrepaidTotals(String explanation) {
		try {
			Query query = query = getEntityManager().createNamedQuery(
					"MunicipalBond.SumTotalPrepaidBetweenDatesByItem");
			query.setParameter("explanation", explanation);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			List<EntryTotalCollected> retorno = query.getResultList();
			if (!retorno.isEmpty()) {
				for (EntryTotalCollected entryTotalCollected : retorno) {
					ParameterFutureEmissionDTO parameters = new ObjectMapper()
							.readValue(entryTotalCollected
									.getParametersFutureEmission(),
									ParameterFutureEmissionDTO.class);
					entryTotalCollected
							.setParametersFutureEmissionDTO(parameters);
				}
			}
			System.out.println("Retono Future:" + retorno);
			return retorno;
		} catch (Exception e) {
			System.out.println(e);
			return new ArrayList<EntryTotalCollected>();
		}
	}

	private List<EntryTotalCollected> getAllFormalizeTotals(String explanation) {
		try {
			Query query = query = getEntityManager().createNamedQuery(
					"MunicipalBond.SumTotalPrepaidBetweenDatesByItem");
			query.setParameter("explanation", explanation);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			List<EntryTotalCollected> retorno = query.getResultList();
			if (!retorno.isEmpty()) {
				for (EntryTotalCollected entryTotalCollected : retorno) {
					ParameterFutureEmissionDTO parameters = new ObjectMapper()
							.readValue(entryTotalCollected
									.getParametersFutureEmission(),
									ParameterFutureEmissionDTO.class);
					entryTotalCollected
							.setParametersFutureEmissionDTO(parameters);
				}
			}
			System.out.println("Retono Future:" + retorno);
			return retorno;
		} catch (Exception e) {
			System.out.println(e);
			return new ArrayList<EntryTotalCollected>();
		}
	}

	private List<EntryTotalCollected> getAllEmissionTotalsCurrentPeriod(
			Long entryId) {
		Query query = getEntityManager()
				.createNamedQuery(
						"MunicipalBond.SumTotalEmittedByEntryBetweenDatesAndCurrrentPeriodByItem");
		query.setParameter("statusIds", statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("entryId", entryId);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getAllEmissionTotalsPreviousPeriods(
			Long entryId) {
		Query query = getEntityManager()
				.createNamedQuery(
						"MunicipalBond.SumTotalEmittedByEntryBetweenDatesAndPreviousPeriodsByItem");
		query.setParameter("statusIds", statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("emisionPeriod", userSession.getFiscalPeriod()
				.getStartDate());
		query.setParameter("entryId", entryId);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getAllEmissionTotalsByEntry(Long entryId) {
		List<EntryTotalCollected> currentPeriod = getAllEmissionTotalsCurrentPeriod(entryId);
		List<EntryTotalCollected> previousPeriod = getAllEmissionTotalsPreviousPeriods(entryId);
		List<EntryTotalCollected> missing = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected et1 : previousPeriod) {
			boolean exist = false;
			for (EntryTotalCollected et : currentPeriod) {
				if (et.getId().equals(et1.getId())) {
					exist = Boolean.TRUE;
					et.setPreviousYears(et1.getValue());
					et.setTotal(et.getValue().add(et.getPreviousYears()));
					break;
				}
			}
			if (!exist) {
				et1.setPreviousYears(et1.getValue());
				et1.setValue(BigDecimal.ZERO);
				et1.setTotal(et1.getPreviousYears());
				missing.add(et1);
			}
		}
		currentPeriod.addAll(missing);
		return currentPeriod;
	}

	private List<EntryTotalCollected> getAllReversedByEntry(Entry e,
			Long statusId) {
		statusIds = new ArrayList<Long>();
		statusIds.add(statusId);
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.SumTotalReversedByEntryBetweenDatesByItem");
		query.setParameter("municipalBondStatusIds", statusIds);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("entryId", e.getId());
		return query.getResultList();
	}

	private List<EntryTotalCollected> getTotalEmittedByEntryAndStatus(
			Long statusId) {
		List<EntryTotalCollected> totals = new ArrayList<EntryTotalCollected>();
		if (entry == null) {
			totals = getAllReversedTotals(statusId);
		} else {
			totals = getAllReversedByEntry(entry, statusId);
		}
		return totals;
	}

	private List<EntryTotalCollected> getTotalEmittedFutureByEntryAndStatus(
			Long statusId) throws Exception {
		List<EntryTotalCollected> totals = new ArrayList<EntryTotalCollected>();
		System.out.println(municipalBondStatus);
		if (entry == null) {
			if (municipalBondStatus == null) {
				totals = getAllFutureTotals(statusId);
			} else if (municipalBondStatus.getId().equals(
					futureBondStatus.getId())) {
				totals = getAllFutureTotals(statusId);
			}
			// TODO consultar emisiones futuras SumTotalFutureBetweenDatesByItem

		} else if (entry != null) {
			if (municipalBondStatus == null) {
				totals = getAllFutureTotals(statusId, entry.getId());
			} else if (municipalBondStatus.getId().equals(
					futureBondStatus.getId())) {
				totals = getAllFutureTotals(statusId, entry.getId());
			}

		}
		return totals;
	}

	private List<EntryTotalCollected> getTotalEmittedPrepaidByEntryAndStatus(
			String explanation) {
		List<EntryTotalCollected> totals = new ArrayList<EntryTotalCollected>();

		// TODO consultar emisiones futuras SumTotalFutureBetweenDatesByItem
		totals = getAllPrepaidTotals(explanation);

		return totals;
	}

	private List<EntryTotalCollected> getTotalEmittedFormalizeByEntryAndStatus(
			String explanation) {
		List<EntryTotalCollected> totals = new ArrayList<EntryTotalCollected>();

		// TODO consultar emisiones futuras SumTotalFutureBetweenDatesByItem
		totals = getAllFormalizeTotals(explanation);

		return totals;
	}

	/**
	 * Total emitido por rubro
	 * 
	 * @return
	 */
	private List<EntryTotalCollected> getTotalEmissionByEntry() {
		System.out.println("Entra al getTotalEmissionByEntry");
		List<EntryTotalCollected> totals = new ArrayList<EntryTotalCollected>();
		if (entry == null) {
			System.out.println("entry == null");
			totals = getAllEmissionTotals();
		} else {
			totals = getAllEmissionTotalsByEntry(entry.getId());
		}
		return totals;
	}

	private boolean isAChild(Account child, Account parent) {
		if (child == null)
			return false;
		Account p = child.getParent();
		if (child.equals(parent)) {
			return true;
		}
		while (p != null) {
			if (p.equals(parent)) {
				return true;
			}
			p = p.getParent();
		}
		return false;
	}

	private boolean isAChildAccount(String child, Account parent) {

		if (child.equals(parent.getAccountCode())) {
			return true;
		}

		List<Account> children = parent.getChildren();

		for (Account ac : children) {
			if (ac.getAccountCode().equals(child)) {
				return true;
			} else {
				if (isAChildAccount(child, ac))
					return true;
			}
		}

		return false;
	}

	private AccountBalance buildAccountBalance(Account ac) {
		AccountBalance accountBalance = createAccountBalance(ac);
		for (Account ac1 : ac.getChildren()) {
			accountBalance.add(buildAccountBalance(ac1));
		}
		return accountBalance;
	}

	private void addNodes(TreeNode<AccountBalance> node, Long id,
			TreeNode<AccountBalance> child) {
		node.addChild(id, child);
	}

	private void startAccounting(AccountBalance mainAccount) {
		this.rootNode = new TreeNodeImpl<AccountBalance>();
		this.rootNode.setData(mainAccount);
	}

	@BypassInterceptors
	private void loadTree() throws NamingException {
		startAccounting(mainAccountBalance);
		accountsForSummary = new ArrayList<AccountBalance>();
		buildTree(rootNode);
	}

	/**
	 * Balance para cada cuenta por item en el caso de recaudación o por entry
	 * del municipalBond en el caso de emisión
	 * 
	 * @param ab
	 */
	private void buildTotalBalance(AccountBalance ab) {
		if (isFromIncome) {
			buildBalanceByItem(ab);
		} else {
			buildEmissionBalance(ab);
		}
	}

	public String getColumnsNumber() {
		return columnsNumber;
	}

	public void setColumnsNumber(String columnsNumber) {
		this.columnsNumber = columnsNumber;
	}

	public String getColumnsSize() {
		return columnsSize;
	}

	public void setColumnsSize(String columnsSize) {
		this.columnsSize = columnsSize;
	}

	private String columnsSize;

	/**
	 * En el caso de reporte por items las columnas son 6 caso contrario son 10
	 * 
	 * @return
	 */
	private String numberOfColumnsGlobalReport() {
		String aux = "10";
		if (globalReport)
			aux = "6";
		return aux;
	}

	/**
	 * En el caso de reporte por items el tamaños de las columnas es '5 5 2 2 2
	 * 1' caso contrario son '5 5 2 2 2 1 1 1 1 1'
	 * 
	 * @return
	 */
	private String sizeOfColumnsGlobalReport() {
		String aux = "5 5 2 2 2 1 1 1 1 1";
		if (globalReport) {
			aux = "5 5 2 2 2 1";
		}

		return aux;
	}

	private boolean existsInAccountBalance(List<AccountBalance> list,
			AccountBalance ab) {
		for (AccountBalance a : list) {
			if (a.getAccount() != null
					&& ab.getAccount() != null
					&& a.getAccount().getAccountCode()
							.equals(ab.getAccount().getAccountCode()))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param _rootNode
	 */
	private void buildTree(TreeNode<AccountBalance> _rootNode) {
		AccountBalance a = (AccountBalance) _rootNode.getData();
		for (AccountBalance oe : a.getChildren()) {

			if (!globalReport && oe.getAccount() != null
					&& oe.getAccount().getIsShowSummary()) {
				buildTotalBalance(oe);
				if (oe.getTotal().compareTo(BigDecimal.ZERO) == 1) {
					if (!existsInAccountBalance(accountsForSummary, oe)) {
						accountsForSummary.add(oe);
					}
				}
			}

			if (globalReport && oe.getAccount() != null
					&& oe.getAccount().getShowSubtotal()) {
				buildTotalBalance(oe);
				if (oe.getTotal().compareTo(BigDecimal.ZERO) == 1) {
					if (!existsInAccountBalance(accountsForSummary, oe)) {
						accountsForSummary.add(oe);
					}
				}
			}

			TreeNodeImpl<AccountBalance> child = new TreeNodeImpl<AccountBalance>();
			child.setData(oe);
			buildTree(child);
			addNodes(_rootNode, idForTree, child);
			idForTree++;
		}
	}

	public TreeNode getTreeNode() throws NamingException {
		loadTree();
		return rootNode;
	}

	public AccountBalance getMainAccountBalance() {
		return mainAccountBalance;
	}

	public void setMainAccountBalance(AccountBalance mainAccountBalance) {
		this.mainAccountBalance = mainAccountBalance;
	}

	public void showAccountBalance(AccountBalance ac) {
		for (AccountBalance ac1 : ac.getChildren()) {
			showAccountBalance(ac1);
		}
	}

	public boolean isPartOfOtherAccount(AccountBalance ab, Entry e) {
		for (AccountBalance ab1 : ab.getChildren()) {
			if (ab1.getAccount().getAccountCode().equals(e.getCode()))
				return true;
		}
		return false;
	}

	public List<ReplacementAccountDTO> getReplacementAccountDTOs() {
		return replacementAccountDTOs;
	}

	public void setReplacementAccountDTOs(List<ReplacementAccountDTO> replacementAccountDTOs) {
		this.replacementAccountDTOs = replacementAccountDTOs;
	}

	public String getFontStyle(int parentsNumber) {
		int comp = parentsNumber;
		while (comp > 4) {
			comp = comp - 4;
		}
		if (parentsNumber == 0)
			return "bold";
		if (parentsNumber == 1)
			return "italic,oblique";
		if (parentsNumber == 2)
			return "normal,bold";
		if (parentsNumber == 3)
			return "normal,oblique";
		if (parentsNumber == 4)
			return "bold,oblique,italic";
		return "normal";
	}

	public String getIdentationWihtSpecificAccount(String name, Account child,
			Account parent) {
		String aux = "  ";
		for (int i = 0; i < parentsNumber(child, parent); i++) {
			name = aux + name;
		}
		return name;
	}

	public int parentsNumber(Account child, Account parent) {
		int n = 1;
		Account parent1 = child.getParent();
		if (parent1.equals(parent)) {
			return 1;
		}
		while (parent1 != null) {
			n++;
			parent1 = parent1.getParent();
			if (parent1.equals(parent)) {
				return n;
			}
		}
		return n;
	}

	public void buildEmissionBalance(AccountBalance ac) {
		if (ac.getAccount().getId() != null) {
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal value = BigDecimal.ZERO;
			BigDecimal previousYears = BigDecimal.ZERO;

			if (entryTotalCollecteds != null) {
				for (EntryTotalCollected ec : entryTotalCollecteds) {
					if (ec.getAccount() != null
							&& isAChildAccount(ec.getAccount(), ac.getAccount())) {
						total = total.add(ec.getTotal());
						value = value.add(ec.getValue());
						if (ec.getPreviousYears() != null)
							previousYears = previousYears.add(ec
									.getPreviousYears());
					}
				}
			}

			ac.setTotal(total);
			ac.setValue(value);
			ac.setPreviousYears(previousYears);
		}
	}

	public List<Entry> findAllEntries(Account ac) {
		List<Entry> entries = new ArrayList<Entry>();
		entries.addAll(ac.getEntries());
		for (Account ac1 : ac.getChildren()) {
			entries.addAll(findAllEntries(ac1));
		}
		return entries;
	}

	private List<MunicipalBond> findMunicipalBondFromDeposits(
			List<Deposit> deposits) {
		List<MunicipalBond> list = new ArrayList<MunicipalBond>();
		for (Deposit d : deposits) {
			if (!list.contains(d.getMunicipalBond()))
				list.add(d.getMunicipalBond());
		}
		return list;
	}

	public List<Item> findItemFromDeposits() {
		List<Item> result = new ArrayList<Item>();
		for (MunicipalBond m : municipalBonds) {
			if (m != null)
				result.addAll(m.getItems());
		}
		return result;
	}

	public List<TaxItem> findTaxItemFromDeposits() {
		List<TaxItem> result = new ArrayList<TaxItem>();
		for (MunicipalBond m : municipalBonds) {
			if (m != null)
				result.addAll(m.getTaxItems());
		}
		return result;
	}

	public List<Item> findDiscountItemFromDeposits() {
		List<Item> result = new ArrayList<Item>();
		for (MunicipalBond m : municipalBonds) {
			if (m != null)
				result.addAll(m.getDiscountItems());
		}
		return result;
	}

	public List<Item> findSurchargeItemFromDeposits() {
		List<Item> result = new ArrayList<Item>();
		for (MunicipalBond m : municipalBonds) {
			if (m != null)
				result.addAll(m.getSurchargeItems());
		}
		return result;
	}

	private List<Long> findChildren(Long id) {
		Query query = getEntityManager()
				.createNativeQuery(
						"WITH RECURSIVE recursetree(id, parent_id) AS ("
								+ "SELECT id, parent_id FROM account WHERE parent_id = :id "
								+ "UNION "
								+ "SELECT t.id, t.parent_id "
								+ "FROM account t "
								+ "JOIN recursetree rt ON rt.id = t.parent_id) "
								+ "SELECT id FROM recursetree order by id;");
		query.setParameter("id", id);
		return query.getResultList();
	}

	public void buildBalanceByItem(AccountBalance ac) {
		List<Long> ids = findChildren(ac.getAccount().getId());
		ids.add(0, ac.getAccount().getId());

		if (ac.getAccount().getId() != null) {
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal value = BigDecimal.ZERO;
			BigDecimal discount = BigDecimal.ZERO;
			BigDecimal surcharge = BigDecimal.ZERO;
			BigDecimal interest = BigDecimal.ZERO;
			BigDecimal taxes = BigDecimal.ZERO;
			BigDecimal previousYears = BigDecimal.ZERO;
			BigDecimal nextYears = BigDecimal.ZERO;

			for (EntryTotalCollected et : entryTotalCollectedsForTree) {
				if (ids.contains(et.getId())) {
					total = total.add(et.getTotal());
					value = value.add(et.getValue());
					if (et.getPreviousYears() == null)
						et.setPreviousYears(BigDecimal.ZERO);
					previousYears = previousYears.add(et.getPreviousYears());
				}
			}

			if (entryTotalDiscount != null) {
				for (EntryTotalCollected et : entryTotalDiscount) {
					if (ids.contains(et.getId())) {
						total = total.add(et.getTotal());
						value = value.add(et.getValue());
						if (et.getPreviousYears() == null)
							et.setPreviousYears(BigDecimal.ZERO);
						previousYears = previousYears
								.add(et.getPreviousYears());
					}
				}
			}
			ac.setTotal(total);
			ac.setNextYears(nextYears);
			ac.setPreviousYears(previousYears);
			ac.setValue(value);
			ac.setDiscount(discount);
			ac.setSurcharge(surcharge);
			ac.setInterest(interest);
			ac.setTaxes(taxes);

		}
	}

	public boolean existInDiscount(String accountCode) {
		if (entryTotalDiscount != null) {
			for (EntryTotalCollected et : entryTotalDiscount) {
				if (et.getGroupBy().equals(accountCode))
					return true;
			}
		}

		return false;
	}

	public boolean existInDiscountByCompensation(String accountCode) {
		for (EntryTotalCollected et : entryTotalDiscountByCompensation) {
			if (et.getGroupBy().equals(accountCode))
				return true;
		}
		return false;
	}

	public AccountBalance createAccountBalance(Account a) {
		AccountBalance ab = new AccountBalance();
		ab.setAccount(a);
		return ab;
	}

	public Account parentForSummary(Account ac) {
		Account res = null;
		Account parent = ac.getParent();
		while (parent != null) {
			if (globalReport) {
				if (parent.getShowSubtotal()) {
					res = parent;
				}
				parent = parent.getParent();
			} else {
				if (parent.getIsShowSummary()) {
					res = parent;
				}
				parent = parent.getParent();
			}

		}

		if (res != null)
			return res;

		if (globalReport && ac.getShowSubtotal()) {
			return ac;
		} else {
			if (!globalReport && ac.getIsShowSummary()) {
				return ac;
			}
		}

		return res;
	}

	public List<Account> findAccountsByItemDeposits() {
		List<Account> accounts = new ArrayList<Account>();
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SystemParameterService.LOCAL_NAME);
		if (paidMunicipalBondStatus == null)
			paidMunicipalBondStatus = systemParameterService.materialize(
					MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");

		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.findAccountsByDepositsAndItem");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId",
				paidMunicipalBondStatus.getId());
		accounts = query.getResultList();

		return accounts;
	}

	public List<Account> findAccountsBySurchargeItemDeposits() {
		List<Account> accounts = new ArrayList<Account>();

		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.findAccountsByDepositsAndSurchargeItem");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId",
				paidMunicipalBondStatus.getId());
		accounts = query.getResultList();

		return accounts;
	}

	public List<Account> findAccountsByDiscountItemDeposits() {
		List<Account> accounts = new ArrayList<Account>();
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.findAccountsByDepositsAndDiscountItem");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId",
				paidMunicipalBondStatus.getId());
		accounts = query.getResultList();
		return accounts;
	}

	public List<Account> findAccountsByTaxItemDeposits() {
		List<Account> accounts = new ArrayList<Account>();
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.findAccountsByDepositsAndTaxItem");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId",
				paidMunicipalBondStatus.getId());
		return query.getResultList();
	}

	public List<Account> findAccountsByDeposits() {
		List<Account> accounts = findAccountsByItemDeposits();
		accounts.addAll(findAccountsByTaxItemDeposits());
		accounts.addAll(findAccountsBySurchargeItemDeposits());
		accounts.addAll(findAccountsByDiscountItemDeposits());
		return accounts;
	}

	public void findAccounts() {
		parentAccounts = new ArrayList<Account>();
		List<Account> accounts = findAccountsByDeposits();

		for (Account ac : accounts) {
			if (!parentAccounts.contains(ac)) {
				Account ac1 = parentForSummary(ac);
				if (ac1 != null && !parentAccounts.contains(ac1)) {
					parentAccounts.add(ac1);
				}
			}
		}
		orderByAccountCode(parentAccounts);
	}

	public boolean isFirstLevel(Account a) {
		if (a == null)
			return true;
		if (a.getParent() == null) {
			return false;
		}

		if (a.getParent().getParent() == null) {
			return true;
		}
		return false;
	}

	public List<Account> findParents(Account a) {
		List<Account> accounts = new ArrayList<Account>();
		Account parentAccount = a.getParent();
		while (parentAccount != null) {
			accounts.add(parentAccount);
			parentAccount = parentAccount.getParent();
		}
		return accounts;
	}

	public List<Deposit> getDepositsFromPayments(List<Payment> payments) {
		List<Deposit> deposits = new ArrayList<Deposit>();
		List<Long> paymentsIds = new ArrayList<Long>();
		for (Payment p : payments) {
			paymentsIds.add(p.getId());
		}

		if (paymentsIds.size() > 0) {
			Query query = getEntityManager().createNamedQuery(
					"Deposit.findDepositsFromPayments");
			query.setParameter("paymentsIds", paymentsIds);
			deposits = query.getResultList();
		}

		return deposits;
	}

	public void loadDefaultDates() {
		if (isFirstTime) {
			initializeSystemParameterService();
			loadMunicipalBondStatus();
			loadDates();
			groupBy = "ac.accountCode";
			loadCharge();
			isFirstTime = false;
			explanation = systemParameterService
					.findParameter("STATUS_CHANGE_FUTURE_EMISSION_EXPLANATION");

			explanationFormalize = systemParameterService
					.findParameter("STATUS_CHANGE_FOMALIZE_EMISSION_EXPLANATION");
		}
	}

	private void loadInterestAccount() {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		interestAccount = systemParameterService.materialize(Account.class,
				"INTEREST_ACCOUNT_ID");
		interestAccountEmaalep = systemParameterService.materialize(
				Account.class, "INTEREST_ACCOUNT_ID_EMAALEP");
	}

	public String confirmPrinting() {
		return "printed";
	}

	public MunicipalBondStatus getBlockedMunicipalBondStatus() {
		return blockedMunicipalBondStatus;
	}

	public void setBlockedMunicipalBondStatus(
			MunicipalBondStatus blockedMunicipalBondStatus) {
		this.blockedMunicipalBondStatus = blockedMunicipalBondStatus;
	}

	private void loadMunicipalBondStatus() {
		if (pendingStatus != null)
			return;
		pendingStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");
		paidMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");
		compensationMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
		inPaymentAgreementStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
		externalChannelStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL");
		blockedMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_BLOCKED");
		reversedMunicipalBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_REVERSED");
		futureBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_FUTURE_ISSUANCE");
	}

	public void loadDates() {
		if (isFirstTime) {
			Calendar c = Calendar.getInstance();
			startDate = c.getTime();
			endDate = c.getTime();
			isFirstTime = false;
			loadReversedStatus();
		}

	}

	public MunicipalBondStatus getPendingStatus() {
		return pendingStatus;
	}

	public void setPendingStatus(MunicipalBondStatus pendingStatus) {
		this.pendingStatus = pendingStatus;
	}

	public void detailByCash() {
		setPaymentsForDetail(paymentsFractionForCashDetail);
	}

	public void printDetail(boolean showDeatil) {
		this.showDetail = showDeatil;
	}

	public void printEmisionDetail(boolean showDeatil) {
		this.showDetail = showDeatil;
		buildMainAccounts();
	}

	public void detailByCreditCard() {
		setPaymentsForDetail(paymentsFractionForCardDetail);
	}

	public void detailByCheck() {
		setPaymentsForDetail(paymentsFractionForCheckDetail);
	}

	public void setPaymentsForDetail(List<PaymentFraction> paymentsForDetail) {
		this.paymentsForDetail = paymentsForDetail;
	}

	private List<PaymentFraction> paymentsForDetail;

	public List<PaymentFraction> getPaymentsForDetail() {
		return paymentsForDetail;
	}

	public BigDecimal getTotalCashCollected() {
		return totalCashCollected;
	}

	public void setTotalCashCollected(BigDecimal totalCashCollected) {
		this.totalCashCollected = totalCashCollected;
	}

	public BigDecimal getTotalCreditCardCollected() {
		return totalCreditCardCollected;
	}

	public void setTotalCreditCardCollected(BigDecimal totalCreditCardCollected) {
		this.totalCreditCardCollected = totalCreditCardCollected;
	}

	public BigDecimal getTotalCheckCollected() {
		return totalCheckCollected;
	}

	public void setTotalCheckCollected(BigDecimal totalCheckCollected) {
		this.totalCheckCollected = totalCheckCollected;
	}

	public BigDecimal getTotalCreditNoteCollected() {
		return totalCreditNoteCollected;
	}

	public void setTotalCreditNoteCollected(BigDecimal totalCreditNoteCollected) {
		this.totalCreditNoteCollected = totalCreditNoteCollected;
	}

	public Payment createPayment(Payment p) {
		Payment payment = new Payment();
		payment.setId(p.getId());
		payment.setCashier(p.getCashier());
		payment.setDate(p.getDate());
		payment.setPaymentFractions(p.getPaymentFractions());
		payment.setStatus(p.getStatus());
		payment.setTime(p.getTime());
		payment.setValue(p.getValue());
		payment.setDeposits(createDeposits(p));
		return payment;
	}

	public List<Deposit> createDeposits(Payment p) {
		List<Deposit> deposits = new ArrayList<Deposit>();
		for (Deposit d : p.getDeposits()) {
			deposits.add(createDeposit(d));
		}
		return deposits;
	}

	public Deposit createDeposit(Deposit d) {
		Deposit dp = new Deposit();
		dp.setId(d.getId());
		dp.setBalance(d.getBalance());
		dp.setValue(d.getValue());
		dp.setCapital(d.getCapital());
		dp.setConcept(d.getConcept());
		dp.setDate(d.getDate());
		dp.setHasConflict(d.getHasConflict());
		dp.setInterest(d.getInterest());
		dp.setIsPrinted(d.getIsPrinted());
		dp.setIsSelected(d.getIsSelected());
		dp.setMunicipalBond(d.getMunicipalBond());
		dp.setNumber(d.getNumber());
		dp.setPaidTaxes(d.getPaidTaxes());
		dp.setPayment(d.getPayment());
		dp.setReversedMunicipalBond(d.getReversedMunicipalBond());
		dp.setReversedPayment(d.getReversedPayment());
		dp.setStatus(d.getStatus());
		dp.setTime(d.getTime());
		return dp;
	}

	public PaymentFraction createPaymentFraction(PaymentFraction p,
			BigDecimal value) {
		PaymentFraction paymentFraction = new PaymentFraction();
		paymentFraction.setPayment(createPayment(p.getPayment()));
		paymentFraction.setPaymentType(p.getPaymentType());
		paymentFraction.setAccountNumber(p.getAccountNumber());
		paymentFraction.setDocumentNumber(p.getDocumentNumber());
		paymentFraction.setFinantialInstitution(p.getFinantialInstitution());
		paymentFraction.setReceivedAmount(value);
		BigDecimal temp = paymentFraction.getReceivedAmount();

		for (Deposit d : paymentFraction.getPayment().getDeposits()) {
			if (!readyDeposits.contains(d)) {
				readyDeposits.add(d);
			} else {
				break;
			}
			if (temp.compareTo(BigDecimal.ZERO) == 0)
				break;

			if (existsPartialDeposit(d)) {
				BigDecimal res = getPartialTotalDeposit(d);
				d.setValue(d.getValue().subtract(res));
				if (d.getValue().compareTo(BigDecimal.ZERO) == 0)
					break;
				if (d.getValue().compareTo(temp) == 0
						|| d.getValue().compareTo(temp) == -1) {
					paymentFraction.add(d);
					completeDeposits.add(d);
					temp = temp.subtract(d.getValue());
				} else if (d.getValue().compareTo(temp) == 1) {
					d.setValue(temp);
					partialDeposits.add(d);
					temp = BigDecimal.ZERO;
					paymentFraction.add(d);
				}
			}

			if (temp.compareTo(BigDecimal.ZERO) == 0)
				break;

			if (!existsCompleteDeposit(d) && !existsPartialDeposit(d)) {
				if (d.getValue().compareTo(temp) == 0
						|| d.getValue().compareTo(temp) == -1) {
					paymentFraction.add(d);
					completeDeposits.add(d);
					temp = temp.subtract(d.getValue());
				} else if (d.getValue().compareTo(temp) == 1) {
					d.setValue(temp);
					paymentFraction.add(d);
					partialDeposits.add(d);
					temp = BigDecimal.ZERO;
				}
			}
		}
		return paymentFraction;
	}

	public boolean existsCompleteDeposit(Deposit d) {
		for (Deposit dp : completeDeposits) {
			if (dp.getId() == d.getId()) {
				return true;
			}
		}
		return false;
	}

	public boolean existsPartialDeposit(Deposit d) {
		for (Deposit dp : partialDeposits) {
			if (dp.getId() == d.getId()) {
				return true;
			}
		}
		return false;
	}

	public BigDecimal getPartialTotalDeposit(Deposit d) {
		Deposit aux = null;
		BigDecimal total = BigDecimal.ZERO;
		int x = 0;
		for (Deposit dp : partialDeposits) {
			if (dp.getId() == d.getId()) {
				x++;
				total = total.add(dp.getValue());
				aux = dp;
			}
		}
		return total;
	}

	public void totalDetailCollected() {
		paymentsFractionForCheckDetail = new ArrayList<PaymentFraction>();
		paymentsFractionForCashDetail = new ArrayList<PaymentFraction>();
		paymentsFractionForCardDetail = new ArrayList<PaymentFraction>();
		paymentsFractionForCreditNoteDetail = new ArrayList<PaymentFraction>();
		totalCreditNoteCollected = BigDecimal.ZERO;
		totalCreditCardCollected = BigDecimal.ZERO;
		totalCheckCollected = BigDecimal.ZERO;
		totalCashCollected = BigDecimal.ZERO;

		for (Payment p : payments) {
			BigDecimal total = p.getValue();
			List<PaymentFraction> fractions = orderPaymentFractionsByPaymentType(p);
			for (PaymentFraction pf : fractions) {
				BigDecimal cancelled = BigDecimal.ZERO;
				if (pf.getReceivedAmount().compareTo(total) == 0
						|| pf.getReceivedAmount().compareTo(total) == -1) {
					cancelled = pf.getReceivedAmount();
				} else {
					cancelled = total;
				}
				PaymentFraction pfAux = createPaymentFraction(pf, cancelled);
				if (pfAux.getPaymentType() == PaymentType.CHECK) {
					paymentsFractionForCheckDetail.add(pfAux);
					totalCheckCollected = totalCheckCollected.add(cancelled);
				}
				if (pfAux.getPaymentType() == PaymentType.CASH) {
					paymentsFractionForCashDetail.add(pfAux);
					totalCashCollected = totalCashCollected.add(cancelled);
				}

				if (pfAux.getPaymentType() == PaymentType.CREDIT_CARD) {
					paymentsFractionForCardDetail.add(pfAux);
					totalCreditCardCollected = totalCreditCardCollected
							.add(cancelled);
				}

				if (pfAux.getPaymentType() == PaymentType.CREDIT_NOTE) {
					paymentsFractionForCreditNoteDetail.add(pfAux);
					totalCreditNoteCollected = totalCreditNoteCollected
							.add(cancelled);
				}

				total = total.subtract(cancelled);
			}
		}
		paymentsFractionForCashDetail = orderPaymentsFractionForResident(paymentsFractionForCashDetail);
		paymentsFractionForCheckDetail = orderPaymentsFractionForResident(paymentsFractionForCheckDetail);
		paymentsFractionForCardDetail = orderPaymentsFractionForResident(paymentsFractionForCardDetail);
		paymentsFractionForCreditNoteDetail = orderPaymentsFractionForResident(paymentsFractionForCreditNoteDetail);

	}

	public List<PaymentFraction> orderPaymentsFractionForResident(
			List<PaymentFraction> list) {
		List<String> names = new ArrayList<String>();
		List<PaymentFraction> res = new ArrayList<PaymentFraction>();
		for (PaymentFraction pf : list) {
			names.add(pf.getPayment().getDeposits().get(0).getMunicipalBond()
					.getResident().getName());
		}
		Collections.sort(names);
		for (String n : names) {
			for (PaymentFraction pf : list) {
				if (pf.getPayment().getDeposits().get(0).getMunicipalBond()
						.getResident().getName().equals(n)
						&& !res.contains(pf)) {
					res.add(pf);
				}
			}
		}
		return res;
	}

	public List<Payment> getPaymentsByBranchAndDate(Long branchId, Date d) {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		Long municipalBondStatusId = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
		Query query = getEntityManager().createNamedQuery(
				"Payment.findByBranchAndDate");
		query.setParameter("date", d);
		query.setParameter("branchId", branchId);
		query.setParameter("statusId", municipalBondStatusId);
		return query.getResultList();
	}

	public void generateCompensationPaymentReport() {
		setReadyForPrint(false);
		payments = getPaymentsByBranchAndDate(branch.getId(), startDate);
		filterPayments();
		deposits = getDepositsFromPayments(payments);
		totalDetailCollected();
	}

	public void mergeEmissions(List<ReportView> emitted,
			List<ReportView> nullified) {
		for (ReportView rv : emitted) {
			rv.setTotal(rv.getTotalEmitted());
		}
		for (ReportView rv : emitted) {
			for (ReportView rv1 : nullified) {
				if (rv.getPersonId().equals(rv1.getPersonId())) {
					rv.setTotalReversed(rv1.getTotalCorrect());
					rv.setTotalValueReversed(rv1.getTotalEmitted());
					rv.setTotal(rv.getTotalEmitted().subtract(
							rv.getTotalValueReversed()));
				}
			}
		}
	}

	private List<MunicipalBond> reversedBonds;

	private List<MunicipalBond> getReversedMunicipalBondsBetweenDates() {
		loadMunicipalBondStatus();
		String namedQuery = "";
		if (person == null) {
			namedQuery = "StatusChange.findBondsByStatusAndDate";
		} else {
			namedQuery = "StatusChange.findBondsByStatusAndDateAndUser";
		}

		Query query = getEntityManager().createNamedQuery(namedQuery);

		if (person != null)
			query.setParameter("userId", person.getUser().getId());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("statusId", reversedMunicipalBondStatus.getId());

		return query.getResultList();
	}

	public void generateEmittersReport() {
		if (!hasRoleNameRevenueBoss())
			person = userSession.getPerson();
		totalEmitteds = getMunicipalBondsTotalEmittedBetweenDates();
		totalNullified = getMunicipalBondsTotalNullifiedBetweenDates();
		mergeEmissions(totalEmitteds, totalNullified);
		municipalBonds = getMunicipalBondsEmittedBetweenDates();
		try {
			municipalBondItems = MunicipalBondUtil
					.fillMunicipalBondItems(municipalBonds);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reportTotalEmitted();
		sumTotalMunicipalBondEmitted();
		sumTotalMunicipalBondNullified();
		sumTotalNullified();
		sumRealTotalEmitted();

		reversedBonds = getReversedMunicipalBondsBetweenDates();
		totalReversed = sumPaidTotal(reversedBonds);
	}

	private BigDecimal sumPaidTotal(List<MunicipalBond> bonds) {
		BigDecimal total = BigDecimal.ZERO;
		for (MunicipalBond mb : bonds) {
			total = total.add(mb.getPaidTotal());
		}
		return total;
	}

	public List<ReportView> getMunicipalBondsTotalNullifiedBetweenDates() {
		String namedQuery = "";
		if (person == null) {
			namedQuery = "MunicipalBond.SumTotalNullified";
		} else {
			namedQuery = "MunicipalBond.SumTotalNullifiedByPerson";
		}

		Query query = getEntityManager().createNamedQuery(namedQuery);

		if (person != null)
			query.setParameter("personId", person.getId());
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("statusId", cancelledBondStatus.getId());
		return query.getResultList();
	}

	public void generateCashiersReport() {
		totalByCashier = getTotalTransactionsBetweenDates();
		if (person != null)
			findDetailedDepositsByCashier();
	}

	public List<ReportView> getTotalTransactionsBetweenDates() {
		List<ReportView> totalReversed = new ArrayList<ReportView>();

		List<ReportView> ready = new ArrayList<ReportView>();

		if (person == null) {
			ready = getTotalCorrectTransactionsBetweenDates(null);
			totalReversed = getTotalReversedTransactionsBetweenDates(null);
		} else {
			ready = getTotalCorrectTransactionsBetweenDates(person.getId());
			totalReversed = getTotalReversedTransactionsBetweenDates(person
					.getId());
		}

		for (ReportView r : ready) {
			if (totalReversed.size() == 0)
				r.calculateEfficiency();
			for (ReportView rv : totalReversed) {
				if (r.getPersonId().equals(rv.getPersonId())) {
					r.setTotalReversed(rv.getTotalCorrect());
					r.calculateEfficiency();
				}
			}
		}

		for (ReportView rv : findMissing(ready, totalReversed)) {
			rv.setTotalReversed(rv.getTotalCorrect());
			rv.setTotalCorrect(0L);
			rv.calculateEfficiency();
			ready.add(rv);
		}

		return ready;
	}

	private List<ReportView> findMissing(List<ReportView> correct,
			List<ReportView> reversed) {
		List<ReportView> list = new ArrayList<ReportView>();

		for (ReportView rv : reversed) {
			boolean aux = false;
			for (ReportView r : correct) {
				if (r.getPersonId().equals(rv.getPersonId())) {
					aux = true;
				}
			}
			if (!aux)
				list.add(rv);
		}
		return list;
	}

	public List<ReportView> getTotalReversedTransactionsBetweenDates(
			Long cashierId) {
		String namedQuery = "";
		if (cashierId == null) {
			namedQuery = "Deposit.reversedDepositNumberBetweenDates";
		} else {
			namedQuery = "Deposit.reversedDepositNumberByCashierBetweenDates";
		}

		Query query = getEntityManager().createNamedQuery(namedQuery);

		if (cashierId != null)
			query.setParameter("cashierId", cashierId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	public List<ReportView> getTotalCorrectTransactionsBetweenDates(
			Long cashierId) {
		String namedQuery = "";
		if (cashierId == null) {
			namedQuery = "Deposit.correctDepositNumberBetweenDates";
		} else {
			namedQuery = "Deposit.correctDepositNumberByCashierBetweenDates";
		}

		Query query = getEntityManager().createNamedQuery(namedQuery);

		if (cashierId != null)
			query.setParameter("cashierId", cashierId);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	public TillPermissionDetail createTillPermissionDetail(TillPermission t) {
		TillPermissionDetail td = new TillPermissionDetail();
		td.setDate(t.getWorkday().getDate());
		td.setInitialBalance(t.getInitialBalance());
		td.setInChargeName(t.getPerson().getName());
		td.setOpened(t.getClosingTime() == null ? true : false);
		return td;
	}

	private List<EntryTotalCollected> findTotalCreditNoteBetweenDatesByCreditNoteType(
			Date start, Date end) {
		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected (cnt.name ,sum(pf.paidAmount)) from Payment payment "
				+ "join payment.paymentFractions pf "
				+ "left join pf.creditNote cn "
				+ "left join cn.creditNoteType cnt "
				+ "where payment.date BETWEEN :startDate AND :endDate "
				+ "and cn is not null "
				+ "and payment.status = 'VALID' "
				+ "GROUP BY cnt";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	private BigDecimal findTotal(List<EntryTotalCollected> list) {
		BigDecimal aux = BigDecimal.ZERO;
		for (EntryTotalCollected e : list) {
			aux = aux.add(e.getTotal());
		}
		return aux;
	}

	@SuppressWarnings("el-unresolved")
	public List<EntryTotalCollected> getSummaryTotalCollected() {
		List<EntryTotalCollected> totalCollecteds = new ArrayList<EntryTotalCollected>();

		EntryTotalCollected entryTotalCollected = new EntryTotalCollected();
		String message = Interpolator.instance().interpolate(
				"#{messages['workday.cashCollected']}", new Object[0]);
		entryTotalCollected.setGroupBy(message);
		entryTotalCollected.setTotal(sumTotalCashCollected());

		List<EntryTotalCollected> creditNoteList = findTotalCreditNoteBetweenDatesByCreditNoteType(
				startDate, endDate);
		totalCollecteds.addAll(creditNoteList);

		entryTotalCollected.setTotal(entryTotalCollected.getTotal().subtract(
				findTotal(creditNoteList)));

		totalCollecteds.add(0, entryTotalCollected);

		if (globalReport) {
			EntryTotalCollected entryTotalCollected4 = new EntryTotalCollected();
			message = Interpolator.instance()
					.interpolate("#{messages['item.totalDiscountCollected']}",
							new Object[0]);
			entryTotalCollected4.setGroupBy(message);
			entryTotalCollected4.setTotal(discountsValueEffective
					.multiply(new BigDecimal(-1)));
			totalCollecteds.add(entryTotalCollected4);
		}

		EntryTotalCollected entryTotalCollected1 = new EntryTotalCollected();
		message = Interpolator.instance().interpolate(
				"#{messages['workday.inPaymentAgreement']}", new Object[0]);
		entryTotalCollected1.setGroupBy(message);
		entryTotalCollected1.setTotal(totalInAgreement());

		totalCollecteds.add(entryTotalCollected1);

		EntryTotalCollected entryTotalCollected2 = new EntryTotalCollected();
		message = Interpolator.instance().interpolate(
				"#{messages['workday.pendingPaid']}", new Object[0]);
		entryTotalCollected2.setGroupBy(message);
		entryTotalCollected2.setTotal(totalCompensationGenerate());
		totalCollecteds.add(entryTotalCollected2);

		if (globalReport) {
			EntryTotalCollected entryTotalCollected5 = new EntryTotalCollected();
			message = Interpolator.instance().interpolate(
					"#{messages['item.totalDiscountCollectedCompensation']}",
					new Object[0]);
			entryTotalCollected5.setGroupBy(message);
			entryTotalCollected5.setTotal(discountsValueByCompensation
					.multiply(new BigDecimal(-1)));
			totalCollecteds.add(entryTotalCollected5);
		}

		return totalCollecteds;
	}

	public List<EntryTotalCollected> getTotals(boolean isPaidCompensation) {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		Long municipalBondId = null;

		if (isPaidCompensation) {
			municipalBondId = systemParameterService
					.findParameter("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
		} else {
			municipalBondId = systemParameterService
					.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID");
		}

		if (groupBy == null)
			groupBy = "ac.accountCode";

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,count(m.id), e.name,"
				+ groupBy
				+ ", SUM(m.value), SUM(m.discount), SUM(m.surcharge),"
				+ " SUM(m.interest), SUM(m.taxesTotal), SUM (m.paidTotal)) "
				+ "from MunicipalBond m join m.entry e left join e.account ac where m.id in "
				+ "(select distinct (m.id) from MunicipalBond m "
				+ "join m.deposits d "
				+ "left join m.paymentAgreement pa "
				+ "where d.date Between :startDate and :endDate "
				+ "AND m.municipalBondStatus.id = :municipalBondStatusId "
				+ "AND pa is null) "
				+ "GROUP BY e.id, e.name, "
				+ groupBy
				+ " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondId);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getTotalsByItems(
			Long municipalBondStatusId) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name,"
				+ groupBy
				+ ", SUM(i.total)) "
				+ "from MunicipalBond m join m.items i join i.entry e left join e.account ac where m.id in (select distinct (m.id) from Payment payment join payment.deposits d join d.municipalBond m where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId)"
				+ " GROUP BY e.id, e.name, " + groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getTotalsDiscountByItems(
			Long municipalBondStatusId) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name,"
				+ groupBy
				+ ", SUM(i.total)) "
				+ "from MunicipalBond m join m.discountItems i join i.entry e left join e.account ac where m.id in (select distinct (m.id) from Payment payment join payment.deposits d join d.municipalBond m where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId)"
				+ " GROUP BY e.id, e.name, " + groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);
		List<EntryTotalCollected> result = query.getResultList();
		for (EntryTotalCollected e : result) {
			e.setIsDiscount(true);
		}
		return result;
	}

	private List<EntryTotalCollected> getTotalsTaxByItems(
			Long municipalBondStatusId) {

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(ac.id, ac.accountName, ac.accountCode, SUM(i.value)) "
				+ "from MunicipalBond m join m.taxItems i join i.tax t join t.taxAccount ac "
				+ "WHERE m.id in (select distinct (m.id) from Payment payment join payment.deposits d join d.municipalBond m where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId) "
				+ "GROUP BY ac.id, ac.accountName ORDER BY ac.id";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);
		return query.getResultList();
	}

	private List<EntryTotalCollected> getTotalsSurchargeByItems(
			Long municipalBondStatusId) {
		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id, e.name,"
				+ groupBy
				+ ", SUM(i.total)) "
				+ "from MunicipalBond m join m.surchargeItems i join i.entry e left join e.account ac where m.id in (select distinct (m.id) from Payment payment join payment.deposits d join d.municipalBond m where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId)"
				+ " GROUP BY e.id, e.name, " + groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);

		return query.getResultList();

	}

	private void deleteEmptyEntryTotalCollected(List<EntryTotalCollected> list) {
		List<EntryTotalCollected> remove = new ArrayList<EntryTotalCollected>();
		for (EntryTotalCollected e : list) {
			if (e.getTotal() == null
					|| e.getTotal().compareTo(BigDecimal.ZERO) == 0) {
				remove.add(e);
			}
		}
		list.removeAll(remove);

	}

	@SuppressWarnings("unchecked")
	private List<EntryTotalCollected> getTotalsInterestByItems(
			Long municipalBondStatusId) {
		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(SUM(m.interest)) "
				+ "from MunicipalBond m join m.entry e left join e.account ac where m.id in "
				+ "(select distinct (m.id) from Payment payment join payment.deposits d join d.municipalBond m "
				+ "where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId)";

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("municipalBondStatusId", municipalBondStatusId);

		List<EntryTotalCollected> list = query.getResultList();

		deleteEmptyEntryTotalCollected(list);

		for (EntryTotalCollected e : list) {
			e.setAccount(interestAccount.getAccountName());
			e.setGroupBy(interestAccount.getAccountCode());
			e.setEntry(interestAccount.getAccountName());
			e.setId(interestAccount.getId());
		}

		return list;
	}

	public List<EntryTotalCollected> getTotalsByItem(boolean isPaidCompensation) {
		List<EntryTotalCollected> result;
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		Long municipalBondStatusId = null;

		if (isPaidCompensation) {
			municipalBondStatusId = systemParameterService
					.findParameter("MUNICIPAL_BOND_STATUS_ID_COMPENSATION");
		} else {
			municipalBondStatusId = systemParameterService
					.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID");
		}

		if (groupBy == null)
			groupBy = "ac.accountCode";

		result = getTotalsByItems(municipalBondStatusId);
		result.addAll(getTotalsDiscountByItems(municipalBondStatusId));
		result.addAll(getTotalsSurchargeByItems(municipalBondStatusId));
		result.addAll(getTotalsTaxByItems(municipalBondStatusId));
		result.addAll(getTotalsInterestByItems(municipalBondStatusId));

		return result;
	}

	public List<EntryTotalCollected> getTotalDepositsInAgreementByItem() {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		Long municipalBondStatusId = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");

		if (groupBy == null)
			groupBy = "ac.accountCode";

		List<EntryTotalCollected> result;

		result = getTotalsByItems(municipalBondStatusId);
		result.addAll(getTotalsDiscountByItems(municipalBondStatusId));
		result.addAll(getTotalsSurchargeByItems(municipalBondStatusId));
		result.addAll(getTotalsTaxByItems(municipalBondStatusId));
		result.addAll(getTotalsInterestByItems(municipalBondStatusId));

		return result;

	}

	public List<EntryTotalCollected> getTotalDepositsInAgreement() {
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}

		if (groupBy == null)
			groupBy = "ac.accountCode";

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,count(d.id), e.name,"
				+ groupBy
				+ ", SUM(d.value), "
				+ " SUM(d.interest), SUM(d.paidTaxes)) from Deposit d join d.municipalBond m "
				+ "join m.entry e left join e.account ac "
				+ "where d.date Between :startDate and :endDate AND m.paymentAgreement is not null"
				+ " GROUP BY e.id, e.name," + groupBy + " ORDER BY " + groupBy;

		Query query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return query.getResultList();
	}

	public List<EntryTotalCollected> getTotalsInPaymentAgreementByItem(
			boolean isPaidByCompensation) {
		List<EntryTotalCollected> result;
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		Long municipalBondStatusId = systemParameterService
				.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");

		if (groupBy == null)
			groupBy = "ac.accountCode";

		result = getTotalsByItems(municipalBondStatusId);
		result.addAll(getTotalsDiscountByItems(municipalBondStatusId));
		result.addAll(getTotalsSurchargeByItems(municipalBondStatusId));
		result.addAll(getTotalsTaxByItems(municipalBondStatusId));
		result.addAll(getTotalsInterestByItems(municipalBondStatusId));

		return result;
	}

	public List<EntryTotalCollected> getTotalsInPaymentAgreement(
			boolean isPaidByCompensation, List<Long> ids) {
		if (ids == null || ids.size() == 0)
			return new ArrayList<EntryTotalCollected>();

		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}

		if (groupBy == null)
			groupBy = "ac.accountCode";

		String sql = "select NEW ec.gob.gim.income.model.EntryTotalCollected(e.id,count(id), e.name,"
				+ groupBy
				+ ", SUM(d.value), SUM(m.discount), SUM(m.surcharge), "
				+ " SUM(d.interest), SUM(d.paidTaxes), SUM (d.value)) from Payment payment join payment.deposits d join d.municipalBond m join m.entry e "
				+ "left join e.account ac where d.date Between :startDate and :endDate "
				+ " AND m.id in (:ids) "
				+ "GROUP BY e.id, e.name,"
				+ groupBy
				+ " ORDER BY " + groupBy;

		Query query = query = getEntityManager().createQuery(sql);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("ids", ids);
		return query.getResultList();
	}

	public List<EntryTotalCollected> getTotalCompensationCollectedByEntry() {
		List<EntryTotalCollected> totalCollecteds = getTotals(true);

		for (EntryTotalCollected res : totalCollecteds) {
			if (res.getTotal() != null
					&& res.getTotal().compareTo(BigDecimal.ZERO) == 0) {
				res.setTotal(res.getValue());
			}
		}
		return totalCollecteds;
	}

	public List<EntryTotalCollected> getTotalInAgreementCollectedByEntry() {
		return getTotalDepositsInAgreement();
	}

	public List<EntryTotalCollected> getTotalCollectedByEntry(
			boolean isPaidByCompensation) {
		return getTotals(isPaidByCompensation);
	}

	public void wire() {
		if (!isFirstTime) {
			return;
		}

		if (isFirstTime && isClosingWorkday) {

			if (closingCurrentWorkday) {
				Long currentId = findWorkdayId();
				if (getNumberOfTillOpened(currentId) > 0) {
					existOpenedTills = true;
					return;
				}
			}

			if (getWorkdayId() != null
					&& getNumberOfTillOpened(getWorkdayId()) > 0) {
				existOpenedTills = true;
				return;
			}

			if (getWorkdayId() == null) {
				setWorkdayId(findWorkdayId());
				startDate = new Date();
			}

			if (getWorkdayId() == null) {
				renderPanel = false;
				existsCurrentWorkday = false;
				return;
			}

			existsCurrentWorkday = true;
			isReadyForPrint = false;
			isFirstTime = false;
			generateGlobalReport();
		}

		getInstance();

		if (isFirstTime && !this.isManaged()) {
			if (!existsTaxRates()) {
				needsTaxRateDefined = true;
				return;
			}
			this.getInstance().setCharge(nextCharge());
			Calendar now = Calendar.getInstance();
			this.getInstance().setDate(now.getTime());
			this.getInstance().setOpeningTime(now.getTime());
			isFirstTime = false;
		}

		if (isFromIncome && this.getInstance().getTillPermissions().size() == 0) {
			createTillPermissions();
		}
		isFirstTime = false;
	}

	public List<String> loadGroupByValues() {
		valuesGroupBy.put("accountCode", "ac.accountCode");
		valuesGroupBy.put("budgetCertificate", "ac.budgetCertificateCode");
		valuesGroupBy.put("entry", "e.code");

		List<String> res = new ArrayList<String>();

		List<String> values = new ArrayList<String>();

		Collection c = valuesGroupBy.values();

		Iterator itr = c.iterator();

		while (itr.hasNext()) {
			values.add(itr.next().toString());
		}

		for (String s : values) {
			for (Iterator i = valuesGroupBy.keySet().iterator(); i.hasNext();) {
				String key = (String) i.next();
				if (valuesGroupBy.get(key).equals(s))
					res.add(key);
			}
		}
		return res;
	}

	public Long nextCharge() {
		Query query = getPersistenceContext().createNamedQuery(
				"Workday.findLastCharge");
		Long c = (Long) query.getSingleResult();
		if (c == null) {
			return 1L;
		}
		return c + 1;
	}

	public Long findWorkdayId() {
		Query query = getPersistenceContext().createNamedQuery(
				"Workday.findCurrentWorkday");
		List<Workday> list = query.getResultList();
		Long c = null;
		if (list != null && list.size() > 0) {
			c = list.get(0).getId();
		}

		return c;
	}

	public List<Person> findCashiers() {
		if (cashiers == null) {
			if (systemParameterService == null) {
				systemParameterService = ServiceLocator.getInstance()
						.findResource(SYSTEM_PARAMETER_SERVICE_NAME);
			}
			String role_name = systemParameterService
					.findParameter("ROLE_NAME_CASHIER");
			Query query = getPersistenceContext().createNamedQuery(
					"Person.findByRoleName")
					.setParameter("roleName", role_name);
			cashiers = (List<Person>) query.getResultList();
		}
		return cashiers != null ? cashiers : new ArrayList<Person>();
	}

	public void createTillPermissions() {
		for (Branch b : findBranches()) {
			for (Till t : b.getTills()) {
				if (t.isActive()) {
					TillPermission tp = new TillPermission();
					tp.setPerson(t.getPerson());
					tp.setEnabled(true);
					tp.setTill(t);
					this.getInstance().add(tp);
				}
			}
		}
	}

	public List<Branch> findBranches() {
		if (branches == null) {
			Query query = getEntityManager().createNamedQuery("Branch.findAll");
			branches = query.getResultList();
			if (branches.size() > 0) {
				if (branch == null)
					branch = branches.get(0);
			}
		}

		return branches != null ? branches : new ArrayList<Branch>();
	}

	public List<TillPermission> getTillPermissionsByBranch() {
		List<TillPermission> tillPermissions = new ArrayList<TillPermission>();
		for (TillPermission t : this.getInstance().getTillPermissions()) {
			if (t.getTill().getBranch().equals(getBranch())) {
				tillPermissions.add(t);
			}
		}
		return tillPermissions;
	}

	public void updateWorkday(Workday w) {
		if (isFromIncome) {
			w.setIncomeObservations(this.getInstance().getIncomeObservations());
			w.setIsIncomeOpening(true);
			w.setTillPermissions(this.getInstance().getTillPermissions());
		} else {
			w.setRevenueObservations(this.getInstance()
					.getRevenueObservations());
			w.setIsRevenueOpening(true);

		}
	}

	public BigDecimal getTotalEmittedEntries(
			List<EntryTotalCollected> entryTotalCollecteds) {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getTotalEmitted() != null)
				res = res.add(e.getTotalEmitted());
		}
		return res;
	}

	public BigDecimal getTotalMainAccounts() {
		BigDecimal res = BigDecimal.ZERO;
		for (AccountBalance e : finallyAccountsForSummary) {
			if (e.getTotal() != null)
				if (!existInDiscount(e.getAccount().getAccountCode())) {
					res = res.add(e.getTotal());
				} else {
					res = res.subtract(e.getTotal());
				}
		}
		return res;
	}

	public BigDecimal getDiscountMainAccounts() {
		BigDecimal res = BigDecimal.ZERO;
		for (AccountBalance e : finallyAccountsForSummary) {
			if (e.getDiscount() != null)
				res = res.add(e.getDiscount());
		}
		return res;
	}

	public BigDecimal getSurchargeMainAccounts() {
		BigDecimal res = BigDecimal.ZERO;
		for (AccountBalance e : finallyAccountsForSummary) {
			if (e.getSurcharge() != null)
				res = res.add(e.getSurcharge());
		}
		return res;
	}

	public BigDecimal getInterestMainAccounts() {
		BigDecimal res = BigDecimal.ZERO;
		for (AccountBalance e : finallyAccountsForSummary) {
			if (e.getInterest() != null)
				res = res.add(e.getInterest());
		}
		return res;
	}

	public BigDecimal getTaxesMainAccounts() {
		BigDecimal res = BigDecimal.ZERO;
		for (AccountBalance e : finallyAccountsForSummary) {
			if (e.getTaxes() != null)
				res = res.add(e.getTaxes());
		}
		return res;
	}

	public BigDecimal getValueMainAccounts() {
		BigDecimal res = BigDecimal.ZERO;
		for (AccountBalance e : finallyAccountsForSummary) {
			if (e.getValue() != null)
				if (!existInDiscount(e.getAccount().getAccountCode())) {
					res = res.add(e.getValue());
				} else {
					res = res.subtract(e.getValue());
				}
		}
		return res;
	}

	public BigDecimal getPreviousYearsValueMainAccounts() {
		BigDecimal res = BigDecimal.ZERO;
		for (AccountBalance e : finallyAccountsForSummary) {
			if (e.getPreviousYears() != null)
				res = res.add(e.getPreviousYears());
		}
		return res;
	}

	public BigDecimal getPreviousYearsValue() {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getPreviousYears() != null)
				res = res.add(e.getPreviousYears());
		}
		return res;
	}

	public BigDecimal getFutureYearsValue() {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getFutureYears() != null)
				res = res.add(e.getFutureYears());
		}
		return res;
	}

	public BigDecimal getEmittedValue() {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getValue() != null)
				res = res.add(e.getValue());
		}

		return res;
	}

	public BigDecimal getValuePreviousYearsMainAccounts() {
		BigDecimal res = BigDecimal.ZERO;
		for (AccountBalance e : finallyAccountsForSummary) {
			if (e.getValue() != null)
				if (e.getPreviousYears() != null)
					res = res.add(e.getPreviousYears());
		}
		return res;
	}

	public BigDecimal getValueNextYearsMainAccounts() {
		BigDecimal res = BigDecimal.ZERO;
		for (AccountBalance e : finallyAccountsForSummary) {
			if (e.getNextYears() != null)
				if (!existInDiscount(e.getAccount().getAccountCode())) {
					res = res.add(e.getNextYears());
				} else {
					res = res.subtract(e.getNextYears());
				}
		}
		return res;
	}

	public BigDecimal getTotalValue(
			List<EntryTotalCollected> entryTotalCollecteds) {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getValue() != null)
				res = res.add(e.getValue());
		}
		return res;
	}

	public BigDecimal getTotalValuePreviousYears(
			List<EntryTotalCollected> entryTotalCollecteds) {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getPreviousYears() != null)
				res = res.add(e.getPreviousYears());
		}
		return res;
	}

	public BigDecimal getTotalValueNextYears(
			List<EntryTotalCollected> entryTotalCollecteds) {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getNextYears() != null)
				res = res.add(e.getNextYears());
		}
		return res;
	}

	public BigDecimal getByCompensationTotalValue() {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollectedsByCompensation) {
			if (e.getValue() != null)
				res = res.add(e.getValue());
		}
		return res;
	}

	public BigDecimal getTotalDiscounts(
			List<EntryTotalCollected> entryTotalCollecteds) {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			if (e.getDiscount() != null)
				res = res.add(e.getDiscount());
		}
		return res;
	}

	public BigDecimal getCompensationTotalDiscounts(List<MunicipalBond> list) {
		BigDecimal res = BigDecimal.ZERO;
		for (MunicipalBond m : list) {
			if (m.getDiscount() != null)
				res = res.add(m.getDiscount());
		}
		return res;
	}

	public BigDecimal getTotalSurcharges(
			List<EntryTotalCollected> entryTotalCollecteds) {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			res = res.add(e.getSurcharge());
		}
		return res;
	}

	public BigDecimal getCompensationTotalSurcharges(List<MunicipalBond> list) {
		BigDecimal res = BigDecimal.ZERO;
		for (MunicipalBond m : list) {
			if (m.getSurcharge() != null)
				res = res.add(m.getSurcharge());
		}
		return res;
	}

	public BigDecimal getTotalInterests(
			List<EntryTotalCollected> entryTotalCollecteds) {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			res = res.add(e.getInterest());
		}
		return res;
	}

	public BigDecimal getByCompensationTotalInterests() {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollectedsByCompensation) {
			if (e.getInterest() != null)
				res = res.add(e.getInterest());
		}
		return res;
	}

	public BigDecimal getTotalTaxes(
			List<EntryTotalCollected> entryTotalCollecteds) {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollecteds) {
			res = res.add(e.getTaxes());
		}
		return res;
	}

	public BigDecimal getByCompensationTotalTaxes() {
		BigDecimal res = BigDecimal.ZERO;
		for (EntryTotalCollected e : entryTotalCollectedsByCompensation) {
			if (e.getTaxes() != null)
				res = res.add(e.getTaxes());
		}
		return res;
	}

	public List<AccountBalance> getAllChildren(AccountBalance ac) {
		List<AccountBalance> acBalances = new ArrayList<AccountBalance>();
		for (AccountBalance ab : ac.getChildren()) {
			acBalances.add(ab);
			if (ab.getChildren().size() > 0)
				acBalances.addAll(getAllChildren(ab));
			if (isFromIncome) {
				if (ab.getTotal() != null)
					ac.setTotal(ac.getTotal().add(ab.getTotal()));
				if (ab.getPreviousYears() != null)
					ac.setPreviousYears(ac.getPreviousYears().add(
							ab.getPreviousYears()));
				if (ab.getValue() != null)
					ac.setValue(ac.getValue().add(ab.getValue()));
				if (ab.getNextYears() != null)
					ac.setNextYears(ac.getNextYears().add(ab.getNextYears()));
			}
		}
		return acBalances;
	}

	public AccountBalance copyAccountBalance(AccountBalance a) {
		AccountBalance ab = new AccountBalance();
		ab.setAccount(a.getAccount());
		ab.setDiscount(a.getDiscount() == null ? BigDecimal.ZERO : a
				.getDiscount());
		ab.setInterest(a.getInterest() == null ? BigDecimal.ZERO : a
				.getInterest());
		ab.setParent(a.getParent());
		ab.setSurcharge(a.getSurcharge() == null ? BigDecimal.ZERO : a
				.getSurcharge());
		ab.setTaxes(a.getTaxes() == null ? BigDecimal.ZERO : a.getTaxes());
		ab.setTotal(a.getTotal() == null ? BigDecimal.ZERO : a.getTotal());
		ab.setValue(a.getValue() == null ? BigDecimal.ZERO : a.getValue());
		ab.setPreviousYears(a.getPreviousYears() == null ? BigDecimal.ZERO : a
				.getPreviousYears());
		ab.setNextYears(a.getNextYears() == null ? BigDecimal.ZERO : a
				.getNextYears());
		ab.setChildren(a.getChildren());
		return ab;
	}

	public void buildMainAccounts() {
		mainAccounts = new ArrayList<AccountBalance>();
		for (AccountBalance c : mainAccountBalance.getChildren()) {
			AccountBalance ab = copyAccountBalance(c);
			List<AccountBalance> accountBalances = getAllChildren(ab);
			ab.setChildren(new ArrayList<AccountBalance>());
			ab.setChildren(accountBalances);
			if (ab.getTotal() != null
					&& ab.getTotal().compareTo(BigDecimal.ZERO) > 0) {
				mainAccounts.add(ab);
			}
		}

		columnsNumber = numberOfColumnsGlobalReport();
		columnsSize = sizeOfColumnsGlobalReport();
	}

	public List<AccountBalance> orderAccountBalanceByAccountCode(
			List<AccountBalance> list) {
		List<String> accountCodes = new ArrayList<String>();
		List<AccountBalance> accounts = new ArrayList<AccountBalance>();
		for (AccountBalance ac : list) {
			String code = ac.getAccount().getAccountCode();
			if (ac.getAccount() != null && !accountCodes.contains(code))
				accountCodes.add(code);
		}

		Collections.sort(accountCodes);

		for (String s : accountCodes) {
			for (AccountBalance ac : list) {
				if (ac.getAccount() != null
						&& ac.getAccount().getAccountCode().equals(s)
						&& !accounts.contains(ac)) {
					accounts.add(ac);
				}
			}
		}

		for (AccountBalance ac : list) {
			if (!accounts.contains(ac)) {
				accounts.add(ac);
			}
		}

		return accounts;
	}

	public void orderByAccountCode(List<Account> list) {
		List<String> accountCodes = new ArrayList<String>();
		List<Account> accounts = new ArrayList<Account>();
		for (Account ac : list) {
			if (ac != null)
				accountCodes.add(ac.getAccountCode());
		}

		Collections.sort(accountCodes);

		for (String s : accountCodes) {
			for (Account ac : list) {
				if (ac != null && ac.getAccountCode().equals(s)
						&& !accounts.contains(ac)) {
					accounts.add(ac);
				}
			}
		}

		for (Account ac : list) {
			if (!accounts.contains(ac)) {
				accounts.add(ac);
			}
		}

		list.clear();
		list.addAll(accounts);
	}

	public List<AccountBalance> classificationBuildMainAccounts() {
		finallyAccountsForSummary = orderAccountBalanceByAccountCode(mainAccounts);

		for (int i = 0; i < finallyAccountsForSummary.size(); i++) {
			for (int j = i + 1; j < finallyAccountsForSummary.size(); j++) {
				if (isAChild(finallyAccountsForSummary.get(j).getAccount(),
						finallyAccountsForSummary.get(i).getAccount())) {
					finallyAccountsForSummary.remove(j);
					j = j - 1;
				}
			}
		}
		return finallyAccountsForSummary;
	}

	public void selectEntries() {
		entries = new ArrayList<Entry>();
		generalTotal = BigDecimal.ZERO;
		statusIds = new ArrayList<Long>();

		if (municipalBondStatus != null) {
			statusIds.add(municipalBondStatus.getId());
		} else {
			statusIds.add(paidMunicipalBondStatus.getId());
			statusIds.add(pendingStatus.getId());
			statusIds.add(compensationMunicipalBondStatus.getId());
			statusIds.add(inPaymentAgreementStatus.getId());
			statusIds.add(externalChannelStatus.getId());
			statusIds.add(blockedMunicipalBondStatus.getId());
			// statusIds.add(reversedMunicipalBondStatus.getId());
		}

		if (entry == null) {
			municipalBondViews = getMunicipalBondsViewBetweenDates();
			entries = orderEntriesByCode(getEntriesFromMunicipalBondsBetweenDates());
		} else {
			entries.add(entry);
			municipalBondViews = getMunicipalBondsViewBetweenDatesByEntry(entry);
		}
	}

	public BigDecimal totalByEntry(Entry e) {
		BigDecimal aux = BigDecimal.ZERO;
		for (MunicipalBondView m : municipalBondViews) {
			if (e.getId().equals(m.getEntryId())) {
				aux = aux.add(m.getValue());
			}
		}
		generalTotal = generalTotal.add(aux);
		return aux;
	}

	public List<Entry> orderEntriesByCode(List<Entry> enList) {
		List<Entry> aux = new ArrayList<Entry>();
		List<String> codes = new ArrayList<String>();
		for (Entry e : enList) {
			if (!codes.contains(e.getCode()))
				codes.add(e.getCode());
		}
		Collections.sort(codes);

		for (String s : codes) {
			for (Entry e : enList) {
				if (!containsEntry(aux, e) && e.getCode().equals(s))
					aux.add(e);
			}
		}

		return aux;

	}

	public boolean containsEntry(List<Entry> entries, Entry en) {
		for (Entry e : entries) {
			if (e.getId() == en.getId())
				return true;
		}
		return false;
	}

	public Long getNumberOfMunicipalBondsByEntry(Entry e) {
		Long res = 0L;
		for (MunicipalBondView m : this.municipalBondViews) {
			if (e.getId().equals(m.getEntryId())) {
				res++;
			}
		}
		return res;
	}

	public List<MunicipalBondView> getMunicipalBondsByEntry(Entry e) {
		List<MunicipalBondView> municipalBondsList = new ArrayList<MunicipalBondView>();
		for (MunicipalBondView m : this.municipalBondViews) {
			if (e.getId().equals(m.getEntryId())) {
				municipalBondsList.add(m);
			}
		}
		return municipalBondsList;
	}

	@Override
	public String update() {
		if (!isDateAvailable(this.getInstance().getDate())) {
			String message = Interpolator.instance().interpolate(
					"#{messages['workday.dateNoAvailable']}", new Object[0]);
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return "failed";
		}
		if (!isChargeAvailable()) {
			String message = Interpolator.instance().interpolate(
					"#{messages['workday.chargeNoAvailable']}", new Object[0]);
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					message);
			return "failed";
		}

		if (isClosingWorkday) {
			this.getInstance().setIsIncomeOpening(false);
			isReadyForPrint = true;
		} else if (isFromIncome) {
			this.getInstance().setIsIncomeOpening(true);
		} else if (!isFromIncome) {
			this.getInstance().setIsRevenueOpening(true);
		}

		return super.update();

	}

	public void updateTills() {
		for (TillPermission tp : this.getInstance().getTillPermissions()) {
			if (tp.getPerson() != tp.getTill().getPerson()) {
				tp.getTill().setPerson(tp.getPerson());
			}
		}
	}

	public void openTillsBank() {
		Calendar cal = new GregorianCalendar();
		for (TillPermission tp : this.getInstance().getTillPermissions()) {
			// if ((tp.getTill().isTillBank()) && (tp.getOpeningTime() ==
			// null)){
			// tp.setOpeningTime(cal.getTime());
			// }
			if ((!tp.isEnabled()) && (tp.getOpeningTime() != null)) {
				tp.setOpeningTime(null);
			}
		}
	}

	public String findAdministratorRole() {
		systemParameterService = ServiceLocator.getInstance().findResource(
				SystemParameterService.LOCAL_NAME);
		return systemParameterService.findParameter("ROLE_NAME_ADMINISTRATOR");
	}

	public boolean existsDuplicateUsers() {
		boolean aux = false;
		String rootRoleName = findAdministratorRole();

		List<Person> cashiers = new ArrayList<Person>();
		for (TillPermission t : this.getInstance().getTillPermissions()) {
			if (!t.getPerson().getUser().hasRole(rootRoleName)) {
				if (!cashiers.contains(t.getPerson())) {
					cashiers.add(t.getPerson());
				} else {
					return true;
				}
			}
		}
		return aux;
	}

	public String save() {
		if (existsDuplicateUsers()) {
			addFacesMessageFromResourceBundle("till.duplicateUsers");
			return "failed";
		}

		updateTills();

		openTillsBank();

		if (!needsTaxRateDefined && existsCurrentWorkday == null
				&& !existOpenedTills) {
			if (isManaged()) {
				return update();
			} else {
				new File(
						"/opt/jboss/6.1.0.Final/server/instance01/deploy/gim.ear/gim.war/PDFDocuments/*")
						.delete();
				new File(
						"/opt/jboss/6.1.0.Final/server/instance02/deploy/gim.ear/gim.war/PDFDocuments/*")
						.delete();
				new File(
						"/opt/jboss/6.1.0.Final/server/instance03/deploy/gim.ear/gim.war/PDFDocuments/*")
						.delete();
				return persist();
			}
		}

		if (isManaged() && !needsTaxRateDefined && existsCurrentWorkday
				&& !isReadyForPrint) {
			return updateClosingWorkDay();
		}
		return "failed";
	}

	@Override
	public String persist() {
		try {
			// Workday wd = existsActualWorkday();
			// if (wd != null) {
			// updateWorkday(wd);
			// this.setInstance(wd);
			// this.getEntityManager().merge(this.getInstance());
			// String res = this.update();
			// if (res.equals("updated")) {
			// return "persisted";
			// } else {
			// return "failed";
			// }
			// }

			Calendar current = DateUtils.getTruncatedInstance(Calendar
					.getInstance().getTime());
			if (!isDateAvailable(this.getInstance().getDate())
					|| (this.getInstance().getDate().before(current.getTime()))) {
				String message = Interpolator.instance()
						.interpolate("#{messages['workday.dateNoAvailable']}",
								new Object[0]);
				facesMessages
						.addToControl(
								"",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								message);
				return "failed";
			}

			if (!isChargeAvailable()) {
				String message = Interpolator.instance().interpolate(
						"#{messages['workday.chargeNoAvailable']}",
						new Object[0]);
				facesMessages
						.addToControl(
								"",
								org.jboss.seam.international.StatusMessage.Severity.ERROR,
								message);
				return "failed";
			}

			if (isFromIncome) {
				this.getInstance().setIsIncomeOpening(true);
			} else {
				this.getInstance().setIsRevenueOpening(true);
			}

			TillPermission tillPermision;

			return super.persist();

		} catch (Exception e) {
			System.out
					.println("::::::::::::::::::::::::exception:::::::::::::::::::::::::::::::::::::: "
							+ e);
		}
		return "failed";
	}

	public boolean hasRoleNameRevenueBoss() {
		return userSession.hasRole(UserSession.ROLE_NAME_REVENUE_BOSS);
	}

	public boolean canCreateNewWorkday() {
		if (existOpenWorkdays == null)
			existOpenWorkdays = existOpenedWorkdays();
		return !existOpenWorkdays;
	}

	public String updateClosingWorkDay() {
		if (isClosingWorkday) {
			this.getInstance().setIsIncomeOpening(false);
			this.getInstance().setClosingWorkdayDate(new Date());
			Calendar now = Calendar.getInstance();
			this.getInstance().setClosingTime(now.getTime());
			isReadyForPrint = true;
		}
		String aux = super.update();
		if (aux.equals("updated")) {
			return "readyForPrint";
		}
		return aux;
	}

	public boolean isDateAvailable(Date d) {
		Query query = null;
		if (isFromIncome) {
			query = getPersistenceContext().createNamedQuery(
					"Workday.findByIncomeOpeningAndDate").setParameter("date",
					d);
		} else {
			query = getPersistenceContext().createNamedQuery(
					"Workday.findByRevenueOpeningAndDate").setParameter("date",
					d);
		}

		List<Workday> workdays = query.getResultList();
		if (workdays != null && workdays.size() > 0
				&& workdays.get(0).getId() != this.getInstance().getId()) {
			return false;
		}
		return true;
	}

	public void entrySelectedListener(ActionEvent event) {
		UIComponent component = event.getComponent();
		Entry entry = (Entry) component.getAttributes().get("entry");
		this.setEntry(entry);
		if (entry.getAccount() != null) {
			setEntryCode(entry.getAccount().getAccountCode());
		} else {
			setEntryCode(entry.getCode());
		}
	}

	public void searchEntry() {
		if (entryCode != null) {
			RevenueService revenueService = ServiceLocator.getInstance()
					.findResource(REVENUE_SERVICE_NAME);
			Entry entry = revenueService.findEntryByCode(entryCode);
			if (entry != null) {
				this.entry = entry;
				this.setEntry(entry);
				if (entry.getAccount() != null) {
					setEntryCode(entry.getAccount().getAccountCode());
				} else {
					setEntryCode(entry.getCode());
				}
			} else {
				setEntry(null);
			}
		}
	}

	public void searchEntryByCriteria() {
		// logger.info("SEARCH Entry BY CRITERIA "+this.criteriaEntry);
		if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()) {
			RevenueService revenueService = (RevenueService) ServiceLocator
					.getInstance().findResource(REVENUE_SERVICE_NAME);
			entries = revenueService.findEntryByCriteria(criteriaEntry);
		}
	}

	public void clearSearchEntryPanel() {
		this.setCriteriaEntry(null);
		entries = null;
	}

	public boolean existOpenedWorkdays() {
		Query query = getPersistenceContext().createNamedQuery(
				"Workday.countOpenedWorkday");

		Long openedWorkday = (Long) query.getSingleResult();

		if (openedWorkday > 0)
			return true;
		return false;
	}

	public Workday existsActualWorkday() {
		Query query = getPersistenceContext().createNamedQuery(
				"Workday.findByDate").setParameter("date", new Date());
		List<Workday> workdays = query.getResultList();
		if (workdays != null && workdays.size() > 0) {
			return workdays.get(0);
		}
		return null;
	}

	public boolean isChargeAvailable() {
		Query query = getPersistenceContext().createNamedQuery(
				"Workday.findByCharge").setParameter("charge",
				this.getInstance().getCharge());
		List<Workday> workdays = query.getResultList();
		if (workdays != null && workdays.size() > 0
				&& workdays.get(0).getId() != this.getInstance().getId()) {
			return false;
		}
		return true;
	}

	public boolean isWired() {
		return true;
	}

	public Workday getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Branch getBranch() {
		return branch;
	}

	public boolean isNeedsTaxRateDefined() {
		return needsTaxRateDefined;
	}

	public void setNeedsTaxRateDefined(boolean needsTaxRateDefined) {
		this.needsTaxRateDefined = needsTaxRateDefined;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEntryTotalCollecteds(
			List<EntryTotalCollected> entryTotalCollecteds) {
		this.entryTotalCollecteds = entryTotalCollecteds;
	}

	public List<EntryTotalCollected> getEntryTotalCollecteds() {
		return entryTotalCollecteds;
	}

	public void setEntryTotalCollectedsByCompensation(
			List<EntryTotalCollected> entryTotalCollectedsByCompensation) {
		this.entryTotalCollectedsByCompensation = entryTotalCollectedsByCompensation;
	}

	public List<EntryTotalCollected> getEntryTotalCollectedsByCompensation() {
		return entryTotalCollectedsByCompensation;
	}

	public boolean isReadyForPrint() {
		return isReadyForPrint;
	}

	public void setReadyForPrint(boolean isReadyForPrint) {
		this.isReadyForPrint = isReadyForPrint;
	}

	public boolean isClosingWorkday() {
		return isClosingWorkday;
	}

	public void setClosingWorkday(boolean isClosingWorkday) {
		this.isClosingWorkday = isClosingWorkday;
	}

	public Boolean getExistsCurrentWorkday() {
		return existsCurrentWorkday;
	}

	public void setExistsCurrentWorkday(Boolean existsCurrentWorkday) {
		this.existsCurrentWorkday = existsCurrentWorkday;
	}

	public boolean isRenderPanel() {
		return renderPanel;
	}

	public void setRenderPanel(boolean renderPanel) {
		this.renderPanel = renderPanel;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public void setSummaryTotalCollecteds(
			List<EntryTotalCollected> summaryTotalCollecteds) {
		this.summaryTotalCollecteds = summaryTotalCollecteds;
	}

	public List<EntryTotalCollected> getSummaryTotalCollecteds() {
		return summaryTotalCollecteds;
	}

	public Map<String, String> getValuesGroupBy() {
		return valuesGroupBy;
	}

	public void setValuesGroupBy(Map<String, String> valuesGroupBy) {
		this.valuesGroupBy = valuesGroupBy;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getKeygroupBy() {
		return keygroupBy;
	}

	public void setKeygroupBy(String keygroupBy) {
		this.keygroupBy = keygroupBy;
	}

	public boolean isFromIncome() {
		return isFromIncome;
	}

	public void setFromIncome(boolean isFromIncome) {
		this.isFromIncome = isFromIncome;
	}

	public List<AccountBalance> getMainAccounts() {
		return mainAccounts;
	}

	public void setMainAccounts(List<AccountBalance> mainAccounts) {
		this.mainAccounts = mainAccounts;
	}

	public List<AccountBalance> getAccountsForSummary() {
		return accountsForSummary;
	}

	public void setAccountsForSummary(List<AccountBalance> accountsForSummary) {
		this.accountsForSummary = accountsForSummary;
	}

	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	public void setSystemParameterService(
			SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}

	public List<EntryTotalCollected> getEntryTotalDiscount() {
		return entryTotalDiscount;
	}

	public void setEntryTotalDiscount(
			List<EntryTotalCollected> entryTotalDiscount) {
		this.entryTotalDiscount = entryTotalDiscount;
	}

	public List<EntryTotalCollected> getEntryTotalDiscountByCompensation() {
		return entryTotalDiscountByCompensation;
	}

	public void setEntryTotalDiscountByCompensation(
			List<EntryTotalCollected> entryTotalDiscountByCompensation) {
		this.entryTotalDiscountByCompensation = entryTotalDiscountByCompensation;
	}

	public boolean isNeedsInterestRateDefined() {
		return needsInterestRateDefined;
	}

	public void setNeedsInterestRateDefined(boolean needsInterestRateDefined) {
		this.needsInterestRateDefined = needsInterestRateDefined;
	}

	public MunicipalBondStatus getReversedMunicipalBondStatus() {
		return reversedMunicipalBondStatus;
	}

	public void setReversedMunicipalBondStatus(
			MunicipalBondStatus reversedMunicipalBondStatus) {
		this.reversedMunicipalBondStatus = reversedMunicipalBondStatus;
	}

	public Account getInterestAccount() {
		return interestAccount;
	}

	public void setInterestAccount(Account interestAccount) {
		this.interestAccount = interestAccount;
	}

	public Account getInterestAccountEmaalep() {
		return interestAccountEmaalep;
	}

	public void setInterestAccountEmaalep(Account interestAccountEmaalep) {
		this.interestAccountEmaalep = interestAccountEmaalep;
	}

	public BigDecimal getTotalCompensationCollected() {
		return totalCompensationCollected;
	}

	public void setTotalCompensationCollected(
			BigDecimal totalCompensationCollected) {
		this.totalCompensationCollected = totalCompensationCollected;
	}

	public BigDecimal getTotalSummaryCollected() {
		return totalSummaryCollected;
	}

	public void setTotalSummaryCollected(BigDecimal totalSummaryCollected) {
		this.totalSummaryCollected = totalSummaryCollected;
	}

	public int getNumberOfServedPeople() {
		return numberOfServedPeople;
	}

	public void setNumberOfServedPeople(int numberOfServedPeople) {
		this.numberOfServedPeople = numberOfServedPeople;
	}

	public Long getNumberOfTransactions() {
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(Long numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}

	public BigDecimal getTotalEmitted() {
		return totalEmitted;
	}

	public void setTotalEmitted(BigDecimal totalEmitted) {
		this.totalEmitted = totalEmitted;
	}

	public List<EntryTotalCollected> getEntryTotalCollectedsForTree() {
		return entryTotalCollectedsForTree;
	}

	public void setEntryTotalCollectedsForTree(
			List<EntryTotalCollected> entryTotalCollectedsForTree) {
		this.entryTotalCollectedsForTree = entryTotalCollectedsForTree;
	}

	public List<EntryTotalCollected> getEntryTotalCollectedsByCompensationForTree() {
		return entryTotalCollectedsByCompensationForTree;
	}

	public void setEntryTotalCollectedsByCompensationForTree(
			List<EntryTotalCollected> entryTotalCollectedsByCompensationForTree) {
		this.entryTotalCollectedsByCompensationForTree = entryTotalCollectedsByCompensationForTree;
	}

	public List<EntryTotalCollected> getEntryTotalCollectedsInAgreementForTree() {
		return entryTotalCollectedsInAgreementForTree;
	}

	public void setEntryTotalCollectedsInAgreementForTree(
			List<EntryTotalCollected> entryTotalCollectedsInAgreementForTree) {
		this.entryTotalCollectedsInAgreementForTree = entryTotalCollectedsInAgreementForTree;
	}

	public BigDecimal getDiscountsValueEffective() {
		return discountsValueEffective;
	}

	public void setDiscountsValueEffective(BigDecimal discountsValueEffective) {
		this.discountsValueEffective = discountsValueEffective;
	}

	public BigDecimal getDiscountsValueByCompensation() {
		return discountsValueByCompensation;
	}

	public void setDiscountsValueByCompensation(
			BigDecimal discountsValueByCompensation) {
		this.discountsValueByCompensation = discountsValueByCompensation;
	}

	public List<EntryTotalCollected> getEntryTotalReversed() {
		return entryTotalReversed;
	}

	public void setEntryTotalReversed(
			List<EntryTotalCollected> entryTotalReversed) {
		this.entryTotalReversed = entryTotalReversed;
	}

	public List<EntryTotalCollected> getTaxesReversed() {
		return taxesReversed;
	}

	public void setTaxesReversed(List<EntryTotalCollected> taxesReversed) {
		this.taxesReversed = taxesReversed;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public MunicipalBondStatus getMunicipalBondStatus() {
		return municipalBondStatus;
	}

	public void setMunicipalBondStatus(MunicipalBondStatus municipalBondStatus) {
		this.municipalBondStatus = municipalBondStatus;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public String getEntryCode() {
		return entryCode;
	}

	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}

	public String getCriteriaEntry() {
		return criteriaEntry;
	}

	public void setCriteriaEntry(String criteriaEntry) {
		this.criteriaEntry = criteriaEntry;
	}

	public List<Entry> getEntriesSearch() {
		return entriesSearch;
	}

	public void setEntriesSearch(List<Entry> entriesSearch) {
		this.entriesSearch = entriesSearch;
	}

	public boolean isShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	public List<PaymentFraction> getCreditNotePayments() {
		return creditNotePayments;
	}

	public void setCreditNotePayments(List<PaymentFraction> creditNotePayments) {
		this.creditNotePayments = creditNotePayments;
	}

	private List<Person> cashiers;

	public List<Person> getCashiers() {
		return cashiers;
	}

	public void setCashiers(List<Person> cashiers) {
		this.cashiers = cashiers;
	}

	public BigDecimal getGeneralTotal() {
		return generalTotal;
	}

	public void setGeneralTotal(BigDecimal generalTotal) {
		this.generalTotal = generalTotal;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}

	public List<EntryTotalCollected> getEntryTotalCollectedsInAgreement() {
		return entryTotalCollectedsInAgreement;
	}

	public List<EntryTotalCollected> getEntryTotalCancelled() {
		return entryTotalCancelled;
	}

	public void setEntryTotalCancelled(
			List<EntryTotalCollected> entryTotalCancelled) {
		this.entryTotalCancelled = entryTotalCancelled;
	}

	public List<EntryTotalCollected> getEntryTotalFuture() {
		return entryTotalFuture;
	}

	public void setEntryTotalFuture(List<EntryTotalCollected> entryTotalFuture) {
		this.entryTotalFuture = entryTotalFuture;
	}

	public List<EntryTotalCollected> getEntryTotalPrepaid() {
		return entryTotalPrepaid;
	}

	public void setEntryTotalPrepaid(List<EntryTotalCollected> entryTotalPrepaid) {
		this.entryTotalPrepaid = entryTotalPrepaid;
	}

	public List<EntryTotalCollected> getEntryTotalFormalize() {
		return entryTotalFormalize;
	}

	public void setEntryTotalFormalize(
			List<EntryTotalCollected> entryTotalFormalize) {
		this.entryTotalFormalize = entryTotalFormalize;
	}

	public void setEntryTotalCollectedsInAgreement(
			List<EntryTotalCollected> entryTotalCollectedsInAgreement) {
		this.entryTotalCollectedsInAgreement = entryTotalCollectedsInAgreement;
	}

	public boolean isFirstTime() {
		return isFirstTime;
	}

	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public List<EntryTotalCollected> getTaxesEmitted() {
		return taxesEmitted;
	}

	public void setTaxesEmitted(List<EntryTotalCollected> taxesEmitted) {
		this.taxesEmitted = taxesEmitted;
	}

	public List<EntryTotalCollected> getTaxesCancelled() {
		return taxesCancelled;
	}

	public void setTaxesCancelled(List<EntryTotalCollected> taxesCancelled) {
		this.taxesCancelled = taxesCancelled;
	}

	public BigDecimal getTotalCashCollectedPreviousYears() {
		return totalCashCollectedPreviousYears;
	}

	public void setTotalCashCollectedPreviousYears(
			BigDecimal totalCashCollectedPreviousYears) {
		this.totalCashCollectedPreviousYears = totalCashCollectedPreviousYears;
	}

	public BigDecimal getTotalCashCollectedCurrentYear() {
		return totalCashCollectedCurrentYear;
	}

	public void setTotalCashCollectedCurrentYear(
			BigDecimal totalCashCollectedCurrentYear) {
		this.totalCashCollectedCurrentYear = totalCashCollectedCurrentYear;
	}

	public BigDecimal getSurchargesValueEffective() {
		return surchargesValueEffective;
	}

	public void setSurchargesValueEffective(BigDecimal surchargesValueEffective) {
		this.surchargesValueEffective = surchargesValueEffective;
	}

	public BigDecimal getSurchargesValueCompensation() {
		return surchargesValueCompensation;
	}

	public void setSurchargesValueCompensation(
			BigDecimal surchargesValueCompensation) {
		this.surchargesValueCompensation = surchargesValueCompensation;
	}

	public BigDecimal getInterestValueEffective() {
		return interestValueEffective;
	}

	public void setInterestValueEffective(BigDecimal interestValueEffective) {
		this.interestValueEffective = interestValueEffective;
	}

	public BigDecimal getInterestValueCompensation() {
		return interestValueCompensation;
	}

	public void setInterestValueCompensation(
			BigDecimal interestValueCompensation) {
		this.interestValueCompensation = interestValueCompensation;
	}

	public BigDecimal getTaxesValueEffective() {
		return taxesValueEffective;
	}

	public void setTaxesValueEffective(BigDecimal taxesValueEffective) {
		this.taxesValueEffective = taxesValueEffective;
	}

	public BigDecimal getTaxesValueCompensation() {
		return taxesValueCompensation;
	}

	public void setTaxesValueCompensation(BigDecimal taxesValueCompensation) {
		this.taxesValueCompensation = taxesValueCompensation;
	}

	public BigDecimal getPreviousValueAgreement() {
		return previousValueAgreement;
	}

	public void setPreviousValueAgreement(BigDecimal previousValueAgreement) {
		this.previousValueAgreement = previousValueAgreement;
	}

	public BigDecimal getCurrentValueAgreement() {
		return currentValueAgreement;
	}

	public void setCurrentValueAgreement(BigDecimal currentValueAgreement) {
		this.currentValueAgreement = currentValueAgreement;
	}

	public BigDecimal getTaxesValueAgreement() {
		return taxesValueAgreement;
	}

	public void setTaxesValueAgreement(BigDecimal taxesValueAgreement) {
		this.taxesValueAgreement = taxesValueAgreement;
	}

	public BigDecimal getTotalValueAgreement() {
		return totalValueAgreement;
	}

	public void setTotalValueAgreement(BigDecimal totalValueAgreement) {
		this.totalValueAgreement = totalValueAgreement;
	}

	public BigDecimal getInterestValueAgreement() {
		return interestValueAgreement;
	}

	public void setInterestValueAgreement(BigDecimal interestValueAgreement) {
		this.interestValueAgreement = interestValueAgreement;
	}

	public BigDecimal getCurrentDiscountsValueEffective() {
		return currentDiscountsValueEffective;
	}

	public void setCurrentDiscountsValueEffective(
			BigDecimal currentDiscountsValueEffective) {
		this.currentDiscountsValueEffective = currentDiscountsValueEffective;
	}

	public BigDecimal getPreviousDiscountsValueEffective() {
		return previousDiscountsValueEffective;
	}

	public void setPreviousDiscountsValueEffective(
			BigDecimal previousDiscountsValueEffective) {
		this.previousDiscountsValueEffective = previousDiscountsValueEffective;
	}

	public BigDecimal getTotalValueByCompensation() {
		return totalValueByCompensation;
	}

	public void setTotalValueByCompensation(BigDecimal totalValueByCompensation) {
		this.totalValueByCompensation = totalValueByCompensation;
	}

	public BigDecimal getCurrentValueCompensation() {
		return currentValueCompensation;
	}

	public void setCurrentValueCompensation(BigDecimal currentValueCompensation) {
		this.currentValueCompensation = currentValueCompensation;
	}

	public BigDecimal getPreviousValueCompensation() {
		return previousValueCompensation;
	}

	public void setPreviousValueCompensation(
			BigDecimal previousValueCompensation) {
		this.previousValueCompensation = previousValueCompensation;
	}

	public BigDecimal getCurrentDiscountsValueCompensation() {
		return currentDiscountsValueCompensation;
	}

	public void setCurrentDiscountsValueCompensation(
			BigDecimal currentDiscountsValueCompensation) {
		this.currentDiscountsValueCompensation = currentDiscountsValueCompensation;
	}

	public BigDecimal getPreviousDiscountsValueCompensation() {
		return previousDiscountsValueCompensation;
	}

	public void setPreviousDiscountsValueCompensation(
			BigDecimal previousDiscountsValueCompensation) {
		this.previousDiscountsValueCompensation = previousDiscountsValueCompensation;
	}

	public BigDecimal getTotalReversed() {
		return totalReversed;
	}

	public void setTotalReversed(BigDecimal totalReversed) {
		this.totalReversed = totalReversed;
	}

	public Charge getIncomeCharge() {
		return incomeCharge;
	}

	public void setIncomeCharge(Charge incomeCharge) {
		this.incomeCharge = incomeCharge;
	}

	public Charge getRevenueCharge() {
		return revenueCharge;
	}

	public void setRevenueCharge(Charge revenueCharge) {
		this.revenueCharge = revenueCharge;
	}

	public Delegate getIncomeDelegate() {
		return incomeDelegate;
	}

	public void setIncomeDelegate(Delegate incomeDelegate) {
		this.incomeDelegate = incomeDelegate;
	}

	public Delegate getRevenueDelegate() {
		return revenueDelegate;
	}

	public void setRevenueDelegate(Delegate revenueDelegate) {
		this.revenueDelegate = revenueDelegate;
	}

	public Long getTotalMunicipalBondEmitted() {
		return totalMunicipalBondEmitted;
	}

	public void setTotalMunicipalBondEmitted(Long totalMunicipalBondEmitted) {
		this.totalMunicipalBondEmitted = totalMunicipalBondEmitted;
	}

	public Long getTotalMunicipalBondNullified() {
		return totalMunicipalBondNullified;
	}

	public void setTotalMunicipalBondNullified(Long totalMunicipalBondNullified) {
		this.totalMunicipalBondNullified = totalMunicipalBondNullified;
	}

	public BigDecimal getTotalNullifieds() {
		return totalNullifieds;
	}

	public void setTotalNullifieds(BigDecimal totalNullifieds) {
		this.totalNullifieds = totalNullifieds;
	}

	public BigDecimal getRealTotalEmitted() {
		return realTotalEmitted;
	}

	public void setRealTotalEmitted(BigDecimal realTotalEmitted) {
		this.realTotalEmitted = realTotalEmitted;
	}

	public TreeNode<AccountBalance> getRootNode() {
		return rootNode;
	}

	public void setRootNode(TreeNode<AccountBalance> rootNode) {
		this.rootNode = rootNode;
	}

	public boolean isGlobalReport() {
		return globalReport;
	}

	public void setGlobalReport(boolean globalReport) {
		this.globalReport = globalReport;
	}

	public boolean isDetailDifferentYears() {
		return detailDifferentYears;
	}

	public void setDetailDifferentYears(boolean detailDifferentYears) {
		this.detailDifferentYears = detailDifferentYears;
	}

	public List<ReportView> getTotalNullified() {
		return totalNullified;
	}

	public void setTotalNullified(List<ReportView> totalNullified) {
		this.totalNullified = totalNullified;
	}

	public List<ReportView> getTotalByCashier() {
		return totalByCashier;
	}

	public void setTotalByCashier(List<ReportView> totalByCashier) {
		this.totalByCashier = totalByCashier;
	}

	public List<ReportView> getTotalEmitteds() {
		return totalEmitteds;
	}

	public void setTotalEmitteds(List<ReportView> totalEmitteds) {
		this.totalEmitteds = totalEmitteds;
	}

	public List<MunicipalBondItem> getMunicipalBondItems() {
		return municipalBondItems;
	}

	public void setMunicipalBondItems(List<MunicipalBondItem> municipalBondItems) {
		this.municipalBondItems = municipalBondItems;
	}

	public Date getClosingWorkdayDate() {
		return closingWorkdayDate;
	}

	public void setClosingWorkdayDate(Date closingWorkdayDate) {
		this.closingWorkdayDate = closingWorkdayDate;
	}

	public boolean isClosingCurrentWorkday() {
		return closingCurrentWorkday;
	}

	public void setClosingCurrentWorkday(boolean closingCurrentWorkday) {
		this.closingCurrentWorkday = closingCurrentWorkday;
	}

	public boolean isExistOpenedTills() {
		return existOpenedTills;
	}

	public void setExistOpenedTills(boolean existOpenedTills) {
		this.existOpenedTills = existOpenedTills;
	}

	public List<MunicipalBondView> getMunicipalBondViews() {
		return municipalBondViews;
	}

	public void setMunicipalBondViews(List<MunicipalBondView> municipalBondViews) {
		this.municipalBondViews = municipalBondViews;
	}

	public Long getExternalPaidStatus() {
		return externalPaidStatus;
	}

	public void setExternalPaidStatus(Long externalPaidStatus) {
		this.externalPaidStatus = externalPaidStatus;
	}

	public Long getTotalBondsCollected() {
		return totalBondsCollected;
	}

	public void setTotalBondsCollected(Long totalBondsCollected) {
		this.totalBondsCollected = totalBondsCollected;
	}

	public Long getTotalBondsCollectedByCompensation() {
		return totalBondsCollectedByCompensation;
	}

	public void setTotalBondsCollectedByCompensation(
			Long totalBondsCollectedByCompensation) {
		this.totalBondsCollectedByCompensation = totalBondsCollectedByCompensation;
	}

	public Boolean getIsCurrentBalanceReport() {
		return isCurrentBalanceReport;
	}

	public void setIsCurrentBalanceReport(Boolean isCurrentBalanceReport) {
		this.isCurrentBalanceReport = isCurrentBalanceReport;
	}

	public MunicipalBondStatus getCancelledBondStatus() {
		return cancelledBondStatus;
	}

	public void setcancelledBondStatus(MunicipalBondStatus cancelledBondStatus) {
		this.cancelledBondStatus = cancelledBondStatus;
	}

	public MunicipalBondStatus getFutureBondStatus() {
		return futureBondStatus;
	}

	public void setFutureBondStatus(MunicipalBondStatus futureBondStatus) {
		this.futureBondStatus = futureBondStatus;
	}

	public BigDecimal getTotalCollected() {
		return totalCollected;
	}

	public void setTotalCollected(BigDecimal totalCollected) {
		this.totalCollected = totalCollected;
	}

	public List<MunicipalBond> getReversedBonds() {
		return reversedBonds;
	}

	public void setReversedBonds(List<MunicipalBond> reversedBonds) {
		this.reversedBonds = reversedBonds;
	}

	public List<EntryTotalCollected> getTaxesFuture() {
		return taxesFuture;
	}

	public void setTaxesFuture(List<EntryTotalCollected> taxesFuture) {
		this.taxesFuture = taxesFuture;
	}

	public List<EntryTotalCollected> getTaxesPrepaid() {
		return taxesPrepaid;
	}

	public void setTaxesPrepaid(List<EntryTotalCollected> taxesPrepaid) {
		this.taxesPrepaid = taxesPrepaid;
	}

	public List<EntryTotalCollected> getTaxesFormalize() {
		return taxesFormalize;
	}

	public void setTaxesFormalize(List<EntryTotalCollected> taxesFormalize) {
		this.taxesFormalize = taxesFormalize;
	}

	public List<ReplacementAgreementDTO> getReplacementAgreements() {
		return replacementAgreements;
	}

	public void setReplacementAgreements(List<ReplacementAgreementDTO> replacementAgreements) {
		this.replacementAgreements = replacementAgreements;
	}
	
}