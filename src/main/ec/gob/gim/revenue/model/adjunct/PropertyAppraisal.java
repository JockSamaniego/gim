package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.envers.Audited;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.revenue.model.Adjunct;

@Audited
@Entity
@NamedQueries(value = {
		@NamedQuery(name = "PropertyAppraisal.findById", 
			query = "select propertyAppraisal from PropertyAppraisal propertyAppraisal where propertyAppraisal.id=:idPropertyAppraisal ")
	})
@DiscriminatorValue("PRA")
public class PropertyAppraisal extends Adjunct{
	private Boolean emitWithoutProperty;
	private String cadastralCode;
	private String previousCadastralCode;
	private BigDecimal lotAppraisal;
	private BigDecimal buildingAppraisal;
	private BigDecimal commercialAppraisal;
	private BigDecimal exemptionValue;
	private Boolean changeAppraisals;
	private BigDecimal realLotAppraisal;
	private BigDecimal realBuildingAppraisal;
	
	//rfam 2017-12-15 aprobacion de ordenanza
	private BigDecimal lotArea;
	private BigDecimal constructionArea;
	
	//rfam 2018-12-56 avaluo de mejoras para sinat
	private BigDecimal improvementAppraisal;
	
	//private String territorialCode;

	@ManyToOne
	private Property property;
	
	public PropertyAppraisal() {
		emitWithoutProperty = Boolean.FALSE;
		changeAppraisals = Boolean.FALSE;
		exemptionValue = BigDecimal.ZERO;
	}

			
	public void setProperty(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}

	public String getCadastralCode() {
		return cadastralCode;
	}

	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}

	public String getPreviousCadastralCode() {
		return previousCadastralCode;
	}

	public void setPreviousCadastralCode(String previousCadastralCode) {
		this.previousCadastralCode = previousCadastralCode;
	}

	public BigDecimal getLotAppraisal() {
		return lotAppraisal;
	}

	public void setLotAppraisal(BigDecimal lotAppraisal) {
		this.lotAppraisal = lotAppraisal;
		if(lotAppraisal == null){
			this.lotAppraisal = BigDecimal.ZERO;
		}
	}

	public BigDecimal getBuildingAppraisal() {
		return buildingAppraisal;
	}

	public void setBuildingAppraisal(BigDecimal buildingAppraisal) {
		this.buildingAppraisal = buildingAppraisal;
		if(buildingAppraisal == null){
			this.buildingAppraisal = BigDecimal.ZERO;
		}
	}

	public BigDecimal getCommercialAppraisal() {
		return commercialAppraisal;
	}

	public void setCommercialAppraisal(BigDecimal commercialAppraisal) {
		this.commercialAppraisal = commercialAppraisal;
		if(commercialAppraisal == null){
			this.commercialAppraisal = BigDecimal.ZERO;
		}
	}	

	public Boolean getEmitWithoutProperty() {
		return emitWithoutProperty;
	}

	public void setEmitWithoutProperty(Boolean emitWithoutProperty) {
		this.emitWithoutProperty = emitWithoutProperty;
	}	


	@Override
	public String toString() {
		if(property != null && property.getLocation() != null && property.getLocation().getMainBlockLimit() != null && property.getLocation().getMainBlockLimit().getStreet() != null){ 
			return property.getPreviousCadastralCode() + " - Dir: " + property.getLocation().getMainBlockLimit().getStreet().getPlace() + " - " + property.getLocation().getMainBlockLimit().getStreet().getName();
		}
		return "";
	}
	
	public List<ValuePair> getDetails(){
		List<ValuePair> details = new LinkedList<ValuePair>();
//		ValuePair pair = new ValuePair("Clave catastral",cadastralCode);
		//rfam 2018-12-26
		ValuePair pair = new ValuePair("Clave Catastral",cadastralCode);
		details.add(pair);
		//rfam 2018-12-26
//		pair = new ValuePair("Clave anterior",previousCadastralCode);
		pair = new ValuePair("Clave Catastral Anterior",previousCadastralCode);
		details.add(pair);
		
		
		//rfam 2018-12-26
		pair = new ValuePair("Área Lote",lotArea != null ? lotArea.toString() : "-");
		details.add(pair);
		
		pair = new ValuePair("Área Construcción",constructionArea != null ? constructionArea.toString() : "-");
		details.add(pair);		
		
		
		pair = new ValuePair("Avaluo terreno", lotAppraisal != null ? lotAppraisal.toString() : "");
		details.add(pair);
		pair = new ValuePair("Avaluo construcción",buildingAppraisal != null ? buildingAppraisal.toString() : "");
		details.add(pair);
		
		//rfam 2018-12-26 se presenta solo en el caso de ser mayor a cero
		if (improvementAppraisal != null && improvementAppraisal.intValue() > 0) {
			pair = new ValuePair("Avaluo mejoras", improvementAppraisal != null ? improvementAppraisal.toString() : "");
			details.add(pair);
		}
		
		pair = new ValuePair("Avaluo comercial",commercialAppraisal != null ? commercialAppraisal.toString() : "");
		details.add(pair);
		pair = new ValuePair("Valor de exencion",exemptionValue != null ? exemptionValue.toString() : "");
		details.add(pair);
		return details;
	}

	@Override
	public String getCode() {
		String pcc = previousCadastralCode != null ? previousCadastralCode : "";  
		String cc = cadastralCode != null ? cadastralCode : "";
		return pcc+" - "+cc;
	}

	public BigDecimal getExemptionValue() {
		return exemptionValue;
	}


	public void setExemptionValue(BigDecimal exemptionValue) {
		this.exemptionValue = exemptionValue;
	}


	public Boolean getChangeAppraisals() {
		return changeAppraisals;
	}


	public void setChangeAppraisals(Boolean changeAppraisals) {
		this.changeAppraisals = changeAppraisals;
	}


	public BigDecimal getRealLotAppraisal() {
		return realLotAppraisal;
	}


	public void setRealLotAppraisal(BigDecimal realLotAppraisal) {
		this.realLotAppraisal = realLotAppraisal;
		if(realLotAppraisal == null){
			this.realLotAppraisal = BigDecimal.ZERO;
		}
	}


	public BigDecimal getRealBuildingAppraisal() {
		return realBuildingAppraisal;
	}


	public void setRealBuildingAppraisal(BigDecimal realBuildingAppraisal) {
		this.realBuildingAppraisal = realBuildingAppraisal;
		if(realBuildingAppraisal == null){
			this.realBuildingAppraisal = BigDecimal.ZERO;
		}
	}


	public BigDecimal getLotArea() {
		return lotArea;
	}


	public void setLotArea(BigDecimal lotArea) {
		this.lotArea = lotArea;
	}


	public BigDecimal getConstructionArea() {
		return constructionArea;
	}


	public void setConstructionArea(BigDecimal constructionArea) {
		this.constructionArea = constructionArea;
	}
	
	public BigDecimal getImprovementAppraisal() {
		return improvementAppraisal;
	}


	public void setImprovementAppraisal(BigDecimal improvementAppraisal) {
		this.improvementAppraisal = improvementAppraisal;
	}

}
