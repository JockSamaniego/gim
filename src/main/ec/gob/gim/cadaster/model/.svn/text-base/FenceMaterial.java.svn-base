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
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:34
 */
@Audited
@Entity
@TableGenerator(
	 name="FenceMaterialGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="FenceMaterial",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="FenceMaterial.findAll",
			query="select fm from FenceMaterial fm " +
				"order by fm.name"
	),
	@NamedQuery(name="FenceMaterial.findByName",
			query="select fm from FenceMaterial fm where " +
				"lower(fm.name) like lower(concat(:name, '%')) order by fm.name"
	)
})
public class FenceMaterial {

	@Id
	@GeneratedValue(generator="FenceMaterialGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=25, nullable=false)
	private String name;

	public FenceMaterial(){

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
}//end FenceMaterial