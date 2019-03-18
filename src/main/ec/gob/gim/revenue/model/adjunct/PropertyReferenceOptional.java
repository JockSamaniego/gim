package ec.gob.gim.revenue.model.adjunct;


import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.revenue.model.Adjunct;

@Entity
@DiscriminatorValue("PRO")
public class PropertyReferenceOptional extends Adjunct{
	@ManyToOne
	private Property property;

	@Column(length=150)
	private String owner;

	@Column(length=100)
	private String location;
	
	private Boolean emitWithoutProperty;
	
	public Boolean getEmitWithoutProperty() {
		return emitWithoutProperty;
	}

	public void setEmitWithoutProperty(Boolean emitWithoutProperty) {
		this.emitWithoutProperty = emitWithoutProperty;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}
	
	
	public PropertyReferenceOptional() {
		this.emitWithoutProperty = Boolean.FALSE;
	}

	@Override
	public String toString() {
		if(property == null){
			return"";
		}else{
			if(property.getLocation() == null || this.emitWithoutProperty) return "";
			return "Dir: " + property.getLocation().getMainBlockLimit().getStreet().getPlace() + " - " + location.trim();
		}		
	}
	
	public List<ValuePair> getDetails(){
		List<ValuePair> details = new LinkedList<ValuePair>();
		ValuePair pair = new ValuePair("Propietario", owner);
		details.add(pair);
		if ((location != null) && (location.trim().length() > 0)){
			pair = new ValuePair("Ubicación", location.trim());
			details.add(pair);
		}
		return details;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String getCode() {
		if(this.emitWithoutProperty){
			return "";
		}else{
			String pcc = property.getPreviousCadastralCode() != null ? property.getPreviousCadastralCode() : "";  
			String cc = property.getCadastralCode() != null ? property.getCadastralCode() : "";
			return pcc+" - "+cc;
		}		
	}

}
