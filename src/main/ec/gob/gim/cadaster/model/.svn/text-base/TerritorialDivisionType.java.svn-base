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
 * @created 04-Ago-2011 16:30:48
 */
@Audited
@Entity
@TableGenerator(
	 name="TerritorialDivisionTypeGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="TerritorialDivisionType",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="TerritorialDivisionType.findAll", query="select o from TerritorialDivisionType o order by o.priority"),
	@NamedQuery(name="TerritorialDivisisonType.findByName", query="select o from TerritorialDivisionType o where o.name = :name")
})
public class TerritorialDivisionType {

	@Id
	@GeneratedValue(generator="TerritorialDivisionTypeGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=25, nullable=false, unique=true)
	private String name;
	private Integer priority;
	
	private Boolean isUrban;

	public TerritorialDivisionType(){

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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * @return the isUrban
	 */
	public Boolean getIsUrban() {
		return isUrban;
	}

	/**
	 * @param isUrban the isUrban to set
	 */
	public void setIsUrban(Boolean isUrban) {
		this.isUrban = isUrban;
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

		TerritorialDivisionType that = (TerritorialDivisionType) o;
		if (getId() != null ? !(getId().equals(that.getId())) : that.getId() != null) {
			return false;
		}

		if (that.getId() == null && getId() == null) {
			return false;
		}

		return true;
	}	
	
}//end TerritorialDivisionType