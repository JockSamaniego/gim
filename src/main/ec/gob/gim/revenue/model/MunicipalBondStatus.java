package ec.gob.gim.revenue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Identifiable;

/**
 * Permite verificar si un titulo de credito ha sido anulado, recalculado,
 * pagado o cualquier otro estado posible
 * 
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:30
 */
@Audited
@Entity
@TableGenerator(
		name = "MunicipalBondStatusGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "MunicipalBondStatus", 
		initialValue = 1, allocationSize = 1
)
@NamedQueries(value= {
		@NamedQuery(name="MunicipalBondStatus.findAll", 
				query="Select municipalBondStatus from MunicipalBondStatus municipalBondStatus"),
		@NamedQuery(name="MunicipalBondStatus.findByName", 
				query="Select municipalBondStatus from MunicipalBondStatus municipalBondStatus " +
					"where lower(municipalBondStatus.name) like lower(concat(:name, '%'))"),
		@NamedQuery(name="MunicipalBondStatus.findById", 
				query="Select municipalBondStatus from MunicipalBondStatus municipalBondStatus " +
					"where municipalBondStatus.id = :id")		
})
public class MunicipalBondStatus extends Identifiable{

	@Id
	@GeneratedValue(generator = "MunicipalBondStatusGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(length = 50, nullable=false)
	private String name;
	
	private String description;

	public MunicipalBondStatus() {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof MunicipalBondStatus)) {
			return false;
		}
		MunicipalBondStatus other = (MunicipalBondStatus) object;
				
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return name;
	}
}// end MunicipalBondStatus