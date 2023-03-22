package org.gob.gim.income.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.revenue.facade.RevenueService;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.revenue.model.Entry;

@Name("paymentAgreementReport")
public class PaymentAgreementReport extends EntityHome<PaymentAgreement>{
    
    private static final long serialVersionUID = 2709250452627933819L;

    public static String REVENUE_SERVICE_NAME = "/gim/RevenueService/local";
    private Date beginDate = new Date();
    private Date endDate = new Date();
    private Date expirationDate = new Date();
    private List<Object> list = new ArrayList<Object>();
    
    private Entry entry;
    private String entryCode;
    private String entryCodeForSearch;
    private String criteriaEntry;
    private List<Entry> entries;

    private boolean readyForPrint = false;

    public PaymentAgreementReport() {

    }

    /**
     * @return the beginDate
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * @param beginDate the beginDate to set
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the expirationDate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the list
     */
    public List<Object> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<Object> list) {
        this.list = list;
    }

    /**
     * @return the entry
     */
    public Entry getEntry() {
        return entry;
    }

    /**
     * @param entry the entry to set
     */
    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    /**
     * @return the entryCode
     */
    public String getEntryCode() {
        return entryCode;
    }

    /**
     * @param entryCode the entryCode to set
     */
    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    /**
     * @return the entryCodeForSearch
     */
    public String getEntryCodeForSearch() {
        return entryCodeForSearch;
    }

    /**
     * @param entryCodeForSearch the entryCodeForSearch to set
     */
    public void setEntryCodeForSearch(String entryCodeForSearch) {
        this.entryCodeForSearch = entryCodeForSearch;
    }

    /**
     * @return the criteriaEntry
     */
    public String getCriteriaEntry() {
        return criteriaEntry;
    }

    /**
     * @param criteriaEntry the criteriaEntry to set
     */
    public void setCriteriaEntry(String criteriaEntry) {
        this.criteriaEntry = criteriaEntry;
    }

    /**
     * @return the entries
     */
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * @param entries the entries to set
     */
    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    /**
     * @return the readyForPrint
     */
    public boolean isReadyForPrint() {
        return readyForPrint;
    }

    /**
     * @param readyForPrint the readyForPrint to set
     */
    public void setReadyForPrint(boolean readyForPrint) {
        this.readyForPrint = readyForPrint;
    }

    public void initialize(){

    }

    public void limpiar(){
        list.clear();
        beginDate = new Date();
        endDate = new Date();
        expirationDate = new Date();
        entry = null;
        readyForPrint = false;
        clearEntryChooserPanel();
        clearSearchEntryPanel();
    }
    
    public void consult(){
        list.clear();
        list = getConsult();
        readyForPrint = true;
    }

    private List<Object> getConsult() {
        String sql = "";
        if (entry != null)
            sql = "select p.id, p.agreementtype, p.firstpaymentdate, r.identificationnumber, r.name,"
                + "(Select coalesce(SUM(d.value),0) from Deposit d join municipalbond m on m.id = d.municipalbond_id where m.paymentagreement_id=p.id and d.status='VALID' and m.entry_id = " + entry.getId() + " and m.municipalbondstatus_id in(4, 6, 11)) as pagado, "
                + "(Select coalesce(sum(m1.value),0) from municipalbond m1 where m1.paymentagreement_id=p.id and m1.entry_id = " + entry.getId() + " and m1.municipalbondstatus_id in (4, 14)) as saldo,"
                + "(Select max(d2.date) ultimopago from Deposit d2 join municipalbond m2 on m2.id = d2.municipalbond_id where m2.paymentagreement_id=p.id and d2.status='VALID' and m2.entry_id = " + entry.getId() + " and m2.municipalbondstatus_id in(4, 6, 11, 14)) ultimopago "
                        + "from paymentagreement p "
                        + "inner join resident r on r.id= p.resident_id "
                        + "where p.isactive = true "
                        + "and p.firstpaymentdate between '" + beginDate + "' and '" + endDate + "' "
                        + "and (Select max(d2.date) ultimopago from Deposit d2 join municipalbond m2 on m2.id = d2.municipalbond_id where m2.paymentagreement_id=p.id and d2.status='VALID' and m2.entry_id = " + entry.getId() + " and m2.municipalbondstatus_id in(4, 6, 11, 14)) < '" + expirationDate + "' "
                        + "order by ultimopago, p.firstpaymentdate, id";
        else
            sql = "select p.id, p.agreementtype, p.firstpaymentdate, r.identificationnumber, r.name,"
                    + "(Select coalesce(SUM(d.value),0) from Deposit d join municipalbond m on m.id = d.municipalbond_id where m.paymentagreement_id=p.id and d.status='VALID' and m.municipalbondstatus_id in(4, 6, 11)) as pagado, "
                    + "(Select coalesce(sum(m1.value),0) from municipalbond m1 where m1.paymentagreement_id=p.id and m1.municipalbondstatus_id in (4, 14)) as saldo,"
                    + "(Select max(d2.date) ultimopago from Deposit d2 join municipalbond m2 on m2.id = d2.municipalbond_id where m2.paymentagreement_id=p.id and d2.status='VALID' and m2.municipalbondstatus_id in(4, 6, 11, 14)) ultimopago "
                            + "from paymentagreement p "
                            + "inner join resident r on r.id= p.resident_id "
                            + "where p.isactive = true "
                            + "and p.firstpaymentdate between '" + beginDate + "' and '" + endDate + "' "
                            + "and (Select max(d2.date) ultimopago from Deposit d2 join municipalbond m2 on m2.id = d2.municipalbond_id where m2.paymentagreement_id=p.id and d2.status='VALID' and m2.municipalbondstatus_id in(4, 6, 11, 14)) < '" + expirationDate + "' "
                            + "order by ultimopago, p.firstpaymentdate, id";
            
        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList();
    }

    public void searchEntry() {
        if (entryCode != null) {
            RevenueService revenueService = ServiceLocator.getInstance().findResource(REVENUE_SERVICE_NAME);
            Entry entry = revenueService.findEntryByCode(entryCode);
            if (entry != null) {
                this.entry = entry;
                this.setEntry(entry);
                if (entry.getAccount() != null) {
                    setEntryCode(entry.getAccount().getAccountCode());
                } else {
                    setEntryCode(entry.getCode());
                }
                setEntryCodeForSearch(entry.getCode());
                readyForPrint = false;
            } else {
                setEntry(null);
                setEntryCodeForSearch(null);
                readyForPrint = false;
            }
        }
    }

    public void searchEntryByCriteria() {
        // logger.info("SEARCH Entry BY CRITERIA "+this.criteriaEntry);
        if (this.criteriaEntry != null && !this.criteriaEntry.isEmpty()) {
            RevenueService revenueService = (RevenueService) ServiceLocator.getInstance()
                    .findResource(REVENUE_SERVICE_NAME);
            entries = revenueService.findEntryByCriteria(criteriaEntry);
        }
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
        setEntryCodeForSearch(entry.getCode());
        readyForPrint = false;
    }

    public void clearEntryChooserPanel() {
        clearSearchEntryPanel();
        setEntryCode(null);
        setEntryCodeForSearch(null);
        setEntry(null);
        readyForPrint = false;
    }

    public void clearSearchEntryPanel() {
        this.setCriteriaEntry(null);
        entries = null;
        readyForPrint = false;
    }

}
