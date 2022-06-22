/**
 * 
 */
package org.gob.gim.coercive.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.pagination.NotificationInfractionsDataModel;
import org.gob.gim.coercive.service.NotificationInfractionsService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;

import ec.gob.gim.coercive.model.infractions.HistoryStatusNotification;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;

/**
 * @author René
 *
 */

@Name("notificationInfractionListHome")
@Scope(ScopeType.CONVERSATION)
public class NotificationInfractionListHome extends EntityController {


	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NotificationInfractionSearchCriteria criteria;

	private List<Long> generatedNotificationIds = new ArrayList<Long>();

	private List<ItemCatalog> statuses = new ArrayList<ItemCatalog>();
	private ItemCatalog status;

	private String changeStatusExplanation;

	private Date date;

	private Person person;

	private NotificationInfractions currentNotification;

	private ItemCatalogService itemCatalogService;
	
	private NotificationInfractionsService notificationInfractionsService;

	/**
	 * 
	 */
	public NotificationInfractionListHome() {
		super();
		this.criteria = new NotificationInfractionSearchCriteria();
		this.initializeService();
		this.statuses = this.itemCatalogService
				.findItemsForCatalogCodeOrderById("CAT_STATUS_NOTIF_INFRACCCIONS");
		this.search();
	}

	public void initializeService() {

		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		
		if (notificationInfractionsService == null) {
			notificationInfractionsService = ServiceLocator.getInstance().findResource(
					NotificationInfractionsService.LOCAL_NAME);
		}
		
		
	}

	public NotificationInfractionSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(NotificationInfractionSearchCriteria criteria) {
		this.criteria = criteria;
	}

	/**
	 * @return the statuses
	 */
	public List<ItemCatalog> getStatuses() {
		return statuses;
	}

	/**
	 * @param statuses
	 *            the statuses to set
	 */
	public void setStatuses(List<ItemCatalog> statuses) {
		this.statuses = statuses;
	}

	/**
	 * @return the status
	 */
	public ItemCatalog getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(ItemCatalog status) {
		this.status = status;
	}

	/**
	 * @return the changeStatusExplanation
	 */
	public String getChangeStatusExplanation() {
		return changeStatusExplanation;
	}

	/**
	 * @param changeStatusExplanation
	 *            the changeStatusExplanation to set
	 */
	public void setChangeStatusExplanation(String changeStatusExplanation) {
		this.changeStatusExplanation = changeStatusExplanation;
	}

	/**
	 * @return the currentNotification
	 */
	public NotificationInfractions getCurrentNotification() {
		return currentNotification;
	}

	/**
	 * @param currentNotification
	 *            the currentNotification to set
	 */
	public void setCurrentNotification(
			NotificationInfractions currentNotification) {
		this.currentNotification = currentNotification;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the generatedNotificationIds
	 */
	public List<Long> getGeneratedNotificationIds() {
		return generatedNotificationIds;
	}

	/**
	 * @param generatedNotificationIds
	 *            the generatedNotificationIds to set
	 */
	public void setGeneratedNotificationIds(List<Long> generatedNotificationIds) {
		this.generatedNotificationIds = generatedNotificationIds;
	}

	public void search() {
		getDataModel().setCriteria(this.criteria);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());
	}

	private NotificationInfractionsDataModel getDataModel() {

		NotificationInfractionsDataModel dataModel = (NotificationInfractionsDataModel) Component
				.getInstance(NotificationInfractionsDataModel.class, true);

		return dataModel;
	}

	public String prepareRePrint(Long notificationId) {

		this.generatedNotificationIds = new ArrayList<Long>();
		this.generatedNotificationIds.add(notificationId);

		return "sendToPrint";
	}

	public void prepareChangeStatus(NotificationInfractions notification) {
		this.currentNotification = notification;
		this.status = null;
		this.changeStatusExplanation = null;
		this.date = new Date();
		this.person = null;
	}
	
	public void prepareViewHistoryChangeStatus(NotificationInfractions notification) {
		this.currentNotification = notificationInfractionsService.findWithHistoryById(notification.getId());
	}

	public void saveChangeStatus() {
		
		this.currentNotification = notificationInfractionsService.findObjectById(this.currentNotification.getId());
		this.currentNotification.setStatus(status);		
		this.currentNotification = notificationInfractionsService.updateNotification(currentNotification);
		
		
		HistoryStatusNotification record = new HistoryStatusNotification();
		record.setDate(date);
		record.setNotification(currentNotification);
		record.setObservation(changeStatusExplanation);
		record.setResponsible(person);
		record.setStatus(status);
		record.setUser(this.userSession.getUser());
		
		notificationInfractionsService.saveHistoryStatus(record);
		
		
	}
}
