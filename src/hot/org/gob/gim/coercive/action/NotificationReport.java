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
	
	//macartuche 2019-11-26
	List<Notification> notificationsNotPayed;
	List<Notification> notificationsPayed;
	
	
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
	
	
	@SuppressWarnings("unchecked")
	public List<Notification> getBondsNotPayed(Long notificationId){				
		String strQuery = "select mb from MunicipalBond mb "
				+ "WHERE mb.notification.id=:id "
				+ "AND mb.municipalBondStatus.id not in (6,11)";
				
		Query q = this.getEntityManager().createQuery(strQuery);		
		q.setParameter("id", notificationId);
		List<Notification> notPayed = (List<Notification>)q.getResultList(); 
		return notPayed;			
	}
	
	public boolean hasPendingPayment(Long notificationId) {
		String strQuery = "select count(mb) from MunicipalBond mb " 
				+ "WHERE mb.notification.id=:id " 
				+ "AND mb.municipalBondStatus.id not in (6,11)";
		Query q = this.getEntityManager().createQuery(strQuery);	
		q.setParameter("id", notificationId);
		Long total = (Long) q.getSingleResult();		
		return (total.intValue() > 0);
	}
	
	public boolean hasPayedBonds(Long notificationId) {
		
		
		String strQuery = "select count(mb) from MunicipalBond mb " 
				+ "WHERE mb.notification.id=:id " 
				+ "AND mb.municipalBondStatus.id in (6,11)";
		Query q = this.getEntityManager().createQuery(strQuery);	
		q.setParameter("id", notificationId);
		Long total = (Long) q.getSingleResult();		
		return (total.intValue() > 0);
		
		
		
	}
	
	private List<MunicipalBond> payedBonds=new ArrayList<MunicipalBond>();
	private List<MunicipalBond> notPayedBonds=new ArrayList<MunicipalBond>();
	private boolean isPending= Boolean.FALSE;
	private boolean payed= Boolean.FALSE;
	
	public boolean hasPendingPayment(Notification notification) {
		isPending= false;
		payed=false;
		
		payedBonds.clear();
		notPayedBonds.clear();
		
		for(MunicipalBond mb: notification.getMunicipalBonds()) {
			if (mb.getMunicipalBondStatus().getId()==6 || 
					mb.getMunicipalBondStatus().getId()==11) {
				payed=true;
				payedBonds.add(mb);
			}else {
				isPending=true;
				notPayedBonds.add(mb);
			}			
		}
		
		return isPending;
 	
	}
	

	
	@SuppressWarnings("unchecked")
	public List<Notification> getBondsPayed(Long notificationId){
		String strQuery = "select mb from MunicipalBond mb "
				+ "WHERE mb.notification.id=:id "
				+ "AND mb.municipalBondStatus.id in (6,11)";
		
		Query q = this.getEntityManager().createQuery(strQuery);
		q.setParameter("id", notificationId);
		
		List<Notification> payed = (List<Notification>)q.getResultList();
		System.out.println("pagadas");
		System.out.println(getSelectedItemAsList());
		System.out.println(payed.size());
		return payed ;
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

	public List<Notification> getNotificationsNotPayed() {
		return notificationsNotPayed;
	}

	public void setNotificationsNotPayed(List<Notification> notificationsNotPayed) {
		this.notificationsNotPayed = notificationsNotPayed;
	}

	public List<Notification> getNotificationsPayed() {
		return notificationsPayed;
	}

	public void setNotificationsPayed(List<Notification> notificationsPayed) {
		this.notificationsPayed = notificationsPayed;
	}

	public List<MunicipalBond> getPayedBonds() {
		return payedBonds;
	}

	public void setPayedBonds(List<MunicipalBond> payedBonds) {
		this.payedBonds = payedBonds;
	}

	public List<MunicipalBond> getNotPayedBonds() {
		return notPayedBonds;
	}

	public void setNotPayedBonds(List<MunicipalBond> notPayedBonds) {
		this.notPayedBonds = notPayedBonds;
	}

	public boolean isPending() {
		return isPending;
	}

	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}

	public boolean isPayed() {
		return payed;
	}

	public void setPayed(boolean payed) {
		this.payed = payed;
	}
  
}
