/**
 * 
 */
package org.gob.gim.coercive.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.coercive.dto.criteria.PaymentInfractionsSearchCriteria;
import org.gob.gim.coercive.pagination.PaymentInfractionsDataModel;
import org.gob.gim.coercive.service.PaymentInfractionsService;
import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.service.ItemCatalogService;
import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.coercive.model.infractions.PaymentInfraction;
import ec.gob.gim.common.model.ItemCatalog;
import ec.gob.gim.common.model.Person;

/**
 * @author macartuche
 *
 */

@Name("paymentInfractionHome")
@Scope(ScopeType.CONVERSATION)
public class PaymentInfractionHome extends EntityController {

	private static final long serialVersionUID = 1L;
	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";
	private SystemParameterService systemParameterService;
	private PaymentInfractionsSearchCriteria criteria;
	private ItemCatalogService itemCatalogService;
	private PaymentInfractionsService paymentInfractionService;
	private final String STATUS_PAYMENT_COERCIVE = "COERCIVE_PAYMENT_STATUS";
	private final String STATUS_PAYMENT_COERCIVE_VALID = "VALID";
	private List<PaymentInfraction> payments = new ArrayList<PaymentInfraction>();
	// ArrayList<PaymentNotification>();
	private BigDecimal totalPrint = BigDecimal.ZERO;
	private BigDecimal total = BigDecimal.ZERO;
	private List<Person> cashiers;
	private Person person;

	@In(create = true)
	UserSession userSession;

	/**
	 * 
	 */
	public PaymentInfractionHome() {
		super();
		this.initializeService();
		ItemCatalog validStatus = itemCatalogService
				.findItemByCodeAndCodeCatalog(STATUS_PAYMENT_COERCIVE,
						STATUS_PAYMENT_COERCIVE_VALID);

		// solo pagos validos
		this.criteria = new PaymentInfractionsSearchCriteria();
		this.criteria.setPerson(null);
		this.criteria.setStatusid(validStatus.getId());
		this.search();
	}

	public void initializeService() {

		if (itemCatalogService == null) {
			itemCatalogService = ServiceLocator.getInstance().findResource(
					ItemCatalogService.LOCAL_NAME);
		}

		if (paymentInfractionService == null) {
			paymentInfractionService = ServiceLocator.getInstance()
					.findResource(PaymentInfractionsService.LOCAL_NAME);
		}
	}

	public void search() {
		getDataModel().setCriteria(this.criteria);
		getDataModel().setRowCount(getDataModel().getObjectsNumber());
		this.total = this.paymentInfractionService
				.getTotalByCriteriaSearch(criteria);

	}

	public String sendToPrint() {
		this.totalPrint = BigDecimal.ZERO;
		ItemCatalog validStatus = itemCatalogService
				.findItemByCodeAndCodeCatalog(STATUS_PAYMENT_COERCIVE,
						STATUS_PAYMENT_COERCIVE_VALID);

		this.payments = paymentInfractionService.getPaymentsByCriteria(
				criteria, validStatus.getId());

		for (PaymentInfraction paymentNotification : payments) {
			this.totalPrint = this.totalPrint.add(paymentNotification.getValue());
		}
		return "sendToPrint";
	}

	public List<Person> findCashiers() {
		if (cashiers == null) {
			if (systemParameterService == null) {
				systemParameterService = ServiceLocator.getInstance()
						.findResource(SYSTEM_PARAMETER_SERVICE_NAME);
			}

			String role_name = systemParameterService
					.findParameter("ROLE_NAME_CASHIER");
			Query query = getPersistenceContext().createNamedQuery(
					"Person.findByRoleName")
					.setParameter("roleName", role_name);
			cashiers = (List<Person>) query.getResultList();
		}
		return cashiers != null ? cashiers : new ArrayList<Person>();
	}

	private PaymentInfractionsDataModel getDataModel() {
		PaymentInfractionsDataModel dataModel = (PaymentInfractionsDataModel) Component
				.getInstance(PaymentInfractionsDataModel.class, true);
		return dataModel;
	}

	public PaymentInfractionsSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(PaymentInfractionsSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public BigDecimal getTotalPrint() {
		return totalPrint;
	}

	public void setTotalPrint(BigDecimal totalPrint) {
		this.totalPrint = totalPrint;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<Person> getCashiers() {
		return cashiers;
	}

	public void setCashiers(List<Person> cashiers) {
		this.cashiers = cashiers;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<PaymentInfraction> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentInfraction> payments) {
		this.payments = payments;
	} 
	
}
