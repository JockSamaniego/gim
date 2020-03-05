package ec.gob.gim.cadaster.model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

import org.hibernate.annotations.Cascade;
import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.CheckingRecord;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.security.model.User;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:31
 * 
 * modified: Ronald Paladines
 */
@Audited
@Entity
@TableGenerator(
		name = "DomainGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "Domain", 
		initialValue = 1, allocationSize = 1)

@NamedQueries(value = {
		@NamedQuery(name = "Domain.setLotValueM2", query = "UPDATE Domain domain "
			+ "SET domain.valueBySquareMeter = :valueBySquareMeter "
			+ "WHERE domain.id IN (:domainId)"), 
		@NamedQuery(name = "Domain.setAppraisals", query = "UPDATE Domain domain "
			+ "SET domain.lotAppraisal = :lotAppraisal, "
			+ "domain.buildingAppraisal = :buildingAppraisal, "
			+ "domain.commercialAppraisal = :commercialAppraisal "
			+ "WHERE domain.id IN (:domainId)"), 
		@NamedQuery(name = "Domain.setLotValueM2Tmp", query = "UPDATE Domain domain "
			+ "SET domain.valueBySquareMeterTmp = :valueBySquareMeterTmp "
			+ "WHERE domain.id IN (:domainId)"), 
		@NamedQuery(name = "Domain.setAppraisalsTmp", query = "UPDATE Domain domain "
			+ "SET domain.lotAppraisalTmp = :lotAppraisalTmp, "
			+ "domain.buildingAppraisalTmp = :buildingAppraisalTmp, "
			+ "domain.commercialAppraisalTmp = :commercialAppraisalTmp "
			+ "WHERE domain.id IN (:domainId)"),
		//@tag exoneraciones2017
		@NamedQuery(name = "Domain.totalAppraisalCurrentDomainByResident", query = "select sum(domain.commercialAppraisal) from Domain domain "	
			+ "join domain.resident resident "
			+ "join domain.property property "			
			+ "where resident.id = :residentId and domain.currentProperty is not null and property.deleted=false"),
		@NamedQuery(name  = "Domain.findPendingTransfersByPropertyId", 
			query = "select domain from Domain domain " +
					"  where domain.property.id = :propertyId and " +
					"  domain.changeOwnerConfirmed is null"
			)
	})

public class Domain {

	@Id
	@GeneratedValue(generator = "DomainGenerator", strategy = GenerationType.TABLE)
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	@Temporal(TemporalType.TIME)
	private Date creationTime;
	
	private Boolean hasDeed;
	private Boolean isActive;
	private Integer notaryNumber;
	private String observations;
	private String beneficiaries;
	
	@Column(length=10)
	private String blockNumberTransfer;

	@Column(length=10)
	private String lotNumberTransfer;

	@Column(length=60)
	private String urbanizationTransfer;

	@Column(length=100)
	private String streetTransfer;

	@Column(length = 25)
	private String realStateNumber;

	private BigDecimal valueBySquareMeter;

	private BigDecimal valueBySquareMeterTmp;

	private BigDecimal valueTransaction;

	private BigDecimal lotAppraisal;

	private BigDecimal lotAppraisalTmp;

	private BigDecimal buildingAppraisal;

	private BigDecimal buildingAppraisalTmp;

	private BigDecimal commercialAppraisal;
	
	private BigDecimal newBuildingValue;

	private BigDecimal commercialAppraisalTmp;

	private String description;
	
	@Column(length=30)
	private String tramitNumber;
	
	@Column(length=150)
	private String legalRevision;
	
	@Transient
	private BigDecimal valueForCalculate;
	
	private BigDecimal specialContribution;
	
	private Boolean changeOwnerConfirmed;

	private BigDecimal totalAreaConstruction;
	
	private BigDecimal buildingAreaTransfer;
	
	private BigDecimal lotAreaTransfer;

	private String sellName;
	/**
	 * Relationships
	 */

	@ManyToOne
	@JoinColumn(name = "purchaseType_id")
	private PurchaseType purchaseType;

	@ManyToOne
	@JoinColumn(name = "notarysProvince_id")
	private TerritorialDivision notarysProvince;

	@ManyToOne
	@JoinColumn(name = "notarysCity_id")
	private TerritorialDivision notarysCity;

	@ManyToOne
	private Resident resident;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private PropertyUse propertyUse;

