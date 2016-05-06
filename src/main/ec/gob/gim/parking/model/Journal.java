package ec.gob.gim.parking.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;


import ec.gob.gim.common.model.Person;

@Audited
@Entity
@TableGenerator(name = "JournalGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "Journal", 
		initialValue = 1, 
		allocationSize = 1)
		
@NamedQueries({
		@NamedQuery(name = "Journal.findCurrent", 
				query = "select j from Journal j where :currentDate BETWEEN cast(j.startTime as date) and cast(j.endTime as date) and :operatorId = j.operator.id order by j.startTime")
})
public class Journal {

	@Id
	@GeneratedValue(generator="JournalGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@ManyToOne
	private Person operator;
	
	@ManyToOne
	private ParkingLot parkingLot;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Person getOperator() {
		return operator;
	}
	public void setOperator(Person operator) {
		this.operator = operator;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setParkingLot(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
	}
	public ParkingLot getParkingLot() {
		return parkingLot;
	}

	
	
}
