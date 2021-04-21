/**
 * 
 */
package org.gob.gim.ws.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.loja.gim.ws.dto.queries.DebtsDTO;
import org.gob.loja.gim.ws.dto.queries.EntryDTO;
import org.gob.loja.gim.ws.dto.queries.LocalDTO;
import org.gob.loja.gim.ws.dto.queries.OperatingPermitDTO;
import org.gob.loja.gim.ws.dto.queries.response.BondDTO;

/**
 * @author René
 *
 */
@Local
public interface QueriesService {
	public String LOCAL_NAME = "/gim/QueriesService/local";
	
	BondDTO findBondById(Long bondId);
	List<OperatingPermitDTO> findOperatingPermits(String ruc);
	List<LocalDTO> findLocals(String cedRuc);
	EntryDTO findEntry(String code);
	DebtsDTO findDebts(String identification);
	
}
