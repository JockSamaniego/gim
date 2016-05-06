package ec.gob.gim.revenue.model.adjunct.detail;

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
@TableGenerator(name = "VehicleMakerGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "VehicleMaker", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "VehicleMaker.findAll", query = "select o from VehicleMaker o order by o.name"),
		@NamedQuery(name = "VehicleMaker.findByName", query = "select o from VehicleMaker o where o.name like :name") })
public class VehicleMaker {
	@Id
	@GeneratedValue(generator = "VehicleMakerGenerator", strategy = GenerationType.TABLE)
	private Long id;
	private String name;

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

}
