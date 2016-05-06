package ec.gob.gim.contravention.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

/**
 * @author richard
 * @version 1.0
 * @created 23-oct-2013 15:57:36
 */
@Entity
@TableGenerator(name = "ProvenanceGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Provenance", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "Provenance.findAll", query = "SELECT p FROM Provenance p order by p.name")
})
public class Provenance {

	@Id
	@GeneratedValue(generator = "ProvenanceGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private Boolean isActive;

	@Column(length = 60)
	private String name;

	public Provenance() {

	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}// end Provenance