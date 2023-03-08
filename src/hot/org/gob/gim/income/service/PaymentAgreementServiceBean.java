package org.gob.gim.income.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.gob.gim.income.model.PaymentAgreement;

@Stateless(name = "PaymentAgreementService")
public class PaymentAgreementServiceBean implements PaymentAgreementService{
	
	@PersistenceContext
	EntityManager em;

	@Override
	public Integer numberAgreementsActivesByResident(Long residentId) {
		
		Query query = em.createNamedQuery("PaymentAgreement.findNumberByResidentIdAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("isActive", Boolean.TRUE);
		Long size = (Long) query.getSingleResult();

		return size.intValue();
		
	}

	@Override
	public PaymentAgreement findActivePaymentResident(Long residentId) {
		Query query = em.createNamedQuery("PaymentAgreement.findByResidentIdAndStatus");
		query.setParameter("residentId", residentId);
		query.setParameter("isActive", Boolean.TRUE);
		
		List<PaymentAgreement> agreements = query.getResultList();
		if(agreements.size() > 0) {
			return agreements.get(0);
		}else {
			return null;
		}
		
	}

}
