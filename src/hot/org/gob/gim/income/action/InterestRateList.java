package org.gob.gim.income.action;

import java.util.Arrays;
import java.util.Date;

import org.gob.gim.common.action.UserSession;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import ec.gob.gim.income.model.InterestRate;

@Name("interestRateList")
public class InterestRateList extends EntityQuery<InterestRate> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String EJBQL = "select interestRate from InterestRate interestRate";

	private static final String[] RESTRICTIONS = {
			"interestRate.beginDate >= #{interestRateList.beginDate}",
			"interestRate.endDate <= #{interestRateList.endDate}",};

	private Date beginDate;
	private Date endDate;
	
	private boolean isFirstTime = true;

	public boolean isFirstTime() {
		return isFirstTime;
	}

	public void setFirstTime(boolean isFirstTime) {
		this.isFirstTime = isFirstTime;
	}

	@In
	UserSession userSession;
	
	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	public InterestRateList() {
		setEjbql(EJBQL);
		setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
		setMaxResults(25);
		setOrder("beginDate desc");
		//setOrderDirection("desc");
	}
	
	public void setCurrentDate(){		
		if(isFirstTime){
			beginDate = userSession.getFiscalPeriod().getStartDate();
			isFirstTime = false;
		}				
	}

	/**
	 * @return the beginDate
	 */
	public Date getBeginDate() {
		return beginDate;
	}

	/**
	 * @param beginDate the beginDate to set
	 */
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	
	
}
