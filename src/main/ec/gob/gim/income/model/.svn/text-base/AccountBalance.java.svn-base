package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountBalance{
		
	private List<AccountBalance> children;

	private AccountBalance parent;	
	
	private Account account;
	
	private BigDecimal value;
	
	private BigDecimal nextYears;
	
	private BigDecimal previousYears;
		
	private BigDecimal discount;
	
	private BigDecimal surcharge;
	
	private BigDecimal interest;
	
	private BigDecimal taxes;	
	
	private BigDecimal total;
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public AccountBalance(){
		children = new ArrayList<AccountBalance>();
	}
	
	public void setParent(AccountBalance parent) {
		this.parent = parent;
	}

	public AccountBalance getParent() {
		return parent;
	}
	
	public List<AccountBalance> getChildren() {
		return children;
	}

	public void setChildren(List<AccountBalance> children) {
		this.children = children;
	}
	
	public void add(AccountBalance a){
		if(!children.contains(a) && a != null){
			children.add(a);
			a.setParent(this);
		}
	}
	
	public void remove(AccountBalance a){
		if(children.contains(a)){
			children.remove(a);
			a.setParent(null);
		}
	}
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(BigDecimal surcharge) {
		this.surcharge = surcharge;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getTaxes() {
		return taxes;
	}

	public void setTaxes(BigDecimal taxes) {
		this.taxes = taxes;
	}
	
	public BigDecimal getNextYears() {
		return nextYears;
	}

	public void setNextYears(BigDecimal nextYears) {
		this.nextYears = nextYears;
	}

	public BigDecimal getPreviousYears() {
		return previousYears;
	}

	public void setPreviousYears(BigDecimal previousYears) {
		this.previousYears = previousYears;
	}
	
}
