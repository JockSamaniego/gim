package ec.gob.gim.income.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(
		name = "CashRecordGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "CashRecord", 
		initialValue = 1, allocationSize = 1
)
public class CashRecord {
	@Id
	@GeneratedValue(generator = "CashRecordGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Money money;
	
	private Long amount;
	
	@Transient
	private BigDecimal total;
	
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Money getMoney() {
		return money;
	}
	public void setMoney(Money money) {
		this.money = money;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	} 
	
	public void calculateTotal(){
		if(amount == null) amount = 0L;
		if(money == null){
			setTotal(BigDecimal.ZERO);
		}else{
			setTotal(money.getValue().multiply(new BigDecimal(amount)));
		}
		
	}
}
