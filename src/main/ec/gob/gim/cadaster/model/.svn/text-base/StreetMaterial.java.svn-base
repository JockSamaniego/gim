package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;

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
 * @created 04-Ago-2011 16:30:46
 */
@Audited
@Entity
@TableGenerator(
	 name="StreetMaterialGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="StreetMaterial",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="StreetMaterial.findAll",
			query="select sm from StreetMaterial sm " +
				"order by sm.name"
	),
	@NamedQuery(name="StreetMaterial.findByName",
			query="select sm from StreetMaterial sm where " +
				"lower(sm.name) like lower(concat(:name, '%')) order by sm.name"
	),
	@NamedQuery(name="StreetMaterial.findAllOrderByCode",
			query="select sm from StreetMaterial sm order by sm.code"
	)
})
public class StreetMaterial {

	@Id
	@GeneratedValue(generator="StreetMaterialGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=25, nullable=false)
	private String name;
	
	@Column(length=2)
	private String code;

	@Column(nullable=false)
	private BigDecimal factor;
	
	public StreetMaterial(){

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

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getFactor() {
		return factor;
	}

	public void setFactor(BigDecimal factor) {
		this.factor = factor;
	}
	
}