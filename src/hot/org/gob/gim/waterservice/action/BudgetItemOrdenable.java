package org.gob.gim.waterservice.action;

import java.util.ArrayList;
import java.util.List;

import ec.gob.gim.waterservice.model.BudgetEntryType;
import ec.gob.gim.waterservice.model.BudgetItem;

public class BudgetItemOrdenable {

	private BudgetEntryType budgetEntryType;
	private List<BudgetItem> budgetItems=new ArrayList<BudgetItem>();

	public BudgetEntryType getBudgetEntryType() {
		return budgetEntryType;
	}

	public void setBudgetEntryType(BudgetEntryType budgetEntryType) {
		this.budgetEntryType = budgetEntryType;
	}

	public List<BudgetItem> getBudgetItems() {
		return budgetItems;
	}

	public void setBudgetItems(List<BudgetItem> budgetItems) {
		this.budgetItems = budgetItems;
	}

}
