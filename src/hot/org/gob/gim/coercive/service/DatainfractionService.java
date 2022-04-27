/**
 * 
 */
package org.gob.gim.coercive.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.gim.coercive.dto.InfractionItemDTO;
import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.dto.criteria.OverdueInfractionsSearchCriteria;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;

/**
 * @author René
 *
 */
@Local
public interface DatainfractionService {
	
	public String LOCAL_NAME = "/gim/DatainfractionService/local";
	
	public List<Datainfraction> findInfractionsByIdentification(String identification);
	
	public Long getNextValue();
	
	public List<NotificationInfractions> getNotifications(List<Long> ids);
	
	public List<NotificationInfractions> findNotificationInfractionByCriteria(NotificationInfractionSearchCriteria criteria, Integer firstRow,Integer numberOfRows);
	
	public NotificationInfractions findObjectById(Long id);
	
	public Integer findNotificationInfractionsNumber(NotificationInfractionSearchCriteria criteria);
	
	public Datainfraction getDataInfractionById(Long id);
	
	public Datainfraction updateDataInfraction(Datainfraction data);
	
}
