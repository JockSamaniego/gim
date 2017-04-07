package ec.gob.gim.finances.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * @author rfarmijosm
 * @version 1.0
 * @created 02-Feb-2017 11:17:25
 */

@Audited
@Entity
@TableGenerator(name = "SequenceManagerTypeGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "SequenceManagerType", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "SequenceManagerType.findAll", query = "select smt from SequenceManagerType smt where smt.isActive = true order by smt.name ") })
public class SequenceManagerType {

	@Id
	@GeneratedValue(generator = "SequenceManagerTypeGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	private Boolean isActive;

	@Column(length = 30, unique = true)
	private String name;

	public SequenceManagerType() {
		this.isActive = Boolean.TRUE;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

}
