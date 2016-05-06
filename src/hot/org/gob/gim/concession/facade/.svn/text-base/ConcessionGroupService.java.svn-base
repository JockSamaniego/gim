package org.gob.gim.concession.facade;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.common.model.FiscalPeriod;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.consession.model.ConcessionGroup;
import ec.gob.gim.consession.model.ConcessionGroupEmission;
import ec.gob.gim.consession.model.ConcessionItem;
import ec.gob.gim.revenue.model.EmissionOrder;

@Local
public interface ConcessionGroupService {
	EmissionOrder generateEmissionOrder(ConcessionGroup cg,
			List<ConcessionItem> items, FiscalPeriod f, Person per, int year, int month, String descriptionPart) throws Exception;
	void saveEmissionOrder(EmissionOrder emissionOrder,ConcessionGroupEmission cgEmission, Boolean preEmit );
}
