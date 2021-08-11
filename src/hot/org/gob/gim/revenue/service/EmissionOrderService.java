package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.revenue.model.EmissionOrder;
import ec.gob.gim.revenue.model.MunicipalBond;
import ec.gob.gim.revenue.model.DTO.EmissionOrderDTO;
import ec.gob.gim.revenue.model.criteria.EmissionOrderSearchCriteria;
import ec.gob.gim.security.model.User;

@Local
public interface EmissionOrderService{
	
	public String LOCAL_NAME = "/gim/EmissionOrderService/local";		
	
	EmissionOrder createEmissionOrder(MunicipalBond municipalBond, User user) throws Exception;
	
	EmissionOrder createEmissionOrder(EmissionOrder e) throws Exception;
	
	EmissionOrder createEmissionOrder(List<MunicipalBond> municipalBonds) throws Exception;
	
	EmissionOrder save(EmissionOrder emissionOrder) throws Exception;
	
	EmissionOrder update(EmissionOrder emissionOrder) throws Exception;
	
	EmissionOrder findById(Long id);
	
	List<EmissionOrderDTO> searchOrders(EmissionOrderSearchCriteria criteria);
	
}
