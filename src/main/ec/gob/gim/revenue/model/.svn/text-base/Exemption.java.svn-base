package ec.gob.gim.revenue.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Resident;


@Audited
@Entity
@TableGenerator(name="ExemptionGenerator",
				pkColumnName="name",
				pkColumnValue="Exemption",
				table="IdentityGenerator",
				valueColumnName="value",
				allocationSize = 1, initialValue = 1)
@NamedQueries({
	@NamedQuery(name = "Exemption.findByFiscalPeriodAndResident", 
			query = "select e from Exemption e " +
					"where e.fiscalPeriod.id =:fiscalPeriodId and e.resident.id = :residentId and e.exemptionType.id <> 4"),
	@NamedQuery(name = "Exemption.findByFiscalPeriod", 
			query = "select e from Exemption e " +
					"where e.fiscalPeriod.id =:fiscalPeriodId"),
	@NamedQuery(name = "Exemption.findByFiscalPeriodAndActive", 
			query = "select e from Exemption e " +
					"where e.fiscalPeriod.id =:fiscalPeriodId and e.active=true order by e.id"),
	
	//macartuche
	//agregar fecha de creacion
	@NamedQuery(name = "Exemption.findByFiscalPeriodAndActiveAndDate", 
	query = "select e from Exemption e " +
			"where e.fiscalPeriod.id =:fiscalPeriodId and (e.creationDate between :start and :end) and e.active=true order by e.id")

	})
public class Exemption {
	
	@Id
	@GeneratedValue(generator="ExemptionGenerator", strategy=GenerationType.TABLE)
	private Long id;

	@ManyToOne
	@JoinColumn(name="resident_id")
	private Resident resident;
	
	@ManyToOne
	@JoinColumn(name="partner_id")
	private Resident partner;
	
	@ManyToOne
	@JoinColumn(name="fiscalPeriod_id")
	private FiscalPeriod fiscalPeriod;
	
	@ManyToOne
	@JoinColumn(name="exemptionType_id")
	private ExemptionType exemptionType;
	
	@OneToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
	@JoinColumn(name="exemption_id")
	private List<ExemptionForProperty> propertiesInExemption = new ArrayList<ExemptionForProperty>();
	
	private BigDecimal propertiesAppraisal;
	
	@Transient
	private BigDecimal patrimony;

	private BigDecimal vehiclesAppraisal;
	
	private BigDecimal personalAssets;
	
	private BigDecimal commercialValues;
	
	private BigDecimal discountPercentage;
	
	private BigDecimal exemptionPercentage;
	
	@Column(length=150)
	private String reference;
	
	@Column(length=200)
	private String explanation;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;
	
	@Temporal(TemporalType.DATE)
	private Date expirationDate;
	
	private Long discountYearNumber;
	
	private Boolean active;
	
	public Exemption(){
		propertiesAppraisal = BigDecimal.ZERO;
		vehiclesAppraisal = BigDecimal.ZERO;
		personalAssets = BigDecimal.ZERO;
		commercialValues = BigDecimal.ZERO;
		discountPercentage = BigDecimal.ZERO;
		exemptionPercentage = new BigDecimal(100);
		active = true;
	}
	
	
	public void calculatePatrimony(){
		patrimony = BigDecimal.ZERO;
		patrimony = patrimony.add(propertiesAppraisal).add(vehiclesAppraisal).add(personalAssets).add(commercialValues);		
	}
	
	public void calculatePatrimony(BigDecimal propertyAppraisal){
		patrimony = BigDecimal.ZERO;
		patrimony = patrimony.add(propertyAppraisal).add(vehiclesAppraisal).add(personalAssets).add(commercialValues);		
	}
	
	public BigDecimal getPropertiesAppraisal() {
		return propertiesAppraisal;
	}

	public void setPropertiesAppraisal(BigDecimal propertiesAppraisal) {
		this.propertiesAppraisal = propertiesAppraisal;
	}

	public BigDecimal getVehiclesAppraisal() {
		return vehiclesAppraisal;
	}

	public void setVehiclesAppraisal(BigDecimal vehiclesAppraisal) {
		this.vehiclesAppraisal = vehiclesAppraisal;
	}

	public BigDecimal getPersonalAssets() {
		return personalAssets;
	}

	public void setPersonalAssets(BigDecimal personalAssets) {
		this.personalAssets = personalAssets;
	}

	public BigDecimal getCommercialValues() {
		return commercialValues;
	}

	public void setCommercialValues(BigDecimal commercialValues) {
		this.commercialValues = commercialValues;
	}

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public FiscalPeriod getFiscalPeriod() {
		return fiscalPeriod;
	}

	public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
		this.fiscalPeriod = fiscalPeriod;
	}
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Resident getPartner() {
		return partner;
	}

	public void setPartner(Resident partner) {
		this.partner = partner;
	}
	
	public BigDecimal getPatrimony() {
		if(patrimony == null) calculatePatrimony();
		return patrimony;
	}

	public void setPatrimony(BigDecimal patrimony) {
		this.patrimony = patrimony;
	}


	public BigDecimal getDiscountPercentage() {
		return discountPercentage;
	}


	public void setDiscountPercentage(BigDecimal discountPercentage) {
		this.discountPercentage = discountPercentage;
	}


	public ExemptionType getExemptionType() {
		return exemptionType;
	}


	public void setExemptionType(ExemptionType exemptionType) {
		this.exemptionType = exemptionType;
	}


	public BigDecimal getExemptionPercentage() {
		return exemptionPercentage;
	}


	public void setExemptionPercentage(BigDecimal exemptionPercentage) {
		this.exemptionPercentage = exemptionPercentage;
	}


	public String getExplanation() {
		return explanation;
	}


	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}


	public String getReference() {
		return reference;
	}


	public void setReference(String reference) {
		this.reference = reference;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}


	public Boolean getActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}


	public List<ExemptionForProperty> getPropertiesInExemption() {
		return propertiesInExemption;
	}


	public void setPropertiesInExemption(
			List<ExemptionForProperty> propertiesInExemption) {
		this.propertiesInExemption = propertiesInExemption;
	}


	public Long getDiscountYearNumber() {
		return discountYearNumber;
	}


	public void setDiscountYearNumber(Long discountYearNumber) {
		this.discountYearNumber = discountYearNumber;
	}

}
