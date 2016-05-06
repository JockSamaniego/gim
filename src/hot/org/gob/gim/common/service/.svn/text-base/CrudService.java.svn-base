package org.gob.gim.common.service;


import java.util.List;
import java.util.Map;
import javax.ejb.Local;

/**
 *
 * @author gerson
 */
@Local
public interface CrudService {
	public String LOCAL_NAME = "/gim/CrudService/local";
	
    public <T> T create(T t);
    public <T> T find(Class type, Object id);
    public <T> T getReference(Class type, Object id);
    public <T> T update(T t);
    public void delete(Class type,Object id);
    public List findWithNamedQuery(String queryName);
    public List findWithNamedQuery(String queryName,int resultLimit);
    public List findByNativeQuery(String sql, Class type);
    public List findWithNamedQuery(String namedQueryName, Map parameters);
    public List findWithNamedQuery(String namedQueryName, Map parameters,int resultLimit);
}


