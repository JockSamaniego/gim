package org.gob.gim.income.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.income.model.TaxRate;

@Local
public interface TaxRateService {

	List<TaxRate> findActiveByTaxIdAndPaymentDate(Long taxId, Date paymentDate);
}
