/**
 * 
 */
package org.gob.gim.coercive.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.gim.coercive.dto.InfractionItemDTO;
import org.gob.gim.coercive.dto.criteria.DataInfractionSearchCriteria;
import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.dto.criteria.OverdueInfractionsSearchCriteria;
import org.gob.gim.coercive.view.InfractionUserData;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.HistoryStatusInfraction;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;
import ec.gob.gim.coercive.model.infractions.PaymentNotification;

/**
 * @author René
 *
 */
@Local
public interface DatainfractionService {
	
	public String LOCAL_NAME = "/gim/DatainfractionService/local";
	
	public List<Datainfraction> findInfractionsByIdentification(String identification);
	
	public Long getNextValue();
	
	
	public Datainfraction getDataInfractionById(Long id);
	
	public Datainfraction getDataInfractionWithHistoryById(Long id);
	
	public Datainfraction updateDataInfraction(Datainfraction data);
	
	public List<Datainfraction> findDataInfractionByCriteria(DataInfractionSearchCriteria criteria, Integer firstRow,Integer numberOfRows);
	
	public Integer findDataInfractionNumber(DataInfractionSearchCriteria criteria);

	public List<PaymentNotification> findPaymentsByNotification(Long notificationId);

	public InfractionUserData userData(Long notificationId);

	public void savePaymentNotification(PaymentNotification payment);
	
	public HistoryStatusInfraction saveHIstoryRecord(HistoryStatusInfraction record);
	
}
