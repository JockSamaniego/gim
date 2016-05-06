package org.gob.gim.audit.action;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import ec.gob.gim.audit.model.AuditedField;

public class AuditedFieldConverter implements Converter {
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        System.out.println("AuditedFieldConverter value: "+value.toString());
        AuditedField field = new AuditedField();
        field.setName(value);
        return field;
    }
    
    
    public String getAsString(FacesContext context, 
              UIComponent component, Object value) {
             return value.toString();
    }
}
