package ec.gob.gim.parking.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.Entry;

@Audited
@Entity
@TableGenerator(name = "ParkingLotGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "ParkingLot", 
		initialValue = 1, 
		allocationSize = 1)
		
@NamedQueries(value = {
		@NamedQuery(name = "ParkingLot.findById", query = "SELECT pl FROM ParkingLot pl "
				+ "WHERE pl.id = :id "
				+ "ORDER BY pl.id")
		})
public class ParkingLot {

	@Id
	@GeneratedValue(generator="ParkingLotGenerator",strategy=GenerationType.TABLE)
	private Long id;
	private String name;
	private String description;
	private Long parkings;
	
	@ManyToOne
	private Person manager;
	
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Entry entry;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Entry cashClosingEntry;

	@OneToMany
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name="parkinglot_id")
	private List<Journal> journals;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Entry getEntry() {
		return entry;
	}

	public void setEntry(Entry entry) {
		this.entry = entry;
	}

	public void setParkings(Long parkings) {
		this.parkings = parkings;
	}

	public Long getParkings() {
		return parkings;
	}

	public void setJournals(List<Journal> journals) {
		this.journals = journals;
	}

	public List<Journal> getJournals() {
		return journals;
	}
	
	public void addJournal(Journal journal){
		this.journals.add(journal);
	}

	public void setManager(Person manager) {
		this.manager = manager;
	}

	public Person getManager() {
		return manager;
	}

	public void setCashClosingEntry(Entry cashClosingEntry) {
		this.cashClosingEntry = cashClosingEntry;
	}

	public Entry getCashClosingEntry() {
		return cashClosingEntry;
	}
}
