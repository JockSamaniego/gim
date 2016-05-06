package ec.gob.gim.revenue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * Estructura de rubro: Permite definir la estructura de cada uno de los rubros
 * 
 * @author wilman
 * @version 1.0
 * @created 12-Nov-2011 16:30:33
 */
@Audited
@Entity
@TableGenerator(
		name = "EntryStructureGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "EntryStructure", 
		initialValue = 1, allocationSize = 1
)

@NamedQueries(value = {
		@NamedQuery(name="EntryStructure.findEntryStructureChildren", 
				query="select entryStructure from EntryStructure entryStructure " +
						"left join fetch entryStructure.targetEntry targetEntry " +
							//"left join fetch entryStructure.parent parent " + 
						"where " +
							"entryStructure.parent.id = :entryParentId " +
						"order by entryStructure.order, entryStructure.entryStructureType"
			),
			@NamedQuery(name="EntryStructure.findEntryStructureChildrenByType", 
					query="select entryStructure from EntryStructure entryStructure " +
							"left join fetch entryStructure.targetEntry targetEntry " +
							"where " +
								"entryStructure.parent.id = :entryParentId and " +
								"entryStructure.entryStructureType = :entryStructureType " +
							"order by entryStructure.order"
			),
			@NamedQuery(name="EntryStructure.findEntryStructureParent", 
				query="select entryStructure from EntryStructure entryStructure " +
						"left join fetch entryStructure.targetEntry targetEntry " +
						//"left join fetch entryStructure.parent parent " + 
						"where " +
						"entryStructure.child.id = :entryChildId " +
						"order by entryStructure.order, entryStructure.entryStructureType"
			)
		/*@NamedQuery(name="EntryStructure.findByEntryAndMaxStarDate", 
					query="select entryDefinition from EntryStructure entryDefinition " +
								"left join fetch entryDefinition.entry entry " + 
								"left join fetch entryDefinition.rule " + 
							"where " +
								"entryDefinition.id in " +
									"(Select max(ed.id) from EntryStructure ed " +
										"where (ed.entry = :entry) and " +
											"ed.startDate <= :startDate )" 
			),		
		@NamedQuery(name="EntryStructure.findByEntryParentAndMaxStarDate", 
				query="select entryDefinition from EntryStructure entryDefinition " +
							"left join fetch entryDefinition.entry entry " + 
							"left join fetch entryDefinition.rule " + 
						"where " +
							"entryDefinition.id in " +
								"(Select max(ed.id) from EntryStructure ed " +
									//"join ed.entry entry join entry.parents parents " +
									//"where parents = :entry and " +
									"join ed.entry entry " +
									"where :entry member of entry.parents and " +
										"ed.startDate <= :startDate " + 
								"group by ed.entry)" 
		)*/		
}
)
public class EntryStructure {

	@Id
	@GeneratedValue(generator = "EntryStructureGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(name="_order")
	private Integer order;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 15)
	private EntryStructureType entryStructureType; 
	
	@ManyToOne
	private Entry child;
	
	@ManyToOne
	private Entry parent;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Entry targetEntry;

	public EntryStructure() {
		order = new Integer(1);
		entryStructureType = EntryStructureType.NORMAL;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}


	public Entry getTargetEntry() {
		return targetEntry;
	}

	public void setTargetEntry(Entry targetEntry) {
		this.targetEntry = targetEntry;
	}

	/**
	 * @return the child
	 */
	public Entry getChild() {
		return child;
	}

	/**
	 * @param child the child to set
	 */
	public void setChild(Entry child) {
		this.child = child;
	}

	/**
	 * @return the parent
	 */
	public Entry getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Entry parent) {
		this.parent = parent;
	}

	public EntryStructureType getEntryStructureType() {
		return entryStructureType;
	}

	public void setEntryStructureType(EntryStructureType entryStructureType) {
		this.entryStructureType = entryStructureType;
	}
	

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
	
    @Override
    public boolean equals(Object o) {
    	if (!(o instanceof EntryStructure)) {
			return false;
		}
        
        EntryStructure that = (EntryStructure) o;
        
        if(child != null && that.child != null && child.getId().equals(that.child.getId())) return true;
        
        if ((this.id == null && that.id != null) || (this.id != null && !this.id.equals(that.id))
        		|| (child != null && that.child == null) || (child == null && that.child != null)
        		|| (child != null && that.child != null && !child.getId().equals(that.child.getId()))) {        	
			return false;
		}
        
        return true;
    }
	
	
}// end EntryStructure