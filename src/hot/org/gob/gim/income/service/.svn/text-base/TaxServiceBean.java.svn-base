package org.gob.gim.income.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.gob.gim.common.service.CrudService;

import ec.gob.gim.income.model.Tax;

/**
 * @author wilman
 *
 */
@Stateless(name="TaxService")
public class TaxServiceBean implements TaxService{
	
	@EJB
	CrudService crudService;

	@SuppressWarnings("unchecked")
	@Override
	public List<Tax> findByEntryId(Long entryId) {
		Map<String, Long> parameters = new HashMap<String, Long>();
		parameters.put("entryId", entryId);
		return crudService.findWithNamedQuery("Tax.findByEntryId", parameters);
	}

}
