package org.gob.gim.coercive.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.persistence.Query;

import org.gob.gim.coercive.view.ResidentItem;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.Util;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.coercive.model.Notification;
import ec.gob.gim.coercive.model.NotificationTask;
import ec.gob.gim.coercive.model.NotificationTaskType;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.Resident;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;

import java.math.BigInteger;
import java.util.LinkedHashMap;

import javax.persistence.NoResultException;

@Name("notificaterHome")
@Scope(ScopeType.CONVERSATION)
public class NotificaterHome extends EntityHome<Notification> {

	/**
	 * 	
	 */
	private static final long serialVersionUID = 1L;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	private static final String PENDING_BOND_STATUS_ID = "MUNICIPAL_BOND_STATUS_ID_PENDING";
	private static final String AGREEMENT_BOND_STATUS_ID = "MUNICIPAL_BOND_STATUS_ID_AGREEMENT";

	private SystemParameterService systemParameterService;

	@Logger
	Log logger;

	private NotificationTask notificationTask = null;

	@In(scope = ScopeType.SESSION, value = "userSession")
	UserSession userSession; // Fuente del actuary

	@In(create = true)
	private NotificationTaskHome notificationTaskHome;

	@In(create = true)
	NotificationTaskTypeList notificationTaskTypeList;

	@In(required = true, create = true)
	ResidentWithMunicipalBondOutOfDateList residentWithMunicipalBondOutOfDateList;

	@In
	FacesMessages facesMessages;

	private Date startDate;
	private Long daysToPayAfterNotification;
	private Date endDate;
	private Boolean withCopy = Boolean.TRUE;
	private Boolean isOriginal = Boolean.TRUE;
	private Long numberNotification;
	private String nameNotifier;
	private ArrayList nameNotifierAndNumberNotification;
	private Long pendingMunicipalBondStatusId;
	private String selectedItems;
	protected String criteria;
	private Long daysForOutOfDate = 0L;
	private BigDecimal amount;
	private Date expirationDate;
	private BigDecimal totalCollected;
	private BigDecimal totalNotificationGenerated;

	private Boolean isStateJuice = Boolean.FALSE;

	private Boolean isStateNotify = Boolean.FALSE;

	private String identificationNumber;

	private Person responsible = new Person();

	private List<Long> generatedNotificationIds = new ArrayList<Long>();

	private List<Resident> residents;

	protected String criteriaResponsible;

	private Boolean isRequiredResident = Boolean.FALSE;

	private Boolean isOptionOne = Boolean.FALSE;

	public ArrayList getNameNotifierAndNumberNotification() {
		return nameNotifierAndNumberNotification;
	}

	public void setNameNotifierAndNumberNotification(
			ArrayList nameNotifierAndNumberNotification) {
		this.nameNotifierAndNumberNotification = nameNotifierAndNumberNotification;
	}

	public Boolean getIsOptionOne() {
		return isOptionOne;
	}

	public void setIsOptionOne(Boolean isOptionOne) {
		this.isOptionOne = isOptionOne;
	}

	public Boolean printCopies() {
		setIsOriginal(Boolean.FALSE);
		return withCopy;
	}

	public void loadValues() {
		Calendar ca = Calendar.getInstance();
		startDate = ca.getTime();
		endDate = ca.getTime();
	}

	public void setNotificationId(Long id) {
		setId(id);
	}

