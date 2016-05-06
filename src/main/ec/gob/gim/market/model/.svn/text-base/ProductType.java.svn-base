package ec.gob.gim.market.model;

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
 * @created 12-Dic-2011 10:14:56
 */
@Audited
@Entity
@TableGenerator(name = "ProductTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ProductType", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "ProductType.findAll", query = "SELECT p FROM ProductType p order by p.name"),
		@NamedQuery(name = "ProductType.findByName", query = "SELECT p FROM ProductType p WHERE "
				+ "lower(p.name) LIKE lower(concat(:criteria,'%')) order by p.name") })
public class ProductType {

	@Id
	@GeneratedValue(generator = "ProductTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 25, nullable = false)
	private String name;

	private Boolean isActive;

	public ProductType() {

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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}// end ProductType