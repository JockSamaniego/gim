package org.gob.gim.income.service;

import java.util.List;

import javax.ejb.Local;

/**
 *  * @author René Ortega
 *  * @Fecha 2019-05-30
 */

@Local
public interface PaymentLocalService {

	public String LOCAL_NAME = "/gim/PaymentLocalService/local";

	/*
	 * DEBITOS BANCARIOS
	 * By: René Ortega
	 * 2019-05-30
	 */
	public Boolean calculateDebts(List<Long> residentIds);
	
}
