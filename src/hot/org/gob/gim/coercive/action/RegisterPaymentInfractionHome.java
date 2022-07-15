/**
 * 
 */
package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.coercive.dto.criteria.DataInfractionSearchCriteria;
import org.gob.gim.coercive.pagination.DataInfractionDataModel;
import org.gob.gim.coercive.service.DatainfractionService;
import org.gob.gim.coercive.view.InfractionData;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.HistoryStatusInfraction;
import ec.gob.gim.coercive.model.infractions.PaymentInfraction;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.income.model.PaymentType;
import ec.gob.gim.revenue.model.FinancialInstitution;
import ec.gob.gim.revenue.model.FinancialInstitutionType;
import ec.gob.loja.middleapp.ResponseInfraccion;

import javax.transaction.Transactional;

/**
 * @author René
 *
 */
@Name("registerPaymentInfractionHome")
@Scope(ScopeType.CONVERSATION)
public class RegisterPaymentInfractionHome extends EntityController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@In(create = true)
	UserSession userSession;

	@In
	FacesMessages facesMessages;

	private DatainfractionService datainfractionService;

	private ItemCatalogService itemCatalogService;
	
	private SystemParameterService systemParameterService;

	private DataInfractionSearchCriteria criteria;

	private Datainfraction infractionSelected = null;

	private BigDecimal totalPayments = BigDecimal.ZERO;
	private List<PaymentInfraction> newPayments = new ArrayList<PaymentInfraction>();
	private List<PaymentInfraction> payments = new ArrayList<PaymentInfraction>();
	private ItemCatalog validStatus;

	private BigDecimal change = BigDecimal.ZERO;
	private BigDecimal totalToPay = BigDecimal.ZERO;
	private List<FinancialInstitution> banks;
	private List<FinancialInstitution> stateBanks;
	private List<FinancialInstitution> creditCardEmitors;
	private Boolean invalidAmount = Boolean.TRUE;
	private List<ItemCatalog> paymentTypes = new ArrayList<ItemCatalog>();

	private final String STATUS_PAYMENT_COERCIVE = "COERCIVE_PAYMENT_STATUS";
	private final String STATUS_PAYMENT_COERCIVE_VALID = "VALID";
	private final String PAYMENTS_TYPE_CATALOG = "PAYMENT_TYPES";
	private Boolean queryAntOk = Boolean.FALSE;
	private BigDecimal infractionValue = BigDecimal.ZERO;
	private BigDecimal infractionInterest = BigDecimal.ZERO;
	private BigDecimal infractionTotal = BigDecimal.ZERO;
	private String statusANT;

	private String observationPaid;

	private ItemCatalog itemPaid;

	private ItemCatalog itemPending;

	private List<ItemCatalog> statuses = new ArrayList<ItemCatalog>();

	private BigDecimal totalPayed;
	private InfractionData infractionData;
	
	private String cashierInfractionsRoleName;
	
	private List<Long> statusPermitPayment;
	
	private Boolean permitPay;

	/**
	 * 
	 */
	public RegisterPaymentInfractionHome() {
		super();
		initializeService();
		this.validStatus = itemCatalogService.findItemByCodeAndCodeCatalog(
				STATUS_PAYMENT_COERCIVE, STATUS_PAYMENT_COERCIVE_VALID);

		this.paymentTypes = itemCatalogService
				.findItemsForCatalogCode(PAYMENTS_TYPE_CATALOG);
		this.criteria = new DataInfractionSearchCriteria();
		if (itemPaid == null) {
			itemPaid = this.itemCatalogService.findItemByCodeAndCodeCatalog(
					"CATALOG_STATUS_INFRACTIONS", "PAID");
		}

		if (itemPending == null) {
			itemPending = this.itemCatalogService.findItemByCodeAndCodeCatalog(
					"CATALOG_STATUS_INFRACTIONS", "PENDING");
		}

		if (this.statuses.isEmpty()) {
			this.statuses = this.itemCatalogService
					.findItemsForCatalogCodeOrderById("CATALOG_STATUS_INFRACTIONS");
		}
		this.criteria.setStatus(itemPending);
		
		cashierInfractionsRoleName = systemParameterService
				.findParameter("ROLE_NAME_CASHIER_INFRACTIONS");
		
		statusPermitPayment = systemParameterService.findListIds("PAYMENT_INFRACTIONS_STATUS_IDS");

	}

	public void initializeService() {
		if (datainfractionService == null) {
			datainfractionService = ServiceLocator.getInstance().findResource(
					DatainfractionService.LOCAL_NAME);
		}

		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					itemCatalogService.LOCAL_NAME);
		}
		
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SystemParameterService.LOCAL_NAME);
		}

		
	}

	/**
	 * @return the criteria
	 */
	public DataInfractionSearchCriteria getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria
	 *            the criteria to set
	 */
	public void setCriteria(DataInfractionSearchCriteria criteria) {
		this.criteria = criteria;
	}

	/**
	 * @return the datainfractionService
	 */
	public DatainfractionService getDatainfractionService() {
		return datainfractionService;
	}

	/**
	 * @param datainfractionService
	 *            the datainfractionService to set
	 */
	public void setDatainfractionService(
			DatainfractionService datainfractionService) {
		this.datainfractionService = datainfractionService;
	}

	/**
	 * @return the infractionSelected
	 */
	public Datainfraction getInfractionSelected() {
		return infractionSelected;
	}

	/**
	 * @param infractionSelected
	 *            the infractionSelected to set
	 */
	public void setInfractionSelected(Datainfraction infractionSelected) {
		this.infractionSelected = infractionSelected;
	}

	/**
	 * @return the totalPayments
	 */
	public BigDecimal getTotalPayments() {
		return totalPayments;
	}

	/**
	 * @param totalPayments
	 *            the totalPayments to set
	 */
	public void setTotalPayments(BigDecimal totalPayments) {
		this.totalPayments = totalPayments;
	}

	/**
	 * @return the newPayments
	 */
	public List<PaymentInfraction> getNewPayments() {
		return newPayments;
	}

	/**
	 * @param newPayments
	 *            the newPayments to set
	 */
	public void setNewPayments(List<PaymentInfraction> newPayments) {
		this.newPayments = newPayments;
	}

	/**
	 * @return the payments
	 */
	public List<PaymentInfraction> getPayments() {
		return payments;
	}

	/**
	 * @param payments
	 *            the payments to set
	 */
	public void setPayments(List<PaymentInfraction> payments) {
		this.payments = payments;
	}

	/**
	 * @return the validStatus
	 */
	public ItemCatalog getValidStatus() {
		return validStatus;
	}

	/**
	 * @param validStatus
	 *            the validStatus to set
	 */
	public void setValidStatus(ItemCatalog validStatus) {
		this.validStatus = validStatus;
	}

	/**
	 * @return the change
	 */
	public BigDecimal getChange() {
		return change;
	}

	/**
	 * @param change
	 *            the change to set
	 */
	public void setChange(BigDecimal change) {
		this.change = change;
	}

	/**
	 * @return the totalToPay
	 */
	public BigDecimal getTotalToPay() {
		return totalToPay;
	}

	/**
	 * @param totalToPay
	 *            the totalToPay to set
	 */
	public void setTotalToPay(BigDecimal totalToPay) {
		this.totalToPay = totalToPay;
	}

	/**
	 * @return the banks
	 */
	public List<FinancialInstitution> getBanks() {
		return banks;
	}

	/**
	 * @param banks
	 *            the banks to set
	 */
	public void setBanks(List<FinancialInstitution> banks) {
		this.banks = banks;
	}

	/**
	 * @return the stateBanks
	 */
	public List<FinancialInstitution> getStateBanks() {
		return stateBanks;
	}

	/**
	 * @param stateBanks
	 *            the stateBanks to set
	 */
	public void setStateBanks(List<FinancialInstitution> stateBanks) {
		this.stateBanks = stateBanks;
	}

	/**
	 * @return the creditCardEmitors
	 */
	public List<FinancialInstitution> getCreditCardEmitors() {
		return creditCardEmitors;
	}

	/**
	 * @param creditCardEmitors
	 *            the creditCardEmitors to set
	 */
	public void setCreditCardEmitors(
			List<FinancialInstitution> creditCardEmitors) {
		this.creditCardEmitors = creditCardEmitors;
	}

	/**
	 * @return the invalidAmount
	 */
	public Boolean getInvalidAmount() {
		return invalidAmount;
	}

	/**
	 * @param invalidAmount
	 *            the invalidAmount to set
	 */
	public void setInvalidAmount(Boolean invalidAmount) {
		this.invalidAmount = invalidAmount;
	}

	/**
	 * @return the paymentTypes
	 */
	public List<ItemCatalog> getPaymentTypes() {
		return paymentTypes;
	}

	/**
	 * @param paymentTypes
	 *            the paymentTypes to set
	 */
	public void setPaymentTypes(List<ItemCatalog> paymentTypes) {
		this.paymentTypes = paymentTypes;
	}

	/**
	 * @return the queryAntOk
	 */
	public Boolean getQueryAntOk() {
		return queryAntOk;
	}

	/**
	 * @param queryAntOk
	 *            the queryAntOk to set
	 */
	public void setQueryAntOk(Boolean queryAntOk) {
		this.queryAntOk = queryAntOk;
	}

	/**
	 * @return the infractionValue
	 */
	public BigDecimal getInfractionValue() {
		return infractionValue;
	}

	/**
	 * @param infractionValue
	 *            the infractionValue to set
	 */
	public void setInfractionValue(BigDecimal infractionValue) {
		this.infractionValue = infractionValue;
	}

	/**
	 * @return the infractionInterest
	 */
	public BigDecimal getInfractionInterest() {
		return infractionInterest;
	}

	/**
	 * @param infractionInterest
	 *            the infractionInterest to set
	 */
	public void setInfractionInterest(BigDecimal infractionInterest) {
		this.infractionInterest = infractionInterest;
	}

	/**
	 * @return the infractionTotal
	 */
	public BigDecimal getInfractionTotal() {
		return infractionTotal;
	}

	/**
	 * @param infractionTotal
	 *            the infractionTotal to set
	 */
	public void setInfractionTotal(BigDecimal infractionTotal) {
		this.infractionTotal = infractionTotal;
	}

	/**
	 * @return the statusANT
	 */
	public String getStatusANT() {
		return statusANT;
	}

	/**
	 * @param statusANT
	 *            the statusANT to set
	 */
	public void setStatusANT(String statusANT) {
		this.statusANT = statusANT;
	}

	/**
	 * @return the observationPaid
	 */
	public String getObservationPaid() {
		return observationPaid;
	}

	/**
	 * @param observationPaid
	 *            the observationPaid to set
	 */
	public void setObservationPaid(String observationPaid) {
		this.observationPaid = observationPaid;
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

	public void search() {
		getDataModel().setCriteria(this.criteria);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());
	}

	private DataInfractionDataModel getDataModel() {

		DataInfractionDataModel dataModel = (DataInfractionDataModel) Component
				.getInstance(DataInfractionDataModel.class, true);

		return dataModel;
	}

	public void viewPayments(Long infractionId) {

		// limpiar data
		this.newPayments.clear();
		this.totalPayments = BigDecimal.ZERO;

		if (datainfractionService == null) {
			datainfractionService = ServiceLocator.getInstance().findResource(
					datainfractionService.LOCAL_NAME);
		}

		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					itemCatalogService.LOCAL_NAME);
		}

		// this.userData = this.datainfractionService.userData(notificationId);
		this.payments = this.datainfractionService.findPaymentsByInfraction(
				infractionId, this.validStatus.getId());
		this.infractionSelected = datainfractionService
				.getDataInfractionById(infractionId);
		
		
		
		Boolean hasRole = userSession.getUser().hasRole(cashierInfractionsRoleName);
		Boolean hasStatus = this.statusPermitPayment.contains(this.infractionSelected.getState().getId());
		this.permitPay = hasRole && hasStatus; 
		
		

		for (PaymentInfraction payment : this.payments) {
			this.totalPayments = this.totalPayments.add(payment.getValue());
		}

		// agregar por defecto una fraccion de pago
		ItemCatalog CASH = itemCatalogService.findItemByCodeAndCodeCatalog(
				PAYMENTS_TYPE_CATALOG, "CASH");
		this.loadLists();

		PaymentInfraction payment = new PaymentInfraction();
		payment.setPaymentType(CASH);
		payment.setValue(BigDecimal.ZERO);
		this.newPayments.add(payment);

		this.invalidAmount = Boolean.TRUE;

		try {

			if (this.infractionSelected.getId_factura() == null) {
				this.queryAntOk = Boolean.FALSE;
				this.infractionValue = this.infractionSelected.getValue();
				this.infractionInterest = this.infractionSelected.getInterest();
				this.infractionTotal = this.infractionSelected.getTotalValue();
			} else {
				ResponseInfraccion responseAnt = this.datainfractionService
						.findInfractionByIdANT(this.infractionSelected
								.getId_factura());

				if (responseAnt.getCode() == 200) {

					if (responseAnt.getInfraccion().getResultado()
							.getCodError() == null) {
						this.queryAntOk = Boolean.TRUE;
						this.infractionValue = new BigDecimal(responseAnt
								.getInfraccion().getValor());
						this.infractionInterest = responseAnt.getInfraccion()
								.getPagada().equals("S") ? new BigDecimal(
								responseAnt.getInfraccion().getIntereses())
								: new BigDecimal(responseAnt.getInfraccion()
										.getSaldoMultas());
						this.infractionTotal = this.infractionValue
								.add(this.infractionInterest);
						this.statusANT = responseAnt.getInfraccion()
								.getEstado();
					} else {
						this.queryAntOk = Boolean.FALSE;
						this.infractionValue = this.infractionSelected
								.getValue();
						this.infractionInterest = this.infractionSelected
								.getInterest();
						this.infractionTotal = this.infractionSelected
								.getTotalValue();
						this.statusANT = null;
					}
				} else {
					this.queryAntOk = Boolean.FALSE;
					this.infractionValue = this.infractionSelected.getValue();
					this.infractionInterest = this.infractionSelected
							.getInterest();
					this.infractionTotal = this.infractionSelected
							.getTotalValue();
					this.statusANT = null;
				}
			}

		} catch (Exception e) {
			this.queryAntOk = Boolean.FALSE;
			this.infractionValue = this.infractionSelected.getValue();
			this.infractionInterest = this.infractionSelected.getInterest();
			this.infractionTotal = this.infractionSelected.getTotalValue();
			this.statusANT = null;
		}

	}

	private void loadLists() {
		if (banks == null)
			banks = findFinantialInstitutions(FinancialInstitutionType.BANK);
		if (creditCardEmitors == null)
			creditCardEmitors = findFinantialInstitutions(FinancialInstitutionType.CREDIT_CARD_EMISOR);
		if (stateBanks == null)
			stateBanks = findFinantialInstitutions(FinancialInstitutionType.STATE_BANK);
	}

	public List<FinancialInstitution> getFinantialInstitutions(
			ItemCatalog paymentType) {
		if (paymentType == null) {
			return new ArrayList<FinancialInstitution>();
		}

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

	@SuppressWarnings("unchecked")
	private List<FinancialInstitution> findFinantialInstitutions(
			FinancialInstitutionType finantialInstitutionType) {
		Query query = getPersistenceContext().createNamedQuery(
				"FinancialInstitution.findByType");
		query.setParameter("type", finantialInstitutionType);
		return query.getResultList();
	}

	public void clearValues(PaymentInfraction fraction) {
		fraction.setValue(BigDecimal.ZERO);
		fraction.setFinantialInstitution(null);
		fraction.setDocumentNumber(null);
		fraction.setAccountNumber(null);
		calculateChange();
	}

	/**
	 * Quitar fraccion-abono
	 */
	public void removePayment(PaymentInfraction payment) {
		this.newPayments.remove(payment);

	}

	/**
	 * Nueva fraccion-abono
	 */
	public void addnewPayment() {
		this.newPayments.add(new PaymentInfraction());
	}

	public void calculateChange() {
		this.invalidAmount = Boolean.FALSE;
	}

	@Transactional
	public void savePayment() {

		if (infractionSelected == null) {
			return;
		}

		// recorrer todas los abonos
		for (PaymentInfraction payment : this.newPayments) {
			payment.setCashier(userSession.getUser());
			payment.setInfraction(infractionSelected);
			payment.setStatus(this.validStatus);
			this.datainfractionService.savePaymentInfraction(payment);
		}
		this.infractionSelected = datainfractionService
				.getDataInfractionById(this.infractionSelected.getId());
		
		this.viewPayments(this.infractionSelected.getId());
	}

	public void prepareChangeToPaid(Datainfraction infraction) {
		this.infractionSelected = datainfractionService
				.getDataInfractionById(infraction.getId());
		this.observationPaid = null;
	}

	public void savePaidStatus() {
		if ((this.observationPaid == null || this.observationPaid.isEmpty())) {
			facesMessages.addToControl("",
					org.jboss.seam.international.StatusMessage.Severity.ERROR,
					"Campos obligatorios vacios");
		} else {
			if (datainfractionService == null) {
				datainfractionService = ServiceLocator.getInstance()
						.findResource(datainfractionService.LOCAL_NAME);
			}

			this.infractionSelected.setState(this.itemPaid);
			this.infractionSelected = this.datainfractionService
					.updateDataInfraction(this.infractionSelected);

			// agregar al historial
			HistoryStatusInfraction record = new HistoryStatusInfraction();
			record.setDate(new Date());
			record.setInfraction(this.infractionSelected);
			record.setObservation(this.observationPaid);
			record.setStatus(this.itemPaid);
			record.setUser(this.userSession.getUser());

			this.datainfractionService.saveHIstoryRecord(record);
		}
	}

	/**
	 * @author macartuche
	 * @param infractionId
	 *            Long
	 */
	public String printPayments(Long infractionId) {

		//info de la infraccion
		this.infractionData = datainfractionService.infractionData(infractionId);		
		this.payments = this.datainfractionService.findPaymentsByInfraction(
				infractionId, this.validStatus.getId());
		
		this.totalPayed = BigDecimal.ZERO;
		for (PaymentInfraction payment : this.payments) {
			this.totalPayed = this.totalPayed.add(payment.getValue());
		}
		return "printPayment";
	}

	public BigDecimal getTotalPayed() {
		return totalPayed;
	}

	public void setTotalPayed(BigDecimal totalPayed) {
		this.totalPayed = totalPayed;
	}

	public InfractionData getInfractionData() {
		return infractionData;
	}

	public void setInfractionData(InfractionData infractionData) {
		this.infractionData = infractionData;
	}

	/**
	 * @return the permitPay
	 */
	public Boolean getPermitPay() {
		return permitPay;
	}

	/**
	 * @param permitPay the permitPay to set
	 */
	public void setPermitPay(Boolean permitPay) {
		this.permitPay = permitPay;
	}
	
}
