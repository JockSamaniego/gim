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

/**
 * @author jock
 * @version 1.0
 * @created 16-Sep-2022
 */
@Audited
@Entity
@TableGenerator(
	 name="EquipmentsGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Equipments",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="Equipments.findAll",
			query="select e from Equipments e " +
				"order by e.name"
	),
	@NamedQuery(name="Equipments.findByName",
			query="select e from Equipments e where " +
				"lower(e.name) like lower(concat(:name, '%')) order by e.name"
	)
})
public class Equipments {

	@Id
	@GeneratedValue(generator="EquipmentsGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=20, nullable=false)
	private String unit;

	@Column(length=100, nullable=false)
	private String name;

	public Equipments(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}//end LandUse