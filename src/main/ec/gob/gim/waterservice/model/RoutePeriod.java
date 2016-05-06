package ec.gob.gim.waterservice.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:44
 */
@Audited
@Entity
@TableGenerator(name = "RoutePeriodGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "RoutePeriod", initialValue = 1, allocationSize = 1)
public class RoutePeriod {

	@Id
	@GeneratedValue(generator = "RoutePeriodGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date endDate;

	private Boolean isActive;
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@ManyToOne
	// @JoinColumn(name = "route_id")
	private Route route;

	@ManyToOne
	@JoinColumn(name = "readingMan_id")
	private Person readingMan;

	public RoutePeriod() {
		this.isActive = true;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public Person getReadingMan() {
		return readingMan;
	}

	public void setReadingMan(Person readingMan) {
		this.readingMan = readingMan;
	}
}// end RoutePeriod