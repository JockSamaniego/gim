package ec.gob.gim.revenue.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.security.model.Role;

@Audited
@Entity
@TableGenerator(
		name = "PreissuerPermissionGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "PreissuerPermission", 
		initialValue = 1, allocationSize = 1
)
public class PreissuerPermission {
	@Id
	@GeneratedValue(generator = "PreissuerPermissionGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Role role;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Entry entry;
	
	private Date startDate;
	
	private Boolean isActive;

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}


}
