package org.gob.gim.revenue.action;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.revenue.model.FinancialInstitution;
import ec.gob.gim.revenue.model.FinancialInstitutionType;

@Name("financialInstitutionHome")
public class FinancialInstitutionHome extends EntityHome<FinancialInstitution> {
	
	private static final long serialVersionUID = 1L;

	public void setFinancialInstitutionId(Long id) {
		setId(id);
	}

	public Long getFinancialInstitutionId() {
		return (Long) getId();
	}

	public void load() {
		if (isIdDefined()) {
			wire();
		}
	}

	public void wire() {
		getInstance();
	}

	public boolean isWired() {
		return true;
	}

	public FinancialInstitution getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	@Factory("financialInstitutionTypes")
	public List<FinancialInstitutionType> loadGarbageCollections() {
		return Arrays.asList(FinancialInstitutionType.values());
	}

	@SuppressWarnings("unchecked")
	@Factory("creditCardEmisors")
	public List<FinancialInstitution> findFinancialInstitutionsCreditCardEmitors() {
		Query query = getPersistenceContext().createNamedQuery(
				"FinancialInstitution.findByType");	
		query.setParameter("type", FinancialInstitutionType.CREDIT_CARD_EMISOR);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Factory("banks")
	public List<FinancialInstitution> findFinancialInstitutionsBanks() {
		Query query = getPersistenceContext().createNamedQuery(
				"FinancialInstitution.findByType");		
		query.setParameter("type", FinancialInstitutionType.BANK);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Factory("financialInstitutions")
	public List<FinancialInstitution> findFinancialInstitutions() {
		Query query = getPersistenceContext().createNamedQuery(
				"FinancialInstitution.findAll");		
		return query.getResultList();
	}
	

}
