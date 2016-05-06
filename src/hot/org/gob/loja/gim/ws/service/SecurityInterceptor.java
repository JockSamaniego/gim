package org.gob.loja.gim.ws.service;

import java.security.MessageDigest;
import java.util.List;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.gob.loja.gim.ws.dto.ServiceRequest;
import org.gob.loja.gim.ws.exception.AccountIsBlocked;
import org.gob.loja.gim.ws.exception.AccountIsNotActive;
import org.gob.loja.gim.ws.exception.InvalidUser;

import ec.gob.gim.security.model.User;

public class SecurityInterceptor {
	@PersistenceContext
    private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@AroundInvoke
	public Object verifyAccess(InvocationContext invocationContext) throws Exception{
		System.out.println("Entering method:  " + invocationContext.getMethod().getName());
		Object[] parameters = invocationContext.getParameters();
		
		String userName;
		String pwd;
				
		if (parameters[0] instanceof ServiceRequest){
			ServiceRequest request = (ServiceRequest) parameters[0];
			userName = request.getUsername();
			pwd = request.getPassword();
		}else{
			userName = (String)parameters[0];
			pwd = (String)parameters[1];
		}

		System.out.println("User verifyAccess: " + userName);
		String hashPassword = hash(pwd);
		Query query = entityManager.createNamedQuery("User.findByUsernameAndPassword");
		query.setParameter("name", userName);
		query.setParameter("password", hashPassword);
		
		
		List<User> users = query.getResultList(); 
		query = null;
		if(users != null && users.size() == 1){
			User user = users.get(0);
			if (!user.getIsActive()) {
				throw new AccountIsNotActive();
			}
			
			if (user.getIsBlocked()) {
				throw new AccountIsBlocked();
			}
			user = null;
			users = null;
			return invocationContext.proceed();
		} 
		throw new InvalidUser();
	}
	
	private String hash(String plainTextPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(plainTextPassword.getBytes("UTF-8"));
			byte[] rawHash = digest.digest();
			return new String(org.jboss.seam.util.Hex.encodeHex(rawHash));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
