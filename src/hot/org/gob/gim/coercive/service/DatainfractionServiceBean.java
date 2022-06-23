/**
 * 
 */
package org.gob.gim.coercive.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.gim.coercive.dto.criteria.DataInfractionSearchCriteria;
import org.gob.gim.coercive.dto.criteria.NotificationInfractionSearchCriteria;
import org.gob.gim.coercive.view.InfractionUserData;
import org.gob.gim.common.service.CrudService;

import ec.gob.gim.coercive.model.infractions.Datainfraction;
import ec.gob.gim.coercive.model.infractions.HistoryStatusInfraction;
import ec.gob.gim.coercive.model.infractions.NotificationInfractions;
import ec.gob.gim.coercive.model.infractions.PaymentNotification;

/**
 * @author René
 *
 */
@Stateless(name = "DatainfractionService")
public class DatainfractionServiceBean implements DatainfractionService {
	
	@PersistenceContext
	EntityManager entityManager;
	
	@EJB
	CrudService crudService;

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
	public Datainfraction updateDataInfraction(Datainfraction data) {

		Datainfraction dat = this.entityManager.merge(data);
		this.entityManager.flush();
		return dat;

	}

	@Override
	public Datainfraction getDataInfractionById(Long id) {
		Query query = entityManager
				.createQuery("SELECT d FROM Datainfraction d WHERE d.id=:id");
		query.setParameter("id", id);
		return (Datainfraction) query.getSingleResult();
	}

	@Override
	public List<Datainfraction> findDataInfractionByCriteria(
			DataInfractionSearchCriteria criteria, Integer firstRow,
			Integer numberOfRows) {
		
		String strQry = "SELECT d FROM Datainfraction d JOIN d.state s WHERE 1 = 1 ";
		
		if(criteria.getIdentification() != null && !criteria.getIdentification().isEmpty()){
			strQry += "AND d.identification =:identification ";
		}
		
		if(criteria.getName() != null && !criteria.getName().isEmpty()){
			strQry += "AND UPPER(d.name) like :name ";
		}
		
		if(criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()){
			strQry += "AND d.licensePlate =:licensePlate ";
		}
		
		if(criteria.getTicket() != null && !criteria.getTicket().isEmpty()){
			strQry += "AND d.ticket =:ticket ";
		}
		
		if(criteria.getStatus() != null){
			strQry += "AND s.id =:statusId ";
		}
		
		strQry += "ORDER BY d.name ASC "; 
		
		Query query = this.entityManager
				.createQuery(strQry);
		//query.setParameter("identification", identification);
		
		if(criteria.getIdentification() != null && !criteria.getIdentification().isEmpty()){
			query.setParameter("identification", criteria.getIdentification().trim());
		}
		
		if(criteria.getName() != null && !criteria.getName().isEmpty()){
			query.setParameter("name", "%"+ criteria.getName().trim().toUpperCase()+"%");
		}
		
		if(criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()){
			query.setParameter("licensePlate", criteria.getLicensePlate().trim());
		}
		
		if(criteria.getTicket() != null && !criteria.getTicket().isEmpty()){
			query.setParameter("ticket", criteria.getTicket().trim());
		}
		
		if(criteria.getStatus() != null){
			query.setParameter("statusId", criteria.getStatus().getId());
		}
		
		query.setFirstResult(firstRow);
		query.setMaxResults(numberOfRows);
		
		return query.getResultList();
	}

	@Override
	public Integer findDataInfractionNumber(
			DataInfractionSearchCriteria criteria) {
		
		String strQry = "SELECT COUNT(*) FROM Datainfraction d JOIN d.state s WHERE 1 = 1 ";
		
		if(criteria.getIdentification() != null && !criteria.getIdentification().isEmpty()){
			strQry += "AND d.identification =:identification ";
		}
			
		if(criteria.getName() != null && !criteria.getName().isEmpty()){
			strQry += "AND UPPER(d.name) like :name ";
		}
		
		if(criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()){
			strQry += "AND d.licensePlate =:licensePlate ";
		}
		
		if(criteria.getTicket() != null && !criteria.getTicket().isEmpty()){
			strQry += "AND d.ticket =:ticket ";
		}
		
		if(criteria.getStatus() != null){
			strQry += "AND s.id =:statusId ";
		}
		
		
		Query query = this.entityManager
				.createQuery(strQry);
		//query.setParameter("identification", identification);
		
		if(criteria.getIdentification() != null && !criteria.getIdentification().isEmpty()){
			query.setParameter("identification", criteria.getIdentification().trim());
		}
		
		if(criteria.getName() != null && !criteria.getName().isEmpty()){
			query.setParameter("name", "%"+ criteria.getName().trim().toUpperCase()+"%");
		}
		
		if(criteria.getLicensePlate() != null && !criteria.getLicensePlate().isEmpty()){
			query.setParameter("licensePlate", criteria.getLicensePlate().trim());
		}
		
		if(criteria.getTicket() != null && !criteria.getTicket().isEmpty()){
			query.setParameter("ticket", criteria.getTicket().trim());
		}
		
		if(criteria.getStatus() != null){
			query.setParameter("statusId", criteria.getStatus().getId());
		}
		
		
		Long size = (Long) query.getSingleResult();

		return size.intValue();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<PaymentNotification> findPaymentsByNotification(Long notificationId) {
		Query query = entityManager
				.createQuery("SELECT pnotif FROM PaymentNotification pnotif "
						+ "JOIN fetch pnotif.finantialInstitution "
						+ "JOIN fetch pnotif.cashier "
						+ "JOIN fetch pnotif.paymentType "
						+ "WHERE pnotif.notification.id=:notificationId "
						+ "order by pnotif.date, pnotif.time");
		query.setParameter("notificationId", notificationId);		
		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public InfractionUserData userData(Long notificationId) {
		Query query = entityManager
				.createQuery("SELECT distinct "
							+"NEW org.gob.gim.coercive.view.InfractionUserData(di.name, di.identification, di.email, di.phoneSms, count(di)) "
							+"FROM Datainfraction di "
							+"WHERE di.notification.id=:notificationId "
							+"GROUP BY di.name, di.identification, di.email, di.phoneSms ");
		query.setParameter("notificationId", notificationId);		
		List<InfractionUserData>list =  query.getResultList();
		return (!list.isEmpty())? list.get(0): null;
	}
	
	
	/**
	 * Grabar un nuevo abono de coactiva-infraccion
	 * @param payment
	 */
	@Override
	public void savePaymentNotification(PaymentNotification payment){
		entityManager.persist(payment);
	}

	@Override
	public HistoryStatusInfraction saveHIstoryRecord(
			HistoryStatusInfraction record) {
		return this.crudService.create(record);
	}

	@Override
	public Datainfraction getDataInfractionWithHistoryById(Long id) {
		Query query = entityManager
				.createQuery("SELECT d FROM Datainfraction d LEFT JOIN d.statusChange s WHERE d.id=:id");
		query.setParameter("id", id);
		return (Datainfraction) query.getSingleResult();
	}
}
