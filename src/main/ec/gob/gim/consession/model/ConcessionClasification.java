package ec.gob.gim.consession.model;

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
 * @author richard
 * @version 1.0
 * @created 02-ago-2013 16:54:33
 */
@Audited
@Entity
@TableGenerator(name = "ConcessionClasificationGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ConcessionClasification", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "ConcessionClasification.findAll", query = "SELECT cc FROM ConcessionClasification cc order by cc.name") })
public class ConcessionClasification {

	@Id
	@GeneratedValue(generator = "ConcessionClasificationGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;

	public ConcessionClasification() {

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
		this.name = name.toUpperCase();
	}

}// end ConcessionClasification