package org.gob.gim.income.facade;

import java.math.BigDecimal;

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
	
	public static boolean applyDiscount(Long entryId, String groupingCode, Long residentId) {
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		return incomeService.verifyApplyDiscount(entryId, groupingCode, residentId);
	}

	public static BigDecimal getPercentageDiscount(String itemcode, String catalogcode, Long residentid, Long adjunctid){ 
		IncomeService incomeService = ServiceLocator.getInstance().findResource(IncomeService.LOCAL_NAME);
		return incomeService.checkHasDiscountCEM(itemcode, catalogcode, residentid, adjunctid);
	}
}
