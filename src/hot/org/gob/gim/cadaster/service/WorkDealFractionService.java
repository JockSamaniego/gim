/**
 * 
 */
package org.gob.gim.cadaster.service;

import java.util.List;

import javax.ejb.Local;

import ec.gob.gim.cadaster.model.WorkDealFraction;

/**
 * @author rene
 *
 */
@Local
public interface WorkDealFractionService {

	public String LOCAL_NAME = "/gim/WorkDealFractionService/local";
	
	List<WorkDealFraction> findFractions(Long workDeal_id,Integer firstRow,Integer numberOfRows);
	
	Long findWorkDealFractionsNumber(Long workDeal_id);
	
}
