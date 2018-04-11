package org.gob.gim.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.gob.gim.revenue.action.MunicipalBondHome;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Interpolator;

/**
 * @author rfam
 * @date 2018-03-26 Impedir el ingreso de notificaciones del simert duplicadas
 */

@Name("simertNotificationNumberValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class SimertNotificationNumberValidator implements javax.faces.validator.Validator, Serializable {

	private static final long serialVersionUID = 1L;
	private String message;

	public SimertNotificationNumberValidator() {

	}

	@Override
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String notificationNumber = value.toString().trim();
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><< " + notificationNumber);
		MunicipalBondHome mb = (MunicipalBondHome) Component.getInstance("municipalBondHome");
		if (mb.findSimertfine(notificationNumber)) {
			//System.out.println("ya existe>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			message = Interpolator.instance().interpolate("Número infracción ya existente", new Object[0]);
			throw new ValidatorException(new FacesMessage(message));
		}
		/*else {
			System.out.println("...................... no hay nada si deja ingresar");
		}*/

		/*
		 * ResidentHome residentHome = (ResidentHome)
		 * Component.getInstance("residentHome"); Resident resident =
		 * residentHome.getInstance(); pattern = Pattern.compile(PLATE_PATTERN); matcher
		 * = pattern.matcher(value.toString()); if(!matcher.matches()){ FacesMessage msg
		 * = new FacesMessage("Número de placa Incorrecto",
		 * "Formato de placa inválido."); msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		 * throw new ValidatorException(msg);
		 * 
		 * }
		 */

	}
}
