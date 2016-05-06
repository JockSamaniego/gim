package ec.gob.gim.parking.model;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.EmissionOrder;



@Audited
@Entity
@TableGenerator(name = "TicketGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "Ticket", 
		initialValue = 1, 
		allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "Ticket.findNumberOfBusySpacesInParkingLot", query = "SELECT count(t.id) FROM Ticket t join t.journal.parkingLot pl "
				+ "WHERE t.going is null AND cast(t.coming as date) = :comingDate AND pl.id = :id"),
		@NamedQuery(name = "Ticket.findCollectedTicketsInParkingLot", query = "SELECT t FROM Ticket t join t.journal.parkingLot pl "
				+ "WHERE cast(t.going as date) BETWEEN :startDate AND :endDate AND pl.id=:id AND t.invalidated = FALSE ORDER BY t.cashier.name"),
		@NamedQuery(name = "Ticket.findByIdAndNoPaidAndNoInvalidated", query = "SELECT t FROM Ticket t "
				+ "WHERE t.id = :id and t.going is null and t.invalidated = false"),
		@NamedQuery(name = "Ticket.findInvalidatedTicketsInParkingLot", query = "SELECT t FROM Ticket t left join t.journal j left join t.cashier c left join j.parkingLot pl "
				+ "WHERE cast(t.coming as date) BETWEEN :startDate AND :endDate AND pl.id=:id  AND t.invalidated = TRUE ORDER BY c.name")
		})
public class Ticket {

	@Id
	@GeneratedValue(generator="TicketGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20, nullable=true)
	private TimeUnit timeUnit;
	
	private BigDecimal charge;
	
	@Transient
	private Boolean isTotalCollected;
	
	public Boolean getIsTotalCollected() {
		return isTotalCollected;
	}

	public void setIsTotalCollected(Boolean isTotalCollected) {
		this.isTotalCollected = isTotalCollected;
	}

	@ManyToOne
	private Journal journal;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date coming;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date going;
	
	private boolean invalidated = false;
	
	private String numberPlate;
	
	private String observation;
	
	@ManyToOne
	private Person manager;
	
	@ManyToOne
	private Person cashier;
	
	@ManyToOne
	private EmissionOrder emissionOrder;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Journal getJournal() {
		return journal;
	}

	public void setJournal(Journal journal) {
		this.journal = journal;
	}

	public Date getComing() {
		return coming;
	}

	public void setComing(Date coming) {
		this.coming = coming;
	}

	public Date getGoing() {
		return going;
	}

	public void setGoing(Date going) {
		this.going = going;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	

	@Transient
	public long getTimeTakenInMillis(){
		
		if (this.getGoing() == null){
			return 0;
		}
		Calendar t1 = Calendar.getInstance();
		Calendar t2 = Calendar.getInstance();
		t1.setTime(this.getComing());
		t2.setTime(this.getGoing());
		return t2.getTimeInMillis() - t1.getTimeInMillis();
	}
	
	@Transient
	public long getTimeTakenInSeconds(){
		return java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(getTimeTakenInMillis());
	}
	
	@Transient
	public long getTimeTakenInMinuts(){
		return java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(getTimeTakenInMillis());
	}

	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}

	public BigDecimal getCharge() {
		return charge;
	}

	public void setInvalidated(boolean invalidated) {
		this.invalidated = invalidated;
	}

	public boolean isInvalidated() {
		return invalidated;
	}

	public String getNumberPlate() {
		return numberPlate;
	}

	public void setNumberPlate(String numberPlate) {
		this.numberPlate = numberPlate;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public void setEmissionOrder(EmissionOrder emissionOrder) {
		this.emissionOrder = emissionOrder;
	}

	public EmissionOrder getEmissionOrder() {
		return emissionOrder;
	}

	public void setManager(Person manager) {
		this.manager = manager;
	}

	public Person getManager() {
		return manager;
	}

	public void setCashier(Person cashier) {
		this.cashier = cashier;
	}

	public Person getCashier() {
		return cashier;
	}
}
