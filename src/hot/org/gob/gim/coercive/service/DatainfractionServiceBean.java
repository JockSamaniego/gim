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

import ec.gob.gim.coercive.model.infractions.Datainfraction;

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

}
