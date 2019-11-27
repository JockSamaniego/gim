package ec.gob.gim.cadaster.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import ec.gob.gim.common.model.ItemCatalog;

@Entity
@TableGenerator(name = "AffectationFactorGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "AffectationFactor", initialValue = 1, allocationSize = 1)
public class AffectationFactor {

	@Id
	@GeneratedValue(generator = "AffectationFactorGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private String categoty;

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

	public String getCategoty() {
		return categoty;
	}

	public void setCategoty(String categoty) {
		this.categoty = categoty;
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
				+ ((categoty == null) ? 0 : categoty.hashCode());
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
		if (categoty == null) {
			if (other.categoty != null)
				return false;
		} else if (!categoty.equals(other.categoty))
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
		return "AffectationFactor [id=" + id + ", categoty=" + categoty
				+ ", type=" + type + ", active=" + active + "]";
	}
}
