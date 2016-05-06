package ec.gob.gim.market.model;

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

@Audited
@Entity
@TableGenerator(name = "MarketEmissionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "MarketEmission", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "MarketEmission.finEmission", query = "SELECT me FROM MarketEmission me "
				+ "WHERE me.month = :month and me.year = :year and me.market.id = :marketId "),
		@NamedQuery(name = "MarketEmission.findByDate", query = "SELECT me FROM MarketEmission me "
				+ "WHERE me.Date between :startDate and :endDate ") })
public class MarketEmission {

	@Id
	@GeneratedValue(generator = "MarketEmissionGenerator", strategy = GenerationType.TABLE)
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
	@JoinColumn(name = "standType_id")
	private StandType standType;

	@ManyToOne
	@JoinColumn(name = "market_id")
	private Market market;

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

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	public StandType getStandType() {
		return standType;
	}

	public void setStandType(StandType standType) {
		this.standType = standType;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

}
