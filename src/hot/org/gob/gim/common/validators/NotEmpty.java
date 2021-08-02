package org.gob.gim.common.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = { NotEmptyValidator.class })
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME )
public @interface NotEmpty {
	String message() default "Valor no puede ser vacío";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
