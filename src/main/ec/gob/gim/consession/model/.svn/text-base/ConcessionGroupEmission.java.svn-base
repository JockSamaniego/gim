package ec.gob.gim.consession.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import ec.gob.gim.waterservice.model.MonthType;

/**
 * @version 1.0
 * @created 02-ago-2013 16:54:33
 */
@Audited
@Entity
@TableGenerator(name = "ConcessionGroupEmissionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ConcessionGroupEmission", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "ConcessionGroupEmission.findByDate", query = "SELECT cge FROM ConcessionGroupEmission cge "
				+ "WHERE cge.Date between :startDate and :endDate ") })
public class ConcessionGroupEmission {

	@Id
	@GeneratedValue(generator = "ConcessionGroupEmissionGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date Date;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private MonthType monthType;

	private Integer month;

	private Integer year;

	private Boolean hasPreEmit;

	@Column(length = 60)
	private String explanation;

	@ManyToOne
	@JoinColumn(name = "concessionPlaceType_id")
	private ConcessionPlaceType concessionPlaceType;

	/*@ManyToOne
	@JoinColumn(name = "concessionPlace_id")
	private ConcessionPlace concessionPlace;*/

	@ManyToOne
	@JoinColumn(name = "concessionGroup_id")
	private ConcessionGroup concessionGroup;

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

	public MonthType getMonthType() {
		return monthType;
	}

	public void setMonthType(MonthType monthType) {
		this.monthType = monthType;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
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

	public ConcessionPlaceType getConcessionPlaceType() {
		return concessionPlaceType;
	}

	public void setConcessionPlaceType(ConcessionPlaceType concessionPlaceType) {
		this.concessionPlaceType = concessionPlaceType;
	}


	public ConcessionGroup getConcessionGroup() {
		return concessionGroup;
	}

	public void setConcessionGroup(ConcessionGroup concessionGroup) {
		this.concessionGroup = concessionGroup;
	}

}// end ConcessionGroup