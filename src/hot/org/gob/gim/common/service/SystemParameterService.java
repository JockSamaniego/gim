package org.gob.gim.common.service;

import java.util.List;

import javax.ejb.Local;

@Local
public interface SystemParameterService {
	public String LOCAL_NAME = "/gim/SystemParameterService/local";
	public static String ENTRY_ID_GARAJE_SERVICE_UMTTT = "ENTRY_ID_GARAJE_SERVICE_UMTTT";
	public static String ENTRY_ID_GRUA_SERVICE_UMTTT = "ENTRY_ID_GRUA_SERVICE_UMTTT";
	public static String ROLE_NAME_UCOT_SYSTEM = "ROLE_NAME_UCOT_SYSTEM";
	public static String ROLE_NAME_CRV_SYSTEM = "ROLE_NAME_CRV_SYSTEM";
	public static String USER_NAME_ANT_SERVICE = "USER_NAME_ANT_SERVICE";
	public static String PASSWORD_ANT_SERVICE = "PASSWORD_ANT_SERVICE";
	public static String PATH_FILES_BINNACLE_CRV = "PATH_FILES_BINNACLE_CRV";
	public void initialize();
	public void updateParameters();
	public <T> T findParameter(String name);
	public <T> T materialize(Class<?> klass, String parameterName);
	public List<Long> findListIds(String name);
}
