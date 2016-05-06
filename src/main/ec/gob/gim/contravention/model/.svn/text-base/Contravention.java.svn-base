package ec.gob.gim.contravention.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author richard
 * @version 1.0
 * @created 23-oct-2013 15:57:36
 */
@Audited
@Entity
@TableGenerator(name = "ContraventionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Contravention", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "Contravention.findMaxNumber", query = "SELECT MAX(c.number) FROM Contravention c where c.year = :year ")
})
public class Contravention {

	@Id
	@GeneratedValue(generator = "ContraventionGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	@Temporal(TemporalType.DATE)
	private Date registerDate;

	@Column(length = 100)
	private String description;

	@Column(length = 100)
	private String detail;

	private Boolean hasEmited;
	
	private Integer year;

	/**
	 * por que razon se elimina la contravencion
	 */
	@Column(length = 100)
	private String inactiveReason;

	private Boolean isActive;

	private Integer number;

	@Column(length = 100)
	private String sanctionDetail;

	private BigDecimal value;

	@NotAudited
	@ManyToOne
	@JoinColumn(name = "sanctionType_id")
	private SanctionType sanctionType;

	@ManyToOne
	@JoinColumn(name = "resident_id")
	private Resident resident;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "originator_id")
	private Person originator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipalBond_id")
	private MunicipalBond municipalBond;

	@NotAudited
	@ManyToOne
	@JoinColumn(name = "provenance_id")
	private Provenance provenance;

	public Contravention() {
		registerDate = new Date();
		isActive = Boolean.TRUE;
		date = new Date();
	}

	public void finalize() throws Throwable {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Boolean getHasEmited() {
		return hasEmited;
	}

	public void setHasEmited(Boolean hasEmited) {
		this.hasEmited = hasEmited;
	}

	public String getInactiveReason() {
		return inactiveReason;
	}

	public void setInactiveReason(String inactiveReason) {
		this.inactiveReason = inactiveReason;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getSanctionDetail() {
		return sanctionDetail;
	}

	public void setSanctionDetail(String sanctionDetail) {
		this.sanctionDetail = sanctionDetail;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public SanctionType getSanctionType() {
		return sanctionType;
	}

	public void setSanctionType(SanctionType sanctionType) {
		this.sanctionType = sanctionType;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Person getOriginator() {
		return originator;
	}

	public void setOriginator(Person originator) {
		this.originator = originator;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public Provenance getProvenance() {
		return provenance;
	}

	public void setProvenance(Provenance provenance) {
		this.provenance = provenance;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
}// end Contravention