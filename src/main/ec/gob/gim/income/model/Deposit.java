package ec.gob.gim.income.model;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.MunicipalBond;


/**
 * Abono pagado a una obligación
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:31
 */
@Audited
@Entity
@TableGenerator(
	 name="DepositGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Deposit",
	 initialValue=1, allocationSize=1
)

@NamedQueries({
	@NamedQuery(name="Deposit.setAsPrinted", 
			query="UPDATE Deposit d SET d.isPrinted = true WHERE d.id IN (:printedDepositIds)"),
			
	@NamedQuery(name="Deposit.correctDepositNumberBetweenDates", 
			query="SELECT NEW org.gob.gim.revenue.view.ReportView(d.payment.cashier.id,d.payment.cashier.name, count(d.id)) from Deposit d "		
				+ "where d.payment.date Between :startDate and :endDate and d.municipalBond is not null " 
				+ "GROUP BY d.payment.cashier.id,d.payment.cashier.name"),
		
	@NamedQuery(name="Deposit.correctDepositNumberByCashierBetweenDates", 
			query="SELECT NEW org.gob.gim.revenue.view.ReportView(d.payment.cashier.id,d.payment.cashier.name, count(d.id)) from Deposit d "		
				+ "where d.payment.date Between :startDate and :endDate and d.municipalBond is not null and d.payment.cashier.id = :cashierId " 
				+ "GROUP BY d.payment.cashier.id,d.payment.cashier.name"),
				
	@NamedQuery(name="Deposit.reversedDepositNumberBetweenDates", 
			query="SELECT NEW org.gob.gim.revenue.view.ReportView(d.payment.cashier.id,d.payment.cashier.name, count(d.id)) from Deposit d "		
				+ "where d.payment.date Between :startDate and :endDate and d.reversedMunicipalBond is not null " 
				+ "GROUP BY d.payment.cashier.id,d.payment.cashier.name"),				
	
	@NamedQuery(name="Deposit.reversedDepositNumberByCashierBetweenDates", 
			query="SELECT NEW org.gob.gim.revenue.view.ReportView(d.payment.cashier.id,d.payment.cashier.name, count(d.id)) from Deposit d "		
				+ "where d.payment.date Between :startDate and :endDate and d.reversedMunicipalBond is not null and d.payment.cashier.id = :cashierId " 
				+ "GROUP BY d.payment.cashier.id,d.payment.cashier.name"),					
				
	@NamedQuery(name="Deposit.setAsValid", 
			query="UPDATE Deposit d SET d.concept = :concept, d.status =:status, d.municipalBond.id=d.reversedMunicipalBond.id, d.reversedMunicipalBond.id=null WHERE d.id IN (:depositIds)"),
	@NamedQuery(name="Deposit.setAsVoid", 
			query="UPDATE Deposit d SET d.concept = :concept, d.status =:status, d.reversedMunicipalBond.id=d.municipalBond.id, d.municipalBond.id=null, "
					+ "d.reversedDate = :reversedDate, d.reversedTime = :reversedTime, d.reversedResident = :reversedResident "
					+ "WHERE d.id IN (:depositIds)"),
	@NamedQuery(name="Deposit.findDepositsByResidentIdAndDate", 
			query="SELECT d FROM Deposit d " +
					" LEFT JOIN FETCH d.municipalBond mb" +
					//" LEFT JOIN FETCH d.reversedMunicipalBond rmb" +
					" LEFT JOIN FETCH mb.entry e" +
					//" LEFT JOIN FETCH rmb.entry e" +
					" LEFT JOIN FETCH mb.receipt re" +
					//" LEFT JOIN FETCH rmb.receipt re" +
					" LEFT JOIN FETCH mb.resident r" +
					//" LEFT JOIN FETCH rmb.resident r" +
					" WHERE r.id = :residentId AND "+
					" d.date = :date" +
					" ORDER BY d.id"),
	
	@NamedQuery(name="Deposit.findDepositsFromPayments", 
			query="SELECT d FROM Deposit d " +
					" LEFT JOIN FETCH d.municipalBond mb" +
					" LEFT JOIN FETCH d.reversedMunicipalBond " +				
					" LEFT JOIN FETCH mb.receipt r" +
					" LEFT JOIN FETCH d.payment p" +					
					" WHERE p.id in (:paymentsIds) and" +
					" d.status = 'VALID'" +
					" ORDER BY d.id"),
					
	@NamedQuery(name="Deposit.findDepositsFromBondsInAgreementFromPayments", 
			query="SELECT d FROM Deposit d " +
					" LEFT JOIN FETCH d.municipalBond mb" +									
					" LEFT JOIN FETCH mb.receipt r" +
					" LEFT JOIN FETCH d.payment p" +					
					" WHERE p.date between :startDate and :endDate " +
					" AND mb.paymentAgreement is not null " +
					" AND d.date between :startDate and :endDate " +
					" AND p.cashier.id = :cashierId " +
					" AND d.status = 'VALID'" +
					" AND p.status = 'VALID'" +
					" ORDER BY d.id"),
					
		@NamedQuery(name="Deposit.findDepositsFromBondsInSubscriptionFromPayments", 
					query="SELECT d FROM Deposit d " +
							" LEFT JOIN FETCH d.municipalBond mb" +									
							" LEFT JOIN FETCH mb.receipt r" +
							" LEFT JOIN FETCH d.payment p" +					
							" WHERE p.date between :startDate and :endDate " +
							" AND d.date between :startDate and :endDate " +
							" AND p.cashier.id = :cashierId " +
							" AND (select count(maux) " +
							" from MunicipalbondAux maux " +
							" where maux.municipalbond.id = mb.id " +
							" AND maux.typepayment = 'SUBSCRIPTION' " +
							" AND maux.status = 'VALID') > 0 " +
							" AND d.status = 'VALID'" +
							" AND p.status = 'VALID'" +
							" ORDER BY d.id"),
					
	@NamedQuery(name="Deposit.findDepositsByResidentIdAndCashierAndDate", 
			query="SELECT distinct d FROM Deposit d " +
					" LEFT JOIN FETCH d.municipalBond mb" +
					//" LEFT JOIN FETCH d.reversedMunicipalBond rmb" +
					" LEFT JOIN FETCH mb.entry e" +
					//" LEFT JOIN FETCH rmb.entry e" +
					" LEFT JOIN FETCH mb.receipt re" +
					//" LEFT JOIN FETCH rmb.receipt re" +
					" LEFT JOIN FETCH mb.resident r" +
					//" LEFT JOIN FETCH rmb.resident r" +
					" LEFT JOIN FETCH d.payment p" +
					" WHERE r.id = :residentId AND p.cashier.id=:cashierId AND"+
					" d.date = :date" +
					" ORDER BY d.id"),
					
	@NamedQuery(name="Deposit.findDepositsReversedBetweenDates", 
			query="SELECT distinct d FROM Deposit d " +
					" LEFT JOIN FETCH d.municipalBond mb" +
					" LEFT JOIN FETCH d.reversedMunicipalBond rmb" +
					" LEFT JOIN FETCH rmb.entry e" +
					" LEFT JOIN FETCH mb.receipt re" +
					" LEFT JOIN FETCH mb.resident r" +
					" LEFT JOIN FETCH d.payment p" +
					" LEFT JOIN FETCH p.cashier c" +
					" WHERE d.reversedDate is not null"+
					" AND (d.date between :startDate and :endDate)" +
					" ORDER BY d.date, d.id ASC"),
					
	@NamedQuery(name ="Deposit.findByMunicipalBondId",
			  query="SELECT d FROM Deposit d " +
					"left join fetch d.payment p " +
					"left join fetch p.cashier c " +
			  		"WHERE d.municipalBond.id = :municipalBondId " +
			  		"ORDER BY d.date"),
			  		
	@NamedQuery(name ="Deposit.findByMunicipalBondIds",
	  query="SELECT d.id FROM Deposit d " +
	  		" WHERE d.municipalBond.id in (:municipalBondIds) " +
	  		" ORDER BY d.date"),
			  
	@NamedQuery(name = "Deposit.findByMunicipalBondIdForPrinting",
				query =   "SELECT DISTINCT deposit FROM Deposit deposit "
						+ "LEFT JOIN FETCH deposit.municipalBond mb "
						+ "LEFT JOIN FETCH mb.entry "
						+ "LEFT JOIN FETCH mb.resident res "
						+ "LEFT JOIN FETCH res.currentAddress "
						+ "LEFT JOIN FETCH deposit.payment payment "
						+ "LEFT JOIN FETCH mb.institution "
						+ "LEFT JOIN FETCH mb.receipt r "
						+ "LEFT JOIN FETCH mb.adjunct adjunct "
						+ "LEFT JOIN FETCH mb.emitter emitter "
						+ "LEFT JOIN FETCH r.receiptType "
						+ "LEFT JOIN FETCH payment.cashier cashier "
						+ "LEFT JOIN FETCH mb.items it "
						+ "LEFT JOIN FETCH mb.discountItems di "
						+ "LEFT JOIN FETCH mb.surchargeItems si "
						+ "LEFT JOIN FETCH mb.taxItems ti "
						+ "LEFT JOIN FETCH it.entry "
						+ "LEFT JOIN FETCH si.entry "
						+ "LEFT JOIN FETCH di.entry "
						+ "LEFT JOIN FETCH ti.tax "
						+ "WHERE mb.id = :municipalBondId "),			  
			  
	@NamedQuery(name="Deposit.SumPaidTotalsByEntry", 
		    query="select e.id, e.name, e.code, ac.accountCode, ac.budgetCertificateCode, SUM(d.value), " +
		    	  " SUM(d.interest), SUM(d.paidTaxes) from Deposit d join d.municipalBond m join m.entry e left join e.account ac where d.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId" +
		    	  " GROUP BY e.id, e.name, e.code, ac.accountCode, ac.budgetCertificateCode ORDER BY ac.budgetCertificateCode"),
	
	@NamedQuery(name="Transfer.findByPaymentDateAndCashier", 
    		query="SELECT NEW org.gob.loja.gim.ws.dto.Transfer(d.value, d.municipalBond.id, d.concept) " +
				  " FROM Deposit d " +
				  " WHERE d.date = :paymentDate " +
				  " AND d.payment.cashier.id = :cashierId"),

	@NamedQuery(name="Transfer.findTotalByPaymentDateAndCashier", 
			query="SELECT SUM(d.value) " +
				  " FROM Deposit d " +
				  " WHERE d.date = :paymentDate " +
				  " AND d.payment.cashier.id = :cashierId"), 
				  //" AND d.status = 'VALID'"),
 
    @NamedQuery(name="Deposit.findByIds", 
			query="SELECT DISTINCT d FROM Deposit d " +
				  " LEFT JOIN FETCH d.payment p " +
				  " LEFT JOIN FETCH p.paymentFractions pf " +
				  " LEFT JOIN FETCH pf.creditNote pf " +
				  " WHERE d.id IN (:depositIds)"),
				  
	@NamedQuery(name="Deposit.findDetailedBetweenDatesByCashier",  
		    query="SELECT NEW ec.gob.gim.revenue.model.MunicipalBondView(mb.resident.identificationNumber, mb.entry.name,mb.resident.name, " +
		    		" mb.number, d.date, d.time, d.value) from Deposit d " +
		    		" join d.municipalBond mb " + 
		    		" WHERE mb is not null AND " +
		    		" d.status = 'VALID' AND " +
		    		" d.date BETWEEN :startDate and :endDate AND " +
				  	" d.payment.cashier.id = :cashierId ORDER BY d.date, d.time, mb.number"),
	
	@NamedQuery(name="Deposit.findPaymentsByDepositIds",
				query=  "SELECT DISTINCT d.payment FROM Deposit d " +
						" WHERE d.id IN (:depositIds)"),

	@NamedQuery(name="Deposit.findReversedDepositsByCashierIdAndDate",
				query=  "SELECT d FROM Deposit d "
						+ "LEFT JOIN FETCH d.payment p "
						+ "JOIN FETCH d.reversedMunicipalBond rmb "
						+ "WHERE d.payment.cashier.id = :cashierId AND (d.date between :paymentStartDate and :paymentEndDate) "
						+ "ORDER BY d.reversedDate, d.reversedTime"),
	
	
	@NamedQuery(name="Deposit.findByExternalTransaccionId",
				query=  "SELECT d FROM Deposit d " 
						+ " WHERE d.payment.externalTransactionId= :transactionId"),

	
})
public class Deposit {

