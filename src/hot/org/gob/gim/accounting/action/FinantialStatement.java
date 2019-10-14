package org.gob.gim.accounting.action;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletResponse;

import org.gob.gim.accounting.dto.AccountDetail;
import org.gob.gim.accounting.dto.AccountItem;
import org.gob.gim.accounting.dto.Criteria;
import org.gob.gim.accounting.dto.ReportFilter;
import org.gob.gim.accounting.dto.ReportType;
import org.gob.gim.accounting.service.FinantialService;
import org.gob.gim.common.DateUtils;
import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.view.OverduePortfolio;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.income.model.Account;
import ec.gob.gim.revenue.model.MunicipalBond;
import static ch.lambdaj.Lambda.*;
import static org.hamcrest.CoreMatchers.equalTo;

@Name("finantialStatement")
@Scope(ScopeType.CONVERSATION)
public class FinantialStatement extends EntityController {

	@In(create = true)
	UserSession userSession;

	private static final long serialVersionUID = 2171237729330726054L;

	private List<AccountItem> accountItems;

	private List<AccountItem> accountItemsOrnato;

	private List<AccountDetail> accountDetails;
	private AccountDetail detailTotal;

	private Criteria criteria = new Criteria();

	private String accountCode;

	private String accountName;

	private FiscalPeriod fiscalPeriod;

	private BigDecimal totalCredit;
	
	private BigDecimal totalResolutionOrnato;

	private BigDecimal totalDebit;

	private Account quotasAccount;
	
	//@macartuche
	private Account quotasAccountSubscription;
	//

	private Account interestAccount;

	private AccountItem quotasAccountItem;

	private SystemParameterService systemParameterService;
	
	//@tag carteraVencida
	//@date 2019-10-09
	//@author macartuche
	private BigDecimal totalDue;
	private List<Long> emittedStatus;
	private Integer totalRows;
	private List<MunicipalBond> detailBond;
	private Resident resident;
	private BigDecimal totalBond;

	@Observer("org.jboss.seam.postCreate.finantialStatement")
	public void initializeCriteria() {
		Date today = DateUtils.truncate(new Date());
		Query query = getEntityManager().createNamedQuery(
				"FiscalPeriod.findCurrent");
		query.setParameter("currentDate", today);
		FiscalPeriod currentPeriod = (FiscalPeriod) query.getSingleResult();
		criteria.setFiscalPeriodId(currentPeriod.getId());
		criteria.setAccountCode("");
		criteria.setStartDate(today);
		criteria.setEndDate(today);
		loadAccounts();
	}

	private void loadAccounts() {
		systemParameterService = ServiceLocator.getInstance().findResource(
				SystemParameterService.LOCAL_NAME);
		quotasAccount = systemParameterService.materialize(Account.class,
				"QUOTAS_ACCOUNT_ID");
		//@author macartuche
		quotasAccountSubscription = systemParameterService.materialize(Account.class,
				"QUOTAS_ACCOUNT_SUBSCRIPTION_ID");
		//fin cuenta
		
		interestAccount = systemParameterService.materialize(Account.class,
				"INTEREST_ACCOUNT_ID");
	}

	public void sumTotalDebit() {
		totalDebit = BigDecimal.ZERO;
		if (accountItems == null)
			return;	
		for (AccountItem ai : accountItems) {
			if (ai.getDebit() != null)
				totalDebit = totalDebit.add(ai.getDebit());
		}
	}

	private Long findAccountByDefinedParameter(String parameterName) {
		Long accountId = systemParameterService.findParameter(parameterName);
		return accountId;
	}

	public void sumTotalCredit() {
		totalCredit = BigDecimal.ZERO;
		Long accountId = findAccountByDefinedParameter("QUOTAS_ACCOUNT_ID");
		ReportType type = ReportType.QUOTAS_LIQUIDATION;
		//@macartuche
		if(criteria.getReportType() == ReportType.SUBSCRIPTION) {
			accountId = findAccountByDefinedParameter("QUOTAS_ACCOUNT_SUBSCRIPTION_ID");
			type = ReportType.SUBSCRIPTION;
		}
		//
		for (AccountItem ai : accountItems) {
			if (ai.getCredit() != null) {
				if (!(ai.getAccountId().equals(accountId) && criteria
						.getReportType().equals(type))) {
					totalCredit = totalCredit.add(ai.getCredit());
				}
			}
		}
	}

