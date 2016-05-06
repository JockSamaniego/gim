package ec.gob.gim.wsrest.dto;

import java.util.List;

public class ConsumptionPackage {
	private List<DtoConsumption> dtoConsumptionList;

	public ConsumptionPackage() {

	}

	public ConsumptionPackage(List<DtoConsumption> dtoConsumptionList) {
		this.dtoConsumptionList = dtoConsumptionList;
	}

	public List<DtoConsumption> getDtoConsumptionList() {
		return dtoConsumptionList;
	}

	public void setDtoConsumptionList(List<DtoConsumption> dtoConsumptionList) {
		this.dtoConsumptionList = dtoConsumptionList;
	}
	
}
