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
 * @created 04-Ago-2011 16:30:42
 */
@Audited
@Entity
@TableGenerator(name = "PurchaseTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "PurchaseType", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "PurchaseType.findAll", query = "select pt from PurchaseType pt order by pt.id"),
		@NamedQuery(name = "PurchaseType.findByName", query = "select pt from PurchaseType pt where "
				+ "lower(pt.name) like lower(concat(:name, '%')) "
				+ "order by pt.name") })
public class PurchaseType {

	@Id
	@GeneratedValue(generator = "PurchaseTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Column(length = 50)
	private String name;

	private boolean changeName;

	public PurchaseType() {

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
	
	public boolean isChangeName() {
		return changeName;
	}

	public void setChangeName(boolean changeName) {
		this.changeName = changeName;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}
	
	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof PurchaseType)) {
			return false;
		}
		PurchaseType other = (PurchaseType) object;
					
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

}// end PurchaseType