package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.NativeQueryResultsMapper;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.dto.BondDownStatus;
import org.gob.gim.income.view.StatusChangeItem;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.StatusChange;
import ec.gob.gim.revenue.model.DTO.MunicipalBondErrorsCorrectionDTO;

@Name("statusChangeHome")
public class StatusChangeHome extends EntityHome<StatusChange> {

    private static final long serialVersionUID = 1L;

    @In
    UserSession userSession;

    private String resolution;

    private boolean allBondsSelected;

    private StatusChange statusChange;
    private List<StatusChangeItem> statusChangeListGroup = new ArrayList<StatusChangeItem>();
    private List<MunicipalBond> bonds = new LinkedList<MunicipalBond>();
    private List<BondDownStatus> bondsDownStatus = new ArrayList<BondDownStatus>();
    private List<BondDownStatus> bondsDownStatusByAccount = new ArrayList<BondDownStatus>();
    private BondDownStatus bondDownStatus;
    private BigDecimal pendingTotal;

    private Boolean viewByResolution = true;
    private Boolean firstTime = true;

    private BigDecimal total = BigDecimal.ZERO;

    private java.util.Date startDate = new Date();
    private java.util.Date endDate = new Date();

    // para traspaso de ogligaciones
    private String criteria;
    private String identificationNumberPrevious;
    private String identificationNumberCurrent;
    private Long residentId;
    private List<Resident> residents;

    private Resident previousResident;
    private Resident currentResident;
    @In
    EntityManager entityManager;
    
    @In(create = true)   
    private WorkdayHome workdayHome;

    private boolean selectable;

    private static MunicipalBondStatus municipalBondStatus;

    private static MunicipalBondStatus inAgreementStatus;

    private ArrayList<StatusChange> statusChangeLst;

    public BigDecimal getPendingTotal() {
        return pendingTotal;
    }

    public void setPendingTotal(BigDecimal pendingTotal) {
        this.pendingTotal = pendingTotal;
    }
        
    public ArrayList<StatusChange> getStatusChangeLst() {
        return statusChangeLst;
    }

    public void setStatusChangeLst(ArrayList<StatusChange> statusChangeLst) {
        this.statusChangeLst = statusChangeLst;
    }

    public static MunicipalBondStatus getMunicipalBondStatus() {
        return municipalBondStatus;
    }

    public static void setMunicipalBondStatus(MunicipalBondStatus municipalBondStatus) {
        StatusChangeHome.municipalBondStatus = municipalBondStatus;
    }

    public static MunicipalBondStatus getInAgreementStatus() {
        return inAgreementStatus;
    }

    public static void setInAgreementStatus(MunicipalBondStatus inAgreementStatus) {
        StatusChangeHome.inAgreementStatus = inAgreementStatus;
    }

    public StatusChangeHome() {
//        this.selectable = false;
        this.allBondsSelected = false;
    }

    public List<StatusChangeItem> fillMunicipalBondItems(List<MunicipalBond> municipalBonds) {

        StatusChangeItem root = new StatusChangeItem(null);
        statusChangeLst = new ArrayList<StatusChange>();
        pendingTotal= BigDecimal.ZERO;
        for (MunicipalBond municipalBond : municipalBonds) {
            String entryId = municipalBond.getEntry().getId().toString();
            StatusChangeItem item = root.findNode(entryId, municipalBond);

            String groupingCode = municipalBond.getGroupingCode();
            StatusChangeItem groupingItem = item.findNode(groupingCode, municipalBond);
//            if (municipalBond.getMunicipalBondStatus().equals(municipalBondStatus)) {
//                municipalBond.setTotalCancelled(BigDecimal.ZERO);
//            }
//            if (municipalBond.getMunicipalBondStatus().equals(inAgreementStatus)) {
//                municipalBond.setTotalCancelled(sumTotalCancelled(municipalBond));
//            }
            StatusChangeItem mbi = new StatusChangeItem(municipalBond);
            StatusChange sc = new StatusChange();
            sc.setChangeResident(Boolean.FALSE);
            sc.setMunicipalBond(municipalBond);
            mbi.setStatusChange(sc);
            groupingItem.add(mbi);
            statusChangeLst.add(sc);
            pendingTotal = pendingTotal.add(sc.getMunicipalBond().getValue());
        }

        int i = 0;
        for (StatusChangeItem mbi : root.getMunicipalBondItems("g")) {
            //System.out.println("call ->> " + i + "size >>>" + mbi.getChildren().size());
            mbi.calculateTotals(findPendingStatus(), findInAgreementStatus());            
            i++;

        }
        return root.getMunicipalBondItems("-");
    }

    private MunicipalBondStatus findPendingStatus() {
        SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
        return systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PENDING");
    }

    private MunicipalBondStatus findInAgreementStatus() {
        SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
        return systemParameterService.materialize(MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_IN_PAYMENT_AGREEMENT");
    }

    public Boolean getSelectable() {
        return selectable;
    }

    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }

    public void selectAllItems() {
//        if (!allBondsSelected) {
//            allBondsSelected = Boolean.TRUE;
//        } else {
//            allBondsSelected = Boolean.FALSE;
//        }
        for (StatusChangeItem mbi : statusChangeListGroup) {
            selectAllItemsR(mbi,"-");
        }
    }

    public void selectAllItemsR(StatusChangeItem mbi, String o) {        
        if (mbi.getChildren() != null && mbi.getChildren().size() > 0) {
            for (StatusChangeItem mbi2 : mbi.getChildren().values()) {
                selectAllItemsR(mbi2,o+"-");                
            }
        }
        //System.out.println(o+":"+mbi.toString());
        mbi.setIsSelectedNoR(allBondsSelected ? Boolean.TRUE : Boolean.FALSE);
    }

    public Boolean getAllBondsSelected() {
        return allBondsSelected;
    }

