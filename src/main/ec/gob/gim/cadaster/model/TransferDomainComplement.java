package ec.gob.gim.cadaster.model;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ec.gob.gim.security.model.User;

/**
 * @author jock samaniego
 * @version 1.0
 * 
 */
@Entity
@TableGenerator(
		name = "TransferDomainComplementGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "TransferDomainComplement", 
		initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "DomainComplement.findFinalByDomain", query = "SELECT domainComplement from TransferDomainComplement domainComplement WHERE "
				+ "domainComplement.id = (select max(domainComplement.id) from TransferDomainComplement domainComplement where domainComplement.domain.id = :domain_id)")
})
public class TransferDomainComplement {
	@Id
	@GeneratedValue(generator = "TransferDomainComplementGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	private String secondSellerTitle;
	
	private String secondSellerName;
	
	private String secondSellerIdentification;
	
	private String secondBuyerTitle;
	
	private String secondBuyerName;
	
	private String secondBuyerIdentification;
	
	private String notary;
	
	private String country;
	
	private int propertyNumber;
	
	private String buildingType;
	
	private String previousParish;
	
	private BigDecimal acquisitionValue;
	
	private BigDecimal improvementsValues;
	
	private BigDecimal CEMValues;
	
	private BigDecimal othersValues;
	
	private BigDecimal contractualValue;
	
	private BigDecimal proportionalValues;
	
	private String documentsDescription;
	
	private String observations;
	
	private String codeDocument;
	
	private String userDocument;
	
	@Temporal(TemporalType.DATE)
	private Date validityDate;
	
	@ManyToOne
	@JoinColumn(name = "domain_id")
	private Domain domain;
	
	private Boolean isActive;

	public TransferDomainComplement() {
		this.country = "LOJA";
		this.creationDate = new Date();
		this.isActive = Boolean.TRUE;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSecondSellerTitle() {
		return secondSellerTitle;
	}


	public void setSecondSellerTitle(String secondSellerTitle) {
		this.secondSellerTitle = secondSellerTitle;
	}


	public String getSecondSellerName() {
		return secondSellerName;
	}


	public void setSecondSellerName(String secondSellerName) {
		this.secondSellerName = secondSellerName;
	}


	public String getSecondSellerIdentification() {
		return secondSellerIdentification;
	}


	public void setSecondSellerIdentification(String secondSellerIdentification) {
		this.secondSellerIdentification = secondSellerIdentification;
	}


	public String getSecondBuyerTitle() {
		return secondBuyerTitle;
	}


	public void setSecondBuyerTitle(String secondBuyerTitle) {
		this.secondBuyerTitle = secondBuyerTitle;
	}


	public String getSecondBuyerName() {
		return secondBuyerName;
	}


	public void setSecondBuyerName(String secondBuyerName) {
		this.secondBuyerName = secondBuyerName;
	}


	public String getSecondBuyerIdentification() {
		return secondBuyerIdentification;
	}


	public void setSecondBuyerIdentification(String secondBuyerIdentification) {
		this.secondBuyerIdentification = secondBuyerIdentification;
	}

	public String getNotary() {
		return notary;
	}


	public void setNotary(String notary) {
		this.notary = notary;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public int getPropertyNumber() {
		return propertyNumber;
	}


	public void setPropertyNumber(int propertyNumber) {
		this.propertyNumber = propertyNumber;
	}


	public String getBuildingType() {
		return buildingType;
	}


	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public BigDecimal getAcquisitionValue() {
		return acquisitionValue;
	}


	public void setAcquisitionValue(BigDecimal acquisitionValue) {
		this.acquisitionValue = acquisitionValue;
	}


	public BigDecimal getContractualValue() {
		return contractualValue;
	}


	public void setContractualValue(BigDecimal contractualValue) {
		this.contractualValue = contractualValue;
	}


	public BigDecimal getProportionalValues() {
		return proportionalValues;
	}


	public void setProportionalValues(BigDecimal proportionalValues) {
		this.proportionalValues = proportionalValues;
	}


	public String getObservations() {
		return observations;
	}


	public void setObservations(String observations) {
		this.observations = observations;
	}


	public Date getValidityDate() {
		return validityDate;
	}


	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getPreviousParish() {
		return previousParish;
	}


	public void setPreviousParish(String previousParish) {
		this.previousParish = previousParish;
	}


	public BigDecimal getImprovementsValues() {
		return improvementsValues;
	}


	public void setImprovementsValues(BigDecimal improvementsValues) {
		this.improvementsValues = improvementsValues;
	}


	public BigDecimal getCEMValues() {
		return CEMValues;
	}


	public void setCEMValues(BigDecimal cEMValues) {
		CEMValues = cEMValues;
	}


	public BigDecimal getOthersValues() {
		return othersValues;
	}


	public void setOthersValues(BigDecimal othersValues) {
		this.othersValues = othersValues;
	}


	public String getDocumentsDescription() {
		return documentsDescription;
	}


	public void setDocumentsDescription(String documentsDescription) {
		this.documentsDescription = documentsDescription;
	}


	public String getCodeDocument() {
		return codeDocument;
	}


	public void setCodeDocument(String codeDocument) {
		this.codeDocument = codeDocument;
	}


	public String getUserDocument() {
		return userDocument;
	}


	public void setUserDocument(String userDocument) {
		this.userDocument = userDocument;
	}


	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}


	public Domain getDomain() {
		return domain;
	}


	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	
	
}
