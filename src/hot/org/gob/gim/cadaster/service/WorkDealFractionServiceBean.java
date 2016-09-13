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
public class WorkDealFractionServiceBean implements WorkDealFractionService {

	@PersistenceContext
	EntityManager entityManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gob.gim.cadaster.service.WorkDealFractionService#findFractions(java
	 * .lang.Long)
	 */
	@Override
	public List<WorkDealFraction> findFractions(Long workDeal_id,
			String cadastralCode, Integer firstRow, Integer numberOfRows) {
		String qryBase = "select wf from WorkDealFraction wf where wf.workDeal.id=:workDeal_id";
		String conditionAdditional = " and wf.property.cadastralCode=:cadastralCode";
		// TODO Auto-generated method stub
		String finalQuery = qryBase;

		if (cadastralCode != null) {
			finalQuery = finalQuery + conditionAdditional;
		}
		Query query = entityManager.createQuery(finalQuery);
		query.setParameter("workDeal_id", workDeal_id);
		if (cadastralCode != null) {
			query.setParameter("cadastralCode", cadastralCode);
		}
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gob.gim.cadaster.service.WorkDealFractionService#
	 * findWorkDealFractionsNumber(java.lang.Long)
	 */
	@Override
	public Long findWorkDealFractionsNumber(Long workDeal_id,
			String cadastralCode) {
		// TODO Auto-generated method stub
		String qryBase = "SELECT count(wf) from WorkDealFraction wf where wf.workDeal.id=:workDeal_id";
		String conditionAdditional = " and wf.property.cadastralCode=:cadastralCode";
		String finalQuery = qryBase;

		if (cadastralCode != null) {
			finalQuery = finalQuery + conditionAdditional;
		}

		Query query = entityManager.createQuery(finalQuery);
		query.setParameter("workDeal_id", workDeal_id);
		if (cadastralCode != null) {
			query.setParameter("cadastralCode", cadastralCode);
		}
		Long size = (Long) query.getSingleResult();
		return size;
	}
	
	@Override
	public void updateWorkDealFraction(WorkDealFraction workDealFraction){
		this.entityManager.merge(workDealFraction);
		
	}
	
	@Override
	public WorkDealFraction findById(Long fractionId){
		Query query = entityManager.createQuery("SELECT wf FROM WorkDealFraction wf WHERE wf.id=:fractionId");
		query.setParameter("fractionId", fractionId);
		return (WorkDealFraction) query.getSingleResult();
	}
	
	@Override
	public void deleteWorkDealFraction(WorkDealFraction workDealFraction){
		this.entityManager.remove(entityManager.merge(workDealFraction));
		this.entityManager.flush();
	}
	
	
	

}
