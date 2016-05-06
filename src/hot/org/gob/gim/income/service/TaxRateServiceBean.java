package org.gob.gim.income.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.gob.gim.common.service.CrudService;

import ec.gob.gim.income.model.TaxRate;

/**
 * @author wilman
 *
 */
@Stateless(name="TaxRateService")
public class TaxRateServiceBean implements TaxRateService{
	
	@EJB
	CrudService crudService;

	@SuppressWarnings("unchecked")
	@Override
	public List<TaxRate> findActiveByTaxIdAndPaymentDate(Long taxId, Date paymentDate) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("taxId", taxId);
		parameters.put("paymentDate", paymentDate);
		return crudService.findWithNamedQuery("TaxRate.findActiveByTaxIdAndPaymentDate", parameters);
	}

}
