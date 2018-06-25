package org.gob.gim.income.facade;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gob.gim.common.ServiceLocator;

public class QueryBondsAux {
	

	@PersistenceContext
	static EntityManager entityManager;
	
	public static boolean itemIsPayed(Long municipalbondID, String type) {
		
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);		
		return incomeService.checkIsPayed(municipalbondID, type);
	}


}
