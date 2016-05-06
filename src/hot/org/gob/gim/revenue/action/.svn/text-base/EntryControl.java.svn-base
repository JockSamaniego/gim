package org.gob.gim.revenue.action;

/**
 * @author wilman
 */

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.Controller;

import ec.gob.gim.revenue.model.Entry;

@Name("entryControl")
public class EntryControl extends Controller{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@In(create=true)
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public List<Entry> findCriteria(Object suggest){
		String pref = (String)suggest;
		Query query = entityManager.createNamedQuery("Entry.findByCriteria");
		query.setParameter("criteria", pref);
		return (List<Entry>)query.getResultList();
	}
}
