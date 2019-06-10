package ec.gob.gim.waterservice.model.dto;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class ResidentsInRouteDTO {

	@NativeQueryResultColumn(index = 0)
	private Long waterSupplyId;
	
	@NativeQueryResultColumn(index = 1)
	private Integer serviceNumber;
	
	@NativeQueryResultColumn(index = 2)
	private Integer routeOrder;

	public Long getWaterSupplyId() {
		return waterSupplyId;
	}

	public void setWaterSupplyId(Long waterSupplyId) {
		this.waterSupplyId = waterSupplyId;
	}

	public Integer getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(Integer serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public Integer getRouteOrder() {
		return routeOrder;
	}

	public void setRouteOrder(Integer routeOrder) {
		this.routeOrder = routeOrder;
	}
	
	
}
