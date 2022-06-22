/**
 * 
 */
package org.gob.gim.coercive.pagination;

import java.util.List;

import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.service.NotificationInfractionsService;
import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import ec.gob.gim.coercive.model.infractions.NotificationInfractions;

/**
 * @author René
 *
 */
@Name("notificationsInfractions")
@Scope(ScopeType.CONVERSATION)
public class NotificationInfractionsDataModel extends
		PageableDataModel<NotificationInfractions, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Criteria
	private NotificationInfractionSearchCriteria criteria;

	private NotificationInfractionsService notificationInfractionsService;

	/**
	 * 
	 */
	public NotificationInfractionsDataModel() {
		super();
		this.initializeService();
		// TODO Auto-generated constructor stub
	}

	public void initializeService() {
		if (notificationInfractionsService == null) {
			notificationInfractionsService = ServiceLocator.getInstance()
					.findResource(notificationInfractionsService.LOCAL_NAME);
		}
	}

	/**
	 * @return the criteria
	 */
	public NotificationInfractionSearchCriteria getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(NotificationInfractionSearchCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Long getId(NotificationInfractions object) {
		return object.getId();
	}

	@Override
	public List<NotificationInfractions> findObjects(int firstRow,
			int numberOfRows, String sortField, boolean descending) {
		List<NotificationInfractions> notifications = notificationInfractionsService.findNotificationInfractionByCriteria(criteria, firstRow, numberOfRows);
		return notifications;
	}

	@Override
	public NotificationInfractions getObjectById(Long id) {
		return this.notificationInfractionsService.findObjectById(id);
	}

	@Override
	public String getDefaultSortField() {
		return "attribute(EOD)";
	}

	@Override
	public int getObjectsNumber() {
		return notificationInfractionsService.findNotificationInfractionsNumber(criteria)
				.intValue();
	}

	@Override
	public int getRowCount() {
		return rowCount.intValue();
	}
	
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

}
