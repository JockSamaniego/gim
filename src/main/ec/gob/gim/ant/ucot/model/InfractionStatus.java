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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

@NamedQueries(value = {
		@NamedQuery(name = "status.findByInfractionId", query = "Select s from InfractionStatus s WHERE s.infraction.id = :infractionId ORDER BY s.id ASC")})
public class InfractionStatus {

	@Id
	@GeneratedValue(generator="InfractionStatusGenerator",strategy=GenerationType.TABLE)
	private Long id;

	@ManyToOne
	@JoinColumn(name="itemcatalog_id")
	private ItemCatalog name;

	@Temporal(TemporalType.DATE)
	private Date statusDate;

	@ManyToOne
	@JoinColumn(name="infraction_id")
	private Infractions infraction;
	 
	private String Detail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(Date statusDate) {
		this.statusDate = statusDate;
	}

	public ItemCatalog getName() {
		return name;
	}

	public void setName(ItemCatalog name) {
		this.name = name;
	}

	public Infractions getInfraction() {
		return infraction;
	}

	public void setInfraction(Infractions infraction) {
		this.infraction = infraction;
	}

	public String getDetail() {
		return Detail;
	}

	public void setDetail(String detail) {
		Detail = detail;
	}

}
	
	
	
	
	