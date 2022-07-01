/**
 * 
 */
package org.gob.gim.coercive.service;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;

import org.gob.gim.coercive.dto.criteria.PaymentInfractionsSearchCriteria;

import ec.gob.gim.coercive.model.infractions.PaymentInfraction;

/**
 * @author René
 *
 */
@Local
public interface PaymentInfractionsService {
	
	public String LOCAL_NAME = "/gim/PaymentInfractionsService/local";

	public List<PaymentInfraction> getPaymentsByCriteria(PaymentInfractionsSearchCriteria criteria, Long statusid);

	public List<PaymentInfraction> findPaymentInfractionByCriteria(
			PaymentInfractionsSearchCriteria criteria, Integer firstRow,
			Integer numberOfRows);

	public PaymentInfraction findObjectById(Long id);
	  
	public Integer findPaymentsNumber(
			PaymentInfractionsSearchCriteria criteria);
	
	public BigDecimal getTotalByCriteriaSearch(PaymentInfractionsSearchCriteria criteria);
	 

}
