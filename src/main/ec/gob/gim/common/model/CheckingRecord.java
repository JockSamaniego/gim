package ec.gob.gim.common.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Property;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:27
 */

@Audited
@Entity
@TableGenerator(
	 name="CheckingRecordGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="CheckingRecord",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = {
		@NamedQuery(name = "CheckingRecord.findByProperty", 
				query =   "SELECT checkingRecord FROM CheckingRecord checkingRecord "
						+ "LEFT JOIN FETCH checkingRecord.checker checker "
						+ "WHERE checkingRecord.property = :property "					
						+ "ORDER BY checkingRecord.date desc, checkingRecord.time desc")
	})

public class CheckingRecord {

	@Id
	@GeneratedValue(generator="CheckingRecordGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date date;

	@Temporal(TemporalType.TIME)
	private Date time;

	@ManyToOne
	@JoinColumn(name="checker_id")
	private Person checker;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private CheckingRecordType checkingRecordType;
	
	@ManyToOne
	private Property property;
	
	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	private String observation;

	public CheckingRecord(){

	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the checker
	 */
	public Person getChecker() {
		return checker;
	}

	/**
	 * @param checker the checker to set
	 */
	public void setChecker(Person checker) {
		this.checker = checker;
	}

	/**
	 * @return the checkingRecordType
	 */
	public CheckingRecordType getCheckingRecordType() {
		return checkingRecordType;
	}

	/**
	 * @param checkingRecordType the checkingRecordType to set
	 */
	public void setCheckingRecordType(CheckingRecordType checkingRecordType) {
		this.checkingRecordType = checkingRecordType;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getObservation() {
		return observation;
	}

}