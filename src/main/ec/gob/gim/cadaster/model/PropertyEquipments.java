package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author jock
 * @version 1.0
 * @created 16-SEP-2022 */
@Audited
@Entity
@TableGenerator(
	 name="PropertyEquipmentsGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="PropertyEquipments",
	 initialValue=1, allocationSize=1
)
 public class PropertyEquipments {
	
	@Id
	@GeneratedValue(generator="PropertyEquipmentsGenerator",strategy=GenerationType.TABLE)
	private Long id;

	private Boolean enabled;

	private BigDecimal amount;
	
	@ManyToOne
	@JoinColumn(name="equipment_id")
	private Equipments equipments;
	
	@ManyToOne
	private Property property;

	public PropertyEquipments(){
		equipments = new Equipments();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Equipments getEquipments() {
		return equipments;
	}

	public void setEquipments(Equipments equipments) {
		this.equipments = equipments;
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

		PropertyEquipments that = (PropertyEquipments) o;
		if (getId() != null ? !(getId().equals(that.getId())) : that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}
	
	
}//end PropertyEquipments