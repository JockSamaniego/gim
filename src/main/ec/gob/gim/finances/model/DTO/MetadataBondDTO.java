package ec.gob.gim.finances.model.DTO;

import java.math.BigDecimal;

public class MetadataBondDTO {
	
	
	private BigDecimal interest;
	
	private BigDecimal surcharge;
	

	public MetadataBondDTO() {
		super();
		this.interest = BigDecimal.ZERO;
		this.surcharge = BigDecimal.ZERO;
	}
	
	public MetadataBondDTO(BigDecimal interest, BigDecimal surcharge) {
		super();
		this.interest = interest;
		this.surcharge = surcharge;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(BigDecimal surcharge) {
		this.surcharge = surcharge;
	}
	

	
	
}