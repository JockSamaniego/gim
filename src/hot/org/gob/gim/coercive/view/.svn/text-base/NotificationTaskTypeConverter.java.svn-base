package org.gob.gim.coercive.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import ec.gob.gim.coercive.model.NotificationTaskType;

@Name("notificationTaskTypeConverter")
@org.jboss.seam.annotations.faces.Converter(forClass=NotificationTaskType.class)
@BypassInterceptors

public class NotificationTaskTypeConverter implements Converter{
	
	private EntityManager entityManager;

    public NotificationTaskTypeConverter() {
    }

    Long getKey(String value) {
        Long key;
        key = Long.valueOf(value);
        return key;
    }

    String getStringKey(Long value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }
    
    String getStringKey(String value) {
        StringBuilder sb = new StringBuilder();
        sb.append(value);
        return sb.toString();
    }

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String value) {
		if (value == null || value.length() == 0) {
			return null;
		}
		NotificationTaskType ntt = null;
		Query query;
		try {
			query = getEntityManager().createNamedQuery("NotificationTaskType.findByName");
			query.setParameter("name", value);
			try {
				ntt = (NotificationTaskType) query.getSingleResult();
			} catch (Exception e) {
			}

			return ntt;
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
		
	}

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
		if (object == null) {
            return null;
        }
        if (object instanceof NotificationTaskType) {
            NotificationTaskType e = (NotificationTaskType) object;
            return getStringKey(e.getName());
        }
        else
            throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + NotificationTaskType.class.getName());
	}

	public EntityManager getEntityManager() throws NamingException {		
		if (this.entityManager == null){
			this.entityManager = (EntityManager) Component.getInstance("entityManager");
		}
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	
}
