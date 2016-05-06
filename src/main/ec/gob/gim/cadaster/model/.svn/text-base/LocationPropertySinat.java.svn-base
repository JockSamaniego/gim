package ec.gob.gim.cadaster.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.TableGenerator;


@Entity
@TableGenerator(
	 name="LocationPropertySinatGenerator",
	 table="IdentityGenerator",
	 pkColumnName="name",
	 valueColumnName="value",
	 pkColumnValue="LocationPropertySinat",
	 initialValue=1, allocationSize=1
 )

@NamedQueries(value = { 
//macartuche
//2015-12-18 16:41
@NamedQuery(name = "LocationPropertySinat.findSinat", query = "Select lps.property from LocationPropertySinat lps "
		+ "left join lps.property property "
		+ "left join property.currentDomain currentDomain "				
		+ "left join currentDomain.notarysProvince "		
		+ "left join currentDomain.notarysCity "
		+ "left join currentDomain.purchaseType purchaseType "
		+ "left join property.streetMaterial sm "		
		+ "left join property.location l "
		+ "left join l.neighborhood nb "
		+ "left join nb.place place "
		+ "left JOIN l.mainBlockLimit bl " 
		+ "left join bl.sidewalkMaterial swm "
		+ "left join bl.street street "
		+ "left join bl.streetMaterial streetMaterial "
		+ "left join bl.streetType streetType "		
		+ "left join property.lotPosition lp "
		+ "left join property.fenceMaterial fm "
		+ "left join property.block block "
		+ "left join block.sector sector "
		+ "left join sector.territorialDivisionType "
		+ "left join property.propertyType pt "
		+ "left join pt.entry entry "
		+ "left join currentDomain.resident resident "		
		+ "left join resident.currentAddress address "		
		+ "where lps.sectorName in :list "
		+ "and property.deleted = false "
		+ "and lps.property.id not in :noEmitFor "
		+ "and lps.parishName=:parishName "
		+ "ORDER BY property.cadastralCode")
})
public class LocationPropertySinat {
	@Id
	@GeneratedValue(generator="LocationPropertySinatGenerator",strategy=GenerationType.TABLE)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "property_id")
	private Property property;
	private String parishName;
	private String parishCode;
	private String sectorName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Property getProperty() {
		return property;
	}
	public void setProperty(Property property) {
		this.property = property;
	}
	public String getParishName() {
		return parishName;
	}
	public void setParishName(String parishName) {
		this.parishName = parishName;
	}
	public String getParishCode() {
		return parishCode;
	}
	public void setParishCode(String parishCode) {
		this.parishCode = parishCode;
	}
	public String getSectorName() {
		return sectorName;
	}
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	
}