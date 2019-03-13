package ec.gob.gim.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(name="DelegateGenerator",
				table="IdentityGenerator",
				valueColumnName="value",
				pkColumnName="name",
				pkColumnValue="Delegate",
				initialValue=1,allocationSize=1)
public class Delegate {
	
	@Id
	@GeneratedValue(generator="DelegateGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private String name;
	
	/**
	 * notificacion al correo por cualquier novedad
	 */
	private String email;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	private Boolean isActive;	
	
	@ManyToOne
	private Charge charge;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public void setCharge(Charge charge) {
		this.charge = charge;
	}

	public Charge getCharge() {
		return charge;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
