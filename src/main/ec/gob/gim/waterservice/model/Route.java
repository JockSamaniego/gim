package ec.gob.gim.waterservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Street;
import ec.gob.gim.cadaster.model.TerritorialDivision;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:44
 */
@Audited
@Entity
@TableGenerator(name = "RouteGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Route", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { 
		@NamedQuery(name = "Route.countByName", query = "SELECT COUNT (route) FROM Route route WHERE lower(route.name) LIKE lower(concat(:routeName,'%'))"),
		@NamedQuery(name = "Route.findByName", 
		query = "SELECT route FROM Route route " +
				"left join fetch route.routePeriods rp " +								
				"WHERE lower(route.name) LIKE lower(concat(:routeName,'%')) ORDER BY route.name")
//		@NamedQuery(name = "Route.findPendingRouteByReadingMan", 
//			query = "SELECT new ec.gob.gim.wsrest.dto.DtoRoute(r.id, r.name, rp.readingMan.id, c.month, c.year) "
//					+ "FROM WaterSupply ws "
//					+ "LEFT JOIN ws.route r "
//					+ "LEFT JOIN r.routePeriods rp "
//					+ "LEFT JOIN Consumption c "
//					+ "WHERE c.readingDate is null and rp.readingMan = 0")
		})
@XmlRootElement
public class Route implements Serializable{
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "RouteGenerator", strategy = GenerationType.TABLE)
	private Long id;
	/**
	 * numero de ruta
	 */
	private Integer number;
	/**
	 * el nombre de la ruta no siempre va a ser un numero
	 */
	@Column(length = 40, nullable = false)
	private String name;

	/**
	 * Relationships
	 */
	@OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<RoutePeriod> routePeriods;
	
	/**
	 * parroquia a la que pertenece
	 */
	@ManyToOne
	@JoinColumn(name = "parish_id")
	private TerritorialDivision parish;

	public Route() {
		routePeriods = new ArrayList<RoutePeriod>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public List<RoutePeriod> getRoutePeriods() {
		return routePeriods;
	}

	public void setRoutePeriods(List<RoutePeriod> routePeriods) {
		this.routePeriods = routePeriods;
	}

	public void add(RoutePeriod routePeriod) {
		if (!this.routePeriods.contains(routePeriod)) {
			this.routePeriods.add(routePeriod);
			routePeriod.setRoute(this);
		}
	}

	public void remove(RoutePeriod routePeriod) {
		boolean removed = this.routePeriods.remove(routePeriod);
		if (removed)
			routePeriod.setRoute(null);
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	@Override
	public int hashCode() {
		int hash = 13;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Street that = (Street) o;
		if (getId() != null ? !(getId().equals(that.getId())) : that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}

}// end Route