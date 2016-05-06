package ec.gob.gim.income.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.Entry;

@Audited
@Entity
@TableGenerator(
	 name="AccountGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Account",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = { 
		@NamedQuery(name="Account.findFirstLevel", 
					query="select a from Account a "							
						+ "LEFT JOIN FETCH a.children " 
						+ "WHERE a.parent is null order by a.accountCode"),
				
		@NamedQuery(name="Account.findAccounts", 
					query="select a from Account a "							
						+ "LEFT JOIN FETCH a.entries "
						//+ "LEFT JOIN FETCH a.children"
						),
		
		@NamedQuery(name="Account.searchByCriteria", query="select a from Account a " 
					+ "LEFT JOIN FETCH a.entries " 
					+ "where " +
					"lower(a.accountName) like lower(concat(:criteria,'%')) or " +
					"a.accountCode like concat(:criteria,'%') or " +
					"a.budgetCertificateCode like concat(:criteria,'%') order by a.accountCode"),		
		
		@NamedQuery(name="Account.findForShowSubtotal", query="select a from Account a where a.showSubtotal = true order by a.accountCode"),
		@NamedQuery(name="Account.findForSummary", query="select a from Account a where a.isShowSummary = true order by a.accountCode"),
		@NamedQuery(name="Account.findChildsAccountByCriteria", query="select a from Account a where a.id not in (select distinct (ac1.parent.id) from Account ac1 where ac1.parent.id is not null) " +
				"and (lower (a.accountCode) like lower(concat(:criteria,'%')) or lower(a.accountName) like lower(concat(:criteria,'%'))) order by a.accountCode"),
		@NamedQuery(name="Account.findChildsSubLineAccountByCriteria", query="select a from Account a where a.isSubLineAccount = true and a.id not in (select distinct (ac1.parent.id) from Account ac1 where ac1.parent.id is not null) " +
				"and (lower (a.accountCode) like lower(concat(:criteria,'%')) or lower(a.accountName) like lower(concat(:criteria,'%'))) order by a.accountCode"),
		@NamedQuery(name="Account.findChildsAccount", query="select a from Account a where a.id not in (select distinct (ac1.parent.id) from Account ac1 where ac1.parent.id is not null) " +
				"and a.accountCode like concat(:accountCode,'%')) order by a.accountCode")}) 
public class Account{
	
	@Id
	@GeneratedValue(generator="AccountGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@OneToMany(mappedBy = "parent",cascade=CascadeType.ALL, fetch=FetchType.EAGER)	
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Account> children;

	@ManyToOne(fetch=FetchType.LAZY)	
	private Account parent;	
	
	@Column	
	private String accountName;	
	
	@Column(unique = true)
	private String accountCode;
		
	@Column(unique = true)
	private String previousYearsAccountCode;
	
	@Column(unique = true)
	private String futureYearsAccountCode;

	@Column	
	private String budgetCertificateName;
	
	@Column(unique = true)
	private String budgetCertificateCode;
	
	@Column(unique = true)
	private String previousYearsBudgetCertificateCode;
	
	@Column(unique = true)
	private String futureYearsBudgetCertificateCode;
	
	@Column
	private String parameterFutureEmission;
	
	private Boolean isShowSummary;
	
	private Boolean showSubtotal;	
		
	private Boolean isSubLineAccount;
	
	@OneToMany(mappedBy = "account",fetch=FetchType.LAZY)	
	private List<Entry> entries;
	
	@OneToMany(mappedBy = "taxAccount",fetch=FetchType.LAZY)
	private List<Tax> taxes;
	
	public Account(){
		children = new ArrayList<Account>();
		entries = new ArrayList<Entry>();
		taxes = new ArrayList<Tax>();
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setParent(Account parent) {
		this.parent = parent;
	}

	public Account getParent() {
		return parent;
	}
	
	public List<Account> getChildren() {
		return children;
	}

	public void setChildren(List<Account> children) {
		this.children = children;
	}
	
	public void add(Account a){
		if(!children.contains(a) && a != null){
			children.add(a);
			a.setParent(this);
		}
	}
	
	public void remove(Account a){
		if(children.contains(a)){
			children.remove(a);
			a.setParent(null);
		}
	}
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getBudgetCertificateName() {
		return budgetCertificateName;
	}

	public void setBudgetCertificateName(String budgetCertificateName) {
		this.budgetCertificateName = budgetCertificateName;
	}
	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getBudgetCertificateCode() {
		return budgetCertificateCode;
	}

	public void setBudgetCertificateCode(String budgetCertificateCode) {
		this.budgetCertificateCode = budgetCertificateCode;
	}

	public Boolean getIsShowSummary() {
		return isShowSummary;
	}

	public void setIsShowSummary(Boolean isShowSummary) {
		this.isShowSummary = isShowSummary;
	}
	
	public List<Tax> getTaxes() {
		return taxes;
	}

	public void setTaxes(List<Tax> taxes) {
		this.taxes = taxes;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}

	public Boolean getShowSubtotal() {
		return showSubtotal;
	}

	public void setShowSubtotal(Boolean showSubtotal) {
		this.showSubtotal = showSubtotal;
	}

	public Boolean getIsSubLineAccount() {
		return isSubLineAccount;
	}

	public void setIsSubLineAccount(Boolean isSubLineAccount) {
		this.isSubLineAccount = isSubLineAccount;
	}

	public String getPreviousYearsAccountCode() {
		return previousYearsAccountCode;
	}

	public void setPreviousYearsAccountCode(String previousYearsAccountCode) {
		this.previousYearsAccountCode = previousYearsAccountCode;
	}

	public String getFutureYearsAccountCode() {
		return futureYearsAccountCode;
	}

	public void setFutureYearsAccountCode(String futureYearsAccountCode) {
		this.futureYearsAccountCode = futureYearsAccountCode;
	}

	public String getPreviousYearsBudgetCertificateCode() {
		return previousYearsBudgetCertificateCode;
	}

	public void setPreviousYearsBudgetCertificateCode(
			String previousYearsBudgetCertificateCode) {
		this.previousYearsBudgetCertificateCode = previousYearsBudgetCertificateCode;
	}

	public String getFutureYearsBudgetCertificateCode() {
		return futureYearsBudgetCertificateCode;
	}

	public void setFutureYearsBudgetCertificateCode(
			String futureYearsBudgetCertificateCode) {
		this.futureYearsBudgetCertificateCode = futureYearsBudgetCertificateCode;
	}

	public String getParameterFutureEmission() {
		return parameterFutureEmission;
	}

	public void setParameterFutureEmission(String parameterFutureEmission) {
		this.parameterFutureEmission = parameterFutureEmission;
	}
}