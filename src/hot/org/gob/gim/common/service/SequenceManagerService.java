package org.gob.gim.common.service;

import javax.ejb.Local;

import ec.gob.gim.finances.model.SequenceManager;
import ec.gob.gim.finances.model.SequenceManagerType;

@Local
public interface SequenceManagerService {
	
	public String LOCAL_NAME = "/gim/SequenceManagerService/local";
	
	Long getNextValue();
	SequenceManagerType getTypeByCode(String code);
	SequenceManager save(SequenceManager sequenceManager);
	
}