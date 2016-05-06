package ec.gob.gim.revenue.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;


/**
 * @author wilman
 * @version 1.0
 * @created 18-Oct-2011 16:30:30
 */
@Audited
@Entity
@TableGenerator(
		name = "TimePeriodGenerator", 
		table = "IdentityGenerator", 
		pkColumnName = "name", 
		valueColumnName = "value", 
		pkColumnValue = "TimePeriod", 
		initialValue = 1, allocationSize = 1
)
@NamedQueries({
	@NamedQuery(name="TimePeriod.findAll",
			query="select tp from TimePeriod tp " +
				"order by tp.daysNumber"
	),
	@NamedQuery(name="TimePeriod.findByName",
			query="select tp from TimePeriod tp where " +
				"lower(tp.name) like lower(concat(:name, '%')) order by tpname"
	)
})


public class TimePeriod {
	
	@Id
	@GeneratedValue(generator = "TimePeriodGenerator", strategy = GenerationType.TABLE)
	private Long id;
	
	@Column(length=30, nullable=false)
	private String name;
	
	private Integer daysNumber;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name.toUpperCase();
	}

	/**
	 * @return the daysNumber
	 */
	public Integer getDaysNumber() {
		return daysNumber;
	}

	/**
	 * @param daysNumber the daysNumber to set
	 */
	public void setDaysNumber(Integer daysNumber) {
		this.daysNumber = daysNumber;
	}

}
