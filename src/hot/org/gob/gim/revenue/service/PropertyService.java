package org.gob.gim.revenue.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.cadaster.model.Property;
import ec.gob.gim.revenue.model.DTO.PropertyDTO;

/**
 *  * @author René Ortega
 *  * @Fecha 2018-02-20
 */

@Local
public interface PropertyService {

	public String LOCAL_NAME = "/gim/PropertyService/local";

	public List<PropertyDTO> findByResidentIds(List<Long> idsResidents, List<Long> idsProperties);
	
	public Property findPropertyById(Long id);
	
}
