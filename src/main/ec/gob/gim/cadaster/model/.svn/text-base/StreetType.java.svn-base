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
 * AVENIDA,
 * CALLE LOCAL
 * PEATONAL
 * ESCALINATA
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:46
 */
@Audited
@Entity
@TableGenerator(
	 name="StreetTypeGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="StreetType",
	 initialValue=1, allocationSize=1
)
@NamedQuery(name="StreetType.findAll", query="select o from StreetType o")
public class StreetType {

	@Id
	@GeneratedValue(generator="StreetTypeGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=25, nullable=false)
	private String name;
	
	@Column(length=2)
	private String code;

	public StreetType(){

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
	
	
}//end StreetType