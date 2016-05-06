package org.gob.gim.validator;

/**
 * @author wilman
 */

import java.io.Serializable;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Interpolator;

@Name("entryCodeValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class EntryCodeValidator implements
		javax.faces.validator.Validator, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//private static final String PATTERN="[a-zA-Z]{3}[0-9]{4}";
	private static final String PATTERN="[0-9]{5}|[A-Z]{3}-[A-Z]{3}";
	
	private String message;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		if (value != null) {
			String code = value.toString().trim().toUpperCase();
			
			if (!Pattern.matches(PATTERN, code)) {
				message = Interpolator.instance().interpolate(
						"#{messages['entry.wrongCode']}",
						new Object[0]);
				throw new ValidatorException(new FacesMessage(message));
			}

		} else {
			message = Interpolator.instance().interpolate(
					"#{messages['entry.requiredCode']}",
					new Object[0]);
			throw new ValidatorException(new FacesMessage(message));
		}

	}
	
}
