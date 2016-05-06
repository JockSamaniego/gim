package org.gob.gim.common.service;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ec.gob.gim.common.model.SystemParameter;

@Stateless(name="SystemParameterService")
public class SystemParameterServiceBean implements SystemParameterService{
	
	public static Map<String, Object> parameters = new HashMap<String, Object>();
	
	@EJB
	CrudService crudService;
	// TODO codigo repetido en los siguientes dos metodos
	@SuppressWarnings("unchecked")
	public void initialize(){
		if (parameters.isEmpty()) {
			List<SystemParameter> params = crudService.findWithNamedQuery("SystemParameter.findAll");
            for (SystemParameter parameter : params) {
            	String key = parameter.getName();
            	Object value = getParameterValue(parameter);
                parameters.put(key, value);
            }
        }
	}
	
	@SuppressWarnings("unchecked")
	public void updateParameters(){
		parameters.clear();
		List<SystemParameter> params = crudService.findWithNamedQuery("SystemParameter.findAll");
        for (SystemParameter parameter : params) {
           	String key = parameter.getName();
           	Object value = getParameterValue(parameter);
            parameters.put(key, value);
        }        
	}
	
	@SuppressWarnings("unchecked")
	public <T> T findParameter(String name){
		T value = (T) parameters.get(name);
	    return value;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T materialize(Class<?> klass, String parameterName){
		Long id = findParameter(parameterName);
		return (T) crudService.find(klass, id);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getParameterValue(SystemParameter parameter){
	    if (parameter != null) {
	        try {
	            Class<?> klass = Class.forName(parameter.getClassName());
	            Constructor<?> constructor = klass.getConstructor(String.class);
	            T value = (T) constructor.newInstance(parameter.getValue());
	            return value;
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	    return null;
	}
	
}
