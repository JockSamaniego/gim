package ec.gob.gim.revenue.model.impugnment.criteria;

import ec.gob.gim.common.model.ItemCatalog;

public class ImpugnmentSearchCriteria {

	private String numberProsecution;

	private String numberInfringement;

	private String identificationNumber;

	private ItemCatalog state;
	
	public ImpugnmentSearchCriteria() {
		this.state =null; 
	}

	public String getNumberProsecution() {
		return numberProsecution;
	}

	public void setNumberProsecution(String numberProsecution) {
		this.numberProsecution = numberProsecution;
	}

	public String getNumberInfringement() {
		return numberInfringement;
	}

	public void setNumberInfringement(String numberInfringement) {
		this.numberInfringement = numberInfringement;
	}
	
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public ItemCatalog getState() {
		return state;
	}

	public void setState(ItemCatalog state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "ImpugnmentSearchCriteria [numberProsecution="
				+ numberProsecution + ", numberInfringement="
				+ numberInfringement + "]";
	}
}