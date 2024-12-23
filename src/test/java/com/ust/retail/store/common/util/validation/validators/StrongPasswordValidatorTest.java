package com.ust.retail.store.common.util.validation.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.common.util.validation.constraints.StrongPassword;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StrongPasswordValidatorTest {


	@Mock
	private StrongPassword annotation = mock(StrongPassword.class);
	private StrongPasswordValidator validator;

	@BeforeEach
	void setUp() {
		validator = new StrongPasswordValidator();

		when(annotation.oneLowerCase()).thenReturn(false);
		when(annotation.oneUpperCase()).thenReturn(false);
		when(annotation.oneDigit()).thenReturn(false);
		when(annotation.oneSpecialCharacter()).thenReturn(false);
		when(annotation.characterList()).thenReturn(new String[]{});
	}

	@Test
	void isValidReturnsTrueWhenAllConditionsApplied() {
		when(annotation.oneLowerCase()).thenReturn(true);
		when(annotation.oneUpperCase()).thenReturn(true);
		when(annotation.oneDigit()).thenReturn(true);
		when(annotation.oneSpecialCharacter()).thenReturn(true);
		when(annotation.characterList()).thenReturn(new String[]{"!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "-", "=", "[", "]", "{", "}", ";", "'", ":", "\"", "\\", "|", ",", ".", "<", ">", "/", "?"});

		validator.initialize(annotation);

		assertThat(validator.isValid("MyPassw0rd!", null), is(true));
	}

	@Test
	void isValidReturnsFalseWhenOneLowerCaseDoesNotMatch() {
		when(annotation.oneLowerCase()).thenReturn(true);
		validator.initialize(annotation);

		assertThat(validator.isValid("UPPER", null), is(false));
	}

	@Test
	void isValidReturnsFalseWhenOneUpperCaseDoesNotMatch() {
		when(annotation.oneUpperCase()).thenReturn(true);
		validator.initialize(annotation);

		assertThat(validator.isValid("lower", null), is(false));
	}

	@Test
	void isValidReturnsFalseWhenOneDigitDoesNotMatch() {
		when(annotation.oneDigit()).thenReturn(true);
		validator.initialize(annotation);

		assertThat(validator.isValid("nodigits", null), is(false));
	}

	@Test
	void isValidReturnsFalseWhenOneSpecialCharacterDoesNotMatch() {
		when(annotation.oneSpecialCharacter()).thenReturn(true);
		validator.initialize(annotation);

		assertThat(validator.isValid("nospecial", null), is(false));
	}

	@Test
	void isValidReturnsFalseWhenEmptyString() {
		validator.initialize(annotation);

		assertThat(validator.isValid("", null), is(false));
	}

	@Test
	void isValidReturnsFalseWhenNullString() {
		validator.initialize(annotation);

		assertThat(validator.isValid(null, null), is(false));
	}
}
