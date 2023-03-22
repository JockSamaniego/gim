package ec.gob.gim.propertyregister.model;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.security.model.User;

@Audited
@Entity
@TableGenerator(name = "PropertyRegisterGenerator", 
	table = "IdentityGenerator", 
	pkColumnName = "name", 
	valueColumnName = "value", 
	pkColumnValue = "PropertyRegister", 
	initialValue = 1, allocationSize = 1)

public class PropertyRegister {

	@Id
	@GeneratedValue(generator = "PropertyRegisterGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length=100)
	private String inscriptionType;
	
	@Temporal(TemporalType.DATE)
	private Date inscriptionDate;
	
	@Column
	private int inscriptionNumber;
	
	@Column
	private int recordNumber;

	@Column(length=100)
	private String notary;
	
	@Temporal(TemporalType.DATE)
	private Date notaryDate;

	@Column(length=30)
	private String identificationNumber;
	
	@Column(length=50)
	private String calidad;
	
	@Column(length=200)
	private String intervenerName;
	
	@Column(length=40)
	private String cadastralCode;
	
	@Column(length=300)
	private String location;
	
	@Column
	private Double amount;
	
	@Column(length=15)
	private String registeredChange;
	
	@Column(length=200)
	private String obs;

	@Temporal(TemporalType.DATE)
	private Date loadDate; // Fecha de carga de la información
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateUpdate;
	
	@ManyToOne
	@JoinColumn(name = "responsableupdate_id")
	private User responsableUpdate;

	@OneToOne(cascade = CascadeType.ALL)
	private Domain domain;

	@OneToMany(mappedBy = "propertyRegister", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<PropertyRegisterItem> propertyRegisterItems;

	public PropertyRegister() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the inscriptionType
	 */
	public String getInscriptionType() {
		return inscriptionType;
	}

	/**
	 * @param inscriptionType the inscriptionType to set
	 */
	public void setInscriptionType(String inscriptionType) {
		this.inscriptionType = inscriptionType;
	}

	/**
	 * @return the inscriptionDate
	 */
	public Date getInscriptionDate() {
		return inscriptionDate;
	}

	/**
	 * @param inscriptionDate the inscriptionDate to set
	 */
	public void setInscriptionDate(Date inscriptionDate) {
		this.inscriptionDate = inscriptionDate;
	}

	/**
	 * @return the inscriptionNumber
	 */
	public int getInscriptionNumber() {
		return inscriptionNumber;
	}

	/**
	 * @param inscriptionNumber the inscriptionNumber to set
	 */
	public void setInscriptionNumber(int inscriptionNumber) {
		this.inscriptionNumber = inscriptionNumber;
	}

	/**
	 * @return the recordNumber
	 */
	public int getRecordNumber() {
		return recordNumber;
	}

	/**
	 * @param recordNumber the recordNumber to set
	 */
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}

	/**
	 * @return the notary
	 */
	public String getNotary() {
		return notary;
	}

	/**
	 * @param notary the notary to set
	 */
	public void setNotary(String notary) {
		this.notary = notary;
	}

	/**
	 * @return the notaryDate
	 */
	public Date getNotaryDate() {
		return notaryDate;
	}

	/**
	 * @param notaryDate the notaryDate to set
	 */
	public void setNotaryDate(Date notaryDate) {
		this.notaryDate = notaryDate;
	}

	/**
	 * @return the identificationNumber
	 */
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	/**
	 * @param identificationNumber the identificationNumber to set
	 */
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	/**
	 * @return the calidad
	 */
	public String getCalidad() {
		return calidad;
	}

	/**
	 * @param calidad the calidad to set
	 */
	public void setCalidad(String calidad) {
		this.calidad = calidad;
	}

	/**
	 * @return the intervenerName
	 */
	public String getIntervenerName() {
		return intervenerName;
	}

	/**
	 * @param intervenerName the intervenerName to set
	 */
	public void setIntervenerName(String intervenerName) {
		this.intervenerName = intervenerName;
	}

	/**
	 * @return the cadastralCode
	 */
	public String getCadastralCode() {
		return cadastralCode;
	}

	/**
	 * @param cadastralCode the cadastralCode to set
	 */
	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the registeredChange
	 */
	public String getRegisteredChange() {
		return registeredChange;
	}

	/**
	 * @param registeredChange the registeredChange to set
	 */
	public void setRegisteredChange(String registeredChange) {
		this.registeredChange = registeredChange;
	}

	/**
	 * @return the obs
	 */
	public String getObs() {
		return obs;
	}

	/**
	 * @param obs the obs to set
	 */
	public void setObs(String obs) {
		this.obs = obs;
	}

	/**
	 * @return the loadDate
	 */
	public Date getLoadDate() {
		return loadDate;
	}

	/**
	 * @param loadDate the loadDate to set
	 */
	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
	}

	/**
	 * @return the dateUpdate
	 */
	public Date getDateUpdate() {
		return dateUpdate;
	}

	/**
	 * @param dateUpdate the dateUpdate to set
	 */
	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	/**
	 * @return the responsableUpdate
	 */
	public User getResponsableUpdate() {
		return responsableUpdate;
	}

	/**
	 * @param responsableUpdate the responsableUpdate to set
	 */
	public void setResponsableUpdate(User responsableUpdate) {
		this.responsableUpdate = responsableUpdate;
	}

	/**
	 * @return the domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * @return the propertyRegisterItems
	 */
	public List<PropertyRegisterItem> getPropertyRegisterItems() {
		return propertyRegisterItems;
	}

	/**
	 * @param propertyRegisterItems the propertyRegisterItems to set
	 */
	public void setPropertyRegisterItems(List<PropertyRegisterItem> propertyRegisterItems) {
		this.propertyRegisterItems = propertyRegisterItems;
	}

}