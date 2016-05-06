package ec.gob.gim.income.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

@Audited
@Entity
@TableGenerator(
		name = "MoneyGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "Money", 
		initialValue = 1, allocationSize = 1
)
@NamedQueries(value = {
		@NamedQuery(name="Money.findByMoneyTypeAndValue", 
				query="select money from Money money " +
						"where "+
						"money.moneyType = :moneyType AND money.value=:value)"),
		@NamedQuery(name="Money.findbyMoneyTypeOrderByValue", 
				query="select money from Money money " +
						"where "+
						"money.moneyType = :moneyType ORDER BY money.value DESC)")
})
public class Money {
	@Id
	@GeneratedValue(generator = "MoneyGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=4)
	private MoneyType moneyType;
	
	@Column(length=10)
	private String denomination;
	
	private BigDecimal value;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public MoneyType getMoneyType() {
		return moneyType;
	}
	public void setMoneyType(MoneyType moneyType) {
		this.moneyType = moneyType;
	}
	public String getDenomination() {
		return denomination;
	}
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
}
