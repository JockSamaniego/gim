package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:42
 */
@Audited
@Entity
@TableGenerator(
	 name="TariffGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="Tariff",
	 initialValue=1, allocationSize=1
)
public class Tariff {

	@Id
	@GeneratedValue(generator="TariffGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	private BigDecimal base;
	private BigDecimal finalRange;
	private BigDecimal initialRange;
	
	private Boolean isActive;
	private BigDecimal valueByMeter;

	public Tariff(){

	}

	public BigDecimal getBase() {
		return base;
	}

	public void setBase(BigDecimal base) {
		this.base = base;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getFinalRange() {
		return finalRange;
	}

	public void setFinalRange(BigDecimal finalRange) {
		this.finalRange = finalRange;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getInitialRange() {
		return initialRange;
	}

	public void setInitialRange(BigDecimal initialRange) {
		this.initialRange = initialRange;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public BigDecimal getValueByMeter() {
		return valueByMeter;
	}

	public void setValueByMeter(BigDecimal valueByMeter) {
		this.valueByMeter = valueByMeter;
	}
}//end Rate