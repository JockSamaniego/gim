package ec.gob.gim.commissioners.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;


@Audited
@Entity
@TableGenerator(name = "CommissionerBallotGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "CommissionerBallot", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "commissionerBallot.findAll", query = "SELECT cb FROM CommissionerBallot cb order by cb.creationDate"),
						@NamedQuery(name = "commissionerBallot.findByNumberAndType", query = "SELECT cb FROM CommissionerBallot cb where cb.ballotNumber =:ballotNumber and cb.commissionerBallotType.code =:commissionerType "),
						@NamedQuery(name = "commissionerBallot.findResidentNameByIdent", query = "Select r.name from Resident r where r.identificationNumber = :identNum"),
						@NamedQuery(name = "commissionerBallot.findResidentByIdent", query = "Select r from Resident r where r.identificationNumber = :identNum"),
						@NamedQuery(name = "CommissionerBallot.findGeneralReport", query = "SELECT cb FROM CommissionerBallot cb where (cb.creationDate BETWEEN :startDate and :endDate) and cb.commissionerBallotType.code =:commissionerType ORDER BY cb.creationDate, cb.ballotNumber ASC  "),
						@NamedQuery(name = "CommissionerBallot.findGeneralReportInfractionDate", query = "SELECT cb FROM CommissionerBallot cb where (cb.infractionDate BETWEEN :startDate and :endDate) and cb.commissionerBallotType.code =:commissionerType ORDER BY cb.infractionDate, cb.ballotNumber ASC  "),
						@NamedQuery(name = "CommissionerBallot.findGeneralReportNullified", query = "SELECT cb FROM CommissionerBallot cb where (cb.creationDate BETWEEN :startDate and :endDate) and cb.isNullified = true and cb.commissionerBallotType.code =:commissionerType ORDER BY cb.creationDate, cb.ballotNumber ASC  "),
						@NamedQuery(name = "CommissionerBallot.findByBulletin", query = "SELECT cb FROM CommissionerBallot cb where cb.bulletin.id =:bulletinId and cb.commissionerBallotType.code =:commissionerType ORDER BY cb.ballotNumber ASC  "),
						@NamedQuery(name = "CommissionerBallot.findForEmission", query = "SELECT cb FROM CommissionerBallot cb where cb.commissionerBallotType.code =:commissionerType and cb.isNullified = false and cb.currentStatus.statusName.code = 'READY_ISSUE' ORDER BY cb.ballotNumber ASC  "),
						@NamedQuery(name = "commissionerBallot.findAddressByIdent", query = "Select r.currentAddress.street from Resident r where r.identificationNumber = :identNum"),
						@NamedQuery(name = "commissionerBallot.findPhoneByIdent", query = "Select r.currentAddress.phoneNumber from Resident r where r.identificationNumber = :identNum"),
						@NamedQuery(name = "commissionerBallot.findEmailByIdent", query = "Select r.email from Resident r where r.identificationNumber = :identNum")
					  })

public class CommissionerBallot {
	
