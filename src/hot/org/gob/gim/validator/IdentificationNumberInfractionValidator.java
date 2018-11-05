package org.gob.gim.validator;

/**
 * @author wilman
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.gob.gim.common.action.ResidentHome;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Interpolator;
import org.jboss.seam.core.ResourceBundle;
import org.jboss.seam.framework.EntityHome;

import ec.gob.gim.ant.ucot.model.Infractions;
import ec.gob.gim.common.model.Resident;

@Name("identificationNumberInfractionValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class IdentificationNumberInfractionValidator extends EntityHome<Infractions> implements
		javax.faces.validator.Validator, Serializable  {

	private static final long serialVersionUID = 1L;
	
	private String message;
	

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException  {
		if (value != null) {
			String identificationNumber = value.toString().trim();
			List<Resident> residents = new ArrayList<Resident>();
			Query query = getEntityManager().createNamedQuery(
					"infractions.findResidentByIdent");
			query.setParameter("identNum", identificationNumber);
			residents = query.getResultList();
			if(residents.size()>0){
				ResidentHome rtHome = (ResidentHome) Contexts.getConversationContext().get(ResidentHome.class);
				rtHome.setInstance(residents.get(0));
			}
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
