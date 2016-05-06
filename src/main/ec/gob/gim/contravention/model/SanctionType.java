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
@TableGenerator(name = "SanctionTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "SanctionType", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "SanctionType.findAll", query = "SELECT st FROM SanctionType st order by st.name")
})
public class SanctionType {

	@Id
	@GeneratedValue(generator = "SanctionTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length=40)
	private String name;

	public SanctionType() {

	}

	public void finalize() throws Throwable {

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
}// end SanctionType