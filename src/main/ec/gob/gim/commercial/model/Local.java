package ec.gob.gim.commercial.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Address;

import ec.gob.gim.commercial.model.Local;

/**
 * Local
 * 
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:37
 */
@Audited
@Entity
@TableGenerator(name = "LocalGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Local", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "Local.findByNoLicense", 
				query = "SELECT local from Local local WHERE "+
				"local.isActive = true AND (local.licenseyear = null OR local.licenseyear != :year)"),
		@NamedQuery(name = "Local.findByLicense", 
				query = "SELECT local from Local local WHERE "+
				"local.isActive = true AND local.licenseyear = :year"),	
		@NamedQuery(name = "Local.findByNoActive", 
				query = "SELECT local from Local local WHERE "+
				"local.isActive = false"),
		@NamedQuery(name = "Local.findById", 
				query = "SELECT local from Local local WHERE "+
				"local.id = :id"),
		@NamedQuery(name = "Local.findByResidentAndNoActive", query = "SELECT l FROM Local l "
				+ "LEFT JOIN FETCH l.business b "
				+ "LEFT JOIN FETCH l.address a "
				+ "LEFT JOIN FETCH b.owner o "
				+ "WHERE o.id = :ownerId AND l.isActive = false"),		
		@NamedQuery(name = "Local.findByOwnerId", query = "SELECT l FROM Local l "
				+ "LEFT JOIN FETCH l.business b "
				+ "LEFT JOIN FETCH l.address a "
				+ "LEFT JOIN FETCH b.owner o "
				+ "WHERE o.id = :ownerId AND isActive = true"),
		@NamedQuery(name = "Local.findMissingLocals", query = "select l from Local l "
				+ "LEFT JOIN FETCH l.localFeature lf "
				+ "LEFT JOIN FETCH lf.businessCatalog bc "
				+ "where l.isActive = true and bc.id = :bcId and l.id not in (:localsAlreadyGenerated)"),
		@NamedQuery(name = "Local.findLocalsByCatalog", query = "select l from Local l "
				+ "LEFT JOIN FETCH l.localFeature lf "
				+ "LEFT JOIN FETCH lf.businessCatalog bc "
				+ "where l.isActive = true and bc.id = :bcId") })
public class Local {

	@Id
	@GeneratedValue(generator = "LocalGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date closingDate;

	@Column(length = 120)
	private String name;

	@Column(length = 120)
	private String reference;

	private Boolean isActive;

	@Temporal(TemporalType.DATE)
	private Date openingDate;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Address address;

	@ManyToOne
	private Business business;

	/*
	 * @ManyToOne(cascade=CascadeType.ALL)
	 * 
	 * @JoinColumn(name="localFeature_id")
	 * 
	 * @Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	 * private LocalFeature localFeature;
	 */

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private LocalFeature localFeature;

	public Local() {
		isActive = Boolean.TRUE;
	}

	/**
	 * @return the closingDate
	 */
	public Date getClosingDate() {
		return closingDate;
	}

	/**
	 * @param closingDate
	 *            the closingDate to set
	 */
	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the openingDate
	 */
	public Date getOpeningDate() {
		return openingDate;
	}

	/**
	 * @param openingDate
	 *            the openingDate to set
	 */
	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the business
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * @param business
	 *            the business to set
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

	public String toString() {
		String code = id != null ? "" + id : "";
		String businessName = business != null ? business.toString() : "";
		String streetName = address != null ? address.toString() : "";
		return code + " " + businessName + " " + streetName;
	}

	public LocalFeature getLocalFeature() {
		return localFeature;
	}

	public void setLocalFeature(LocalFeature localFeature) {
		this.localFeature = localFeature;
	}
	
	@JoinColumn(name="code")
	@Column(length = 30)
	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@JoinColumn(name="licenseyear")
	@Column(length = 4)
	private String licenseyear;

	public String getLicenseyear() {
		return licenseyear;
	}

	public void setLicenseyear(String licenseyear) {
		this.licenseyear = licenseyear;
	}



	

}// end Local