package ec.gob.gim.appraisal.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
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
		 name="AppraisalRossHeideckeGenerator",
		 table="IdentityGenerator",
		 pkColumnName="name",
		 valueColumnName="value",
		 pkColumnValue="AppraisalRossHeidecke",
		 initialValue=1, allocationSize=1
	 )
	 
 @NamedQueries(value = { @NamedQuery(name = "AppraisalRossHeidecke.findAll", 
		query = "SELECT appraisalRossHeidecke FROM AppraisalRossHeidecke appraisalRossHeidecke " +
				"order by appraisalRossHeidecke.year") })

public class AppraisalRossHeidecke {
	@Id
	@GeneratedValue(generator="AppraisalRossHeideckeGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private int year;
	
	private BigDecimal goodState;
	private BigDecimal regularState;
	private BigDecimal badState;
	
	public AppraisalRossHeidecke() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public BigDecimal getGoodState() {
		return goodState;
	}

	public void setGoodState(BigDecimal goodState) {
		this.goodState = goodState;
	}

	public BigDecimal getRegularState() {
		return regularState;
	}

	public void setRegularState(BigDecimal regularState) {
		this.regularState = regularState;
	}

	public BigDecimal getBadState() {
		return badState;
	}

	public void setBadState(BigDecimal badState) {
		this.badState = badState;
	}
	
}
