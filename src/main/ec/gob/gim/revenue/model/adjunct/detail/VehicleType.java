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
@TableGenerator(name = "VehicleTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "VehicleType", initialValue = 1, allocationSize = 1)

@NamedQueries(value = {
		@NamedQuery(name = "VehicleType.findAll", query = "select o from VehicleType o order by o.name"),
		@NamedQuery(name = "VehicleType.findForSimert", query = "select o from VehicleType o "
				+ "where o.id in (1,19,26) order by o.name") 
})
public class VehicleType {
	@Id
	@GeneratedValue(generator = "VehicleTypeGenerator", strategy = GenerationType.TABLE)
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
		this.name = name;
	}
}