	public boolean isQuotasOrInterestAccount(Long accountId) {
		if (quotasAccount.getId().equals(accountId)
				|| interestAccount.getId().equals(accountId))
			return true;
		return false;
	}

	public void prePrintAction(boolean isForDetail) {
		loadFiscalPeriod(criteria.getFiscalPeriodId());
		if (!isForDetail)
			separateQuotasAccountItem();
	}

	private void loadFiscalPeriod(Long id) {
		Query query = getEntityManager().createNamedQuery(
				"FiscalPeriod.findById");
		query.setParameter("id", id);
		fiscalPeriod = (FiscalPeriod) query.getSingleResult();
	}

	private List<MunicipalBond> municipalBonds;

	private List<Long> paidBondStatuses;

	private void getPaidStatuses() {
		if (paidBondStatuses != null)
			return;
		paidBondStatuses = new ArrayList<Long>();
		paidBondStatuses.add((Long) systemParameterService
				.findParameter(IncomeService.PAID_BOND_STATUS));
		paidBondStatuses
				.add((Long) systemParameterService
						.findParameter(IncomeService.PAID_FROM_EXTERNAL_CHANNEL_BOND_STATUS));
	}

	private String dataForReport(List<AccountItem> list) {
		String data = list.toString();
		// Separar con una salto entre lineas en lugar de comas (,) a los
		// elementos del List
		data = data.replace(',', '\n');
		// Reemplazar el inicio del toString del List
		data = data.replace('[', ' ');
		// Reemplazar los * por las comas (,)
		data = data.replace('*', ',');
		// Reemplazar el final del toString del List
		data = data.replace(']', ' ');
		return data;
	}

	public void download() throws IOException {

		try {

			String data = dataForReport(accountItems);

			HttpServletResponse response = (HttpServletResponse) FacesContext
					.getCurrentInstance().getExternalContext().getResponse();

			response.reset();
			response.setContentType("text/csv");
			String reportName = Interpolator
					.instance()
					.interpolate(
							"#{messages['" + criteria.getReportType() + "']}",
							new Object[0]).replace(" ", "");
			String name = reportName + "-"
					+ DateUtils.formatDate(Calendar.getInstance().getTime())
					+ ".csv";
			response.addHeader("Content-Disposition", "attachment;filename="
					+ name);

			response.setContentLength(data.getBytes().length);

			OutputStream writer = response.getOutputStream();

			writer.write(data.getBytes());

			writer.flush();
			writer.close();

			FacesContext.getCurrentInstance().responseComplete();

		} catch (Exception e) {
			addFacesMessageFromResourceBundle("common.noResultsReturned " + e);
		}

	}

	public String generateReportByMunicipalBondInPaymentAgreement() {
		getPaidStatuses();
		loadFiscalPeriod(this.criteria.getFiscalPeriodId());
		municipalBonds = findMunicipalBondsInPaymentAgreementByStatus(paidBondStatuses);
		calculateMunicipalBondsValues();
		return "/accounting/report/ReportByMunicipalBond.xhtml";
	}
	
	//@author macartuche --abonos
	public String generateReportBySubscription() {
		getPaidStatuses();
		loadFiscalPeriod(this.criteria.getFiscalPeriodId());
		municipalBonds = findMunicipalBondsInSusbscriptionByStatus(paidBondStatuses);
		calculateMunicipalBondsValues();
		return "/accounting/report/ReportByMunicipalBond.xhtml";
	}
	//fin abonos
	
	
	
		
	public List<MunicipalBond> findMunicipalBondsInSusbscriptionByStatus(
			List<Long> statuses) {
		Query query = getEntityManager().createQuery("SELECT distinct mba.municipalbond FROM MunicipalbondAux mba "
				+ " join mba.deposit dep "
				+ " WHERE mba.municipalbond.liquidationDate BETWEEN :startDate AND :endDate AND mba.municipalbond.municipalBondStatus.id in(:statusIds)"
				+ " AND dep.status=:status "
				+ " and mba.typepayment=:type");
		query.setParameter("statusIds", statuses);
		query.setParameter("startDate", criteria.getStartDate());
		query.setParameter("endDate", criteria.getEndDate());
		query.setParameter("status", FinancialStatus.VALID);
		query.setParameter("type", "SUBSCRIPTION");
		return query.getResultList();
	}
	
