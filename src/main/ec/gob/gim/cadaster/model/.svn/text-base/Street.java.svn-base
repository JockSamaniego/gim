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

@Audited
@Entity
@TableGenerator(
	 name="StreetGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Street",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
		@NamedQuery(name="Street.findByName", 
				query="select street from Street street where lower(street.name) like lower(concat(:name,'%')) order by street.name"),
		@NamedQuery(name="Street.findByBlockId",
			query="select street from Block b " +
					"left join b.limits limits " +
					"left join limits.street street " +
					"where b.id = :blockId"
	)		
})
public class Street {
	@Id
	@GeneratedValue(generator="StreetGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=60, nullable=false)
	private String name;
	
	@ManyToOne
	private Place place;
	

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

		Street that = (Street) o;
		if (getId() != null ? !(getId().equals(that.getId())) : that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}


	public void setPlace(Place place) {
		this.place = place;
	}

	public Place getPlace() {
		return place;
	}
	
}
