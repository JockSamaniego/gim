package ec.gob.gim.revenue.model.impugnment.criteria;

public class ImpugnmentSearchCriteria {
	
	private Integer numberProsecution;
	
	private Integer numberInfringement;
	
	public Integer getNumberProsecution() {
		return numberProsecution;
	}
	
	public void setNumberProsecution(Integer numberProsecution) {
		this.numberProsecution = numberProsecution;
	}
	
	public Integer getNumberInfringement() {
		return numberInfringement;
	}
	
	public void setNumberInfringement(Integer numberInfringement) {
		this.numberInfringement = numberInfringement;
	}
	
	@Override
	public String toString() {
		return "ImpugnmentSearchCriteria [numberProsecution="
				+ numberProsecution + ", numberInfringement="
				+ numberInfringement + "]";
	}
}