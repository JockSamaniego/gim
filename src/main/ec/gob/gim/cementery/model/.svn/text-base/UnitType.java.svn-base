package ec.gob.gim.cementery.model;

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
 * @created 25-Nov-2011 11:21:43 AM
 */

@Audited
@Entity
 @TableGenerator(
	 name="UnitTypeGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="UnitType",
	 initialValue=1, allocationSize=1
 )
 
@NamedQueries({
	@NamedQuery(name="UnitType.findAll",
			query="select unitType from UnitType unitType order by unitType.name "),
	@NamedQuery(name="UnitType.findByName",
			query="select unitType from UnitType unitType where unitType.name = :name"),
	@NamedQuery(name="UnitType.findAllNames", 
			query="select unitType.name from UnitType unitType order by unitType.name ")		
		
	})

public class UnitType {

	@Id
	@GeneratedValue(generator="UnitTypeGenerator",strategy=GenerationType.TABLE)
	private Long id;
	private String name;

	public UnitType(){

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
}//end UnitType