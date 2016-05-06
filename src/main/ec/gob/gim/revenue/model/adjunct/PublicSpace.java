package ec.gob.gim.revenue.model.adjunct;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ec.gob.gim.revenue.model.Adjunct;

@Entity
@DiscriminatorValue("PS") 
public class PublicSpace extends Adjunct{
	
	private BigDecimal daysNumber;
	
	public PublicSpace() {
		daysNumber = BigDecimal.ONE;
	}
	
	@Override
	public String toString() {	
		return "Numero de dias" + daysNumber;
	}
	
	public List<ValuePair> getDetails(){
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Numero de dias", daysNumber.toString());
		details.add(pair);		
		return details;
	}

	public BigDecimal getDaysNumber() {
		return daysNumber;
	}

	public void setDaysNumber(BigDecimal daysNumber) {
		this.daysNumber = daysNumber;
	}
	
}
