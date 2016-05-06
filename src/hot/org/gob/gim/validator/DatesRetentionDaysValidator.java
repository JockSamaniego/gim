package org.gob.gim.validator;

/**
 * @author Manuel Cartuche
 * @date 2015-06-02
 */

import java.io.Serializable;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.gob.gim.electronicvoucher.action.ElectronicVoucherHome;
import org.jboss.seam.Component;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.intercept.BypassInterceptors;
import org.jboss.seam.core.Interpolator;

import ec.gob.gim.complementvoucher.model.ElectronicVoucher;

@Name("datesRetentionDaysValidator")
@BypassInterceptors
@org.jboss.seam.annotations.faces.Validator
public class DatesRetentionDaysValidator implements
		javax.faces.validator.Validator, Serializable {

	private static final long serialVersionUID = 1L;

	private String message;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		if (value != null) {
			ElectronicVoucherHome voucherHome = (ElectronicVoucherHome) Component
					.getInstance("electronicVoucherHome");
			ElectronicVoucher voucher = voucherHome.getInstance();

			Date documentDate = (Date) value;
			Date emissionDate = voucher.getEmissionDate();
			//validations
			
			Date now = new Date();
			
			Boolean error=false;
			if(emissionDate.after(now)){
				error=true;
			}
			
			if(documentDate.after(now)){
				error=true;
			}
			
			if(documentDate.after(emissionDate)){
				error=true;
			}
			
			long diff = emissionDate.getTime() - documentDate.getTime();
			double days = diff / (1000 * 60 * 60 * 24);

			if (days > 1) {
				error=true; 
			}
			
			if(error){
				message = Interpolator
						.instance()
						.interpolate("#{messages['electronicVoucher.docDateError']}",
								new Object[0]);
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
