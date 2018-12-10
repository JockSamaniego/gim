package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.MunicipalBond;

@Audited
@Entity
@TableGenerator(
	 name="CreditNoteGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="CreditNote",
	 initialValue=1, allocationSize=1
 )
 @NamedQueries({
	 @NamedQuery(name="CreditNote.findAll", query="SELECT o FROM CreditNote o"),
	 @NamedQuery(name="CreditNote.findBetweenDates", query="SELECT o FROM CreditNote o WHERE o.date BETWEEN :startDate AND :endDate ORDER BY o.date"),
	 @NamedQuery(name="CreditNote.findActive", query="SELECT o FROM CreditNote o WHERE o.isActive = true"),
	 @NamedQuery(name="CreditNote.findActiveByResidentId", query="SELECT distinct o FROM CreditNote o LEFT JOIN FETCH o.paymentFractions f WHERE o.isActive = true AND o.useToPay = true AND o.resident.id = :residentId"),
	 @NamedQuery(name="CreditNote.reactivate", 
	 			query="UPDATE CreditNote cn " +
	 				  "SET cn.isActive=true, " +
	 				  "    cn.availableAmount=(cn.availableAmount+:availableAmount) " +
	 				  "WHERE cn.id = :creditNoteId ")
 })
public class CreditNote {
	@Id
	@GeneratedValue(generator="CreditNoteGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private String reference;
	
	private String resolutionNumber;
	
	private String description;
	
	private Date date;
	
	private BigDecimal value;
	
	private BigDecimal availableAmount;
	
	private Boolean isActive;
	
	private Boolean useToPay;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Resident resident;
	
	@OneToMany(mappedBy="creditNote")
	private List<PaymentFraction> paymentFractions;
	
	@OneToMany(mappedBy="creditNote")
	private List<MunicipalBond> municipalBonds;
	
	@ManyToOne
	@JoinColumn(name="creditNoteType_id")
	private CreditNoteType creditNoteType;
	
	private Long parentCreditNote_id;
	
	public CreditNote() {
		paymentFractions = new ArrayList<PaymentFraction>();
		municipalBonds = new ArrayList<MunicipalBond>();
		isActive = Boolean.TRUE;
		useToPay = Boolean.TRUE;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getResolutionNumber() {
		return resolutionNumber;
	}

	public void setResolutionNumber(String resolutionNumber) {
		this.resolutionNumber = resolutionNumber;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<MunicipalBond> getMunicipalBonds() {
		return municipalBonds;
	}

	public void setMunicipalBonds(List<MunicipalBond> municipalBonds) {
		this.municipalBonds = municipalBonds;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/*
	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
	*/

	public Resident getResident() {
		return resident;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public List<PaymentFraction> getPaymentFractions() {
		return paymentFractions;
	}

	public void setPaymentFractions(List<PaymentFraction> paymentFractions) {
		this.paymentFractions = paymentFractions;
	}
	
	public CreditNoteType getCreditNoteType() {
		return creditNoteType;
	}

	public void setCreditNoteType(CreditNoteType creditNoteType) {
		this.creditNoteType = creditNoteType;
	}
	
	public void add(MunicipalBond municipalBond){
		if (!this.municipalBonds.contains(municipalBond)){
			this.municipalBonds.add(municipalBond);
			municipalBond.setCreditNote(this);
		}
	}
	
	public void remove(MunicipalBond municipalBond){
		boolean removed = this.municipalBonds.remove(municipalBond);
		if (removed) {
			municipalBond.setResident((Resident)null); 
		}
	}
	
	public void add(PaymentFraction paymentFraction) {
		if (!this.paymentFractions.contains(paymentFraction)) {
			this.paymentFractions.add(paymentFraction);
			paymentFraction.setCreditNote(this);
		}
	}
	
	public void remove(PaymentFraction paymentFraction){
		boolean removed = this.paymentFractions.remove(paymentFraction);
		if (removed) {
			paymentFraction.setPayment(null);
		}
	}
	
	@Override
	public String toString() {
		return resolutionNumber;
	}
	
	/*
	public BigDecimal getAvailableAmount(){
		BigDecimal available = value;
		for(PaymentFraction pf : paymentFractions){
			available = available.subtract(pf.getReceivedAmount());
		}
		return available;
	}
	*/

	public Boolean getUseToPay() {
		return useToPay;
	}

	public void setUseToPay(Boolean useToPay) {
		this.useToPay = useToPay;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public Long getParentCreditNote_id() {
		return parentCreditNote_id;
	}

	public void setParentCreditNote_id(Long parentCreditNote_id) {
		this.parentCreditNote_id = parentCreditNote_id;
	}


}
