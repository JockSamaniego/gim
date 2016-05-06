package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.FinancialInstitution;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:38
 */
@Audited
@Entity
@TableGenerator(
	 name="MortgageGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Mortgage",
	 initialValue=1, allocationSize=1
)
public class Mortgage {

	@Id
	@GeneratedValue(generator="MortgageGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private BigDecimal amount;
	
	private Boolean isActive;
	
	@ManyToOne
	private FinancialInstitution financialInstitution;

	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	@Enumerated(EnumType.STRING)
	@Column(length=25)
	private MortgageType mortgageType;
	
	@ManyToOne
	private Domain domain;

	public Mortgage(){

	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public MortgageType getMortgageType() {
		return mortgageType;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setMortgageType(MortgageType mortgageType) {
		this.mortgageType = mortgageType;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public void setFinancialInstitution(FinancialInstitution financialInstitution) {
		this.financialInstitution = financialInstitution;
	}

	public FinancialInstitution getFinancialInstitution() {
		return financialInstitution;
	}
}//end Mortgage