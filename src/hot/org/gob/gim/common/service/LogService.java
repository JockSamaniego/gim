/**
 * 
 */
package org.gob.gim.common.service;

import javax.ejb.Local;

import ec.gob.gim.common.model.Logs;

/**
 * @author Rene
 *
 */
@Local
public interface LogService {

	public String LOCAL_NAME = "/gim/LogService/local";
	
	public Logs save(Logs log);
	
}
