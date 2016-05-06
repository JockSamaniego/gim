package ec.gob.gim.commercial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author jock samaniego
 */

@Audited
@Entity
@TableGenerator(name = "FireNamesGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "FireNames", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "FireNames.findAllNames", query = "select fn from FireNames fn")})
public class FireNames {

	@Id
	@GeneratedValue(generator = "FireNamesGenerator", strategy = GenerationType.TABLE)
	private Integer id;
	
	@JoinColumn(name="name")
	@Column(length = 80)
	private String name;
	
	@JoinColumn(name="codeName")
	@Column(length = 20)
	private String codeName;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

}