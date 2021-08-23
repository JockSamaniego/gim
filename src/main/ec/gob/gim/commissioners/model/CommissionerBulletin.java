package ec.gob.gim.commissioners.model;



import java.math.BigInteger;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;


@Audited
@Entity
@TableGenerator(
	 name="CommissionerBulletinGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="CommissionerBulletin",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {
		@NamedQuery(name = "CommissionerBulletin.findByType", query = "SELECT cb FROM CommissionerBulletin cb where cb.commissionerBallotType.code =:commissionerType ORDER BY cb.bulletinNumber ASC "),
		@NamedQuery(name = "CommissionerBulletin.findBySerial", query = "Select cb from CommissionerBulletin cb where cb.startNumber <= :ballotNumber AND cb.endNumber >= :ballotNumber and cb.commissionerBallotType.code =:commissionerType ORDER BY cb.bulletinNumber ASC"),
		@NamedQuery(name = "CommissionerBulletin.findByRank", query = "Select cb from CommissionerBulletin cb where cb.startNumber > :startSerial AND cb.endNumber < :endSerial"),
		@NamedQuery(name = "CommissionerBulletin.findById", query = "Select cb from CommissionerBulletin cb where cb.id = :id"),
		@NamedQuery(name = "CommissionerBulletin.findByNumber", query = "Select cb from CommissionerBulletin cb where cb.bulletinNumber = :number and cb.commissionerBallotType.code =:commissionerType"),
		@NamedQuery(name = "CommissionerBulletin.findByStartSerial", query = "Select cb from CommissionerBulletin cb where cb.startNumber <= :startNumber and cb.endNumber >= :startNumber and cb.commissionerBallotType.code =:commissionerType"),
		@NamedQuery(name = "CommissionerBulletin.findByEndSerial", query = "Select cb from CommissionerBulletin cb where cb.startNumber <= :endNumber and cb.endNumber >= :endNumber and cb.commissionerBallotType.code =:commissionerType"),
		@NamedQuery(name = "CommissionerBulletin.findByInspector", query = "Select cb from CommissionerBulletin cb where cb.inspector.numberIdentification = :identNum  and cb.inspector.isActive = true and cb.commissionerBallotType.code =:commissionerType ORDER BY cb.bulletinNumber ASC ")})


public class CommissionerBulletin {

	@Id
	@GeneratedValue(generator="CommissionerBulletinGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(nullable=false)
	private BigInteger startNumber;
	
	@Column(nullable=false)
	private BigInteger endNumber;
	
	@Column(nullable=false)
	private BigInteger bulletinNumber;

	@ManyToOne
	@JoinColumn(name="itemcatalogtype_id")
	private ItemCatalog commissionerBallotType;
	
	@Temporal(TemporalType.DATE)
	private Date deliveryDate;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	@JoinColumn(name="inspector_id", nullable=false)
	private CommissionerInspector inspector;
	
	@OneToMany(mappedBy = "bulletin", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<CommissionerBallot> ballot;
	
	private String observation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsible_id") 
	private Person responsible;
	   
	@JoinColumn(name = "responsible_user")	 
	@Column(length = 100)	 
	private String responsible_user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigInteger getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(BigInteger startNumber) {
		this.startNumber = startNumber;
	}

	public BigInteger getEndNumber() {
		return endNumber;
	}

	public void setEndNumber(BigInteger endNumber) {
		this.endNumber = endNumber;
	}

	public BigInteger getBulletinNumber() {
		return bulletinNumber;
	}

	public void setBulletinNumber(BigInteger bulletinNumber) {
		this.bulletinNumber = bulletinNumber;
	}

	public ItemCatalog getCommissionerBallotType() {
		return commissionerBallotType;
	}

	public void setCommissionerBallotType(ItemCatalog commissionerBallotType) {
		this.commissionerBallotType = commissionerBallotType;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public CommissionerInspector getInspector() {
		return inspector;
	}

	public void setInspector(CommissionerInspector inspector) {
		this.inspector = inspector;
	}

	public List<CommissionerBallot> getBallot() {
		return ballot;
	}

	public void setBallot(List<CommissionerBallot> ballot) {
		this.ballot = ballot;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
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

	
}//