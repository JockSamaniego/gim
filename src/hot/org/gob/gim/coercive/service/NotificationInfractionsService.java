/**
 * 
 */
package org.gob.gim.coercive.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;

import ec.gob.gim.coercive.model.infractions.HistoryStatusNotification;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;

/**
 * @author René
 *
 */
@Local
public interface NotificationInfractionsService {
	
	public String LOCAL_NAME = "/gim/NotificationInfractionsService/local";

	public List<NotificationInfractions> getNotifications(List<Long> ids);

	public List<NotificationInfractions> findNotificationInfractionByCriteria(
			NotificationInfractionSearchCriteria criteria, Integer firstRow,
			Integer numberOfRows);

	public NotificationInfractions findObjectById(Long id);

	public Integer findNotificationInfractionsNumber(
			NotificationInfractionSearchCriteria criteria);
	
	public HistoryStatusNotification saveHistoryStatus(HistoryStatusNotification record);
	
	public NotificationInfractions updateNotification(NotificationInfractions notification);

}
