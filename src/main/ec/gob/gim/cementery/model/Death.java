package ec.gob.gim.cementery.model;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.Contract;

/**
 * Defuncion
 * @author gerson
 * @version 1.0
 * @created 25-Nov-2011 11:21:42 AM
 */
@Audited
@Entity
 @TableGenerator(
	 name="DeathGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Death",
	 initialValue=1, allocationSize=1
 )

@NamedQueries(value = {
		@NamedQuery(name = "Death.FindById", query = "Select d from Death d where d.id=:id")})

 public class Death {

	@Id
	@GeneratedValue(generator="DeathGenerator",strategy=GenerationType.TABLE)
	private Long id;
	@Column(length=100)
	private String deathName;
	@Temporal(TemporalType.DATE)
	private Date dateOfDeath;
	@Temporal(TemporalType.DATE)
	private Date burialDate;
	@Temporal(TemporalType.DATE)
	private Date dateForExhumation;
	@Temporal(TemporalType.DATE)
	private Date exhumationDate;
	private int renovationNumber;
	private boolean isCurrent;
	private String observations;
	@ManyToOne(cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name="unit_id")
	private Unit unit;
	@ManyToOne
	@JoinColumn(name="cementery_id")
	private Cementery cementery;
	@OneToMany(cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name="death_id")
	private List<Contract> contracts;
	@OneToOne(cascade=CascadeType.ALL)
	@Cascade(value=org.hibernate.annotations.CascadeType.ALL)
	@JoinColumn(name="currentContract_id")
	private Contract currentContract;
	@OneToOne
	private Person deceased;

	@OneToOne
	private Person responsableExhumation;

	@OneToOne
	private Person responsableReserve;

	public Death(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDeathName(String deathName) {
		this.deathName = deathName;
	}

	public String getDeathName() {
		return deathName;
	}

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public Date getBurialDate() {
		return burialDate;
	}

	public void setBurialDate(Date burialDate) {
		this.burialDate = burialDate;
	}

	public Date getDateForExhumation() {
		return dateForExhumation;
	}

	public void setDateForExhumation(Date dateForExhumation) {
		this.dateForExhumation = dateForExhumation;
	}

	public Date getExhumationDate() {
		return exhumationDate;
	}

	public void setExhumationDate(Date exhumationDate) {
		this.exhumationDate = exhumationDate;
	}

	public int getRenovationNumber() {
		return renovationNumber;
	}

	public void setRenovationNumber(int renovationNumber) {
		this.renovationNumber = renovationNumber;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public void setCementery(Cementery cementery) {
		this.cementery = cementery;
	}

	public Cementery getCementery() {
		return cementery;
	}

	public List<Contract> getContracts() {
		return contracts;
	}

	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}

	public void setCurrentContract(Contract currentContract) {
		this.currentContract = currentContract;
	}

	public Contract getCurrentContract() {
		return currentContract;
	}

	public Person getDeceased() {
		return deceased;
	}

	public void setDeceased(Person deceased) {
		this.deceased = deceased;
	}

	public Person getResponsableExhumation() {
		return responsableExhumation;
	}

	public void setResponsableExhumation(Person responsableExhumation) {
		this.responsableExhumation = responsableExhumation;
	}

	public Person getResponsableReserve() {
		return responsableReserve;
	}

	public void setResponsableReserve(Person responsableReserve) {
		this.responsableReserve = responsableReserve;
	}
	
}//end Death