package ec.gob.gim.waterservice.model.dto;

import org.gob.gim.common.NativeQueryResultColumn;
import org.gob.gim.common.NativeQueryResultEntity;

@NativeQueryResultEntity
public class WaterMeterDisabledDTO {

	@NativeQueryResultColumn(index = 0)
	private Integer serviceNumber;
	
	@NativeQueryResultColumn(index = 1)
	private Integer totalFalse;
	
	@NativeQueryResultColumn(index = 2)
	private Integer totalTrue;
	
	@NativeQueryResultColumn(index = 3)
	private Integer totalCount;

	public Integer getServiceNumber() {
		return serviceNumber;
	}

	public void setServiceNumber(Integer serviceNumber) {
		this.serviceNumber = serviceNumber;
	}

	public Integer getTotalFalse() {
		return totalFalse;
	}

	public void setTotalFalse(Integer totalFalse) {
		this.totalFalse = totalFalse;
	}

	public Integer getTotalTrue() {
		return totalTrue;
	}

	public void setTotalTrue(Integer totalTrue) {
		this.totalTrue = totalTrue;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
