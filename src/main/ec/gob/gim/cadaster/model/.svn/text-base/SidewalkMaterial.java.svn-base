package ec.gob.gim.cadaster.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author wilman
 * @version 1.0
 * @created 09-Ago-2011 16:56:16
 */
@Audited
@Entity
@TableGenerator(
	 name="SidewalkMaterialGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="SidewalkMaterial",
	 initialValue=1, allocationSize=1
)
@NamedQuery(name="SidewalkMaterial.findAll", query="select o from SidewalkMaterial o")
public class SidewalkMaterial {

	@Id
	@GeneratedValue(generator="SidewalkMaterialGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=25)
	private String code;
	
	@Column(length=40, nullable=false)
	private String name;

	public SidewalkMaterial(){

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}//end SidewalkMaterial