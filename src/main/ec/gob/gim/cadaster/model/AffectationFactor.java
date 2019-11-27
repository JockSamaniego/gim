	package ec.gob.gim.cadaster.model;

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

import ec.gob.gim.common.model.ItemCatalog;

@Entity
@TableGenerator(name = "AffectationFactorGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "AffectationFactor", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "AffectationFactor.findByCategoryAndStatus", 
			query = "SELECT af FROM AffectationFactor af where af.type.code=:code and af.active=:status") })
public class AffectationFactor {

	@Id
	@GeneratedValue(generator = "AffectationFactorGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private String category;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "type_itm_id", nullable = false, referencedColumnName = "id")
	private ItemCatalog type;

	private Boolean active;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	} 

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ItemCatalog getType() {
		return type;
	}

	public void setType(ItemCatalog type) {
		this.type = type;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result
				+ ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AffectationFactor other = (AffectationFactor) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AffectationFactor [id=" + id + ", category=" + category
				+ ", type=" + type + ", active=" + active + "]";
	}
}
