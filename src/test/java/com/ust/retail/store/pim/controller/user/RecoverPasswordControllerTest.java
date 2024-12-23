package com.ust.retail.store.pim.controller.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.ust.retail.store.pim.dto.security.RecoverPasswordDTO;
import com.ust.retail.store.pim.service.recoverpassword.RecoverPasswordService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(MockitoExtension.class)
class RecoverPasswordControllerTest {
	@Mock
	private RecoverPasswordService mockRecoverPasswordService;

	@InjectMocks
	private RecoverPasswordController controller;

	@Test
	void sendEmailRecoverPasswordReturnsExpected() {
		RecoverPasswordDTO request = new RecoverPasswordDTO();

		ResponseEntity<Void> result = controller.sendEmailRecoverPassword(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updatePasswordReturnsExpected() {
		RecoverPasswordDTO request = new RecoverPasswordDTO();

		ResponseEntity<Void> result = controller.updatePassword(request);

		assertThat(result, is(notNullValue()));
	}
}
