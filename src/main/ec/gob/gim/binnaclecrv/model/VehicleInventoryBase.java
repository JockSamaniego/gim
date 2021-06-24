package ec.gob.gim.binnaclecrv.model;

import java.io.Serializable;

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
 * Bitácora Digital: inventario base para completar el inventario del vehículo retenido
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 07-May-2021
 */
@Audited
@Entity
@TableGenerator(
	name = "VehicleInventoryBaseGenerator", 
	table = "IdentityGenerator", 
	pkColumnName = "name", 
	valueColumnName = "value", 
	pkColumnValue = "VehicleInventoryBase", 
	initialValue = 1, allocationSize = 1)

@NamedQueries({
	@NamedQuery(name="VehicleInventoryBase.findAllActive",
		query="select o from VehicleInventoryBase o "
			+ "where o.active = true order by o.orderColumn"
	)
})

public class VehicleInventoryBase implements Serializable {

	private static final long serialVersionUID = -2614028142495673474L;

	@Id
	@GeneratedValue(generator = "VehicleInventoryBaseGenerator", strategy = GenerationType.TABLE)
	private Long id; 
    
	@Column(length = 40)
	private String name; //Nombre
	private Boolean active; //activo
	private Boolean amountVisible; //ver campo de cantidad
	private Integer orderColumn; //valor para ordenar
    
	public VehicleInventoryBase() {

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the amountVisible
	 */
	public Boolean getAmountVisible() {
		return amountVisible;
	}

	/**
	 * @param amountVisible the amountVisible to set
	 */
	public void setAmountVisible(Boolean amountVisible) {
		this.amountVisible = amountVisible;
	}

	/**
	 * @return the orderColumn
	 */
	public Integer getOrderColumn() {
		return orderColumn;
	}

	/**
	 * @param orderColumn the orderColumn to set
	 */
	public void setOrderColumn(Integer orderColumn) {
		this.orderColumn = orderColumn;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VehicleInventoryBase other = (VehicleInventoryBase) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VehicleInventoryBase [id=" + id + ", name=" + name + ", active=" + active + "]";
	}
    
}
