/**
 * 
 */
package org.gob.gim.coercive.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.dto.criteria.PaymentInfractionsSearchCriteria;

import ec.gob.gim.coercive.model.infractions.HistoryStatusNotification;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;
import ec.gob.gim.coercive.model.infractions.PaymentNotification;

/**
 * @author René
 *
 */
@Local
public interface PaymentInfractionsService {
	
	public String LOCAL_NAME = "/gim/PaymentInfractionsService/local";

	public List<PaymentNotification> getPaymentsByCriteria(PaymentInfractionsSearchCriteria criteria, Long statusid);

	public List<PaymentNotification> findPaymentInfractionByCriteria(
			PaymentInfractionsSearchCriteria criteria, Integer firstRow,
			Integer numberOfRows);

	public PaymentNotification findObjectById(Long id);
	  
	public Integer findPaymentsNumber(
			PaymentInfractionsSearchCriteria criteria);
	 

}
