package ec.gob.gim.revenue.model.adjunct;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.envers.Audited;
import org.jboss.seam.core.Interpolator;

import ec.gob.gim.revenue.model.Adjunct;
import ec.gob.gim.revenue.model.ConcessionType;

@Audited
@Entity
@DiscriminatorValue("CONC") 
public class MiningConcession extends Adjunct{
	private ConcessionType concessionType;	
	
	@Override
	public String toString() {
		String name = Interpolator.instance().interpolate("#{messages['"+ concessionType.name() + "']}", new Object[0]);
		return name;
	}
	
	public List<ValuePair> getDetails(){
		List<ValuePair> details = new LinkedList<ValuePair>();
		String name = Interpolator.instance().interpolate("#{messages['"+ concessionType.name() + "']}", new Object[0]);
		ValuePair pair = new ValuePair("Tipo de Concesión", name);
		details.add(pair);		
		return details;
	}

	public ConcessionType getConcessionType() {
		return concessionType;
	}

	public void setConcessionType(ConcessionType concessionType) {
		this.concessionType = concessionType;
	}	
}
