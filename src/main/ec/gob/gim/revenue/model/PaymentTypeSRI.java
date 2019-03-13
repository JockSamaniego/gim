package ec.gob.gim.revenue.model;

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

import ec.gob.gim.income.model.PaymentType;

@Audited
@Entity
@TableGenerator(
		name = "PaymentTypeSRIGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "PaymentTypeSRI", 
		initialValue = 1, allocationSize = 1
)
@NamedQueries(value = {
		@NamedQuery(name = "PaymentTypeSRI.findByType", query = "select f from PaymentTypeSRI f where f.paymentType = :type"),
		@NamedQuery(name = "PaymentTypeSRI.findByCode", query = "select f from PaymentTypeSRI f where f.code = :code")})
public class PaymentTypeSRI {
	@Id
	@GeneratedValue(generator = "PaymentTypeSRIGenerator", strategy = GenerationType.TABLE)
	private Long id;	
	
	private String code;
	
	private String name;
		
	@Enumerated(EnumType.STRING)
	@Column(length=15)
	private PaymentType paymentType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}
}
