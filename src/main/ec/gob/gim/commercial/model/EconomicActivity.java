package ec.gob.gim.commercial.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.Entry;

/**
 * Actividad economica
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:32
 */
@Audited
@Entity
@TableGenerator(
	 name="EconomicActivityGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="EconomicActivity",
	 initialValue=1, allocationSize=1
 )
 
 @NamedQueries(value = {
		@NamedQuery(name="EconomicActivity.findByCriteria", 
				query="select distinct(economicActivity) from EconomicActivity economicActivity where "+
						"lower(economicActivity.code) like lower(concat(:criteria, '%')) or " +
						"lower(economicActivity.name) like lower(concat(:criteria, '%')) or " +
						"lower(economicActivity.economicActivityType) like lower(concat(:criteria, '%'))" +
						" order by economicActivity.name"),
		@NamedQuery(name="EconomicActivity.findChildsByType", query="select distinct(e) from EconomicActivity e " +												
						"where e.economicActivityType in (:types) " +						
						"and (" +
						"lower(e.code) like lower(concat(:criteria, '%')) or " +
						"lower(e.name) like lower(concat(:criteria, '%')) )" +						
						"order by e.economicActivityType, e.name")
		}
)
 public class EconomicActivity {

	@Id
	@GeneratedValue(generator="EconomicActivityGenerator",
			strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=10, nullable=false, unique=true)
	private String code;
	
	@Column(length=700, nullable=false)
	private String name;
	
	@Column
	private int level;
	
	
	@Enumerated(EnumType.STRING)
	@Column(length=20, nullable=false)
	private EconomicActivityType economicActivityType;
	
	/**
	 * Relationships
	 */

	@OneToMany(mappedBy = "parent", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<EconomicActivity> children;
	
	@ManyToOne
	private EconomicActivity parent;
	
	@ManyToOne
	@JoinColumn(name="entry_id")
	public Entry entry;
	
	
	public EconomicActivity(){
		children = new ArrayList<EconomicActivity>();
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

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return level;
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
	 * @return the children
	 */
	public List<EconomicActivity> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<EconomicActivity> children) {
		this.children = children;
	}

	/**
	 * @return the parent
	 */
	public EconomicActivity getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(EconomicActivity parent) {
		this.parent = parent;
	}
	
	/**
	 * @return the entry
	 */
	public Entry getEntry() {
		return entry;
	}

	/**
	 * @param entry the entry to set
	 */
	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	/**
	 * @return the economicActivityType
	 */
	public EconomicActivityType getEconomicActivityType() {
		return economicActivityType;
	}

	/**
	 * @param economicActivityType the economicActivityType to set
	 */
	public void setEconomicActivityType(EconomicActivityType economicActivityType) {
		this.economicActivityType = economicActivityType;
	}

	public void add(EconomicActivity economicActivity) {
		if (!this.children.contains(economicActivity)) {
			this.children.add(economicActivity);
			economicActivity.setParent(this);
		}
	}

	public void remove(EconomicActivity economicActivity) {
		boolean removed = this.children.remove(economicActivity);
		if (removed)
			economicActivity.setParent(null);
	}

}//end EconomicActivity