	public List<MunicipalBond> findMunicipalBondsInPaymentAgreementByStatus(
			List<Long> statuses) {
		Query query = getEntityManager().createNamedQuery(
				"MunicipalBond.findInPaymentAgreementAndStatusIdAndDates");
		query.setParameter("statusIds", statuses);
		query.setParameter("startDate", criteria.getStartDate());
		query.setParameter("endDate", criteria.getEndDate());
		return query.getResultList();
	}

	private BigDecimal total;
	private BigDecimal taxes;
	private BigDecimal interest;
	private BigDecimal discounts;
	private BigDecimal surcharges;
	private BigDecimal values;

	private void calculateMunicipalBondsValues() {
		total = BigDecimal.ZERO;
		taxes = BigDecimal.ZERO;
		interest = BigDecimal.ZERO;
		discounts = BigDecimal.ZERO;
		surcharges = BigDecimal.ZERO;
		values = BigDecimal.ZERO;
		for (MunicipalBond mb : municipalBonds) {
			total = total.add(mb.getPaidTotal());
			taxes = taxes.add(mb.getTaxesTotal());
			interest = interest.add(mb.getInterestTotal());
			discounts = discounts.add(mb.getDiscount());
			surcharges = surcharges.add(mb.getSurcharge());
			values = values.add(mb.getValue());
		}
	}

	public void buildReport() {
		System.out.println("BUILDING FINANTIAL STATEMENT   Type : "
				+ criteria.getReportType());
		System.out.println("BUILDING FINANTIAL STATEMENT   From : "
				+ criteria.getStartDate());
		System.out.println("BUILDING FINANTIAL STATEMENT     To : "
				+ criteria.getEndDate());
		System.out.println("BUILDING FINANTIAL STATEMENT Period : "
				+ criteria.getFiscalPeriodId());
		System.out.println("BUILDING FINANTIAL STATEMENT Account: "
				+ criteria.getAccountCode());

		quotasAccountItem = null;
		Map<String, AccountItem> report;
		
		
		FinantialService finantialService = ServiceLocator.getInstance()
				.findResource(FinantialService.LOCAL_NAME);
		if (criteria.getReportType() == ReportType.DUE_PORTFOLIO) {
			report = finantialService.findDuePortfolioReport(criteria);
		} else {
			report = finantialService.findBalanceReport(criteria);
		}
		accountItems = new LinkedList<AccountItem>();
		accountItems.addAll(report.values());

		 
		Collections.sort(accountItems);
		sumTotalCredit();
		sumTotalDebit();
		
		totalResolutionOrnato = BigDecimal.ZERO;

		/*
		 * René Ortega Implementacion Memo: ML-DF-2015-1069 2015-12-15
		*/
		/*accountItemsOrnato = new ArrayList<AccountItem>();
		if (criteria.getReportType() == ReportType.BALANCE) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SystemParameterService.LOCAL_NAME);
			String finantialReportGADRoleName = systemParameterService
					.findParameter("ACCOUNT_CODE_RESOLUTION_HORNATO");
			AccountItem itemMultaComisariaHornato = selectFirst(
					accountItems,
					having(on(AccountItem.class).getAccountNumber(),
							equalTo(finantialReportGADRoleName)));
			
			int index = accountItems.indexOf(itemMultaComisariaHornato);

			
			AccountItem itemOrnatoActual = new AccountItem();
			itemOrnatoActual.setAccountName(itemMultaComisariaHornato.getAccountName());
			itemOrnatoActual.setAccountNumber(itemMultaComisariaHornato.getAccountNumber());
			itemOrnatoActual.setDebit(itemMultaComisariaHornato.getDebit());
			
			
			accountItemsOrnato.add(itemOrnatoActual);
			
			BigDecimal valorSumar = new BigDecimal(196513.82);
			valorSumar = valorSumar.setScale(2, RoundingMode.HALF_UP);
			
			AccountItem itemOrnato = new AccountItem();
			itemOrnato.setAccountName(itemMultaComisariaHornato.getAccountName());
			itemOrnato.setAccountNumber(itemMultaComisariaHornato.getAccountNumber());
			itemOrnato
					.setDebit(valorSumar);
			
			accountItemsOrnato.add(itemOrnato);
			
			BigDecimal valorTotal = itemMultaComisariaHornato.getDebit().add(itemOrnato.getDebit());
			valorTotal = valorTotal.setScale(2, RoundingMode.HALF_UP);
			
			accountItems.get(index).setDebit(valorTotal);
			
			totalResolutionOrnato = valorTotal;
		}*/

	}

