package ec.gob.gim.cadaster.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import ec.gob.gim.common.model.Resident;


/**
 * @author Rene Ortega
 * @version 1.0
 * @created 2-Dic-2015
 * Dominio Sinat Migracion
 */
@Entity
@TableGenerator(
	 name="DomainSinatGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="DomainSinat",
	 initialValue=1, allocationSize=1
)
public class DomainSinat implements Serializable{

	@Id
	@GeneratedValue(generator="DomainSinatGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	private BigDecimal appraisedValue;//Avaluo
	private BigDecimal appraisalBuilding; //Avaluo construccion
	private BigDecimal appraisalImprovements;//Avaluo mejoras
	private BigDecimal appraisalLand;//Avaluo suelo Bruto
	private BigDecimal appraisalTotal;//Avaluo Total
	private Boolean active;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Resident resident;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private PropertySinat property;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getAppraisedValue() {
		return appraisedValue;
	}
	public void setAppraisedValue(BigDecimal appraisedValue) {
		this.appraisedValue = appraisedValue;
	}
	public BigDecimal getAppraisalBuilding() {
		return appraisalBuilding;
	}
	public void setAppraisalBuilding(BigDecimal appraisalBuilding) {
		this.appraisalBuilding = appraisalBuilding;
	}
	public BigDecimal getAppraisalImprovements() {
		return appraisalImprovements;
	}
	public void setAppraisalImprovements(BigDecimal appraisalImprovements) {
		this.appraisalImprovements = appraisalImprovements;
	}
	
	public BigDecimal getAppraisalLand() {
		return appraisalLand;
	}
	public void setAppraisalLand(BigDecimal appraisalLand) {
		this.appraisalLand = appraisalLand;
	}
	public BigDecimal getAppraisalTotal() {
		return appraisalTotal;
	}
	public void setAppraisalTotal(BigDecimal appraisalTotal) {
		this.appraisalTotal = appraisalTotal;
	}
	public Resident getResident() {
		return resident;
	}
	public void setResident(Resident resident) {
		this.resident = resident;
	}
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public PropertySinat getProperty() {
		return property;
	}
	public void setProperty(PropertySinat property) {
		this.property = property;
	}
}