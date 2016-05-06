package ec.gob.gim.revenue.model.adjunct.pindal;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ec.gob.gim.revenue.model.adjunct.BusinessLocalReference;
import ec.gob.gim.revenue.model.adjunct.ValuePair;

@Entity
@DiscriminatorValue("BL")
public class BusinessLocal extends BusinessLocalReference{	
	
	private Boolean collectPaperValue;
	
	public BusinessLocal(){
		collectPaperValue = Boolean.FALSE;
	}
		
	@Override
	public String toString() {	
		if(super.getLocal() == null || super.getLocal().getBusiness() == null) {
			return "";
		}
		return super.getLocal().getBusiness().getName() + " - " + super.getLocal().getAddress().toString() + (super.getLostFiscalYear() ? " - EJERCICIO EN PÉRDIDA"  : "" );		
	}
	
	public List<ValuePair> getDetails(){
		List<ValuePair> details = new LinkedList<ValuePair>();
		if(super.getLocal() == null) return details;
		ValuePair pair = new ValuePair("Negocio",super.getLocal().getBusiness().getName());
		details.add(pair);
		pair = new ValuePair("Ubicación", super.getLocal().getAddress().toString());
		details.add(pair);
		if(super.getLocal()!=null && super.getLocal().getBusiness() !=null && super.getLocal().getBusiness().getManager() != null){
			pair = new ValuePair("Administrador", super.getLocal().getBusiness().getManager().getName());
		} else{
			pair = new ValuePair("Propietario", super.getLocal().getBusiness().getOwner().getName());
		}
			
		details.add(pair);		
		return details;
	}

	public Boolean getCollectPaperValue() {
		return collectPaperValue;
	}

	public void setCollectPaperValue(Boolean collectPaperValue) {
		this.collectPaperValue = collectPaperValue;
	}

}
