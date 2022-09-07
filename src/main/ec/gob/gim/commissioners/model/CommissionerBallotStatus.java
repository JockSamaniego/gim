package ec.gob.gim.commissioners.model;

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

import org.gob.gim.accounting.dto.AccountItem;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;


@Audited
@Entity
@TableGenerator(name = "CommissionerBallotStatusGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "CommissionerBallotStatus", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "CommissionerBallotStatus.findAll", query = "SELECT st FROM CommissionerBallotStatus st order by st.creationDate"),
						@NamedQuery(name = "CommissionerBallotStatus.findByType", query = "SELECT st FROM CommissionerBallotStatus st where st.statusName.name =:statusName "),
						@NamedQuery(name = "CommissionerBallotStatus.findResidentNameByIdent", query = "Select r.name from Resident r where r.identificationNumber = :identNum"),
						@NamedQuery(name = "CommissionerBallotStatus.findLastStatusForBallot", query = "SELECT st FROM CommissionerBallotStatus st where st.commissionerBallot.id =:commissionerBallotId ORDER BY st.id DESC "),
						@NamedQuery(name = "CommissionerBallotStatus.findLastStatusForBallotInOrder", query = "SELECT st FROM CommissionerBallotStatus st where st.commissionerBallot.id =:commissionerBallotId ORDER BY st.id, st.id ASC "),
						@NamedQuery(name = "CommissionerBallotStatus.findLastStatusForBallotExceptId", query = "SELECT st FROM CommissionerBallotStatus st where st.commissionerBallot.id =:commissionerBallotId and st.id !=:statusId ORDER BY st.id DESC ")
					  })
public class CommissionerBallotStatus implements Comparable<CommissionerBallotStatus>{
	@Id
	@GeneratedValue(generator = "CommissionerBallotStatusGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(nullable = false)
	private Date creationDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "responsible_id") 
	private Person responsible;
	   
	@JoinColumn(name = "responsible_user")	 
	@Column(length = 100)	 
	private String responsible_user;
	
	@ManyToOne
	@JoinColumn(name="itemcatalogstatus_id")
	private ItemCatalog statusName;
	
	private String observations;
	
	@ManyToOne
	@JoinColumn(name="commissionerBallot_id")
	private CommissionerBallot commissionerBallot;

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

	public ItemCatalog getStatusName() {
		return statusName;
	}

	public void setStatusName(ItemCatalog statusName) {
		this.statusName = statusName;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public CommissionerBallot getCommissionerBallot() {
		return commissionerBallot;
	}

	public void setCommissionerBallot(CommissionerBallot commissionerBallot) {
		this.commissionerBallot = commissionerBallot;
	}

	@Override
	public int compareTo(CommissionerBallotStatus o) {
		if(o != null){
			return this.creationDate.compareTo(o.creationDate);
		}
		return 0;
	}
	
	
}
