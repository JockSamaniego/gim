package ec.gob.gim.commercial.model;

import java.math.BigDecimal;
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
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * Local
 * 
 * @author richard
 * @version 1.0
 * @created 13-Nov-2013 10:00:01
 */
@Audited
@Entity
@TableGenerator(name = "TouristLicenseItemGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "TouristLicenseItem", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "TouristLicenseItem.findLocalIdsByCatalogAndYear", query = "SELECT local.id FROM TouristLicenseItem tli "
				+ "LEFT JOIN tli.businessCatalog bc "
				+ "LEFT JOIN tli.local local "
				+ "WHERE bc.id = :bcId AND year = :year and local.isActive =true "),
		@NamedQuery(name = "TouristLicenseItem.findByCatalogAndYear", query = "SELECT tli FROM TouristLicenseItem tli "
				+ "LEFT JOIN tli.businessCatalog bc "
				+ "LEFT JOIN tli.local local "
				+ "WHERE bc.id = :bcId AND year = :year and local.isActive =true "
				+ "order by tli.sequence"),
		@NamedQuery(name = "TouristLicenseItem.findByCatalogAndYearDisabled", query = "SELECT tli FROM TouristLicenseItem tli "
				+ "LEFT JOIN tli.businessCatalog bc "
				+ "LEFT JOIN tli.local local "
				+ "WHERE bc.id = :bcId AND year = :year and local.isActive =false "
				+ "order by tli.sequence")})

public class TouristLicenseItem {

	@Id
	@GeneratedValue(generator = "TouristLicenseItemGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date registerdate;

	@Temporal(TemporalType.TIME)
	private Date registerTime;
	
	@Column(length = 60)
	private String adisionalDescription;

	private Integer year;
	
	private Integer sequence;

	private BigDecimal value;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "local_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Local local;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "businessCatalog_id")
	private BusinessCatalog businessCatalog;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emitter_id")
	private Person originator;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "municipalBond_id")
	private MunicipalBond municipalBond;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRegisterdate() {
		return registerdate;
	}

	public void setRegisterdate(Date registerdate) {
		this.registerdate = registerdate;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Local getLocal() {
		return local;
	}

	public void setLocal(Local local) {
		this.local = local;
	}

	public BusinessCatalog getBusinessCatalog() {
		return businessCatalog;
	}

	public void setBusinessCatalog(BusinessCatalog businessCatalog) {
		this.businessCatalog = businessCatalog;
	}

	public Person getOriginator() {
		return originator;
	}

	public void setOriginator(Person originator) {
		this.originator = originator;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public String getAdisionalDescription() {
		return adisionalDescription;
	}

	public void setAdisionalDescription(String adisionalDescription) {
		this.adisionalDescription = adisionalDescription;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}	

}