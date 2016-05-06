package ec.gob.gim.consession.model;

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

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.Entry;

/**
 * @version 1.0
 * @created 02-ago-2013 16:54:34
 */
@Audited
@Entity
@TableGenerator(name = "ConcessionPlaceTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ConcessionPlaceType", initialValue = 1, allocationSize = 1)
@NamedQueries(value = { @NamedQuery(name = "ConcessionPlaceType.findAll", query = "SELECT cpt FROM ConcessionPlaceType cpt order by cpt.name")
						})
public class ConcessionPlaceType {

	@Id
	@GeneratedValue(generator = "ConcessionPlaceTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;

	@Column(length = 100, nullable = false)
	private String reason;

	private Boolean isActive;

	@ManyToOne
	@JoinColumn(name = "entry_id")
	private Entry entry;

	public ConcessionPlaceType() {

	}

	public void finalize() throws Throwable {

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

}// end ConcessionType