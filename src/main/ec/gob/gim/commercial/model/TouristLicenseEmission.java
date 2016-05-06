package ec.gob.gim.commercial.model;

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

@Audited
@Entity
@TableGenerator(name = "TouristLicenseEmissionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "TouristLicenseEmission", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "TouristLicenseEmission.findByDate", query = "SELECT tle FROM TouristLicenseEmission tle "
				+ "WHERE tle.Date between :startDate and :endDate ") })
public class TouristLicenseEmission {
	@Id
	@GeneratedValue(generator = "TouristLicenseEmissionGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date Date;

	private Integer year;

	private Boolean hasPreEmit;

	@Column(length = 60)
	private String explanation;

	@ManyToOne
	@JoinColumn(name = "businessCatalog_id")
	private BusinessCatalog businessCatalog;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return Date;
	}

	public void setDate(Date date) {
		Date = date;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Boolean getHasPreEmit() {
		return hasPreEmit;
	}

	public void setHasPreEmit(Boolean hasPreEmit) {
		this.hasPreEmit = hasPreEmit;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public BusinessCatalog getBusinessCatalog() {
		return businessCatalog;
	}

	public void setBusinessCatalog(BusinessCatalog businessCatalog) {
		this.businessCatalog = businessCatalog;
	}
	
}