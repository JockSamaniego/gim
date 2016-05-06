package ec.gob.gim.cadaster.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:42
 */
@Audited
@Entity
@TableGenerator(
	 name="PropertyLandUseGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="PropertyLandUse",
	 initialValue=1, allocationSize=1
)
 public class PropertyLandUse {
	
	@Id
	@GeneratedValue(generator="PropertyLandUseGenerator",strategy=GenerationType.TABLE)
	private Long id;

	private Boolean hasOperatingPermit;
	/*
	 * Tarjas
	 */
	private Integer slices;
	
	@ManyToOne
	@JoinColumn(name="landUse_id")
	private LandUse landUse;
	
	@ManyToOne
	private Property property;

	public PropertyLandUse(){
		landUse = new LandUse();
	}

	public Boolean getHasOperatingPermit() {
		return hasOperatingPermit;
	}

	public void setHasOperatingPermit(Boolean hasOperatingPermit) {
		this.hasOperatingPermit = hasOperatingPermit;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LandUse getLandUse() {
		return landUse;
	}

	public void setLandUse(LandUse landUse) {
		this.landUse = landUse;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}

	/**
	 * @return the slices
	 */
	public Integer getSlices() {
		return slices;
	}

	/**
	 * @param slices the slices to set
	 */
	public void setSlices(Integer slices) {
		this.slices = slices;
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

		PropertyLandUse that = (PropertyLandUse) o;
		if (getId() != null ? !(getId().equals(that.getId())) : that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}
	
	
}//end PropertyLandUse