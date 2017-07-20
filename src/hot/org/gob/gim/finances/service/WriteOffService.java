package org.gob.gim.finances.service;

/**
 * 
 */


import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.finances.model.WriteOffRequest;
import ec.gob.gim.finances.model.DTO.ConsumptionPreviousDTO;
import ec.gob.gim.finances.model.DTO.WriteOffDetailDTO;
import ec.gob.gim.finances.model.DTO.WriteOffRequestDTO;

/**
 * @author rene
 *
 */
@Local
public interface WriteOffService {

	public String LOCAL_NAME = "/gim/WriteOffService/local";
	
	List<WriteOffDetailDTO> searchBondDetail(Long bond_number);
	
	List<WriteOffRequestDTO> findByCriteria(String number_request_criteria, String identification_number_criteria, String name_resident_criteria, Integer firstRow, Integer numberOfRows);
	
	Long findWriteOffRequestsNumber(String number_request_criteria, String identification_number_criteria, String name_resident_criteria);
	
	WriteOffRequestDTO findById(Long writeOffRequestId);
	
	List<ConsumptionPreviousDTO> findPreviousReading(Long watersupply_id, String _year, String _month);

}
