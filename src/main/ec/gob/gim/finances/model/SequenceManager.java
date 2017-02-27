package ec.gob.gim.finances.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;

/**
 * @author rfarmijosm
 * @version 1.0
 * @created 02-Feb-2017 11:17:25
 */

@Audited
@Entity
@TableGenerator(name = "SequenceManagerGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "SequenceManager", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "SequenceManager.findMaxCodeByYear", query = "select MAX(b.code) from SequenceManager b where year = :year") })
public class SequenceManager {

	@Id
	@GeneratedValue(generator = "SequenceManagerGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;

	private Boolean isActive;

	/**
	 * codigo secuencial para el año
	 */
	private Integer code;

	@Column(length = 30)
	private String explanation;

	/**
	 * quien toma el número para dar la baja
	 */
	@ManyToOne
	@JoinColumn(name = "takenby_id")
	private Resident takenBy;

	@ManyToOne
	@JoinColumn(name = "sequenceyype_id")
	private SequenceManagerType sequenceManagerType;

	public SequenceManager() {
		this.isActive = Boolean.TRUE;
		this.date = new Date();
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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public Resident getTakenBy() {
		return takenBy;
	}

	public void setTakenBy(Resident takenBy) {
		this.takenBy = takenBy;
	}

	public SequenceManagerType getSequenceManagerType() {
		return sequenceManagerType;
	}

	public void setSequenceManagerType(SequenceManagerType sequenceManagerType) {
		this.sequenceManagerType = sequenceManagerType;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

}
