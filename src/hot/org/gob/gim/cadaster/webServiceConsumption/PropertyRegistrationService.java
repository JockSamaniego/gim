package org.gob.gim.cadaster.webServiceConsumption;

import javax.ejb.Local;

@Local
public interface PropertyRegistrationService {
	
	public String LOCAL_NAME = "/gim/PropertyRegistrationService/local";
	
	public PropertyWs findByRegistrationForm(String code);

}
