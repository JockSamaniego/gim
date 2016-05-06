package org.gob.gim.income.action;

import java.util.Arrays;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.income.model.Money;
import ec.gob.gim.income.model.MoneyType;

@Name("moneyList")
public class MoneyList extends EntityQuery<Money> {

	private static final String EJBQL = "select money from Money money";

	private static final String[] RESTRICTIONS = {		
		"money.moneyType = #{moneyList.moneyType}",
		"lower(money.denomination) = #{moneyList.money.denomination}",		
		};

	private Money money = new Money();
	
	private MoneyType moneyType;
	
	public Money getMoney() {
		return money;
	}

	public void setMoney(Money money) {
		this.money = money;
	}

	public MoneyType getMoneyType() {		
		return moneyType;
	}

	public void setMoneyType(MoneyType moneyType) {	
		this.moneyType = moneyType;
	}

	public MoneyList() {
		setEjbql(EJBQL);		
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));		
		setMaxResults(25);
		setOrder("money.value");
	}
	
}
