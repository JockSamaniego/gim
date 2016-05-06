package org.gob.gim.income.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.income.model.Account;
import ec.gob.gim.income.model.Tax;
import ec.gob.gim.income.model.TaxRate;

@Name("taxHome")
public class TaxHome extends EntityHome<Tax> {

	private static final long serialVersionUID = 1L;
	
	@Logger
	Log logger;
	
	private String accountCode;
	
	private Account currentAccount;

	public void setTaxId(Long id) {
		setId(id);
	}

	public Long getTaxId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}
	
	private boolean isFirstTime = true;

	public void wire() {		
		if(this.getInstance().getTaxAccount() != null && isFirstTime) {
			setAccountCode(this.getInstance().getTaxAccount().getAccountCode() + " - " +  this.getInstance().getTaxAccount().getAccountName());				
			setCurrentAccount(this.getInstance().getTaxAccount());
			isFirstTime = false;
		}
	}

	public boolean isWired() {
		return true;
	}

	public Tax getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	public List<TaxRate> getTaxRates() {
		return getInstance() == null ? null : new ArrayList<TaxRate>(
				getInstance().getTaxRates());
	}
	
	private TaxRate buildTaxRate(Date currentDate){
		TaxRate taxRate = new TaxRate();
		/*
		Date lastDate = lastTaxRateDateOfYear();
		if( lastDate == null){
			taxRate.setStartDate(new Date(currentDate.getYear(),0,1));
			taxRate.setEndDate(new Date(currentDate.getYear(),2,31));
		}
		
		if(lastDate != null){
			if(lastDate.getMonth() == 2){
				taxRate.setStartDate(new Date(lastDate.getYear(),3,1));
				taxRate.setEndDate(new Date(lastDate.getYear(),5,30));
			}
			
			if(lastDate.getMonth() == 5){
				taxRate.setStartDate(new Date(lastDate.getYear(),6,1));
				taxRate.setEndDate(new Date(lastDate.getYear(),8,30));
			}
			
			if(lastDate.getMonth() == 8){
				taxRate.setStartDate(new Date(lastDate.getYear(),9,1));
				taxRate.setEndDate(new Date(lastDate.getYear(),11,31));
			}
			
			if(lastDate.getMonth() == 11){
				taxRate.setStartDate(new Date(lastDate.getYear() + 1,0,1));
				taxRate.setEndDate(new Date(currentDate.getYear() + 1 ,2,31));
			}
		}*/		
		taxRate.setRate(BigDecimal.ONE);
		return taxRate;
	}
	
	/*
	private Date lastTaxRateDateOfYear(){		
		if(this.getInstance().getTaxRates().size() > 0 ){
			return this.getInstance().getTaxRates().get(this.getInstance().getTaxRates().size() - 1).getEndDate();
		}
		return null;
	}
	*/
	
	public TaxRate findActiveTaxRate(Tax t) {
		Query query = getEntityManager().createNamedQuery("TaxRate.findActiveByTaxIdAndPaymentDate");
		query.setParameter("taxId", t.getId());
		query.setParameter("paymentDate", new Date());
		List<TaxRate> list = query.getResultList();
		if(list.size() > 0) return list.get(0);
		return null;
	}

