package org.gob.gim.revenue.view;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class ReportView {
	
	private String person;
	
	private Long personId;		
	
	private Long totalCorrect;
	
	private BigDecimal efficiency;
		
	private Long totalReversed;
	
	private BigDecimal totalEmitted;
	
	private BigDecimal total;

	private BigDecimal totalValueReversed;
	
	public BigDecimal getTotalValueReversed() {
		return totalValueReversed;
	}

	public void setTotalValueReversed(BigDecimal totalValueReversed) {
		this.totalValueReversed = totalValueReversed;
	}

	public ReportView(Long id,String person, Long totalCorrect, BigDecimal total) {
		this.personId = id;
		this.person = person;
		this.totalCorrect = totalCorrect;
		this.totalEmitted = total;
		this.total=BigDecimal.ZERO;
		this.totalReversed = 0L;
		this.totalValueReversed = BigDecimal.ZERO;
	}
	
	public ReportView(Long id,String person, Long total) {
		this.personId = id;
		this.person = person;
		this.totalCorrect = total;		
	}
	
	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getTotalCorrect() {
		return totalCorrect;
	}

	public void setTotalCorrect(Long totalCorrect) {
		this.totalCorrect = totalCorrect;
	}

	public Long getTotalReversed() {
		return totalReversed;
	}

	public void setTotalReversed(Long totalReversed) {
		this.totalReversed = totalReversed;
	}
	
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	
	public BigDecimal getTotalEmitted() {
		return totalEmitted;
	}
	public void setTotalEmitted(BigDecimal totalEmitted) {
		this.totalEmitted = totalEmitted;
	}
	
	public void calculateEfficiency(){
		efficiency = BigDecimal.ZERO;
		
		if(totalCorrect == null && totalReversed == null){
			efficiency = new BigDecimal(100);
		}else{
			if(totalCorrect == null){
				totalCorrect = 0L;
			}
			
			if(totalReversed == null){
				totalReversed = 0L;
			}

			efficiency = (new BigDecimal(totalCorrect).multiply(new BigDecimal(100))).divide(new BigDecimal(totalCorrect).add(new BigDecimal(totalReversed)),2,RoundingMode.HALF_UP);		
			efficiency = efficiency.setScale(2, RoundingMode.HALF_UP);			
		}
		
	}
	
	public BigDecimal getEfficiency() {
		return efficiency;
	}

	public void setEfficiency(BigDecimal efficiency) {
		this.efficiency = efficiency;
	}
	
	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
