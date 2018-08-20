package org.gob.loja.gim.ws.service;

import javax.ejb.Local;

import org.gob.loja.gim.ws.dto.ServiceRequest;

@Local
public interface WorkDayService {
	
	public String LOCAL_NAME = "/gim/GimService/local";

	//public Boolean workDay(ServiceRequest request, HttpServletRequest hrs) throws  InvalidUser, DateNoAvalible, InterestRateNoDefined, TaxesNoDefined, CashierOpen, ExistsDuplicateUsers, Exception;
	
	/*public Boolean isWorkdayOpen(org.loxageek.common.ws.ServiceRequest arg0) ;
	public Boolean areThereOpenTills();
	public Boolean closeTills();*/
	public Boolean openWorkday(ServiceRequest request);
	public Boolean closeWorkday(ServiceRequest request);
	//public Boolean openBankTills();*/
	
}
