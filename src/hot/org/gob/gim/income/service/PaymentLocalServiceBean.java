package org.gob.gim.income.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.service.CrudService;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.income.facade.IncomeService;
import org.gob.gim.income.facade.IncomeServiceBean;
import org.gob.gim.revenue.exception.EntryDefinitionNotFoundException;

import ec.gob.gim.revenue.model.MunicipalBondType;

/**
 * * @author René Ortega * @Fecha 2019-05-30
 */

@Stateless(name = "PaymentLocalService")
public class PaymentLocalServiceBean implements PaymentLocalService {

	@PersistenceContext
	EntityManager em;

	@EJB
	CrudService crudService;

	@EJB
	private SystemParameterService systemParameterService;

	@EJB
	private IncomeService incomeService;

	@SuppressWarnings("unchecked")
	private List<Long> hasPendingBonds(Long taxpayerId) {
		Long pendingBondStatusId = systemParameterService
				.findParameter(IncomeServiceBean.PENDING_BOND_STATUS);
		Query query = em.createNamedQuery("Bond.findIdsByStatusAndResidentId");
		query.setParameter("residentId", taxpayerId);
		query.setParameter("municipalBondType", MunicipalBondType.CREDIT_ORDER);
		query.setParameter("pendingBondStatusId", pendingBondStatusId);
		List<Long> ids = query.getResultList();
		// System.out.println("PENDING BONDS TOTAL ---->" + ids.size());
		return ids;
	}

	@Override
	public Boolean calculateDebts(List<Long> residentIds) {

		try {
			System.out.println("LLega al calculateDebts");

			for (Long residentId : residentIds) {
				List<Long> pendingBondIds = hasPendingBonds(residentId);
				if (pendingBondIds.size() > 0) {

					incomeService.calculatePayment(new Date(), pendingBondIds,
							true, true);
				}
			}

		} catch (EntryDefinitionNotFoundException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

}
