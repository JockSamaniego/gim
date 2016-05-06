package org.gob.gim.common.service;

import javax.ejb.Local;

@Local
public interface UserService {
	public String LOCAL_NAME = "/gim/UserService/local";
	public void lockAll();
	public void unlockAll();
}
