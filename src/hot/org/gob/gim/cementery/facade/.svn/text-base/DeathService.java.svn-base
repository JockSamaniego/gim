package org.gob.gim.cementery.facade;

import javax.ejb.Local;

import ec.gob.gim.cementery.model.Unit;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.EmissionOrder;

@Local
public interface DeathService {
	
	void preEmitDeathUnit(Contract c, Unit d, FiscalPeriod f, Person per) throws Exception;	
	void savePreEmissionOrder(EmissionOrder e) throws Exception;	
	EmissionOrder calculatePreEmissionDeathUnit(Contract c, Unit d, FiscalPeriod f, Person p) throws Exception;
//	void preEmitParkingRent(ParkingRent pr, FiscalPeriod f) throws Exception;
	
}