	private void separateQuotasAccountItem() {
		if (quotasAccount == null || quotasAccountSubscription == null)
			return;
		//@macartuche
		if(criteria.getReportType() == ReportType.SUBSCRIPTION) {
			quotasAccount = quotasAccountSubscription;
		}//fin
		
		for (AccountItem ai : accountItems) {
			if (ai.getAccountNumber().equals(quotasAccount.getAccountCode())) {
				quotasAccountItem = ai;
				accountItems.remove(ai);
				return;
			}
		}
	}

	public void buildDetailedReport(Criteria criteria, Long accountId,
			String accountName, ReportFilter reportFilter) {
		System.out.println("LOADING DETAIL FOR ");
		System.out.println("Account code      " + accountCode);
		System.out.println("Account startDate " + criteria.getStartDate());
		System.out.println("Account endDate   " + criteria.getEndDate());
		FinantialService finantialService = ServiceLocator.getInstance()
				.findResource(FinantialService.LOCAL_NAME);
		Map<Long, AccountDetail> report = finantialService.findDetailReport(
				criteria, accountId, reportFilter);
		this.accountName = accountName;
		this.accountCode = accountCode;
		detailTotal = report.remove(0L);
		accountDetails = new LinkedList<AccountDetail>();
		accountDetails.addAll(report.values());
		Collections.sort(accountDetails);

	}

	@Observer("org.jboss.seam.postDestroy.finantialStatement")
	public void destroyComponent() {
		System.out.println("COMPONENT DESTROYED");
	}

	@Factory("reportTypes")
	public List<ReportType> loadReportTypes() {
		// return Arrays.asList(ReportType.values());
		systemParameterService = ServiceLocator.getInstance().findResource(
				SystemParameterService.LOCAL_NAME);
		String finantialReportGADRoleName = systemParameterService
				.findParameter("ROLE_NAME_FINANTIAL_REPORT_GAD_SYSTEM");
		String finantialReportEMAALEPRoleName = systemParameterService
				.findParameter("ROLE_NAME_FINANTIAL_REPORT_EMAALEP_SYSTEM");
		List<ReportType> listValues = new ArrayList<ReportType>();
		if (userSession.getUser().hasRole(finantialReportGADRoleName)) {
			for (ReportType reportType : ReportType.values()) {
				if (reportType.getRoleSystem().compareTo("GAD") == 0) {
					listValues.add(reportType);
				}
			}
		}

		if (userSession.getUser().hasRole(finantialReportEMAALEPRoleName)) {
			for (ReportType reportType : ReportType.values()) {
				if (reportType.getRoleSystem().compareTo("EMAALEP") == 0) {
					listValues.add(reportType);
				}
			}
		}

		return listValues;
	}

	@Factory("reportFilters")
	public List<ReportFilter> loadReportFilters() {
		List<ReportFilter> filters = new LinkedList<ReportFilter>();
		filters.add(ReportFilter.ALL);
		filters.add(ReportFilter.CURRENT);
		filters.add(ReportFilter.PREVIOUS);
		return filters;
		// return Arrays.asList(ReportFilter.values());
	}

	@SuppressWarnings("unchecked")
	@Factory("periods")
	public List<FiscalPeriod> findFiscalPeriods() {
		Query query = getEntityManager().createNamedQuery(
				"FiscalPeriod.findAll");
		List<FiscalPeriod> fiscalPeriods = query.getResultList();
		return fiscalPeriods;
	}

	public List<AccountItem> getAccountItems() {
		return accountItems;
	}

