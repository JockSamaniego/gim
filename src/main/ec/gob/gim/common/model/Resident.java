package ec.gob.gim.common.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.gob.gim.common.exception.IdentificationNumberExistsException;
import org.gob.gim.common.exception.IdentificationNumberSizeException;
import org.gob.gim.common.exception.IdentificationNumberWrongException;
import org.gob.gim.common.exception.InvalidIdentificationNumberException;
import org.gob.gim.common.exception.InvalidIdentificationNumberFinishedException;
import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.ant.ucot.model.Agent;
import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.commercial.model.Business;
import ec.gob.gim.income.model.PaymentAgreement;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.security.model.User;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:43
 */
@Audited
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="residentType", discriminatorType=DiscriminatorType.STRING, length=1)
@TableGenerator(
	 name="ResidentGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Resident",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = {
		@NamedQuery(name="Resident.findAllDistinctFromIdentificationNumber", 
				query="select resident from Resident resident WHERE "+
						"resident.identificationNumber like upper(:identificationNumber) AND " +
						"resident.id NOT IN (select r.id from Resident r WHERE r.identificationNumber like :identificationPrefix )"),
						
		@NamedQuery(name="Resident.findDistinctByIdentificationNumberAndId", 
				query="select resident from Resident resident WHERE "+
						"resident.identificationNumber like upper(:identificationNumber) AND " +
						"resident.id <> :id") ,
		
		@NamedQuery(name="Resident.findByIdentificationNumber", 
				query="select resident from Resident resident left join fetch resident.currentAddress where "+
						"resident.identificationNumber like upper(:identificationNumber)"),
		
		@NamedQuery(name="Resident.findIdentificationNumber", 
				query="select resident.identificationNumber from Resident resident where "+
						"resident.identificationNumber like upper(concat(:identificationNumber,'%'))"),
						
		@NamedQuery(name="Resident.findById", 
				query="select resident from Resident resident left join fetch resident.currentAddress where "+
						"resident.id = :id"),
		@NamedQuery(name="Resident.findByCriteria", 
				query="select resident from Resident resident left join fetch resident.currentAddress where "+
						"resident.identificationNumber like upper(concat(:criteria,'%')) or " +
						"lower(resident.name) like lower(concat(:criteria, '%')) " +
						"order by resident.name"),
						
		@NamedQuery(name="Taxpayer.findByIdentificationNumber", 
				query="SELECT NEW org.gob.loja.gim.ws.dto.Taxpayer(r.id, r.identificationNumber, r.name, r.email) " +
					  "FROM Resident r " +
					  "WHERE r.identificationNumber = upper(:identificationNumber)"),
		@NamedQuery(name="Taxpayer.findPersonByIdentification", 
				query="SELECT " +
						"NEW org.gob.loja.gim.ws.dto.Taxpayer(" +
						"p.id, p.identificationNumber, p.firstName, p.lastName, p.email) " +
					  "FROM Person p " +
					  "WHERE p.identificationNumber LIKE upper(:identificationNumber)"),
		@NamedQuery(name = "Resident.findResidentByIds", query = "SELECT "
				+ "resident FROM Resident resident WHERE resident.id in (:idUsers)"),
		
		@NamedQuery(name="Taxpayer.findResidentFullByIdentification", 
				query="SELECT " +
				"NEW org.gob.loja.gim.ws.dto.Taxpayer( " +
				"r.id, r.identificationNumber, r.name, r.name, COALESCE(r.email, 'S/E', r.email), COALESCE(currentAddress.street, 'S/C', currentAddress.street), COALESCE(currentAddress.phoneNumber, 'S/N', currentAddress.phoneNumber) ) " +
				"FROM Resident r " + 
				"left join r.currentAddress currentAddress " +
				"WHERE r.identificationNumber = upper(:identificationNumber) "),
		
		@NamedQuery(name="Resident.findByCreditNoteElect", 
				query = "SELECT DISTINCT res From MunicipalBond mb "
						+ "LEFT JOIN mb.resident res "
						+ "WHERE mb.id in :mbIds"),
		
		@NamedQuery(name="Resident.findByCreditNoteElectAndCreditNote", 
				query = "SELECT DISTINCT res, cn From MunicipalBond mb "
						+ "LEFT JOIN mb.resident res "
						+ "LEFT JOIN mb.creditNote cn "
						+ "WHERE mb.id in :mbIds "),
		//macartuche
		//taxpayerwp para mostrar info en pagina web
		@NamedQuery(name="TaxpayerWP.findPersonByIdentification", 
					query="SELECT " +
							"NEW org.gob.loja.gim.ws.dto.TaxpayerWP(" +
							"p.id, p.identificationNumber, p.firstName, p.lastName, p.email, p.name) " +
							"FROM Person p " +
							"WHERE p.identificationNumber LIKE upper(:identificationNumber)"),
							
		@NamedQuery(name="TaxpayerWP.findByIdentificationNumber", 		
					query="SELECT NEW org.gob.loja.gim.ws.dto.TaxpayerWP(r.id, r.identificationNumber, r.name, r.email, r.name) " +
						  "FROM Resident r " +
						  "WHERE r.identificationNumber = upper(:identificationNumber)")
 
			  /*@NamedQuery(name="Taxpayer.findLegalEntityFullByIdentification", 
						query="SELECT " +
								"NEW org.gob.loja.gim.ws.dto.Taxpayer(" +
								"r.id, r.identificationNumber, r.name, r.email) " +
							  "FROM Resident r " +
							  "WHERE r.identificationNumber LIKE :identificationNumber")*/
					  /*@NamedQuery(name="Taxpayer.findPersonFullByIdentification", 
						query="SELECT " +
								"NEW org.gob.loja.gim.ws.dto.Taxpayer(" +
								"p.id, p.identificationNumber, p.firstName, p.lastName, p.email, currentAddress.street, currentAddress.phoneNumber) " +
							  "FROM Person p left join p.currentAddress currentAddress " +
							  "WHERE p.identificationNumber LIKE :identificationNumber"),
			  @NamedQuery(name="Taxpayer.findLegalEntityFullByIdentification", 
						query="SELECT " +
								"NEW org.gob.loja.gim.ws.dto.Taxpayer(" +
								"r.id, r.identificationNumber, r.name, r.email, currentAddress.street, currentAddress.phoneNumber) " +
							  "FROM Resident r left join r.currentAddress currentAddress " +
							  "WHERE r.identificationNumber LIKE :identificationNumber")*/
	}
)

