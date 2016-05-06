package ec.gob.gim.cadaster.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

/**
 * @author Rene Ortega
 * @version 1.0
 * @created 2-Dic-2015
 * Propiedad Sinat migracion
 */

@Entity
@TableGenerator(
	 name="PropertySinatGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="PropertySinat",
	 initialValue=1, allocationSize=1
)
public class PropertySinat implements Serializable{
	
	@Id
	@GeneratedValue(generator="PropertySinatGenerator",strategy=GenerationType.TABLE)
	private Long id;
	private Long propertyIdSinat;
	private String cadastralCode;
	private String cadastralCodeGim;
	private String name;
	private BigDecimal landArea;
	private BigDecimal buildingArea;
	private String sectorName;
	private String parishName;
	private String cadastralCodeParish;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPropertyIdSinat() {
		return propertyIdSinat;
	}
	public void setPropertyIdSinat(Long propertyIdSinat) {
		this.propertyIdSinat = propertyIdSinat;
	}
	public String getCadastralCode() {
		return cadastralCode;
	}
	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}
	public String getCadastralCodeGim() {
		return cadastralCodeGim;
	}
	public void setCadastralCodeGim(String cadastralCodeGim) {
		this.cadastralCodeGim = cadastralCodeGim;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getLandArea() {
		return landArea;
	}
	public void setLandArea(BigDecimal landArea) {
		this.landArea = landArea;
	}
	public BigDecimal getBuildingArea() {
		return buildingArea;
	}
	public void setBuildingArea(BigDecimal buildingArea) {
		this.buildingArea = buildingArea;
	}
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	public String getParishName() {
		return parishName;
	}
	public void setParishName(String parishName) {
		this.parishName = parishName;
	}
	public String getCadastralCodeParish() {
		return cadastralCodeParish;
	}
	public void setCadastralCodeParish(String cadastralCodeParish) {
		this.cadastralCodeParish = cadastralCodeParish;
	}
}