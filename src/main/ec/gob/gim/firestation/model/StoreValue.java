package ec.gob.gim.firestation.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 12-Dic-2011 10:14:56
 */
@Audited
@Entity
@TableGenerator(name = "StoreValueGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "StoreValue", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {@NamedQuery(name = "StoreValue.findAll", query = "SELECT o FROM StoreValue o order by o.name"),
                       @NamedQuery(name = "StoreValue.findByName", query = "SELECT s FROM StoreValue s WHERE "
		                           + "lower(s.name) LIKE lower(concat(:criteria,'%')) order by s.name") })

public class StoreValue {

	@Id
	@GeneratedValue(generator = "StoreValueGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 60, nullable = false)
	private String name;
	private double value;
	private double transport;
	
	@ManyToOne
	private GroupStores groupstores;


	public StoreValue() {
		
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

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Double getTransport() {
		return transport;
	}

	public void setTransport(Double transport) {
		this.transport = transport;
	}

	public GroupStores getGroupStores() {
		return groupstores;
	}

	public void setGroupStores(GroupStores groupstores) {
		this.groupstores = groupstores;
	}
	
}// Store Value