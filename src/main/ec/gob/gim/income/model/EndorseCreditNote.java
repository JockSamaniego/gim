package ec.gob.gim.income.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.envers.Audited;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.security.model.User;

@Audited
@Entity
@TableGenerator(
	 name="EndorseCreditNoteGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="EndorseCreditNote",
	 initialValue=1, allocationSize=1
 )
public class EndorseCreditNote {
	@Id
	@GeneratedValue(generator="EndorseCreditNoteGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private String endorseDetail;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date endorseDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Resident previousResident;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Resident currentResident;
	
	@ManyToOne
	@JoinColumn(name="creditNote_id")
	private CreditNote creditNote;
	
	@OneToOne
	private User endorseUser;
	
	
	public EndorseCreditNote() {

	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getEndorseDetail() {
		return endorseDetail;
	}


	public void setEndorseDetail(String endorseDetail) {
		this.endorseDetail = endorseDetail;
	}


	public Date getEndorseDate() {
		return endorseDate;
	}


	public void setEndorseDate(Date endorseDate) {
		this.endorseDate = endorseDate;
	}

	public Resident getPreviousResident() {
		return previousResident;
	}


	public void setPreviousResident(Resident previousResident) {
		this.previousResident = previousResident;
	}


	public Resident getCurrentResident() {
		return currentResident;
	}


	public void setCurrentResident(Resident currentResident) {
		this.currentResident = currentResident;
	}


	public CreditNote getCreditNote() {
		return creditNote;
	}


	public void setCreditNote(CreditNote creditNote) {
		this.creditNote = creditNote;
	}


	public User getEndorseUser() {
		return endorseUser;
	}


	public void setEndorseUser(User endorseUser) {
		this.endorseUser = endorseUser;
	}


}
