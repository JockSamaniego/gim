package ec.gob.gim.ant.ucot.model;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne; 
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;

/**
 * 
 * @author mack
 * @date 2017-06-28T10:22:18
 */
@Audited
@Entity
@TableGenerator(
	 name="BulletinGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Bulletin",
	 initialValue=1, allocationSize=1
)
public class Bulletin {

	@Id
	@GeneratedValue(generator="BulletinGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(nullable=false)
	private BigInteger startNumber;
	
	@Column(nullable=false)
	private BigInteger endNumber;

	@ManyToOne
	@JoinColumn(name="itemcatalog_id")
	private ItemCatalog type;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	@Temporal(TemporalType.TIME)
	private Date creationTime;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	@JoinColumn(name="agent_id")
	private Agent agent;


	private String detail;
	
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


	public ItemCatalog getType() {
		return type;
	}


	public void setType(ItemCatalog type) {
		this.type = type;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getCreationTime() {
		return creationTime;
	}


	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}


	public Agent getAgent() {
		return agent;
	}


	public void setAgent(Agent agent) {
		this.agent = agent;
	}


	public String getDetail() {
		return detail;
	}


	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
}//