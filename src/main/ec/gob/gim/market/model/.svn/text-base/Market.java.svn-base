package ec.gob.gim.market.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 12-Dic-2011 10:14:56
 */
@Audited
@Entity
@TableGenerator(name = "MarketGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Market", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "Market.findAll", query = "select market from Market market "
				+ " order by market.name"),
		@NamedQuery(name = "Market.findAllActive", query = "select market from Market market where market.isActive=true "
				+ " order by market.name") })
public class Market {

	@Id
	@GeneratedValue(generator = "MarketGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 60, nullable = false)
	private String name;

	private Boolean isActive;

	@OneToOne(cascade = CascadeType.ALL)
	private Concession currentConcession;

	@OneToMany(mappedBy = "market", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Concession> concessions;

	@OneToMany(mappedBy = "market", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Stand> stands;

	public Market() {
		concessions = new ArrayList<Concession>();
		stands = new ArrayList<Stand>();
		isActive = Boolean.TRUE;
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

	public List<Concession> getConcessions() {
		return concessions;
	}

	public void setConcessions(List<Concession> concessions) {
		this.concessions = concessions;
	}

	public List<Stand> getStands() {
		return stands;
	}

	public void setStands(List<Stand> stands) {
		this.stands = stands;
	}

	public void add(Stand stand) {
		if (!stands.contains(stand)) {
			stand.setMarket(this);
			stands.add(stand);
		}
	}

	public void remove(Stand stand) {
		if (stands.remove(stand)) {
			stand.setMarket(null);
		}
	}

	public void add(Concession concession) {
		if (!concessions.contains(concession)) {
			concession.setMarket(this);
			concessions.add(concession);
		}
	}

	public void remove(Concession concession) {
		if (concessions.remove(concession)) {
			concession.setMarket(null);
		}
	}

	public Concession getCurrentConcession() {
		return currentConcession;
	}

	public void setCurrentConcession(Concession currentConcession) {
		this.currentConcession = currentConcession;
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

		Market that = (Market) o;
		if (getId() != null ? !(getId().equals(that.getId()))
				: that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

}// end Market