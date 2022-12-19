package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.commercial.model.Business;
import ec.gob.gim.commercial.model.FireRates;


@Local
public interface BusinessService {
	
	public String LOCAL_NAME = "/gim/BusinessService/local";

	Business save(Business business) throws Exception;
	
	Business update(Business business) throws Exception;
	
	List<Business> listLocals(Long residentId);
	
	List<FireRates> searchFireRates(String criteria);
	
}