	@Id
	@GeneratedValue(generator = "CommissionerBallotGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(nullable = false)
	private Date creationDate;
	
	@Column(nullable = false)
	private Date infractionDate;
	
	private Date nullifiedDate;
	
	@Column(nullable = false)
	private Date infractionTime;
	
	private String infractorName;
	
	private String infractorIdentification; 
	
	@Column(nullable=false)
	private BigInteger ballotNumber;
	
	private String infractionPlace;
	
	private Date appearanceDate;
	
	private Date appearanceTime;
	
	private String appearanceObservation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsibleappearance_id")
	private Person responsibleAppearance;
	
	@JoinColumn(name = "responsibleappearance_user")	 
	@Column(length = 100)	 
	private String responsibleAppearance_user;

	private BigDecimal sanctionValue;
	
	private String observations;
	
	private String inspectorIdentification;
	
	private String inspectorName;
	
	@ManyToOne
	@JoinColumn(name="itemcatalogtype_id")
	private ItemCatalog commissionerBallotType;
	
	private String plate;
	
	@ManyToOne
	@JoinColumn(name="article_id")
	private SanctioningArticle sanctioningArticle;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsible_id")
	private Person responsible;
	   
	@JoinColumn(name = "responsible_user")	 
	@Column(length = 100)	 
	private String responsible_user;
	
	@OneToMany(mappedBy = "commissionerBallot", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<CommissionerBallotStatus> status;
	
	@ManyToOne
	@JoinColumn(name="currentstatus_id")
	private CommissionerBallotStatus currentStatus;
	
	private Boolean isActive;
	
	private Boolean isNullified;
	
	private String nullifiedReason;
	
	private String fileNumber;
	
	@JoinColumn(name = "responsiblenullified_user")	 
	@Column(length = 100)	 
	private String responsibleNullified_user;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	@JoinColumn(name="bulletin_id", nullable=false)
	private CommissionerBulletin bulletin;
	
	
	public CommissionerBallot() {
		status = new ArrayList<CommissionerBallotStatus>();
		isActive = Boolean.TRUE;
		isNullified = Boolean.FALSE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getInfractionDate() {
		return infractionDate;
	}

	public void setInfractionDate(Date infractionDate) {
		this.infractionDate = infractionDate;
	}

	public Date getInfractionTime() {
		return infractionTime;
	}

	public void setInfractionTime(Date infractionTime) {
		this.infractionTime = infractionTime;
	}

	public String getInfractorName() {
		return infractorName;
	}

	public void setInfractorName(String infractorName) {
		this.infractorName = infractorName;
	}

	public String getInfractorIdentification() {
		return infractorIdentification;
	}

	public void setInfractorIdentification(String infractorIdentification) {
		this.infractorIdentification = infractorIdentification;
	}

	public BigInteger getBallotNumber() {
		return ballotNumber;
	}

	public void setBallotNumber(BigInteger ballotNumber) {
		this.ballotNumber = ballotNumber;
	}

	public CommissionerBulletin getBulletin() {
		return bulletin;
	}

	public void setBulletin(CommissionerBulletin bulletin) {
		this.bulletin = bulletin;
	}

	public String getInfractionPlace() {
		return infractionPlace;
	}

	public void setInfractionPlace(String infractionPlace) {
		this.infractionPlace = infractionPlace;
	}

	public Date getAppearanceDate() {
		return appearanceDate;
	}

	public void setAppearanceDate(Date appearanceDate) {
		this.appearanceDate = appearanceDate;
	}

	public Date getAppearanceTime() {
		return appearanceTime;
	}

	public void setAppearanceTime(Date appearanceTime) {
		this.appearanceTime = appearanceTime;
	}

	public BigDecimal getSanctionValue() {
		return sanctionValue;
	}

	public void setSanctionValue(BigDecimal sanctionValue) {
		this.sanctionValue = sanctionValue;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public String getInspectorIdentification() {
		return inspectorIdentification;
	}

	public void setInspectorIdentification(String inspectorIdentification) {
		this.inspectorIdentification = inspectorIdentification;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getInspectorName() {
		return inspectorName;
	}

	public void setInspectorName(String inspectorName) {
		this.inspectorName = inspectorName;
	}

	public Person getResponsible() {
		return responsible;
	}

	public void setResponsible(Person responsible) {
		this.responsible = responsible;
	}

	public String getResponsible_user() {
		return responsible_user;
	}

	public void setResponsible_user(String responsible_user) {
		this.responsible_user = responsible_user;
	}

	public ItemCatalog getCommissionerBallotType() {
		return commissionerBallotType;
	}

	public void setCommissionerBallotType(ItemCatalog commissionerBallotType) {
		this.commissionerBallotType = commissionerBallotType;
	}

	public SanctioningArticle getSanctioningArticle() {
		return sanctioningArticle;
	}

	public void setSanctioningArticle(SanctioningArticle sanctioningArticle) {
		this.sanctioningArticle = sanctioningArticle;
	}

	public List<CommissionerBallotStatus> getStatus() {
		return status;
	}

	public void setStatus(List<CommissionerBallotStatus> status) {
		this.status = status;
	}

	public CommissionerBallotStatus getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(CommissionerBallotStatus currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getAppearanceObservation() {
		return appearanceObservation;
	}

	public void setAppearanceObservation(String appearanceObservation) {
		this.appearanceObservation = appearanceObservation;
	}

	public Person getResponsibleAppearance() {
		return responsibleAppearance;
	}

	public void setResponsibleAppearance(Person responsibleAppearance) {
		this.responsibleAppearance = responsibleAppearance;
	}

	public String getResponsibleAppearance_user() {
		return responsibleAppearance_user;
	}

	public void setResponsibleAppearance_user(String responsibleAppearance_user) {
		this.responsibleAppearance_user = responsibleAppearance_user;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public Date getNullifiedDate() {
		return nullifiedDate;
	}

	public void setNullifiedDate(Date nullifiedDate) {
		this.nullifiedDate = nullifiedDate;
	}

	public Boolean getIsNullified() {
		return isNullified;
	}

	public void setIsNullified(Boolean isNullified) {
		this.isNullified = isNullified;
	}

	public String getNullifiedReason() {
		return nullifiedReason;
	}

	public void setNullifiedReason(String nullifiedReason) {
		this.nullifiedReason = nullifiedReason;
	}

	public String getResponsibleNullified_user() {
		return responsibleNullified_user;
	}

	public void setResponsibleNullified_user(String responsibleNullified_user) {
		this.responsibleNullified_user = responsibleNullified_user;
	}

	public void remove(CommissionerBallotStatus cbs) {
		boolean removed = this.status.remove(cbs);
		if (removed)
			cbs.setCommissionerBallot((CommissionerBallot) null);
	}
	
	public void add(CommissionerBallotStatus cbs) {
		if (!this.status.contains(cbs) && cbs != null) {
			this.status.add(cbs);
			cbs.setCommissionerBallot((CommissionerBallot) this);
		}
	}
	
}