	public Long getNotificationId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
		if (this.getNotificationTask() == null) {
			setNotificationTask(notificationTaskHome.createInstance()); // cargar
																		// con
																		// una
																		// nueva
																		// instancia
																		// hija
		}

	}

	public boolean isWired() {
		return true;
	}

	public Notification getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}

	private void getNumberOfCreatedNotifcations(Date startDate, Date endDate,
			Person person) {
		Query query = getEntityManager().createNamedQuery(
				"Notification.countCreatedBetweenDatesAndNotifitifier");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("notifierV", person.getId());

		try {
			Object[] i = (Object[]) query.getSingleResult();
			numberNotification = (Long) i[0];
			nameNotifier = (String) i[1];
			identificationNumber = (String) i[2];
		} catch (NoResultException ex) {
			numberNotification = 0L;
			nameNotifier = person.getName();
			identificationNumber = person.getIdentificationNumber();
			// ex.printStackTrace();
		}
		totalNotificationGenerated = BigDecimal.valueOf(numberNotification);

		//System.out.println("entrada2");
	}

	private void getNumberOfCreatedNotifcations(Date startDate, Date endDate) {
		Query query = getEntityManager().createNamedQuery(
				"Notification.countCreatedBetweenDatesAndWithoutNotifitifier");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		nameNotifierAndNumberNotification = new ArrayList<DataModelConsultNotification>();
		try {
			totalNotificationGenerated = BigDecimal.ZERO;
			List i = query.getResultList();
			for (Object v : i) {
				Object[] vl = (Object[]) v;

				nameNotifierAndNumberNotification
						.add(new DataModelConsultNotification((String) vl[2],
								(String) vl[1], (Long) vl[0]));
				totalNotificationGenerated = totalNotificationGenerated
						.add(BigDecimal.valueOf((Long) vl[0]));
			}

		} catch (NoResultException ex) {
			ex.printStackTrace();
		}

		//System.out.println("entrada2");
	}

	private Long getNumberOfDaysToPayAfterNotification() {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		Long daysToPay = systemParameterService
				.findParameter("DAYS_TO_PAY_AFTER_NOTIFICATION");
		return daysToPay;
	}

	private BigDecimal getTotalBondsValues(Date startDate, Date endDate) {
		Query query = getEntityManager().createNamedQuery(
				"Notification.sumBondValueBetweenDates");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		return (BigDecimal) query.getSingleResult();

	}

	private BigDecimal getTotalBondsPaidTotals(Date startDate, Date endDate) {
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		if (pendingMunicipalBondStatusId == null)
			pendingMunicipalBondStatusId = systemParameterService
					.findParameter(PENDING_BOND_STATUS_ID);
		Query query = getEntityManager().createNamedQuery(
				"Notification.sumBondPaidTotalBetweenDatesAndBondStatus");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("statusId", pendingMunicipalBondStatusId);
		return (BigDecimal) query.getSingleResult();

	}

	private Long getNumberOfCancelledNotifications(Date startDate, Date endDate) {

		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		Long notificationTaskTypeId = systemParameterService
				.findParameter("NOTIFICATION_TASK_TYPE_CANCELLED");
		Query query = getEntityManager().createNamedQuery(
				"Notificationtask.countForTypeBetweenDates");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("typeId", notificationTaskTypeId);
		return (Long) query.getSingleResult();

	}

	public String generateReportbyNotifier() {
        if(responsible != null && responsible.getId() != null && !responsible.getId().equals("")){
	getNumberOfCreatedNotifcations(startDate, endDate,responsible);
            isOptionOne=false;	
        }
        else{
            getNumberOfCreatedNotifcations(startDate, endDate);
             isOptionOne=true;
        }
	
	return "readyForPrint";
//            return "";
}
	

	public void loadBondsForDetail() {
		residentWithMunicipalBondOutOfDateList.setMunicipalBonds(this
				.getInstance().getMunicipalBonds());
	}

	public String addNotificationTask() {
		Date now = Calendar.getInstance().getTime();
		if (this.getNotificationTask().getId() == null) {
			// cerrar estado activo
			NotificationTask last = getLastNotificationTask(this.getInstance());
			last.setNotifiedDate(now);
			this.notificationTaskHome.setInstance(last);
			this.notificationTaskHome.update();

			// Almacenar nuevo estado
			this.getNotificationTask().setActuary(userSession.getPerson());
			if (this.isStateJuice) {
				this.notificationTask.setJudgmental(responsible);
			}

			if (this.isStateNotify) {
				this.notificationTask.setNotifier(responsible);
			}

			this.notificationTaskHome.setInstance(this.getNotificationTask());
			this.notificationTaskHome.persist();
			this.getInstance().add(this.getNotificationTask());
		} else {
			this.notificationTaskHome.setInstance(this.getNotificationTask());
			this.notificationTaskHome.update();
		}
		this.update();
		this.setIdentificationNumber(new String());
		this.setResponsible(null);
		this.setNotificationTask(null);
		return "updated";
	}

	public void removeNotificationTask(NotificationTask notificationTask) {
		this.getInstance().getNotificationTasks().remove(notificationTask);
		this.update();
		this.setNotificationTask(null);
	}

	public NotificationTask getLastNotificationTask(Notification n) {
		int index = n.getNotificationTasks().size();
		if (index == 0)
			return null;
		List<Long> ids = new ArrayList<Long>();
		for (NotificationTask nt : n.getNotificationTasks()) {
			ids.add(nt.getId());
		}
		Collections.sort(ids);
		Long lastId = ids.get(ids.size() - 1);
		for (NotificationTask nt : n.getNotificationTasks()) {
			if (nt.getId() == lastId)
				return nt;
		}

		return null;
	}

	private int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {

		Calendar startCal;
		Calendar endCal;
		startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int workDays = 0;

		// Return 0 if start and end are the same
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
			return 0;
		}

		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			startCal.setTime(endDate);
			endCal.setTime(startDate);
		}

		do {
			startCal.add(Calendar.DAY_OF_MONTH, 1);
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
					&& startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				++workDays;
			}
		} while (startCal.getTimeInMillis() < endCal.getTimeInMillis());

		workDays = workDays - 1;

		return workDays;
	}

	/**
	 * Para poner en rojo las notificaciones que pasaron el tiempo que se les da
	 * para que cancelen
	 * 
	 * @param notification
	 * @return String
	 */
	public String showWarned(NotificationTask notification) {
		int days = getWorkingDaysBetweenTwoDates(
				notification.getCreationDate(), new Date());
		if (daysToPayAfterNotification == null)
			daysToPayAfterNotification = getNumberOfDaysToPayAfterNotification();
		if (notification.getNotificationTaskType().getName()
				.equals("NOTIFICADO")
				&& days >= daysToPayAfterNotification) {
			return "redFont";
		} else {
			return "";
		}
	}

	public boolean isPaid() {
		boolean res = true;
		if (systemParameterService == null)
			systemParameterService = ServiceLocator.getInstance().findResource(
					SYSTEM_PARAMETER_SERVICE_NAME);
		MunicipalBondStatus paid = systemParameterService.materialize(
				MunicipalBondStatus.class, "MUNICIPAL_BOND_STATUS_ID_PAID");
		for (MunicipalBond m : getInstance().getMunicipalBonds()) {
			if (!m.getMunicipalBondStatus().equals(paid)) {
				return false;
			}
		}
		return res;
	}

	public List<NotificationTaskType> getNotificationTaskTypeList() {
		this.notificationTaskTypeList.setSequence(this
				.getLastNotificationTask(this.getInstance())
				.getNotificationTaskType().getSequence() + 1);
		return this.notificationTaskTypeList.getResultList();
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * Devuelve los ids de los residents que han sido seleccionados en la lista
	 * de notificaciones
	 * 
	 * @return List<Long>
	 */
	public List<Long> getResidentSelectedItems() {
		List<Long> selectedItems = new ArrayList<Long>();
		residentWithMunicipalBondOutOfDateList
				.setAllResidentsSelected(Boolean.FALSE);
		if (residentWithMunicipalBondOutOfDateList.getSelectedList() == null
				|| residentWithMunicipalBondOutOfDateList.getSelectedList()
						.size() == 0)
			return selectedItems;
		for (ResidentItem ri : residentWithMunicipalBondOutOfDateList
				.getSelectedList()) {
			if (ri.isSelected()) {
				if (!selectedItems.contains(ri.getResident().getId()))
					selectedItems.add(ri.getResident().getId());
			}
		}
		return selectedItems;
	}

	// private void recalculatePendingBonds(){
	// List <Long> ids = getResidentSelectedItems();
	// Query query = null;
	// if(residentWithMunicipalBondOutOfDateList.getEntry() == null) {
	// query =
	// getEntityManager().createNamedQuery("MunicipalBond.findExpiratedByResidentIdAndAmount");
	// }else{
	// query =
	// getEntityManager().createNamedQuery("MunicipalBond.findExpiratedByResidentIdAndEntryIdAndAmount");
	// query.setParameter("entryId",
	// residentWithMunicipalBondOutOfDateList.getEntry().getId());
	// }
	//
	// systemParameterService =
	// ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
	// Long pendingMunicipalBondStatusId =
	// systemParameterService.findParameter(PENDING_BOND_STATUS_ID);
	// query.setParameter("residentIds", ids);
	// query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
	// query.setParameter("municipalBondStatusId",
	// pendingMunicipalBondStatusId);
	// query.setParameter("expirationDate",
	// residentWithMunicipalBondOutOfDateList.getExpirationDate());
	// query.setParameter("value",
	// residentWithMunicipalBondOutOfDateList.getAmount());
	// List<MunicipalBond> municipalBonds = query.getResultList();
	//
	// IncomeService incomeService =
	// ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
	// try {
	// incomeService.calculatePayment(municipalBonds, new Date(), true, true);
	// incomeService.updateMunicipalBonds(municipalBonds);
	//
	// } catch (EntryDefinitionNotFoundException e) {
	// facesMessages.addToControl("savePaymentBtn",
	// Severity.ERROR,"#{messages['entryDefinition.entryDefinitionNotFoundException']}");
	// e.printStackTrace();
	// }
	// }

	public String createNotification() throws IOException {

		// recalculatePendingBonds();

		Date now = Calendar.getInstance().getTime();

		if (this.getResidentSelectedItems().isEmpty()) {
			addFacesMessageFromResourceBundle("common.noSelectedItems");
			return "failed";
		}

		setSelectedItems(Util.listToString(this.getResidentSelectedItems()));

		this.setExpirationDate(residentWithMunicipalBondOutOfDateList
				.getExpirationDate());
		this.setAmount(residentWithMunicipalBondOutOfDateList.getAmount());

		Notification notification = null;
		NotificationTask notificationTask = null;

		for (Long rid : getResidentSelectedItems()) { // son los id's de todos
														// los resident's
			Long entryId = null;
			if (residentWithMunicipalBondOutOfDateList.getEntry() != null)
				entryId = residentWithMunicipalBondOutOfDateList.getEntry()
						.getId();

			// System.out.println("el long del entry-____"+entryId);

			List<MunicipalBond> municipalBonds = findPendingMunicipalBonds(rid,
					entryId, getExpirationDate(), getAmount());

			List<Entry> entries = new ArrayList<Entry>();

			//System.out.println("--------------------------- mb");

			for (MunicipalBond mb : municipalBonds) {
				// System.out.println("el mb: "+mb.getNumber());
				if (!entries.contains(mb.getEntry())) {
					// System.out.println("agregando el entry-...................... : "+mb.getEntry().getId());
					entries.add(mb.getEntry());
				}
			}
			// for(Entry e: entries){
			notification = new Notification();

			notification.setNotified(municipalBonds.get(0).getResident());
			// notification.setEntry(e);

			for (MunicipalBond municipalBond : municipalBonds) {
				// if(municipalBond.getEntry().equals(e)){
				notification.add(municipalBond);
				// }
			}

			notification.setCreationTimeStamp(now);

			notificationTask = new NotificationTask();
			notificationTask.setActuary(userSession.getPerson());
			notificationTask.setCreationDate(now);
			NotificationTaskType notificationTaskType = getNotificationTaskType(NotificationTaskType.FIRST);

			if (notificationTaskType != null) {
				notificationTask.setNotificationTaskType(notificationTaskType);
			} else {
				return "failed";
			}

			notification.add(notificationTask);
			setInstance(notification);

			persist();
			generatedNotificationIds.add(getNotificationId());

			// }
		}

		return "sendToPrint";
	}

	public NotificationTaskType getNotificationTaskType(Long sequence) {
		Query query = getPersistenceContext().createNamedQuery(
				"NotificationTaskType.findBySequence");
		query.setParameter("sequence", sequence);
		List<NotificationTaskType> list = query.getResultList();
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private List<MunicipalBond> findPendingMunicipalBonds(Long residentId,
			Long entryId, Date expirationDate, BigDecimal value) {
		Query query = null;
		if (entryId == null) {
			query = getEntityManager().createNamedQuery(
					"MunicipalBond.findExpiratedByResidentIdAndAmountAndStatus");
		} else {
			query = getEntityManager()
					.createNamedQuery(
							"MunicipalBond.findExpiratedByResidentIdAndEntryIdAndAmountAndStatus");
			query.setParameter("entryId", entryId);
		}

		systemParameterService = ServiceLocator.getInstance().findResource(
				SystemParameterService.LOCAL_NAME);
		List<Long> ids = new ArrayList<Long>();
		ids.add(residentId);
		Long pendingMunicipalBondStatusId = systemParameterService
				.findParameter(PENDING_BOND_STATUS_ID);
		query.setParameter("residentIds", ids);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("municipalBondStatusIds", findMunicipalBondStatusIdsList());
		query.setParameter("expirationDate", expirationDate);
		query.setParameter("value", amount);

		return query.getResultList();

	}
	
	public List<Long> findMunicipalBondStatusIdsList(){
		List<Long> statusIds = new ArrayList();
		statusIds.add(findPendingMunicipalBondStatus().getId());
		statusIds.add(findAgreementMunicipalBondStatus().getId());
		return statusIds;
	}
	
	public MunicipalBondStatus findPendingMunicipalBondStatus(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		return systemParameterService.materialize(MunicipalBondStatus.class, PENDING_BOND_STATUS_ID);		
	}
	public MunicipalBondStatus findAgreementMunicipalBondStatus(){
		SystemParameterService systemParameterService = ServiceLocator.getInstance().findResource(SystemParameterService.LOCAL_NAME);
		return systemParameterService.materialize(MunicipalBondStatus.class, AGREEMENT_BOND_STATUS_ID);		
	}

	public String confirmPrinting() {
		return "printed";
	}

	/**
	 * @return the criteria
	 */
	public String getCriteria() {
		if (this.criteria == null) {
			setCriteria("");
		}
		return criteria;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	public void setDaysForOutOfDate(Long daysForOutOfDate) {
		this.daysForOutOfDate = daysForOutOfDate;
	}

	public Long getDaysForOutOfDate() {
		return daysForOutOfDate;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Long getDaysToPayAfterNotification() {
		return daysToPayAfterNotification;
	}

	public void setDaysToPayAfterNotification(Long daysToPayAfterNotification) {
		this.daysToPayAfterNotification = daysToPayAfterNotification;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getWithCopy() {
		return withCopy;
	}

	public void setWithCopy(Boolean withCopy) {
		this.withCopy = withCopy;
	}

	public Boolean getIsOriginal() {
		return isOriginal;
	}

	public void setIsOriginal(Boolean isOriginal) {
		this.isOriginal = isOriginal;
	}

	/**
	 * @return the identificationNumber
	 */
	public String getIdentificationNumber() {
		return identificationNumber;
	}

	/**
	 * @param identificationNumber
	 *            the identificationNumber to set
	 */
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	
	
	

	/**
	 * @return the isStateNotify
	 */
	public Boolean getIsStateNotify() {
		return isStateNotify;
	}

	/**
	 * @param isStateNotify
	 *            the isStateNotify to set
	 */
	public void setIsStateNotify(Boolean isStateNotify) {
		this.isStateNotify = isStateNotify;
	}

	/**
	 * @return the residents
	 */
	public List<Resident> getResidents() {
		return residents;
	}

	/**
	 * @param residents
	 *            the residents to set
	 */
	public void setResidents(List<Resident> residents) {
		this.residents = residents;
	}

	/**
	 * @return the criteriaResponsible
	 */
	public String getCriteriaResponsible() {
		return criteriaResponsible;
	}

	/**
	 * @param criteriaResponsible
	 *            the criteriaResponsible to set
	 */
	public void setCriteriaResponsible(String criteriaResponsible) {
		this.criteriaResponsible = criteriaResponsible;
	}

	public void setNotificationTask(NotificationTask notificationTask) {
		this.notificationTask = notificationTask;
		this.identificationNumber = new String();
		this.setResponsible(null);
		this.isStateJuice = Boolean.FALSE;
		this.isStateNotify = Boolean.FALSE;
		if (this.notificationTask != null) {
			if (this.notificationTask.getNotificationTaskType() != null) {
				if (this.notificationTask.getNotificationTaskType().getName()
						.equals("JUICIO")) {
					this.setIsStateJuice(Boolean.TRUE);
					if (this.getNotificationTask().getJudgmental() != null) {
						this.setResponsible(this.notificationTask
								.getJudgmental());
						this.setIdentificationNumber(this.notificationTask
								.getJudgmental().getIdentificationNumber());
					}
				}
			}

			if (this.notificationTask.getNotificationTaskType() != null) {
				if (this.notificationTask.getNotificationTaskType().getName()
						.equals("NOTIFICADO")) {
					this.setIsStateNotify(Boolean.TRUE);
					if (this.getNotificationTask().getNotifier() != null) {
						this.setResponsible(this.notificationTask.getNotifier());
						this.setIdentificationNumber(this.notificationTask
								.getNotifier().getIdentificationNumber());
					}

				}
			}
		}
	}

	public NotificationTask getNotificationTask() {
		return notificationTask;
	}

	public Long getNumberNotification() {
		return numberNotification;
	}

	public void setNumberNotification(Long createdNotification) {
		this.numberNotification = createdNotification;
	}

	public String getNameNotifier() {
		return nameNotifier;
	}

	public void setNameNotifier(String nameNotifier) {
		this.nameNotifier = nameNotifier;
	}

	public void setGeneratedNotificationIds(List<Long> generatedNotificationIds) {
		this.generatedNotificationIds = generatedNotificationIds;
	}

	public List<Long> getGeneratedNotificationIds() {
		return generatedNotificationIds;
	}

	public String getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(String selectedItems) {
		this.selectedItems = selectedItems;
	}

	public BigDecimal getTotalCollected() {
		return totalCollected;
	}

	public void setTotalCollected(BigDecimal totalCollected) {
		this.totalCollected = totalCollected;
	}

	public BigDecimal getTotalNotificationGenerated() {
		return totalNotificationGenerated;
	}

	public void setTotalNotificationGenerated(
			BigDecimal totalNotificationGenerated) {
		this.totalNotificationGenerated = totalNotificationGenerated;
	}

	public Long getPendingMunicipalBondStatusId() {
		return pendingMunicipalBondStatusId;
	}

	public void setPendingMunicipalBondStatusId(
			Long pendingMunicipalBondStatusId) {
		this.pendingMunicipalBondStatusId = pendingMunicipalBondStatusId;
	}

	/**
	 * @return the isStateJuice
	 */
	public Boolean getIsStateJuice() {
		return isStateJuice;
	}

	/**
	 * @param isStateJuice
	 *            the isStateJuice to set
	 */
	public void setIsStateJuice(Boolean isStateJuice) {
		this.isStateJuice = isStateJuice;
	}

	/**
	 * @return the isRequiredResident
	 */
	public Boolean getIsRequiredResident() {
		return isStateJuice || isStateNotify;
	}

	/**
	 * @param isRequiredResident
	 *            the isRequiredResident to set
	 */
	public void setIsRequiredResident(Boolean isRequiredResident) {
		this.isRequiredResident = isRequiredResident;
	}

	public String showDateAlertCompliance(Date creationDate) {
		int days = getWorkingDaysBetweenTwoDates(creationDate, new Date());
		if (days >= 8) {
			return "redFont";
		} else {
			return "";
		}
	}

	public Person getResponsible() {
		return responsible;
	}

	public void setResponsible(Person responsible) {
		this.responsible = responsible;
	}
}
