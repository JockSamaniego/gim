package org.gob.gim.coercive.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.gob.gim.coercive.view.ResidentItem;
import org.gob.gim.common.NativeQueryResultsMapper;
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
import ec.gob.gim.coercive.model.NotificationPayedDTO;
import ec.gob.gim.coercive.model.NotificationPayedDetailDTO;
import ec.gob.gim.coercive.model.NotificationTask;
import ec.gob.gim.coercive.model.NotificationTaskType;
import ec.gob.gim.common.model.Charge;
import ec.gob.gim.common.model.Delegate;
import ec.gob.gim.common.model.Person;
import ec.gob.gim.common.model.SystemParameter;
import ec.gob.gim.revenue.model.Entry;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.MunicipalBondType;
import ec.gob.gim.revenue.model.impugnment.Impugnment;

@Name("notificationHome")
@Scope(ScopeType.CONVERSATION)
public class NotificationHome extends EntityHome<Notification> {

	public ArrayList getNotificationTotalDescendentOrden() {
		return notificationTotalDescendentOrden;
	}

	public void setNotificationTotalDescendentOrden(
			ArrayList notificationTotalDescendentOrden) {
		this.notificationTotalDescendentOrden = notificationTotalDescendentOrden;
	}

	public ArrayList getVarvaluesBoundsNotificationsandPendings() {
		return varvaluesBoundsNotificationsandPendings;
	}

	public void setVarvaluesBoundsNotificationsandPendings(
			ArrayList varvaluesBoundsNotificationsandPendings) {
		this.varvaluesBoundsNotificationsandPendings = varvaluesBoundsNotificationsandPendings;
	}

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
	private Long createdNotification;
	private Long cancelledNotification;
	private Long pendingMunicipalBondStatusId;
	private String selectedItems;
	protected String criteria;
	private Long daysForOutOfDate = 0L;
	private BigDecimal amount;
	private Date expirationDate;
	private BigDecimal totalCollected;
	private BigDecimal totalGenerated;
	private ArrayList notificationTotalDescendentOrden;
	private ArrayList varvaluesBoundsNotificationsandPendings;
	
	private Boolean isStateJuice = Boolean.FALSE;

	private Boolean isStateNotify = Boolean.FALSE;

	private String identificationNumber;

	private List<Long> generatedNotificationIds = new ArrayList<Long>();

	public Boolean printCopies() {
		setIsOriginal(Boolean.FALSE);
		return withCopy;
	}