//	private void updateActiveTaxRates(List<TaxRate> taxRateList){
//		for (TaxRate taxRate : taxRateList){
//			taxRate.setIsActive(false);
//		}
//	}
	
	public void addTaxRate(){
		logger.info("======= INGRESO A AGREGAR TaxRate", this);
		Calendar now = Calendar.getInstance();		
		TaxRate taxRate = buildTaxRate(now.getTime());
		//List<TaxRate> taxRateList = getInstance().getTaxRates();					
		this.getInstance().add(taxRate);		
	}
	
	@In
	FacesMessages facesMessages;
		
	@Override
	@Transactional
	public String persist() {
		if (!correctDates()) {
			String message = Interpolator.instance().interpolate("#{messages['common.incorrectDates']}", new Object[0]);
			facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);		
			return "failed";
		}
		return super.persist();
	}
	
	private boolean isBetweenDates(Date start, Date end, Date between){
		if(start.before(between) && end.after(between)) return true;
				
		if(start.equals(between) || end.equals(between)) return true;
		
		return false;
	}
	
	
	public boolean correctDates(){
		for(TaxRate t : this.getInstance().getTaxRates()){
			for(TaxRate tr : this.getInstance().getTaxRates()){
				if(!t.equals(tr) && (isBetweenDates(t.getStartDate(), t.getEndDate(), tr.getStartDate())
						|| isBetweenDates(t.getStartDate(), t.getEndDate(), tr.getEndDate()))){					
					return false;
				}
			}
		}
		
		return true;
	}
	
	@Override
	@Transactional
	public String update() {	
		if (!correctDates()) {
			String message = Interpolator.instance().interpolate("#{messages['common.incorrectDates']}", new Object[0]);
			facesMessages.addToControl("residentChooser",org.jboss.seam.international.StatusMessage.Severity.ERROR,message);		
			return "failed";
		}
		return super.update();
	}
	

	
	@SuppressWarnings("unchecked")
	public List<Account> findAccounts(String code){		
		Query query = this.getEntityManager().createNamedQuery(
		"Account.findChildsAccount");
		query.setParameter("accountCode", code);
		return query.getResultList();		
	}
	
	public void beforeAutcomplete(Account a) {		
		this.getInstance().setTaxAccount(a);		
		if(a == null){
			accountCode = null;
		}else{
			//a.setEntry(this.getInstance());
			accountCode = a.getAccountCode() + " - " + a.getAccountName();
		}
	}
	
	public List<Account> findAvailableAccounts(Object suggestion){		
		String accountCode = suggestion.toString();
		List<Account> list = findAccounts(accountCode);		
		if(this.isManaged() && this.getInstance().getTaxAccount() != null && this.getInstance().getTaxAccount().getAccountCode().startsWith(accountCode)
				&& getCurrentAccount() != null && !list.contains(getCurrentAccount())){
			list.add(this.getCurrentAccount());
		}
		return list;
	}
	
//	public boolean canEdit(TaxRate t){
//		Calendar now = Calendar.getInstance();
//		if((t.getStartDate().before(now.getTime()) && t.getEndDate().after(now.getTime())) || t.getStartDate().after(now.getTime())){
//			return true;
//		}		
//		return false;
//	}
	
	public boolean isLastTaxRateManaged(TaxRate t){
		Long id = 0L;
		for(TaxRate tr: this.getInstance().getTaxRates()){
			if(tr.getId() != null && tr.getId() > id){
				id = tr.getId();
			}
		}
		if(t.getId().intValue() == id.intValue()) return true;
		return false;
	}
	
	public boolean canEdit(TaxRate t){
		if(t.getCanBeEdited() != null) return t.getCanBeEdited();
		t.setCanBeEdited(false);
		if(t.getId() == null) {
			t.setCanBeEdited(true);
			return t.getCanBeEdited();
		}		
		return t.getCanBeEdited();
	}
	
	public void reloadEndDateLast(TaxRate taxRate){
		Calendar now = Calendar.getInstance();
		if(taxRate.getStartDate().before(now.getTime())){
			taxRate.setStartDate(now.getTime());
		}
		int position = this.getInstance().getTaxRates().indexOf(taxRate);
		if(position > 0){
			TaxRate before = this.getInstance().getTaxRates().get(position - 1);
			if(taxRate.getStartDate().after(before.getStartDate()) && taxRate.getStartDate().before(before.getEndDate())){
				taxRate.setEndDate(before.getEndDate());
			}else{
				this.getInstance().getTaxRates().remove(taxRate);
				buildTaxRate(now.getTime());
			}			
		}
	}	

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	

	public Account getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(Account currentAccount) {
		this.currentAccount = currentAccount;
	}

}
