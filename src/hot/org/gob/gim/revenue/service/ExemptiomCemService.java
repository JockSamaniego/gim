/**
 * 
 */
package org.gob.gim.revenue.service;

import javax.ejb.Local;

import ec.gob.gim.revenue.model.ExemptionCem;

/**
 * @author René
 *
 */
@Local
public interface ExemptiomCemService {
	public String LOCAL_NAME = "/gim/ExemptiomCemService/local";
	
	public ExemptionCem save(ExemptionCem exemptionCem);
	
}
