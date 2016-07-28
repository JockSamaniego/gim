package ec.gob.gim.revenue.model.impugnment.criteria;

public class ImpugnmentSearchCriteria {
	
	private Integer numberProsecution;
	
	private String numberInfringement;
	
	public Integer getNumberProsecution() {
		return numberProsecution;
	}
	
	public void setNumberProsecution(Integer numberProsecution) {
		this.numberProsecution = numberProsecution;
	}
	
	public String getNumberInfringement() {
		return numberInfringement;
	}
	
	public void setNumberInfringement(String numberInfringement) {
		this.numberInfringement = numberInfringement;
	}
	
	@Override
	public String toString() {
		return "ImpugnmentSearchCriteria [numberProsecution="
				+ numberProsecution + ", numberInfringement="
				+ numberInfringement + "]";
	}
}