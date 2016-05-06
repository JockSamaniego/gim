package ec.gob.gim.market.model;

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
 * BODEGA, LOCAL DE VENTA
 * 
 * @author gerson
 * @version 1.0
 * @created 12-Dic-2011 10:14:56
 */

@Audited
@Entity
@TableGenerator(name = "StandTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "StandType", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "StandType.findByName", query = "select s from StandType s order by s.name"),
		@NamedQuery(name = "StandType.findAll", query = "select s.name from StandType s order by s.name") })
public class StandType {

	@Id
	@GeneratedValue(generator = "StandTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 25, nullable = false)
	private String name;
	private String reason;
	
	@ManyToOne
	@JoinColumn(name = "entry_id")
	private Entry entry;

	public StandType() {

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

}// end PlaceType