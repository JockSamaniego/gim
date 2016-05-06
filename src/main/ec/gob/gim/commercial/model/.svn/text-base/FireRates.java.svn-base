package ec.gob.gim.commercial.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

/**
 * @author jock samaniego
 */

@Audited
@Entity
@TableGenerator(name = "FireRatesGenerator", table = "IdentityGenerator", pkColumnName = "name", valueColumnName = "value", pkColumnValue = "FireRates", initialValue = 1, allocationSize = 1)
@NamedQueries(value = {
		@NamedQuery(name = "FireRates.findForTypes", query = "select fr from FireRates fr where "
				+ "fr.name = :name")})
public class FireRates {

	@Id
	@GeneratedValue(generator = "FireRatesGenerator", strategy = GenerationType.TABLE)
	private Integer id;
	
	@JoinColumn(name="name")
	@Column(length = 20)
	private String name;
	
	@JoinColumn(name="code")
	@Column(length = 20)
	private String code;
	
	@JoinColumn(name="activity")
	@Column(length = 80)
	private String activity;
	
	@Column(nullable = false, columnDefinition = "DECIMAL(7,2)")
	    private BigDecimal value;

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}


	
}