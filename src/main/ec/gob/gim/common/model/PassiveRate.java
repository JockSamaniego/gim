package ec.gob.gim.common.model;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import ec.gob.gim.security.model.User;

/**
 * @author macartuche
 * @version 1.0
 * @created 2013-05-05
 */
@Audited
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@TableGenerator(
	 name="PassiveRateGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="PassiveRate",
	 initialValue=1, allocationSize=1
)

@NamedQueries(value = {
		@NamedQuery(name="PassiveRate.findAVG", 
			query = "select pr from PassiveRate pr where pr.startDate=:currentDate")
		}
	)
 public class PassiveRate {

	@Id
	@GeneratedValue(generator="PassiveRateGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private BigDecimal rate;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	public PassiveRate(){
		
	}

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

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}