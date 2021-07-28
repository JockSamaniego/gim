/**
 * 
 */
package org.gob.gim.revenue.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.gob.gim.common.service.CrudService;

import ec.gob.gim.revenue.model.ExemptionCem;

/**
 * @author René
 *
 */
@Stateless(name = "ExemptiomCemService")
public class ExemptiomCemServiceBean implements ExemptiomCemService {

	@PersistenceContext
	EntityManager entityManager;

	@EJB
	CrudService crudService;

	@Override
	public ExemptionCem save(ExemptionCem exemptionCem) {
		return crudService.create(exemptionCem);
	}

	@Override
	public ExemptionCem findById(Long id) {
		return crudService.find(ExemptionCem.class, id);
	}

}
