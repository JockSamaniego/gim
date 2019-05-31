package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.revenue.model.bankDebit.BankDebit;
import ec.gob.gim.revenue.model.bankDebit.criteria.BankDebitSearchCriteria;
import ec.gob.gim.revenue.model.bankDebit.dto.BankDebitDTO;
import ec.gob.gim.revenue.model.impugnment.Impugnment;
import ec.gob.gim.waterservice.model.WaterSupply;

/**
 *  * @author René Ortega
 *  * @Fecha 2019-05-22
 */

@Local
public interface BankDebitService {

	public String LOCAL_NAME = "/gim/BankDebitService/local";

	public List<BankDebitDTO> findDebitsForCriteria(BankDebitSearchCriteria criteria);
	
	public WaterSupply findWaterSupplyByServiceNumber(Integer serviceNumber);
	
	public BankDebit save(BankDebit debit);
	
	public BankDebit findById(Long bankDebitId);
	
	public BankDebit update(BankDebit debit);
		
}
