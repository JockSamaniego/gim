package ec.gob.gim.revenue.model.bankDebit.dto;

import java.math.BigDecimal;

public class MunicipalBondLiquidationReportDTO {
	
	private Long municipalBondId;
	
	private String residentIdentification;
	
	private String residentName;
	
	private BigDecimal paidTotal;
	
	private String observation;
	
	public MunicipalBondLiquidationReportDTO(){
		observation= "liquidada";
	}

	public Long getMunicipalBondId() {
		return municipalBondId;
	}

	public void setMunicipalBondId(Long municipalBondId) {
		this.municipalBondId = municipalBondId;
	}

	public String getResidentIdentification() {
		return residentIdentification;
	}

	public void setResidentIdentification(String residentIdentification) {
		this.residentIdentification = residentIdentification;
	}

	public String getResidentName() {
		return residentName;
	}

	public void setResidentName(String residentName) {
		this.residentName = residentName;
	}

	public BigDecimal getPaidTotal() {
		return paidTotal;
	}

	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
	
	
}
