/**
 * 
 */
package org.gob.gim.coercive.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.common.service.CrudService;

import ec.gob.gim.coercive.model.infractions.HistoryStatusNotification;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;

/**
 * @author René
 *
 */
@Stateless(name = "NotificationInfractionsService")
public class NotificationInfractionsServiceBean implements NotificationInfractionsService{
		
	@PersistenceContext
	EntityManager entityManager;
	
	@EJB
	CrudService crudService;
	
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
		if (criteria.getIdentification() != null
				&& criteria.getIdentification().trim() != "") {
			qry += "AND i.identification =:identification ";
		}

		if (criteria.getNumber() != null && criteria.getNumber().trim() != "") {
			qry += "AND (n.year || '-' || n.number) like :num ";
		}

		qry += "ORDER BY n.year, n.number DESC ";
		Query query = this.entityManager.createQuery(qry);

		if (criteria.getIdentification() != null
				&& criteria.getIdentification().trim() != "") {
			query.setParameter("identification", criteria.getIdentification()
					.trim());
		}

		if (criteria.getNumber() != null && criteria.getNumber().trim() != "") {
			query.setParameter("num", "%" + criteria.getNumber().trim() + "%");
		}

		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		return query.getResultList();
	}

	@Override
	public NotificationInfractions findObjectById(Long id) {
		Query query = entityManager
				.createQuery("SELECT n FROM NotificationInfractions n JOIN n.infractions i WHERE n.id=:id");
		query.setParameter("id", id);
		// query.setFirstResult(rowCount.intValue()).setMaxResults(12);
		return (NotificationInfractions) query.getSingleResult();
	}

	@Override
	public Integer findNotificationInfractionsNumber(
			NotificationInfractionSearchCriteria criteria) {
		String qry = "SELECT count(DISTINCT n.id) FROM NotificationInfractions n JOIN n.infractions i WHERE 1=1 ";

		if (criteria.getIdentification() != null
				&& criteria.getIdentification().trim() != "") {
			qry += "AND i.identification =:identification ";
		}

		if (criteria.getNumber() != null && criteria.getNumber().trim() != "") {
			qry += "AND (n.year || '-' || n.number) like :num ";
		}

		Query query = this.entityManager.createQuery(qry);

		if (criteria.getIdentification() != null
				&& criteria.getIdentification().trim() != "") {
			query.setParameter("identification", criteria.getIdentification()
					.trim());
		}

		if (criteria.getNumber() != null && criteria.getNumber() != "") {
			query.setParameter("num", "%" + criteria.getNumber().trim() + "%");
		}

		Long size = (Long) query.getSingleResult();

		return size.intValue();
	}

	@Override
	public HistoryStatusNotification saveHistoryStatus(
			HistoryStatusNotification record) {
		return crudService.create(record);
	}

	@Override
	public NotificationInfractions updateNotification(
			NotificationInfractions notification) {
		return crudService.update(notification);
	}

	@Override
	public NotificationInfractions findWithHistoryById(Long id) {
		Query query = entityManager
				.createQuery("SELECT n FROM NotificationInfractions n LEFT JOIN n.statusChange s WHERE n.id=:id");
		query.setParameter("id", id);
		// query.setFirstResult(rowCount.intValue()).setMaxResults(12);
		return (NotificationInfractions) query.getSingleResult();
	}
	
}
