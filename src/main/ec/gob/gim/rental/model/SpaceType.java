package ec.gob.gim.rental.model;

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
		name = "SpaceTypeGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "SpaceType", 
		initialValue = 1, allocationSize = 1
)
@NamedQueries(value = {
		@NamedQuery(name="SpaceType.findById", 
				query="select s from SpaceType s " +						
						"where "+
						"s.id = :id)"),
		@NamedQuery(name="SpaceType.findAll", 
				query="select s from SpaceType s "), 
					
		@NamedQuery(name="SpaceType.findByName", 
				query="select s from SpaceType s " +						
						"where "+
						"lower(s.name) like lower(:name)"), 
						
		@NamedQuery(name="SpaceType.findAllNames", 
				query="select s.name from SpaceType s ")		
		}
)
public class SpaceType {
	@Id
	@GeneratedValue(generator = "SpaceTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	private String name;
	
	private String reason;

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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
