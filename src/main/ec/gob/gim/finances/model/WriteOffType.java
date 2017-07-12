package ec.gob.gim.finances.model;

import javax.persistence.Column;
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
@TableGenerator(name = "WriteOffTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "WriteOffType", initialValue = 1, allocationSize = 1)
@NamedQueries({
	@NamedQuery(name = "WriteOffType.findAll", query = "select t from WriteOffType t where t.isActive = true order by t.id asc") 
})
public class WriteOffType {

	@Id
	@GeneratedValue(generator = "WriteOffTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	private Boolean isActive;

	@Column(length = 50)
	private String name;

	@Column(length = 100)
	private String detail;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	}
