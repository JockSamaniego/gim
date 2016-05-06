package ec.gob.gim.cadaster.model;
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
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:42
 */
@Audited
@Entity
 @TableGenerator(
	 name="PropertyTypeGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="PropertyType",
	 initialValue=1, allocationSize=1
 )
 @NamedQueries(value = { 
		@NamedQuery(name = "PropertyType.findById", query = "select p from PropertyType p "
				+ "where p.id = :id")
})
 public class PropertyType {

	@Id
	@GeneratedValue(generator="PropertyTypeGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=15, nullable=false)
	private String name;
	
	@ManyToOne
	@JoinColumn(name="entry_id")
	private Entry entry;

	public PropertyType(){

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
		this.name = name;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}
}//end PropertyType