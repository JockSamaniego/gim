package ec.gob.gim.income.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.hibernate.envers.Audited;

import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author gerson
 * @version 1.0
 * @created 04-Ago-2011 16:30:47
 */
@Audited
@Entity
@TableGenerator(
	 name="TaxItemGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="TaxItem",
	 initialValue=1, allocationSize=1
 )
 public class TaxItem implements Comparable<TaxItem>{

	@Id
	@GeneratedValue(generator="TaxItemGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private java.math.BigDecimal appliedRate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private TaxRate appliedTaxRate;
	
	private java.math.BigDecimal value;
		
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tax_id")
	private Tax tax;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private MunicipalBond municipalBond;

	public MunicipalBond getMunicipalBond() {
		return municipalBond;
	}

	public void setMunicipalBond(MunicipalBond municipalBond) {
		this.municipalBond = municipalBond;
	}

	public TaxItem(){
		appliedRate = java.math.BigDecimal.ZERO;  
	}

	public java.math.BigDecimal getAppliedRate() {
		return appliedRate;
	}

	public void setAppliedRate(java.math.BigDecimal appliedRate) {
		this.appliedRate = appliedRate;
	}

	public TaxRate getAppliedTaxRate() {
		return appliedTaxRate;
	}

	public void setAppliedTaxRate(TaxRate appliedTaxRate) {
		this.appliedTaxRate = appliedTaxRate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public java.math.BigDecimal getValue() {
		return value;
	}

	public void setValue(java.math.BigDecimal value) {
		this.value = value;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}
	
	@Override
	public int compareTo(TaxItem o) {
		if(o != null){ 
			return this.hashCode() - o.hashCode();
		}
		return -1;
	}	
	
	public String taxRate(){
		BigDecimal rate = appliedRate.multiply(new BigDecimal(100));
		return rate.toString();
	}
	
}//end TaxItem