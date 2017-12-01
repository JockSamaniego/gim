package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.gob.gim.common.DateUtils;

import ec.gob.gim.cadaster.model.Domain;
import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.adjunct.detail.EarlyTransferDiscount;

@Entity
@DiscriminatorValue("DOM")
public class DomainTransfer extends Adjunct{
	@ManyToOne
	private Property property;
	@ManyToOne
	private Domain domain;
	private Date buyingDate;
	private Date transactionDate;
	private String seller;
	@Transient
	private String address;
	private String buyer;
	private String cadastralCode;
	private String previousCadastralCode;
	
	private String notaryNumber;	
	
	private BigDecimal lotAppraisal;
	private BigDecimal buildingAppraisal;
	private BigDecimal commercialAppraisal;
	private BigDecimal buyingTransactionValue;
	private BigDecimal transactionValue;
	private BigDecimal lotArea;
	private BigDecimal buildingArea;
	private BigDecimal improvementsContribution;
	private BigDecimal newBuildingValue;
	private Boolean mortgageDiscount;
	private Boolean emitWithoutReport;
	
	//rfam 2017-11-29 pedido de la parra sin memo, se pasara por parte de informatica
	private Boolean isHalfDiscount;
	private BigDecimal halfDiscountAmount;
	
	/*@ManyToOne
	@JoinColumn(name="itemcatalog_id")
	private ItemCatalog domainTransferType;*/
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private EarlyTransferDiscount earlyTransferDiscount; 
	
	public DomainTransfer() {
		mortgageDiscount = Boolean.FALSE;
		earlyTransferDiscount = EarlyTransferDiscount.NOT_APPLICABLE;
		buyingDate = new Date();
		transactionDate = new Date();
		isHalfDiscount = Boolean.FALSE;
	}
	
