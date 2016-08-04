/**
 * 
 */
package ec.gob.gim.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 * @author rene
 *
 */
@Entity
@TableGenerator(name = "ItemCatalogGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "ItemCatalog", initialValue = 1, allocationSize = 1)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ItemCatalog {

	@Id
	@GeneratedValue(generator = "ItemCatalogGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Version
	private int version;

	@Column(length = 15)
	private String code;

	@Column(length = 80)
	private String name;

	@Column(updatable = false)
	private String description;

	@Column(length = 15)
	private String catalogCode;

	@ManyToOne
	private Catalog catalog;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ItemCatalog)) {
			return false;
		}
		ItemCatalog other = (ItemCatalog) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		if (id == null) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCatalogCode() {
		return catalogCode;
	}

	public void setCatalogCode(String catalogCode) {
		this.catalogCode = catalogCode;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (id != null) {
			result += "id: " + id;
		}
		result += ", version: " + version;
		if (code != null && !code.trim().isEmpty()) {
			result += ", code: " + code;
		}
		if (name != null && !name.trim().isEmpty()) {
			result += ", name: " + name;
		}
		if (description != null && !description.trim().isEmpty()) {
			result += ", description: " + description;
		}
		if (catalogCode != null && !catalogCode.trim().isEmpty()) {
			result += ", catalogCode: " + catalogCode;
		}
		return result;
	}

	public Catalog getCatalog() {
		return this.catalog;
	}

	public void setCatalog(final Catalog catalog) {
		this.catalog = catalog;
	}

}
