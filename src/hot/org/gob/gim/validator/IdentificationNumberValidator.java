package org.gob.gim.validator;

/**
 * @author wilman
 */

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.gob.gim.common.action.ResidentHome;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.core.ResourceBundle;

import ec.gob.gim.common.model.Resident;

@Name("identificationNumberValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class IdentificationNumberValidator implements
		javax.faces.validator.Validator, Serializable {

	private static final long serialVersionUID = 1L;
	
	private String message;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value != null) {
			String identificationNumber = value.toString().trim();
			ResidentHome residentHome = (ResidentHome) Component.getInstance("residentHome");
			Resident resident = residentHome.getInstance();
			try{
				resident.isIdentificationNumberValid(identificationNumber);
			}catch(Exception e){
				message = ResourceBundle.instance().getString(e.getClass().getSimpleName());
				throw new ValidatorException(new FacesMessage(message));
			}
		} else {
			message = Interpolator.instance().interpolate(
					"#{messages['common.requiredIdentificationNumber']}",
					new Object[0]);
			throw new ValidatorException(new FacesMessage(message));
		}

	}
}
