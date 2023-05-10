package org.gob.gim.coercive.service;

import java.math.BigInteger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name = "InfringementAgreementService")
public class InfringementAgreementServiceBean implements InfringementAgreementService{
	
	@PersistenceContext
	EntityManager entityManager;

	@Override
	public Boolean hasInfringementAgreementActive(String identification) {
		Query query = entityManager
				.createNativeQuery("SELECT COUNT(*) " + 
						"FROM infracciones.infringementagreement agr " + 
						"WHERE agr.active = TRUE " + 
						"AND agr.infractoridentification = :identification ");
		query.setParameter("identification", identification);		
		BigInteger result = (BigInteger) query.getSingleResult();

		return result.intValue() > 0;

	}

}
