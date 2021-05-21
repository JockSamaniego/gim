/**
 * 
 */
package org.gob.gim.common.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author René
 *
 */
@Constraint(validatedBy = { InListValidator.class })
@Target(value = { ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
public @interface InList {
	String[] values() default { "" };

	String message() default "Codigo de cuenta no autorizado";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