	public void loadValues() {
		Calendar ca = Calendar.getInstance();
		startDate = ca.getTime();
		endDate = ca.getTime();
		loadCharge();
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

	private Long getNumberOfCreatedNotifcations(Date startDate, Date endDate) {
		Query query = getEntityManager().createNamedQuery(
				"Notification.countCreatedBetweenDates");
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);

		return (Long) query.getSingleResult();
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

	public String generateReport() {
		createdNotification = getNumberOfCreatedNotifcations(startDate, endDate);
		cancelledNotification = getNumberOfCancelledNotifications(startDate,
				endDate);
		totalGenerated = getTotalBondsValues(startDate, endDate);
		totalCollected = getTotalBondsPaidTotals(startDate, endDate);
		return "readyForPrint";
	}

	public void loadBondsForDetail() {
		residentWithMunicipalBondOutOfDateList.setMunicipalBonds(this
				.getInstance().getMunicipalBonds());
	}

	public String addNotificationTask() {
//		this.notificationTask.setJudgmental(null);
//		this.notificationTask.setNotifier(null);
		Date now = Calendar.getInstance().getTime();
		//System.out.println("Responsable:" + person.getName());
		//System.out.println("000 valor:" + this.getNotificationTask().getId());
		if (this.getNotificationTask().getId() == null) {
			// cerrar estado activo
			NotificationTask last = getLastNotificationTask(this.getInstance());
			last.setNotifiedDate(now);
			this.notificationTaskHome.setInstance(last);
			this.notificationTaskHome.update();

			// Almacenar nuevo estado
			this.getNotificationTask().setActuary(userSession.getPerson());
			if (this.isStateJuice) {
				// System.out.println("2222" + this.isStateJuice +  " person:" +person);
				this.notificationTask.setJudgmental(person);
			}

			if (this.isStateNotify) {
				//System.out.println("333" + this.isStateNotify +  " person:" +person);System.out.println("333" + this.isStateNotify +  " person:" +person);
				this.notificationTask.setNotifier(person);
			}

			this.notificationTaskHome.setInstance(this.getNotificationTask());
			this.notificationTaskHome.persist();
			this.getInstance().add(this.getNotificationTask());
		} else {
			//System.out.println("444");
			if (this.isStateJuice) {
				//System.out.println("555" + this.isStateJuice +  " person:" +person);
				this.notificationTask.setJudgmental(person);
			}

			if (this.isStateNotify) {
				//System.out.println("666" + this.isStateNotify +  " person:" +person);
				this.notificationTask.setNotifier(person);
			}
			this.notificationTaskHome.setInstance(this.getNotificationTask());
			this.notificationTaskHome.update();
		}
		this.update();
		this.setIdentificationNumber(new String());
		this.setPerson(null);
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
				;
				if(findAcceptedImpugnments(municipalBond.getId()).size()<=0){
					notification.add(municipalBond);
					System.out.println("====================La obligacion "+municipalBond.getId()+" ha sido notificada!!!");
				}else{
					//System.out.println("--------------------La obligacion "+municipalBond.getId()+" tiene impugnaciones:");
					municipalBond.setNotification(notification);
				}
				
				// }
			}

			notification.setCreationTimeStamp(now);
			// set anio notificacion
			notification.setYear(Calendar.getInstance().get(Calendar.YEAR));
			//System.out.println("anio que se envia:" + notification.getYear());
			// setear siguiente numero de notificacion
			notification.setNumber(getNextNumberNotification(notification
					.getYear()));

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

	public int getNextNumberNotification(int year) {

		//System.out.println("anio que llega para obetener numero:" + year);
		Query query = getPersistenceContext().createNamedQuery(
				"Notification.actualNumber");
		query.setParameter("year", year);
		Object object = query.getSingleResult();

		if (object == null) {
			return new Integer(1);
		}
		return ((Integer) object)+1;
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

	public void setNotificationTask(NotificationTask notificationTask) {
		this.notificationTask = notificationTask;
		this.identificationNumber = new String();
		this.setPerson(null);
		this.isStateJuice = Boolean.FALSE;
		this.isStateNotify = Boolean.FALSE;
		if (this.notificationTask != null) {
			if (this.notificationTask.getNotificationTaskType() != null) {
				if (this.notificationTask.getNotificationTaskType().getName()
						.equals("JUICIO")) {
					this.setIsStateJuice(Boolean.TRUE);
					if (this.getNotificationTask().getJudgmental() != null) {
						this.setPerson(this.notificationTask
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
						this.setPerson(this.notificationTask.getNotifier());
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

	public Long getCreatedNotification() {
		return createdNotification;
	}

	public void setCreatedNotification(Long createdNotification) {
		this.createdNotification = createdNotification;
	}

	public Long getCancelledNotification() {
		return cancelledNotification;
	}

	public void setCancelledNotification(Long cancelledNotification) {
		this.cancelledNotification = cancelledNotification;
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

	public BigDecimal getTotalGenerated() {
		return totalGenerated;
	}

	public void setTotalGenerated(BigDecimal totalGenerated) {
		this.totalGenerated = totalGenerated;
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

	public String showDateAlertCompliance(Date creationDate) {
		int days = getWorkingDaysBetweenTwoDates(creationDate, new Date());
		if (days >= 8) {
			return "redFont";
		} else {
			return "";
		}
	}

	public void onChangeStatus() {
		this.identificationNumber = new String();
		this.isStateJuice = Boolean.FALSE;
		this.isStateNotify = Boolean.FALSE;
		
		//System.out.println("Entro al onchange:");
		if (notificationTask.getNotificationTaskType().getName()
				.equals("JUICIO")) {
			//System.out.println("ES juicio");			
			this.setIsStateJuice(Boolean.TRUE);
			System.out.println("combo estados 111: " + this.isStateJuice);
		}

		if (notificationTask.getNotificationTaskType().getName()
				.equals("NOTIFICADO")) {
			//System.out.println("ES Notificacion");			
			this.setIsStateNotify(Boolean.TRUE);
			//System.out.println("combo estados 222: " + this.isStateNotify);
		}

		if (!notificationTask.getNotificationTaskType().getName()
				.equals("NOTIFICADO")
				|| !notificationTask.getNotificationTaskType().getName()
						.equals("JUICIO")) {
			this.setPerson(null);
			this.setIdentificationNumber(new String());
			//System.out.println("combo estados 333:" + this.isStateNotify + " estadojuidio:"+ this.isStateJuice);
		}

		if (!isStateJuice && !isStateNotify) {
			//System.out.println("No en niguna iocion controlada");
			//System.out.println("combo estados 444:" + this.isStateNotify + " estadojuidio:"+ this.isStateJuice);
		}

		//System.out.println("Es Juicio: " + isStateJuice);
		//System.out.println("Es Notificacion: " + isStateNotify);
	}
	
	public void reset() {
	    FacesContext context = FacesContext.getCurrentInstance();
	    UIInput input =  (UIInput)context.getViewRoot().findComponent("business:notificationTaskNumberJudgment:numerojuicio");
	    UIInput input2 = (UIInput)context.getViewRoot().findComponent("business:personField:person");
//	    UIInput input3 = (UIInput)context.getViewRoot().findComponent("business:reponsible:_name");
	    input.resetValue();
	    input2.resetValue();	    
//	    input3.resetValue();
	}
	
	public void resetResponsible() {
	    FacesContext context = FacesContext.getCurrentInstance();	   
	    UIInput input2 = (UIInput)context.getViewRoot().findComponent("business:reponsible:personField");
	    input2.setRequired(false);
	}
	
	public String getReponsibleName(NotificationTask notificationTask){
		if(notificationTask.getNotifier()!=null){
			return notificationTask.getNotifier().getName();
		}else if(notificationTask.getJudgmental() !=null){
			return notificationTask.getJudgmental().getName();
		}
		return new String();
	}
	
	private Person person;
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public void onChangeStatusResponsible() {
//		this.identificationNumber = new String();		
//		this.isStateJuice = Boolean.FALSE;
//		this.isStateNotify = Boolean.FALSE;
		
		//System.out.println("Entro al onchange de responsable:");
		if (person.getName().equals(true)) {
			//System.out.println("0011: no esta vacia");
			resetResponsible();
			//this.isStateJuice = Boolean.FALSE;
			//this.setIsStateJuice(Boolean.TRUE);
			//System.out.println("combo estados 0022: juicio" + this.isStateJuice+" notificacion:"+ this.isStateNotify);
		}

	}	

	public String generateReportRubros() {
		getNotifcationsDescendingOrder(startDate, endDate);
		return "readyForPrint";
	}
	
	private void getNotifcationsDescendingOrder(Date startDate, Date endDate) {		
		String sqlQuery = "SELECT res.name, array_to_string(array_agg(distinct concat(n.year,'-',n.number)), ', ') as number, sum(mb.paidTotal) as total "
			    + "FROM gimprod.Notification n "
			    + "INNER JOIN gimprod.MunicipalBond mb ON (mb.notification_id = n.id) "
			    + "INNER JOIN gimprod.resident res ON (mb.resident_id = res.id) "
			    + "WHERE cast(n.creationTimeStamp as date) BETWEEN ? AND ? "
			    + "AND mb.id IN (SELECT DISTINCT mb.id FROM gimprod.notification n "
			    	+ "INNER JOIN gimprod.municipalbond mb ON  mb.notification_id = n.id "
			    	+ "INNER JOIN gimprod.resident res ON n.notified_id = res.id "
			    	+ "INNER JOIN gimprod.notificationtask nt ON nt.notification_id = n.id "
			    	+ "INNER JOIN gimprod.notificationtasktype ntt ON nt.notificationtasktype_id = ntt.id "
			    	+ "INNER JOIN gimprod.municipalbondstatus mbs ON mbs.id = mb.municipalbondstatus_id "
			    	+ "WHERE ntt.name = 'NOTIFICADO' AND mbs.name in ('PENDIENTE')) "
		    	+ "GROUP BY res.name "
		    	+ "ORDER BY total DESC";
		Query query = getEntityManager().createNativeQuery(sqlQuery);	
		query.setParameter(1, startDate);
		query.setParameter(2, endDate);		
		notificationTotalDescendentOrden = new ArrayList<NotificationTotalDescendenteOrdenDTO>();
		try {
			List i = query.getResultList();
			//System.out.println("lista size 2do query: "+i.size());
			for (Object v : i) {
				Object[] vl = (Object[]) v;
				notificationTotalDescendentOrden.add(new NotificationTotalDescendenteOrdenDTO((String) vl[0],
													 (String) vl[1].toString(), 
													 (BigDecimal) vl[2]));
			}
		} catch (NoResultException ex) {
			ex.printStackTrace();
		}
	}
	
	
	// Para controlar las municipal Bond que tienen impugnaciones aceptadas.............
	// Jock Samaniego............. 14-09-2016........
		
		private List<Impugnment> impugnmentsTotal;
		private String[] states;
		
		@SuppressWarnings("unchecked")
		public List<Impugnment> findAcceptedImpugnments(Long id){
			impugnmentsTotal = new ArrayList<Impugnment>();
			List<Impugnment> impugnments = new ArrayList<Impugnment>();
			for (String st : states){
				Query query = getEntityManager().createNamedQuery("Impugnment.findByMunicipalBond");
				query.setParameter("municipalBond_id", id);
				query.setParameter("code", st);
				impugnments = query.getResultList();
				if(impugnments.size()>0){
					impugnmentsTotal.addAll(impugnments);
				}		
			}
			return impugnmentsTotal;
		}

		public void chargeControlImpugnmentStates(){
			Query query = getEntityManager().createNamedQuery("SystemParameter.findByName");
			query.setParameter("name", "STATES_IMPUGNMENT_CONTROL_REGISTER_NOTIFICATION");
			SystemParameter controlStates = (SystemParameter) query.getSingleResult();
			states = controlStates.getValue().trim().split(",");
		}

		public List<Impugnment> getImpugnmentsTotal() {
			return impugnmentsTotal;
		}

		public void setImpugnmentsTotal(List<Impugnment> impugnmentsTotal) {
			this.impugnmentsTotal = impugnmentsTotal;
		}
		
		//para reporte detallado de coactivas
		//Jock Samaniego M
		//29-10-2019
		
		private List<NotificationPayedDTO> payeds;
		private BigDecimal totalPayed;
		private Date startDatePayed;
		private Date endDatePayed;
		
		public List<NotificationPayedDTO> getPayeds() {
			return payeds;
		}

		public BigDecimal getTotalPayed() {
			return totalPayed;
		}

		public Date getStartDatePayed() {
			return startDatePayed;
		}

		public void setStartDatePayed(Date startDatePayed) {
			this.startDatePayed = startDatePayed;
		}

		public Date getEndDatePayed() {
			return endDatePayed;
		}

		public void setEndDatePayed(Date endDatePayed) {
			this.endDatePayed = endDatePayed;
		}

		public void getTotalNotificationsPayed(){
			totalPayed = BigDecimal.ZERO;
			payeds = new ArrayList();
			String query = "SELECT count(mb.id), ent.name, sum(mb.paidtotal), ent.id from gimprod.municipalbond mb "
							+ "LEFT JOIN gimprod.notification nt on nt.id = mb.notification_id "
							+ "LEFT JOIN gimprod.entry ent On ent.id = mb.entry_id "
							+ "WHERE mb.liquidationdate BETWEEN :startDate and :endDate "
							+ "and mb.notification_id is not NULL "
							+ "and mb.municipalbondstatus_id in (6,11) "
							+ "GROUP BY ent.name, ent.id "
							+ "ORDER BY ent.name ASC ";
		
			Query q = this.getEntityManager().createNativeQuery(query);
			q.setParameter("startDate", startDatePayed);
			q.setParameter("endDate", endDatePayed);
			payeds = NativeQueryResultsMapper.map(q.getResultList(), NotificationPayedDTO.class);
			for(NotificationPayedDTO np : payeds){
				totalPayed = totalPayed.add(np.getTotal());
			}
		}
		
		private NotificationPayedDTO entrySelected;
		private List<NotificationPayedDetailDTO> payedsDetail;
		
		public NotificationPayedDTO getEntrySelected() {
			return entrySelected;
		}

		public List<NotificationPayedDetailDTO> getPayedsDetail() {
			return payedsDetail;
		}

		public List<NotificationPayedDetailDTO> generateNotificationPayedDetail(NotificationPayedDTO _entry){
			entrySelected = _entry;
			List <NotificationPayedDetailDTO> details = new ArrayList<NotificationPayedDetailDTO>();
			String query = "SELECT mb.number, res.identificationnumber, res.name, nt.id, mb.emisiondate, mb.expirationdate, nt.creationtimestamp, mb.liquidationdate, mb.paidtotal "
							+ "from gimprod.municipalbond mb "
							+ "LEFT JOIN gimprod.resident res on res.id = mb.resident_id "
							+ "LEFT JOIN gimprod.notification nt on nt.id = mb.notification_id "
							+ "WHERE mb.entry_id =:entryId "
							+ "and mb.notification_id is not NULL "
							+ "and mb.liquidationdate BETWEEN :startDate and :endDate "
							+ "and mb.municipalbondstatus_id in (6,11) "
							+ "ORDER BY res.name, mb.liquidationdate ";
			
			Query q = this.getEntityManager().createNativeQuery(query);
			q.setParameter("entryId", entrySelected.getEntryId());
			q.setParameter("startDate", startDatePayed);
			q.setParameter("endDate", endDatePayed);
			details = NativeQueryResultsMapper.map(q.getResultList(), NotificationPayedDetailDTO.class);
			return details;
		} 
		
		public void findNotificationPayedDetail(NotificationPayedDTO _entry){
			payedsDetail = new ArrayList<NotificationPayedDetailDTO>();
			payedsDetail = generateNotificationPayedDetail(_entry);
		}
		
		public String generateTotalNotificationsReport(){			
			return "/coercive/report/PayedNotificationTotalReport.xhtml";
		}
		
		
		private Charge coerciveCharge;
		private Delegate coerciveDelegate;

		public Charge getCoerciveCharge() {
			return coerciveCharge;
		}

		public Delegate getCoerciveDelegate() {
			return coerciveDelegate;
		}
		
		
		public void loadCharge() {
			coerciveCharge = getCharge("DELEGATE_ID_COERCIVE");
			if (coerciveCharge != null) {
				for (Delegate d : coerciveCharge.getDelegates()) {
					if (d.getIsActive())
						coerciveDelegate = d;
				}
			}
		}
		
		private Charge getCharge(String systemParameter) {
			if (systemParameterService == null)
				systemParameterService = ServiceLocator.getInstance().findResource(
						SYSTEM_PARAMETER_SERVICE_NAME);
			Charge charge = systemParameterService.materialize(Charge.class,
					systemParameter);
			return charge;
		}
}