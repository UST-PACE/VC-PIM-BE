package com.ust.retail.store.common.util.validation.validators;

import java.util.List;
import java.util.stream.Stream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.ust.retail.store.common.util.validation.constraints.StrongPassword;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

	private boolean lowerCase;
	private boolean upperCase;
	private boolean digit;
	private boolean specialCharacters;
	private List<String> characterList;

	@Override
	public void initialize(StrongPassword constraintAnnotation) {
		lowerCase = constraintAnnotation.oneLowerCase();
		upperCase = constraintAnnotation.oneUpperCase();
		digit = constraintAnnotation.oneDigit();
		specialCharacters = constraintAnnotation.oneSpecialCharacter();
		characterList = List.of(constraintAnnotation.characterList());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value)) return false;

		boolean hasLowerCase = lowerCase && value.matches(".*[a-z].*");
		boolean hasUpperCase = upperCase && value.matches(".*[A-Z].*");
		boolean hasDigit = digit && value.matches(".*[0-9].*");
		boolean hasSpecialChar = specialCharacters && Stream.of(value.split("")).anyMatch(characterList::contains);

		return hasLowerCase && hasUpperCase && hasDigit && hasSpecialChar;
	}
}
