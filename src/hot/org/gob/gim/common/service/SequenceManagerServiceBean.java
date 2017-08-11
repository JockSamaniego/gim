/**
 * 
 */
package org.gob.gim.common.service;

import java.math.BigInteger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.gob.gim.finances.model.SequenceManager;
import ec.gob.gim.finances.model.SequenceManagerType;

/**
 * @author René
 *
 */
@Stateless(name="SequenceManagerService")
public class SequenceManagerServiceBean implements SequenceManagerService {
	
	@PersistenceContext
    private EntityManager em;

	@Override
	public Long getNextValue() {
		Query query = this.em
				.createNativeQuery("SELECT nextval('sequence_manager')");
		BigInteger _ret = (BigInteger) query.getSingleResult();
		return _ret.longValue();
	}

	@Override
	public SequenceManagerType getTypeByCode(String code) {
		Query query = this.em.createNamedQuery("SequenceManagerType.findByCode");
		query.setParameter("code", code);
		return (SequenceManagerType) query.getSingleResult();
	}

	@Override
	public SequenceManager save(SequenceManager sequenceManager) {
		return this.em.merge(sequenceManager);
	}
}