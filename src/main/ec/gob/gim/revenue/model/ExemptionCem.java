/**
 * 
 */
package ec.gob.gim.revenue.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Resident;

/**
 * @author René
 * @date 2021-07-21
 *
 */
@Audited
@Entity
@TableGenerator(name = "ExemptionCemGenerator", pkColumnName = "name", pkColumnValue = "ExemptionCem", table = "IdentityGenerator", valueColumnName = "value", allocationSize = 1, initialValue = 1)
public class ExemptionCem {

	@Id
	@GeneratedValue(generator = "ExemptionCemGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "resident_id")
	private Resident resident;
	
	@Column(length = 150)
	private String reference;
	
	@Column(length = 200)
	private String explanation;
	
	@ManyToOne
	@JoinColumn(name = "property_id")
	private Property property;
	
	private BigDecimal discountPercentage;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "type_id", nullable = true, referencedColumnName = "id")
	private ItemCatalog type;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	private Boolean active;
	
	@Version
	private Long version = 0L;
	
	
	@Transient
	private String estado;
	/**
	 * 
	 */
	public ExemptionCem() {
		super();
		this.discountPercentage =BigDecimal.ZERO;
		this.active = Boolean.TRUE;
		this.creationDate = new Date();
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
	 * @return the resident
	 */
	public Resident getResident() {
		return resident;
	}

	/**
	 * @param resident the resident to set
	 */
	public void setResident(Resident resident) {
		this.resident = resident;
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
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * @param explanation the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(Property property) {
		this.property = property;
	}

	/**
	 * @return the discountPercentage
	 */
	public BigDecimal getDiscountPercentage() {
		return discountPercentage;
	}

	/**
	 * @param discountPercentage the discountPercentage to set
	 */
	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	/**
	 * @return the type
	 */
	public ItemCatalog getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ItemCatalog type) {
		this.type = type;
	}
	
	
	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	

	public String getEstado() {
		return active? "SI" : "NO";
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return "ExemptionCem [id=" + id + ", resident=" + resident
				+ ", reference=" + reference + ", explanation=" + explanation
				+ ", property=" + property + ", discountPercentage="
				+ discountPercentage + ", type=" + type + ", creationDate="
				+ creationDate + ", active=" + active + ", version=" + version
				+ "]";
	}
	
}
