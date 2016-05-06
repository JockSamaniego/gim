package ec.gob.gim.cadaster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:38
 */
@Audited
@Entity
@TableGenerator(
	 name="NeighborhoodGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Neighborhood",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {
		@NamedQuery(name="Neighborhood.findByName", 
				query="select n from Neighborhood n where "+
						"n.name like concat(:name,'%') order by n.name"),
		@NamedQuery(name="Neighborhood.findAll", 
				query="select n from Neighborhood n order by n.name")
	}
)
public class Neighborhood {

	@Id
	@GeneratedValue(generator="NeighborhoodGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=5, nullable=false, unique=true)
	private String code;

	@Column(length=45, nullable=false)
	private String name;

	@ManyToOne
	private Place place;

	public Neighborhood(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}


}//end Neighborhood