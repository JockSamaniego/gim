package org.gob.gim.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ServiceLocator {

    private Context initialContext;
    private Map<String, Object> cache;
    private static ServiceLocator instance = new ServiceLocator();

    private ServiceLocator(){
        try {
            Properties p = System.getProperties();
            System.out.println("POLICY ADDED");
            this.initialContext = new InitialContext(p);
            this.cache = Collections.synchronizedMap(new HashMap<String, Object>());
        } catch (NamingException ex) {
            System.err.printf("ERROR IN CONTEXT LOOKING UP %s because of %s while %s",
                    ex.getRemainingName(), ex.getCause(), ex.getExplanation());

        }
    }

    public static ServiceLocator getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
	public <T> T findResource(String resourceName) {
        T resource = null;
        try {
            if (this.cache.containsKey(resourceName)) {
                resource = (T) this.cache.get(resourceName);
            } else {
                resource = (T) initialContext.lookup(resourceName);
                this.cache.put(resourceName, resource);
            }
        } catch (NamingException ex) {
            System.err.printf("Error in CTX looking up %s because of %s while %s",
                    ex.getRemainingName(), ex.getCause(), ex.getExplanation());
        }
        return resource;
    }
}

