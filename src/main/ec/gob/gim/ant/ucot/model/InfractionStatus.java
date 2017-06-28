package ec.gob.gim.ant.ucot.model;
 

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;

/**
 * 
 * @author mack
 * @date 2017-06-28T12:19
 * 
 */
@Audited
@Entity
@TableGenerator(
	 name="InfractionStatusGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="InfractionStatus",
	 initialValue=1, allocationSize=1
)
public class InfractionStatus {

	@Id
	@GeneratedValue(generator="InfractionStatusGenerator",strategy=GenerationType.TABLE)
	private Long id;

	@Column(nullable=false)
	private BigInteger serial;

	
	@Column(nullable=false)
	private Date statusDate;
	
	@ManyToOne
	@JoinColumn(name="statusitem_id")
	private ItemCatalog status;

	@ManyToOne
	@JoinColumn(name="infraction_id")
	private Infractions infraction;
	 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigInteger getSerial() {
		return serial;
	}

	public void setSerial(BigInteger serial) {
		this.serial = serial;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public ItemCatalog getStatus() {
		return status;
	}

	public void setStatus(ItemCatalog status) {
		this.status = status;
	}

	public Infractions getInfraction() {
		return infraction;
	}

	public void setInfraction(Infractions infraction) {
		this.infraction = infraction;
	}
	
	
}
	
	
	
	
	