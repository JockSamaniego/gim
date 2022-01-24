package org.gob.gim.coercive.view;

import java.math.BigDecimal;


public class InfractionItem {

	private boolean isSelected;
	private String name;
	private String identification;
	private Long totalinfractions;
	private BigDecimal value;
	private BigDecimal interest;
	private BigDecimal total;
		
	public InfractionItem() {
		this.isSelected = Boolean.FALSE;
		this.identification = "";
		this.name = "";
		this.totalinfractions = 0L;
		this.value = BigDecimal.ZERO;
		this.interest = BigDecimal.ZERO;
		this.total = BigDecimal.ZERO;				
	}
		
	public InfractionItem(String identification,
			String name,
			Long totalinfractions, 
			BigDecimal value, 
			BigDecimal interest, 
			BigDecimal total){		
		this.isSelected = Boolean.FALSE;
		this.identification = identification;
		this.name = name;
		this.totalinfractions = totalinfractions;
		this.value = value;
		this.interest = interest;
		this.total = total;		
	}

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
		return "InfractionItem [isSelected=" + isSelected + ", name=" + name + ", identification="
				+ identification + ", value=" + value + ", interest=" + interest + ", total=" + total + "]";
	}
	
}

