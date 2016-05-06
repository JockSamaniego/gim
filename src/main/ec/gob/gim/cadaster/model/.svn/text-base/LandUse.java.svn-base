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
 * @created 04-Ago-2011 16:30:36
 */
@Audited
@Entity
@TableGenerator(
	 name="LandUseGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="LandUse",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="LandUse.findAll",
			query="select l from LandUse l " +
				"order by l.name"
	),
	@NamedQuery(name="LandUse.findByName",
			query="select l from LandUse l where " +
				"lower(l.name) like lower(concat(:name, '%')) order by l.name"
	)
	,
	@NamedQuery(name="LandUse.findAllOrderByCode",
			query="select l from LandUse l order by l.code"
	)
})
public class LandUse {

	@Id
	@GeneratedValue(generator="LandUseGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=5, nullable=false, unique=true)
	private String code;

	@Column(length=100, nullable=false)
	private String name;

	public LandUse(){

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

}//end LandUse