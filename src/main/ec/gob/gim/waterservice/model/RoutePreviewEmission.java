package ec.gob.gim.waterservice.model;

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

@Audited
@Entity
@TableGenerator(name = "RoutePreviewEmissionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "RoutePreviewEmission", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "RoutePreviewEmission.finIfPreEmission", query = "SELECT count(routePE) FROM RoutePreviewEmission routePE "
				+ "WHERE routePE.month = :month and routePE.year = :year and routePE.route.id = :routeId "),
		@NamedQuery(name = "RoutePreviewEmission.findPerformEmissionByDate", query = "SELECT routePE FROM RoutePreviewEmission routePE "
				+ "LEFT JOIN FETCH routePE.route "
				+ "WHERE routePE.Date between :startDate and :endDate") })
public class RoutePreviewEmission {

	@Id
	@GeneratedValue(generator = "RoutePreviewEmissionGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date Date;

	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private MonthType monthType;

	private Integer month;

	private Integer year;

	private Boolean hasPreEmit;

	@ManyToOne
	@JoinColumn(name = "route_id")
	private Route route;

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

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

}
