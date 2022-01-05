/**
 * 
 */
package org.gob.gim.coercive.service;

import java.util.List;

import javax.ejb.Local;

import org.gob.gim.coercive.dto.OverdueInfractionDTO;
import org.gob.gim.coercive.dto.OverdueInfractionsSearchCriteria;

/**
 * @author René
 *
 */
@Local
public interface OverdueInfractionsService {
	
	public String LOCAL_NAME = "/gim/OverdueInfractionsService/local";
	
	public List<OverdueInfractionDTO> findInfractions(OverdueInfractionsSearchCriteria criteria, Integer firstRow,Integer numberOfRows);
	
	public OverdueInfractionDTO findDtoById(Long id);
	
	public Integer findOverdueInfractionsNumber(OverdueInfractionsSearchCriteria criteria);
}
