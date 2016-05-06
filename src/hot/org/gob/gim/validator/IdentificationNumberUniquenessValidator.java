package org.gob.gim.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.gob.gim.common.ServiceLocator;
import org.gob.gim.common.service.ResidentService;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.ResourceBundle;

import ec.gob.gim.common.model.IdentificationType;

@Name("identificationNumberUniquenessValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class IdentificationNumberUniquenessValidator implements
		javax.faces.validator.Validator, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String message;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value != null) {
			String nid = value.toString().trim();
			try {
				Long residentId = (Long) component.getAttributes().get("residentId");
				IdentificationType identificationType = (IdentificationType) component.getAttributes().get("identificationType");
				System.out.println("REMEBER A DAY: identificationType "+identificationType);
				ResidentService residentService = ServiceLocator.getInstance().findResource(ResidentService.LOCAL_NAME);
				residentService.verifyUniqueness(residentId, nid, identificationType);
			} catch (Exception e) {
				message = ResourceBundle.instance().getString(e.getClass().getSimpleName());				
				throw new ValidatorException(new FacesMessage(message));
			}
		}
	}
	
}
