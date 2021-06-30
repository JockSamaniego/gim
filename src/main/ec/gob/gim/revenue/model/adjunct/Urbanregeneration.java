package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import com.sun.istack.Nullable;

import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.cadaster.model.Property;

@Audited
@Entity
@DiscriminatorValue("UREG")
public class Urbanregeneration extends Adjunct{

	private BigDecimal commercialAppraisal;
	private BigDecimal front;
	private BigDecimal lotArea;
	
	@ManyToOne
	private Resident resident;
	@ManyToOne
	private Property property;
	
	private String cadastralCode;
	private String previousCadastralCode;					
	private Integer paymentsNumber;
	
	//para emision x BD se pondran en nulo los booleanos
	@Nullable
	private Boolean emitWithoutProperty = Boolean.FALSE;
	
	@Nullable
	private Boolean changeAppraisals = Boolean.FALSE;
	
	
	public BigDecimal getCommercialAppraisal() {
		return commercialAppraisal;
	}


	public void setCommercialAppraisal(BigDecimal commercialAppraisal) {
		this.commercialAppraisal = commercialAppraisal;
	}


	public BigDecimal getFront() {
		return front;
	}


	public void setFront(BigDecimal front) {
		this.front = front;
	}


	public BigDecimal getLotArea() {
		return lotArea;
	}

	public void setLotArea(BigDecimal lotArea) {
		this.lotArea = lotArea;
	}



	public String getCadastralCode() {
		return cadastralCode;
	}



	public void setCadastralCode(String cadastralCode) {
		this.cadastralCode = cadastralCode;
	}


	public Integer getPaymentsNumber() {
		return paymentsNumber;
	}



	public void setPaymentsNumber(Integer paymentsNumber) {
		this.paymentsNumber = paymentsNumber;
	}
	

	public Resident getResident() {
		return resident;
	}


	public void setResident(Resident resident) {
		this.resident = resident;
	}

	

	public Property getProperty() {
		return property;
	}


	public void setProperty(Property property) {
		this.property = property;
	}


	public String getPreviousCadastralCode() {
		return previousCadastralCode;
	}


	public void setPreviousCadastralCode(String previousCadastralCode) {
		this.previousCadastralCode = previousCadastralCode;
	}


	@Override
	public List<ValuePair> getDetails() {
		List<ValuePair> details = new LinkedList<ValuePair>();

		
		
		ValuePair pair = new ValuePair("Clave catastral", cadastralCode); 
		details.add(pair);		

		if(resident!= null) {
			pair = new ValuePair("Identificacion", resident.getIdentificationNumber());
			details.add(pair);
		}
		
				
		pair = new ValuePair("Clave catastral anterior: ", previousCadastralCode);
		details.add(pair);
		
		if(paymentsNumber != null) {
			pair = new ValuePair("Numero cuotas", paymentsNumber.toString());
			details.add(pair);
		}
		
		pair = new ValuePair("Avalúo comercial", commercialAppraisal.toString());		
		details.add(pair);
		
		if(front!=null) {
			pair = new ValuePair("Frente", front.toString());		
			details.add(pair);
		}
		
		pair = new ValuePair("Area", lotArea.toString());		
		details.add(pair);

		return details;
	}

	public Boolean getEmitWithoutProperty() {
		return emitWithoutProperty;
	}

	public void setEmitWithoutProperty(Boolean emitWithoutProperty) {
		this.emitWithoutProperty = emitWithoutProperty;
	}
	
	public Boolean getChangeAppraisals() {
		return changeAppraisals;
	}

	public void setChangeAppraisals(Boolean changeAppraisals) {
		this.changeAppraisals = changeAppraisals;
	}


	@Override
	public String toString() {
		return "Clave catastral anterior: "+previousCadastralCode;
	}
	
	
	
}
