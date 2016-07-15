package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.service.CrudService;

import ec.gob.gim.revenue.model.impugnment.Impugnment;

/**
 *  * @author René Ortega
 *  * @Fecha 2016-07-14
 */


@Stateless(name = "ImpugnmentService")
public class ImpugnmentServiceBean implements ImpugnmentService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	CrudService crudService;

	@Override
	public Impugnment save(Impugnment impugnment) {
		return crudService.create(impugnment);
	}

	@Override
	public Impugnment update(Impugnment impugnment) {
		return crudService.update(impugnment);
	}

	@Override
	public List<Impugnment> findImpugnments(Integer numberProsecution,
			Integer numberInfringement, Integer firstRow, Integer numberOfRows) {
		Query query = entityManager.createQuery("select i from Impugnment i");
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		List<Impugnment> result = query.getResultList();

		return result;

	}

	@Override
	public Long findImpugnmentsNumber(Integer numberProsecution,
			Integer numberInfringement) {
//		Query query = entityManager.createQuery("select i from Impugnment i");
//		query.setFirstResult(firstRow);
//		query.setMaxResults(numberOfRows);
//
//		List<Impugnment> result = query.getResultList();

		return new Long(1);
	}
}
