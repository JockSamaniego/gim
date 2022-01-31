/**
 * 
 */
package org.gob.gim.coercive.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;

/**
 * @author René
 *
 */
@Stateless(name = "DatainfractionService")
public class DatainfractionServiceBean implements DatainfractionService {
	
	@PersistenceContext
	EntityManager entityManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gob.gim.coercive.service.DatainfractionService#
	 * findInfractionsByIdentification(java.lang.String)
	 */
	@Override
	public List<Datainfraction> findInfractionsByIdentification(
			String identification) {
		Query query = this.entityManager
				.createQuery("SELECT d FROM Datainfraction d WHERE d.identification=:identification AND d.notification IS NULL");
		query.setParameter("identification", identification);
		return query.getResultList();
	}

	@Override
	public Long getNextValue() {
		Query query = this.entityManager
				.createNativeQuery("SELECT nextval('infracciones.notificationInfraction_seq')");
		BigInteger _ret = (BigInteger) query.getSingleResult();
		return _ret.longValue();
	}

	@Override
	public List<NotificationInfractions> getNotifications(List<Long> ids) {
		Query query = this.entityManager
				.createQuery("SELECT n FROM NotificationInfractions n WHERE n.id IN (:ids)");
		query.setParameter("ids", ids);
		List<NotificationInfractions> l = query.getResultList();
		return query.getResultList();
	}

	@Override
	public List<NotificationInfractions> findNotificationInfractionByCriteria(
			NotificationInfractionSearchCriteria criteria, Integer firstRow,
			Integer numberOfRows) {
		
		String qry = "SELECT DISTINCT n FROM NotificationInfractions n JOIN n.infractions i WHERE 1=1 ";
		if(criteria.getIdentification() != null && criteria.getIdentification().trim() != ""){
			qry += "AND i.identification =:identification ";
		}
		
		if(criteria.getNumber() != null && criteria.getNumber().trim() != ""){
			qry += "AND (n.year || '-' || n.number) like :num ";
		}
		
		qry+= "ORDER BY n.year, n.number ASC ";
		Query query = this.entityManager
				.createQuery(qry);
		
		if(criteria.getIdentification() != null && criteria.getIdentification().trim() != ""){
			query.setParameter("identification", criteria.getIdentification().trim());
		}
		
		if(criteria.getNumber() != null && criteria.getNumber().trim() != ""){
			query.setParameter("num", "%"+criteria.getNumber().trim()+"%");
		}
		
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		return query.getResultList();
	}

	@Override
	public NotificationInfractions findObjectById(Long id) {
		Query query = entityManager.createQuery("SELECT n FROM NotificationInfractions n JOIN n.infractions i WHERE n.id=:id");
		query.setParameter("id", id);
		// query.setFirstResult(rowCount.intValue()).setMaxResults(12);
		return (NotificationInfractions) query.getSingleResult();
	}

	@Override
	public Integer findNotificationInfractionsNumber(
			NotificationInfractionSearchCriteria criteria) {
		String qry = "SELECT count(DISTINCT n.id) FROM NotificationInfractions n JOIN n.infractions i WHERE 1=1 ";
		
		if(criteria.getIdentification() != null && criteria.getIdentification().trim() != ""){
			qry += "AND i.identification =:identification ";
		}
		
		if(criteria.getNumber() != null && criteria.getNumber().trim() != ""){
			qry += "AND (n.year || '-' || n.number) like :num ";
		}
		
		Query query = this.entityManager
				.createQuery(qry);
		
		if(criteria.getIdentification() != null && criteria.getIdentification().trim() != ""){
			query.setParameter("identification", criteria.getIdentification().trim());
		}
		
		if(criteria.getNumber() != null && criteria.getNumber() != ""){
			query.setParameter("num", "%" + criteria.getNumber().trim()+"%");
		}
		
		Long size = (Long) query.getSingleResult();

		return size.intValue();
	}

	@Override
	public Datainfraction updateDataInfraction(Datainfraction data) {
		
		Datainfraction dat = this.entityManager.merge(data);
		this.entityManager.flush();
		return dat;
		
	}

	@Override
	public Datainfraction getDataInfractionById(Long id) {
		Query query = entityManager.createQuery("SELECT d FROM Datainfraction d WHERE d.id=:id");
		query.setParameter("id", id);
		return (Datainfraction) query.getSingleResult();	
	}

}
