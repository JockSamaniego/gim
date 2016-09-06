/**
 * 
 */
package org.gob.gim.cadaster.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.gob.gim.cadaster.model.WorkDealFraction;

/**
 * @author rene
 *
 */
@Stateless(name = "WorkDealFractionService")
public class WorkDealFractionServiceBean implements WorkDealFractionService{
	
	@PersistenceContext
	EntityManager entityManager;

	/* (non-Javadoc)
	 * @see org.gob.gim.cadaster.service.WorkDealFractionService#findFractions(java.lang.Long)
	 */
	@Override
	public List<WorkDealFraction> findFractions(Long workDeal_id,Integer firstRow,
			Integer numberOfRows) {
		// TODO Auto-generated method stub
		Query query = entityManager.createQuery("select wf from WorkDealFraction wf where wf.workDeal.id=:workDeal_id");
		query.setParameter("workDeal_id", workDeal_id);
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);
		return query.getResultList();
	}

	
	/* (non-Javadoc)
	 * @see org.gob.gim.cadaster.service.WorkDealFractionService#findWorkDealFractionsNumber(java.lang.Long)
	 */
	@Override
	public Long findWorkDealFractionsNumber(Long workDeal_id) {
		// TODO Auto-generated method stub
		Query query = entityManager.createQuery("SELECT count(wf) from WorkDealFraction wf where wf.workDeal.id=:workDeal_id");
		query.setParameter("workDeal_id", workDeal_id);
		Long size = (Long) query.getSingleResult();
		return size;
	}

}
