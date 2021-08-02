package org.gob.gim.common.service;

import java.math.BigInteger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ec.gob.gim.common.model.Person;
import ec.gob.gim.security.model.User;

@Stateless(name = "UserService")
public class UserServiceBean implements UserService {

	@PersistenceContext
	private EntityManager em;

	@EJB
	CrudService crudService;

	@EJB
	private SystemParameterService systemParameterService;

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void lockAll() {
		String rootRoleName = systemParameterService
				.findParameter("ROLE_NAME_ADMINISTRATOR");
		// System.out.println("ROOT ROLE -----> "+rootRoleName);
		Query query = em.createNamedQuery("User.lockAll");
		query.setParameter("rootRoleName", rootRoleName);
		query.executeUpdate();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void unlockAll() {
		String rootRoleName = systemParameterService
				.findParameter("ROLE_NAME_ADMINISTRATOR");
		// System.out.println("ROOT ROLE -----> "+rootRoleName);
		Query query = em.createNamedQuery("User.unlockAll");
		query.setParameter("rootRoleName", rootRoleName);
		query.executeUpdate();
	}

	@Override
	public User save(User user) {
		Boolean status = false;
		User updateNewUpdate = new User();
		if (user.getId() != null) {
			updateNewUpdate = crudService.update(user);
		} else {
			updateNewUpdate = crudService.create(user);
		}
		return updateNewUpdate;
	}

	/**
	 * comprobar que el usuario tenga el rol emisor
	 * 
	 * @author René
	 * @since 2021-03-18
	 * @param Long
	 *            userId
	 * @param String
	 *            parameterRolName
	 * @return Boolean
	 */
	@Override
	public Boolean checkUserRole(Long userId, String parameterRolName) {
		Long roleId = systemParameterService.findParameter(parameterRolName);
		String sql = "select count(*) from role__user as ru where ru.users_id = :userId and ru.roles_id = :roleId";
		Query query = em.createNativeQuery(sql);
		query.setParameter("userId", userId);
		query.setParameter("roleId", roleId);
		BigInteger quantity = (BigInteger) query.getSingleResult();
		return quantity.intValue() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public User getUserByResident(Long residentId) {
		try {
			Query query = em.createNamedQuery("User.findByResidentId");
			query.setParameter("residentId", residentId);
			User user = (User) query.getSingleResult();
			return user;
		} catch (Exception e) {
			return null;
		}

	}

}
