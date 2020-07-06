package org.gob.gim.common.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.gim.common.exception.IdentificationNumberExistsException;
import org.gob.gim.common.exception.InvalidIdentificationNumberException;
import org.gob.gim.common.exception.NonUniqueIdentificationNumberException;

import ec.gob.gim.common.model.IdentificationType;
import ec.gob.gim.common.model.Resident;
import ec.gob.loja.client.model.UserWS;

@Local
public interface ResidentService {
	
	public String LOCAL_NAME = "/gim/ResidentService/local";

	Resident find(Long id);
	Resident find(String identificationNumber) throws NonUniqueIdentificationNumberException;
	List<Resident> findByCriteria(String criteria);
	void verifyUniqueness(Long id, String identificationNumber, IdentificationType identificationType) throws IdentificationNumberExistsException;
	void save(Resident resident) throws IdentificationNumberExistsException;
	Boolean isPublicInstitution(Long residentId);
	void loadPublicInstitutions();
	void updatePublicInstitutions();
	
	String updateUserIntoEBilling(UserWS user);
	
	Resident saveResident(Resident resident) throws IdentificationNumberExistsException;
	
}
