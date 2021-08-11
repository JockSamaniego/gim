/**
 * 
 */
package org.gob.gim.common.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author René
 *
 */
public class InListValidator implements ConstraintValidator<InList, String> {
	String[] listPermitValues;

	@Override
	public void initialize(InList arg0) {
		// TODO Auto-generated method stub
		listPermitValues = arg0.values();

	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		if (arg0 == null) {
			return false;
		}
		if (arg0.isEmpty()) {
			return false;
		}
		
		for (int i = 0; i < listPermitValues.length; i++) {
			String permitValue = listPermitValues[i];
			if(permitValue.equals(arg0)){
				return true;
			}
			
		}
		return false;
	}
}
