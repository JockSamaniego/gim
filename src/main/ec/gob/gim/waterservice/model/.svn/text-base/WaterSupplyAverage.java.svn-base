package ec.gob.gim.waterservice.model;

import java.math.BigDecimal;

public class WaterSupplyAverage {
	
	private Long waterSupplyId;
	private BigDecimal amount;
	
	public WaterSupplyAverage(Long waterSupplyId, Object amount){		
		this.waterSupplyId = waterSupplyId;
		if(amount != null)this.amount = new BigDecimal(amount.toString());
		else this.amount = BigDecimal.ZERO;
	}
	
	public WaterSupplyAverage(){
	}

	
	public Long getWaterSupplyId() {
		return waterSupplyId;
	}

	public void setWaterSupplyId(Long waterSupplyId) {
		this.waterSupplyId = waterSupplyId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Override
	public boolean equals(Object object) {		
		if (!(object instanceof WaterSupplyAverage)) {
			return false;
		}
		WaterSupplyAverage other = (WaterSupplyAverage) object;
					
		if ((this.waterSupplyId == null && other.getWaterSupplyId() != null)|| (this.waterSupplyId != null && !this.waterSupplyId.equals(other.getWaterSupplyId()))) {
			return false;
		}
		return true;
	}

}
