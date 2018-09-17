package ec.gob.gim.revenue.model;

import java.io.Serializable;
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
import javax.persistence.Version;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;

@Audited
@Entity
@TableGenerator(name = "PhotoFineGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "PhotoFine", initialValue = 1, allocationSize = 1)
public class PhotoFine implements Serializable {

	/**
	 *  
	 */
	private static final long serialVersionUID = 18386387333339876L;

	@Id
	@GeneratedValue(generator = "PhotoFineGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Version
	private Long version = 0L;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(length = 10)
	private String numberPlate;

	private String reference;

	private String description;

	private String address;
	
	private Boolean isEmitted;

	/**
	 * nuevo numero generad por la empresa es alfanumerico rfarmijosm 2017-07-19
	 */
	@Column(length = 30)
	private String contraventionNumber;

	/**
	 * exceso de velocidad
	 */
	private BigDecimal speeding;
	/**
	 * tipo liviano o pesado
	 */
	private String vehicleType;

	/**
	 * publico o privado
	 */
	private String serviceType;

	/**
	 * yyyy-MM-dd
	 */
	private Date citationDate;

	/**
	 * rfam 2017-07-21 ML-JRM-2017-404-MY 332 TV-UMTTTSV-2017
	 */
	private String supportDocumentURL;

	private String reversedResolution;

	@Temporal(TemporalType.TIMESTAMP)
	private Date reversedDate;

	private BigDecimal amount;

	private BigDecimal total;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resident_id")
	private Resident resident;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entry_id")
	private Entry entry;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fiscalperiod_id")
	private FiscalPeriod fiscalPeriod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "originator_id")
	private Person originator;

	/**
	 * una vez emitido
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipalbond_id")
	private MunicipalBond municipalBond;

	18
	
	public PhotoFine() {
		reversedDate = new Date();
		isEmitted = Boolean.FALSE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContraventionNumber() {
		return contraventionNumber;
	}

	public void setContraventionNumber(String contraventionNumber) {
		this.contraventionNumber = contraventionNumber;
	}

	public BigDecimal getSpeeding() {
		return speeding;
	}

	public void setSpeeding(BigDecimal speeding) {
		this.speeding = speeding;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Date getCitationDate() {
		return citationDate;
	}

	public void setCitationDate(Date citationDate) {
		this.citationDate = citationDate;
	}

	public String getSupportDocumentURL() {
		return supportDocumentURL;
	}

	public void setSupportDocumentURL(String supportDocumentURL) {
		this.supportDocumentURL = supportDocumentURL;
	}

	public String getReversedResolution() {
		return reversedResolution;
	}

	public void setReversedResolution(String reversedResolution) {
		this.reversedResolution = reversedResolution;
	}

	public Date getReversedDate() {
		return reversedDate;
	}

	public void setReversedDate(Date reversedDate) {
		this.reversedDate = reversedDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getIsEmitted() {
		return isEmitted;
	}

	public void setIsEmitted(Boolean isEmitted) {
		this.isEmitted = isEmitted;
	}
	
	

}
