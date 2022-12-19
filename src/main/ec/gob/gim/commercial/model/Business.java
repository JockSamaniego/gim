package ec.gob.gim.commercial.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Address;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;

/**
 * Negocio:
 * 
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:27
 */
@Audited
@Entity
@TableGenerator(name = "BusinessGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "Business", initialValue = 1, allocationSize = 1)
public class Business {

	@Id
	@GeneratedValue(generator = "BusinessGenerator", strategy = GenerationType.TABLE)
	private Long id; 
	
	@Column(length = 120)
	private String name;

	@ManyToOne
	@JoinColumn(name = "businessType_id")
	private BusinessType businessType;

	@ManyToMany
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "business_id")
	private List<EconomicActivity> economicActivities;

	@ManyToMany
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "businessCatalog_id")
	private List<BusinessCatalog> businessCatalogs;

	@ManyToOne
	private Person manager;

	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Local> locales;

	@ManyToOne
	private Resident owner;
	
	// bomberos
	
	private String address;
	
	@Column(length = 120)
	private String reference;

	private Boolean isActive;

	public Business() {
		locales = new ArrayList<Local>();
		economicActivities = new ArrayList<EconomicActivity>();
		businessCatalogs = new ArrayList<BusinessCatalog>();
		this.isActive = Boolean.TRUE;
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
	 * @return the economicActivities
	 */
	public List<EconomicActivity> getEconomicActivities() {
		return economicActivities;
	}

	/**
	 * @param economicActivities
	 *            the economicActivities to set
	 */
	public void setEconomicActivities(List<EconomicActivity> economicActivities) {
		this.economicActivities = economicActivities;
	}

	/**
	 * @return the manager
	 */
	public Person getManager() {
		return manager;
	}

	/**
	 * @param manager
	 *            the manager to set
	 */
	public void setManager(Person manager) {
		this.manager = manager;
	}

	/**
	 * @return the locales
	 */
	public List<Local> getLocales() {
		return locales;
	}

	/**
	 * @param locales
	 *            the locales to set
	 */
	public void setLocales(List<Local> locales) {
		this.locales = locales;
	}

	/**
	 * @return the owner
	 */
	public Resident getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	public void setOwner(Resident owner) {
		this.owner = owner;
	}

	public void add(EconomicActivity economicActivity) {
		if (!this.economicActivities.contains(economicActivity)
				&& economicActivity != null) {
			this.economicActivities.add(economicActivity);
		}
	}

	public void remove(EconomicActivity economicActivity) {
		// boolean removed = this.economicActivities.remove(economicActivity);
		this.economicActivities.remove(economicActivity);
	}

	public void add(BusinessCatalog businessCatalog) {
		if (!this.businessCatalogs.contains(businessCatalog)
				&& businessCatalog != null) {
			this.businessCatalogs.add(businessCatalog);
		}
	}

	public void remove(BusinessCatalog businessCatalog) {
		this.businessCatalogs.remove(businessCatalog);
	}

	public void add(Local local) {
		if (!this.locales.contains(local) && local != null) {
			this.locales.add(local);
			local.setBusiness((Business) this);
		}
	}

	public void remove(Local local) {
		boolean removed = this.locales.remove(local);
		if (removed)
			local.setBusiness((Business) null);
	}

	@Override
	public String toString() {
		String s = name != null ? name : "";
		return s;
	}

	public BusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
	}

	public List<BusinessCatalog> getBusinessCatalogs() {
		return businessCatalogs;
	}

	public void setBusinessCatalogs(List<BusinessCatalog> businessCatalogs) {
		this.businessCatalogs = businessCatalogs;
	}

	@Column(length = 13)
	private String cedruc;

	public String getCedruc() {
		return cedruc;
	}

	public void setCedruc(String cedruc) {
		this.cedruc = cedruc;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	

}// end Business
