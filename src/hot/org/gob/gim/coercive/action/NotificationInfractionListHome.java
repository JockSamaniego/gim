/**
 * 
 */
package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.pagination.NotificationInfractionsDataModel;
import org.gob.gim.coercive.service.NotificationInfractionsService;
import org.gob.gim.coercive.service.DatainfractionService;
import org.gob.gim.coercive.view.InfractionUserData;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;

import ec.gob.gim.coercive.model.Notification;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;
import ec.gob.gim.coercive.model.infractions.PaymentNotification;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.income.model.PaymentFraction;
import ec.gob.gim.income.model.PaymentType;
import ec.gob.gim.revenue.model.FinancialInstitution;
import ec.gob.gim.revenue.model.FinancialInstitutionType;
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
public class NotificationInfractionListHome extends EntityController{
	
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
	
	private List<PaymentNotification> payments = null;
	private DatainfractionService datainfractionService; 
	private InfractionUserData userData = null;
	private List<PaymentNotification> newPayments = new ArrayList<PaymentNotification>();
	private List<ItemCatalog> paymentTypes = new ArrayList<ItemCatalog>();
	private BigDecimal change = BigDecimal.ZERO;
	private BigDecimal totalToPay = BigDecimal.ZERO;
	private List<FinancialInstitution> banks;
	private List<FinancialInstitution> stateBanks;
	private List<FinancialInstitution> creditCardEmitors;
	private Boolean invalidAmount = Boolean.TRUE;
	private NotificationInfractions notificationSelected=null;
	private final String PAYMENTS_TYPE_CATALOG = "PAYMENT_TYPES";

	@In(create = true)
	UserSession userSession;
	
	/**
	 * 
	 */
	public NotificationInfractionListHome() {
		super();
		this.criteria = new NotificationInfractionSearchCriteria();
		this.initializeService();
		this.statuses = this.itemCatalogService
				.findItemsForCatalogCodeOrderById("CAT_STATUS_NOTIF_INFRACCCIONS");
		//macartuche
		this.paymentTypes = itemCatalogService.findItemsForCatalogCode(PAYMENTS_TYPE_CATALOG);
		//
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
	
	//=================================================
	//MACARTUCHE
	/**
	 * Ver abonos
	 * @param notificationId
	 * @return
	 */
	public void viewPayments(Long notificationId){		
		
		if (datainfractionService == null) {
			datainfractionService = ServiceLocator.getInstance().findResource(
					datainfractionService.LOCAL_NAME);
		}
	 
		this.userData = this.datainfractionService.userData(notificationId);
		this.payments = this.datainfractionService.findPaymentsByNotification(notificationId);	
		this.notificationSelected = notificationInfractionsService.findObjectById(notificationId);
		
		//consultar los tipos de pago de catalogo NO ENUMERADOR
		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(itemCatalogService.LOCAL_NAME);
		}
		ItemCatalog CASH = itemCatalogService.findItemByCodeAndCodeCatalog(PAYMENTS_TYPE_CATALOG, "CASH");
		this.loadLists();
		
		PaymentNotification payment = new PaymentNotification();
		payment.setPaymentType(CASH);
		payment.setValue(BigDecimal.ZERO);
		this.newPayments.add(payment);
		
		this.invalidAmount=Boolean.TRUE;
		
	}
	
	public List<FinancialInstitution> getFinantialInstitutions(ItemCatalog paymentType) {
		if(paymentType == null){
			return new ArrayList<FinancialInstitution>();
		}
		
		System.out.println(PaymentType.CHECK.name());
		if (paymentType.getCode().equals(PaymentType.CHECK.name().toString())) {
			return banks;
		} else {
			if (paymentType.getCode() == PaymentType.CREDIT_CARD.name()) {
				return creditCardEmitors;
			} else {
				if (paymentType.getCode() == PaymentType.TRANSFER.name()) {
					return stateBanks;
				}
			}
		}
		return new ArrayList<FinancialInstitution>();
	}

	private void loadLists() {
		if (banks == null)
			banks = findFinantialInstitutions(FinancialInstitutionType.BANK);
		if (creditCardEmitors == null)
			creditCardEmitors = findFinantialInstitutions(FinancialInstitutionType.CREDIT_CARD_EMISOR);
		if (stateBanks == null)
			stateBanks = findFinantialInstitutions(FinancialInstitutionType.STATE_BANK); 
	}
	
	@SuppressWarnings("unchecked")
	private List<FinancialInstitution> findFinantialInstitutions(FinancialInstitutionType finantialInstitutionType) {
		Query query = getPersistenceContext().createNamedQuery("FinancialInstitution.findByType");
		query.setParameter("type", finantialInstitutionType);
		return query.getResultList();
	}
	
	public void clearValues(PaymentNotification fraction) {
		// System.out.println("VALUES CLEARED");
		fraction.setValue(BigDecimal.ZERO);
		fraction.setFinantialInstitution(null);
		fraction.setDocumentNumber(null);
		fraction.setAccountNumber(null);
		calculateChange();
	}

	
	/**
	 * Quitar fraccion-abono
	 */
	public void removePayment(PaymentNotification payment){
		this.getNewPayments().remove(payment);
		
	}

	/**
	 * Nueva fraccion-abono
	 */
	public void addnewPayment() {
		this.getNewPayments().add(new PaymentNotification());
	}
	
	public void calculateChange(){
		//TO-DO
		System.out.println("TO-DO");

		this.invalidAmount = Boolean.FALSE;
	}
	
	@Transactional
	public void savePayment(){
		
		if(notificationSelected==null){
			return;
		}
		
		//recorrer todas los abonos		
		for (PaymentNotification payment : this.newPayments) {
			payment.setCashier(userSession.getUser());
			payment.setNotification(this.notificationSelected);
			this.datainfractionService.savePaymentNotification(payment);
		}
	}
	
	public List<PaymentNotification> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentNotification> payments) {
		this.payments = payments;
	}

	public InfractionUserData getUserData() {
		return userData;
	}

	public void setUserData(InfractionUserData userData) {
		this.userData = userData;
	}

	public List<PaymentNotification> getNewPayments() {
		return newPayments;
	}

	public void setNewPayments(List<PaymentNotification> newPayments) {
		this.newPayments = newPayments;
	}

	public List<ItemCatalog> getPaymentTypes() {
		return paymentTypes;
	}

	public void setPaymentTypes(List<ItemCatalog> paymentTypes) {
		this.paymentTypes = paymentTypes;
	}

	public BigDecimal getChange() {
		return change;
	}

	public void setChange(BigDecimal change) {
		this.change = change;
	}

	public BigDecimal getTotalToPay() {
		return totalToPay;
	}

	public void setTotalToPay(BigDecimal totalToPay) {
		this.totalToPay = totalToPay;
	}

	public Boolean getInvalidAmount() {
		return invalidAmount;
	}

	public void setInvalidAmount(Boolean invalidAmount) {
		this.invalidAmount = invalidAmount;
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

	 

}
