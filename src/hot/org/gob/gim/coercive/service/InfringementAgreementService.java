package org.gob.gim.coercive.service;

import javax.ejb.Local;

@Local
public interface InfringementAgreementService {
	
	public String LOCAL_NAME = "/gim/InfringementAgreementService/local";
	
	public Boolean hasInfringementAgreementActive(String identification);

}
