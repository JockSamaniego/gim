/**
 * 
 */
package org.gob.gim.coercive.action;

import java.math.BigDecimal;

import org.gob.gim.coercive.service.DatainfractionService;
import org.gob.gim.common.ServiceLocator;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;

/**
 * @author René
 *
 */
@Name("detailNotificationInfractionsHome")
@Scope(ScopeType.CONVERSATION)
public class DetailNotificationInfractionsHome extends EntityController{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DatainfractionService datainfractionService;
	
	private Long notificationId;
	
	private boolean isFirstTime = true;
	
	private NotificationInfractions notification;
	
	/**
	 * 
	 */
	public DetailNotificationInfractionsHome() {
		super();
	}
	
	public void loadNotificationInfraction(){
		if (isFirstTime) {
			if (datainfractionService == null) {
				datainfractionService = ServiceLocator.getInstance().findResource(
						datainfractionService.LOCAL_NAME);
			}
			// this.notifications = this.datainfractionService.getNotifications(this.getSelectedItemAsList());
			this.notification = this.datainfractionService.findObjectById(this.notificationId);
		}
	}
	
	/**
	 * @return the notificationId
	 */
	public Long getNotificationId() {
		return notificationId;
	}

	/**
	 * @param notificationId the notificationId to set
	 */
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	/**
	 * @return the notification
	 */
	public NotificationInfractions getNotification() {
		return notification;
	}

	/**
	 * @param notification the notification to set
	 */
	public void setNotification(NotificationInfractions notification) {
		this.notification = notification;
	}
	
	public BigDecimal getTotal(){
		BigDecimal total = BigDecimal.ZERO;
		for (Datainfraction infraction : this.notification.getInfractions()) {
			total = total.add(infraction.getValue());
		}
		return total;
	}
	
}
