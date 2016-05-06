package org.gob.gim.rental.facade;

import javax.ejb.Local;

import org.gob.gim.parking.action.TicketCashClosingList;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.parking.model.ParkingRent;
import ec.gob.gim.rental.model.Space;
import ec.gob.gim.revenue.model.Contract;
import ec.gob.gim.revenue.model.EmissionOrder;

@Local
public interface RentalService {
	
	void preEmitRentSpace(Contract c,Space s, FiscalPeriod f, Person per) throws Exception;	
	void savePreEmissionOrder(EmissionOrder e) throws Exception;	
	EmissionOrder calculatePreEmissionRentSpace(Contract c,Space s, FiscalPeriod f, Person p) throws Exception;
	void preEmitParkingRent(ParkingRent pr, FiscalPeriod f, Long payments) throws Exception;
	EmissionOrder preEmitCashClosingParkingRent(TicketCashClosingList  ticketList, FiscalPeriod f, Person p)
			throws Exception;
	
}

