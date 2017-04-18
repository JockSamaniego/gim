package ec.gob.gim.income.model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.FinancialStatus;
import ec.gob.gim.common.model.Person;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:39
 */

@Audited
@Entity
@TableGenerator(
	 name="PaymentGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Payment",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {@NamedQuery(name="Payment.findAll", query="select payment from Payment payment"),
						@NamedQuery(name="Payment.findByIds", 
							query="select distinct(payment) from Payment payment " +
									"join payment.deposits d " +
									"join payment.paymentFractions pf " +
									"join d.municipalBond m " +
									"join m.resident r " +
									"join m.entry e " +
									"left join e.account ac " +
									"where payment.id in (:ids)"),

		 			   @NamedQuery(name="Payment.findByCashierAndDate", 
		 						    query="select distinct(payment) from Payment payment " +
		 						    		"join payment.deposits d " +
		 						    		"join payment.paymentFractions pf " +
		 						    		"join d.municipalBond m " +
		 						    		"join m.resident r " +
		 						    		"join m.entry e " +
		 						    		"left join e.account ac " +
		 						    		"where payment.cashier.id = :cashierId and " +
		 						    		"payment.date = :date AND " +
		 						    		"m.municipalBondStatus.id = :statusId"),
		 						    		
		 				@NamedQuery(name="Payment.findOnlyByCashierAndDate", 
				 				    query="select distinct(payment) from Payment payment " +
				 				    		"join payment.deposits d " +
				 				    		"join payment.paymentFractions pf " +
				 				    		"join d.municipalBond m " +
				 				    		"join m.resident r " +
				 				    		"join m.entry e " +
				 				    		"left join e.account ac " +
				 				    		"where payment.cashier.id = :cashierId and " +
				 				    		"payment.date = :date AND " +
				 				    		"payment.status = 'VALID' "),
				 						    		
		 				@NamedQuery(name="Payment.findByBranchAndDate", 
				 				    query="select distinct payment from Payment payment join payment.deposits d join d.municipalBond m join m.entry e left join e.account ac where payment.cashier.id in (select t.person.id from TillPermission t where t.workday.date = :date and t.till.branch.id = :branchId) and payment.date = :date AND m.municipalBondStatus.id = :statusId ORDER BY payment.id"),
		 			   @NamedQuery(name="Payment.findBetweenDates", 
				 				    query="select distinct payment from Payment payment " +
				 				    	"join payment.paymentFractions pf " +
				 				    	"join payment.deposits d " +
				 				   		"join d.municipalBond m " +
				 				   		"join m.entry e " +
				 				   		"join e.account ac " +
				 				   		"where payment.date Between :startDate and :endDate " +
				 				   		"AND payment.status = 'VALID' " +
				 				   		"AND m.municipalBondStatus.id = :statusId " +
				 				   		"AND m is not null"),
				 				   		
				 	@NamedQuery(name="Payment.findMunicipalBondBetweenDatesByPaymentAndCashier",
				 			    query="select m from Payment payment " +
				 			    	"join payment.deposits d " +
				 			   		"join d.municipalBond m " +
				 			   		"join m.resident r " +
				 			   		"join m.entry e " +
				 			   		"join e.account ac " +
				 			   		"where payment.date Between :startDate and :endDate " +
				 			   		"AND payment.status = 'VALID' " +
				 			   		"AND payment.cashier.id = :cashierId " +
				 			   		"AND m.municipalBondStatus.id = :statusId " +
				 			   		"AND m.paymentAgreement is null ORDER BY e.code, r.name"),
				 				   		
				 	@NamedQuery(name="Payment.findMunicipalBondBetweenDatesByPaidStatusesAndCashier",
				 			    query="select m from Payment payment " +
				 			    	"join payment.deposits d " +
				 			   		"join d.municipalBond m " +
				 			   		"join m.resident r " +
				 			   		"join m.entry e " +
				 			   		"join e.account ac " +
				 			   		"where payment.date Between :startDate and :endDate " +
				 			   		"AND payment.status = 'VALID' " +
				 			   		"AND payment.cashier.id = :cashierId " +
				 			   		"AND m.municipalBondStatus.id in (:paidStatuses) " +
				 			   		"AND m.paymentAgreement is null ORDER BY e.code, r.name"),
					 				   		
				 	@NamedQuery(name="Payment.BetweenDates", 
				 			    query="select distinct payment from Payment payment " +				 			    	
				 			   		"where payment.date Between :startDate and :endDate " +
				 			   		"AND payment.status = 'VALID' " ),
				 	
				 	@NamedQuery(name="Payment.SumTotalBetweenDatesByCashier", 
				 			    query="select sum(payment.value) from Payment payment " +				 			    	
				 			   		"where payment.date Between :startDate and :endDate " +
				 			    	"AND payment.cashier.id = :cashierId " +
				 			   		"AND payment.status = 'VALID' " ),
				 			   		
				 	
				 	@NamedQuery(name="Payment.findNumberBetweenDates",
				 			    query="select distinct(payment.id) from Payment payment " +			 			   		
				 			   		"join payment.deposits d " +
				 			   		"join d.municipalBond m " +
				 			   		"where payment.date Between :startDate and :endDate " +		 			   		
		 				   			"AND m is not null AND d is not null"),
		 				   			
		 			@NamedQuery(name="Payment.countBetweenDatesByCashier",
				 			    query="select count(payment.id) from Payment payment " +			 			   		
				 			   		"where payment.date Between :startDate and :endDate " +		 			   		
				 			   		"AND payment.status = 'VALID' " +
		 				   			"AND payment.cashier.id = :cashierId"),
		 				   			
		 			@NamedQuery(name="Payment.countDepositsNumberByCashier",			 			
			 				    query="select count(d.id) from Payment p " +
			 				    		"left join p.deposits d " +
			 				    		"where p.date Between :startDate and :endDate " +
			 				    		"and p.cashier.id = :cashierId " +
			 				    		"and p.status = 'VALID'"),
				 			   		
				 	@NamedQuery(name="Payment.depositsNumber",			 			
			 				    query="select count(d.id) from Payment p " +
			 				    		"left join p.deposits d " +
			 				    		"where p.date Between :startDate and :endDate " +			 				    		
			 				    		"and p.status = 'VALID'"),
				 			   		
					@NamedQuery(name="Payment.SumPaidTotalsInPaymentAgreementByEntry", 
							    query="select e.id, e.name, e.code, ac.accountCode, ac.budgetCertificateCode, SUM(d.value), " +
							    	  " SUM(d.interest), SUM(d.paidTaxes), SUM (d.value) from Payment payment join payment.deposits d join d.municipalBond m join m.entry e left join e.account ac where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId" +
					 " and payment.cashier.id in (select t.person.id from Branch branch join branch.tills t where branch.isPaidByCompensation = :isPaidByCompensation )  GROUP BY e.id, e.name, e.code, ac.accountCode, ac.budgetCertificateCode ORDER BY ac.budgetCertificateCode"),
							    	  
						@NamedQuery(name="Payment.SumPaymentAgreementTotalsByEntryRestrictByPaidCompensation", 
								    query="select e.id, e.name, e.code, ac.accountCode, ac.budgetCertificateCode, SUM(m.value), SUM(m.discount), SUM(m.surcharge)," +
								    	  " SUM(m.interest), SUM(m.taxesTotal), SUM (m.paidTotal) from Payment payment join payment.deposits d join d.municipalBond m join m.entry e left join e.account ac where payment.date Between :startDate and :endDate AND m.municipalBondStatus.id = :municipalBondStatusId" +
								    	  " and payment.cashier.id in (select t.person.id from Branch branch join branch.tills t where branch.isPaidByCompensation = :isPaidCompensation )  GROUP BY e.id, e.name, e.code, ac.accountCode, ac.budgetCertificateCode ORDER BY ac.budgetCertificateCode"),
							    	  
					   @NamedQuery(name="Payment.SumTotalsWithGroupByBetweenDates", 
								    query="select e.name, e.code, ac.accountCode, ac.budgetCertificateCode, SUM(d.value), SUM(m.discount), SUM(m.surcharge)," +
								    	  " SUM(d.interest), SUM(m.taxesTotal), SUM (m.paidTotal) from Payment payment join payment.deposits d join d.municipalBond m join m.entry e left join e.account ac where payment.date Between :startDate and :endDate" +
								    	  " GROUP BY e.name, e.code, ac.accountCode, ac.budgetCertificateCode ORDER BY e.name")})
