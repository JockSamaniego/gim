package org.gob.gim.revenue.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.common.service.CrudService;

import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.impugnment.Impugnment;
import ec.gob.gim.revenue.model.impugnment.criteria.ImpugnmentSearchCriteria;

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

	@Override
	public List<Impugnment> findAll() {
		Query query = entityManager.createQuery("select i from Impugnment i");
		return query.getResultList();
	}

	@Override
	public MunicipalBond findMunicipalBondForImpugnment(
			Integer numberInfringement) {
		try {
			Query query = entityManager.createNativeQuery("select mb.id from municipalbond mb inner join antreference ant on mb.adjunct_id = ant.id where ant.antnumber=:numberInfringement");
			query.setParameter("numberInfringement", numberInfringement);
			BigInteger municipalBondId = (BigInteger) query.getSingleResult();
			if(municipalBondId != null){
				Query query1 = entityManager.createNamedQuery("MunicipalBond.findById");
				query1.setParameter("municipalBondId", municipalBondId.longValue());
				List<MunicipalBond> results = query1.getResultList();
				if(!results.isEmpty()){
					return results.get(0);
				}
			}
			return null;
			
		} catch (javax.persistence.NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Impugnment> findImpugnmentsForCriteria(
			ImpugnmentSearchCriteria criteria) {
		Query query  = entityManager.createNamedQuery("Impugnment.findByCriteria");
		query.setParameter("numberInfringement", criteria.getNumberInfringement() == null ? 0 :criteria.getNumberInfringement());
		query.setParameter("numberProsecution", criteria.getNumberProsecution() ==null ? 0 :criteria.getNumberProsecution());
		return query.getResultList();
	}

	@Override
	public Impugnment findById(Long impugnmentId) {
		Query query = entityManager.createNamedQuery("Impugnment.findById");
		query.setParameter("impugnmentId", impugnmentId);
		List<Impugnment> resultList = query.getResultList();
		if(resultList.isEmpty()){
			return null;
		}
		return resultList.get(0);
	}
	
}
