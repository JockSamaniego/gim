package ec.gob.gim.firestation.model;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.OneToMany;

import org.hibernate.envers.Audited;
import org.hibernate.annotations.Cascade;

/**
 * @author gerson
 * @version 1.0
 * @created 12-Dic-2011 10:14:56
 */
@Audited
@Entity
@TableGenerator(name = "GroupStoresGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "GroupStores", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {@NamedQuery(name = "GroupStores.findAll", query = "SELECT s FROM GroupStores s order by s.name")})
public class GroupStores {

	@Id
	@GeneratedValue(generator = "GroupStoresGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;

	@OneToMany(mappedBy = "groupstores", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<StoreValue> storesvalues;

	
	public GroupStores() {
		storesvalues = new ArrayList<StoreValue>();
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
		this.name = name.toUpperCase();
	}
	
	public List<StoreValue> getStoresValues() {
		return storesvalues;
	}

	public void setStoresValues(List<StoreValue> storesvalues) {
		this.storesvalues = storesvalues;
	}

	public void add(StoreValue storevalue) {
		if (!storesvalues.contains(storevalue)) {
			storevalue.setGroupStores(this);
			storesvalues.add(storevalue);
		}
	}
}// end GroupStores