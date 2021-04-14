/**
 * 
 */
package org.gob.gim.ws.service;

import javax.ejb.Local;

import org.gob.loja.gim.ws.dto.queries.response.BondDTO;

/**
 * @author René
 *
 */
@Local
public interface QueriesService {
	public String LOCAL_NAME = "/gim/QueriesService/local";
	
	BondDTO findBondById(Long bondId);
	
}
