package ec.gob.gim.appraisal.model;

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
@TableGenerator(
	 name="AppraisalItemTypeGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="AppraisalItemType",
	 initialValue=1, allocationSize=1
)

 @NamedQueries({
	@NamedQuery(name="AppraisalItemType.findByName", 
			query="select n from AppraisalItemType n where "+
					"n.name like concat(:name,'%') order by n.name"),
	@NamedQuery(name="AppraisalItemType.findAll", 
			query="select n from AppraisalItemType n order by n.name")
	})

public class AppraisalItemType {

	@Id
	@GeneratedValue(generator="AppraisalItemTypeGenerator",strategy=GenerationType.TABLE)
	private Long id;
	private String name;
	
	public AppraisalItemType() {

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

}
