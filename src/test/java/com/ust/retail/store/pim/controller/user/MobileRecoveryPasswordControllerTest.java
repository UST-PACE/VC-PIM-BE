package com.ust.retail.store.pim.controller.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.ust.retail.store.pim.dto.security.RecoverPasswordDTO;
import com.ust.retail.store.pim.service.recoverpassword.MobileRecoverPasswordService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(MockitoExtension.class)
class MobileRecoveryPasswordControllerTest {
	@Mock
	private MobileRecoverPasswordService mockMobileRecoverPasswordService;

	@InjectMocks
	private MobileRecoveryPasswordController controller;

	@Test
	void recoverPasswordReturnsExpected() {
		RecoverPasswordDTO request = new RecoverPasswordDTO();

		ResponseEntity<Void> result = controller.recoverPassword(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void changePasswordReturnsExpected() {
		RecoverPasswordDTO request = new RecoverPasswordDTO();

		ResponseEntity<Void> result = controller.changePassword(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updatePasswordReturnsExpected() {
		RecoverPasswordDTO request = new RecoverPasswordDTO();

		ResponseEntity<Void> result = controller.updatePassword(request);

		assertThat(result, is(notNullValue()));
	}
}
