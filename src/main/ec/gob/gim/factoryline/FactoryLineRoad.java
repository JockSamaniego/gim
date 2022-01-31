package ec.gob.gim.factoryline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;


/**
 * Vías Línea de Fábrica: información de vías e higrografía en líneas de fábrica
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 05-Ene-2022
 */
@Audited
@Entity
@TableGenerator(
	 name="FactoryLineRoadGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="FactoryLineRoad",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = {
	@NamedQuery(name = "FactoryLineRoad.findAll", query = "select f from FactoryLineRoad f " +
			"order by f.denomination, f.location"),
})

public class FactoryLineRoad implements Serializable{

    private static final long serialVersionUID = -8055220383069772954L;
	
	@Id
    @GeneratedValue(generator="FactoryLineRoadGenerator",strategy=GenerationType.TABLE)
	private Long id;
    @Column(length=50)
    private String denomination;
    @Column(length=10)
    private String width;
    @Column(length=100)
    private String reference;
    @Column(length=30)
    private String location;
    @Column(length=200)
    private String obs;
    
	@ManyToMany
    @JoinTable(name = "road_hydrography")
	private Set<FactoryLine> factoryLines = new HashSet<FactoryLine>();

	public FactoryLineRoad() {
        
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
	 * @return the denomination
	 */
	public String getDenomination() {
		return denomination;
	}

	/**
	 * @param denomination the denomination to set
	 */
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	/**
	 * @return the width
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the obs
	 */
	public String getObs() {
		return obs;
	}

	/**
	 * @param obs the obs to set
	 */
	public void setObs(String obs) {
		this.obs = obs;
	}

	/**
	 * @return the factoryLines
	 */
	public Set<FactoryLine> getFactoryLines() {
		return factoryLines;
	}

	/**
	 * @param factoryLines the factoryLines to set
	 */
	public void setFactoryLines(Set<FactoryLine> factoryLines) {
		this.factoryLines = factoryLines;
	}

	public void add(FactoryLine factoryLine){
		if (!factoryLines.contains(factoryLine)){
			factoryLines.add(factoryLine);
		}
	}
	
	public void remove(FactoryLine factoryLine){
		if (factoryLines.contains(factoryLine)){
			factoryLines.remove(factoryLine);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((denomination == null) ? 0 : denomination.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((obs == null) ? 0 : obs.hashCode());
		result = prime * result
				+ ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((width == null) ? 0 : width.hashCode());
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
		FactoryLineRoad other = (FactoryLineRoad) obj;
		if (denomination == null) {
			if (other.denomination != null)
				return false;
		} else if (!denomination.equals(other.denomination))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (obs == null) {
			if (other.obs != null)
				return false;
		} else if (!obs.equals(other.obs))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (width == null) {
			if (other.width != null)
				return false;
		} else if (!width.equals(other.width))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FactoryLineRoad [id=" + id + ", denomination=" + denomination
				+ ", width=" + width + ", reference=" + reference
				+ ", location=" + location + ", obs=" + obs + "]";
	}

}
