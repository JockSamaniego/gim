package org.gob.gim.common.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.common.model.FiscalPeriod;

@Local
public interface FiscalPeriodService {
	public String LOCAL_NAME = "/gim/FiscalPeriodService/local";
	
	FiscalPeriod find(Long id);
	List<FiscalPeriod> findCurrent(Date current);
	List<FiscalPeriod> find();
}
