package ec.gob.gim.cementery.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;

/**
 * @author GADM-Loja Ronald
 * @version 1.0
 * @created 20-Feb-2014
 */
@Audited
@Entity
@TableGenerator(
	 name="CrematoriumGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Crematorium",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = { 
		 @NamedQuery(name = "Crematorium.findAll", 
				 	query = "SELECT crematorium FROM Crematorium crematorium " +
				 			"order by crematorium.name"), 

		 @NamedQuery(name = "Crematorium.findAllName", 
				 	query = "SELECT crematorium.name FROM Crematorium crematorium " +
				 			"order by crematorium.name"), 

		 @NamedQuery(name = "Crematorium.findById", 
		 			query = "SELECT crematorium FROM Crematorium crematorium " +
		 					"where crematorium.id = :id") 
 
})

public class Crematorium {

	@Id
	@GeneratedValue(generator="CrematoriumGenerator",strategy=GenerationType.TABLE)
	private Long id;
	@Column(length = 60)
	private String address;
	@Column(length = 20)
	private String fono;
	@Column(length = 60)
	private String name;
	private Double rate;
	@Column(length = 50)
	private String reference;

	@OneToOne(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "manager_id")
	private Resident manager;

	public Crematorium(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFono() {
		return fono;
	}

	public void setFono(String fono) {
		this.fono = fono;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Resident getManager() {
		return manager;
	}

	public void setManager(Resident manager) {
		this.manager = manager;
	}

}