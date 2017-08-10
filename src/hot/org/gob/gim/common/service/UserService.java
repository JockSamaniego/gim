package org.gob.gim.common.service;

import javax.ejb.Local;

import ec.gob.gim.security.model.User;
 

@Local
public interface UserService {
	public String LOCAL_NAME = "/gim/UserService/local";
	public void lockAll();
	public void unlockAll();
	/**
	 * @tag cuentaUnica
	 * @return
	 */
	public User save(User user);
}
