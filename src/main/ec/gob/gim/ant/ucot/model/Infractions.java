package ec.gob.gim.ant.ucot.model;
 

import java.math.BigDecimal;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Property;
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

@NamedQueries(value = {
		@NamedQuery(name = "infractions.findByBulletinId", query = "Select i from Infractions i where i.bulletin.id = :bulletinId"),
		@NamedQuery(name = "infractions.findBySerial", query = "Select i from Infractions i where i.serial = :serial")})

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
	
	@Column(length=10)
	private String partNumber;
	
	@Temporal(TemporalType.DATE)
	private Date citationDate;
	
	@Temporal(TemporalType.TIME)
	private Date citationTime;
	
	private Boolean inPhysic;
	
	private String identification;
	
	private String name;
	
	private String licensePlate;
	
	private Boolean nullified;
	
	private Boolean digitized;
	
	private Boolean white_green;
	
	private Boolean yellow;
	
	private BigDecimal points;
	
	private BigDecimal value;
	
	@Temporal(TemporalType.DATE)
	private Date nullifiedDate;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	@JoinColumn(name="bulletin_id")
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

	public Boolean getNullified() {
		return nullified;
	}

	public void setNullified(Boolean nullified) {
		this.nullified = nullified;
	}

	public Bulletin getBulletin() {
		return bulletin;
	}

	public void setBulletin(Bulletin bulletin) {
		this.bulletin = bulletin;
	}

	public Date getCitationTime() {
		return citationTime;
	}

	public void setCitationTime(Date citationTime) {
		this.citationTime = citationTime;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public Date getNullifiedDate() {
		return nullifiedDate;
	}

	public void setNullifiedDate(Date nullifiedDate) {
		this.nullifiedDate = nullifiedDate;
	}

	public Boolean getDigitized() {
		return digitized;
	}

	public void setDigitized(Boolean digitized) {
		this.digitized = digitized;
	}

	public BigDecimal getPoints() {
		return points;
	}

	public void setPoints(BigDecimal points) {
		this.points = points;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getWhite_green() {
		return white_green;
	}

	public void setWhite_green(Boolean white_green) {
		this.white_green = white_green;
	}

	public Boolean getYellow() {
		return yellow;
	}

	public void setYellow(Boolean yellow) {
		this.yellow = yellow;
	}
	
	
}