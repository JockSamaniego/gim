package ec.gob.gim.cadaster.model;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * Avaluo
 * @author wilman
 * @version 1.0
 * @created 10-Ago-2011 18:19:54
 */
@Audited
@Entity
@TableGenerator(
	 name="AppraisalGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Appraisal",
	 initialValue=1, allocationSize=1
)

public class Appraisal {

	@Id
	@GeneratedValue(generator="AppraisalGenerator",strategy=GenerationType.TABLE)
	private Long id;
	/**
	 * LOTE: Area del lote
	 */
	private BigDecimal lot;
	private BigDecimal building;
	private BigDecimal commercialAppraisal;
	private BigDecimal valueBySquareMeter;
	
	private Date date;

	/**
	 * Relationships
	 */
	
//	@ManyToOne
//	private Domain domain;
//	
	@ManyToOne
	private Property property;
	
	public Appraisal(){
		lot = BigDecimal.ZERO;
		building = BigDecimal.ZERO;
		commercialAppraisal = BigDecimal.ZERO;
		date = new Date();
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
	 * @return the lot
	 */
	public BigDecimal getLot() {
		return lot;
	}

	/**
	 * @param lot the lot to set
	 */
	public void setLot(BigDecimal lot) {
		this.lot = lot;
	}

	/**
	 * @return the building
	 */
	public BigDecimal getBuilding() {
		return building;
	}

	/**
	 * @param building the building to set
	 */
	public void setBuilding(BigDecimal building) {
		this.building = building;
	}


//	/**
//	 * @return the domain
//	 */
//	public Domain getDomain() {
//		return domain;
//	}
//
//	/**
//	 * @param domain the domain to set
//	 */
//	public void setDomain(Domain domain) {
//		this.domain = domain;
//	}
//	
	public BigDecimal getCommercialAppraisal() {
		return commercialAppraisal;
	}

	public void setCommercialAppraisal(BigDecimal commercialAppraisal) {
		this.commercialAppraisal = commercialAppraisal;
	}

	public BigDecimal getValueBySquareMeter() {
		return valueBySquareMeter;
	}

	public void setValueBySquareMeter(BigDecimal valueBySquareMeter) {
		this.valueBySquareMeter = valueBySquareMeter;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

}