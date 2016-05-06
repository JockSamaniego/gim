package ec.gob.gim.revenue.model.unification;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;

/**
 * @author richard
 * @version 1.0
 * @created 26-Ene-2015 11:22:22
 */
@Audited
@Entity
@TableGenerator(name = "UnificationProcessGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "UnificationProcess", initialValue = 1, allocationSize = 1)
public class UnificationProcess {
	
	@Id
	@GeneratedValue(generator = "UnificationProcessGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	UnificationType unificationType;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.TIME)
	private Date time;
	
	@Column(length = 100)
	private String description;
	
	@Type(type="text")
	private String performedSql;
	
	@ManyToOne
	@JoinColumn(name = "unifiedResdient_id")
	private Resident unifiedResdient;
	
	private Long previousResidentId;
	
	private Long resdientDeletedId;
	
	@ManyToOne
	@JoinColumn(name="performchange_id")
	private Resident performChange;

	public UnificationProcess() {
		date = new Date();
		time = new Date();
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*public Long getActualResdientId() {
		return actualResdientId;
	}

	public void setActualResdientId(Long actualResdientId) {
		this.actualResdientId = actualResdientId;
	}*/

	public Long getPreviousResidentId() {
		return previousResidentId;
	}

	public void setPreviousResidentId(Long previousResidentId) {
		this.previousResidentId = previousResidentId;
	}

	public Long getResdientDeletedId() {
		return resdientDeletedId;
	}

	public void setResdientDeletedId(Long resdientDeletedId) {
		this.resdientDeletedId = resdientDeletedId;
	}

	public Resident getPerformChange() {
		return performChange;
	}

	public void setPerformChange(Resident performChange) {
		this.performChange = performChange;
	}

	public Resident getUnifiedResdient() {
		return unifiedResdient;
	}

	public void setUnifiedResdient(Resident unifiedResdient) {
		this.unifiedResdient = unifiedResdient;
	}

	public String getPerformedSql() {
		return performedSql;
	}

	public void setPerformedSql(String performedSql) {
		this.performedSql = performedSql;
	}

	public UnificationType getUnificationType() {
		return unificationType;
	}

	public void setUnificationType(UnificationType unificationType) {
		this.unificationType = unificationType;
	}
		
}