	public void setAccountItems(List<AccountItem> accountItems) {
		this.accountItems = accountItems;
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public void setCriteria(Criteria criteria) {
		this.criteria = criteria;
	}

	public List<AccountDetail> getAccountDetails() {
		return accountDetails;
	}

	public void setAccountDetails(List<AccountDetail> accountDetails) {
		this.accountDetails = accountDetails;
	}

	public AccountDetail getDetailTotal() {
		return detailTotal;
	}

	public void setDetailTotal(AccountDetail detailTotal) {
		this.detailTotal = detailTotal;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}

	public Account getQuotasAccount() {
		return quotasAccount;
	}

	public void setQuotasAccount(Account quotasAccount) {
		this.quotasAccount = quotasAccount;
	}

	public AccountItem getQuotasAccountItem() {
		return quotasAccountItem;
	}

	public void setQuotasAccountItem(AccountItem quotasAccountItem) {
		this.quotasAccountItem = quotasAccountItem;
	}

	public void setTotalCredit(BigDecimal totalCredit) {
		this.totalCredit = totalCredit;
	}

	public void setTotalDebit(BigDecimal totalDebit) {
		this.totalDebit = totalDebit;
	}

	public BigDecimal getTotalCredit() {
		return totalCredit;
	}

	public BigDecimal getTotalDebit() {
		return totalDebit;
	}

	public Account getInterestAccount() {
		return interestAccount;
	}

	public void setInterestAccount(Account interestAccount) {
		this.interestAccount = interestAccount;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getDiscounts() {
		return discounts;
	}

	public void setDiscounts(BigDecimal discounts) {
		this.discounts = discounts;
	}

	public BigDecimal getSurcharges() {
		return surcharges;
	}

	public void setSurcharges(BigDecimal surcharges) {
		this.surcharges = surcharges;
	}

	public BigDecimal getValues() {
		return values;
	}

	public void setValues(BigDecimal values) {
		this.values = values;
	}
	
	public BigDecimal getTotalResolutionOrnato() {
		return totalResolutionOrnato;
	}

	public void setTotalResolutionOrnato(BigDecimal totalResolutionOrnato) {
		this.totalResolutionOrnato = totalResolutionOrnato;
	}

	//@author macartuche
	//@tag carteraVencida
	//@date 2019-10-09
	private List<OverduePortfolio> overdues;


	  @SuppressWarnings("unchecked")
	public void generateOverduePortafolio()
	  {
	    this.total = BigDecimal.ZERO;
	    
	    FinantialService finantialService = (FinantialService)ServiceLocator.getInstance().findResource("/gim/FinantialService/local");
	    
	    this.emittedStatus = finantialService.getEmittedStatuses();
	    String sql = "SELECT res.id, res.identificationnumber, res.name, res.email, "
	    		+ "sum(paidTotal)    "
	    		+ "FROM municipalBond mb   "
	    		+ "join resident res on res.id=mb.resident_id    "
	    		+ "join entry ent on ent.id = mb.entry_id      "
	    		+ "WHERE    "
	    		+ "mb.expirationDate BETWEEN :startDate and :endDate  AND       "
	    		+ "mb.municipalBondStatus_id in (:status) AND "
	    		+ "mb.paidTotal > 0 ";
	    if (!this.criteria.getTextSearch().trim().isEmpty()) {
	      sql = sql + " AND (res.identificationnumber like :criteria OR lower(res.name) like :criteria) ";
	    }
	    if (!this.criteria.getAccountCode().trim().isEmpty()) {
	      sql = sql + " AND (ent.code like :code or lower(ent.name) like :code) ";
	    }
	    sql = sql + "   GROUP BY 1,2,3,4 ";
	    sql = sql + "   ORDER BY 5 desc";
	    
	    Query q = getEntityManager().createNativeQuery(sql);
	    q.setParameter("startDate", this.criteria.getStartDate());
	    q.setParameter("endDate", this.criteria.getEndDate());
	    q.setParameter("status", this.emittedStatus);
	    if (!this.criteria.getTextSearch().trim().isEmpty()) {
	      q.setParameter("criteria", "%" + this.criteria.getTextSearch().toLowerCase() + "%");
	    }
	    if (!this.criteria.getAccountCode().trim().isEmpty()) {
	      q.setParameter("code", "%" + this.criteria.getAccountCode().toLowerCase() + "%");
	    }
	    this.overdues = NativeQueryResultsMapper.map(q
	      .getResultList(), OverduePortfolio.class);
	    
	    this.totalRows = Integer.valueOf(this.overdues.size());
	    
	    String sqlTotal = "SELECT coalesce(sum(paidTotal),0)     FROM municipalBond mb   \tjoin resident res on res.id=mb.resident_id    \tjoin entry ent on ent.id = mb.entry_id      WHERE    \tmb.expirationDate BETWEEN :startDate and :endDate  AND        mb.municipalBondStatus_id in (:status) AND\t\tmb.paidTotal > 0 ";
	    if (!this.criteria.getTextSearch().trim().isEmpty()) {
	      sqlTotal = sqlTotal + " AND res.identificationnumber like :criteria ";
	    }
	    if (!this.criteria.getAccountCode().trim().isEmpty()) {
	      sqlTotal = sqlTotal + " AND ent.code like :code ";
	    }
	    q = getEntityManager().createNativeQuery(sqlTotal);
	    q.setParameter("startDate", this.criteria.getStartDate());
	    q.setParameter("endDate", this.criteria.getEndDate());
	    q.setParameter("status", this.emittedStatus);
	    if (!this.criteria.getTextSearch().trim().isEmpty()) {
	      q.setParameter("criteria", "%" + this.criteria.getTextSearch() + "%");
	    }
	    if (!this.criteria.getAccountCode().trim().isEmpty()) {
	      q.setParameter("code", "%" + this.criteria.getAccountCode() + "%");
	    }
	    this.totalDue = ((BigDecimal)q.getSingleResult());
	  }
	  
	  public void detailOverdue(Long residentId)
	  {
	    Query q = getEntityManager().createQuery("SELECT mb from MunicipalBond mb WHERE mb.expirationDate between :startDate and :endDate and mb.municipalBondStatus.id in (:status) and mb.resident.id=:resident ORDER BY mb.expirationDate");
	    
	    q.setParameter("startDate", this.criteria.getStartDate());
	    q.setParameter("endDate", this.criteria.getEndDate());
	    q.setParameter("status", this.emittedStatus);
	    q.setParameter("resident", residentId);
	    
	    this.detailBond = q.getResultList();
	    this.totalBond = BigDecimal.ZERO;
	    for (MunicipalBond bond : this.detailBond) {
	      this.totalBond = this.totalBond.add(bond.getPaidTotal());
	    }
	    q = getEntityManager().createQuery("Select r from Resident r where r.id=:id");
	    q.setParameter("id", residentId);
	    this.resident = ((Resident)q.getSingleResult());
	  }
	public List<OverduePortfolio> getOverdues() {
		return overdues;
	}

	public void setOverdues(List<OverduePortfolio> overdues) {
		this.overdues = overdues;
	}

	public List<AccountItem> getAccountItemsOrnato() {
		return accountItemsOrnato;
	}

	public void setAccountItemsOrnato(List<AccountItem> accountItemsOrnato) {
		this.accountItemsOrnato = accountItemsOrnato;
	}

	public Account getQuotasAccountSubscription() {
		return quotasAccountSubscription;
	}

	public void setQuotasAccountSubscription(Account quotasAccountSubscription) {
		this.quotasAccountSubscription = quotasAccountSubscription;
	}

	 public BigDecimal getTotalDue()
	  {
	    return this.totalDue;
	  }
	  
	  public void setTotalDue(BigDecimal totalDue)
	  {
	    this.totalDue = totalDue;
	  }
	  
	  public List<MunicipalBond> getDetailBond()
	  {
	    return this.detailBond;
	  }
	  
	  public void setDetailBond(List<MunicipalBond> detailBond)
	  {
	    this.detailBond = detailBond;
	  }
	  
	  public Resident getResident()
	  {
	    return this.resident;
	  }
	  
	  public void setResident(Resident resident)
	  {
	    this.resident = resident;
	  }
	  
	  public BigDecimal getTotalBond()
	  {
	    return this.totalBond;
	  }
	  
	  public void setTotalBond(BigDecimal totalBond)
	  {
	    this.totalBond = totalBond;
	  }
	  
	  public Integer getTotalRows()
	  {
	    return this.totalRows;
	  }
	  
	  public void setTotalRows(Integer totalRows)
	  {
	    this.totalRows = totalRows;
	  }
}