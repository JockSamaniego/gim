package org.gob.gim.validator;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;

/**
 * @author Jock Samaniego
 */

@Name("validadorArea")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class ValidadorArea implements javax.faces.validator.Validator, Serializable{
	
	private static final String AREA_PATTERN = "((([1-9][1-9]*0*)+)|0)(.[0-9][0-9]?)?";

	private Pattern pattern;
	private Matcher matcher;
 
	public ValidadorArea(){
		  pattern = Pattern.compile(AREA_PATTERN);
	}
 
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
 
		matcher = pattern.matcher(value.toString());	
		if(!matcher.matches()){
			FacesMessage msg = new FacesMessage("Validación de Cantidad", "datos incorrectos!");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
		}
		
 
	}
	
	
}
