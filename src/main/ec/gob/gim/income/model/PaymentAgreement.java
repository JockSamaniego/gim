package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.gob.gim.income.action.AgreementType;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.MunicipalBond;

@Audited
@Entity
@TableGenerator(
	 name="PaymentAgreementGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="PaymentAgreement",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {
		@NamedQuery(name = "PaymentAgreement.findByResidentIdAndStatus", 
				query = "SELECT o FROM PaymentAgreement o " +
						"WHERE o.resident.id=:residentId AND " +
						"o.isActive=:isActive"),
		@NamedQuery(name = "PaymentAgreement.findNumberByResidentIdAndStatus", 
				query = "SELECT COUNT(o) FROM PaymentAgreement o " +
						"WHERE o.resident.id=:residentId AND " +
						"o.isActive=:isActive"),
		@NamedQuery(name = "PaymentAgreement.deactivate", 
				query = "UPDATE PaymentAgreement pa " +
						"SET pa.isActive = :isActive, " +
						"    pa.closingDate = :closingDate, " +
						"    pa.closingTime = :closingTime " +
						"WHERE pa.id = :paymentAgreementId "),
		@NamedQuery(name = "PaymentAgreement.reactivate", 
				query = "UPDATE PaymentAgreement pa " +
						"SET pa.isActive = true, " +
						"    pa.closingDate = null, " +
						"    pa.closingTime = null " +
						"WHERE pa.id = :paymentAgreementId ")
})
public class PaymentAgreement {
	@Id
	@GeneratedValue(generator="PaymentAgreementGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private String description;
	
	private String observation;
	
	private Boolean isActive;
	
	private Boolean lowerPercentage;
	
	private BigDecimal value;
	
	private BigDecimal balance;
	
	private BigDecimal initialDividend;
	
	private Integer dividendsNumber;
	
	private Date firstPaymentDate;
	
	@Temporal(TemporalType.DATE)
	private Date closingDate;
	
	@Temporal(TemporalType.TIME)
	private Date closingTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Resident resident;
	
	@OneToMany(mappedBy="paymentAgreement")
	@OrderBy("expirationDate")
	private List<MunicipalBond> municipalBonds;
	
	@OneToMany(cascade={CascadeType.MERGE, CascadeType.PERSIST})
	@JoinColumn(name="paymentAgreement_id")
	private List<Dividend> dividends;
	
	@Transient
	private Integer alreadyPayed = new Integer("0");
	@Transient
	private Integer notPayedYet = new Integer("0");
	
	
	//2016-07-19T10:45
	//@author macartuche
	//@tag recaudacionCoactivas
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private AgreementType agreementType;
	
	public AgreementType getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(AgreementType agreementType) {
		this.agreementType = agreementType;
	}

	public PaymentAgreement() {
		this.municipalBonds = new ArrayList<MunicipalBond>();
		this.dividends = new LinkedList<Dividend>(); 
		this.isActive = Boolean.TRUE;
		this.lowerPercentage = Boolean.FALSE;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}
	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}
	public List<Dividend> getDividends() {
		return dividends;
	}
	public void setDividends(List<Dividend> dividends) {
		this.dividends = dividends;
	}
	public Resident getResident() {
		return resident;
	}
	public void setResident(Resident resident) {
		this.resident = resident;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	public BigDecimal getInitialDividend() {
		return initialDividend;
	}
	public void setInitialDividend(BigDecimal initialDividend) {
		this.initialDividend = initialDividend;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public Integer getDividendsNumber() { 
		return dividendsNumber;
	}

	public void setDividendsNumber(Integer dividendsNumber) {
		this.dividendsNumber = dividendsNumber;
	}


	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}


	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}
	
	public void add(Dividend dividend) {
		if (!this.dividends.contains(dividend)) {
			this.dividends.add(dividend);
		}
	}
	
	public void add(MunicipalBond municipalBond) {
		if (!this.municipalBonds.contains(municipalBond)) {
			this.municipalBonds.add(municipalBond);
			municipalBond.setPaymentAgreement(this);
		}
	}


	public Date getClosingDate() {
		return closingDate;
	}


	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}


	public Date getClosingTime() {
		return closingTime;
	}


	public void setClosingTime(Date closingTime) {
		this.closingTime = closingTime;
	}


	public String getObservation() {
		return observation;
	}	


	public void setObservation(String observation) {
		this.observation = observation;
	}


	public Integer getAlreadyPayed() {
		return alreadyPayed;
	}


	public void setAlreadyPayed(Integer alreadyPayed) {
		this.alreadyPayed = alreadyPayed;
	}


	public Integer getNotPayedYet() {
		return notPayedYet;
	}


	public void setNotPayedYet(Integer notPayedYet) {
		this.notPayedYet = notPayedYet;
	}


	public Boolean getLowerPercentage() {
		return lowerPercentage;
	}


	public void setLowerPercentage(Boolean lowerPercentage) {
		this.lowerPercentage = lowerPercentage;
	}
}
