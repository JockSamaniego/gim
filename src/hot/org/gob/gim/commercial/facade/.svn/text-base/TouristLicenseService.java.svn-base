package org.gob.gim.commercial.facade;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.commercial.model.BusinessCatalog;
import ec.gob.gim.commercial.model.TouristLicenseEmission;
import ec.gob.gim.commercial.model.TouristLicenseItem;
import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.revenue.model.EmissionOrder;

@Local
public interface TouristLicenseService {

	EmissionOrder generateEmissionOrder(BusinessCatalog bc,
			List<TouristLicenseItem> items, FiscalPeriod f, Person per, int year, String descriptionPart) throws Exception;
	void saveEmissionOrder(EmissionOrder emissionOrder, TouristLicenseEmission tle, Boolean preEmit );
}