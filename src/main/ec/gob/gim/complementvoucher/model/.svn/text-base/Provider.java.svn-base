package ec.gob.gim.complementvoucher.model;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.IdentificationType;

  
@Audited
@Entity 
@TableGenerator(
	 name="ProviderGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Provider",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = { 
		@NamedQuery(name="Provider.findAll", query="SELECT p FROM Provider p "),
		@NamedQuery(name="Provider.findByCriteria", query="SELECT p FROM Provider p WHERE p.identificationNumber like :criteria or lower(p.lastname) like :criteria")
	})
public class Provider{

	@Id
	@GeneratedValue(generator="ProviderGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Column(length=15)
	protected String identificationNumber;
	
	@Column(length=100)
	private String name;
	
	@Column(length=200)
	private String lastname;
	
	@Column(length=100)
	private String email;
 
	@Enumerated(EnumType.STRING)
	private IdentificationType identificationtype;
	
	@Temporal(TemporalType.DATE)
	private Date registerDate; 

	@Column(length=255)
	private String address;
	 
	@OneToOne(mappedBy="provider", fetch = FetchType.LAZY)
	private ElectronicVoucher electronicVoucher;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public IdentificationType getIdentificationtype() {
		return identificationtype;
	}

	public void setIdentificationtype(IdentificationType identificationtype) {
		this.identificationtype = identificationtype;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
 
	public ElectronicVoucher getElectronicVoucher() {
		return electronicVoucher;
	}

	public void setElectronicVoucher(ElectronicVoucher electronicVoucher) {
		this.electronicVoucher = electronicVoucher;
	}
}