package ec.gob.gim.revenue.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


/**
 * @author gerson
 * @version 1.0
 * @created 18-Oct-2011 16:30:30
 */
@Entity
@NamedQueries({
	@NamedQuery(name="CurrencyDevaluation.findByYear",
			query="select cd from CurrencyDevaluation cd where cd.year = :year"
	)
})
public class CurrencyDevaluation {	
	@Id
	private Integer year;

	@Column(precision = 12, scale = 3)
	private BigDecimal value;

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	

}