public class Payment {
	
	@Id
	@GeneratedValue(generator="PaymentGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private BigDecimal value;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.TIME)
	private Date time;
	
	@Enumerated(EnumType.STRING)
	@Column(length=10)
	private FinancialStatus status; 
	
	@OneToMany(mappedBy = "payment")
	@OrderBy(value="number")
	private List<Deposit> deposits;	
	
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="cashier_id")
	private Person cashier;
	
	private BigDecimal reversableAmount;
	
	private String externalTransactionId;
	
	@OneToMany(mappedBy = "payment", cascade={CascadeType.MERGE, CascadeType.PERSIST})
	private List<PaymentFraction> paymentFractions;
	
	public Payment(){
		status = FinancialStatus.VALID;
		value = BigDecimal.ZERO;
		deposits = new ArrayList<Deposit>();
		paymentFractions = new java.util.LinkedList<PaymentFraction>();
		PaymentFraction paymentFraction = new PaymentFraction();
		this.add(paymentFraction);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}
	
	public void add(Deposit deposit) {
		if (!this.deposits.contains(deposit)) {
			this.deposits.add(deposit);
			deposit.setPayment(this);
		}
	}
	
	public void remove(Deposit deposit){
		boolean removed = this.deposits.remove(deposit);
		if (removed) {
			deposit.setPayment(null);
		}
	}
	
	public Person getCashier() {
		return cashier;
	}

	public void setCashier(Person cashier) {
		this.cashier = cashier;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	public void setStatus(FinancialStatus status) {
		this.status = status;
	}

	public FinancialStatus getStatus() {
		return status;
	}

	public List<PaymentFraction> getPaymentFractions() {
		return paymentFractions;
	}

	public void setPaymentFractions(List<PaymentFraction> paymentFractions) {
		this.paymentFractions = paymentFractions;
	}
	
	public String getExternalTransactionId() {
		return externalTransactionId;
	}

	public void setExternalTransactionId(String externalTransactionId) {
		this.externalTransactionId = externalTransactionId;
	}

	public void add(PaymentFraction paymentFraction) {
		if (!this.paymentFractions.contains(paymentFraction)) {
			this.paymentFractions.add(paymentFraction);
			paymentFraction.setPayment(this);
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
		// TODO Auto-generated method stub
		return this.id.toString();
	}
	
	public BigDecimal getReversableAmount(){
		if(reversableAmount == null){
			BigDecimal reversable = BigDecimal.ZERO;
			for(PaymentFraction fraction : paymentFractions){
				if(fraction.getPaymentType() == PaymentType.CASH || fraction.getPaymentType() == PaymentType.CREDIT_NOTE){
					reversable.add(fraction.getPaidAmount());
				}
			}
			reversableAmount = reversable;
		}
		return reversableAmount;
	}
}//end Payment