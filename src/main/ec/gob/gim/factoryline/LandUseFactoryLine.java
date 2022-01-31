package ec.gob.gim.factoryline;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;


/**
 * Usos de Suelo para Línea de Fábrica
 * 
 * @author Ronald Paladines C
 * @version 1.0
 * @created 26-Nov-2021
 */
@Audited
@Entity
@TableGenerator(
	 name="LandUseFactoryLineGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="LandUseFactoryLine",
	 initialValue=1, allocationSize=1
)

public class LandUseFactoryLine implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4387451898469245510L;

	@Id
	@GeneratedValue(generator="LandUseFactoryLineGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=10)
    private String code;
	private String name;
	private Boolean applicable;

	@ManyToMany
    @JoinTable(name = "landuse_complementary")
	private Set<TerritorialPolygon> tpComplementaries = new HashSet<TerritorialPolygon>();
	
	@ManyToMany
    @JoinTable(name = "landuse_prohibited")
	private Set<TerritorialPolygon> tpProhibiteds = new HashSet<TerritorialPolygon>();
	
	@ManyToMany
    @JoinTable(name = "landuse_restricted")
	private Set<TerritorialPolygon> tpRestricteds = new HashSet<TerritorialPolygon>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the applicable
	 */
	public Boolean getApplicable() {
		return applicable;
	}

	/**
	 * @param applicable the applicable to set
	 */
	public void setApplicable(Boolean applicable) {
		this.applicable = applicable;
	}

	/**
	 * @return the tpComplementaries
	 */
	public Set<TerritorialPolygon> getTpComplementaries() {
		return tpComplementaries;
	}

	/**
	 * @param tpComplementaries the tpComplementaries to set
	 */
	public void setTpComplementaries(Set<TerritorialPolygon> tpComplementaries) {
		this.tpComplementaries = tpComplementaries;
	}

	/**
	 * @return the tpProhibiteds
	 */
	public Set<TerritorialPolygon> getTpProhibiteds() {
		return tpProhibiteds;
	}

	/**
	 * @param tpProhibiteds the tpProhibiteds to set
	 */
	public void setTpProhibiteds(Set<TerritorialPolygon> tpProhibiteds) {
		this.tpProhibiteds = tpProhibiteds;
	}

	/**
	 * @return the tpRestricteds
	 */
	public Set<TerritorialPolygon> getTpRestricteds() {
		return tpRestricteds;
	}

	/**
	 * @param tpRestricteds the tpRestricteds to set
	 */
	public void setTpRestricteds(Set<TerritorialPolygon> tpRestricteds) {
		this.tpRestricteds = tpRestricteds;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicable == null) ? 0 : applicable.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		LandUseFactoryLine other = (LandUseFactoryLine) obj;
		if (applicable == null) {
			if (other.applicable != null)
				return false;
		} else if (!applicable.equals(other.applicable))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
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
		return "LandUseFactoryLine [id=" + id + ", code=" + code + ", name="
				+ name + ", applicable=" + applicable + "]";
	}

}
