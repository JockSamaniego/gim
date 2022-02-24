/**
 * 
 */
package org.gob.gim.coercive.action;

import java.util.ArrayList;
import java.util.List;

import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.pagination.NotificationInfractionsDataModel;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;

/**
 * @author René
 *
 */

@Name("notificationInfractionListHome")
@Scope(ScopeType.CONVERSATION)
public class NotificationInfractionListHome extends EntityController{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NotificationInfractionSearchCriteria criteria;
	
	private List<Long> generatedNotificationIds = new ArrayList<Long>();
	
	/**
	 * 
	 */
	public NotificationInfractionListHome() {
		super();
		this.criteria = new NotificationInfractionSearchCriteria();
		this.search();
	}

	public NotificationInfractionSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(NotificationInfractionSearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	/**
	 * @return the generatedNotificationIds
	 */
	public List<Long> getGeneratedNotificationIds() {
		return generatedNotificationIds;
	}

	/**
	 * @param generatedNotificationIds the generatedNotificationIds to set
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
	
	public String prepareRePrint(Long notificationId){
		
		this.generatedNotificationIds = new ArrayList<Long>();
		this.generatedNotificationIds.add(notificationId);
		
		return "sendToPrint";
	}
	
	
	
}
