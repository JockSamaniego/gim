package ec.gob.gim.revenue.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

@Entity
@TableGenerator(
		name = "PaymentTypeSRIGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "PaymentTypeSRI", 
		initialValue = 1, allocationSize = 1
)

public class PaymentTypeSRI {
	@Id
	@GeneratedValue(generator = "PaymentTypeSRIGenerator", strategy = GenerationType.TABLE)
	private Long id;	
	
	private String code;
	
	private String name;
		
	/*@OneToOne(fetch=FetchType.LAZY)
	private PaymentFraction fraction;*/

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

	/*public PaymentFraction getFraction() {
		return fraction;
	}

	public void setFraction(PaymentFraction fraction) {
		this.fraction = fraction;
	}*/
}