	@Transient
	private List<Domain> domainTransfers = new LinkedList<Domain>();
	
	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}	
	
	@Override
	public String toString() {	
		return "Vendedor: " + seller + " - Comprador: " + buyer;		
	}
	
	public List<ValuePair> getDetails(){
		List<ValuePair> details = new LinkedList<ValuePair>();		
//		ValuePair pair = new ValuePair("Clave catastral",cadastralCode);
		ValuePair pair = new ValuePair("Codigo Territorial",cadastralCode);
		details.add(pair);
//		pair = new ValuePair("Clave anterior",previousCadastralCode);
		pair = new ValuePair("Clave Catastral",previousCadastralCode);
		details.add(pair);
		pair = new ValuePair("Vendedor", seller);
		details.add(pair);
		pair = new ValuePair("Comprador", buyer);
		details.add(pair);
		pair = new ValuePair("Dirección", address);
		details.add(pair);
		pair = new ValuePair("Tipo transferencia", domain!=null ? domain.getPurchaseType().getName() : "" );
		details.add(pair);
		pair = new ValuePair("Informacion Adicional", getAditionalInformation() );
		details.add(pair);
		
		if(isHalfDiscount!=null && isHalfDiscount) {
			pair = new ValuePair("Descuento 50%", ""+halfDiscountAmount );
			details.add(pair);	
		}
		
		return details;
	}
	
	private String getAditionalInformation(){
		String aux = "";
		if(buyingDate != null)aux = aux + "Fecha Compra: " + DateUtils.formatDate(buyingDate) + "; ";
		if(buyingTransactionValue != null)aux = aux + "Valor Compra: " + buyingTransactionValue + "; ";
		if(transactionValue != null)aux = aux + "Valor Venta: " + transactionValue + "; ";
		if(improvementsContribution != null)aux = aux + "Contribución Mejoras: " + improvementsContribution + "; ";
		if(newBuildingValue != null)aux = aux + "Obra Nueva: " + newBuildingValue;
		
		//if(isHalfDiscount != null)aux = aux + "Descuento 50%: " + halfDiscountAmount;
		return aux;
	}
	
	public BigDecimal getCommercialAppraisal() {
		return commercialAppraisal;
	}

	public void setCommercialAppraisal(BigDecimal commercialAppraisal) {
		this.commercialAppraisal = commercialAppraisal;
		if(commercialAppraisal == null){
			this.commercialAppraisal = BigDecimal.ZERO;
		}
	}

	public BigDecimal getLotAppraisal() {
		return lotAppraisal;
	}

	public void setLotAppraisal(BigDecimal lotAppraisal) {
		this.lotAppraisal = lotAppraisal;
		if(lotAppraisal == null){
			this.lotAppraisal = BigDecimal.ZERO;
		}
	}

	public BigDecimal getBuildingAppraisal() {
		return buildingAppraisal;
	}

	public void setBuildingAppraisal(BigDecimal buildingAppraisal) {
		this.buildingAppraisal = buildingAppraisal;
		if(buildingAppraisal == null){
			this.buildingAppraisal = BigDecimal.ZERO;
		}
	}

	public BigDecimal getTransactionValue() {
		return transactionValue;
	}

	public void setTransactionValue(BigDecimal transactionValue) {
		this.transactionValue = transactionValue;
		if(transactionValue == null){
			this.transactionValue = BigDecimal.ZERO;
		}
	}

	public BigDecimal getLotArea() {
		return lotArea;
	}

	public void setLotArea(BigDecimal lotArea) {
		this.lotArea = lotArea;
	}

	public BigDecimal getBuildingArea() {
		return buildingArea;
	}

	public void setBuildingArea(BigDecimal buildingArea) {
		this.buildingArea = buildingArea;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public String getPreviousCadastralCode() {
		return previousCadastralCode;
	}

	public void setPreviousCadastralCode(String previousCadastralCode) {
		this.previousCadastralCode = previousCadastralCode;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public List<Domain> getDomainTransfers() {
		return domainTransfers;
	}

	public void setDomainTransfers(List<Domain> domainTransfers) {
		this.domainTransfers = domainTransfers;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getMortgageDiscount() {
		return mortgageDiscount;
	}

	public void setMortgageDiscount(Boolean mortgageDiscount) {
		this.mortgageDiscount = mortgageDiscount;
	}

	public Boolean getEmitWithoutReport() {
		return emitWithoutReport;
	}

	public void setEmitWithoutReport(Boolean emitWithoutReport) {
		this.emitWithoutReport = emitWithoutReport;
	}

	public EarlyTransferDiscount getEarlyTransferDiscount() {
		return earlyTransferDiscount;
	}

	public void setEarlyTransferDiscount(EarlyTransferDiscount earlyTransferDiscount) {
		this.earlyTransferDiscount = earlyTransferDiscount;
	}

	public Date getBuyingDate() {
		return buyingDate;
	}

	/*public ItemCatalog getDomainTransferType() {
		return domainTransferType;
	}

	public void setDomainTransferType(ItemCatalog domainTransferType) {
		this.domainTransferType = domainTransferType;
	}*/

	public void setBuyingDate(Date buyingDate) {
		this.buyingDate = buyingDate;
		if(buyingDate == null){
			this.buyingDate = new Date();
		}
	}

	public String getNotaryNumber() {
		return notaryNumber;
	}

	public void setNotaryNumber(String notaryNumber) {
		this.notaryNumber = notaryNumber;
	}

	public BigDecimal getBuyingTransactionValue() {
		return buyingTransactionValue;
	}

	public BigDecimal getHalfDiscountAmount() {
		return halfDiscountAmount;
	}

	public void setHalfDiscountAmount(BigDecimal halfDiscountAmount) {
		this.halfDiscountAmount = halfDiscountAmount;
	}

	public Boolean getIsHalfDiscount() {
		return isHalfDiscount;
	}

	public void setIsHalfDiscount(Boolean isHalfDiscount) {
		this.isHalfDiscount = isHalfDiscount;
	}

	public void setBuyingTransactionValue(BigDecimal buyingTransactionValue) {
		this.buyingTransactionValue = buyingTransactionValue;
		if(buyingTransactionValue == null){
			this.buyingTransactionValue = BigDecimal.ZERO;
		}
	}

	public BigDecimal getImprovementsContribution() {
		return improvementsContribution;
	}

	public void setImprovementsContribution(BigDecimal improvementsContribution) {
		this.improvementsContribution = improvementsContribution;
		if(improvementsContribution == null){
			this.improvementsContribution = BigDecimal.ZERO;
		}
	}

	public BigDecimal getNewBuildingValue() {
		return newBuildingValue;
	}

	public void setNewBuildingValue(BigDecimal newBuildingValue) {
		this.newBuildingValue = newBuildingValue;
		if(newBuildingValue == null){
			this.newBuildingValue = BigDecimal.ZERO;
		} 
		
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
		if(transactionDate == null){
			this.transactionDate = new Date();
		}
	}

	public BigDecimal getTaxableBase(){
		BigDecimal taxableBase = BigDecimal.ZERO;
		if(transactionValue != null && commercialAppraisal != null){
			taxableBase = (transactionValue.compareTo(commercialAppraisal) > 0) ? transactionValue : commercialAppraisal;
		} else {
			if(transactionValue != null){
				taxableBase = transactionValue; 
			} else {
				taxableBase = commercialAppraisal;
			}
		}
		return taxableBase;
	}
	
	public Integer getYearsFromLastTransfer(Date currentDomainDate, Date newDomainDate) {
		currentDomainDate = currentDomainDate != null ? currentDomainDate : new Date();
		newDomainDate =  newDomainDate != null ? newDomainDate : new Date();
		Calendar begin = DateUtils.getTruncatedInstance(currentDomainDate);
		Calendar end = DateUtils.getTruncatedInstance(newDomainDate);
		
		Integer y1 = begin.get(Calendar.YEAR);
		Integer d1 = begin.get(Calendar.DAY_OF_YEAR);
		Integer y2 = end.get(Calendar.YEAR);
		Integer d2 = end.get(Calendar.DAY_OF_YEAR);

		Integer difYears = y2 - y1;
		if (d2 < d1) {
			difYears--;
		} 

		return difYears;	
	}
	
	
	@Override
	public String getCode() {
		String pcc = previousCadastralCode != null ? previousCadastralCode : "";  
		String cc = cadastralCode != null ? cadastralCode : "";
		return pcc+" - "+cc;
	}
}