	@ManyToOne
	private Property property;

	@ManyToOne
	private User userRegister;

	@ManyToOne
	private Resident personLegalRevision;

	@OneToOne
	private Property currentProperty;
/*
	@OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<MunicipalBond> municipalBonds;*/
	
	
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "domain_id")
	private List<Boundary> boundaries;
	
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "domain_id")
	private List<DispatchReason> dispatchReasons ;

	@OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<Mortgage> mortgages;

//	@OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
//	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
//	@OrderBy("id desc")
//	private List<Appraisal> appraisals;
//	
	@OneToMany(cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	@JoinColumn(name = "domain_id")
	private List<CheckingRecord> checkingRecords;
	
	@OneToMany(mappedBy = "domain", cascade = CascadeType.ALL)
	@Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private List<DomainOwner> domainOwners;

	public Domain() {
		//this.municipalBonds = new ArrayList<MunicipalBond>();
		this.mortgages = new ArrayList<Mortgage>();
//		this.appraisals = new ArrayList<Appraisal>();
		this.boundaries = new ArrayList<Boundary>();
		this.dispatchReasons = new ArrayList<DispatchReason>();
		this.checkingRecords = new ArrayList<CheckingRecord>();
		this.totalAreaConstruction = BigDecimal.ZERO;
		this.specialContribution = BigDecimal.ZERO;
		this.newBuildingValue = BigDecimal.ZERO;
		this.domainOwners = new ArrayList<DomainOwner>();
		// /
		// /
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getHasDeed() {
		return hasDeed;
	}

	public void setHasDeed(Boolean hasDeed) {
		this.hasDeed = hasDeed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getNotaryNumber() {
		return notaryNumber;
	}

	public void setNotaryNumber(Integer notaryNumber) {
		this.notaryNumber = notaryNumber;
	}

	public String getObservations() {
		return observations;
	}

	public void setObservations(String observations) {
		this.observations = observations;
	}

	public String getRealStateNumber() {
		return realStateNumber;
	}

	public void setRealStateNumber(String realStateNumber) {
		this.realStateNumber = realStateNumber;
	}

	public BigDecimal getValueBySquareMeter() {
		return valueBySquareMeter;
	}

	public void setValueBySquareMeter(BigDecimal valueBySquareMeter) {
		this.valueBySquareMeter = valueBySquareMeter;
	}

	/**
	 * @return the valueTransaction
	 */
	public BigDecimal getValueTransaction() {
		return valueTransaction;
	}

	/**
	 * @param valueTransaction
	 *            the valueTransaction to set
	 */
	public void setValueTransaction(BigDecimal valueTransaction) {
		this.valueTransaction = valueTransaction;
	}
	/*
	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}*/

//	/**
//	 * @return the appraisals
//	 */
//	public List<Appraisal> getAppraisals() {
//		return appraisals;
//	}
//
//	/**
//	 * @param appraisals
//	 *            the appraisals to set
//	 */
//	public void setAppraisals(List<Appraisal> appraisals) {
//		this.appraisals = appraisals;
//	}
//
	public PurchaseType getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(PurchaseType purchaseType) {
		this.purchaseType = purchaseType;
	}

	public TerritorialDivision getNotarysProvince() {
		return notarysProvince;
	}

	public void setNotarysProvince(TerritorialDivision notarysProvince) {
		this.notarysProvince = notarysProvince;
	}

	public TerritorialDivision getNotarysCity() {
		return notarysCity;
	}

	public void setNotarysCity(TerritorialDivision notarysCity) {
		this.notarysCity = notarysCity;
	}

	public List<Mortgage> getMortgages() {
		return mortgages;
	}

	public void setMortgages(List<Mortgage> mortgages) {
		this.mortgages = mortgages;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}
	/*
	public void add(MunicipalBond municipalBond) {
		if (!this.municipalBonds.contains(municipalBond)) {
			this.municipalBonds.add(municipalBond);
			municipalBond.setDomain(this);
		}
	}

	public void remove(MunicipalBond municipalBond) {
		boolean removed = this.municipalBonds.remove(municipalBond);
		if (removed)
			municipalBond.setDomain((Domain) null);
	}
	*/
	public void add(DispatchReason dispatchReason) {
		if (!this.dispatchReasons.contains(dispatchReason)) {
			this.dispatchReasons.add(dispatchReason);			
		}
	}

	public void remove(DispatchReason dispatchReason) {
		if (this.dispatchReasons.contains(dispatchReason)) {
			this.dispatchReasons.remove(dispatchReason);			
		}
	}
	
	public void add(CheckingRecord checkingRecord) {
		if (!this.checkingRecords.contains(checkingRecord)) {
			this.checkingRecords.add(checkingRecord);
		}
	}

	public void remove(CheckingRecord checkingRecord) {
		this.checkingRecords.remove(checkingRecord);
	}

	public void add(Mortgage mortgage) {
		if (!this.mortgages.contains(mortgage)) {
			this.mortgages.add(mortgage);
			mortgage.setDomain(this);
		}
	}

	public void remove(Mortgage mortgage) {
		boolean removed = this.mortgages.remove(mortgage);
		if (removed)
			mortgage.setDomain((Domain) null);
	}

//	public void add(Appraisal appraisal) {
//		if (!this.appraisals.contains(appraisal)) {
//			this.appraisals.add(appraisal);
//			appraisal.setDomain(this);
//		}
//	}
//
//	public void remove(Appraisal appraisal) {
//		boolean removed = this.appraisals.remove(appraisal);
//		if (removed)
//			appraisal.setDomain((Domain) null);
//	}
//	
//	
	public void add(Boundary boundary) {
		if (!this.boundaries.contains(boundary)) {
			this.boundaries.add(boundary);			
		}
	}

	public void remove(Boundary boundary) {
		if(this.boundaries.contains(boundary))
		this.boundaries.remove(boundary);
	}

	/**
	 * @return the currentProperty
	 */
	public Property getCurrentProperty() {
		return currentProperty;
	}

	/**
	 * @param currentProperty
	 *            the currentProperty to set
	 */
	public void setCurrentProperty(Property currentProperty) {
		if (currentProperty != this.currentProperty) {
			this.currentProperty = currentProperty;
			if (currentProperty != null)
				this.currentProperty.setCurrentDomain(this);
		}
	}

	public void setLotAppraisal(BigDecimal lotAppraisal) {
		this.lotAppraisal = lotAppraisal;
	}

	public BigDecimal getLotAppraisal() {
		return lotAppraisal;
	}

	public void setBuildingAppraisal(BigDecimal buildingAppraisal) {
		this.buildingAppraisal = buildingAppraisal;
	}

	public BigDecimal getBuildingAppraisal() {
		return buildingAppraisal;
	}

	public void setCommercialAppraisal(BigDecimal commercialAppraisal) {
		this.commercialAppraisal = commercialAppraisal;
	}

	public BigDecimal getCommercialAppraisalTmp() {
		return commercialAppraisalTmp;
	}

	public void setCommercialAppraisalTmp(BigDecimal commercialAppraisalTmp) {
		this.commercialAppraisalTmp = commercialAppraisalTmp;
	}

	public BigDecimal getCommercialAppraisal() {
		return commercialAppraisal;
	}

	public void setBoundaries(List<Boundary> boundaries) {
		this.boundaries = boundaries;
	}

	public List<Boundary> getBoundaries() {
		return boundaries;
	}

	public void setDispatchReasons(List<DispatchReason> dispatchReasons) {
		this.dispatchReasons = dispatchReasons;
	}

	public List<DispatchReason> getDispatchReasons() {
		return dispatchReasons;
	}

	public void setSpecialContribution(BigDecimal specialContribution) {
		this.specialContribution = specialContribution;
	}

	public BigDecimal getSpecialContribution() {
		return specialContribution;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public void setCheckingRecords(List<CheckingRecord> checkingRecords) {
		this.checkingRecords = checkingRecords;
	}

	public List<CheckingRecord> getCheckingRecords() {
		return checkingRecords;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setValueForCalculate(BigDecimal valueForCalculate) {
		this.valueForCalculate = valueForCalculate;
	}

	public BigDecimal getValueForCalculate() {
		return valueForCalculate;
	}

	public void setChangeOwnerConfirmed(Boolean changeOwnerConfirmed) {
		this.changeOwnerConfirmed = changeOwnerConfirmed;
	}

	public Boolean getChangeOwnerConfirmed() {
		return changeOwnerConfirmed;
	}
	
	public BigDecimal getTotalAreaConstruction() {
		return totalAreaConstruction;
	}

	public void setTotalAreaConstruction(BigDecimal totalAreaConstruction) {
		this.totalAreaConstruction = totalAreaConstruction;
	}
	
	@Override
	public String toString() {
		if(beneficiaries != null && beneficiaries.length() > 0){
			return beneficiaries;
		}
		if(resident != null){
			return resident.getName();
		}
		return super.toString();
	}

	public BigDecimal getValueBySquareMeterTmp() {
		return valueBySquareMeterTmp;
	}

	public void setValueBySquareMeterTmp(BigDecimal valueBySquareMeterTmp) {
		this.valueBySquareMeterTmp = valueBySquareMeterTmp;
	}

	public BigDecimal getLotAppraisalTmp() {
		return lotAppraisalTmp;
	}

	public void setLotAppraisalTmp(BigDecimal lotAppraisalTmp) {
		this.lotAppraisalTmp = lotAppraisalTmp;
	}

	public BigDecimal getBuildingAppraisalTmp() {
		return buildingAppraisalTmp;
	}

	public void setBuildingAppraisalTmp(BigDecimal buildingAppraisalTmp) {
		this.buildingAppraisalTmp = buildingAppraisalTmp;
	}

	public String getBeneficiaries() {
		return beneficiaries;
	}

	public void setBeneficiaries(String beneficiaries) {
		this.beneficiaries = beneficiaries;
	}
	
	public BigDecimal getNewBuildingValue() {
		return newBuildingValue;
	}

	public void setNewBuildingValue(BigDecimal newBuildingValue) {
		this.newBuildingValue = newBuildingValue;
	}

	public PropertyUse getPropertyUse() {
		return propertyUse;
	}

	public void setPropertyUse(PropertyUse propertyUse) {
		this.propertyUse = propertyUse;
	}

	public String getTramitNumber() {
		return tramitNumber;
	}

	public void setTramitNumber(String tramitNumber) {
		this.tramitNumber = tramitNumber.toUpperCase();
	}

	public User getUserRegister() {
		return userRegister;
	}

	public void setUserRegister(User userRegister) {
		this.userRegister = userRegister;
	}

	public String getLegalRevision() {
		return legalRevision;
	}

	public void setLegalRevision(String legalRevision) {
		this.legalRevision = legalRevision;
	}

	public BigDecimal getBuildingAreaTransfer() {
		return buildingAreaTransfer;
	}

	public void setBuildingAreaTransfer(BigDecimal buildingAreaTransfer) {
		this.buildingAreaTransfer = buildingAreaTransfer;
	}

	public BigDecimal getLotAreaTransfer() {
		return lotAreaTransfer;
	}

	public void setLotAreaTransfer(BigDecimal lotAreaTransfer) {
		this.lotAreaTransfer = lotAreaTransfer;
	}

	public Resident getPersonLegalRevision() {
		return personLegalRevision;
	}

	public void setPersonLegalRevision(Resident personLegalRevision) {
		this.personLegalRevision = personLegalRevision;
	}

	public String getBlockNumberTransfer() {
		return blockNumberTransfer;
	}

	public void setBlockNumberTransfer(String blockNumberTransfer) {
		this.blockNumberTransfer = blockNumberTransfer.toUpperCase();
	}

	public String getLotNumberTransfer() {
		return lotNumberTransfer;
	}

	public void setLotNumberTransfer(String lotNumberTransfer) {
		this.lotNumberTransfer = lotNumberTransfer;
	}

	public String getUrbanizationTransfer() {
		return urbanizationTransfer;
	}

	public void setUrbanizationTransfer(String urbanizationTransfer) {
		this.urbanizationTransfer = urbanizationTransfer.toUpperCase();
	}

	public String getStreetTransfer() {
		return streetTransfer;
	}

	public void setStreetTransfer(String streetTransfer) {
		this.streetTransfer = streetTransfer.toUpperCase();
	}

	public String getSellName() {
		return sellName;
	}

	public void setSellName(String sellName) {
		this.sellName = sellName;
	}

	public List<DomainOwner> getDomainOwners() {
		return domainOwners;
	}

	public void setDomainOwners(List<DomainOwner> domainOwners) {
		this.domainOwners = domainOwners;
	}
	
	public void add(DomainOwner owner) {
		if (!this.domainOwners.contains(owner)) {
			this.domainOwners.add(owner);			
		}
	}

	public void remove(DomainOwner owner) {
		if (this.domainOwners.contains(owner)) {
			this.domainOwners.remove(owner);			
		}
	}

}