package ec.gob.gim.commissioners.model;

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
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Audited
@Entity
@TableGenerator(
	 name="CommissionerInspectorGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="CommissionerInspector",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {
		@NamedQuery(name = "CommissionerInspector.findByType", query = "SELECT ci FROM CommissionerInspector ci where ci.commissionerBallotType.code =:commissionerType ORDER BY ci.name ASC "),
		@NamedQuery(name = "CommissionerInspector.findByIdentificationNumberAndType", query = "SELECT ci FROM CommissionerInspector ci where ci.numberIdentification =:identNum and ci.commissionerBallotType.code =:commissionerType ORDER BY ci.id ASC "),
		@NamedQuery(name = "CommissionerInspector.findByIdentificationNumberAndTypeActives", query = "SELECT ci FROM CommissionerInspector ci where ci.numberIdentification =:identNum and ci.commissionerBallotType.code =:commissionerType and ci.isActive = true ORDER BY ci.id ASC "),
		@NamedQuery(name = "CommissionerInspector.findByCriteria", query = "Select ci from CommissionerInspector ci where "
		+"lower(ci.numberIdentification) like lower(concat(:criteriaSearch,'%')) OR " 
		+"lower(ci.name) like lower(concat(:criteriaSearch,'%')) ")})

public class CommissionerInspector {

	@Id
	@GeneratedValue(generator="CommissionerInspectorGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private String numberIdentification;
	
	private String name;
	
	private String phoneNumber;
	
	private String email;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	@OneToMany(mappedBy = "inspector", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<CommissionerBulletin> bulletins;
	
	@ManyToOne
	@JoinColumn(name="itemcatalogtype_id")
	private ItemCatalog commissionerBallotType;
	
	private Boolean isActive;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsible_id") 
	private Person responsible;
	   
	@JoinColumn(name = "responsible_user")	 
	@Column(length = 100)	 
	private String responsible_user;
	
	public CommissionerInspector(){
		isActive = Boolean.TRUE;
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

	public String getNumberIdentification() {
		return numberIdentification;
	}

	public void setNumberIdentification(String numberIdentification) {
		this.numberIdentification = numberIdentification;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<CommissionerBulletin> getBulletins() {
		return bulletins;
	}

	public void setBulletins(List<CommissionerBulletin> bulletins) {
		this.bulletins = bulletins;
	}

	public ItemCatalog getCommissionerBallotType() {
		return commissionerBallotType;
	}

	public void setCommissionerBallotType(ItemCatalog commissionerBallotType) {
		this.commissionerBallotType = commissionerBallotType;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

}
