package com.ust.retail.store.common.util.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.ust.retail.store.common.util.validation.validators.StrongPasswordValidator;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
	String message() default "Weak Password";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

	boolean oneLowerCase() default true;
	boolean oneUpperCase() default true;
	boolean oneDigit() default true;
	boolean oneSpecialCharacter() default true;

	String[] characterList() default {"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "-", "=", "[", "]", "{", "}", ";", "'", ":", "\"", "\\", "|", ",", ".", "<", ">", "/", "?"};
}
