/**
 * 
 */
package org.gob.gim.common.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ec.gob.gim.common.model.Logs;

/**
 * @author Rene
 *
 */
@Stateless(name="LogService")
public class LogServiceBean implements LogService{
	
	@PersistenceContext
    private EntityManager em;
	
	@EJB
	CrudService crudService;
	
	@Override
	public Logs save(Logs log) {
		// TODO Auto-generated method stub
		return crudService.create(log);
	}
	
	

}
