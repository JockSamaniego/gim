package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.gob.gim.common.Util;
import org.gob.gim.common.action.ResidentHome;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.income.action.PaymentHome;
import org.gob.gim.income.view.MunicipalBondItem;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.coercive.model.Notification;
import ec.gob.gim.coercive.model.NotificationTaskType;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.MunicipalBondType;

@Name("notificationList")
public class NotificationList extends EntityQuery<Notification> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7829139376689892812L;

	//private static final String EJBQL = "select notification from Notification notification left join fetch notification.notificationTasks nt";
	private static final String EJBQL = "select notification from Notification notification ";

	private static final String[] RESTRICTIONS = {
		"notification.notified.id IN (#{notificationList.selectedItemAsList.isEmpty() ? null : notificationList.selectedItemAsList})",
		"(nt.notificationTaskType = #{notificationList.notificationTaskType})", 
		"((lower(notification.notified.name) like lower(concat(#{notificationList.criteria},'%'))) " +
		"or (lower(notification.notified.identificationNumber) like lower(concat(:el3,'%'))) " +
		"or (concat(notification.year,'-',notification.number) = :el3 )"+
		//"or (lower(notification.entry.name) like lower(concat(:el3,'%'))) " +
		"or (concat(notification.id,'') = :el3))"
		};

	private String criteria;

	private String selectedItems = "";

	private BigDecimal amount = new BigDecimal(0);

	private boolean allResidentsSelected = false;

	private Date expirationDate;

	protected MunicipalBondType municipalBondType = null;
	
	private NotificationTaskType notificationTaskType;  

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession;

	private Notification notification = new Notification();
	
	private Resident resident = null;

	public NotificationList() {
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

	/**
	 * @return the criteria
	 */
	public String getCriteria() {
		if (this.criteria == null){
			setCriteria("");
		}
		return criteria;
	}

	public void setSelectedItems(String selectedItems) {
		this.selectedItems = selectedItems;
	}

	public String getSelectedItems() {
		return selectedItems;
	}

	public List<Long> getSelectedItemAsList() {
		return org.gob.gim.common.Util
				.splitArrayString(this.getSelectedItems());
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public boolean isAllResidentsSelected() {
		return allResidentsSelected;
	}

	public void setAllResidentsSelected(boolean allResidentsSelected) {
		this.allResidentsSelected = allResidentsSelected;
	}

	public MunicipalBondType getMunicipalBondType() {
		return municipalBondType;
	}

	public void setMunicipalBondType(MunicipalBondType municipalBondType) {
		this.municipalBondType = municipalBondType;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Date getExpirationDate() {
		Calendar now = Calendar.getInstance();
		if (this.expirationDate == null) {
			setExpirationDate(now.getTime());
		}
		return expirationDate;
	}

	public String getOrderColumn() {
		return "notification.id";
	}

	/**
	 * Retorna la lista de instancias <tt>MunicipalBond</tt> que satisfacen el criterio de búsqueda
	 * @return
	 */
	public List<Long> getMunicipalBonds() {
		PaymentHome paymentHome = null;
		ResidentHome residentHome = null;
		List<Long> municipalBonds = new ArrayList<Long>();
		if (this.selectedItems.isEmpty()) {
			municipalBonds.clear();
			return municipalBonds;
		} 

		for (Long id : Util.splitArrayString(selectedItems)) {
			System.out.println("entra aquissssssssssssssss");
			paymentHome = new PaymentHome();
			paymentHome.setId(null);

			residentHome = new ResidentHome();
			residentHome.setId(id);

			paymentHome.build(residentHome.getInstance(), this.getCriteria(),
					this.getExpirationDate(), this.getAmount(),
					userSession.getFiscalPeriod());

			for (MunicipalBondItem mbi : paymentHome.getMunicipalBondItems()){
				municipalBonds.add(mbi.getMunicipalBond().getId());
			}
		}
		return municipalBonds;
	}

	public void setResident(Resident resident) {
		this.resident = resident;
	}

	public Resident getResident() {
		if (selectedItems.isEmpty()){
			return null;
		}
		if (this.resident == null){
			ResidentHome residentHome =  null;
			for (Long id : Util.splitArrayString(selectedItems)) {
				residentHome = new ResidentHome();
				residentHome.setId(id);
				setResident(residentHome.getInstance());
			}
		}
		return resident;
	}

	public NotificationTaskType getNotificationTaskType() {
		return notificationTaskType;
	}

	public void setNotificationTaskType(NotificationTaskType notificationTaskType) {
		this.notificationTaskType = notificationTaskType;
	}

}
