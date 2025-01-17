package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Query;

import org.gob.gim.common.DateUtils;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.Branch;
import ec.gob.gim.income.model.CashRecord;
import ec.gob.gim.income.model.Deposit;
import ec.gob.gim.income.model.Money;
import ec.gob.gim.income.model.MoneyType;
import ec.gob.gim.income.model.Payment;
import ec.gob.gim.income.model.PaymentFractionView;
import ec.gob.gim.income.model.PaymentType;
import ec.gob.gim.income.model.Till;
import ec.gob.gim.income.model.TillInstitutionDetail;
import ec.gob.gim.income.model.TillPermission;
import ec.gob.gim.income.model.TillPermissionDetail;
import ec.gob.gim.revenue.model.MunicipalBond;

//@Name("tillPermissionHomeWs")
public class TillPermissionHomeWs extends EntityHome<TillPermission> {

    private static final long serialVersionUID = 1L;

    @In(create = true)
    UserSession userSession;

    @In(create = true)
    WorkdayHomeWs workdayHomeWsf;

    public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
    public static String ENTRIES_EMAALEP_LIST = "ENTRIES_EMAALEP_LIST";
    public static String NAME_INSTITUTION_GAD_FOR_CLOSING_REPORT = "NAME_INSTITUTION_GAD_FOR_CLOSING_REPORT";
    public static String NAME_INSTITUTION_EMAALEP_FOR_CLOSING_REPORT = "NAME_INSTITUTION_EMAALEP_FOR_CLOSING_REPORT";

    private SystemParameterService systemParameterService;

    private boolean isTillReport = false;
    private boolean readyForPrint = false;
    private boolean hasPreviousOpeningTime = true;
    private boolean hasPreviousClosingTime = true;
    private boolean isFirstTime = true;
    private boolean closingTill = false;
    private boolean printWithDetail = true;

    private Long totalServedPeople;
    private Long totalTransactions;
    private Long paidStatus;
    private Long paidStatusExternalChannel;
    private List<Long> paidStatuses = new ArrayList<Long>();
    private Long agreementStatus;
    private BigDecimal transferCollected;
    private BigDecimal creditNoteCollected;
    private BigDecimal totalCashCollected;
    private BigDecimal totalBillsCollected;
    private BigDecimal totalCash;
    private BigDecimal totalCoinsCollected;
    private BigDecimal totalCollected;
    private BigDecimal totalCollectedByTill;

    private Branch branch;
    private Till till;
    private TillPermission tillPermission;

    private List<CashRecord> bills;
    private List<Deposit> depositsForAgreement;
    private List<Deposit> deposits;
    private List<Branch> branches;
    private List<CashRecord> coins;
    private List<Payment> payments;
    private List<TillPermissionDetail> tillPermissionsDetails;
    private List<Till> tillList;
    private List<TillInstitutionDetail> tillInstitutionDetails = new ArrayList<TillInstitutionDetail>();
    private List<MunicipalBond> municipalBonds = new ArrayList<MunicipalBond>();
    private List<MunicipalBond> municipalBondsForCompensation = new ArrayList<MunicipalBond>();

    private Map<String, BigDecimal> fractionValues;

    private List<PaymentFractionView> paymentsForDetail;

    private Date startDate;
    private Date endDate;

    private List<Deposit> ReversedDeposits = new ArrayList<Deposit>();

    public void changeTillPermission(TillPermission t) {
        tillPermission = t;
    }

    public List<Till> getTillList() {
        return tillList;
    }

