package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;
import java.util.List;

public class BudgetItemGrouped {

	private Long idBudget;
	private List<BudgetItem> budgetItems;
	private BigDecimal subTotal;
	
	public BudgetItemGrouped(){}

	public BudgetItemGrouped(Long idBudget, List<BudgetItem> budgetItems) {
		this.idBudget = idBudget;
		this.budgetItems = budgetItems;
	}

	public BudgetItemGrouped(List<BudgetItem> budgetItems) {
		this.budgetItems = budgetItems;
	}

	public List<BudgetItem> getBudgetItems() {
		return budgetItems;
	}

	public void setBudgetItems(List<BudgetItem> budgetItems) {
		this.budgetItems = budgetItems;
	}

	public Long getIdBudget() {
		return idBudget;
	}

	public void setIdBudget(Long idBudget) {
		this.idBudget = idBudget;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

}
