package ec.gob.gim.cadaster.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.PassiveRate;

@Audited
@Entity
@TableGenerator(
	 name="CapitalGainGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="CapitalGain",
	 initialValue=1, allocationSize=1
)
public class CapitalGain {
	
	@Id
	@GeneratedValue(generator="CapitalGainGenerator",strategy=GenerationType.TABLE)
	private Long id;
		
	private BigDecimal buildingImprovements = BigDecimal.ZERO;
	private BigDecimal acquisitionEnhancements= BigDecimal.ZERO;
	private BigDecimal cem = BigDecimal.ZERO;
	private BigDecimal acquisitionValue= BigDecimal.ZERO;;
	
	@Temporal(TemporalType.DATE)
	private Date purchaseDate;
	
	@Temporal(TemporalType.DATE)
	private Date saleDate;
	
	@ManyToOne
	@JoinColumn(name="passiverate_id")
	private PassiveRate passiveRate;
	
	@ManyToOne
	@JoinColumn(name="property_id")
	private Property property;
	
	
	private BigDecimal adjustementFactor;
	
	private BigDecimal adjustmentValue;
	
	
	private BigDecimal adjustedAcquisitionValue;
	
	private BigDecimal ordinaryProfit;
	
	private BigDecimal salesValue;
	
	private BigDecimal extraOrdinaryProfit;
	
	private BigDecimal taxable;
	
	private BigDecimal paidTotal;	
	
	@Temporal(TemporalType.DATE)
	private Date creationDate;
	
	@Temporal(TemporalType.TIME)
	private Date creationTime;


	@Temporal(TemporalType.DATE)
	private Date modificationDate;
	
	@Temporal(TemporalType.TIME)
	private Date modificationTime;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getBuildingImprovements() {
		return buildingImprovements;
	}

	public void setBuildingImprovements(BigDecimal buildingImprovements) {
		this.buildingImprovements = buildingImprovements;
	}

	public BigDecimal getAcquisitionEnhancements() {
		return acquisitionEnhancements;
	}

	public void setAcquisitionEnhancements(BigDecimal acquisitionEnhancements) {
		this.acquisitionEnhancements = acquisitionEnhancements;
	}

	public BigDecimal getCem() {
		return cem;
	}

	public void setCem(BigDecimal cem) {
		this.cem = cem;
	}

	public BigDecimal getAcquisitionValue() {
		return acquisitionValue;
	}

	public void setAcquisitionValue(BigDecimal acquisitionValue) {
		this.acquisitionValue = acquisitionValue;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Date getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}

	public PassiveRate getPassiveRate() {
		return passiveRate;
	}

	public void setPassiveRate(PassiveRate passiveRate) {
		this.passiveRate = passiveRate;
	}

	public BigDecimal getAdjustmentValue() {
		return adjustmentValue;
	}

	public void setAdjustmentValue(BigDecimal adjustmentValue) {
		this.adjustmentValue = adjustmentValue;
	}

	public BigDecimal getAdjustedAcquisitionValue() {
		return adjustedAcquisitionValue;
	}

	public void setAdjustedAcquisitionValue(BigDecimal adjustedAcquisitionValue) {
		this.adjustedAcquisitionValue = adjustedAcquisitionValue;
	}

	public BigDecimal getOrdinaryProfit() {
		return ordinaryProfit;
	}

	public void setOrdinaryProfit(BigDecimal ordinaryProfit) {
		this.ordinaryProfit = ordinaryProfit;
	}

	public BigDecimal getSalesValue() {
		return salesValue;
	}

	public void setSalesValue(BigDecimal salesValue) {
		this.salesValue = salesValue;
	}

	public BigDecimal getExtraOrdinaryProfit() {
		return extraOrdinaryProfit;
	}

	public void setExtraOrdinaryProfit(BigDecimal extraOrdinaryProfit) {
		this.extraOrdinaryProfit = extraOrdinaryProfit;
	}

	public BigDecimal getTaxable() {
		return taxable;
	}

	public void setTaxable(BigDecimal taxable) {
		this.taxable = taxable;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public BigDecimal getAdjustementFactor() {
		return adjustementFactor;
	}

	public void setAdjustementFactor(BigDecimal adjustementFactor) {
		this.adjustementFactor = adjustementFactor;
	}
	
}