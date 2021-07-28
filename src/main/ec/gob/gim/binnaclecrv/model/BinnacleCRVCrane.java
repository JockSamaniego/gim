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
 * Bitácora Digital: datos de la Grúa que transporta el vehículo retenido, 
 * si es municipal se debe realizar la preemisión de un título de crédito por los valores generados
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 06-May-2021
 */
@Audited
@Entity
@TableGenerator(name = "BinnacleCRVCraneGenerator", 
	table = "IdentityGenerator", 
	pkColumnName = "name", 
	valueColumnName = "value", 
	pkColumnValue = "BinnacleCRVCrane", 
	initialValue = 1, allocationSize = 1)

@NamedQueries(value = {
	@NamedQuery(name="BinnacleCRVCrane.findAllActive", 
		query="select o from BinnacleCRVCrane o where o.active = true order by o.name")
})

public class BinnacleCRVCrane implements Serializable {

	private static final long serialVersionUID = 2864135557575229111L;

	@Id
	@GeneratedValue(generator = "BinnacleCRVCraneGenerator", strategy = GenerationType.TABLE)
	private Long id; 
    
	@Column(length = 100)
	private String name; //Nombre de la empresa de grúa y número de grúa
	private Boolean active; //activo
	private Boolean generateTaxes; //si genera impuestos por la movilización
    
    public BinnacleCRVCrane() {
        active = true;
        generateTaxes = false;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
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
	 * @return the generateTaxes
	 */
	public Boolean getGenerateTaxes() {
		return generateTaxes;
	}

	/**
	 * @param generateTaxes the generateTaxes to set
	 */
	public void setGenerateTaxes(Boolean generateTaxes) {
		this.generateTaxes = generateTaxes;
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
		BinnacleCRVCrane other = (BinnacleCRVCrane) obj;
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
		return "BinnacleCRVCrane [id=" + id + ", name=" + name + ", active="
				+ active + "]";
	}
    
}
