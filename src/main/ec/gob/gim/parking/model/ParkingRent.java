package ec.gob.gim.parking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.Contract;

@Audited
@Entity
@TableGenerator(name = "ParkingRentGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "ParkingRent", 
		initialValue = 1, 
		allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "ParkingRent.findNumberOfContracsInParkingLotByDate", query = "SELECT count(p.id) FROM ParkingRent p join p.contract c join p.parkingLot pl "
				+ "WHERE :comingDate BETWEEN c.subscriptionDate AND c.expirationDate AND pl.id = :id")
})

public class ParkingRent {

	@Id
	@GeneratedValue(generator="ParkingRentGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@OneToOne
	private Contract contract;
	@ManyToOne
	private ParkingLot parkingLot;
	@ManyToOne
	private Person manager;
	
	@Transient
	private Boolean isMunicipalEmployee;
	
	private String reference;
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public Boolean getIsMunicipalEmployee() {
		return isMunicipalEmployee;
	}
	public void setIsMunicipalEmployee(Boolean isMunicipalEmployee) {
		this.isMunicipalEmployee = isMunicipalEmployee;
	}
		
	public Contract getContract() {
		return contract;
	}
	public void setContract(Contract contract) {
		this.contract = contract;
	}
	public ParkingLot getParkingLot() {
		return parkingLot;
	}
	public void setParkingLot(ParkingLot parkingLot) {
		this.parkingLot = parkingLot;
	}
	public Person getManager() {
		return manager;
	}
	public void setManager(Person manager) {
		this.manager = manager;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}
