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
	
	@Column(nullable=false)
	private Date creationDate;
	

	@ManyToOne
	@JoinColumn(name="agent_id")
	private Agent agent;


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


	public ItemCatalogx getType() {
		return type;
	}


	public void setType(ItemCatalogx type) {
		this.type = type;
	}
}//end Block