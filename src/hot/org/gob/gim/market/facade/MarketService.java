package org.gob.gim.market.facade;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.market.model.Market;
import ec.gob.gim.market.model.MarketEmission;
import ec.gob.gim.market.model.Stand;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.EmissionOrder;

@Local
public interface MarketService {

	void preEmitRentStand(Contract c, Stand s, FiscalPeriod f, Person p)
			throws Exception;

	EmissionOrder generateEmissionOrder(Market market, List<Stand> stands, FiscalPeriod f,
			Person per, int year, int month, String descriptionPart) throws Exception;

	void savePreEmissionOrder(EmissionOrder e) throws Exception;

	void updateStand(Stand e) throws Exception;

	EmissionOrder calculatePreEmissionRentStand(Contract c, Stand s,
			FiscalPeriod f, Person p) throws Exception;

	EmissionOrder generatePreEmissionRentStand(Contract c, Stand s,
			FiscalPeriod f, Person p, BigDecimal value) throws Exception;

	/*EmissionOrder calculateEmissionOrderStand(Market market,
			List<Stand> stands, FiscalPeriod f, Person p, boolean preEmit,
			int year, int month) throws Exception;*/
	
	void saveEmissionOrder(EmissionOrder emissionOrder, Boolean preEmit, MarketEmission me );

}
