/**
 * 
 */
package org.gob.gim.coercive.pagination;

import java.math.BigDecimal;
import java.util.List;

import org.gob.gim.coercive.dto.criteria.PaymentInfractionsSearchCriteria;
import org.gob.gim.coercive.service.PaymentInfractionsService;
import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import ec.gob.gim.coercive.model.infractions.PaymentNotification;

/**
 * @author macartuche
 *
 */
@Name("paymentInfractions")
@Scope(ScopeType.CONVERSATION)
public class PaymentInfractionsDataModel extends
		PageableDataModel<PaymentNotification, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PaymentInfractionsSearchCriteria criteria;
	private PaymentInfractionsService paymentInfractionsService;
	private BigDecimal total = BigDecimal.ZERO;
	private List<PaymentNotification> payments;

	/**
	 * 
	 */
	public PaymentInfractionsDataModel() {
		super();
		this.initializeService();
	}

	@SuppressWarnings("static-access")
	public void initializeService() {
		if (paymentInfractionsService == null) {
			paymentInfractionsService = ServiceLocator.getInstance()
					.findResource(paymentInfractionsService.LOCAL_NAME);
		}
	}

	@Override
	public Long getId(PaymentNotification object) {
		return object.getId();
	}

	@Override
	public List<PaymentNotification> findObjects(int firstRow,
			int numberOfRows, String sortField, boolean descending) {
		List<PaymentNotification> payments = paymentInfractionsService
				.findPaymentInfractionByCriteria(criteria, firstRow,
						numberOfRows);
		this.total = BigDecimal.ZERO;

		for (PaymentNotification paymentNotification : payments) {
			this.total = this.total.add(paymentNotification.getValue());
		}

		this.payments = payments;
		return payments;
	}

	@Override
	public PaymentNotification getObjectById(Long id) {
		return this.paymentInfractionsService.findObjectById(id);
	}

	@Override
	public String getDefaultSortField() {
		return "attribute(EOD)";
	}

	@Override
	public int getObjectsNumber() {
		return paymentInfractionsService.findPaymentsNumber(criteria)
				.intValue();
	}

	@Override
	public int getRowCount() {
		return rowCount.intValue();
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public PaymentInfractionsSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(PaymentInfractionsSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public BigDecimal getTotal() {

		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
