package ec.gob.gim.revenue.model;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.common.model.Resident;

//@Audited
@Entity
@TableGenerator(name="SolvencyHistoryGenerator",
				pkColumnName="name",
				pkColumnValue="SolvencyHistory",
				table="IdentityGenerator",
				valueColumnName="value",
				allocationSize = 1, initialValue = 1)
@NamedQueries({
	@NamedQuery(name = "SolvencyHistory.findLastId", 
			query = "select max(solvencyHistory.id) from SolvencyHistory solvencyHistory"),
	@NamedQuery(name = "SolvencyHistory.findLastCertificate", 
			query = "select solvencyHistory from SolvencyHistory solvencyHistory WHERE "+
					"solvencyHistory.id = :id"),
	@NamedQuery(name = "SolvencyHistory.findByFiscalPeriodAndResident", 
			query = "select e from Exemption e " +
					"where e.fiscalPeriod.id =:fiscalPeriodId and e.resident.id = :residentId and e.exemptionType.id <> 4"),
	@NamedQuery(name = "SolvencyHistory.findByFiscalPeriod", 
			query = "select e from Exemption e " +
					"where e.fiscalPeriod.id =:fiscalPeriodId"),
	@NamedQuery(name = "SolvencyHistory.findByFiscalPeriodAndActive", 
			query = "select e from Exemption e " +
					"where e.fiscalPeriod.id =:fiscalPeriodId and e.active=true order by e.id")

	})
public class SolvencyHistory {
	
	@Id
	@GeneratedValue(generator="SolvencyHistoryGenerator", strategy=GenerationType.TABLE)
	private Long id;

	@Column(length=200)
	private String motivation;
	
	@Temporal(TemporalType.TIME)
	private Date creationTime;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private SolvencyHistoryType solvencyHistoryType;

	@ManyToOne
	@JoinColumn(name="resident_id")
	private Resident resident;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private Resident user;
	
	@ManyToOne
	@JoinColumn(name="responsable_id")
	private Resident responsable;
	
	@ManyToOne
	@JoinColumn(name="property_id")
	private Property property;
	
	@ManyToOne
	@JoinColumn(name="entry_id")
	private Entry entry;
	
	public SolvencyHistory(){
		
	}

	public SolvencyHistory(Resident resident, String motivation, SolvencyHistoryType solvencyHistoryType, Entry entry, Property property, Resident user, Resident responsable, String certificateNumber, String copiesNumber, String applicantIdentificationNumber){
		this.setResident(resident);
		this.setMotivation(motivation);
		this.setSolvencyHistoryType(solvencyHistoryType);
		this.setEntry(entry);
		this.setProperty(property);
		GregorianCalendar calendar = new GregorianCalendar();
		this.setCreationDate(calendar.getTime());
		this.setCreationTime(calendar.getTime());
		this.setUser(user);
		this.setResponsable(responsable);
		this.setCertificateNumber(certificateNumber);
		this.setCopiesNumber(copiesNumber);
		this.setApplicantIdentificationNumber(applicantIdentificationNumber);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMotivation() {
		return motivation;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public SolvencyHistoryType getSolvencyHistoryType() {
		return solvencyHistoryType;
	}

	public void setSolvencyHistoryType(SolvencyHistoryType solvencyHistoryType) {
		this.solvencyHistoryType = solvencyHistoryType;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Resident getUser() {
		return user;
	}

	public void setUser(Resident user) {
		this.user = user;
	}

	public Resident getResponsable() {
		return responsable;
	}

	public void setResponsable(Resident responsable) {
		this.responsable = responsable;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}
	
//Autor: Jock Samaniego M.
//Para crear un nueva columna y guardar el numero de certificado emitido.
	@JoinColumn(name="certificateNumber")
	@Column(length = 20)
	private String certificateNumber;

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	//Autor: Jock Samaniego M.
	//Para crear un nueva columna y guardar el numero de copias emitidas.

	@JoinColumn(name="copiesNumber")
	@Column(length = 8)
	private String copiesNumber;

	public String getCopiesNumber() {
		return copiesNumber;
	}

	public void setCopiesNumber(String copiesNumber) {
		this.copiesNumber = copiesNumber;
	}
	
	//Autor: Jock Samaniego M.
	//Para crear una nueva columna y guardar registro de quien solicita el certificado
	private String applicantIdentificationNumber;

	public String getApplicantIdentificationNumber() {
		return applicantIdentificationNumber;
	}

	public void setApplicantIdentificationNumber(
			String applicantIdentificationNumber) {
		this.applicantIdentificationNumber = applicantIdentificationNumber;
	}
	
	
	//Autor: Jock Samaniego M.
	//Para imprimir observaciones en los certificados.
	private String observation;

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

}
