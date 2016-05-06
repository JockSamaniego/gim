package org.gob.gim.common.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author gerson
 */
@Stateless(name="CrudService")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CrudServiceBean implements CrudService {

    @PersistenceContext
    EntityManager em;

    @Override
    public <T> T create(T t) {
        this.em.persist(t);
        this.em.flush();
        return t;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T find(Class type, Object id) {
        return (T) this.em.find(type, id);
    }

    @Override
    public <T> T getReference(Class type, Object id){
        return (T) this.em.getReference(type, id);
    }

    @Override
    public void delete(Class type, Object id) {
        Object ref = this.em.getReference(type, id);
        this.em.remove(ref);
        this.em.flush();
    }

    @Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public <T> T update(T t) {
        T merged = this.em.merge(t);
        this.em.flush();
        return merged;
    }

    @Override
    public List findWithNamedQuery(String namedQueryName) {
        return this.em.createNamedQuery(namedQueryName).getResultList();
    }

    @Override
    public List findWithNamedQuery(String namedQueryName, Map parameters) {
        return findWithNamedQuery(namedQueryName, parameters, 0);
    }

    @Override
    public List findWithNamedQuery(String queryName, int resultLimit) {
        return this.em.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    @Override
    public List findByNativeQuery(String sql, Class type) {
        return this.em.createNativeQuery(sql, type).getResultList();
    }

    @Override
    public List findWithNamedQuery(String namedQueryName, Map parameters, int resultLimit) {
        Set<Entry> rawParameters = parameters.entrySet();
        Query query = this.em.createNamedQuery(namedQueryName);
        if (resultLimit > 0) {
            query.setMaxResults(resultLimit);
        }
        for (Entry entry : rawParameters) {
            query.setParameter(entry.getKey().toString(), entry.getValue());
        }
        return query.getResultList();
    }
}
