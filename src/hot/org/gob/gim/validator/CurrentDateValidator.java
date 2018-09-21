package org.gob.gim.validator;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import ec.gob.gim.common.model.Resident;


@Name("currentDateValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class CurrentDateValidator implements
javax.faces.validator.Validator, Serializable{

	private static final long serialVersionUID = 1L;

	private String message;
	
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (value != null) {	
			Date today = new Date();
			SimpleDateFormat formatter  = new SimpleDateFormat("dd/MM/yyyy");
			Date selectDate = formatter.parse(formatter.format(value),new ParsePosition(0));
			Date currentDate = formatter.parse(formatter.format(today),new ParsePosition(0));
			if (selectDate.after(currentDate)){	
				message = Interpolator.instance().interpolate(
						"La fecha sobrepasa a la actual",
						new Object[0]);
				throw new ValidatorException(new FacesMessage(message));
			}
				
		} else {
			message = Interpolator.instance().interpolate(
					"Se requiere una fecha",
					new Object[0]);
			throw new ValidatorException(new FacesMessage(message));
		}

	}
}
