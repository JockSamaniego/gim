package ec.gob.gim.ant.ucot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Street;
import ec.gob.gim.revenue.model.MunicipalBondStatus;

/**
 * 
 * @author mack
 * @date 2017-06-28T09:40:05
 */

@Audited
@Entity
@TableGenerator(name = "ItemCatalogGenerator", table = "IdentityGenerator",
pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ItemCatalog", initialValue = 1, allocationSize = 1)
public class ItemCatalog {

	@Id
	@GeneratedValue(generator = "ItemCatalogGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 100)
	private String value;
	

	@Column(length = 100)
	private String description;

	@ManyToOne
	@JoinColumn(name="catalog_id")
	private Catalogue catalog;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Catalogue getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalogue catalog) {
		this.catalog = catalog;
	}
}