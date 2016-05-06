package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:26
 */

@Audited
@Entity
@TableGenerator(name = "BudgetItemGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "BudgetItem", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "BudgetItem.findByBudgetGroupedByEntry", query = "select distinct entry from BudgetItem bi "
				+ "join bi.budgetEntry budgetEntry "
				+ "join budgetEntry.entry entry "
				+ "join entry.account ac "
				+ "where " + "bi.budget.id = :budgetId"),
		@NamedQuery(name = "BudgetItem.addByBudgetEntry", query = "select SUM(bi.total) from BudgetItem bi "
				+ "left join bi.budgetEntry budgetEntry "
				+ "left join budgetEntry.entry entry "
				+ "where "
				+ "bi.budget.id = :budgetId and entry.id = :entryId"),

		@NamedQuery(name = "BudgetItem.findByBudgetEntry", query = "select bi from BudgetItem bi "
				+ "left join bi.budgetEntry budgetEntry "
				+ "left join budgetEntry.entry entry "
				+ "where "
				+ "bi.budget.id = :budgetId and entry.id = :entryId"),
				
		@NamedQuery(name = "BudgetItem.addByBudgetEntries", query = "select NEW ec.gob.gim.waterservice.model.BudgetItemResult(entry.id, SUM(bi.total)) from BudgetItem bi "
				+ "left join bi.budgetEntry budgetEntry "
				+ "left join budgetEntry.entry entry "
				+ "where "
				+ "bi.budget.id = :budgetId and entry.id in (:entriesId) GROUP BY entry.id")

})
public class BudgetItem {

	@Id
	@GeneratedValue(generator = "BudgetItemGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private BigDecimal quantity;
	private Long tax;
	private BigDecimal total;
	private BigDecimal value;

	@ManyToOne
	@JoinColumn(name = "budgetEntry_id")
	private BudgetEntry budgetEntry;

	@ManyToOne
	private Budget budget;

	/*
	 * @Transient private int amount;
	 */

	public BudgetItem() {
		// quantity=new Long(1);
		// this.amount = 1;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTax() {
		return tax;
	}

	public void setTax(Long tax) {
		this.tax = tax;
	}

	public BudgetEntry getBudgetEntry() {
		return budgetEntry;
	}

	public void setBudgetEntry(BudgetEntry budgetEntry) {
		this.budgetEntry = budgetEntry;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public Budget getBudget() {
		return budget;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/*
	 * public int getAmount() { return amount; }
	 * 
	 * public void setAmount(int amount) { this.amount = amount; }
	 */

}// end BudgetItem