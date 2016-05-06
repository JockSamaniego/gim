package org.gob.gim.coercive.action;

import java.util.Arrays;
import java.util.List;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.coercive.model.Notification;
import ec.gob.gim.coercive.model.NotificationTaskType;
import ec.gob.gim.common.model.Resident;

@Name("notificationReport")
public class NotificationReport extends EntityQuery<Notification> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7829139376689892812L;

	private static final String EJBQL = "select notification from Notification notification left join fetch notification.municipalBonds mb";

	private static final String[] RESTRICTIONS = {
		"notification.id IN (#{notificationReport.selectedItemAsList.isEmpty() ? null : notificationReport.selectedItemAsList})",
		};

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	
	private SystemParameterService systemParameterService;

	private String selectedItems = "";

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private Notification notification = new Notification();
	
	private NotificationTaskType notificationTaskType;
	
	private Resident resident = null;
	
	List<Notification> notifications;
	
	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	@Override
	@Transactional
	public List<Notification> getResultList() {
		// TODO Auto-generated method stub
		if(notifications != null && notifications.size() > 0) return notifications;
		notifications = super.getResultList();
		return notifications;
	}

	public NotificationReport() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
	}

	@Override
	public String getRestrictionLogicOperator() {
		return "and";
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public void setSelectedItems(String selectedItems) {		
		this.selectedItems = selectedItems;
	}

	public String getSelectedItems() {
		return selectedItems;
	}

	public List<Long> getSelectedItemAsList() {
		return org.gob.gim.common.Util.splitArrayString(this.getSelectedItems());
	}

	public String getOrderColumn() {
		return "notification.id";
	}
	
	public NotificationTaskType getNotificationType(){
		if(notificationTaskType != null) return notificationTaskType;
		if (systemParameterService == null){
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		notificationTaskType = systemParameterService.materialize(NotificationTaskType.class, "NOTIFICATION_LEGAL_NOTE");
		return notificationTaskType;
	}
	

}
