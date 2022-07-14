package ec.gob.gim.coercive.model.infractions;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;


/**
 * Abono
 * @author Ronald Paladines
 * @created 14-Abr-2022
 */
@Audited
@Entity
@TableGenerator(
	 name="DividendInfractionGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="DividendInfraction",
	 initialValue=1, allocationSize=1
)
@Table(name = "dividendInfraction", schema = "infracciones")

public class DividendInfraction {

	@Id
	@GeneratedValue(generator="DividendInfractionGenerator",strategy=GenerationType.TABLE)
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
	
}//end DividendInfraction