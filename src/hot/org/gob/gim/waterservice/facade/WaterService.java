package org.gob.gim.waterservice.facade;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.waterservice.model.Consumption;
import ec.gob.gim.waterservice.model.RoutePreviewEmission;
import ec.gob.gim.waterservice.model.WaterBlockLog;

@Local
public interface WaterService {

	//void savePreEmissionOrder(EmissionOrder e) throws Exception;
	void saveEmissionOrder(EmissionOrder emissionOrder, Boolean preEmit,List<Long> waterSupplyIds);
	void saveEmissionOrder(EmissionOrder emissionOrder, Boolean preEmit);
	void saveEmissionOrderBudget(EmissionOrder emissionOrder, Boolean preEmit);
	void saveWaterBlockLog(WaterBlockLog waterBlockLog);
	
	public void saveEmissionOrderMerchandising(EmissionOrder emissionOrder, Boolean preEmit, RoutePreviewEmission rpe, List<Long> waterSupplyIds);
	

	// void preEmissionOrderWaterConsumption(String cadastralCode, FiscalPeriod
	// f, Person p, boolean isUrban) throws Exception;
	EmissionOrder calculatePreEmissionOrderWaterConsumption(List<Consumption> consumptions,
			FiscalPeriod f, Person p, int year, int month, String observation) throws Exception;

	EmissionOrder reCalculatePreEmissionOrder(List<Consumption> consumptions, FiscalPeriod f, Person p) throws Exception;
	
	List<Consumption> getConsumptionsToUpdate();
}
