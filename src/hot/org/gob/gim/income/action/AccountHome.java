package org.gob.gim.income.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.income.model.Account;

@Name("accountHome")
public class AccountHome extends EntityHome<Account> {

	private Account parentAccount;
	private Account mainAccount;
	private Account accountForRemove;
	private Long parentId;
	private Account newAccount;
	public boolean isAuxiliar;	
	private String criteria;
	
	@Logger
	Log logger;
	
	private List<Account> accounts;

	public void setAccountId(Long id) {
		setId(id);
	}

	public Long getAccountId() {
		return (Long) getId();
	}

	public void load() {
		if (parentId != null) {			
			setAccountId(parentId);
			getInstance();
			parentId = null;			
		} else {
			if (isIdDefined()) {
				wire();
			}
		}

	}

	public void wire() {		
		if(mainAccount == null) setInstance(findMainAccount());
	}

	public boolean isWired() {
		return true;
	}

	public Account getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
//	public String convertInStringIds(List<Long> ids){
//		String res = "";
//		for(Long l: ids){
//			if(l != null) res = res.concat(l + ", ");
//		}
//		if(res.endsWith(", "))res = res.substring(0, res.length()-2);		
//		return res;
//	}
	
	private boolean canDelete(Account a){		
		if(a == null) return false;
		if(a.getEntries().size() > 0) return false;
		return true;
	}
	
	private List<Long> findIds(Account a){
		List<Long> list = new ArrayList<Long>();
		for(Account ac:a.getChildren()){			
			for(Long id: findIds(ac)){
				list.add(id);
			}
			list.add(ac.getId());			
		}
		return list;
	}

	public void removeAccount() {
		if(accountForRemove == null ) {
			addFacesMessageFromResourceBundle("account.useInEntry");
			return;
		}
		
		parentAccount = accountForRemove.getParent();
		parentAccount.remove(accountForRemove);
		this.update();
		cleanAccounts();
	}

	public void changeAccountForRemove(Account a) {		
		if(!canDelete(a)) return;		
		accountForRemove = a;
	}

	@Override
	public String update() {
		this.setInstance(mainAccount);
		return super.update();
	}

	private boolean existRepeatCodes(List<String> codes) {		
		for (int i = 0; i < codes.size(); i++) {
			for (int j = i + 1; j < codes.size(); j++) {				
				if (codes.get(i).equals(codes.get(j))) {					
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Devuelve Accounts incluyendo los SubAccounts
	 * @param accounts cuentas contables
	 * @return List<Account>
	 */
	private List<Account> findAccounts(List<Account> accounts) {
		List<Account> list = new ArrayList<Account>();
		for (Account a : accounts) {
			list.add(a);
			if (a.getChildren().size() > 0) {
				List<Account> codes2 = findAccounts(a.getChildren());
				if (codes2.size() > 0) {
					list.addAll(codes2);
				}
			}
		}
		return list;
	}

	private List<String> findAccountCodes(List<Account> accounts) {
		List<String> codes = new ArrayList<String>();
		for (Account a : accounts) {
			if(a.getAccountCode() != null){				
				codes.add(a.getAccountCode());
			}
		}
		return codes;
	}
		
	private List<String> findBudgetCertificateCodes(List<Account> accounts) {
		List<String> codes = new ArrayList<String>();
		for (Account a : accounts) {
			if(a.getBudgetCertificateCode() != null){				
				codes.add(a.getBudgetCertificateCode());
			}
		}
		return codes;
	}
	
	public void changeParent(Account a) {		
		parentAccount = a;
		newAccount = new Account();
	}

	public boolean canChange() {		
		if(newAccount == null) return true;		
		if(newAccount.getBudgetCertificateCode() != null) isAuxiliar = true;
		if (newAccount.getEntries().size() > 0) {			
			return false;
		}		
		return true;
	}

	public void changeInstance(Account a) {
		newAccount = a;
		parentAccount = a.getParent();
	}
	
	public List<Account> findAccounts() {				
		Query query = getPersistenceContext().createNamedQuery("Account.findAccounts");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Account findMainAccount() {
		if(mainAccount != null) return mainAccount;		
		Query query = getPersistenceContext().createNamedQuery("Account.findFirstLevel");
		try {
			mainAccount = (Account)query.getSingleResult();
		} catch (Exception e) {
			logger.info("NO SINGLE ROOT ACCOUNT " + e);
		}
		
		return null;
	}

	public void createNewAccount() {
		newAccount = new Account();
		parentAccount = null;
	}	
	
	@SuppressWarnings("unchecked")
	public void searchAccounts(){
		if(criteria == null){
			accounts = null;
			return;
		}
		Query query = getEntityManager().createNamedQuery("Account.searchByCriteria");		
		query.setParameter("criteria", criteria);
		accounts = query.getResultList();		
	}
	
	public void cleanAccounts(){		
		criteria = null;
		accounts = null;
	}

	public void addAccount() {
		List<Account> accounts = findAccounts(mainAccount.getChildren());
		
		List<String> codes = findAccountCodes(accounts);
		if(mainAccount.getAccountCode() != null) {
			codes.add(mainAccount.getAccountCode());
		}
				
		if (existRepeatCodes(codes)) {
			addFacesMessageFromResourceBundle("account.existAccountCode");
			return;
		}
		
		
		codes = findBudgetCertificateCodes(accounts);
		
		if(mainAccount.getBudgetCertificateCode() != null){
			codes.add(mainAccount.getBudgetCertificateCode());			
		}
		
		
		if (!accounts.contains(newAccount)) {
			if(newAccount.getBudgetCertificateCode() != null){
				codes.add(newAccount.getBudgetCertificateCode());				
			}
					}
		if (existRepeatCodes(codes)) {
			addFacesMessageFromResourceBundle("account.existBudgetCertificateCode");
			return;
		}
						
		if(newAccount.getId() == null){
			if (parentAccount == null) {
				mainAccount.add(newAccount);
			} else {
				parentAccount.add(newAccount);
			}
		}
				
		this.update();
		
		isAuxiliar = false;
	}

	public void changeAuxiliarToFalse() {
		isAuxiliar = false;
	}

	public boolean canAddChild(Account a) {		
		if(a == null) return true;
		if (a.getBudgetCertificateCode() == null)return true;
		return false;
	}

	public void setParentAccount(Account parentAccount) {
		this.parentAccount = parentAccount;
	}

	public Account getParentAccount() {
		return parentAccount;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setNewAccount(Account newAccount) {
		this.newAccount = newAccount;
	}

	public Account getNewAccount() {
		return newAccount;
	}

	public void setMainAccount(Account mainAccount) {
		this.mainAccount = mainAccount;
	}

	public Account getMainAccount() {
		return mainAccount;
	}

	public synchronized Account getAccountForRemove() {
		return accountForRemove;
	}

	public void setAccountForRemove(Account accountForRemove) {
		this.accountForRemove = accountForRemove;
	}

	public boolean isAuxiliar() {		
		if(newAccount != null && newAccount.getBudgetCertificateCode() != null) isAuxiliar = true;	
		return isAuxiliar;
	}

	public void setAuxiliar(boolean isAuxiliar) {		
		if (!isAuxiliar) {
			newAccount.setBudgetCertificateCode(null);
			newAccount.setBudgetCertificateName(null);	
		}else{
			newAccount.setIsShowSummary(false);
			newAccount.setShowSubtotal(false);
		}
		this.isAuxiliar = isAuxiliar;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

}
