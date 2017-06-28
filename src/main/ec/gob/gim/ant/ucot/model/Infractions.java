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
 * @date 2017-06-28T12:26:13
 *
 */
@Audited
@Entity
@TableGenerator(
	 name="InfractionsGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Agent",
	 initialValue=1, allocationSize=1
)
public class Infractions {

	@Id
	@GeneratedValue(generator="InfractionsGenerator",strategy=GenerationType.TABLE)
	private Long id;

	@Column(nullable=false)
	private BigInteger serial;
	
	@Column(length=10)
	private String article;
	
	@Column(length=10)
	private String numeral;
	
	@Column(nullable=false)
	private Date citationDate;
	
	private Boolean inPhysic;
	
	private String identification;
	
	private String licensePlate;
	
	private Boolean archived;
	
	@ManyToOne
	@JoinColumn(name="typeitem_id")
	private ItemCatalog type;
	
	@ManyToOne
	@JoinColumn(name="statusitem_id")
	private ItemCatalog status;
	
	private Bulletin bulletin;

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

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getNumeral() {
		return numeral;
	}

	public void setNumeral(String numeral) {
		this.numeral = numeral;
	}

	public Date getCitationDate() {
		return citationDate;
	}

	public void setCitationDate(Date citationDate) {
		this.citationDate = citationDate;
	}

	public Boolean getInPhysic() {
		return inPhysic;
	}

	public void setInPhysic(Boolean inPhysic) {
		this.inPhysic = inPhysic;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
	}

	public ItemCatalog getType() {
		return type;
	}

	public void setType(ItemCatalog type) {
		this.type = type;
	}

	public ItemCatalog getStatus() {
		return status;
	}

	public void setStatus(ItemCatalog status) {
		this.status = status;
	}

	public Bulletin getBulletin() {
		return bulletin;
	}

	public void setBulletin(Bulletin bulletin) {
		this.bulletin = bulletin;
	}
}