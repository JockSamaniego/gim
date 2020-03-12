package org.gob.gim.coercive.action;

import java.util.ArrayList;
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
import ec.gob.gim.revenue.model.MunicipalBond;

import javax.persistence.Query;
import javax.sound.midi.Soundbank;

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
	
	private String MUNICIPAL_BOND_STATUS_ID_PENDING = "MUNICIPAL_BOND_STATUS_ID_PENDING";
	private String MUNICIPAL_BOND_STATUS_ID_AGREEMENT = "MUNICIPAL_BOND_STATUS_ID_AGREEMENT";
	private String MUNICIPAL_BOND_STATUS_ID_SUBSCRIPTION = "MUNICIPAL_BOND_STATUS_ID_SUBSCRIPTION";
	private String MUNICIPAL_BOND_STATUS_ID_COMPENSATION = "MUNICIPAL_BOND_STATUS_ID_COMPENSATION";
	private Long pendingStatus;
	private Long agreementStatus;
	private Long compensationStatus;
	private Long subscriptionStatus;
	
	
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
		if (notifications==null) 
			notifications = super.getResultList();		
		depureBonds();
//		if(notifications != null && notifications.size() > 0) return notifications;		
		return notifications;
	}	
	
	public NotificationReport() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		
		if (systemParameterService == null){
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		}
		pendingStatus = systemParameterService.findParameter(MUNICIPAL_BOND_STATUS_ID_PENDING);
		agreementStatus = systemParameterService.findParameter(MUNICIPAL_BOND_STATUS_ID_AGREEMENT);
		compensationStatus = systemParameterService.findParameter(MUNICIPAL_BOND_STATUS_ID_COMPENSATION);
		subscriptionStatus = systemParameterService.findParameter(MUNICIPAL_BOND_STATUS_ID_SUBSCRIPTION);
		 
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

	/**
	 * Depurar la lista a imprimir de obligaciones
	 * @return
	 */
	public void depureBonds() {
		if (this.notifications==null) return;
		
		List<Long> permitStates = new ArrayList<>();
		permitStates.add(pendingStatus);
		permitStates.add(compensationStatus);
		permitStates.add(agreementStatus);
		permitStates.add(subscriptionStatus);
		
		for (Notification notification : this.notifications) {
			List<MunicipalBond> bonds = notification.getMunicipalBonds();
			List<MunicipalBond> bondsTOPrint = new ArrayList<MunicipalBond>();
			for (MunicipalBond bond : bonds) {
				if (permitStates.contains(bond.getMunicipalBondStatus().getId())) {
					bondsTOPrint.add(bond);
				}
			}
			notification.setMunicipalBonds(bondsTOPrint);
		}
		
	}
}