public abstract class Resident extends Identifiable{

	@Id
	@GeneratedValue(generator="ResidentGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=30)
	private IdentificationType identificationType;
	
//	@Column(length=15, nullable=false, unique=true)
	@Column(length=15)
	protected String identificationNumber;
	
//	@Column(length=100, nullable=false)
	@Column(length=100)
	private String name;
	
	private String email;
	
	@Temporal(TemporalType.DATE)
	private Date registerDate;
	
	private Boolean isEnabledForDeferredPayments;
	
	private boolean enabledIndividualPayment;
	
	//@author macartuche
	//abonos
	@Column(nullable = true)
	private boolean enablesubscription = Boolean.FALSE;
	
	//rfam 2018-08-08
	@Column(nullable = true)
	private Boolean generateUniqueAccountt = Boolean.FALSE;
	               
	/**
	 * Relationships
	 */
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	private Address currentAddress;
	
	@OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)
	@JoinColumn(name="resident_id")
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Address> addresses;
	
	@OneToMany(mappedBy="owner", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Business> businesses;
	
	@OneToMany(mappedBy="subscriber", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value=org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Contract> contracts;
	
	@OneToMany(mappedBy="resident")
	private List<PaymentAgreement> paymentAgreements;
	
	@OneToMany(mappedBy = "resident", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<MunicipalBond> municipalBonds;
	
	@OneToMany(mappedBy = "resident", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Domain> domains;
	
	@OneToOne(fetch=FetchType.LAZY) 
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private User user;

	@OneToMany(mappedBy = "resident", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Alert> alerts;
	
	@OneToMany(mappedBy = "resident", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	//@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Agent> agents;
	
	@Transient
	private boolean elderly; //Adulto Mayor

	public Resident(){
		isEnabledForDeferredPayments = Boolean.FALSE;
		this.addresses= new ArrayList<Address>();
		this.businesses = new ArrayList<Business>();
		this.contracts = new ArrayList<Contract>();
		this.municipalBonds = new ArrayList<MunicipalBond>();
		this.domains = new ArrayList<Domain>();
		this.alerts = new ArrayList<Alert>();
		this.registerDate = java.util.Calendar.getInstance().getTime();
		this.currentAddress = new Address();
		this.add(currentAddress);
		this.enabledIndividualPayment = false;
		
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
	 * @return the identificationType
	 */
	public IdentificationType getIdentificationType() {
		return identificationType;
	}

	/**
	 * @param identificationType the identificationType to set
	 */
	public void setIdentificationType(IdentificationType identificationType) {
		this.identificationType = identificationType;
	}

	/**
	 * @return the identificationNumber
	 */
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	/**
	 * @param identificationNumber the identificationNumber to set
	 */
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name.toUpperCase().trim();
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the registerDate
	 */
	public Date getRegisterDate() {
		return registerDate;
	}

	/**
	 * @param registerDate the registerDate to set
	 */
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	/**
	 * @return the addresses
	 */
	public List<Address> getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @return the businesses
	 */
	public List<Business> getBusinesses() {
		return businesses;
	}

	/**
	 * @param businesses the businesses to set
	 */
	public void setBusinesses(List<Business> businesses) {
		this.businesses = businesses;
	}

	/**
	 * @return the contracts
	 */
	public List<Contract> getContracts() {
		return contracts;
	}



	/**
	 * @param contracts the contracts to set
	 */
	public void setContracts(List<Contract> contracts) {
		this.contracts = contracts;
	}
		

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	/**
	 * @return the domains
	 */
	public List<Domain> getDomains() {
		return domains;
	}

	/**
	 * @param domains the domains to set
	 */
	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

	public void add(Address address){
		if (!this.addresses.contains(address)){
			this.addresses.add(address);
		}
	}
	
	public void remove(Address address){
		this.addresses.remove(address);
	}
	
	public void add(Business business ){
		if(!this.businesses.contains(business)){
			this.businesses.add(business);
			business.setOwner((Resident)this);
		}
	}
	
	public void remove(Business business){
		boolean removed = this.businesses.remove(business);
		if (removed) business.setOwner((Resident)null);
	}
	
	public void add(Contract contract){
		if(!this.contracts.contains(contract)){
			this.contracts.add(contract);
			contract.setSubscriber(this);
		}
	}
	
	public void remove(Contract contract){
		boolean removed = this.contracts.remove(contract);
		if (removed) contract.setSubscriber((Resident)null);
	}
	
	public void add(MunicipalBond municipalBond){
		if (!this.municipalBonds.contains(municipalBond)){
			this.municipalBonds.add(municipalBond);
			municipalBond.setResident(this);
		}
	}
	
	public void remove(MunicipalBond municipalBond){
		boolean removed = this.municipalBonds.remove(municipalBond);
		if (removed) municipalBond.setResident((Resident)null); 
	}
	
	public void add(Domain domain){
		if (!this.domains.contains(domain)){
			this.domains.add(domain);
			domain.setResident(this);
		}
	}

	public void remove(Domain domain){
		boolean removed = this.domains.remove(domain);
		if (removed) domain.setResident((Resident)null); 
	}

	public Address getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(Address currentAddress) {
		this.currentAddress = currentAddress;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		user.setResident(this);
		this.user = user;
	}

	public List<PaymentAgreement> getPaymentAgreements() {
		return paymentAgreements;
	}

	public void setPaymentAgreements(List<PaymentAgreement> paymentAgreements) {
		this.paymentAgreements = paymentAgreements;
	}
	

	/**
	 * @return the alerts
	 */
	public List<Alert> getAlerts() {
		return alerts;
	}

	/**
	 * @param alerts the alerts to set
	 */
	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}
	
	public List<Agent> getAgents() {
		return agents;
	}

	public void setAgents(List<Agent> agents) {
		this.agents = agents;
	}
	
	
//
//	@Override
//	public boolean equals(Object object) {		
//		if (!(object instanceof Resident)) {
//			return false;
//		}
//		Resident other = (Resident) object;
//					
//		if ((this.id == null && other.id != null)
//				|| (this.id != null && !this.id.equals(other.id))) {
//			return false;
//		}
//		return true;
//	}
	
	

	public boolean isElderly() {
		return elderly;
	}

	public void setElderly(boolean elderly) {
		this.elderly = elderly;
	}

	public Boolean getIsEnabledForDeferredPayments() {
		return isEnabledForDeferredPayments;
	}

	public void setIsEnabledForDeferredPayments(Boolean isEnabledForDeferredPayments) {
		this.isEnabledForDeferredPayments = isEnabledForDeferredPayments;
	}
	
	public String getCurrentAddressAsString(){
		if(currentAddress != null){
			return currentAddress.toString();
		}
		return "";
	}
	
	public abstract Boolean isIdentificationNumberValid(String identificationNumber) throws IdentificationNumberSizeException, IdentificationNumberWrongException, 
	InvalidIdentificationNumberException, InvalidIdentificationNumberFinishedException, IdentificationNumberExistsException;

	public boolean isEnabledIndividualPayment() {
		return enabledIndividualPayment;
	}

	public void setEnabledIndividualPayment(boolean enabledIndividualPayment) {
		this.enabledIndividualPayment = enabledIndividualPayment;
	}
	
		
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Resident)) {
            return false;
        }
        
        Resident other = (Resident) obj;
        if (id != null) {
            if (!id.equals(other.id)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }


    
    /**
     * @author macartuche
     * @return
     */
 

    
	public boolean isEnablesubscription() {
		return enablesubscription;
	}

	public void setEnablesubscription(boolean enablesubscription) {
		this.enablesubscription = enablesubscription;
	}

	public Boolean getGenerateUniqueAccountt() {
		return generateUniqueAccountt;
	}

	public void setGenerateUniqueAccountt(Boolean generateUniqueAccountt) {
		this.generateUniqueAccountt = generateUniqueAccountt;
	}

}//end Resident