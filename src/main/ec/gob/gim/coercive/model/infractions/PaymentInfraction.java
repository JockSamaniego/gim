/**
 * 
 */
package ec.gob.gim.coercive.model.infractions;

import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.income.model.PaymentType;
import ec.gob.gim.revenue.model.FinancialInstitution;
import ec.gob.gim.security.model.User;

/**
 * @author René
 * @date 2022-06-20
 */
@Audited
@Entity
@TableGenerator(name = "PaymentInfractionGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "PaymentInfraction", initialValue = 1, allocationSize = 1)
@Table(name = "paymentInfraction", schema = "infracciones")
public class PaymentInfraction {
	@Id
	@GeneratedValue(generator = "PaymentInfractionGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	private BigDecimal value;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	@Temporal(TemporalType.TIME)
	private Date time;
	
	@ManyToOne
	@JoinColumn(name = "paymenttype_id")
	private ItemCatalog paymentType;
	
	@ManyToOne (fetch=FetchType.LAZY)
	@JoinColumn(name="cashier_id")
	private User cashier;
	
	@ManyToOne
	@JoinColumn(name="status_id")
	private ItemCatalog status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "infraction_id")
	private Datainfraction infraction;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "finantialInstitution_id")
	private FinancialInstitution finantialInstitution;
	
	private String accountNumber;

	// Puede ser numero de nota de deposito, numero de cheque, etc.
	private String documentNumber;
	
	@Version
	private Long version = 0L;
	
	/**
	 * 
	 */
	public PaymentInfraction() {
		super();
		this.date = new Date();
		this.time = new Date();
		this.value = BigDecimal.ZERO;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the paymentType
	 */
	public ItemCatalog getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(ItemCatalog paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the cashier
	 */
	public User getCashier() {
		return cashier;
	}

	/**
	 * @param cashier the cashier to set
	 */
	public void setCashier(User cashier) {
		this.cashier = cashier;
	}

	/**
	 * @return the status
	 */
	public ItemCatalog getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ItemCatalog status) {
		this.status = status;
	}

	/**
	 * @return the infraction
	 */
	public Datainfraction getInfraction() {
		return infraction;
	}

	/**
	 * @param infraction the infraction to set
	 */
	public void setInfraction(Datainfraction infraction) {
		this.infraction = infraction;
	}

	/**
	 * @return the finantialInstitution
	 */
	public FinancialInstitution getFinantialInstitution() {
		return finantialInstitution;
	}

	/**
	 * @param finantialInstitution the finantialInstitution to set
	 */
	public void setFinantialInstitution(FinancialInstitution finantialInstitution) {
		this.finantialInstitution = finantialInstitution;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the documentNumber
	 */
	public String getDocumentNumber() {
		return documentNumber;
	}

	/**
	 * @param documentNumber the documentNumber to set
	 */
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PaymentNotification [id=" + id + ", value=" + value + ", date="
				+ date + ", time=" + time + ", paymentType=" + paymentType
				+ ", cashier=" + cashier + ", status=" + status
				+ ", infraction=" + infraction + ", finantialInstitution="
				+ finantialInstitution + ", accountNumber=" + accountNumber
				+ ", documentNumber=" + documentNumber + "]";
	}
	
}
