package ec.gob.gim.cadaster.model;

import java.util.Date;
import java.math.BigDecimal; 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;

/**
 * @author Richard
 * @version 1.0
 * @created 21-Feb-2020 10:18:31
 *  
 */
@Audited
@Entity
@TableGenerator(
		name = "DomainOwnerGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "DomainOwner", 
		initialValue = 1, allocationSize = 1)
public class DomainOwner {
	
	@Id
	@GeneratedValue(generator = "DomainOwnerGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDate;
	
	private Boolean isEnabled;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private DomainOwnerType domainOwnerType;
	
	@ManyToOne
	private Domain domain;
	
	@Column 
	  private BigDecimal percentage; 
	
	@ManyToOne
	@JoinColumn(name = "resident_id")
	private Resident resident;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public DomainOwnerType getDomainOwnerType() {
		return domainOwnerType;
	}

	public void setDomainOwnerType(DomainOwnerType domainOwnerType) {
		this.domainOwnerType = domainOwnerType;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}	
	
	public BigDecimal getPercentage() { 
	    return percentage; 
   } 
 
   public void setPercentage(BigDecimal percentage) { 
     this.percentage = percentage; 
   }   

}
