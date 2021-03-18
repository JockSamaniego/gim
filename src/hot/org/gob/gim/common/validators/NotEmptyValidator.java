package org.gob.gim.common.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {

	@Override
	public void initialize(NotEmpty arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		if(arg0 == null){
			return false;
		}
		if (!arg0.isEmpty()) {
			return true;
		}
		return false;
	}

}
