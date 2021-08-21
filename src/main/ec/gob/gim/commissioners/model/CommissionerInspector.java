package ec.gob.gim.commissioners.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;

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
	
	@OneToMany(mappedBy = "inspector", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<CommissionerBulletin> bulletins;
	
	@ManyToOne
	@JoinColumn(name="itemcatalogtype_id")
	private ItemCatalog commissionerBallotType;
	
	private Boolean isActive;
	
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

}
