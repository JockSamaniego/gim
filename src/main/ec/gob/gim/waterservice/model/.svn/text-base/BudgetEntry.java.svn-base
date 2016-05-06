package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;

import javax.persistence.Column;
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

import ec.gob.gim.revenue.model.Entry;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:44
 */

@Audited
@Entity
@TableGenerator(name = "BudgetEntryGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "BudgetEntry", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "BudgetEntry.findByName", 
			query = "select budgetEntry from BudgetEntry budgetEntry " 
					+ "left join fetch budgetEntry.entry entry "
					+ "left join fetch budgetEntry.budgetEntryType entryType "
					+ "where "		
					+ "lower(budgetEntry.name) LIKE lower(concat(:criteria,'%'))") })
public class BudgetEntry {

	@Id
	@GeneratedValue(generator = "BudgetEntryGenerator", strategy = GenerationType.TABLE)
	private Long id;
	@Column(length = 60, nullable = false)
	private String name;
	@Column(length = 200)
	private String description;

	private Boolean isTaxable;
	
	private BigDecimal value;

	@Column(length = 30)
	private String unity;

	@ManyToOne
	@JoinColumn(name = "budgetEntryType_id")
	private BudgetEntryType budgetEntryType;

	/**
	 * Revisar si la relación Service-Entry es necesaria, recordando que el tipo
	 * de contrato tiene ya una asociación con Entry. Tengo la idea que los
	 * servicios generan un titulo de crédito de un solo valor total en el
	 * reporte.
	 */
	@ManyToOne
	@JoinColumn(name = "entry_id")
	private Entry entry;

	public BudgetEntry() {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public BudgetEntryType getBudgetEntryType() {
		return budgetEntryType;
	}

	public void setBudgetEntryType(BudgetEntryType budgetEntryType) {
		this.budgetEntryType = budgetEntryType;
	}

	/**
	 * Revisar si la relación Service-Entry es necesaria, recordando que el tipo
	 * de contrato tiene ya una asociación con Entry. Tengo la idea que los
	 * servicios generan un titulo de crédito de un solo valor total en el
	 * reporte
	 * 
	 * @return the entry
	 */
	public Entry getEntry() {
		return entry;
	}

	/**
	 * Revisar si la relación Service-Entry es necesaria, recordando que el tipo
	 * de contrato tiene ya una asociación con Entry. Tengo la idea que los
	 * servicios generan un titulo de crédito de un solo valor total en el
	 * reporte
	 * 
	 * @param entry
	 *            the entry to set
	 */
	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public String getUnity() {
		return unity;
	}

	public void setUnity(String unity) {
		this.unity = unity.toUpperCase();
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Boolean getIsTaxable() {
		return isTaxable;
	}

	public void setIsTaxable(Boolean isTaxable) {
		this.isTaxable = isTaxable;
	}

}// end Service