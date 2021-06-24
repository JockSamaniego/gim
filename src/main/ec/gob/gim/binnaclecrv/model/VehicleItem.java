package ec.gob.gim.binnaclecrv.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * Bitácora Digital: objetos encontrados en el vehículo retenido
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 07-May-2021
 */
@Audited
@Entity
@TableGenerator(name = "VehicleItemGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "VehicleItem", initialValue = 1, allocationSize = 1)
public class VehicleItem implements Serializable {

	private static final long serialVersionUID = -5730758051187813160L;

	@Id
	@GeneratedValue(generator = "VehicleItemGenerator", strategy = GenerationType.TABLE)
	private Long id; 
    
	@Column(length = 40)
	private String name; //Nombres
	private Integer amount; //Cantidad que está en el inventario
    
	@ManyToOne
	private BinnacleCRV binnacleCRV;

	public VehicleItem() {

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
	 * @return the amount
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * @return the binnacleCRV
	 */
	public BinnacleCRV getBinnacleCRV() {
		return binnacleCRV;
	}

	/**
	 * @param binnacleCRV the binnacleCRV to set
	 */
	public void setBinnacleCRV(BinnacleCRV binnacleCRV) {
		this.binnacleCRV = binnacleCRV;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
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
		VehicleItem other = (VehicleItem) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
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
		return "VehicleItem [id=" + id + ", name=" + name + ", amount=" + amount + "]";
	}

}
