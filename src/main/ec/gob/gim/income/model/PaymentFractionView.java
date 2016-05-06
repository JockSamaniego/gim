package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.Date;


public class PaymentFractionView {
	
	private String resident;
	private String identificationNumber;
	private String accountNumber;	
	private String documentNumber;	
	private BigDecimal paidAmount;
	private PaymentType paymentType;	
	private String finantialInstitution;	
	private Date time;
	private Date date;
	private Long id;
		
	
	public PaymentFractionView(Long id,String resident, String identificationNumber, String accountNumber, String documentNumber, 
							   BigDecimal paidAmount, PaymentType paymentType, String finantialInstitution, Date date, Date time) {
		this.id = id;
		this.resident = resident ;
		this.identificationNumber = identificationNumber;
		this.accountNumber = accountNumber;	
		this.documentNumber = documentNumber;	
		this.paidAmount = paidAmount;
		this.paymentType = paymentType;	
		this.finantialInstitution = finantialInstitution;
		this.date = date;
		this.time = time;
	}
	
	public PaymentFractionView() {
		
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

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getFinantialInstitution() {
		return finantialInstitution;
	}

	public void setFinantialInstitution(String finantialInstitution) {
		this.finantialInstitution = finantialInstitution;
	}

	public String getResident() {
		return resident;
	}

	public void setResident(String resident) {
		this.resident = resident;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof PaymentFractionView)) {
			return false;
		}
		PaymentFractionView other = (PaymentFractionView) object;
					
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

}
