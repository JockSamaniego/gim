package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;

public class BudgetItemResult {
	
	private Long  entryId;

	private BigDecimal total;
	
	public BudgetItemResult(){
		
	}
	  	  
  	public BudgetItemResult(Long entryId, BigDecimal total){
		this.entryId = entryId;				
		this.total = total;		
	}
  	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

}
