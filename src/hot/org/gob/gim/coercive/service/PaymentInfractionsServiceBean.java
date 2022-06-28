/**
 * 
 */
package org.gob.gim.coercive.service;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.dto.criteria.PaymentInfractionsSearchCriteria;
import org.gob.gim.common.service.CrudService;

import ec.gob.gim.coercive.model.infractions.HistoryStatusNotification;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;
import ec.gob.gim.coercive.model.infractions.PaymentNotification;

/**
 * @author René
 *
 */
@Stateless(name = "PaymentInfractionsService")
public class PaymentInfractionsServiceBean implements PaymentInfractionsService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	CrudService crudService;

	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentNotification> getPaymentsByCriteria(
			PaymentInfractionsSearchCriteria criteria, Long statusid) {

		String qry = "SELECT pnotif FROM PaymentNotification pnotif "
				+ "JOIN FETCH pnotif.notification "
				+ "LEFT JOIN FETCH pnotif.finantialInstitution "
				+ "JOIN FETCH pnotif.cashier" 
				+ " WHERE 1=1 "
				+ " and pnotif.date between :from and :until "
				+ " and pnotif.status.id=:statusid";
		qry += " ORDER BY pnotif.date desc, pnotif.time DESC ";

		Query query = this.entityManager.createQuery(qry);
		query.setParameter("from", criteria.getFrom());
		query.setParameter("until", criteria.getUntil());
		query.setParameter("statusid", statusid);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentNotification> findPaymentInfractionByCriteria(
			PaymentInfractionsSearchCriteria criteria, Integer firstRow,
			Integer numberOfRows) {

		String qry = "SELECT pnotif FROM PaymentNotification pnotif "
				+ " WHERE 1=1 " + " and pnotif.date between :from and :until "
				+ " and pnotif.status.id=:statusid";
		qry += " ORDER BY pnotif.date desc, pnotif.time DESC ";

		Query query = this.entityManager.createQuery(qry);
		query.setParameter("from", criteria.getFrom());
		query.setParameter("until", criteria.getUntil());
		query.setParameter("statusid", criteria.getStatusid());

		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);

		return query.getResultList();
	}

	@Override
	public PaymentNotification findObjectById(Long id) {
		Query query = entityManager
				.createQuery("SELECT pnotif FROM PaymentNotification pnotif "
						+ "WHERE pnotif.id=:id");
		query.setParameter("id", id);
		return (PaymentNotification) query.getSingleResult();
	}

	@Override
	public Integer findPaymentsNumber(PaymentInfractionsSearchCriteria criteria) {
		String qry = "SELECT count(DISTINCT pnotif.id) FROM PaymentNotification pnotif "
				+ " WHERE 1=1 "
				+ " and pnotif.date between :from and :until "
				+ " and pnotif.status.id=:statusid";

		Query query = this.entityManager.createQuery(qry);
		query.setParameter("from", criteria.getFrom());
		query.setParameter("until", criteria.getUntil());
		query.setParameter("statusid", criteria.getStatusid());
		Long size = (Long) query.getSingleResult();

		return size.intValue();
	}

	@Override
	public BigDecimal getTotalByCriteriaSearch(
			PaymentInfractionsSearchCriteria criteria) {
		
		String qry = "SELECT SUM(pnotif.value) FROM PaymentNotification pnotif "
				+ " WHERE 1=1 " + " and pnotif.date between :from and :until "
				+ " and pnotif.status.id=:statusid";

		Query query = this.entityManager.createQuery(qry);
		query.setParameter("from", criteria.getFrom());
		query.setParameter("until", criteria.getUntil());
		query.setParameter("statusid", criteria.getStatusid());
		
		return (BigDecimal) query.getSingleResult();
		
	}
}