    public void setTillList(List<Till> tillList) {
        this.tillList = tillList;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public WorkdayHomeWs getWorkdayHome() {
        return workdayHomeWsf;
    }

    public void setWorkdayHome(WorkdayHomeWs workdayHome) {
        this.workdayHomeWsf = workdayHome;
    }

    public String updateTillPermission() {
        String message = Interpolator.instance().interpolate("#{messages['tillPermission.forcedClosingTill']}", userSession.getPerson().getName(), tillPermission.getObservation());
        message = message.replace("{0}", userSession.getPerson().getName());
        message = message.replace("{1}", tillPermission.getObservation());
        tillPermission.setClosingTime(new Date());
        tillPermission.setObservation(message);
        setInstance(tillPermission);
        return update();
    }

    public String updateOpeningTillPermission() {
        String message = Interpolator.instance().interpolate("#{messages['tillPermission.OpeningTillBank']}", userSession.getPerson().getName());
        message = message.replace("{0}", userSession.getPerson().getName());
        tillPermission.setOpeningTime(new Date());
        tillPermission.setInitialBalance(BigDecimal.ZERO);
        setInstance(tillPermission);
        return update();
    }

    public void setTillPermissionId(Long id) {
        setId(id);
    }

    public Long getTillPermissionId() {
        return (Long) getId();
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        if (isFirstTime) {
            if (userSession.getTillPermission() != null) {
                setTillPermissionId(userSession.getTillPermission().getId());
            }
            getInstance();
            findTotalInstitutionCollected(getInstance());
            Calendar now = Calendar.getInstance();
            if (getInstance().getOpeningTime() == null) {
                getInstance().setOpeningTime(now.getTime());
                hasPreviousOpeningTime = false;
            }
            if (getInstance().getClosingTime() == null && hasPreviousOpeningTime) {
                generateClosingTillReport(this.getInstance().getPerson().getId(), now.getTime());
                closingTill = true;
                hasPreviousClosingTime = false;
                isFirstTime = false;
                loadReversedDepositsByCashierIdAndDate(getInstance().getPerson().getId(), this.getInstance().getWorkday().getDate(), this.getInstance().getWorkday().getDate());
            }

            if (getInstance().getClosingTime() != null) {
                generateClosingTillReport(this.getInstance().getPerson().getId(), this.getInstance().getWorkday().getDate());
                chargeCashRecords();
            }
            isFirstTime = false;
        }
    }

    private void chargeCashRecords() {
        bills = new ArrayList<CashRecord>();
        coins = new ArrayList<CashRecord>();
        for (CashRecord cr : getInstance().getCashRecord()) {
            if (cr.getMoney().getMoneyType().equals(MoneyType.BILL)) {
                bills.add(cr);
            } else {
                coins.add(cr);
            }
        }
        calculateTotalBills();
        calculateTotalCoins();
        calculateTotalCash();
    }

    private void loadValues(Date d) {
        isTillReport = true;
        generateClosingTillReport(this.getInstance().getPerson().getId(), d);
        chargeCashRecords();
    }

    public BigDecimal globalTotal(TillPermissionDetail t) {
        BigDecimal total = BigDecimal.ZERO;
        if (t == null) {
            return total;
        }
        total = total.add(t.getTotalCollected()).add(t.getTotalCompensationCollected()).add(t.getTotalCreditNoteCollected());
        return total;
    }

    public boolean sameDates() {
        if (startDate.equals(endDate)) {
            return true;
        }
        return false;
    }

    /**
     * Generar reporte por cajeros entre fechas
     */
    public void generateCashiersTillReport() {
        // getEntityManager().clear();
        tillPermissionsDetails = null;
        Query query = null;
        if (branch == null) {
            query = getEntityManager().createNamedQuery("TillPermission.findBetweenDates");
        } else {
            if (till == null) {
                query = getEntityManager().createNamedQuery("TillPermission.findByBranchBetweenDates");
            } else {
                query = getEntityManager().createNamedQuery("TillPermission.findByBranchTillBetweenDates");
                query.setParameter("tillId", till.getId());
            }
            query.setParameter("branchId", branch.getId());
        }
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        List<TillPermission> tillPermissions = query.getResultList();

        findTotalCollected(tillPermissions);

        if (startDate != null && startDate.equals(endDate) && tillPermissions.size() == 1) {
            this.setInstance(tillPermissions.get(0));
            getInstance().setPerson(tillPermissions.get(0).getPerson());
            loadValues(startDate);
        }

        if (tillPermissions.size() == 1) {
            tillPermission = tillPermissions.get(0);
            findTotalInstitutionCollected(tillPermission);
        }

        totalServedPeople = getTotalServedPeopleTillPermissions();

    }

    /**
     * Número de personas atendidas
     *
     * @return Long
     */
    public Long getTotalServedPeopleTillPermissions() {
        Long x = 0L;
        for (TillPermissionDetail t : tillPermissionsDetails) {
            if (t.getServedPeople() != null) {
                x = x + t.getServedPeople();
            }
        }
        return x;
    }

    /**
     * Número de transacciones
     *
     * @return Long
     */
    public Long getTotalTransactionsTillPermissions() {
        Long x = 0L;
        if (tillPermissionsDetails == null) {
            return x;
        }
        for (TillPermissionDetail t : tillPermissionsDetails) {
            if (t.getTransactionsNumber() != null) {
                x = x + t.getTransactionsNumber();
            }
        }
        return x;
    }

    /**
     * Total de valores de aperturas de caja
     *
     * @return BigDecimal
     */
    public BigDecimal getTotalInitialBalanceTillPermissions() {
        BigDecimal x = BigDecimal.ZERO;
        for (TillPermissionDetail t : tillPermissionsDetails) {
            if (t.getInitialBalance() != null) {
                x = x.add(t.getInitialBalance());
            }
        }
        return x;
    }

    public BigDecimal getTotalCollectedTillPermissions() {
        BigDecimal x = BigDecimal.ZERO;
        for (TillPermissionDetail t : tillPermissionsDetails) {
            if (t.getTotalCollected() != null) {
                x = x.add(t.getTotalCollected());
            }
        }
        return x;
    }

    /**
     * Maneja el tamaño de las columnas para TillReport
     *
     * @return String
     */
    public String getColumnsSize() {
        if (till != null) {
            return "1 3 1 1 1 1";
        }
        return "1 1 3 1 1 1 1";
    }

    /**
     * Maneja el número de columnas para TillReport
     *
     * @return String
     */
    public String getColumnsNumber() {
        if (till != null) {
            return "6";
        }
        return "7";
    }

    public BigDecimal totalTillPermissionDetailCollected() {
        BigDecimal total = BigDecimal.ZERO;
        if (tillPermissionsDetails == null) {
            return total;
        }
        for (TillPermissionDetail t : tillPermissionsDetails) {
            total = total.add(t.getTotalCollected());
            total = total.add(t.getTotalCompensationCollected());
            total = total.add(t.getTotalCreditNoteCollected());
        }

        return total;
    }

    public List<Branch> findBranches() {
        if (branches == null) {
            Query query = getEntityManager().createNamedQuery("Branch.findAll");
            branches = query.getResultList();
        }
        return branches != null ? branches : new ArrayList<Branch>();
    }

    public List<Till> loadTills() {
        if (branch == null) {
            return new ArrayList<Till>();
        }
        return orderTillsByNumber(branch.getTills());
    }

    private List<Till> orderTillsByNumber(List<Till> list) {
        List<Integer> numbers = new ArrayList<Integer>();
        for (Till t : list) {
            numbers.add(t.getNumber());
        }
        Collections.sort(numbers);
        List<Till> result = new ArrayList<Till>();
        for (Integer n : numbers) {
            for (Till t : list) {
                if (t.getNumber() == n) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    /**
     * Total recaudado por tillPermission en cajas entre fechas
     *
     * @param tillPermissions
     */
    private void findTotalCollected(List<TillPermission> tillPermissions) {
        tillPermissionsDetails = new ArrayList<TillPermissionDetail>();
        String sql = "select count(p) from Payment p "
                + "where p.date Between :startDate and :endDate "
                + "and p.cashier.id = :cashierId "
                + "and p.status = 'VALID' ";
        Query query = getEntityManager().createQuery(sql);
        for (TillPermission t : tillPermissions) {
            TillPermissionDetail td = createTillPermissionDetail(t);
            query.setParameter("startDate", t.getWorkday().getDate());
            query.setParameter("endDate", t.getWorkday().getDate());
            query.setParameter("cashierId", t.getPerson().getId());
            //payments = query.getResultList();
            Long totalServed = (Long) query.getSingleResult();
            td.setTillPermission(t);
            td.setBranch(t.getTill().getBranch().getName());
            td.setTotalCollected(totalPayments(t));
            td.setTotalCompensationCollected(transferCollected);
            td.setTotalCreditNoteCollected(creditNoteCollected);
            td.setTransactionsNumber(getTotalTransactions(t));
            td.setTillNumber(t.getTill().getNumber());
            td.setServedPeople(totalServed == null ? 0 : totalServed.intValue());
            tillPermissionsDetails.add(td);
        }

    }

    /**
     *
     */
    public List<Till> loadTillsWs() {
        tillList = new ArrayList<Till>();

        Query query = getEntityManager().createNamedQuery("Till.findIsTillBank");

        List<Till> totalServed = query.getResultList();
        return totalServed;
    }

    /**
     * Totales de depósito por tillPermission en cajas entre fechas por
     * institution
     *
     * @param tillPermissions
     */
//	private void findTotalInstitutionCollected(List<TillPermission> tillPermissions) {
    private void findTotalInstitutionCollected(TillPermission t) {
        tillInstitutionDetails.clear();
        if (systemParameterService == null) {
            systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
        }
        String strNameGAD = systemParameterService.findParameter(NAME_INSTITUTION_GAD_FOR_CLOSING_REPORT);
        String strNameEMAALEP = systemParameterService.findParameter(NAME_INSTITUTION_EMAALEP_FOR_CLOSING_REPORT);

        BigDecimal partial = BigDecimal.ZERO;
        BigDecimal totalTaxes = BigDecimal.ZERO;
        BigDecimal totalCommissions = BigDecimal.ZERO;
        BigDecimal totalPaymentAgreements = BigDecimal.ZERO;
        String sql = "select count(p) from Payment p where p.id=1";
        Query query = getEntityManager().createQuery(sql);
//		for (TillPermission t : tillPermissions) {

        //Valores para GAD Municipal de Loja
        TillInstitutionDetail tid = new TillInstitutionDetail();
        tid.setName(strNameGAD);
        tid.setDate(t.getWorkday().getDate());
        tid.setTillNumber(t.getTill().getNumber());
        tid.setTillPermission(t);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(true, false, "i.total", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalAccounts(partial);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, false, "mb.interest", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalInterests(partial);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, false, "mb.surcharge", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalSurcharges(partial);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, false, "mb.discount", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalDiscounts(partial);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, false, "mb.taxestotal", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalTaxes(partial);
        totalTaxes = tid.getTotalTaxes();

        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, false, "d.value", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "not null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalPaymentAgreements(partial);
        totalPaymentAgreements = tid.getTotalPaymentAgreements();

        tid.setSubTotal(tid.getTotalAccounts().add(tid.getTotalInterests()).add(tid.getTotalSurcharges()).subtract(tid.getTotalDiscounts()));
        tillInstitutionDetails.add(tid);
//			System.out.println("RRRRRRRR "+"name:"+tid.getName());
//			System.out.println("RRRRRRRR "+"acco:"+tid.getTotalAccounts());
//			System.out.println("RRRRRRRR "+"inte:"+tid.getTotalInterests());
//			System.out.println("RRRRRRRR "+"reca:"+tid.getTotalSurcharges());
//			System.out.println("RRRRRRRR "+"impu:"+tid.getTotalTaxes());
//			System.out.println("RRRRRRRR "+"desc:"+tid.getTotalDiscounts());
//			System.out.println("RRRRRRRR "+"agre:"+tid.getTotalPaymentAgreements());
//			System.out.println("RRRRRRRR "+"subt:"+tid.getSubTotal());
//			System.out.println("RRRRRRRR "+"comi:"+tid.getCommission());
//			System.out.println("RRRRRRRR "+"ttax:"+totalTaxes);
        System.out.println("RRRRRRRR " + "tota:" + tid.getTotal());

        //Valores para EMAAL-EP
        tid = new TillInstitutionDetail();
        tid.setName(strNameEMAALEP);
        tid.setDate(t.getWorkday().getDate());
        tid.setTillNumber(t.getTill().getNumber());
        tid.setTillPermission(t);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(true, true, "i.total", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalAccounts(partial);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, true, "mb.interest", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalInterests(partial);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, true, "mb.surcharge", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalSurcharges(partial);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, true, "mb.discount", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalDiscounts(partial);
        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, true, "mb.taxestotal", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalTaxes(partial);
        totalTaxes = totalTaxes.add(tid.getTotalTaxes());

        query = getEntityManager().createNativeQuery(getQueryForTotalInstitution(false, true, "d.value", t.getWorkday().getDate(), t.getWorkday().getDate(), t.getPerson().getId(), "not null"));
        partial = (BigDecimal) ((query.getSingleResult() == null) ? BigDecimal.ZERO : query.getSingleResult());
        tid.setTotalPaymentAgreements(partial);
        totalPaymentAgreements = totalPaymentAgreements.add(tid.getTotalPaymentAgreements());

        tid.setSubTotal(tid.getTotalAccounts().add(tid.getTotalInterests()).add(tid.getTotalSurcharges()).subtract(tid.getTotalDiscounts()));
        tid.setCommission(tid.getSubTotal().multiply(new BigDecimal(-0.05)).setScale(2, RoundingMode.HALF_UP));
        totalCommissions = tid.getCommission();
        tid.setTotal(tid.getSubTotal().add(tid.getCommission()));
        tillInstitutionDetails.add(tid);
//			System.out.println("RRRRRRRR "+"name:"+tid.getName());
//			System.out.println("RRRRRRRR "+"acco:"+tid.getTotalAccounts());
//			System.out.println("RRRRRRRR "+"inte:"+tid.getTotalInterests());
//			System.out.println("RRRRRRRR "+"reca:"+tid.getTotalSurcharges());
//			System.out.println("RRRRRRRR "+"impu:"+tid.getTotalTaxes());
//			System.out.println("RRRRRRRR "+"desc:"+tid.getTotalDiscounts());
//			System.out.println("RRRRRRRR "+"agre:"+tid.getTotalPaymentAgreements());
//			System.out.println("RRRRRRRR "+"subt:"+tid.getSubTotal());
//			System.out.println("RRRRRRRR "+"comi:"+tid.getCommission());
//			System.out.println("RRRRRRRR "+"ttax:"+totalTaxes);
        System.out.println("RRRRRRRR " + "tota:" + tid.getTotal());
//			
//			query.setParameter("startDate", t.getWorkday().getDate());
//			query.setParameter("endDate", t.getWorkday().getDate());
//			query.setParameter("cashierId", t.getPerson().getId());
//			//payments = query.getResultList();
//			Long totalServed = (Long)query.getSingleResult();
//			td.setTillPermission(t);
//			td.setBranch(t.getTill().getBranch().getName());
//			td.setTotalCollected(totalPayments(t));
//			td.setTotalCompensationCollected(transferCollected);
//			td.setTotalCreditNoteCollected(creditNoteCollected);
//			td.setTransactionsNumber(getTotalTransactions(t));
//			td.setTillNumber(t.getTill().getNumber());
//			td.setServedPeople(totalServed == null ? 0: totalServed.intValue());

//		}
        //Ajuste de Valores para GAD Municipal de Loja comisiones e impuestos fiscales
        tid = tillInstitutionDetails.get(0);
        tid.setCommission(totalCommissions.multiply(new BigDecimal(-1)));
        tid.setSubTotal(tid.getSubTotal().add(totalTaxes));
        tid.setTotal(tid.getSubTotal().add(tid.getCommission()).add(totalPaymentAgreements));
        tid.setTotalPaymentAgreements(totalPaymentAgreements);
//			System.out.println("RRRRRRRR "+"name:"+tid.getName());
//			System.out.println("RRRRRRRR "+"acco:"+tid.getTotalAccounts());
//			System.out.println("RRRRRRRR "+"inte:"+tid.getTotalInterests());
//			System.out.println("RRRRRRRR "+"reca:"+tid.getTotalSurcharges());
//			System.out.println("RRRRRRRR "+"impu:"+tid.getTotalTaxes());
//			System.out.println("RRRRRRRR "+"desc:"+tid.getTotalDiscounts());
//			System.out.println("RRRRRRRR "+"agre:"+tid.getTotalPaymentAgreements());
//			System.out.println("RRRRRRRR "+"subt:"+tid.getSubTotal());
//			System.out.println("RRRRRRRR "+"comi:"+tid.getCommission());
//			System.out.println("RRRRRRRR "+"ttax:"+totalTaxes);
        System.out.println("RRRRRRRR " + "tota:" + tid.getTotal());
    }

    public BigDecimal getTotalByInstitutions() {
        BigDecimal total = BigDecimal.ZERO;
        for (TillInstitutionDetail t : tillInstitutionDetails) {
            total = total.add(t.getTotal());
        }
        return total;
    }

    /**
     * retorna la cadena sql para las diferentes consultas y obtener los totales
     * de deposito por institucion
     *
     * @param isSumItem
     * @param isInList
     * @param field
     */
    private String getQueryForTotalInstitution(boolean isSumItem, boolean isInList, String field, Date startDate, Date endDate, Long cashierId, String agreements) {
        String sql = "";
        if (systemParameterService == null) {
            systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
        }
        String strListEmaalEp = systemParameterService.findParameter(ENTRIES_EMAALEP_LIST);
        if (isSumItem) {
            sql = "select sum(" + field + ") "
                    + "from payment p inner join deposit d "
                    + "on p.id = d.payment_id inner join item i "
                    + "on d.municipalbond_id = i.municipalbond_id "
                    + "inner join municipalbond mb on mb.id = i.municipalbond_id "
                    + "where p.date between '" + startDate + "' and '" + endDate + "' "
                    + "and mb.paymentagreement_id is " + agreements + " "
                    + "and p.cashier_id = " + cashierId + " and p.status = 'VALID' ";
            if (isInList) {
                sql = sql + "and i.entry_id in (" + strListEmaalEp + ")";
            } else {
                sql = sql + "and i.entry_id not in (" + strListEmaalEp + ")";
            }
        } else {
            sql = "select sum(" + field + ") "
                    + "from payment p inner join deposit d "
                    + "on p.id = d.payment_id inner join municipalbond mb "
                    + "on mb.id = d.municipalbond_id "
                    + "where p.date between '" + startDate + "' and '" + endDate + "' "
                    + "and mb.paymentagreement_id is " + agreements + " "
                    + "and p.cashier_id = " + cashierId + " and p.status = 'VALID' ";
            if (isInList) {
                sql = sql + "and mb.entry_id in (" + strListEmaalEp + ")";
            } else {
                sql = sql + "and mb.entry_id not in (" + strListEmaalEp + ")";
            }
        }
        return sql;
    }

    /**
     * ColSpan en GenerateTillReport.xhtml
     *
     * @return String
     */
    public String findColSpan() {
        if (branch == null) {
            return "11";
        }
        if (till == null) {
            return "10";
        }
        return "9";
    }

    public void loadDefaultDates() {
        if (isFirstTime) {
            Calendar c = Calendar.getInstance();
            startDate = c.getTime();
            endDate = c.getTime();
            isFirstTime = false;
        }
    }

    /**
     * Total de transacciones por TillPermission
     *
     * @param t TillPermission
     * @return Long
     */
    private Long getTotalTransactions(TillPermission t) {
        String sql = "select count(d.id) from Payment p "
                + "left join p.deposits d "
                + "where p.date Between :startDate and :endDate "
                + "and p.cashier.id = :cashierId ";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("startDate", t.getWorkday().getDate());
        query.setParameter("endDate", t.getWorkday().getDate());
        query.setParameter("cashierId", t.getPerson().getId());
        Long aux = (Long) query.getSingleResult();
        aux = aux == null ? 0L : aux;

        return aux;
    }

    /**
     * Total recaudado en el TillPermission
     *
     * @param t TillPermission
     * @return BigDecimal
     */
    private BigDecimal totalCollected(TillPermission t) {
        String sql = "select sum(p.value) from Payment p "
                + "where p.date Between :startDate and :endDate "
                + "and p.cashier.id = :cashierId "
                + "and p.status = 'VALID'";
        Query query = getEntityManager().createQuery(sql);
        query.setParameter("startDate", t.getWorkday().getDate());
        query.setParameter("endDate", t.getWorkday().getDate());
        query.setParameter("cashierId", t.getPerson().getId());
        BigDecimal total = (BigDecimal) query.getSingleResult();
        total = total == null ? BigDecimal.ZERO : total;
        totalCollected = total;
        return total;
    }

    /**
     * Total recaudado en el TillPermission por tipo de pago
     *
     * @param t TillPermission
     * @return BigDecimal
     */
    private BigDecimal totalTillPermissionByPaymentType(TillPermission t, PaymentType paymentType) {
        String sql = "select sum(pf.paidAmount) from PaymentFraction pf "
                + "left join pf.payment p "
                + "where p.date Between :startDate and :endDate "
                + "and p.cashier.id = :cashierId "
                + "and p.status = 'VALID' "
                + "and pf.paymentType = :paymentType";

        Query query = getEntityManager().createQuery(sql);
        query.setParameter("startDate", t.getWorkday().getDate());
        query.setParameter("endDate", t.getWorkday().getDate());
        query.setParameter("cashierId", t.getPerson().getId());
        query.setParameter("paymentType", paymentType);
        BigDecimal res = (BigDecimal) query.getSingleResult();
        res = res == null ? BigDecimal.ZERO : res;
        transferCollected = res;
        return res;
    }

    private TillPermissionDetail createTillPermissionDetail(TillPermission t) {
        TillPermissionDetail td = new TillPermissionDetail();
        td.setDate(t.getWorkday().getDate());
        td.setInitialBalance(t.getInitialBalance());
        td.setInChargeName(t.getPerson().getName());
        td.setOpened(false);
        td.setTillBank(t.getTill().isTillBank());
        if (t.getClosingTime() == null && t.getOpeningTime() != null) {
            td.setOpened(true);
        }
        return td;
    }

    private void generateClosingTillReport(Long cashierId, Date d) {
        setTillPermission(getInstance());
        totalDetailCollected();
    }

    @SuppressWarnings("unchecked")
    private List<Money> findDenominationMoney(MoneyType mt) {
        Query query = this.getEntityManager().createNamedQuery("Money.findbyMoneyTypeOrderByValue");
        query.setParameter("moneyType", mt);
        return query.getResultList();
    }

    private CashRecord createCashRecord(Money m) {
        CashRecord cr = new CashRecord();
        cr.setMoney(m);
        return cr;
    }

    /**
     * Calcula el total por tipo de efectivo para el cierre de caja y actualiza
     * el total de efectivo cr.getAmount() --> cantidad de tipo de cash (5
     * billetes) cr.getMoney().getValue() --> valor del tipo de billete (billete
     * de 1 dolar, valor 1)
     *
     * @param cr
     */
    public void calculateTotalCashRecord(CashRecord cr) {
        if (cr.getAmount() != null) {
            cr.setTotal(cr.getMoney().getValue().multiply(new BigDecimal(cr.getAmount())));
            calculateTotalBills();
            calculateTotalCoins();
        } else {
            cr.setTotal(BigDecimal.ZERO);
            calculateTotalBills();
            calculateTotalCoins();
        }
    }

    //@Observer("org.jboss.seam.postCreate.tillPermissionHome")
    public void initialize() {
        loadBills();
        loadCoins();
    }

    /**
     * Crea los DenominationMoney de tipo BILL para hacer el cierre de caja y
     * detallar el efectivo
     *
     * @return List<CashRecord>
     */
    private List<CashRecord> loadBills() {
        if (bills == null) {
            bills = new ArrayList<CashRecord>();
            for (Money m : findDenominationMoney(MoneyType.BILL)) {
                bills.add(createCashRecord(m));
            }
        }
        return bills;
    }

    /**
     * Crea los DenominationMoney de tipo COIN para hacer el cierre de caja y
     * detallar el efectivo
     *
     * @return List<CashRecord>
     */
    private List<CashRecord> loadCoins() {
        if (coins == null) {
            coins = new ArrayList<CashRecord>();
            for (Money m : findDenominationMoney(MoneyType.COIN)) {
                coins.add(createCashRecord(m));
            }
        }
        return coins;
    }

    public boolean isWired() {
        return true;
    }

    /**
     * Antes de actualizar agrega el detalle de lo recaudado (CashRecord)
     */
    @Override
    public String update() {
        if (coins != null) {
            for (CashRecord c : coins) {
                if (c.getTotal() != null && c.getTotal().compareTo(BigDecimal.ZERO) == 1) {
                    getInstance().add(c);
                }
            }
        }
        if (bills != null) {
            for (CashRecord c : bills) {
                if (c.getTotal() != null && c.getTotal().compareTo(BigDecimal.ZERO) == 1) {
                    getInstance().add(c);
                }
            }
        }

        if (closingTill) {
            Calendar ca = Calendar.getInstance();
            getInstance().setClosingTime(ca.getTime());
        }
        String aux = update1();
        if (aux.equals("updated")) {
            userSession.setTillPermission(this.getInstance());
            readyForPrint = true;
        }
        return aux;
    }

    @Override
    public TillPermission getInstance() {
//        try{
//        return super.getInstance(); 
//        }catch(IllegalStateException ex){
//             joinTransaction();
        getEntityManager().joinTransaction();
        if (instance == null) {
            initInstance();
        }
        return instance;
//        }        
    }

    @Override
    public TillPermission find() {
//        try{
//        return super.find();
//         }catch(IllegalStateException ex){
        Query query = getPersistenceContext().createNamedQuery(
                "select t from TillPermission t "
                + "where "
                + "t.id= :id ORDER BY b.id");
        query.setParameter("id", this.getId());
        TillPermission tillPermission = (TillPermission) query.getSingleResult();
        return tillPermission;
//         }
    }

    public String update1() {
//       try{
//           return super.update();
//       }catch(java.lang.IllegalStateException ex){  "2015-10-28 09:12:46.31"
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.getDefault());
        if (tillPermission.getClosingTime() == null) {
            String sql = "UPDATE TillPermission tp \n"
                    + "SET tp.openingTime = '" + formatter.format(tillPermission.getOpeningTime()) + "'\n"
                    + ", tp.initialBalance = '" + tillPermission.getInitialBalance() + "'\n"
                    + "WHERE\n"
                    + "tp.till.id IN (" + tillPermission.getTill().getId() + ") "
                    + "AND tp.workday.id IN (" + tillPermission.getWorkday().getId() + ")";
            Query query = getEntityManager().createQuery(sql);

            int res = query.executeUpdate();
        } else {
            String sql = "UPDATE TillPermission tp \n"
                    + "SET tp.closingTime = '" + formatter.format(tillPermission.getClosingTime()) + "'\n"
                    + ", tp.observation = '" + tillPermission.getObservation() + "'\n"
                    + "WHERE\n"
                    + "tp.till.id IN (" + tillPermission.getTill().getId() + ") "
                    + "AND tp.workday.id IN (" + tillPermission.getWorkday().getId() + ")";
            Query query = getEntityManager().createQuery(sql);

            int res = query.executeUpdate();
        }

//       }
        return "updated";
    }

    /**
     * Calcula el total recaudado en Billetes y actualiza el total de efectivo
     * (billetes y monedas)
     *
     */
    public void calculateTotalBills() {
        totalBillsCollected = BigDecimal.ZERO;
        if (bills != null) {
            for (CashRecord c : bills) {
                c.calculateTotal();
                if (c.getTotal() != null) {
                    totalBillsCollected = totalBillsCollected.add(c.getTotal());
                }
            }
        }
        calculateTotalCash();
    }

    /**
     * Calcula el total recaudado en Monedas y actualiza el total de efectivo
     * (billetes y monedas)
     *
     */
    public void calculateTotalCoins() {
        totalCoinsCollected = BigDecimal.ZERO;
        if (coins != null) {
            for (CashRecord c : coins) {
                c.calculateTotal();
                if (c.getTotal() != null) {
                    totalCoinsCollected = totalCoinsCollected.add(c.getTotal());
                }
            }
        }
        calculateTotalCash();
    }

    private void calculateTotalCash() {
        totalCash = BigDecimal.ZERO;
        if (totalBillsCollected != null && totalCoinsCollected != null) {
            totalCash = totalCash.add(totalBillsCollected).add(totalCoinsCollected);
        }
    }

    public TillPermission getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public BigDecimal getMunicipalBondsTotal() {
        BigDecimal res = BigDecimal.ZERO;
        for (MunicipalBond m : municipalBonds) {
            if (m.getPaidTotal() != null) {
                res = res.add(m.getPaidTotal());
            }
        }
        return res;
    }

    public BigDecimal getMunicipalBondValues() {
        BigDecimal res = BigDecimal.ZERO;
        for (MunicipalBond m : municipalBonds) {
            if (m.getValue() != null) {
                res = res.add(m.getValue());
            }
        }
        return res;
    }

    public BigDecimal getDepositsAgreementValues() {
        BigDecimal res = BigDecimal.ZERO;
        for (Deposit d : depositsForAgreement) {
            if (d.getCapital() != null) {
                res = res.add(d.getCapital());
            }
        }
        return res;
    }

    public BigDecimal getDepositsAgreementTotal() {
        BigDecimal res = BigDecimal.ZERO;
        for (Deposit d : depositsForAgreement) {
            if (d.getValue() != null) {
                res = res.add(d.getValue());
            }
        }
        return res;
    }

    public BigDecimal getDepositsAgreementInterests() {
        BigDecimal res = BigDecimal.ZERO;
        for (Deposit d : depositsForAgreement) {
            if (d.getInterest() != null) {
                res = res.add(d.getInterest());
            }
        }
        return res;
    }

    public BigDecimal getDepositsAgreementTaxes() {
        BigDecimal res = BigDecimal.ZERO;
        for (Deposit d : depositsForAgreement) {
            if (d.getPaidTaxes() != null) {
                res = res.add(d.getPaidTaxes());
            }
        }
        return res;
    }

    public BigDecimal getMunicipalBondDiscounts() {
        BigDecimal res = BigDecimal.ZERO;
        for (MunicipalBond m : municipalBonds) {
            if (m.getDiscount() != null) {
                res = res.add(m.getDiscount());
            }
        }
        return res;
    }

    public BigDecimal getMunicipalBondSurcharges() {
        BigDecimal res = BigDecimal.ZERO;
        for (MunicipalBond m : municipalBonds) {
            if (m.getSurcharge() != null) {
                res = res.add(m.getSurcharge());
            }
        }
        return res;
    }

    public BigDecimal getMunicipalBondInterests() {
        BigDecimal res = BigDecimal.ZERO;
        for (MunicipalBond m : municipalBonds) {
            if (m.getInterestTotal() != null) {
                res = res.add(m.getInterestTotal());
            }
        }
        return res;
    }

    public BigDecimal getMunicipalBondTaxes() {
        BigDecimal res = BigDecimal.ZERO;
        for (MunicipalBond m : municipalBonds) {
            if (m.getTaxesTotal() != null) {
                res = res.add(m.getTaxesTotal());
            }
        }
        return res;
    }

    /**
     * Detalle de pagos por tipo de pago, cajero y fecha
     *
     * @param paymentType tipo de pago
     * @param cashierId cajero
     * @param date fecha
     * @return List<PaymentFractionView>
     */
    private List<PaymentFractionView> findByTypeAndCashierAndDate(PaymentType paymentType, Long cashierId, Date date) {
        Query query = getEntityManager().createNamedQuery("PaymentFraction.findForViewByDateAndPaymentTypeByCashier");
        query.setParameter("startDate", date);
        query.setParameter("endDate", date);
        query.setParameter("paymentType", paymentType);
        query.setParameter("cashierId", cashierId);
        return query.getResultList();
    }

    /**
     * Total recaudado, fractionValues contiene los tipos de pago con el valor
     * recaudado en cada uno de los casos
     */
    private void totalDetailCollected() {
        if (isFirstTime || isTillReport) {
            Date date = null;
            if (startDate != null) {
                date = startDate;
            } else {
                date = DateUtils.getTruncatedInstance(getInstance().getOpeningTime()).getTime();
            }
            totalCollectedByTill(date);
            fractionValues = new HashMap<String, BigDecimal>();
            List<Object[]> results = totalCollectedPaymentFractionByTill(date);
            for (Object[] row : results) {
                fractionValues.put(row[0].toString(), (BigDecimal) row[1]);
            }
            totalServedPeopleByCashier(getInstance().getPerson().getId(), date);
            totalTransactionByCashier(getInstance().getPerson().getId(), date);
        }
    }

    public PaymentType[] orderPaymentType() {
        return PaymentType.getOrderPaymentTypes();
    }

    private void totalServedPeopleByCashier(Long cashierId, Date date) {
        Query query = getEntityManager().createNamedQuery("Payment.countBetweenDatesByCashier");
        query.setParameter("startDate", date);
        query.setParameter("endDate", date);
        query.setParameter("cashierId", cashierId);
        Long res = (Long) query.getSingleResult();
        totalServedPeople = res == null ? 0L : res;
    }

    private void totalTransactionByCashier(Long cashierId, Date date) {
        Query query = getEntityManager().createNamedQuery("Payment.countDepositsNumberByCashier");
        query.setParameter("startDate", date);
        query.setParameter("endDate", date);
        query.setParameter("cashierId", cashierId);
        Long res = (Long) query.getSingleResult();
        totalTransactions = res == null ? 0L : res;
    }

    /**
     * Total recaudado en una fecha agrupado por tipo de pago
     *
     * @param date
     * @return List<Object[]>
     */
    private List<Object[]> totalCollectedPaymentFractionByTill(Date date) {
        Query query = getEntityManager().createNamedQuery("PaymentFraction.SumTotalBetweenDatesByCashierAndValids");
        query.setParameter("startDate", date);
        query.setParameter("endDate", date);
        query.setParameter("cashierId", getInstance().getPerson().getId());
        return query.getResultList();
    }

    /**
     * Total de payments en la fecha
     *
     * @param date
     */
    private void totalCollectedByTill(Date date) {
        totalCollectedByTill = BigDecimal.ZERO;
        Query query = getEntityManager().createNamedQuery("Payment.SumTotalBetweenDatesByCashier");
        query.setParameter("startDate", date);
        query.setParameter("endDate", date);
        query.setParameter("cashierId", getInstance().getPerson().getId());
        totalCollectedByTill = (BigDecimal) query.getSingleResult();
    }

    public List<Deposit> getDepositsForAgreement() {
        return depositsForAgreement;
    }

    public void setDepositsForAgreement(List<Deposit> depositsForAgreement) {
        this.depositsForAgreement = depositsForAgreement;
    }

    private void loadMunicipalBondStatus() {
        if (systemParameterService == null) {
            systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
        }
        paidStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID");
        paidStatusExternalChannel = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_PAID_FROM_EXTERNAL_CHANNEL");
        agreementStatus = systemParameterService.findParameter("MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
        paidStatuses.clear();
        paidStatuses.add(paidStatus);
        paidStatuses.add(paidStatusExternalChannel);
    }

    private List<MunicipalBond> getMunicipalBondsByCashier(Long cashierId, Long municipalBondStatusId, Date date) {
        Query query = getEntityManager().createNamedQuery("Payment.findMunicipalBondBetweenDatesByPaymentAndCashier");
        query.setParameter("startDate", date);
        query.setParameter("endDate", date);
        query.setParameter("cashierId", cashierId);
        query.setParameter("statusId", municipalBondStatusId);
        return query.getResultList();
    }

    private List<MunicipalBond> getMunicipalBondsByCashier(Long cashierId, List<Long> paidStatuses, Date date) {
        Query query = getEntityManager().createNamedQuery("Payment.findMunicipalBondBetweenDatesByPaidStatusesAndCashier");
        query.setParameter("startDate", date);
        query.setParameter("endDate", date);
        query.setParameter("cashierId", cashierId);
        query.setParameter("paidStatuses", paidStatuses);
        return query.getResultList();
    }

    private List<Deposit> getDepositsFromBondsInAgreementByCashier(Long cashierId, Date date) {
        Query query = getEntityManager().createNamedQuery("Deposit.findDepositsFromBondsInAgreementFromPayments");
        query.setParameter("startDate", date);
        query.setParameter("endDate", date);
        query.setParameter("cashierId", cashierId);
        return query.getResultList();
    }

    /**
     * Recupera municipalbonds y deposits ordenados por entry y se genera el
     * árbol de resumen en caso de que sea null
     */
    public void orderByEntry() {
        if (paidStatus == null || agreementStatus == null) {
            loadMunicipalBondStatus();
        }
        Date date = DateUtils.getTruncatedInstance(getInstance().getOpeningTime()).getTime();
//		municipalBonds = getMunicipalBondsByCashier(getInstance().getPerson().getId(), paidStatus, date);
        municipalBonds = getMunicipalBondsByCashier(getInstance().getPerson().getId(), paidStatuses, date);
        depositsForAgreement = getDepositsFromBondsInAgreementByCashier(getInstance().getPerson().getId(), date);
        if (workdayHomeWsf.getRootNode() == null) {
            workdayHomeWsf.loadStatuses();
            workdayHomeWsf.setDeposits(deposits);
            workdayHomeWsf.setDetailDifferentYears(false);
            workdayHomeWsf.generateSummaryTree();
        }
    }

    /**
     * Total de pagos
     *
     * @return BigDecimal
     */
    public BigDecimal totalPaymentsForDetail() {
        BigDecimal aux = BigDecimal.ZERO;
        if (getPaymentsForDetail() == null) {
            return aux;
        }
        for (PaymentFractionView p : getPaymentsForDetail()) {
            aux = aux.add(p.getPaidAmount());
        }
        return aux;
    }

    /**
     * Cambiar PaymentsForDetail para presentar en el detalle de cierre de caja
     * por tipo de pago
     *
     * @param paymentType
     */
    public void detailByPaymentType(PaymentType paymentType) {
        Date date = DateUtils.getTruncatedInstance(getInstance().getOpeningTime()).getTime();
        List<PaymentFractionView> list = findByTypeAndCashierAndDate(paymentType, getInstance().getPerson().getId(), date);
        setPaymentsForDetail(list);
    }

    public boolean isHasPreviousClosingTime() {
        return hasPreviousClosingTime;
    }

    public void setHasPreviousClosingTime(boolean hasPreviousClosingTime) {
        this.hasPreviousClosingTime = hasPreviousClosingTime;
    }

    public boolean isHasPreviousOpeningTime() {
        return hasPreviousOpeningTime;
    }

    public void setHasPreviousOpeningTime(boolean hasPreviousOpeningTime) {
        this.hasPreviousOpeningTime = hasPreviousOpeningTime;
    }

    public void setDeposits(List<Deposit> deposits) {
        this.deposits = deposits;
    }

    public List<Deposit> getDeposits() {
        return deposits;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public boolean isReadyForPrint() {
        return readyForPrint;
    }

    public void setReadyForPrint(boolean readyForPrint) {
        this.readyForPrint = readyForPrint;
    }

    public void setPrintWithDetail(boolean printWithDetail) {
        this.printWithDetail = printWithDetail;
    }

    public boolean isPrintWithDetail() {
        return printWithDetail;
    }

    public List<MunicipalBond> getMunicipalBonds() {
        return municipalBonds;
    }

    public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
        this.municipalBonds = municipalBonds;
    }

    public TillPermission getTillPermission() {
        return tillPermission;
    }

    public void setTillPermission(TillPermission tillPermission) {
        this.tillPermission = tillPermission;
    }

    public BigDecimal getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(BigDecimal totalCash) {
        this.totalCash = totalCash;
    }

    public BigDecimal getTotalBillsCollected() {
        return totalBillsCollected;
    }

    public void setTotalBillsCollected(BigDecimal totalBillsCollected) {
        this.totalBillsCollected = totalBillsCollected;
    }

    public BigDecimal getTotalCoinsCollected() {
        return totalCoinsCollected;
    }

    public void setTotalCoinsCollected(BigDecimal totalCoinsCollected) {
        this.totalCoinsCollected = totalCoinsCollected;
    }

    public List<CashRecord> getBills() {
        return bills;
    }

    public void setBills(List<CashRecord> bills) {
        this.bills = bills;
    }

    public List<CashRecord> getCoins() {
        return coins;
    }

    public void setCoins(List<CashRecord> coins) {
        this.coins = coins;
    }

    public boolean isTillReport() {
        return isTillReport;
    }

    public void setTillReport(boolean isTillReport) {
        this.isTillReport = isTillReport;
    }

    public SystemParameterService getSystemParameterService() {
        return systemParameterService;
    }

    public void setSystemParameterService(SystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    public List<MunicipalBond> getMunicipalBondsForCompensation() {
        return municipalBondsForCompensation;
    }

    public void setMunicipalBondsForCompensation(List<MunicipalBond> municipalBondsForCompensation) {
        this.municipalBondsForCompensation = municipalBondsForCompensation;
    }

    public List<TillPermissionDetail> getTillPermissionsDetails() {
        return tillPermissionsDetails;
    }

    public void setTillInstitutionsDetails(List<TillInstitutionDetail> tillInstitutionDetails) {
        this.tillInstitutionDetails = tillInstitutionDetails;
    }

    public List<TillInstitutionDetail> getTillInstitutionDetails() {
        return tillInstitutionDetails;
    }

    public void setTillPermissionsDetails(List<TillPermissionDetail> tillPermissionsDetails) {
        this.tillPermissionsDetails = tillPermissionsDetails;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Till getTill() {
        return till;
    }

    public void setTill(Till till) {
        this.till = till;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }

    public BigDecimal getTotalCollected() {
        return totalCollected;
    }

    public void setTotalCollected(BigDecimal totalCollected) {
        this.totalCollected = totalCollected;
    }

    public Long getTotalServedPeople() {
        return totalServedPeople;
    }

    public void setTotalServedPeople(Long totalServedPeople) {
        this.totalServedPeople = totalServedPeople;
    }

    public BigDecimal getTotalCollectedByTill() {
        return totalCollectedByTill;
    }

    public void setTotalCollectedByTill(BigDecimal totalCollectedByTill) {
        this.totalCollectedByTill = totalCollectedByTill;
    }

    public List<PaymentFractionView> getPaymentsForDetail() {
        return paymentsForDetail;
    }

    public void setPaymentsForDetail(List<PaymentFractionView> paymentsForDetail) {
        this.paymentsForDetail = paymentsForDetail;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public Long getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(Long paidStatus) {
        this.paidStatus = paidStatus;
    }

    public Long getAgreementStatus() {
        return agreementStatus;
    }

    public void setAgreementStatus(Long agreementStatus) {
        this.agreementStatus = agreementStatus;
    }

    public Map<String, BigDecimal> getFractionValues() {
        return fractionValues;
    }

    public void setFractionValues(Map<String, BigDecimal> fractionValues) {
        this.fractionValues = fractionValues;
    }

    public BigDecimal getCreditNoteCollected() {
        return creditNoteCollected;
    }

    public void setCreditNoteCollected(BigDecimal creditNoteCollected) {
        this.creditNoteCollected = creditNoteCollected;
    }

    public BigDecimal totalPayments(TillPermission t) {
        transferCollected = totalTillPermissionByPaymentType(t, PaymentType.TRANSFER);
        creditNoteCollected = totalTillPermissionByPaymentType(t, PaymentType.CREDIT_NOTE);
        totalCashCollected = totalCollected(t).subtract(transferCollected).subtract(creditNoteCollected);
        return totalCashCollected;
    }

    public BigDecimal getTransferCollected() {
        return transferCollected;
    }

    public void setTransferCollected(BigDecimal transferCollected) {
        this.transferCollected = transferCollected;
    }

    public BigDecimal getTotalCashCollected() {
        return totalCashCollected;
    }

    public void setTotalCashCollected(BigDecimal totalCashCollected) {
        this.totalCashCollected = totalCashCollected;
    }

    public List<Deposit> getReversedDeposits() {
        return ReversedDeposits;
    }

    public void setReversedDeposits(List<Deposit> reversedDeposits) {
        ReversedDeposits = reversedDeposits;
    }

    @SuppressWarnings("unchecked")
    public void loadReversedDepositsByCashierIdAndDate(Long cashierId, Date startDate, Date endDate) {
        System.out.println("Ingreso a loadReversedDepositsByCashierIdAndDay");
        ReversedDeposits.clear();
        Query query = getEntityManager().createNamedQuery("Deposit.findReversedDepositsByCashierIdAndDate");
        query.setParameter("cashierId", cashierId);
        query.setParameter("paymentStartDate", startDate);
        query.setParameter("paymentEndDate", endDate);
        ReversedDeposits = query.getResultList();
    }

    public Long getPaidStatusExternalChannel() {
        return paidStatusExternalChannel;
    }

    public void setPaidStatusExternalChannel(Long paidStatusExternalChannel) {
        this.paidStatusExternalChannel = paidStatusExternalChannel;
    }

    public List<Long> getPaidStatuses() {
        return paidStatuses;
    }

    public void setPaidStatuses(List<Long> paidStatuses) {
        this.paidStatuses = paidStatuses;
    }

    public TillPermission findTp(Object id, Till l) {
        Query query = getEntityManager().createNamedQuery("TillPermission.findWorkId");
        query.setParameter("workday", id);
        int v = l.getId().intValue();
        query.setParameter("till", l.getId());
        return (TillPermission) query.getSingleResult();
    }

}
