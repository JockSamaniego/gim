package ec.gob.gim.income.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@TableGenerator(
	 name="EMoneyPaymentGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="EMoneyPaymentPayment",
	 initialValue=1, allocationSize=1
)
@NamedQueries({
	@NamedQuery(name="EMoneyPayment.findPaymentByDebtIdAmountAndDate",
				query=  "SELECT e FROM EMoneyPayment e "
						+ "WHERE e.debtId = :debtId "
						+ "AND e.amount =:amount "
						+ " AND e.date = :date "
						+ " AND e.isPaid = true "
						+ " AND e.account = :account "
						+ " ORDER BY e.id desc")
})
public class EMoneyPayment {

	@Id
	@GeneratedValue(generator="EMoneyPaymentGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private String debtId;
	

	@Temporal(TemporalType.DATE)
	private java.util.Date date;
	
	@Temporal(TemporalType.TIME)
	private java.util.Date time;
	
	private BigDecimal amount;
	
	private String municipalBonds_id;
	
	private Boolean isPaid;
	
	private Boolean isReverse = Boolean.FALSE;

	private String account; 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDebtId() {
		return debtId;
	}

	public void setDebtId(String debtId) {
		this.debtId = debtId;
	}

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public java.util.Date getTime() {
		return time;
	}

	public void setTime(java.util.Date time) {
		this.time = time;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getMunicipalBonds_id() {
		return municipalBonds_id;
	}

	public void setMunicipalBonds_id(String municipalBonds_id) {
		this.municipalBonds_id = municipalBonds_id;
	}

	public Boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(Boolean isPaid) {
		this.isPaid = isPaid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Boolean getIsReverse() {
		return isReverse;
	}

	public void setIsReverse(Boolean isReverse) {
		this.isReverse = isReverse;
	}  
	
}