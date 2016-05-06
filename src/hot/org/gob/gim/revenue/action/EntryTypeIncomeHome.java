package org.gob.gim.revenue.action;

import java.util.List;

import javax.persistence.Query;

import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;
import org.jboss.seam.log.Log;

import ec.gob.gim.revenue.model.EntryTypeIncome;

@Name("entryTypeIncomeHome")
public class EntryTypeIncomeHome extends EntityHome<EntryTypeIncome> {

	private static final long serialVersionUID = 1L;

	@In(create = true)
	EntryHome entryHome;
	
	@Logger
	Log logger;

	public void setEntryTypeIncomeId(Long id) {
		setId(id);
	}

	public Long getEntryTypeIncomeId() {
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

	public EntryTypeIncome getDefinedInstance() {
		return isIdDefined() ? getInstance() : null;
	}
	
	@Factory("entryTypesIncome")
	@SuppressWarnings("unchecked")
	public List<EntryTypeIncome> findEntryTypesIncome(){
		Query query = getPersistenceContext().createNamedQuery("EntryTypeIncome.findAll");
		return query.getResultList();
	}
		
}
