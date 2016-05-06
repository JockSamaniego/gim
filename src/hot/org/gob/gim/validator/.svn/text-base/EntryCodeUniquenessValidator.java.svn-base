package org.gob.gim.validator;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;

import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Interpolator;

import ec.gob.gim.common.model.Resident;

@Name("entryCodeUniquenessValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class EntryCodeUniquenessValidator implements
		javax.faces.validator.Validator, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		// TODO Auto-generated method stub
		if (value != null) {
			String code = value.toString().trim();
			verifyUniqueness(code);
		}

	}
	
	private void verifyUniqueness(String code) {
		EntityManager entityManager = (EntityManager) Component
				.getInstance("entityManager");
		@SuppressWarnings("unchecked")
		java.util.List<Resident> result = (java.util.List<Resident>) entityManager
				.createNamedQuery("Entry.findByCode").setParameter("code", code).getResultList();

		if (result != null && !result.isEmpty()) {
			message = Interpolator.instance().interpolate(
					"#{messages['entry.alreadyExistCode']}",
					new Object[0]);
			throw new ValidatorException(new FacesMessage(message));
		}
	}

}
