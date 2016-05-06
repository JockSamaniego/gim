package ec.gob.gim.revenue.model;

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

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:29
 */
@Audited
@Entity
@TableGenerator(
		name = "ContractTypeGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "ContractType", 
		initialValue = 1, allocationSize = 1
)
@NamedQueries({
	@NamedQuery(name="ContractType.findByName",
			query="select c from ContractType c where c.name = :name"
	),
	@NamedQuery(name="ContractType.findById",
			query="select c from ContractType c where c.id = :id"
	),
	@NamedQuery(name="ContractType.findByEntry",
			query="select c from ContractType c where c.entry = :entry"
	)
})
public class ContractType {

	@Id
	@GeneratedValue(generator = "ContractTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(length = 50)
	private String name;
	
	private String description;
	
	@ManyToOne
	@JoinColumn(name="entry_id")
	private Entry entry;

	public ContractType() {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

}// end ContractType