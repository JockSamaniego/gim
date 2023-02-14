package org.gob.gim.income.service;


import javax.ejb.Local;

import ec.gob.gim.income.model.PaymentAgreement;

@Local
public interface PaymentAgreementService {
	
	public String LOCAL_NAME = "/gim/PaymentAgreementService/local";
	
	public Integer numberAgreementsActivesByResident(Long residentId);
	
	public PaymentAgreement findActivePaymentResident(Long residentId);
}
