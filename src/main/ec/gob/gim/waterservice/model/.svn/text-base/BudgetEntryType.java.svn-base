package ec.gob.gim.waterservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:45
 */

@Audited
@Entity
@TableGenerator(name = "BudgetEntryTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "BudgetEntryType", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "BudgetEntryType.findAll", query = "select o from BudgetEntryType o order by o.name"),
		@NamedQuery(name = "BudgetEntryType.findAllNames", query = "select bet.name from BudgetEntryType bet ") })
public class BudgetEntryType {

	@Id
	@GeneratedValue(generator = "BudgetEntryTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;
	@Column(length = 50, nullable = false)
	private String name;

	public BudgetEntryType() {

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

	@Override
	public int hashCode() {
		int hash = 13;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		BudgetEntryType that = (BudgetEntryType) o;
		if (getId() != null ? !(getId().equals(that.getId()))
				: that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}

}// end ServiceType