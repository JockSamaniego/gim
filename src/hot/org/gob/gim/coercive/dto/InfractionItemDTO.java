package org.gob.gim.coercive.dto;

import java.math.BigDecimal;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class InfractionItemDTO {

	private boolean isSelected = Boolean.FALSE;
	@NativeQueryResultColumn(index = 0)
	private String name;
	
	@NativeQueryResultColumn(index = 1)
	private String identification;
	
	@NativeQueryResultColumn(index = 2)
	private Long totalinfractions;
	
	@NativeQueryResultColumn(index = 3)
	private BigDecimal value;
	
	@NativeQueryResultColumn(index = 4)
	private BigDecimal interest;
	
	@NativeQueryResultColumn(index = 5)
	private BigDecimal total;
		
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	 

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getInterest() {
		return interest;
	}

	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}	

	public Long getTotalinfractions() {
		return totalinfractions;
	}

	public void setTotalinfractions(Long totalinfractions) {
		this.totalinfractions = totalinfractions;
	}

	@Override
	public String toString() {
		return "InfractionItemDTO [isSelected=" + isSelected + ", name=" + name + ", identification="
				+ identification + ", value=" + value + ", interest=" + interest + ", total=" + total + "]";
	}
	
}

