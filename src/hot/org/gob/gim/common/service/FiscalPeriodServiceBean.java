package org.gob.gim.common.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.gim.common.model.FiscalPeriod;

@Stateless(name="FiscalPeriodService")
public class FiscalPeriodServiceBean implements FiscalPeriodService {
	
	@EJB
	CrudService crudService;

	@SuppressWarnings("unchecked")
	@Override
	public FiscalPeriod find(Long id) {
		Map<String, Long> parameters = new HashMap<String, Long>();
		parameters.put("id", id);
		List<FiscalPeriod> result = crudService.findWithNamedQuery("FiscalPeriod.findById", parameters, 1);
		if (!result.isEmpty())
			return result.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FiscalPeriod> findCurrent(Date currentDate) {
		Map<String, Date> parameters = new HashMap<String, Date>();
		parameters.put("currentDate", currentDate);
		return crudService.findWithNamedQuery("FiscalPeriod.findCurrent", parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FiscalPeriod> find() {
		return crudService.findWithNamedQuery("FiscalPeriod.findAll", null);
	}

}
