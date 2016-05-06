package org.gob.gim.common;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.faces.Converter;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

import ec.gob.gim.common.model.Identifiable;


@Name("org.gob.gim.common.IdentifiableConverter")
@Scope(CONVERSATION)
@Converter(forClass=Identifiable.class)
@BypassInterceptors
public class IdentifiableConverter implements  javax.faces.convert.Converter, Serializable {

	private static final long serialVersionUID = 795578966690874286L;
	
	Map<String, Identifiable> map = new HashMap<String, Identifiable>();
	
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {	
		
		if(arg2 == null) return null;
		
		Object obj = map.get(arg2);
		return obj;
	}

	@SuppressWarnings("unchecked")
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		
		if(arg2 == null) {
			return null;
		}
		
		if(!(arg2 instanceof Identifiable)) {
			throw new IllegalArgumentException("Cannot convert:" + arg2 + " must be an instanceof Identifiable");
		}
		Identifiable identifiable = (Identifiable)arg2;
		Long id = identifiable.getId();
		/**
		 * defensive measure in case we use this converter inside the seam conversation but with different types
		 * and key values are overlapping between types.
		 */
		
		map.put(id.toString(), identifiable);
		return id.toString();
	}

}
