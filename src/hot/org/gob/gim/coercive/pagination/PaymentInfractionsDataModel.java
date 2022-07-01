/**
 * 
 */
package org.gob.gim.coercive.pagination;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.gob.gim.coercive.dto.criteria.PaymentInfractionsSearchCriteria;
import org.gob.gim.coercive.service.PaymentInfractionsService;
import org.gob.gim.common.PageableDataModel;
import org.gob.gim.common.ServiceLocator;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import ec.gob.gim.coercive.model.infractions.PaymentInfraction;


/**
 * @author macartuche
 *
 */
@Name("paymentInfractions")
@Scope(ScopeType.CONVERSATION)
public class PaymentInfractionsDataModel extends
		PageableDataModel<PaymentInfraction, Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PaymentInfractionsSearchCriteria criteria;
	private PaymentInfractionsService paymentInfractionsService;

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
	public Long getId(PaymentInfraction object) {
		return object.getId();
	}

	@Override
	public List<PaymentInfraction> findObjects(int firstRow,
			int numberOfRows, String sortField, boolean descending) {
		List<PaymentInfraction> payments = paymentInfractionsService
				.findPaymentInfractionByCriteria(criteria, firstRow,
						numberOfRows);

		return payments;
	}

	@Override
	public PaymentInfraction getObjectById(Long id) {
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

}
