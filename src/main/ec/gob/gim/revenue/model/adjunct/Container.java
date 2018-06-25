package ec.gob.gim.revenue.model.adjunct;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ec.gob.gim.revenue.model.Adjunct;


@Entity
@DiscriminatorValue("CON")
public class Container  extends Adjunct{
	
	private String containerTypeName;
	private Boolean isContainer = Boolean.FALSE;
	
	

	public String getContainerTypeName() {
		return containerTypeName;
	}



	public void setContainerTypeName(String containerTypeName) {
		this.containerTypeName = containerTypeName;
	}



	public Boolean getIsContainer() {
		return isContainer;
	}



	public void setIsContainer(Boolean isContainer) {
		this.isContainer = isContainer;
	}



	@Override
	public String toString() {
		return "Recipiente [" + containerTypeName
				+ "]";
	}



	@Override
	public List<ValuePair> getDetails() {
		// TODO Auto-generated method stub
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Tipo", containerTypeName);
		details.add(pair);
		pair = new ValuePair("Contenedor",  isContainer);
		details.add(pair);
		return details;
	}

}