    public void setAllBondsSelected(Boolean allBondsSelected) {
        this.allBondsSelected = allBondsSelected;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public List<MunicipalBond> getBonds() {
        return bonds;
    }

    public void setBonds(List<MunicipalBond> bonds) {
        this.bonds = bonds;
    }

    public void updateTotalResolution(BigDecimal total) {
        statusChange.setTotalForResolution(total);
    }

    public StatusChange getStatusChange() {
        return statusChange;
    }

    public void setStatusChange(StatusChange statusChange) {
        this.statusChange = statusChange;
    }

    public List<StatusChangeItem> getStatusChangeListGroup() {
        return statusChangeListGroup;
    }

    public void setStatusChangeListGroup(
            List<StatusChangeItem> statusChangeListGroup) {
        this.statusChangeListGroup = statusChangeListGroup;
    }

    public List<BondDownStatus> getBondsDownStatus() {
        return bondsDownStatus;
    }

    public void setBondsDownStatus(List<BondDownStatus> bondsDownStatus) {
        this.bondsDownStatus = bondsDownStatus;
    }

    public List<BondDownStatus> getBondsDownStatusByAccount() {
        return bondsDownStatusByAccount;
    }

    public void setBondsDownStatusByAccount(
            List<BondDownStatus> bondsDownStatusByAccount) {
        this.bondsDownStatusByAccount = bondsDownStatusByAccount;
    }

    public BondDownStatus getBondDownStatus() {
        return bondDownStatus;
    }

    public void setBondDownStatus(BondDownStatus bondDownStatus) {
        this.bondDownStatus = bondDownStatus;
    }

    public Boolean getViewByResolution() {
        return viewByResolution;
    }

    public void setViewByResolution(Boolean viewByResolution) {
        this.viewByResolution = viewByResolution;
    }

    public void cambiarViewByResolution(Boolean status) {
        this.viewByResolution = status;
        if (status) {
            loadBondDownStatus();
        } else {
            loadBondDownStatusByDates();
        }

    }

    public Boolean getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        this.firstTime = firstTime;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @SuppressWarnings("unchecked")
    public List<StatusChange> findAllStatusChangeGrouped() {
        Query query = this.getEntityManager().createNamedQuery(
                "StatusChange.findAllGrouped");
        query.setParameter("startDateFiscalPeriod", userSession
                .getFiscalPeriod().getStartDate());
        query.setParameter("endDateFiscalPeriod", userSession.getFiscalPeriod()
                .getEndDate());
        this.statusChangeLst = (ArrayList<StatusChange>) query.getResultList();
        return this.statusChangeLst;
    }

    @SuppressWarnings("unchecked")
    public void initBondDownStatus() {
        if (firstTime) {
            bondsDownStatus.clear();
            Query query = this.getEntityManager().createNamedQuery(
                    "StatusChange.findAllGrouped");
            query.setParameter("startDateFiscalPeriod", userSession
                    .getFiscalPeriod().getStartDate());
            query.setParameter("endDateFiscalPeriod", userSession
                    .getFiscalPeriod().getEndDate());
            bondsDownStatus = query.getResultList();
            firstTime = Boolean.FALSE;
        }
    }

    @SuppressWarnings("unchecked")
    public void loadBondDownStatus() {
        bondsDownStatus.clear();
        Query query = this.getEntityManager().createNamedQuery(
                "StatusChange.findAllGrouped");
        query.setParameter("startDateFiscalPeriod", userSession
                .getFiscalPeriod().getStartDate());
        query.setParameter("endDateFiscalPeriod", userSession.getFiscalPeriod()
                .getEndDate());
        bondsDownStatus = query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public void loadBondDownStatusByDates() {
        //System.out.println("ingreso a loadBondDownStatusByDates");
        total = BigDecimal.ZERO;
        bondsDownStatus.clear();
        Query query = this.getEntityManager().createNamedQuery(
                "StatusChange.findAllGroupedByDates");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        bondsDownStatus = query.getResultList();

        for (BondDownStatus row : bondsDownStatus) {
            total = total.add(row.getResolutionTotal());
        }
    }

    @SuppressWarnings("unchecked")
    public void findBondDownStatus() {
        //System.out.println("ingreso a findBondDownStatus");
        total = BigDecimal.ZERO;
        bondsDownStatus.clear();
        Query query = this.getEntityManager().createNamedQuery(
                "StatusChange.findAllGroupedByResolution");
        query.setParameter("resolution", resolution);
        query.setParameter("startDateFiscalPeriod", userSession
                .getFiscalPeriod().getStartDate());
        query.setParameter("endDateFiscalPeriod", userSession.getFiscalPeriod()
                .getEndDate());
        bondsDownStatus = query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public void findBondsDownStatusByResolution(BondDownStatus bondDownStatus) {
        //System.out.println("ingreso a findBondsDownStatusByResolution");
        total = BigDecimal.ZERO;
        bonds.clear();
        bondsDownStatusByAccount.clear();
        this.bondDownStatus = bondDownStatus;
        Query query = this.getEntityManager().createNamedQuery(
                "StatusChange.findBondsDownStatusByResolution");
        query.setParameter("resolution", bondDownStatus.getResolutionNumber());
        bonds = query.getResultList();
        for (MunicipalBond row : bonds) {
            // total =
            // total.add(row.getValue()).add(row.getTaxesTotal()).subtract(row.getDiscount());
            total = total.add(row.getValue()).add(row.getTaxesTotal());
        }
    }

    @SuppressWarnings("unchecked")
    public void findBondsDownStatusByDates() {
        //System.out                .println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr ingreso a findBondsDownStatusByDates");
        total = BigDecimal.ZERO;
        bonds.clear();
        bondsDownStatusByAccount.clear();
        //System.out                .println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr clear bonds");
        Query query = this.getEntityManager().createNamedQuery(
                "StatusChange.findBondsDownStatusByDates");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        //System.out                .println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr prepare query");
        bonds = query.getResultList();
//		int maxResults = 1000;
//		int initialResults = -1;
//		int totalResults=500000;
//		query.setMaxResults(maxResults);
//		while (initialResults <= totalResults) {
//			System.out.println("contador: "+initialResults+1);
//			query.setFirstResult(initialResults+1);
//			bonds.addAll(query.getResultList());
//			initialResults += maxResults;
//		}
        //System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr excecute query");
        for (MunicipalBond row : bonds) {
            // total =
            // total.add(row.getValue()).add(row.getTaxesTotal()).subtract(row.getDiscount());
            total = total.add(row.getValue()).add(row.getTaxesTotal());
        }
        //System.out                .println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr exit procedure");
    }

    @SuppressWarnings("unchecked")
    public void findDownBondsWithAccountByResolution(
            BondDownStatus bondDownStatus) {
        total = BigDecimal.ZERO;
        bonds.clear();
        bondsDownStatusByAccount.clear();
        this.bondDownStatus = bondDownStatus;
        String sqlNative = "select ac.accountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
                + "left join gimprod.municipalbond mb "
                + "on mb.id=sc.municipalbond_id "
                + "left join gimprod.item i "
                + "on i.municipalbond_id=sc.municipalbond_id "
                + "left join gimprod.entry e "
                + "on e.id = i.entry_id "
                + "left join gimprod.account ac "
                + "on ac.id = e.account_id "
                + "where sc.municipalbondstatus_id = 9 "
                + "and sc.explanation = '"
                + bondDownStatus.getResolutionNumber()
                + "'"
                + "and mb.emisionDate >= '"
                + dateToStr(userSession.getFiscalPeriod().getStartDate())
                + "'"
                + "group by ac.accountcode, ac.accountname "
                + "order by ac.accountCode";

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery(sqlNative);
        List<Object[]> results = query.getResultList();

        for (Object[] row : results) {
            BondDownStatus parte = new BondDownStatus((String) row[0],
                    (String) row[1], (BigDecimal) row[2]);
            bondsDownStatusByAccount.add(parte);
            total = total.add((BigDecimal) row[2]);
        }
        results.clear();

        sqlNative = "select ac.previousyearsaccountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
                + "left join gimprod.municipalbond mb "
                + "on mb.id=sc.municipalbond_id "
                + "left join gimprod.item i "
                + "on i.municipalbond_id=sc.municipalbond_id "
                + "left join gimprod.entry e "
                + "on e.id = i.entry_id "
                + "left join gimprod.account ac "
                + "on ac.id = e.account_id "
                + "where sc.municipalbondstatus_id = 9 "
                + "and sc.explanation = '"
                + bondDownStatus.getResolutionNumber()
                + "'"
                + "and mb.emisionDate < '"
                + dateToStr(userSession.getFiscalPeriod().getStartDate())
                + "'"
                + "group by ac.previousyearsaccountcode, ac.accountname "
                + "order by ac.previousyearsaccountcode";

        query = entityManager.createNativeQuery(sqlNative);
        results = query.getResultList();

        for (Object[] row : results) {
            BondDownStatus parte = new BondDownStatus((String) row[0],
                    (String) row[1], (BigDecimal) row[2]);
            bondsDownStatusByAccount.add(parte);
            if ((BigDecimal) row[2] != null) {
                total = total.add((BigDecimal) row[2]);
            }
        }

        // results.clear();
        // sqlNative =
        // "select ac.accountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
        // +
        // "left join gimprod.municipalbond mb " +
        // "on mb.id=sc.municipalbond_id " +
        // "left join gimprod.item i " +
        // "on i.discountedBond_id=sc.municipalbond_id " +
        // "left join gimprod.entry e " +
        // "on e.id = i.entry_id " +
        // "left join gimprod.account ac " +
        // "on ac.id = e.account_id " +
        // "where sc.municipalbondstatus_id = 9 " +
        // "and sc.explanation = '" + bondDownStatus.getResolutionNumber() + "'"
        // +
        // "and mb.emisionDate >= '" +
        // dateToStr(userSession.getFiscalPeriod().getStartDate()) + "'" +
        // "group by ac.accountcode, ac.accountname " +
        // "order by ac.accountcode";
        //
        // query = entityManager.createNativeQuery(sqlNative);
        // results = query.getResultList();
        //
        // for(Object[] row : results){
        // if ((BigDecimal)row[2] != null){
        // BondDownStatus parte = new BondDownStatus((String)row[0],
        // (String)row[1], (BigDecimal)row[2]);
        // parte.setAccountTotal(parte.getAccountTotal().multiply(new
        // BigDecimal(-1)));
        // bondsDownStatusByAccount.add(parte);
        // total = total.subtract((BigDecimal)row[2]);
        // }
        // }
        //
        //
        // results.clear();
        // sqlNative =
        // "select ac.previousyearsaccountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
        // +
        // "left join gimprod.municipalbond mb " +
        // "on mb.id=sc.municipalbond_id " +
        // "left join gimprod.item i " +
        // "on i.discountedBond_id=sc.municipalbond_id " +
        // "left join gimprod.entry e " +
        // "on e.id = i.entry_id " +
        // "left join gimprod.account ac " +
        // "on ac.id = e.account_id " +
        // "where sc.municipalbondstatus_id = 9 " +
        // "and sc.explanation = '" + bondDownStatus.getResolutionNumber() + "'"
        // +
        // "and mb.emisionDate < '" +
        // dateToStr(userSession.getFiscalPeriod().getStartDate()) + "'" +
        // "group by ac.previousyearsaccountcode, ac.accountname " +
        // "order by ac.previousyearsaccountcode";
        //
        // query = entityManager.createNativeQuery(sqlNative);
        // results = query.getResultList();
        //
        // for(Object[] row : results){
        // if ((BigDecimal)row[2] != null){
        // BondDownStatus parte = new BondDownStatus((String)row[0],
        // (String)row[1], (BigDecimal)row[2]);
        // parte.setAccountTotal(parte.getAccountTotal().multiply(new
        // BigDecimal(-1)));
        // bondsDownStatusByAccount.add(parte);
        // total = total.subtract((BigDecimal)row[2]);
        // }
        // }
        //
        //
        //
        results.clear();
        sqlNative = "select ac.accountcode, ac.accountname, sum(ti.value) from gimprod.StatusChange sc "
                + "left join gimprod.municipalbond mb "
                + "on mb.id=sc.municipalbond_id "
                + "left join gimprod.taxitem ti "
                + "on ti.municipalbond_id=sc.municipalbond_id "
                + "left join gimprod.tax t "
                + "on t.id = ti.tax_id "
                + "left join gimprod.account ac "
                + "on ac.id = t.taxaccount_id "
                + "where sc.municipalbondstatus_id = 9 "
                + "and sc.explanation = '"
                + bondDownStatus.getResolutionNumber()
                + "'"
                + "group by ac.accountcode, ac.accountname "
                + "order by ac.accountCode ";

        query = entityManager.createNativeQuery(sqlNative);
        results = query.getResultList();

        for (Object[] row : results) {
            if ((BigDecimal) row[2] != null) {
                BondDownStatus parte = new BondDownStatus((String) row[0],
                        (String) row[1], (BigDecimal) row[2]);
                bondsDownStatusByAccount.add(parte);
                total = total.add((BigDecimal) row[2]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void findDownBondsWithAccountByDates() {
        bonds.clear();
        bondsDownStatusByAccount.clear();
        total = BigDecimal.ZERO;
        // this.bondDownStatus = bondDownStatus;
        String sqlNative = "select ac.accountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
                + "left join gimprod.municipalbond mb "
                + "on mb.id=sc.municipalbond_id "
                + "left join gimprod.item i "
                + "on i.municipalbond_id=sc.municipalbond_id "
                + "left join gimprod.entry e "
                + "on e.id = i.entry_id "
                + "left join gimprod.account ac "
                + "on ac.id = e.account_id "
                + "where sc.municipalbondstatus_id = 9 "
                + "and sc.date between '"
                + dateToStr(startDate)
                + "' AND '"
                + dateToStr(endDate)
                + "' "
                + "and mb.emisionDate >= '"
                + dateToStr(userSession.getFiscalPeriod().getStartDate())
                + "' "
                + "group by ac.accountcode, ac.accountname "
                + "order by ac.accountCode";

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery(sqlNative);
        List<Object[]> results = query.getResultList();

        for (Object[] row : results) {
            BondDownStatus parte = new BondDownStatus((String) row[0],
                    (String) row[1], (BigDecimal) row[2]);
            bondsDownStatusByAccount.add(parte);
            total = total.add((BigDecimal) row[2]);
        }
        results.clear();

        sqlNative = "select ac.previousyearsaccountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
                + "left join gimprod.municipalbond mb "
                + "on mb.id=sc.municipalbond_id "
                + "left join gimprod.item i "
                + "on i.municipalbond_id=sc.municipalbond_id "
                + "left join gimprod.entry e "
                + "on e.id = i.entry_id "
                + "left join gimprod.account ac "
                + "on ac.id = e.account_id "
                + "where sc.municipalbondstatus_id = 9 "
                + "and sc.date between '"
                + dateToStr(startDate)
                + "' AND '"
                + dateToStr(endDate)
                + "' "
                + "and mb.emisionDate < '"
                + dateToStr(userSession.getFiscalPeriod().getStartDate())
                + "' "
                + "group by ac.previousyearsaccountcode, ac.accountname "
                + "order by ac.previousyearsaccountcode";

        query = entityManager.createNativeQuery(sqlNative);
        results = query.getResultList();

        for (Object[] row : results) {
            BondDownStatus parte = new BondDownStatus((String) row[0],
                    (String) row[1], (BigDecimal) row[2]);
            bondsDownStatusByAccount.add(parte);
            total = total.add((BigDecimal) row[2]);
        }

        // sqlNative =
        // "select ac.accountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
        // +
        // "left join gimprod.municipalbond mb " +
        // "on mb.id=sc.municipalbond_id " +
        // "left join gimprod.item i " +
        // "on i.discountedBond_id=sc.municipalbond_id " +
        // "left join gimprod.entry e " +
        // "on e.id = i.entry_id " +
        // "left join gimprod.account ac " +
        // "on ac.id = e.account_id " +
        // "where sc.municipalbondstatus_id = 9 " +
        // "and sc.date between '" + dateToStr(startDate) + "' AND '" +
        // dateToStr(endDate) + "' " +
        // "and mb.emisionDate >= '" +
        // dateToStr(userSession.getFiscalPeriod().getStartDate()) + "' " +
        // "group by ac.accountcode, ac.accountname " +
        // "order by ac.accountCode";
        //
        // entityManager = getEntityManager();
        // query = entityManager.createNativeQuery(sqlNative);
        // results = query.getResultList();
        //
        // for(Object[] row : results){
        // if ((BigDecimal)row[2] != null){
        // BondDownStatus parte = new BondDownStatus((String)row[0],
        // (String)row[1], (BigDecimal)row[2]);
        // parte.setAccountTotal(parte.getAccountTotal().multiply(new
        // BigDecimal(-1)));
        // bondsDownStatusByAccount.add(parte);
        // total = total.subtract((BigDecimal)row[2]);
        // }
        // }
        //
        //
        //
        // results.clear();
        // sqlNative =
        // "select ac.previousyearsaccountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
        // +
        // "left join gimprod.municipalbond mb " +
        // "on mb.id=sc.municipalbond_id " +
        // "left join gimprod.item i " +
        // "on i.discountedBond_id=sc.municipalbond_id " +
        // "left join gimprod.entry e " +
        // "on e.id = i.entry_id " +
        // "left join gimprod.account ac " +
        // "on ac.id = e.account_id " +
        // "where sc.municipalbondstatus_id = 9 " +
        // "and sc.date between '" + dateToStr(startDate) + "' AND '" +
        // dateToStr(endDate) + "' " +
        // "and mb.emisionDate < '" +
        // dateToStr(userSession.getFiscalPeriod().getStartDate()) + "' " +
        // "group by ac.previousyearsaccountcode, ac.accountname " +
        // "order by ac.previousyearsaccountcode";
        // entityManager = getEntityManager();
        // query = entityManager.createNativeQuery(sqlNative);
        // results = query.getResultList();
        //
        // for(Object[] row : results){
        // if ((BigDecimal)row[2] != null){
        // BondDownStatus parte = new BondDownStatus((String)row[0],
        // (String)row[1], (BigDecimal)row[2]);
        // parte.setAccountTotal(parte.getAccountTotal().multiply(new
        // BigDecimal(-1)));
        // bondsDownStatusByAccount.add(parte);
        // total = total.subtract((BigDecimal)row[2]);
        // }
        // }
        //
        //
        //
        results.clear();
        sqlNative = "select ac.accountcode, ac.accountname, sum(ti.value) from gimprod.StatusChange sc "
                + "left join gimprod.municipalbond mb "
                + "on mb.id=sc.municipalbond_id "
                + "left join gimprod.taxitem ti "
                + "on ti.municipalbond_id=sc.municipalbond_id "
                + "left join gimprod.tax t "
                + "on t.id = ti.tax_id "
                + "left join gimprod.account ac "
                + "on ac.id = t.taxaccount_id "
                + "where sc.municipalbondstatus_id = 9 "
                + "and sc.date between '"
                + dateToStr(startDate)
                + "' AND '"
                + dateToStr(endDate)
                + "' "
                + "group by ac.accountcode, ac.accountname "
                + "order by ac.accountCode ";

        entityManager = getEntityManager();
        query = entityManager.createNativeQuery(sqlNative);
        results = query.getResultList();

        for (Object[] row : results) {
            if ((BigDecimal) row[2] != null) {
                BondDownStatus parte = new BondDownStatus((String) row[0],
                        (String) row[1], (BigDecimal) row[2]);
                bondsDownStatusByAccount.add(parte);
                total = total.add((BigDecimal) row[2]);
            }
        }
        
        //rfarmijosm 2016-02-23
        workdayHome.setStartDate(startDate);
        workdayHome.setEndDate(endDate);
        workdayHome.replacementPaymentReport();
        
    }

    public String dateToStr(Date date) {
        String str = "";
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        str = String.valueOf(cal.get(Calendar.YEAR)) + "-"
                + String.valueOf(cal.get(Calendar.MONTH) + 1) + "-"
                + String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        return str;
    }

    // para el cambio de obligaciones de contribuyentes
    @SuppressWarnings("unchecked")
    public void searchResidentByCriteria() {
        if (this.criteria != null && !this.criteria.isEmpty()) {
            Query query = getEntityManager().createNamedQuery(
                    "Resident.findByCriteria");
            query.setParameter("criteria", this.criteria);
            setResidents(query.getResultList());
        }
    }

    @SuppressWarnings("unchecked")
    public Resident searchResidentById() {
        if (this.getResidentId() != null) {
            Query query = getEntityManager().createNamedQuery(
                    "Resident.findById");
            query.setParameter("id", this.getResidentId());
            List<?> list = query.getResultList();
            return (list != null ? (Resident) list.get(0) : null);
        }
        return null;
    }

    public void searchResidentPrevious() {
        Query query = getEntityManager().createNamedQuery(
                "Resident.findByIdentificationNumber");
        query.setParameter("identificationNumber",
                this.identificationNumberPrevious);
        try {
            Resident resident = (Resident) query.getSingleResult();
            this.setPreviousResident(resident);

            if (resident.getId() == null) {
                addFacesMessageFromResourceBundle("resident.notFound");
            }
        } catch (Exception e) {
            this.setPreviousResident(null);
            addFacesMessageFromResourceBundle("resident.notFound");
        }
        searchMunicipalBond();
    }

    public void searchResidentCurrent() {
        Query query = getEntityManager().createNamedQuery(
                "Resident.findByIdentificationNumber");
        query.setParameter("identificationNumber",
                this.identificationNumberCurrent);
        try {
            Resident resident = (Resident) query.getSingleResult();
            this.setCurrentResident(resident);

            if (resident.getId() == null) {
                addFacesMessageFromResourceBundle("resident.notFound");
            }
        } catch (Exception e) {
            this.setCurrentResident(null);
            addFacesMessageFromResourceBundle("resident.notFound");
        }
    }

    public void previousResidentSelectedListener(ActionEvent event) {
        UIComponent component = event.getComponent();
        Resident resident = (Resident) component.getAttributes()
                .get("resident");
        this.setPreviousResident(resident);
        this.setIdentificationNumberPrevious(resident.getIdentificationNumber());
        searchMunicipalBond();
    }

    public void currentSelectedListener(ActionEvent event) {
        UIComponent component = event.getComponent();
        Resident resident = (Resident) component.getAttributes()
                .get("resident");
        this.setCurrentResident(resident);
        this.setIdentificationNumberCurrent(resident.getIdentificationNumber());
    }

    public void clearSearchResidentPanel() {
        this.setCriteria(null);
        setResidents(null);
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public String getIdentificationNumberPrevious() {
        return identificationNumberPrevious;
    }

    public void setIdentificationNumberPrevious(
            String identificationNumberPrevious) {
        this.identificationNumberPrevious = identificationNumberPrevious;
    }

    public String getIdentificationNumberCurrent() {
        return identificationNumberCurrent;
    }

    public void setIdentificationNumberCurrent(
            String identificationNumberCurrent) {
        this.identificationNumberCurrent = identificationNumberCurrent;
    }

    public Long getResidentId() {
        return residentId;
    }

    public void setResidentId(Long residentId) {
        this.residentId = residentId;
    }

    public List<Resident> getResidents() {
        return residents;
    }

    public void setResidents(List<Resident> residents) {
        this.residents = residents;
    }

    public Resident getPreviousResident() {
        return previousResident;
    }

    public void setPreviousResident(Resident previousResident) {
        this.previousResident = previousResident;
    }

    public Resident getCurrentResident() {
        return currentResident;
    }

    public void setCurrentResident(Resident currentResident) {
        this.currentResident = currentResident;
    }

    public void searchMunicipalBond() {
        allBondsSelected=Boolean.FALSE;
        if (previousResident != null) {
            String sentence = "select mb from MunicipalBond mb "
                    + "left join FETCH mb.entry entry "
                    + "left join FETCH mb.resident resident "
                    + "left join FETCH mb.deposits deposit "
                    + "left join FETCH mb.receipt receipt "
                    + "LEFT JOIN FETCH resident.currentAddress "
                    + "where mb.resident.id=:rId "
                    + "and mb.municipalBondStatus.id = 3"
                    + "order by mb.entry,mb.creationDate,mb.number";
            Query q = this.getEntityManager().createQuery(sentence);
            this.bonds = q.setParameter("rId", previousResident.getId()).getResultList();
            statusChangeListGroup = new ArrayList<StatusChangeItem>();

            statusChangeListGroup = fillMunicipalBondItems(bonds);

//        StatusChangeItem change;
//            for (MunicipalBond mb : bonds) {
//                change = new StatusChangeItem(mb);
//                change.setMunicipalBond(mb);
//                StatusChange sc = new StatusChange();
//                sc.setChangeResident(Boolean.FALSE);
//                sc.setMunicipalBond(mb);
//                change.setStatusChange(sc);
//                statusChangeListGroup.add(change);                
//            }
        }
    }

    public void transferMunicipalBond(StatusChange sc, StatusChangeItem municipalBondItem) {
        municipalBondItem.setIsSelected(Boolean.TRUE);
        sc.setChangeResident(Boolean.TRUE);
        
    }

    public void revertMunicipalBond(StatusChange sc, StatusChangeItem municipalBondItem) {
        municipalBondItem.setIsSelected(Boolean.FALSE);
        sc.setChangeResident(Boolean.FALSE);
    }

    public String showTransfer(StatusChange sc) {
        if (sc.getChangeResident().equals(Boolean.TRUE)) {
            return "#FE642E";
        } else {
            //System.out.println("retorn with");
            return "#fFfFfF";
        }
    }

    public String startTransfering() {
        int mbToChange = 0;

        List<Long> mbIds = new ArrayList<Long>();
        //System.out.println("statusChangeListGroup size >> " + statusChangeListGroup.size());
//        if (statusChangeListGroup.size() > 0) {
        if (statusChangeLst.size() > 0) {
            for (StatusChange sc : statusChangeLst) {
                //System.out.println("sc getChangeResident >> " + sc.getChangeResident());
                if (sc.getChangeResident()) {
                    mbIds.add(sc.getMunicipalBond().getId());
                    mbToChange++;
                }
            }
//            for (StatusChangeItem sc : statusChangeListGroup) {
//                for (StatusChangeItem sc2 : sc.getMunicipalBondItems()) {
//                    for (StatusChangeItem sc3 : sc2.getMunicipalBondItems()) {
//                        StatusChange tc = sc3.getStatusChange();
//                        System.out.println("tc getChangeResident >> " + tc.getChangeResident());
//                        if (tc.getChangeResident()) {
//                            mbIds.add(tc.getMunicipalBond().getId());
//                            mbToChange++;
//                        }
//
//                    }
//                }
//            }
//
        } else {

            return null;
        }
        //System.out.println("la canitdad es ......... " + mbToChange);
        if (mbToChange > 0 && currentResident != null) {
            changeResdient(mbIds);
            insertStatusChange();
            return "ok";
        } else {
            return null;
        }
    }

    /**
     * cambio las obligaciones al nuevo resident
     *
     * @param mbIds
     */
    public void changeResdient(List<Long> mbIds) {
        String ids = mbIds.toString().replace("[", "").replace("]", "");
        String sentence = "";
        Query q;
        int value = 0;
        sentence = "update gimprod.municipalbond set resident_id = " + currentResident.getId() + " where id in (" + ids + ")";
        q = entityManager.createNativeQuery(sentence);
        value = q.executeUpdate();
        //System.out.println(">>>>>>>>> " + sentence + " " + value);
        entityManager.flush();
    }

    /**
     * Inserta la nuevos estatuschange dentro la base de datos para los MB
     */
    public void insertStatusChange() {
        StatusChange scNew;
        int i = 1;
        String explanation;
        for (StatusChange sc : statusChangeLst) {
//            StatusChange sc2 = sc.getStatusChange();
            if (sc.getChangeResident()) {
                explanation = 
                        this.getInstance().getExplanation() 
                        + "\n Dueño anterior c.i. = " 
                        + this.getPreviousResident().getIdentificationNumber() + " , "
                        + this.getPreviousResident().getName() 
                        +"\n Nuevo dueño c.i. =" 
                        + this.getCurrentResident().getIdentificationNumber() + " , "
                        + this.getCurrentResident().getName();
                scNew = sc.cloneNew(sc, explanation, this.userSession.getUser());
                entityManager.persist(scNew);
                if ((i % 100) == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
                i++;
            }
        }
        entityManager.flush();
        entityManager.clear();
    }
    
    //Para reporte de correccion de errores
    //Jock Samaniego
    
    private List<MunicipalBondErrorsCorrectionDTO> bondsWithCorrectionDTO = new ArrayList<MunicipalBondErrorsCorrectionDTO>();
    private List<MunicipalBondErrorsCorrectionDTO> bondsWithCorrection = new LinkedList<MunicipalBondErrorsCorrectionDTO>();
    private List<BondDownStatus> bondsWithCorrectionAccount = new ArrayList<BondDownStatus>();
    private MunicipalBondStatus correctionBondStatus;
    private Date correctionStartDate;
	private Date correctionEndDate;
	private BigDecimal totalWithCorrection;
    
    public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	public List<MunicipalBondErrorsCorrectionDTO> getBondsWithCorrectionDTO() {
		return bondsWithCorrectionDTO;
	}

	public void setBondsWithCorrectionDTO(
			List<MunicipalBondErrorsCorrectionDTO> bondsWithCorrectionDTO) {
		this.bondsWithCorrectionDTO = bondsWithCorrectionDTO;
	}

	public List<MunicipalBondErrorsCorrectionDTO> getBondsWithCorrection() {
		return bondsWithCorrection;
	}

	public void setBondsWithCorrection(List<MunicipalBondErrorsCorrectionDTO> bondsWithCorrection) {
		this.bondsWithCorrection = bondsWithCorrection;
	}

	public List<BondDownStatus> getBondsWithCorrectionAccount() {
		return bondsWithCorrectionAccount;
	}

	public void setBondsWithCorrectionAccount(
			List<BondDownStatus> bondsWithCorrectionAccount) {
		this.bondsWithCorrectionAccount = bondsWithCorrectionAccount;
	}

	public MunicipalBondStatus getCorrectionBondStatus() {
		return correctionBondStatus;
	}

	public void setCorrectionBondStatus(MunicipalBondStatus correctionBondStatus) {
		this.correctionBondStatus = correctionBondStatus;
	}
	
	public Date getCorrectionStartDate() {
		return correctionStartDate;
	}

	public void setCorrectionStartDate(Date correctionStartDate) {
		this.correctionStartDate = correctionStartDate;
	}

	public Date getCorrectionEndDate() {
		return correctionEndDate;
	}

	public void setCorrectionEndDate(Date correctionEndDate) {
		this.correctionEndDate = correctionEndDate;
	}
	
	public BigDecimal getTotalWithCorrection() {
		return totalWithCorrection;
	}

	public void setTotalWithCorrection(BigDecimal totalWithCorrection) {
		this.totalWithCorrection = totalWithCorrection;
	}
	
	

	public void findBondsInErrorsCorrection(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		correctionBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_ERRORS_CORRECTION");
		bondsWithCorrectionDTO.clear();
		bondsWithCorrection.clear();
		bondsWithCorrectionAccount.clear();
		totalWithCorrection = BigDecimal.ZERO;
		
		String query = "SELECT mb.number as mbNumber,"
				+ "mb.emisiondate as emission,"
				+ "res.identificationnumber as resIdentification,"
				+ "res.name as resName,"
				+ "ent.name as entry,"
				+ "mb.value as value,"
				+ "mb.taxesTotal as taxes,"
				+ "stc.date as changeStatus,"
				+ "stc.explanation as explanation,"
				+ "usr.name as userName "
				+ "FROM gimprod.municipalbond mb "
				+ "LEFT JOIN gimprod.statuschange stc On stc.municipalbond_id = mb.id "
				+ "LEFT JOIN gimprod._user usr On usr.id = stc.user_id "
				+ "LEFT JOIN gimprod.resident res On res.id = mb.resident_id "
				+ "LEFT JOIN gimprod.entry ent On ent.id = mb.entry_id "
				+ "where mb.municipalbondstatus_id = :status "
				+ "and stc.date BETWEEN :startDate and :endDate "
				+ "ORDER BY stc.date,mb.emisiondate,res.name ASC";
		
		Query q = this.getEntityManager().createNativeQuery(query);
		q.setParameter("status", correctionBondStatus.getId());
		q.setParameter("startDate", correctionStartDate);
		q.setParameter("endDate", correctionEndDate);
		bondsWithCorrectionDTO = NativeQueryResultsMapper.map(q.getResultList(), MunicipalBondErrorsCorrectionDTO.class);
	}
	
	public void findBondsInErrorsCorrectionDetail(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		correctionBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_ERRORS_CORRECTION");
		bondsWithCorrectionDTO.clear();
		bondsWithCorrection.clear();
		bondsWithCorrectionAccount.clear();
		totalWithCorrection = BigDecimal.ZERO;
		
		String query = "SELECT mb.number as mbNumber,"
				+ "mb.emisiondate as emission,"
				+ "res.identificationnumber as resIdentification,"
				+ "res.name as resName,"
				+ "ent.name as entry,"
				+ "mb.value as value,"
				+ "mb.taxesTotal as taxes,"
				+ "stc.date as changeStatus,"
				+ "stc.explanation as explanation,"
				+ "usr.name as userName "
				+ "FROM gimprod.municipalbond mb "
				+ "LEFT JOIN gimprod.statuschange stc On stc.municipalbond_id = mb.id "
				+ "LEFT JOIN gimprod._user usr On usr.id = stc.user_id "
				+ "LEFT JOIN gimprod.resident res On res.id = mb.resident_id "
				+ "LEFT JOIN gimprod.entry ent On ent.id = mb.entry_id "
				+ "where mb.municipalbondstatus_id = :status "
				+ "and stc.date BETWEEN :startDate and :endDate "
				+ "ORDER BY stc.date,mb.emisiondate,res.name ASC";
		
		Query q = this.getEntityManager().createNativeQuery(query);
		q.setParameter("status", correctionBondStatus.getId());
		q.setParameter("startDate", correctionStartDate);
		q.setParameter("endDate", correctionEndDate);
		bondsWithCorrection = NativeQueryResultsMapper.map(q.getResultList(), MunicipalBondErrorsCorrectionDTO.class);
		for(MunicipalBondErrorsCorrectionDTO mbDTO: bondsWithCorrection){
			totalWithCorrection = totalWithCorrection.add(mbDTO.getValue()).add(mbDTO.getTaxesTotal());
		}
	}
	
	public void findBondsInErrorsCorrectionByAccount(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		correctionBondStatus = systemParameterService.materialize(
				MunicipalBondStatus.class,
				"MUNICIPAL_BOND_STATUS_ID_ERRORS_CORRECTION");
		bondsWithCorrectionDTO.clear();
		bondsWithCorrection.clear();
		bondsWithCorrectionAccount.clear();
		totalWithCorrection = BigDecimal.ZERO;
		
		String sqlNative = "select ac.accountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
                + "left join gimprod.municipalbond mb "
                + "on mb.id=sc.municipalbond_id "
                + "left join gimprod.item i "
                + "on i.municipalbond_id=sc.municipalbond_id "
                + "left join gimprod.entry e "
                + "on e.id = i.entry_id "
                + "left join gimprod.account ac "
                + "on ac.id = e.account_id "
                + "where sc.municipalbondstatus_id ="+ correctionBondStatus.getId() +" "
                + "and sc.date between '"
                + dateToStr(correctionStartDate)
                + "' AND '"
                + dateToStr(correctionEndDate)
                + "' "
                + "and mb.emisionDate >= '"
                + dateToStr(userSession.getFiscalPeriod().getStartDate())
                + "' "
                + "group by ac.accountcode, ac.accountname "
                + "order by ac.accountCode";

        EntityManager entityManager = getEntityManager();
        Query query = entityManager.createNativeQuery(sqlNative);
        List<Object[]> results = query.getResultList();

        for (Object[] row : results) {
            BondDownStatus parte = new BondDownStatus((String) row[0],
                    (String) row[1], (BigDecimal) row[2]);
            bondsWithCorrectionAccount.add(parte);
            totalWithCorrection = totalWithCorrection.add((BigDecimal) row[2]);
        }
        results.clear();

        sqlNative = "select ac.previousyearsaccountcode, ac.accountname, sum(i.total) from gimprod.StatusChange sc "
                + "left join gimprod.municipalbond mb "
                + "on mb.id=sc.municipalbond_id "
                + "left join gimprod.item i "
                + "on i.municipalbond_id=sc.municipalbond_id "
                + "left join gimprod.entry e "
                + "on e.id = i.entry_id "
                + "left join gimprod.account ac "
                + "on ac.id = e.account_id "
                + "where sc.municipalbondstatus_id ="+ correctionBondStatus.getId() +" "
                + "and sc.date between '"
                + dateToStr(correctionStartDate)
                + "' AND '"
                + dateToStr(correctionEndDate)
                + "' "
                + "and mb.emisionDate < '"
                + dateToStr(userSession.getFiscalPeriod().getStartDate())
                + "' "
                + "group by ac.previousyearsaccountcode, ac.accountname "
                + "order by ac.previousyearsaccountcode";

        query = entityManager.createNativeQuery(sqlNative);
        results = query.getResultList();

        for (Object[] row : results) {
            BondDownStatus parte = new BondDownStatus((String) row[0],
                    (String) row[1], (BigDecimal) row[2]);
            bondsWithCorrectionAccount.add(parte);
            totalWithCorrection = totalWithCorrection.add((BigDecimal) row[2]);
        }
        results.clear();
        
        sqlNative = "select ac.accountcode, ac.accountname, sum(ti.value) from gimprod.StatusChange sc "
                + "left join gimprod.municipalbond mb "
                + "on mb.id=sc.municipalbond_id "
                + "left join gimprod.taxitem ti "
                + "on ti.municipalbond_id=sc.municipalbond_id "
                + "left join gimprod.tax t "
                + "on t.id = ti.tax_id "
                + "left join gimprod.account ac "
                + "on ac.id = t.taxaccount_id "
                + "where sc.municipalbondstatus_id ="+ correctionBondStatus.getId() +" "
                + "and sc.date between '"
                + dateToStr(correctionStartDate)
                + "' AND '"
                + dateToStr(correctionEndDate)
                + "' "
                + "group by ac.accountcode, ac.accountname "
                + "order by ac.accountCode ";

        entityManager = getEntityManager();
        query = entityManager.createNativeQuery(sqlNative);
        results = query.getResultList();

        for (Object[] row : results) {
            if ((BigDecimal) row[2] != null) {
                BondDownStatus parte = new BondDownStatus((String) row[0],
                        (String) row[1], (BigDecimal) row[2]);
                bondsWithCorrectionAccount.add(parte);
                totalWithCorrection = totalWithCorrection.add((BigDecimal) row[2]);
            }
        }

	}
	
	
	private Charge revenueCharge;		
	private Delegate revenueDelegate;
	
	
	public Charge getRevenueCharge() {
		return revenueCharge;
	}

	public void setRevenueCharge(Charge revenueCharge) {
		this.revenueCharge = revenueCharge;
	}

	public Delegate getRevenueDelegate() {
		return revenueDelegate;
	}

	public void setRevenueDelegate(Delegate revenueDelegate) {
		this.revenueDelegate = revenueDelegate;
	}
	
	public Charge getCharge(String systemParameter) {
		SystemParameterService systemParameterService = ServiceLocator.getInstance()
				.findResource(SystemParameterService.LOCAL_NAME);
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(SYSTEM_PARAMETER_SERVICE_NAME);
		Charge charge = systemParameterService.materialize(Charge.class,systemParameter);
		return charge;
	}

	public void loadCharge() {
		revenueCharge = getCharge("DELEGATE_ID_REVENUE");
		if (revenueCharge != null) {
			for (Delegate d : revenueCharge.getDelegates()) {
				if (d.getIsActive())
					revenueDelegate = d;
			}
		}	
	}

}
