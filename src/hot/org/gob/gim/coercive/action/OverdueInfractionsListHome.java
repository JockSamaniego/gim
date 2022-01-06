/**
 * 
 */
package org.gob.gim.coercive.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.gob.gim.coercive.service.DatainfractionService;
import org.gob.gim.coercive.view.InfractionItem;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.Util;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;

/**
 * @author René
 *
 */
@Name("overdueInfractionsListHome")
@Scope(ScopeType.CONVERSATION)
public class OverdueInfractionsListHome extends
		EntityHome<NotificationInfractions> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Long> generatedNotificationIds = new ArrayList<Long>();

	private String selectedItems;
	
	@In(required = true, create = true)
	OverdueInfractionsList overdueInfractionsList;

	private DatainfractionService datainfractionService;
	
	/**
	 * 
	 */
	public OverdueInfractionsListHome() {
		super();
		this.initializeService();
	}

	public void initializeService() {
		if (datainfractionService == null) {
			datainfractionService = ServiceLocator.getInstance().findResource(
					DatainfractionService.LOCAL_NAME);
		}
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

	/**
	 * @return the selectedItems
	 */
	public String getSelectedItems() {
		return selectedItems;
	}

	/**
	 * @param selectedItems
	 *            the selectedItems to set
	 */
	public void setSelectedItems(String selectedItems) {
		this.selectedItems = selectedItems;
	}

	public String confirmPrinting() {
		return "printed";
	}

	public String createNotification() throws IOException {

		if (this.getResidentSelectedItems().isEmpty()) {
			addFacesMessageFromResourceBundle("common.noSelectedItems");
			return "failed";
		}

		setSelectedItems(Util.listToString(this.getResidentSelectedItems()));

		/*
		 * this.setExpirationDate(residentWithMunicipalBondOutOfDateList
		 * .getExpirationDate());
		 * this.setAmount(residentWithMunicipalBondOutOfDateList.getAmount());
		 */

		NotificationInfractions notification = null;

		for (String identification : getResidentSelectedItems()) { // son los id's de todos
														// los resident's
			
			List<Datainfraction> infractions = this.datainfractionService.findInfractionsByIdentification(identification);
			
			System.out.println(infractions);

			notification = new NotificationInfractions();
			notification.setInfractions(infractions);
			notification.setNumber(this.datainfractionService.getNextValue().intValue());
			notification.setYear(Calendar.getInstance().get(Calendar.YEAR));
			
			setInstance(notification);

			persist();
			generatedNotificationIds.add((Long) getId());
		}

		return "sendToPrint";
	}

	/**
	 * Devuelve los ids de los residents que han sido seleccionados en la lista
	 * de notificaciones
	 * 
	 * @return List<Long>
	 */
	public List<String> getResidentSelectedItems() {
		List<String> selectedItems = new ArrayList<String>();
		overdueInfractionsList.setAllResidentsSelected(Boolean.FALSE);
		if (overdueInfractionsList.getSelectedList() == null
				|| overdueInfractionsList.getSelectedList().size() == 0)
			return selectedItems;
		for (InfractionItem ri : overdueInfractionsList.getSelectedList()) {
			if (ri.isSelected()) {
				if (!selectedItems.contains(ri.getIdentification()))
					selectedItems.add(ri.getIdentification());
			}
		}
		return selectedItems;
	}

}
