package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.FinancialInstitution;
import ec.gob.gim.revenue.model.PaymentTypeSRI;

@Audited
@Entity
@TableGenerator(
	 name="PaymentFractionGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="PaymentFraction",
	 initialValue=1, allocationSize=1
)
@NamedQueries(value = {
		
		@NamedQuery( name="PaymentFraction.findByPaymentId",
			query="SELECT pf FROM PaymentFraction pf WHERE pf.payment.id = :paymentId"),
			
		@NamedQuery(name="PaymentFraction.SumTotalBetweenDatesByCashierAndValids", 
		    query="select pf.paymentType, sum(pf.paidAmount) from PaymentFraction pf " +				 			    	
			   		"where pf.payment.date Between :startDate and :endDate " +			   		
			    	"AND pf.payment.cashier.id = :cashierId " +
			   		"AND pf.payment.status = 'VALID'" +
			   		"GROUP BY pf.paymentType ORDER BY pf.paymentType" ),
		   		
		@NamedQuery(name="PaymentFraction.findForViewByDateAndPaymentTypeByCashier",			 			
		    query="select distinct NEW ec.gob.gim.income.model.PaymentFractionView(pf.id, r.name, r.identificationNumber,pf.accountNumber, pf.documentNumber, pf.paidAmount, pf.paymentType, fi.name, p.date, p.time) from Payment p " +
		    		"left join p.paymentFractions pf " +
		    		"left join pf.finantialInstitution fi " +
		    		"left join p.deposits d " +
		    		"left join d.municipalBond mb " +
		    		"left join mb.resident r " +
		    		"where p.date Between :startDate and :endDate " +
 			    	"AND p.cashier.id = :cashierId " +
 			    	"AND pf.paymentType = :paymentType " +
 			   		"AND p.status = 'VALID' ORDER BY r.name")})

public class PaymentFraction {
	@Id
	@GeneratedValue(generator="PaymentFractionGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private String accountNumber;
	
	// Puede ser numero de nota de deposito, numero de cheque, etc.
	private String documentNumber;
	
	private BigDecimal receivedAmount;
	
	private BigDecimal paidAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private PaymentType paymentType;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="finantialInstitution_id")
	private FinancialInstitution finantialInstitution;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Payment payment;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	private CreditNote creditNote;
	
	
	@Transient
	private List<Deposit> deposits;	
	
	
	//macartuche
	//2018-11-05 15:18 para codigo de pago SRI
	//@OneToOne(mappedBy = "fraction", fetch = FetchType.LAZY)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="paymentTypesri_id")
	private PaymentTypeSRI paymentTypesri;
	
	public PaymentFraction() {
		deposits = new ArrayList<Deposit>();
		receivedAmount = BigDecimal.ZERO;
		paidAmount = BigDecimal.ZERO;
		paymentType = PaymentType.CASH;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	public BigDecimal getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(BigDecimal receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public FinancialInstitution getFinantialInstitution() {
		return finantialInstitution;
	}

	public void setFinantialInstitution(FinancialInstitution finantialInstitution) {
		this.finantialInstitution = finantialInstitution;
	}

	
	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
	
	public CreditNote getCreditNote() {
		return creditNote;
	}

	public void setCreditNote(CreditNote creditNote) {
		this.creditNote = creditNote;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	
	public List<Deposit> getDeposits() {
		return deposits;
	}

	public void setDeposits(List<Deposit> deposits) {
		this.deposits = deposits;
	}
	public void add(Deposit d){
		if(d != null && !deposits.contains(d)){
			deposits.add(d);
		}
	}
	
	public void remove(Deposit d){
		if(d != null && deposits.contains(d)){
			deposits.remove(d);
		}
	}
	
	public PaymentTypeSRI getPaymentTypesri() {
		return paymentTypesri;
	}

	public void setPaymentTypesri(PaymentTypeSRI paymentTypesri) {
		this.paymentTypesri = paymentTypesri;
	}


}
