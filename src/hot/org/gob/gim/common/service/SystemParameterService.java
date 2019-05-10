package org.gob.gim.common.service;

import java.util.List;

import javax.ejb.Local;

@Local
public interface SystemParameterService {
	public String LOCAL_NAME = "/gim/SystemParameterService/local";
	public void initialize();
	public void updateParameters();
	public <T> T findParameter(String name);
	public <T> T materialize(Class<?> klass, String parameterName);
	public List<Long> findListIds(String name);
}
