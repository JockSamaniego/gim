package org.gob.gim.common.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless(name="UserService")
public class UserServiceBean implements UserService{
	
	@PersistenceContext
    private EntityManager em;
	
	@EJB
	private SystemParameterService systemParameterService; 
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void lockAll(){
		String rootRoleName = systemParameterService.findParameter("ROLE_NAME_ADMINISTRATOR");
		System.out.println("ROOT ROLE -----> "+rootRoleName);
		Query query = em.createNamedQuery("User.lockAll");
		query.setParameter("rootRoleName", rootRoleName);
		query.executeUpdate();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void unlockAll(){
		String rootRoleName = systemParameterService.findParameter("ROLE_NAME_ADMINISTRATOR");
		System.out.println("ROOT ROLE -----> "+rootRoleName);
		Query query = em.createNamedQuery("User.unlockAll");
		query.setParameter("rootRoleName", rootRoleName);
		query.executeUpdate();
	}
	
}
