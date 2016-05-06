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
 * @created 04-Ago-2011 16:30:28
 */
@Audited
@Entity
@TableGenerator(name = "ConsumptionStateGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ConsumptionState", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "ConsumptionState.findAll", query = "SELECT cs FROM ConsumptionState cs") })
public class ConsumptionState {

	@Id
	@GeneratedValue(generator = "ConsumptionStateGenerator", strategy = GenerationType.TABLE)
	private Long id;
	@Column(length = 20, nullable = false)
	private String name;

	public ConsumptionState() {

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
		this.name = name;
	}
}// end ConsumptionState