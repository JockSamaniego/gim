package ec.gob.gim.revenue.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.cementery.model.Death;
import ec.gob.gim.common.model.Resident;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:29
 */
@Audited
@Entity
@TableGenerator(name = "ContractGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Contract", initialValue = 1, allocationSize = 1)
public class Contract {

	@Id
	@GeneratedValue(generator = "ContractGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	@Temporal(TemporalType.DATE)
	private Date expirationDate;
	@Temporal(TemporalType.DATE)
	private Date subscriptionDate;
	
	private String description;
	
	@Transient
	private Long totalMonths;
	
	/**
	 * Relationships
	 */
	
	@ManyToOne
	@JoinColumn(name="contractType_id")
	private ContractType contractType;
	
	@ManyToOne
	private Resident subscriber;

	@ManyToOne(cascade=CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name="death_id")
	private Death death;
	
	public Contract() {
		subscriptionDate = new Date();
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {		
		this.expirationDate = expirationDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(Date subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

	/**
	 * @return the subscriber
	 */
	public Resident getSubscriber() {
		return subscriber;
	}

	/**
	 * @param subscriber the subscriber to set
	 */
	public void setSubscriber(Resident subscriber) {
		this.subscriber = subscriber;
	}

	public void setTotalMonths(Long totalMonths) {
		this.totalMonths = totalMonths;
	}

	public Long getTotalMonths() {
		return totalMonths;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Death getDeath() {
		return death;
	}

	public void setDeath(Death death) {
		this.death = death;
	}
			
}// end Contract