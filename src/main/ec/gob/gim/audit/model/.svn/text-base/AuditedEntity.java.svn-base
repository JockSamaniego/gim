package ec.gob.gim.audit.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name="AuditedEntity.findByAuditType",query="select o from AuditedEntity o where auditType = :auditType")
public class AuditedEntity {
	@Id
	private String entityName;	
	private String name;
	private AuditType auditType;
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public AuditType getAuditType() {
		return auditType;
	}
	public void setAuditType(AuditType auditType) {
		this.auditType = auditType;
	}
	
	@Override
	public int hashCode() {		
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		AuditedEntity that = (AuditedEntity) o;
		if (getEntityName() != null ? !(getEntityName().equalsIgnoreCase(that.getEntityName())) : that.getEntityName() != null) {
			return false;
		}

		if (that.getEntityName() == null && getEntityName() == null) {
			return false;
		}

		return true;
	}
}
