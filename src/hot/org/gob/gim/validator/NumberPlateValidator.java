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
 * @author Richard Armijos
 * @date 2015-06-30
 */

@Name("numberPlateValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class NumberPlateValidator implements javax.faces.validator.Validator, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private static final String PLATE_PATTERN = "^[a-zA-Z0-9&gt;&lt;_/.-]*$";
	//private static final String PLATE_PATTERN = "^([a-zA-Z0-9]){7}$";
	private static final String PLATE_PATTERN = "^([a-zA-Z0-9]){6,7}$";
	
 
	private Pattern pattern;
	private Matcher matcher;
 
	public NumberPlateValidator(){
		  
	}
 
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		pattern = Pattern.compile(PLATE_PATTERN);
		matcher = pattern.matcher(value.toString());
		if(!matcher.matches()){
			FacesMessage msg = new FacesMessage("Número de placa Incorrecto", "Formato de placa inválido.");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			throw new ValidatorException(msg);
 
		}
 
	}
}
