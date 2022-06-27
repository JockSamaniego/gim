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
import org.gob.gim.coercive.dto.criteria.PaymentInfractionsSearchCriteria;
import org.gob.gim.coercive.pagination.NotificationInfractionsDataModel;
import org.gob.gim.coercive.pagination.PaymentInfractionsDataModel;
import org.gob.gim.coercive.service.NotificationInfractionsService;
import org.gob.gim.coercive.service.DatainfractionService;
import org.gob.gim.coercive.service.PaymentInfractionsService;
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
 * @author macartuche
 *
 */

@Name("paymentInfractionHome")
@Scope(ScopeType.CONVERSATION)
public class PaymentInfractionHome extends EntityController {

	private static final long serialVersionUID = 1L;
	private PaymentInfractionsSearchCriteria criteria; 
	private ItemCatalogService itemCatalogService;
	private PaymentInfractionsService paymentInfractionService;
	private final String STATUS_PAYMENT_COERCIVE = "COERCIVE_PAYMENT_STATUS";
	private final String STATUS_PAYMENT_COERCIVE_VALID = "VALID";
	private List<PaymentNotification> payments= new ArrayList<PaymentNotification>();
	private BigDecimal totalPrint = BigDecimal.ZERO;
	
	
	@In(create = true)
	UserSession userSession;

	/**
	 * 
	 */
	public PaymentInfractionHome() {
		super();
		this.initializeService();
		ItemCatalog validStatus = itemCatalogService.findItemByCodeAndCodeCatalog(STATUS_PAYMENT_COERCIVE, STATUS_PAYMENT_COERCIVE_VALID);
		
		this.criteria = new PaymentInfractionsSearchCriteria();
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
		 
	}
	
	public String sendToPrint(){
		this.totalPrint = BigDecimal.ZERO;
		ItemCatalog validStatus = itemCatalogService.findItemByCodeAndCodeCatalog(STATUS_PAYMENT_COERCIVE, STATUS_PAYMENT_COERCIVE_VALID);
		this.payments = paymentInfractionService.getPaymentsByCriteria(criteria, validStatus.getId());
		for (PaymentNotification paymentNotification : payments) {
			this.totalPrint = this.totalPrint.add(paymentNotification.getValue());
		}
		return "sendToPrint";
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

	public List<PaymentNotification> getPayments() {
		return payments;
	}

	public void setPayments(List<PaymentNotification> payments) {
		this.payments = payments;
	}

	public BigDecimal getTotalPrint() {
		return totalPrint;
	}

	public void setTotalPrint(BigDecimal totalPrint) {
		this.totalPrint = totalPrint;
	}
	
	
}
