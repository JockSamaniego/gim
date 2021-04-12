/**
 * 
 */
package org.gob.gim.revenue.action;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.action.UserSession;
import org.gob.gim.common.service.SystemParameterService;
import org.gob.gim.revenue.facade.RevenueService;
import org.gob.gim.revenue.service.EmissionOrderService;
import org.gob.gim.revenue.service.MunicipalBondService;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.framework.EntityController;

import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.MunicipalBondStatus;
import ec.gob.gim.revenue.model.DTO.EmissionOrderDTO;
import ec.gob.gim.revenue.model.criteria.EmissionOrderSearchCriteria;

/**
 * @author René
 *
 */
@Name("emissionOrder1Home")
@Scope(ScopeType.CONVERSATION)
public class EmissionOrder1Home extends EntityController {

	public static String SYSTEM_PARAMETER_SERVICE_NAME = "/gim/SystemParameterService/local";

	private EmissionOrderSearchCriteria criteria;

	private EmissionOrderService emissionOrderService;

	private List<EmissionOrderDTO> orders = new ArrayList<EmissionOrderDTO>();

	private MunicipalBondService municipalBondService;

	private SystemParameterService systemParameterService;

	private RevenueService revenueService;

	private MunicipalBondStatus rejectedBondStatus;

	@In
	UserSession userSession;

	public EmissionOrder1Home() {
		this.loadOrders();
	}

	public void loadOrders() {
		initializeService();
		this.criteria = new EmissionOrderSearchCriteria();
	}

	public void initializeService() {

		if (emissionOrderService == null) {
			emissionOrderService = ServiceLocator.getInstance().findResource(
					emissionOrderService.LOCAL_NAME);
		}
		if (municipalBondService == null) {
			municipalBondService = ServiceLocator.getInstance().findResource(
					municipalBondService.LOCAL_NAME);
		}
		if (systemParameterService == null) {
			systemParameterService = ServiceLocator.getInstance().findResource(
					SystemParameterService.LOCAL_NAME);
		}

		if (revenueService == null) {
			revenueService = ServiceLocator.getInstance().findResource(
					RevenueService.LOCAL_NAME);
		}

	}

	public EmissionOrderSearchCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(EmissionOrderSearchCriteria criteria) {
		this.criteria = criteria;
	}

	public List<EmissionOrderDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<EmissionOrderDTO> orders) {
		this.orders = orders;
	}

	/*
	 * OTROS METODOS
	 */

	public void searchOrders() {
		System.out.println(this.criteria);
		this.orders = this.emissionOrderService.searchOrders(criteria);
		System.out.println(this.orders);
	}

	public MunicipalBond loadFirstBond(String bondsIds) {
		String[] bondsArray = bondsIds.split(",");
		if (bondsArray.length > 0) {
			Long bondId = Long.parseLong(bondsArray[0]);
			return municipalBondService.findById(bondId);
		} else {
			return null;
		}

	}

	public void emitEmissionOrder(EmissionOrderDTO emissionOrder) {
		try {
			if (systemParameterService == null)
				systemParameterService = ServiceLocator.getInstance()
						.findResource(SYSTEM_PARAMETER_SERVICE_NAME);

			if (revenueService == null)
				revenueService = ServiceLocator.getInstance().findResource(
						RevenueService.LOCAL_NAME);
			revenueService.emit(emissionOrder.getNumber(), userSession
					.getPerson(), userSession.getFiscalPeriod().getStartDate());
			this.searchOrders();

		} catch (Exception e) {
			addFacesMessageFromResourceBundle("emissionOrder.errorWhileEmitting");
		}
	}

	public void rejectEmissionOrder(EmissionOrderDTO emissionOrderDTO) {
		try {
			EmissionOrder emissionOrder = this.emissionOrderService
					.findById(emissionOrderDTO.getNumber());
			if (systemParameterService == null)
				systemParameterService = ServiceLocator.getInstance()
						.findResource(SYSTEM_PARAMETER_SERVICE_NAME);

			if (revenueService == null)
				revenueService = ServiceLocator.getInstance().findResource(
						RevenueService.LOCAL_NAME);
			rejectedBondStatus = systemParameterService.materialize(
					MunicipalBondStatus.class,
					"MUNICIPAL_BOND_STATUS_ID_REJECTED");
			updateStatus(findMunicipalBondIds(emissionOrder.getId()),
					rejectedBondStatus);
			emissionOrder.setIsDispatched(true);
			this.emissionOrderService.update(emissionOrder);
			this.searchOrders();
		} catch (Exception e) {
			addFacesMessageFromResourceBundle("emissionOrder.errorWhileEmitting");
		}
	}

	private List<Long> findMunicipalBondIds(Long emissionOrderId) {
		Query query = getEntityManager().createNamedQuery(
				"EmissionOrder.MunicipalBondsIdsByEmissionOrderId");
		query.setParameter("id", emissionOrderId);
		return query.getResultList();
	}

	private void updateStatus(List<Long> selected, MunicipalBondStatus status) {
		revenueService.update(selected, status.getId(), userSession.getUser()
				.getId(), null, null);
	}

}
