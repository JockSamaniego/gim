package ec.gob.gim.cadaster.model;

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
	 name="PlaceGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Place",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {
		@NamedQuery(name="Place.findByName", 
				query="select n from Place n where "+
						"n.name like concat(:name,'%') order by n.name"),
		@NamedQuery(name="Place.findAll", 
				query="select n from Place n order by n.name")
	}
)
public class Place {
	@Id
	@GeneratedValue(generator="PlaceGenerator",strategy=GenerationType.TABLE)
	private Long id;

	@Column(length=45, nullable=false)
	private String name;

	public Place(){

	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getName();
	}

}