	@Id
	@GeneratedValue(generator="DepositGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private Integer number;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date date;
	
	@Temporal(TemporalType.TIME)
	private java.util.Date time;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date reversedDate;
	
	@Temporal(TemporalType.TIME)
	private java.util.Date reversedTime;
	
	private BigDecimal capital;
	
	private BigDecimal interest;
	
	private BigDecimal surcharge;
	
	private BigDecimal discount;
	
	private BigDecimal paidTaxes;
	
	private BigDecimal balance;
	
	private BigDecimal value;
	
	private Boolean isPrinted;
	
	private String concept;
	
	@Enumerated(EnumType.STRING)
	@Column(length=10)
	private FinancialStatus status; 
		
	@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private Payment payment;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private MunicipalBond municipalBond;
	
	@ManyToOne
	@JoinColumn(name="reversedPayment_id")
	private Payment reversedPayment;
	
	@ManyToOne
	@JoinColumn(name="reversedMunicipalBond_id") //TODO
	private MunicipalBond reversedMunicipalBond;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="reversedResident_id") //TODO
	private Resident reversedResident;
	
	@Transient
	private Boolean hasConflict;
	
	@Transient
	private Boolean isSelected;
	
	public Deposit() {
		status = FinancialStatus.VALID;
		isPrinted = Boolean.FALSE;
		isSelected = Boolean.FALSE;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		return value;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Boolean getHasConflict() {
		return hasConflict;
	}

	public void setHasConflict(Boolean hasConflict) {
		this.hasConflict = hasConflict;
	}

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public java.util.Date getTime() {
		return time;
	}

	public void setTime(java.util.Date time) {
		this.time = time;
	}

	public BigDecimal getPaidTaxes() {
		return paidTaxes;
	}

	public void setPaidTaxes(BigDecimal paidTaxes) {
		this.paidTaxes = paidTaxes;
	}
	
	public String toString(){
		return String.format("%010d", id != null ? id : 0);
	}

	public Boolean getIsPrinted() {
		return isPrinted;
	}

	public void setIsPrinted(Boolean isPrinted) {
		this.isPrinted = isPrinted;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public void setStatus(FinancialStatus status) {
		this.status = status;
	}

	public FinancialStatus getStatus() {
		return status;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public MunicipalBond getReversedMunicipalBond() {
		return reversedMunicipalBond;
	}

	public void setReversedMunicipalBond(MunicipalBond reversedMunicipalBond) {
		this.reversedMunicipalBond = reversedMunicipalBond;
	}

	public Payment getReversedPayment() {
		return reversedPayment;
	}

	public void setReversedPayment(Payment reversedPayment) {
		this.reversedPayment = reversedPayment;
	}

	public BigDecimal getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(BigDecimal surcharge) {
		this.surcharge = surcharge;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public java.util.Date getReversedDate() {
		return reversedDate;
	}

	public void setReversedDate(java.util.Date reversedDate) {
		this.reversedDate = reversedDate;
	}

	public java.util.Date getReversedTime() {
		return reversedTime;
	}

	public void setReversedTime(java.util.Date reversedTime) {
		this.reversedTime = reversedTime;
	}

	public Resident getReversedResident() {
		return reversedResident;
	}

	public void setReversedResident(Resident reversedResident) {
		this.reversedResident = reversedResident;
	}

}