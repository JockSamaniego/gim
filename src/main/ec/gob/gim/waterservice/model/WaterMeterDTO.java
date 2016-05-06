package ec.gob.gim.waterservice.model;


public class WaterMeterDTO {
	
	private Integer waterMeterDigitsNumber;
	private Long waterSupplyId;	
	
	public WaterMeterDTO(){
		
	}
	
	public WaterMeterDTO(Integer waterMeterDigitsNumber, Long waterSupplyId){
		this.waterMeterDigitsNumber = waterMeterDigitsNumber;
		this.waterSupplyId = waterSupplyId;
	}
	
	public Long getWaterSupplyId() {
		return waterSupplyId;
	}
	public void setWaterSupplyId(Long waterSupplyId) {
		this.waterSupplyId = waterSupplyId;
	}

	public Integer getWaterMeterDigitsNumber() {
		return waterMeterDigitsNumber;
	}

	public void setWaterMeterDigitsNumber(Integer waterMeterDigitsNumber) {
		this.waterMeterDigitsNumber = waterMeterDigitsNumber;
	}

}