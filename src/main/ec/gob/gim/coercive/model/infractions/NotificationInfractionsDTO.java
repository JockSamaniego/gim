package ec.gob.gim.coercive.model.infractions;

import java.util.List;


public class NotificationInfractionsDTO {

	private List<Datainfraction> pendingInfractions;
	private String identificationNumber;
	private String name;
	public List<Datainfraction> getPendingInfractions() {
		return pendingInfractions;
	}
	public void setPendingInfractions(List<Datainfraction> pendingInfractions) {
		this.pendingInfractions = pendingInfractions;
	}
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
