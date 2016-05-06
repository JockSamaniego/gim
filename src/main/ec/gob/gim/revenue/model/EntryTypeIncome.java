package ec.gob.gim.revenue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(
		name = "EntryTypeIncomeGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "EntryTypeIncome", 
		initialValue = 1, allocationSize = 1
)

 @NamedQueries(value = { 
		@NamedQuery(name = "EntryTypeIncome.findAll", 
				query = "SELECT entryTypeIncome FROM EntryTypeIncome entryTypeIncome ")
		})

public class EntryTypeIncome {

	@Id
	@GeneratedValue(generator = "EntryTypeIncomeGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(length = 50)
	private String name;
	
	public EntryTypeIncome() {

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

}