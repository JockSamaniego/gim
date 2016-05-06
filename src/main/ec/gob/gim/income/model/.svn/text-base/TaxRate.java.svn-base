package ec.gob.gim.income.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;


/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:47
 */
@Audited
@Entity
@TableGenerator(
	 name="TaxRateGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="TaxRate",
	 initialValue=1, allocationSize=1
 )
 @NamedQueries(value = {
		@NamedQuery(name="TaxRate.findActiveByTaxIdAndPaymentDate", 
				query="select taxRates from Tax tax JOIN tax.taxRates taxRates " +
						"where "+
						"tax.id = :taxId AND " +
						":paymentDate BETWEEN taxRates.startDate AND taxRates.endDate")			
		}
)

public class TaxRate {

	@Id
	@GeneratedValue(generator="TaxRateGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private java.math.BigDecimal rate;
	
	@Column(length=2, nullable=true)	
	private String sriCode;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Tax tax;

	@Temporal(TemporalType.DATE)
	private java.util.Date endDate;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date startDate;
	
	@Transient
	private Boolean canBeEdited;

	public TaxRate(){

	}
		
	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}


	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public java.math.BigDecimal getRate() {
		return rate;
	}

	public void setRate(java.math.BigDecimal rate) {
		this.rate = rate;
	}

	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	

	public Boolean getCanBeEdited() {
		return canBeEdited;
	}

	public void setCanBeEdited(Boolean canBeEdited) {
		this.canBeEdited = canBeEdited;
	}

	public String getSriCode() {
		return sriCode;
	}

	public void setSriCode(String sriCode) {
		this.sriCode = sriCode;
	}	
	
}//end TaxRate