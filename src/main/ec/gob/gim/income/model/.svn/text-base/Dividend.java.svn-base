package ec.gob.gim.income.model;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;


/**
 * Abono
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:31
 */
@Audited
@Entity
@TableGenerator(
	 name="DividendGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Dividend",
	 initialValue=1, allocationSize=1
)
public class Dividend {

	@Id
	@GeneratedValue(generator="DividendGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private java.util.Date date;
	
	private BigDecimal amount;
	
	private BigDecimal balance;

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

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
}//end Dividend