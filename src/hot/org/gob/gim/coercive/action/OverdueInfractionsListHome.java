/**
 * 
 */
package org.gob.gim.coercive.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.gob.gim.coercive.dto.InfractionItemDTO;
import org.gob.gim.coercive.dto.criteria.OverdueInfractionsSearchCriteria;
import org.gob.gim.coercive.pagination.OverdueInfractionsDataModel;
import org.gob.gim.coercive.service.DatainfractionService;
import org.gob.gim.coercive.service.NotificationInfractionsService;
import org.gob.gim.coercive.service.OverdueInfractionsService;
import org.gob.gim.coercive.view.InfractionItem;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.Util;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.HistoryStatusNotification;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.revenue.model.MunicipalBond;

/**
 * @author René
 *
 */
@Name("overdueInfractionsListHome")
@Scope(ScopeType.CONVERSATION)
public class OverdueInfractionsListHome extends
		EntityHome<NotificationInfractions> {
	
	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Long> generatedNotificationIds = new ArrayList<Long>();

	private String selectedItems;

	private List<InfractionItemDTO> selectedList;

	private OverdueInfractionsSearchCriteria criteria;

	@In(required = true, create = true)
	OverdueInfractionsList overdueInfractionsList;

	private DatainfractionService datainfractionService;

	private ItemCatalogService itemCatalogService;

	private OverdueInfractionsService overdueInfractionsService;
	
	private NotificationInfractionsService notificationInfractionsService;

	private boolean allResidentsSelected = false;

	private boolean rebuiltRequired = false;

	private Integer totalSync = new Integer(0);

	/**
	 * 
	 */
	public OverdueInfractionsListHome() {
		super();
		this.initializeService();
		this.criteria = new OverdueInfractionsSearchCriteria();
		this.totalSync = this.overdueInfractionsService
				.getTotalSyncInfractions();
		this.search();
	}

	public void initializeService() {
		if (datainfractionService == null) {
			datainfractionService = ServiceLocator.getInstance().findResource(
					DatainfractionService.LOCAL_NAME);
		}
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}
		if (overdueInfractionsService == null) {
			overdueInfractionsService = ServiceLocator.getInstance()
					.findResource(OverdueInfractionsService.LOCAL_NAME);
		}
		
		if (notificationInfractionsService == null) {
			notificationInfractionsService = ServiceLocator.getInstance()
					.findResource(NotificationInfractionsService.LOCAL_NAME);
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

	/**
	 * @return the criteria
	 */
	public OverdueInfractionsSearchCriteria getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(OverdueInfractionsSearchCriteria criteria) {
		this.criteria = criteria;
	}

	/**
	 * @return the selectedList
	 */
	public List<InfractionItemDTO> getSelectedList() {
		return selectedList;
	}

	/**
	 * @param selectedList
	 *            the selectedList to set
	 */
	public void setSelectedList(List<InfractionItemDTO> selectedList) {
		this.selectedList = selectedList;
	}

	/**
	 * @return the rebuiltRequired
	 */
	public boolean isRebuiltRequired() {
		return rebuiltRequired;
	}

	/**
	 * @param rebuiltRequired
	 *            the rebuiltRequired to set
	 */
	public void setRebuiltRequired(boolean rebuiltRequired) {
		this.rebuiltRequired = rebuiltRequired;
	}

	/**
	 * @return the totalSync
	 */
	public Integer getTotalSync() {
		return totalSync;
	}

	/**
	 * @param totalSync
	 *            the totalSync to set
	 */
	public void setTotalSync(Integer totalSync) {
		this.totalSync = totalSync;
	}

	public String confirmPrinting() {
		return "printed";
	}

	@Transactional
	public String createNotification() throws IOException {
		this.generatedNotificationIds = new ArrayList<Long>();

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

		ItemCatalog itemPending = this.itemCatalogService
				.findItemByCodeAndCodeCatalog("CATALOG_STATUS_INFRACTIONS",
						"NOTIFIED");
		
		ItemCatalog itemPrint = this.itemCatalogService
				.findItemByCodeAndCodeCatalog("CAT_STATUS_NOTIF_INFRACCCIONS",
						"PRINT");

		for (String identification : getResidentSelectedItems()) { // son los
																	// id's de
																	// todos
			// los resident's

			List<Datainfraction> infractions = this.datainfractionService
					.findInfractionsByIdentification(identification);

			if (infractions.size() == 0) {
				overdueInfractionsList.searchBonds();
				addFacesMessage("Recargue la página");
				return "failed";
			}
			

			notification = new NotificationInfractions();
			notification.setStatus(itemPrint);
			notification.setInfractions(infractions);
			notification.setNumber(this.datainfractionService.getNextValue()
					.intValue());
			notification.setYear(Calendar.getInstance().get(Calendar.YEAR));
			for (int i = 0; i < infractions.size(); i++) {
				infractions.get(i).setState(itemPending);
				infractions.get(i).setNotification(notification);
			}
			setInstance(notification);
			persist();
			
			
			HistoryStatusNotification record = new HistoryStatusNotification();
			record.setDate(new Date());
			record.setNotification(this.instance);
			record.setObservation("Generada por usuario");
			record.setResponsible(this.userSession.getPerson());
			record.setStatus(itemPrint);
			record.setUser(this.userSession.getUser());
			
			notificationInfractionsService.saveHistoryStatus(record);
			
			generatedNotificationIds.add((Long) getId());

		}

		Contexts.removeFromAllContexts("overdueInfractionsList");
		overdueInfractionsList.refresh();

		overdueInfractionsList.searchBonds();

		// this.reload();

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
		// System.out.println(selectedItems);
		return selectedItems;
	}

	public void search() {
		getDataModel().setCriteria(this.criteria);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());
	}

	private OverdueInfractionsDataModel getDataModel() {

		OverdueInfractionsDataModel dataModel = (OverdueInfractionsDataModel) Component
				.getInstance(OverdueInfractionsDataModel.class, true);

		return dataModel;
	}

	public void selectAllResidentItems() {
		for (InfractionItemDTO dto : getDataModel().getItems()) {
			dto.setSelected(allResidentsSelected);
		}
	}

	public void setAllResidentsSelected(boolean allResidentsSelected) {
		this.allResidentsSelected = allResidentsSelected;
	}

	public boolean isAllResidentsSelected() {
		return allResidentsSelected;
	}

	private void fillSelectedList(List<InfractionItemDTO> list) {
		if (selectedList == null)
			selectedList = new ArrayList<InfractionItemDTO>();
		for (InfractionItemDTO ri : list) {
			selectedList.add(ri);
		}
	}

	public void addResidentItem(InfractionItemDTO ri) {
		if (allResidentsSelected && !ri.isSelected())
			allResidentsSelected = Boolean.FALSE;
		if (selectedList == null)
			selectedList = new ArrayList<InfractionItemDTO>();

		if (ri.isSelected()) {
			selectedList.add(ri);
		} else {
			selectedList.remove(ri);
		}

	}

	public void changeSelectedItem(InfractionItemDTO item, boolean selected) {
		System.out.println("Llega al changeSelectedItem");
		item.setSelected(!selected);
		for (InfractionItemDTO itm : getDataModel().getItems()) {
			System.out.println("[" + itm.getName() + ": " + itm.isSelected()
					+ "]");
		}
	}

	public void printSelects() {
		// System.out.println(this.getDataModel().getItems());
		for (InfractionItemDTO item : getDataModel().getItems()) {
			System.out.println("[" + item.getName() + ": " + item.isSelected()
					+ "]");
		}
	}

	public void reload() throws IOException {
		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	public void changeStatusSelecteds() {
		this.generatedNotificationIds = new ArrayList<Long>();

		if (this.getResidentSelectedItems().isEmpty()) {
			addFacesMessageFromResourceBundle("common.noSelectedItems");
			//return "failed";
		}

		setSelectedItems(Util.listToString(this.getResidentSelectedItems()));

		/*
		 * this.setExpirationDate(residentWithMunicipalBondOutOfDateList
		 * .getExpirationDate());
		 * this.setAmount(residentWithMunicipalBondOutOfDateList.getAmount());
		 */

		NotificationInfractions notification = null;

		ItemCatalog itemPending = this.itemCatalogService
				.findItemByCodeAndCodeCatalog("CATALOG_STATUS_INFRACTIONS",
						"NOTIFIED");

		for (String identification : getResidentSelectedItems()) { // son los
																	// id's de
																	// todos
		}

		Contexts.removeFromAllContexts("overdueInfractionsList");
		overdueInfractionsList.refresh();

		overdueInfractionsList.searchBonds();
	}

}
