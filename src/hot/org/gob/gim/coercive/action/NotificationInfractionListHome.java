/**
 * 
 */
package org.gob.gim.coercive.action;

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
	
	public void search() {
		getDataModel().setCriteria(this.criteria);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());
	}
	
	private NotificationInfractionsDataModel getDataModel() {

		NotificationInfractionsDataModel dataModel = (NotificationInfractionsDataModel) Component
				.getInstance(NotificationInfractionsDataModel.class, true);

		return dataModel;
	}
	
}
