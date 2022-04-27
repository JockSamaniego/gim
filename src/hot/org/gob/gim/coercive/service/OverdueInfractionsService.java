/**
 * 
 */
package org.gob.gim.coercive.service;

import java.util.List;

import javax.ejb.Local;
import org.gob.gim.coercive.dto.InfractionItemDTO;
import org.gob.gim.coercive.dto.criteria.OverdueInfractionsSearchCriteria;

/**
 * @author René
 *
 */
@Local
public interface OverdueInfractionsService {
	
	public String LOCAL_NAME = "/gim/OverdueInfractionsService/local";
	
	public List<InfractionItemDTO> searchInfractionGroupByCriteria(OverdueInfractionsSearchCriteria criteria, Integer firstRow,Integer numberOfRows);
	
	public InfractionItemDTO findOverdueInfractionById(String identification, OverdueInfractionsSearchCriteria criteria);
	
	public Integer findOverdueInfractionsNumber(OverdueInfractionsSearchCriteria criteria);
	
	public Integer getTotalSyncInfractions();
}
