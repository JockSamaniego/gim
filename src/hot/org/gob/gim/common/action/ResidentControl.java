package org.gob.gim.common.action;

/**
 * @author wilman
 */

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.Controller;

import ec.gob.gim.common.model.Resident;

@Name("residentControl")
public class ResidentControl extends Controller{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In(create=true)
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Resident> findCriteria(Object suggest){
		String pref = (String)suggest;
		Query query = entityManager.createNamedQuery("Resident.findByCriteria");
		query.setParameter("criteria", pref);
		return (List<Resident>)query.getResultList();
	}
}
