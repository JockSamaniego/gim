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
import ec.gob.gim.coercive.model.infractions.NotificationInfractionsDTO;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.loja.middleapp.ResponseInfraccion;

/**
 * @author René
 *
 */
@Name("overdueInfractionsListHome")
@Scope(ScopeType.CONVERSATION)
public class OverdueInfractionsListHome extends	EntityHome<NotificationInfractions> {
	
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
	
	private Boolean notItems = Boolean.FALSE;
	private List<NotificationInfractionsDTO> notificationsToCreated;
	private int totalResidentsSelected = 0;
	private int totalInfractionsSelected = 0;
	private List<String> selectedItemsList;
	private Boolean isFirstTime = Boolean.TRUE;
	private NotificationInfractions notification;

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
	
	public void wire(){
		if (isFirstTime){
			overdueInfractionsList.setSelectedList(new ArrayList<InfractionItem>());
			notificationsToCreated = new ArrayList<NotificationInfractionsDTO>();
			isFirstTime = Boolean.FALSE;
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
	
	

	public Boolean getNotItems() {
		return notItems;
	}

	public void setNotItems(Boolean notItems) {
		this.notItems = notItems;
	}

	public List<NotificationInfractionsDTO> getNotificationsToCreated() {
		return notificationsToCreated;
	}

	public void setNotificationsToCreated(
			List<NotificationInfractionsDTO> notificationsToCreated) {
		this.notificationsToCreated = notificationsToCreated;
	}

	public int getTotalResidentsSelected() {
		return totalResidentsSelected;
	}

	public void setTotalResidentsSelected(int totalResidentsSelected) {
		this.totalResidentsSelected = totalResidentsSelected;
	}

	public List<String> getSelectedItemsList() {
		return selectedItemsList;
	}

	public void setSelectedItemsList(List<String> selectedItemsList) {
		this.selectedItemsList = selectedItemsList;
	}

	public int getTotalInfractionsSelected() {
		return totalInfractionsSelected;
	}

	public void setTotalInfractionsSelected(int totalInfractionsSelected) {
		this.totalInfractionsSelected = totalInfractionsSelected;
	}

	public NotificationInfractions getNotification() {
		return notification;
	}

	public void setNotification(NotificationInfractions notification) {
		this.notification = notification;
	}

	@Transactional
	public void createNotification() throws IOException {
		notificationsToCreated = new ArrayList<NotificationInfractionsDTO>();
		selectedItemsList = this.getResidentSelectedItems();
		totalInfractionsSelected = 0;
		if (selectedItemsList.isEmpty()) {
			notItems = Boolean.TRUE;
			overdueInfractionsList.searchBonds();
		} else {		
			setSelectedItems(Util.listToString(selectedItemsList));
			for (String identification : selectedItemsList) { 

				List<Datainfraction> infractions = this.datainfractionService
						.findInfractionsPendingByIdentification(identification);
	
				if (infractions.size() > 0) {
					NotificationInfractionsDTO notificationDTO = new NotificationInfractionsDTO();
					notificationDTO.setPendingInfractions(infractions);
					notificationDTO.setIdentificationNumber(identification);
					notificationDTO.setName(infractions.get(0).getName());
					totalInfractionsSelected = totalInfractionsSelected + infractions.size();
					this.notificationsToCreated.add(notificationDTO);
				}
			}
			
		}
	}
	
	@Transactional
	public void confirmCreateNotification() throws IOException {
		notification = null;
		this.generatedNotificationIds = new ArrayList<Long>();
		ItemCatalog itemNotificated = this.itemCatalogService
				.findItemByCodeAndCodeCatalog("CATALOG_STATUS_INFRACTIONS",
						"NOTIFIED");
		
		ItemCatalog itemPrint = this.itemCatalogService
				.findItemByCodeAndCodeCatalog("CAT_STATUS_NOTIF_INFRACCCIONS",
						"PRINT");
		
		for(NotificationInfractionsDTO notificationDTO : notificationsToCreated){
			notification = new NotificationInfractions();
			notification.setStatus(itemPrint);
			notification.setInfractions(notificationDTO.getPendingInfractions());
			notification.setNumber(this.datainfractionService.getNextValue()
					.intValue());
			notification.setYear(Calendar.getInstance().get(Calendar.YEAR));
			setInstance(notification);
			super.persist();
			
			for (int i = 0; i < notification.getInfractions().size(); i++) {
				Datainfraction data = notification.getInfractions().get(i);
				data.setState(itemNotificated);
				data.setNotification(notification);
				datainfractionService.updateDataInfraction(data);
				
			}
			
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
	}
	
	public String requestInfractionCurrentStatus(String id_factura){
		if(id_factura == null || id_factura.equals("")){
			return "No se pudo consultar";
		}else{
			ResponseInfraccion responseInfraccion = this.datainfractionService.findInfractionByIdANT(id_factura);
			if(responseInfraccion != null && responseInfraccion.getCode() == 200){
				if(responseInfraccion.getInfraccion() != null){
					return responseInfraccion.getInfraccion().getEstado();
				}
				return "No se pudo consultar";
			}
			return "No se pudo consultar";
		}
	}

	/**
	 * Devuelve los ids de los residents que han sido seleccionados en la lista
	 * de notificaciones
	 * 
	 * @return List<Long>
	 */
	public List<String> getResidentSelectedItems() {
		selectedItemsList = new ArrayList<String>();
		overdueInfractionsList.setAllResidentsSelected(Boolean.FALSE);
		if (overdueInfractionsList.getSelectedList() != null
				&& overdueInfractionsList.getSelectedList().size() > 0){
			for (InfractionItem ri : overdueInfractionsList.getSelectedList()) {
				if (ri.isSelected()) {
					if (!selectedItemsList.contains(ri.getIdentification()))
						selectedItemsList.add(ri.getIdentification());
				}
			}
		}
		// System.out.println(selectedItems);
		this.totalResidentsSelected = selectedItemsList.size();
		return selectedItemsList;
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
		selectedItemsList = this.getResidentSelectedItems();
		this.generatedNotificationIds = new ArrayList<Long>();

		if (selectedItemsList.isEmpty()) {
			addFacesMessageFromResourceBundle("common.noSelectedItems");
			//return "failed";
		}

		setSelectedItems(Util.listToString(selectedItemsList));

		/*
		 * this.setExpirationDate(residentWithMunicipalBondOutOfDateList
		 * .getExpirationDate());
		 * this.setAmount(residentWithMunicipalBondOutOfDateList.getAmount());
		 */

		NotificationInfractions notification = null;

		ItemCatalog itemPending = this.itemCatalogService
				.findItemByCodeAndCodeCatalog("CATALOG_STATUS_INFRACTIONS",
						"NOTIFIED");

		for (String identification : selectedItemsList) { // son los
																	// id's de
																	// todos
		}

		Contexts.removeFromAllContexts("overdueInfractionsList");
		overdueInfractionsList.refresh();

		overdueInfractionsList.searchBonds();
	}

}
