/**
 * 
 */
package org.gob.gim.coercive.service;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Local;
import javax.persistence.Query;

import ec.gob.gim.coercive.model.infractions.Datainfraction;

/**
 * @author René
 *
 */
@Local
public interface DatainfractionService {
	
	public String LOCAL_NAME = "/gim/DatainfractionService/local";
	
	public List<Datainfraction> findInfractionsByIdentification(String identification);
	
	public Long getNextValue();

}
