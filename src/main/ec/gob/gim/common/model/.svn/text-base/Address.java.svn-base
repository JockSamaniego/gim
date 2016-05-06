package ec.gob.gim.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:24
 */
@Audited
@Entity
@TableGenerator(
	 name="AddressGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Address",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = {
		@NamedQuery(name="Address.findById", 
				query="select address from Address address where address.id = :id "),
		@NamedQuery(name="Address.findCurrentAddressByResidentId",
				query="SELECT currentAddress " +
						"FROM Resident r left join r.currentAddress currentAddress " +
						"WHERE r.id = :residentId"),
		@NamedQuery(name="Address.findCurrentAddressByIdentificationNumber",
				query="SELECT currentAddress " +
						"FROM Resident r left join r.currentAddress currentAddress " +
						"WHERE r.identificationNumber LIKE :identificationNumber")				
})
public class Address {
	
	@Id
	@GeneratedValue(generator="AddressGenerator",strategy=GenerationType.TABLE)
	private Long id;

	@Column(length=25, nullable=false)
	private String city;
	
	@Column(length=25, nullable=false)
	private String country;

	@Column(length=15)
	private String mobileNumber;
	
	@Column(length=60)
	private String neighborhood;
	
	@Column(length=15)
	private String phoneNumber;
	
	@Column(length=10)
	private String postalCode;
	
	@Column(length=120, nullable=false)
	private String street;
	
	//agregado para actualizacion SRI
	//@author macartuche
	//@date 2015-07-13
//	@Column(length=255)
	private String street2;
	
	@Column(length=180)
	private String neighborhood2;
	
	@JoinColumn(name="parish")
	@Column(length = 30)
	private String parish;

	public Address(){

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
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		if(city != null) this.city = city.toUpperCase();
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		if(country != null) this.country = country.toUpperCase();
	}

	/**
	 * @return the mobileNumber
	 */
	public String getMobileNumber() {
		return mobileNumber;
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/**
	 * @return the neighborhood
	 */
	public String getNeighborhood() {
		return neighborhood;
	}

	/**
	 * @param neighborhood the neighborhood to set
	 */
	public void setNeighborhood(String neighborhood) {
		if(neighborhood != null) this.neighborhood = neighborhood.toUpperCase();
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		if(street != null) this.street = street.toUpperCase();
	}
	
	@Override
	public String toString() {
		String s = street != null ? street : "";
		return s;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		if(street2 != null) this.street2 = street2.toUpperCase(); 
	}

	public String getNeighborhood2() {
		return neighborhood2;
	}

	public void setNeighborhood2(String neighborhood2) {
		this.neighborhood2 = neighborhood2;
	}
	
	
	public String getParish() {
		return parish;
	}

	public void setParish(String parish) {
		this.parish = parish;
	}
	
	
	
	
}//end Address